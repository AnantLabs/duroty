window.onload = initialize;
var Mail = new Mail();

/** Our function that initializes when the page is finished loading. */
function initialize() {
	parent.document.body.rows = "100%,*,*";

	initializeAIM();

	// initialize the DHTML History framework
    dhtmlHistory.initialize();	    
   
	// add ourselves as a DHTML History listener
    dhtmlHistory.addListener(handleHistoryChange);	
	
	// determine what our initial location is by retrieving it from the browser's location after the hash
    var currentLocation = dhtmlHistory.getCurrentLocation();
		
	// if there is no location then display
	// the default, which is the inbox
    if (currentLocation === "") {
        currentLocation = "section:INBOX:0*0!0";
    }
	
	// extract the section to display from the initial location 
    currentLocation = currentLocation.replace(/section\:/, "");
	
	// display this initial location
    Mail.displayLocation(currentLocation, null);
}
/** Handles history change events. */
function handleHistoryChange(newLocation, historyData) {	
	// if there is no location then display the default, which is the INBOX
    if (newLocation === "") {
        newLocation = "section:INBOX:0*0!0";
    }
    if (historyData === null) {
        historyData = historyStorage.get(newLocation);
    }
	
	// extract the section to display from the location change; newLocation will begin with the word "section:" 
    newLocation = newLocation.replace(/section\:/, "");
	
	// update the browser to respond to this DHTML history change
    Mail.displayLocation(newLocation, historyData);
}

function Mail() {
	this.FOLDER_INBOX = "INBOX";
	this.FOLDER_SENT = "SENT";
	this.FOLDER_SPAM = "SPAM";
	this.FOLDER_HIDDEN = "HIDDEN";
	this.FOLDER_TRASH = "TRASH";
	this.FOLDER_ALL = "ALL";
	this.FOLDER_ARCHIVED = "ARCHIVED";
	this.FOLDER_DRAFT = "DRAFT";
	this.ACTION = "";
	this.NAME = 0;
	this.UID = 0;
	this.EXTRA = 0;
	this.selectedAll = true;
	this.locationHash = false;
	
	this.hash = new Hash(null);
	
	this.countAtatch = 0;	
	
	this.composeTimer = false;
	this.replyTimer = false;

    /** Displays the given location in the right-hand side content area. 
     *  el format de una newLocation ve donar per //action:name*uid!extra
      */
    this.displayLocation = function (newLocation, sectionData) {    
    	hideInfo();
    
    	setTimeout("window.frames['session'].REFRESH.selectSession(false)", 500);
    	
    	window.frames['session'].REFRESH.date = new Date();
    
	    idx = newLocation.indexOf(':');
		
		this.ACTION = newLocation;
		this.NAME = 0;
		this.UID = 0;		
		this.EXTRA = 0;
		
		this.locationHash = window.location.hash;
		
		if (idx > -1) {		
			//en el cas del search tenim que el folder es un queryString
			aux1 = newLocation.indexOf('*');
			aux2 = newLocation.indexOf('!');
			this.EXTRA = newLocation.substring(aux2 + 1, newLocation.length);	
			this.UID = newLocation.substring(aux1 + 1, aux2);	
			this.NAME = newLocation.substring(idx + 1, aux1);
			this.ACTION = newLocation.substring(0, idx);
		}	
		
		if (this.composeTimer) {
			clearTimeout(this.composeTimer);
		}
		
		if (this.replyTimer) {
			clearTimeout(this.replyTimer);
		}
		
		if (this.ACTION == "simpleSearch") {		
			var token = document.getElementById("token").value;
			if (!token) {
				alert("The text is required");
				return;
			}
			this.simpleSearch(token, this.NAME, this.UID, this.EXTRA);
		} else if (this.ACTION == "advancedSearch") {		
			this.advancedSearch(this.ACTION, this.NAME, this.UID, this.EXTRA);
		} else if (this.ACTION == "testFilter") {		
			this.testFilter(this.ACTION, this.NAME, this.UID, this.EXTRA);
		} else if (startsWith(this.ACTION, "label")) {		
			this.labelMessages(this.ACTION, this.NAME, this.UID, this.EXTRA);
		} else if (this.ACTION == "compose") {	
			this.removeTinyMce("taBody");
			this.removeTinyMce("taReplyBody");
		
			this.hash = new Hash(null);		    	
			
			this.compose();	
		} else if (this.ACTION == "read") {			
			this.hash = new Hash(null);		
						
			this.readMessage(this.NAME, this.UID);			
		} else if (this.ACTION == "reply") {
		} else if (this.ACTION == "replyToAll") {
		} else if (this.ACTION == "forward") {
		} else {
			//El cas per defecte indica que volem llistar els missatges
			this.listMessages(this.ACTION, this.NAME, this.UID, this.EXTRA);					
		}
		
		dhtmlHistory.add("section:" + this.ACTION + ":" + this.NAME + "*" + this.UID + "!" + this.EXTRA, null);
		
		window.scroll(0,0);
		
		window.focus();
    };
    
    this.simpleSearch = function(folder, page, order, desc) {
    	var toHide = new Array();
    	toHide[0] = document.getElementById("message");
    	toHide[1] = document.getElementById("formCompose");
    
    	toShow = document.getElementById("messages");
		
		var divExcludeTrash = document.getElementById("excludeTrash");
		var excludeTrash = false;
		if (divExcludeTrash.checked) {
			excludeTrash = true;
		} else {
			excludeTrash = false;
		}
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("folder", folder);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	ajaxContents.setVar("excludeTrash", excludeTrash);    	
    	ajaxContents.selectContents("mail/simpleSearch.drt", false, "messages", toShow, toHide);
    	this.parseLeft(null, 0);
    };
    
    this.advancedSearch = function(folder, page, order, desc) {
    	var toHide = new Array();
    	toHide[0] = document.getElementById("message");
    	toHide[1] = document.getElementById("formCompose");
    
    	toShow = document.getElementById("messages");
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("folder", folder);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	
    	var url = this.parseAdvancedSearch(ajaxContents);
    	ajaxContents.selectContents("mail/advancedSearch.drt?" + url + "&folder=" + folder + "&page=" + page + "&order=" + order + "&extra=" + desc, false, "messages", toShow, toHide);
    	this.parseLeft(null, 0);
    };
    
    this.testFilter = function(folder, page, order, desc) {
    	var toHide = new Array();
    	toHide[0] = document.getElementById("message");
    	toHide[1] = document.getElementById("formCompose");
    
    	toShow = document.getElementById("messages");
		
		var divExcludeTrash = document.getElementById("excludeTrash");
		var excludeTrash = false;
		if (divExcludeTrash.checked) {
			excludeTrash = true;
		} else {
			excludeTrash = false;
		}
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("folder", folder);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	
    	var url = this.parseTestFilter(ajaxContents);
    	ajaxContents.selectContents("mail/testFilter.drt?" + url + "&folder=" + folder + "&page=" + page + "&order=" + order + "&extra=" + desc, false, "messages", toShow, toHide);
    	this.parseLeft(null, 0);
    };
    
    this.listMessages = function(folder, page, order, desc) {    	
    	var toHide = new Array();
    	toHide[0] = document.getElementById("message");
    	toHide[1] = document.getElementById("formCompose");
    
    	toShow = document.getElementById("messages");
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("folder", folder);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	ajaxContents.selectContents("mail/messages.drt", false, "messages", toShow, toHide);
    	this.parseLeft(folder, 0);
    };
    
    this.labelMessages = function(label, page, order, desc) {
    	var toHide = new Array();
    	toHide[0] = document.getElementById("message");
    	toHide[1] = document.getElementById("formCompose");
    
    	toShow = document.getElementById("messages");
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("folder", label);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	ajaxContents.selectContents("mail/messages.drt", false, "messages", toShow, toHide);
    	this.parseLeft(label, 0);
    };
    
    this.readMessage = function(folder, mid) {
    	var toHide = new Array();
    	toHide[0] = document.getElementById("messages");
    	toHide[1] = document.getElementById("formCompose");
    
    	toShow = document.getElementById("message");
		
		ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.selectContents("mail/readMessage.drt?mid=" + mid + "&folder=" + folder, false, "message", toShow, toHide);
    	
    	if (folder == this.FOLDER_HIDDEN) {
    		this.parseLeft(this.FOLDER_ALL, 0);
    	} else {
    		this.parseLeft(folder, 0);
    	}
    };
    
    this.compose = function(to) {    
    	var message = document.getElementById("message");
		if (message) {
			message.className = "hide";
			message.innerHTML = "";
		}
    
	    var messages = document.getElementById("messages");
		if (messages) {
			messages.className = "hide";
			messages.innerHTML = "";
		}
		
		var formCompose = document.getElementById("formCompose");
		if (formCompose) {
			formCompose.className = "show";			
		}			
		
		if (!to) {
			this.initFormCompose("");
		} else {
			this.initFormCompose(to);
		}
		
    	this.parseLeft(null, 0);
    };
    
    this.initFormCompose = function(to) {    
    	var controlCookie = false;
    	var duroty_compose = this.getCookie('duroty_compose');
    	if (duroty_compose && duroty_compose != 'null') {
    		var ddc = document.getElementById("durotyCookie");
    		if (ddc) {
	    		if (confirm(ddc.innerHTML)) {
	    			document.formCompose.to.value = this.getCookie('duroty_compose_to');
					document.formCompose.cc.value = this.getCookie('duroty_compose_cc');
					document.formCompose.bcc.value = this.getCookie('duroty_compose_bcc');
					document.formCompose.subject.value = this.getCookie('duroty_compose_subject');
					document.formCompose.taBody.value = this.getCookie('duroty_compose_body');
					controlCookie = true;
    			} else {
    				this.initCookieCompose();
    			}
			}
    	}
    
    	this.countAtatch = 0;

		this.removeAllAttach("attachFile");
	
		if (!controlCookie) {
			document.formCompose.to.value = "";
		
			if (to) {
				document.formCompose.to.value = to;
			} 
			
			document.formCompose.cc.value = "";
			document.formCompose.bcc.value = "";
			document.formCompose.subject.value = "";					
			document.formCompose.taBody.value = signature;
		}
		
		if (htmlMessage) {
			this.addTinyMce("taBody");
		} else {			
		}
		
		document.formCompose.to.focus();
		
		this.composeTimer = setInterval('Mail.saveComposeMessage()', 5000);
		
    };
    
    this.displayImages = function(_folder, _mid) {
    	var toHide = new Array();
    	toHide[0] = document.getElementById("messages");
    	toHide[1] = document.getElementById("formCompose");
    
    	toShow = document.getElementById("message");
		
		ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.selectContents("mail/readMessage.drt?mid=" + _mid + "&folder=" + _folder + "&displayImages=true", false, "message", toShow, toHide);
    	
    	if (_folder == this.FOLDER_HIDDEN) {
    		this.parseLeft(this.FOLDER_ALL, 0);
    	} else {
    		this.parseLeft(_folder, 0);
    	}
    }
    
    this.reply = function() {    	
    	var tdReply = document.getElementById("tdReply");
    	  	
    	if (tdReply) {
    		if (tdReply.className === "show") {
    			tdReply.className = "hide";
    			return;
    		}
    		tdReply.className = "show";
    	}
    	
    	var to = "";
    	var subject = "";
    	var body = "";
    	
    	var aux = document.getElementById("msgReplyTo");
    	if (aux) {
    		to = decodeEntities(rtrim(ltrim(aux.innerHTML)));
    	}
    	
    	aux = document.getElementById("msgSubject");
    	if (aux) {
    		subject = rtrim(ltrim(aux.innerHTML));
    	}
    	
    	aux = document.getElementById("msgBody");
    	if (aux) {
    		body = aux.innerHTML;
    	}
    	
    	var replyBody = "";
		if (htmlMessage) {
			if (body) {
				replyBody = '<br/><br/><br/><div id="merda"></div><div id="rep" style="padding-left: 10px;"><div id="repRep" style="padding-left: 10px; border-left: 2px solid red;">' + body + '</div></div>';
			}
		} else {
			if (body) {
				replyBody = replaceString(body, "<br/>", "\n> ");
			}
		}
    	
    	this.initFormReply(to, false, false, "RE: " + subject, replyBody);
    };
    
    this.replyToAll = function() {    	
    	var tdReply = document.getElementById("tdReply");
    	  	
    	if (tdReply) {
    		if (tdReply.className === "show") {
    			tdReply.className = "hide";
    			return;
    		}
    		tdReply.className = "show";
    	}
    	
    	var to = "";
    	var cc = "";
    	var subject = "";
    	var body = "";
    	
    	var aux = document.getElementById("msgReplyTo");
    	if (aux) {
    		to = decodeEntities(rtrim(ltrim(aux.innerHTML)));
    	}
    	
    	aux = document.getElementById("msgTo");
    	if (aux) {
    		cc += decodeEntities(rtrim(ltrim(aux.innerHTML))) + ", ";
    	}
    	
    	aux = document.getElementById("msgCc");
    	if (aux) {
    		cc += decodeEntities(rtrim(ltrim(aux.innerHTML)));
    	}
    	
    	aux = document.getElementById("msgSubject");
    	if (aux) {
    		subject = rtrim(ltrim(aux.innerHTML));
    	}
    	
    	aux = document.getElementById("msgBody");
    	if (aux) {
    		body = aux.innerHTML;
    	}
    	
    	var replyBody = "";
		if (htmlMessage) {
			if (body) {
				replyBody = '<br/><br/><br/><div id="rep" style="padding-left: 10px;"><div id="repRep" style="padding-left: 10px; border-left: 2px solid red;">' + body + '</div></div>';
			}
		} else {
			if (body) {
				replyBody = replaceString(body, "<br/>", "\n> ");
			}
		}    	
    	
    	this.initFormReply(to, cc, false, "RE: " + subject, replyBody);
    };
    
    this.forward = function() {
    	var tdReply = document.getElementById("tdReply");
    	  	
    	if (tdReply) {
    		if (tdReply.className === "show") {
    			tdReply.className = "hide";
    			return;
    		}
    		tdReply.className = "show";
    	}
    	
    	var from = "";
    	var to = "";
    	var cc = "";
    	var subject = "";
    	var body = "";
    	var date = "";
    	
    	var aux = document.getElementById("msgFrom");
    	if (aux) {
    		from = rtrim(ltrim(aux.innerHTML));
    	}
    	
    	aux = document.getElementById("msgReplyTo");
    	if (aux) {
    		to = rtrim(ltrim(aux.innerHTML));
    	}
    	
    	aux = document.getElementById("msgTo");
    	if (aux) {
    		cc += rtrim(ltrim(aux.innerHTML)) + ", ";
    	}
    	
    	aux = document.getElementById("msgCc");
    	if (aux) {
    		cc += rtrim(ltrim(aux.innerHTML));
    	}
    	
    	aux = document.getElementById("msgSubject");
    	if (aux) {
    		subject = rtrim(ltrim(aux.innerHTML));
    	}
    	
    	aux = document.getElementById("msgBody");
    	if (aux) {
    		body = aux.innerHTML;
    	}
    	
    	aux = document.getElementById("msgDate");
    	if (aux) {
    		date = rtrim(ltrim(aux.innerHTML));
    	}
    	
    	var forwardBody = "";
    	if (htmlMessage) {
			forwardBody = '<br/><br/>---------- Forwarded message ----------<br/>From: ' + from + '<br/>To: ' + to + '<br/>Cc: ' + cc + '<br/>Date: ' + date + '<br/><br/><br/><div id="rep" style="padding-left: 10px;"><div id="repRep" style="padding-left: 10px; border-left: 2px solid red;">' + body + '</div></div>';		
		} else {
			forwardBody = "\n\n---------- Forwarded message ----------\nFrom: " + from + "\nTo: " + to + "\nCc: " + cc + "\nDate:" + date + "\n\n\n" + replaceString(body, "<br/>", "\n> ");
		}
    	
    	this.initFormReply(false, false, false, "[Fw: " + subject + "]", forwardBody);
    	
    	this.initForwardAttachments();
    };
    
    this.draft = function() {
    	var tdReply = document.getElementById("tdReply");
    	  	
    	if (tdReply) {
    		if (tdReply.className === "show") {
    			tdReply.className = "hide";
    			return;
    		}
    		tdReply.className = "show";
    	}
    	
    	var to = "";
    	var cc = "";
    	var bcc = "";
    	var subject = "";
    	var body = "";
    	
    	var aux = document.getElementById("msgTo");
    	if (aux) {
    		to = decodeEntities(rtrim(ltrim(aux.innerHTML)));
    	}
    	
    	aux = document.getElementById("msgCc");
    	if (aux) {
    		cc = decodeEntities(rtrim(ltrim(aux.innerHTML)));
    	}
    	
    	aux = document.getElementById("msgBcc");
    	if (aux) {
    		bcc = decodeEntities(rtrim(ltrim(aux.innerHTML)));
    	}
    	
    	aux = document.getElementById("msgSubject");
    	if (aux) {
    		subject = rtrim(ltrim(aux.innerHTML));
    	}
    	
    	aux = document.getElementById("msgBody");
    	if (aux) {
    		body = aux.innerHTML;
    	}        		
    	
    	this.initFormReply(to, cc, bcc, subject, body);
    };
    
    this.initFormReply = function(to, cc, bcc, subject, body) {
    	this.removeTinyMce("taReplyBody");
    
    	var controlCookie = false;
    	var duroty_reply = this.getCookie('duroty_reply');
    	if (duroty_reply && duroty_reply != 'null') {
    		var ddc = document.getElementById("durotyCookie");
    		if (ddc) {
	    		if (confirm(ddc.innerHTML)) {
	    			document.formReply.to.value = this.getCookie('duroty_reply_to');
					document.formReply.cc.value = this.getCookie('duroty_reply_cc');
					document.formReply.bcc.value = this.getCookie('duroty_reply_bcc');
					document.formReply.subject.value = this.getCookie('duroty_reply_subject');
					document.formReply.taReplyBody.value = this.getCookie('duroty_reply_body');
					controlCookie = true;
    			} else {
    				this.initCookieReply();
    			}
			}
    	}
    
    	this.countAtatch = 0;

		this.removeAllAttach("attachReplyFile");
		
		if (!controlCookie) {	
			document.formReply.to.value = "";
		
			if (to) {
				document.formReply.to.value = to;
			} 
			
			document.formReply.cc.value = "";
			if (cc) {
				document.formReply.cc.value = cc;
			}
			
			document.formReply.bcc.value = "";
			if (bcc) {
				document.formReply.bcc.value = bcc;
			}
			
			if (subject) {
				document.formReply.subject.value = subject;
			} else {
				document.formReply.subject.value = "";
			}							
			
			document.formReply.taReplyBody.value = signature + body;	
		}
		
		if (htmlMessage) {
			this.addTinyMce("taReplyBody");
			//tinyMCE.setContent(signature + body);
			tinyMCE.execCommand("mceFocus", true, "taReplyBody");
		} else {
			
		}
		
		this.replyTimer = setInterval('Mail.saveReplyMessage()', 5000);
    };
    
    this.saveComposeMessage = function() {  		
		if (document.formCompose) {
			var body = "";  	
	    	if (htmlMessage) {
				body = tinyMCE.getContent();
			} else {
				body = document.formCompose.taBody.value;
			}
		
			//name,value,expires,path,domain,secure
			Mail.setCookie('duroty_compose', true, 1);
			Mail.setCookie('duroty_compose_to', document.formCompose.to.value, 1);
			Mail.setCookie('duroty_compose_cc', document.formCompose.cc.value, 1);
			Mail.setCookie('duroty_compose_bcc', document.formCompose.bcc.value, 1);
			Mail.setCookie('duroty_compose_subject', document.formCompose.subject.value, 1);
			Mail.setCookie('duroty_compose_body', body, 1);
		}
    };
    
    this.initCookieCompose = function() {
		//name,value,expires,path,domain,secure
		Mail.setCookie('duroty_compose', null, 0);
		Mail.setCookie('duroty_compose_to', null, 0);
		Mail.setCookie('duroty_compose_cc', null, 0);
		Mail.setCookie('duroty_compose_bcc', null, 0);
		Mail.setCookie('duroty_compose_subject', null, 0);
		Mail.setCookie('duroty_compose_body', null, 0);
    };
    
    this.saveReplyMessage = function() {		
		if (document.formReply) {
			var body = "";  	
	    	if (htmlMessage) {
				body = tinyMCE.getContent();
			} else {
				body = document.formReply.taReplyBody.value;
			}
		
			//name,value,expires,path,domain,secure
			Mail.setCookie('duroty_reply', true, 1);
			Mail.setCookie('duroty_reply_to', document.formReply.to.value, 1);
			Mail.setCookie('duroty_reply_cc', document.formReply.cc.value, 1);
			Mail.setCookie('duroty_reply_bcc', document.formReply.bcc.value, 1);
			Mail.setCookie('duroty_reply_subject', document.formReply.subject.value, 1);
			Mail.setCookie('duroty_reply_body', body, 1);
		}
    };    
    
    this.initCookieReply = function() {		
		//name,value,expires,path,domain,secure
		Mail.setCookie('duroty_reply', null, 0);
		Mail.setCookie('duroty_reply_to', null, 0);
		Mail.setCookie('duroty_reply_cc', null, 0);
		Mail.setCookie('duroty_reply_bcc', null, 0);
		Mail.setCookie('duroty_reply_subject', null, 0);
		Mail.setCookie('duroty_reply_body', null, 0);
    };    
    
    this.getMessage = function(_form) {
    };
    
    this.getCookie = function(name) { 
    	var start = document.cookie.indexOf(name+"="); 
		var len = start+name.length+1; 
		if ((!start) && (name != document.cookie.substring(0,name.length))) return null; 
		if (start == -1) return null; 
		var end = document.cookie.indexOf(";",len); 
		if (end == -1) end = document.cookie.length; 
		return unescape(document.cookie.substring(len,end)); 
	};
	
	// This function has been slightly modified
	this.setCookie = function(name,value,expires,path,domain,secure) {
		expires = expires * 60*60*24*1000;
		var today = new Date();
		var expires_date = new Date( today.getTime() + (expires) );
		var cookieString = name + "=" +escape(value) + 
	       ( (expires) ? ";expires=" + expires_date.toGMTString() : "") + 
	       ( (path) ? ";path=" + path : "") + 
	       ( (domain) ? ";domain=" + domain : "") + 
	       ( (secure) ? ";secure" : ""); 
	    document.cookie = cookieString;
	};
    
    this.initForwardAttachments = function() {
    	var aux = document.getElementById("auxAttachments");
    	var forwardAttachments = document.getElementById("forwardAttachments");
    	var control = false;
    	if (forwardAttachments && aux) {
    		forwardAttachments.innerHTML = "";
			for (var i = 0; i < aux.childNodes.length; i++) {
				var currentElement = aux.childNodes[i];
				// see if this is a DOM Element node
				if (currentElement.nodeType == 1) {
					var index = currentElement.id.indexOf('fa');
					var part = currentElement.id.substring(index + 2, currentElement.id.length);					
					forwardAttachments.innerHTML = forwardAttachments.innerHTML + "<input type='checkbox' name='forwardAttachments' checked='true' value='" + currentElement.title + ":" + part + "'/>&nbsp;&nbsp;" + currentElement.innerHTML + "<br/>"; 
				}			
				control = true;										
			} 	
			if (control) {
				forwardAttachments.className = "show";
			}
		}    	
    };
    
    this.attachFile = function(obj) {
		//var aux = document.getElementById("attachFile");	
		var aux = document.getElementById(obj);	
		if (aux != null) {
			var newEl = document.createElement('div');
			var i = this.countAttach++;
			newEl.id = "divAttach" + i;
			newEl.innerHTML = newEl.innerHTML + "<input type='file' name='attachments' size='40'/>&nbsp;&nbsp;<img src='images/cancel.gif' border='0' style='cursor: pointer;' onclick='javascript:Mail.removeAttach(\"" + newEl.id + "\", \"" + obj + "\");' />";
			
			
			aux.appendChild(newEl);
		}
	}
	
	this.removeAttach = function(id, obj) {
		//var aux = document.getElementById("attachFile");
		var aux = document.getElementById(obj);
		if (aux != null) {
			var child = document.getElementById(id);
			if (child != null) {
				aux.removeChild(child);
			}
		}
	}
	
    
    this.removeAllAttach = function(obj) {
		//var aux = document.getElementById("attachFile");
		var aux = document.getElementById(obj);
		if (aux != null) {
			for (var i = 0; i < aux.childNodes.length; i++) {
				aux.removeChild(aux.childNodes[0]);
			}
			aux.innerHTML = "";
		}
	}
    
    this.parseLeft = function(action, idint) {
    	// clear out the old selected menu item
		var menu = document.getElementById("folders");
		for (var i = 0; i < menu.childNodes.length; i++) {
			var currentElement = menu.childNodes[i];
			// see if this is a DOM Element node
			if (currentElement.nodeType == 1) {
				// clear any class name
				currentElement.className = "folder";
			}													
		} 		
	
		// clear out the old selected menu item
		menu = document.getElementById("dlabel");
		if (menu != null) {
			for (var i = 0; i < menu.childNodes.length; i++) {
				var currentElement = menu.childNodes[i];
				// see if this is a DOM Element node
				if (currentElement.nodeType == 1) {
					// clear any class name
					currentElement.className = "";
				}													
			} 
		}
		
		if (action == null) {
			return;
		}
		
		if (!startsWith(action, "label")) {
			// get the menu element that was selected
			var selectedElement = document.getElementById("folder" + action);
			if (selectedElement != null) {		
				// cause the new selected menu item to appear differently in the UI
				if (action == this.FOLDER_SPAM) {
		    		selectedElement.className = "folderSelectedspam";
		    	} else if (action == this.FOLDER_TRASH) {
		    		selectedElement.className = "folderSelectedspam";
		    	} else {
		    		selectedElement.className = "folderSelected";
		    	}
			}
		} else {
			idint = action.substring(5, action.length);
			var selectedElement = document.getElementById('lab' + idint);
			if (selectedElement != null) {		
				// cause the new selected menu item to appear differently in the UI
				selectedElement.className = "labelSelected";	
			}	
		}
    };
    
    this.properties = function(mid) {
    	var msgProperties = document.getElementById("properties");
		if (msgProperties) {
			if (msgProperties.className === "show") {
				msgProperties.className = "hide";
				return;
			}
		
			msgProperties.className = "show";
		}
    
	    ajaxContents = new AjaxContents();
	    ajaxContents.init();
    	ajaxContents.selectContents("mail/properties.drt?mid=" + mid, false, "msgProperties", false, false);
    };
    
    this.flagImportant = function(action) {
    	showLoading();
    
    	if (action) {
    		document.messagesForm.folder.value = "FLAGGED";
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.flagUnread = function(action) {
    	showLoading();
    
    	if (action) {
    		document.messagesForm.folder.value = "RECENT";
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.spam = function(action) {
    	showLoading();
    
    	if (action) {
    		if (document.messagesForm.displayLocation) {
    			if (this.locationHash) {    
		    		var aux = this.locationHash.indexOf('#');
		    		if (aux > -1) {
						var lh = this.locationHash.substring(aux + 1, this.locationHash.length);
						document.messagesForm.displayLocation.value = lh;
				    }
			    } 
    		}
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.notSpam = function(action) {
    	showLoading();
    
    	if (action) {
    		if (document.messagesForm.displayLocation) {
    			if (this.locationHash) {    
		    		var aux = this.locationHash.indexOf('#');
		    		if (aux > -1) {
						var lh = this.locationHash.substring(aux + 1, this.locationHash.length);
						document.messagesForm.displayLocation.value = lh;
				    }
			    } 
    		}
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.move = function(action, obj) {
    	showLoading();
    
    	if (action) {
    		document.messagesForm.folder.value = obj;
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.deleteMessages = function(action, obj) {
    	showLoading();
    
    	if (action) {
    		if (document.messagesForm.displayLocation) {
    			if (this.locationHash) {    
		    		var aux = this.locationHash.indexOf('#');
		    		if (aux > -1) {
						var lh = this.locationHash.substring(aux + 1, this.locationHash.length);
						document.messagesForm.displayLocation.value = lh;
				    }
			    } 
    		}
    		document.messagesForm.folder.value = obj;
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.deleteAll = function(action, obj, msg) {
    	if (!confirm(msg)) {
    		return;
    	}
    
    	showLoading();
    
    	if (action) {
	    	document.messagesForm.folder.value = obj;
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.archive = function(action) {
    	showLoading();
    
    	if (action) {
    		if (document.messagesForm.displayLocation) {
    			if (this.locationHash) {    
		    		var aux = this.locationHash.indexOf('#');
		    		if (aux > -1) {
						var lh = this.locationHash.substring(aux + 1, this.locationHash.length);
						document.messagesForm.displayLocation.value = lh;
				    }
			    } 
    		}
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.deleteLabels = function(action) {
    	showLoading();
    
    	if (action) {
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.selectAll = function() {
    	if (document.messagesForm.mid.length == null) {
			document.messagesForm.mid.checked = this.selectedAll;
			this.cacheMid(document.messagesForm.mid);
		} else {
			for(var i = 0; i < document.messagesForm.mid.length; i++) {
				document.messagesForm.mid[i].checked = this.selectedAll;
				this.cacheMid(document.messagesForm.mid[i]);
			}
		}
		
		this.selectedAll = !this.selectedAll;
    };
    
    this.selectUnread = function() {
    	if (document.messagesForm.mid.length == null) {
    		var td = document.getElementById("td" + document.messagesForm.mid.value);
    		if (td) {
    			if (td.title == "new") {
    				document.messagesForm.mid.checked = true;
    				this.cacheMid(document.messagesForm.mid);
    			} else {
	    			document.messagesForm.mid.checked= false;
	    			this.cacheMid(document.messagesForm.mid);
    			}
    		}			
		} else {
			for(var i = 0; i < document.messagesForm.mid.length; i++) {
				var td = document.getElementById("td" + document.messagesForm.mid[i].value);
	    		if (td) {
	    			if (td.title == "new") {
	    				document.messagesForm.mid[i].checked = true;
	    				this.cacheMid(document.messagesForm.mid[i]);
	    			} else {
		    			document.messagesForm.mid[i].checked = false;
		    			this.cacheMid(document.messagesForm.mid[i]);
	    			}
	    		}		
			}
		}
    };
    
    this.selectRead = function() {
    	if (document.messagesForm.mid.length == null) {
    		var td = document.getElementById("td" + document.messagesForm.mid.value);
    		if (td) {
    			if (td.title == "") {
    				document.messagesForm.mid.checked = true;
    				this.cacheMid(document.messagesForm.mid);
    			} else {
	    			document.messagesForm.mid.checked = false;
	    			this.cacheMid(document.messagesForm.mid);
    			}
    		}			
		} else {
			for(var i = 0; i < document.messagesForm.mid.length; i++) {
				var td = document.getElementById("td" + document.messagesForm.mid[i].value);
	    		if (td) {
	    			if (td.title == "") {
	    				document.messagesForm.mid[i].checked = true;
	    				this.cacheMid(document.messagesForm.mid[i]);
	    			} else {
		    			document.messagesForm.mid[i].checked = false;
		    			this.cacheMid(document.messagesForm.mid[i]);
	    			}
	    		}		
			}
		}
    };
    
    this.markSelection = function() {
    	var control = false;
		var aux = false;
		
		for(var i = 0; i < document.messagesForm.mid.length; i++) {
			if (document.messagesForm.mid[i].checked && !control) {
				control = true;
			} else if (document.messagesForm.mid[i].checked && control) {
				control = false;
			} else {
			}	
			
			if (control) {
				document.messagesForm.mid[i].checked = true;
			}	
			
			this.cacheMid(document.messagesForm.mid[i]);
		}
    };
    
    this.backWithHash = function() {
    	if (this.locationHash) {    
    		var aux = this.locationHash.indexOf('#');
    		if (aux > -1) {
				var lh = this.locationHash.substring(aux + 1, this.locationHash.length);
				handleHistoryChange(lh, null);
		    }
	    } else {
	    	window.history.back();
	    }
    };
    
    this.infoFolders = function (inboxCount, spamCount, htmlQuota) {
		var aux = document.getElementById("infoINBOX");
		if (aux && inboxCount > 0) {
			aux.innerHTML = "(" + inboxCount + ")";
			parent.frames.document.title = "(" + inboxCount + ") Duroty System";
		} else {
			aux.innerHTML = "";
			//parent.frames.document.title = "Duroty System";
			//parent.frames.document.title = "Duroty System";
		}
		
		aux = document.getElementById("infoSPAM");
		if (aux && spamCount > 0) {
			aux.innerHTML = "(" + spamCount + ")";	
		} else {
			aux.innerHTML = "";
		}
		
		aux = document.getElementById("quotaLayer");
		
		if (aux && htmlQuota != null) {
			aux.innerHTML = htmlQuota;
		}
	};
	
	this.infoLabelsToNull = function () {
		var lluga = document.getElementById("dlabel");
		if (lluga) {
			var menu = lluga.getElementsByTagName("SPAN");
			if (menu) {
				for (var i = 0; i < menu.length; i++) {
					var currentElement = menu[i];
					currentElement.innerHTML = "";					
				} 
			}
		}
	};
	
	this.infoLabels = function (labIdint, labCount) {
		var selectedElement = document.getElementById('infoLab' + labIdint);
		if (selectedElement && labCount > 0) {		
			// cause the new selected menu item to appear differently in the UI
			selectedElement.innerHTML = "(" + labCount + ")";
		} else {
			selectedElement.innerHTML = "";
		}
	};
	
	this.refresh = function () {
		showLoading();
		setTimeout("window.frames['session'].REFRESH.selectSession(true)", 10);
	};
	
	this.order = function(orderBy) {	
		if (this.EXTRA && this.EXTRA == 0) {
			this.EXTRA = "DESC";
		}
		
		if (this.EXTRA == "DESC") {
			this.EXTRA = "ASC";
		} else {
			this.EXTRA = "DESC";
		}
		
		this.displayLocation(this.ACTION + ":" + this.NAME + "*" + orderBy + "!" + this.EXTRA);
	};
	
	this.showHideLabels = function () {
		var aux1 = document.getElementById("labelsContents");
		if (aux1 && aux1.style.display == "block") {
			aux1.style.display = "none";
			var aux2 = document.getElementById("imgOpenCloseLabels");
			if (aux2) {
				aux2.src = "images/triangle.gif";
			}
		} else if (aux1 && aux1.style.display == "none") {
			aux1.style.display = "block";
			var aux2 = document.getElementById("imgOpenCloseLabels");
			if (aux2) {
				aux2.src = "images/opentriangle.gif";
			}
		}
	}
	
	this.showHideBuddies = function () {
		var aux1 = document.getElementById("dbuddyContents");
		if (aux1 && aux1.style.display == "block") {
			aux1.style.display = "none";
			var aux2 = document.getElementById("imgOpenCloseBuddy");
			if (aux2) {
				aux2.src = "images/triangle_white.gif";
			}
		} else if (aux1 && aux1.style.display == "none") {
			aux1.style.display = "block";
			var aux2 = document.getElementById("imgOpenCloseBuddy");
			if (aux2) {
				aux2.src = "images/opentriangle_white.gif";
			}
		}
	}
	
	this.parseAdvancedSearch = function (ajaxContents) {
		var _from = document.formAdvancedSearch.from.value;
		var _to = document.formAdvancedSearch.to.value;
		var _subject = document.formAdvancedSearch.subject.value;
		var _label = document.formAdvancedSearch.label.value;
		var _box = document.formAdvancedSearch.box.value;
		var _hasWords = document.formAdvancedSearch.hasWords.value;
		var _hasWordsInBody = document.formAdvancedSearch.hasWordsInBody;
		var _hasWordsInAttachment = document.formAdvancedSearch.hasWordsInAttachment;
		var _filetype = document.formAdvancedSearch.filetype.options[document.formAdvancedSearch.filetype.selectedIndex].value;
		/*var _doesntHaveWords = document.formAdvancedSearch.doesntHaveWords.value;
		var _doesntHaveWordsInBody = document.formAdvancedSearch.doesntHaveWordsInBody;
		var _doesntHaveWordsInAttachment = document.formAdvancedSearch.doesntHaveWordsInAttachment;*/
		var _hasAttachment = document.formAdvancedSearch.hasAttachment;
		var _fixDate = document.formAdvancedSearch.fixDate.options[document.formAdvancedSearch.fixDate.selectedIndex].value;
		
		var _startDate = document.formAdvancedSearch.startDate.value;
		var _endDate = document.formAdvancedSearch.endDate.value;
		
		if (_from != null && _from != "") {
			ajaxContents.setVar("from", _from);
		}
		
		if (_to != null && _to != "") {
			ajaxContents.setVar("to", _to);
		}
		
		if (_subject != null && _subject != "") {
			ajaxContents.setVar("subject", _subject);
		}
		
		if (_label != null && _label != "") {
			ajaxContents.setVar("label", _label);
		}
		
		if (_box != null && _box != "") {
			ajaxContents.setVar("box", _box);
		}
		
		if (_hasWords != null && _hasWords != "") {
			ajaxContents.setVar("hasWords", _hasWords);
			if (_hasWordsInBody != null && _hasWordsInBody.checked == true) {
				ajaxContents.setVar("hasWordsInBody", "true");
			}
			
			if (_hasWordsInAttachment != null && _hasWordsInAttachment.checked == true) {
				ajaxContents.setVar("hasWordsInAttachment", "true");
			}
		}	
		
		if (_filetype != null && _filetype != "") {
			ajaxContents.setVar("filetype", _filetype);
		}
		
		if (_hasAttachment != null && _hasAttachment.checked == true) {
			ajaxContents.setVar("hasAttachment", "true");
		}
		
		if (_fixDate != null && _fixDate > 0) {
			ajaxContents.setVar("fixDate", _fixDate);
		}
		
		if (_startDate != null && _startDate != "") {
			ajaxContents.setVar("startDate", _startDate);
		}
		
		if (_endDate != null && _endDate != "") {
			ajaxContents.setVar("endDate", _endDate);
		}
	}
	
	this.parseTestFilter = function (ajaxContents) {
		var _from = document.formFilter.from.value;
		var _to = document.formFilter.to.value;
		var _subject = document.formFilter.subject.value;
		var _hasWords = document.formFilter.hasWords.value;
		var _doesntHaveWords = document.formFilter.doesntHaveWords.value;		
		var _hasAttachment = document.formFilter.hasAttachment;
		var _operator = document.formFilter.operator;
		
		var control = false;
		var appendText = "";
		var queryString = "";
		
		if (_from != null && _from != "") {
			ajaxContents.setVar("from", _from);
		}
		
		if (_to != null && _to != "") {
			ajaxContents.setVar("to", _to);
		}
		
		if (_subject != null && _subject != "") {
			ajaxContents.setVar("subject", _subject);
		}
		
		if (_hasWords != null && _hasWords != "") {
			ajaxContents.setVar("hasWords", _hasWords);
		}	
		
		if (_doesntHaveWords != null && _doesntHaveWords != "") {
			ajaxContents.setVar("doesntHaveWords", _doesntHaveWords);
		}	
		
		if (_hasAttachment != null && _hasAttachment.checked == true) {
			ajaxContents.setVar("hasAttachment", "true");
		}
		
		if (_operator != null && _operator.checked == true) {
			ajaxContents.setVar("operator", "true");
		}
	}
	
	this.textVersion = function(_objName) {
		try {			
			var aux = document.getElementById("isHtml" + _objName);
			if (aux) {
				if (aux.value == "false") {
					htmlMessage = false;
					return;
				}
				
				this.removeTinyMce(_objName);
				
				aux.value = "false";
				htmlMessage = false;
				
				var _obj = document.getElementById(_objName);
				if (_obj) {
					_obj.value = decodeEntities(_obj.value);
				}
			}
		} catch (e) {
			alert(e);
		}
	};
	
	this.htmlVersion = function(_objName) {
		try {
			var aux = document.getElementById("isHtml" + _objName);
			if (aux) {
				if (aux.value == "true") {
					htmlMessage = true;
					return;
				}
			
				this.addTinyMce(_objName);
				tinyMCE.execCommand("mceFocus", true, _objName);

				aux.value = "true";
				htmlMessage = true;
			}
		} catch (e) {
			alert(e);
		}
	};
	
	this.addTinyMce = function(_ta) {
		try {		
			if (!tinyMCE.getInstanceById(_ta)) {
				tinyMCE.execCommand("mceAddControl", false, _ta);
			} else {
				tinyMCE.execCommand('mceResetDesignMode', false, _ta);
			}			
			
			tinyMCE.idCounter=0;
		} catch (e) {
			//tinyMCE.idCounter=0;
			alert(e);
		}
	}
	
	this.removeTinyMce = function(_ta) {
		try {
			if (tinyMCE.getInstanceById(_ta) != null) {
				//alert('merda');
				tinyMCE.execCommand("mceRemoveControl", false, _ta);
			} else {
//				tinyMCE.execCommand('mceResetDesignMode', false, _ta);
			}
			
			tinyMCE.idCounter=0;
		} catch (e) {
			//tinyMCE.idCounter=0;
			//alert(e);
		}
	}
	
	this.cacheMid = function(_checkbox)  {
		if (_checkbox.checked) {
			this.hash.addItem(_checkbox.value, _checkbox.value);
		} else {
			this.hash.removeItem(_checkbox.value);
		}
	}
}

function Hash(arguments) {
    this.length = 0;
    this.items = []; // Hash keys and their values
    this.order = []; // Array of the order of hash keys
    this.cursorPos = 0; // Current cursor position in the hash
    if (arguments != null) {
        for (var i = 0; i < arguments.length; i += 2) {
            if (typeof (arguments[i + 1]) != "undefined") {
                this.items[arguments[i]] = arguments[i + 1];
                this.order[this.length] = arguments[i];
                this.length++;
            }
        }
    }
    this.getItem = function (key) {
        return this.items[key];
    };
    this.setItem = function (key, val) {
        if (typeof val != "undefined") {
            if (typeof this.items[key] == "undefined") {
                this.order[this.length] = key;
                this.length++;
            }
            this.items[key] = val;
            return this.items[key];
        }
    };
    this.addItem = function (key, val) {
        this.setItem(key, val);
    };
    this.removeItem = function (key) {
        if (typeof this.items[key] != "undefined") {
            var pos = null;
            delete this.items[key]; // Remove the value
            // Find the key in the order list
            for (var i = 0; i < this.order.length; i++) {
                if (this.order[i] == key) {
                    pos = i;
                }
            }
            this.order.splice(pos, 1); // Remove the key
            this.length--; // Decrement the length
        }
    };
    this.hasKey = function (key) {
        return typeof this.items[key] != "undefined";
    };
    this.hasValue = function (val) {
        for (var i = 0; i < this.order.length; i++) {
            if (this.items[this.order[i]] == val) {
                return true;
            }
        }
        return false;
    };
    this.allKeys = function (str) {
        return this.order.join(str);
    };
    this.replaceKey = function (oldKey, newKey) {
        // If item for newKey exists, nuke it
        if (this.hasKey(newKey)) {
            this.removeItem(newKey);
        }
        this.items[newKey] = this.items[oldKey];
        delete this.items[oldKey];
        for (var i = 0; i < this.order.length; i++) {
            if (this.order[i] == oldKey) {
                this.order[i] = newKey;
            }
        }
    };
    this.getAtPos = function (pos) {
        var lookup = this.items[this.order[pos]];
        return typeof lookup != "undefined" ? lookup : false;
    };
    this.removeAtPos = function (pos) {
        var ret = this.items[this.order[pos]];
        if (typeof ret != "undefined") {
            delete this.items[this.order[pos]];
            this.order.splice(pos, 1);
            this.length--;
            return true;
        } else {
            return false;
        }
    };
    this.getFirst = function () {
        return this.items[this.order[0]];
    };
    this.getLast = function () {
        return this.items[this.order[this.length - 1]];
    };
    this.getCurrent = function () {
        return this.items[this.order[this.cursorPos]];
    };
    this.getNext = function () {
        if (this.cursorPos == this.length - 1) {
            return false;
        } else {
            this.cursorPos++;
            return this.items[this.order[this.cursorPos]];
        }
    };
    this.getPrevious = function () {
        if (this.cursorPos == 0) {
            return false;
        } else {
            this.cursorPos--;
            return this.items[this.order[this.cursorPos]];
        }
    };
    this.pop = function () {
        var pos = this.length - 1;
        var ret = this.items[this.order[pos]];
        if (typeof ret != "undefined") {
            this.removeAtPos(pos);
            return ret;
        } else {
            return false;
        }
    };
    this.set = function (cursorPos) {
        this.cursorPos = cursorPos;
    };
    this.reset = function () {
        this.cursorPos = 0;
    };
    this.end = function () {
        this.cursorPos = (this.length - 1);
    };
    this.each = function (func) {
        for (var i = 0; i < this.order.length; i++) {
            var key = this.order[i];
            var val = this.items[key];
            func(key, val);
        }
        return true;
    };
    this.sort = function (specialSort, desc) {
        var sortFunc = this.getSort(specialSort, desc);
        var valSort = [];
        var keySort = [];
        for (var i = 0; i < this.order.length; i++) {
            valSort[i] = this.items[this.order[i]];
        }
        // Sort values
        valSort.sort(sortFunc);
        for (var i = 0; i < valSort.length; i++) {
            for (j in this.items) {
                if (this.items[j] == valSort[i]) {
                    keySort[i] = j;
                    this.removeItem(j);
                }
            }
        }
        for (var i = 0; i < valSort.length; i++) {
            this.sort[i] = keySort[i];
            this.setItem(keySort[i], valSort[i]);
        }
    };
    this.sortByKey = function (specialSort, desc) {
        var sortFunc = this.getSort(specialSort, desc);
        this.order.sort(sortFunc);
    };
    // Sort methods
    // ==============
    this.getSort = function (specialSort, desc) {
        var sortFunc = null;
        if (typeof specialSort == "function") {
            sortFunc = specialSort;
        } else {
            if (specialSort == true) {
                sortFunc = desc ? this.simpleDescNoCase : this.simpleAscNoCase;
            } else {
                sortFunc = desc ? this.simpleDescCase : this.simpleAscCase;
            }
        }
        return sortFunc;
    };
    this.simpleAscCase = function (a, b) {
        return (a >= b) ? 1 : -1;
    };
    this.simpleDescCase = function (a, b) {
        return (a < b) ? 1 : -1;
    };
    this.simpleAscNoCase = function (a, b) {
        return (a.toLowerCase() >= b.toLowerCase()) ? 1 : -1;
    };
    this.simpleDescNoCase = function (a, b) {
        return (a.toLowerCase() < b.toLowerCase()) ? 1 : -1;
    };
}