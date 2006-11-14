window.onload = initialize;
var Notebook = new Notebook();

/** Our function that initializes when the page is finished loading. */
function initialize() {
	parent.document.body.rows = "100%,*,*";

	// initialize the DHTML History framework
    dhtmlHistory.initialize();	
   
	// add ourselves as a DHTML History listener
    dhtmlHistory.addListener(handleHistoryChange);	
	
	// determine what our initial location is by retrieving it from the browser's location after the hash
    var currentLocation = dhtmlHistory.getCurrentLocation();
		
	// if there is no location then display
	// the default, which is the inbox
    if (currentLocation === "") {
        currentLocation = "section:search:0*0!0";
    }
	
	// extract the section to display from the initial location 
    currentLocation = currentLocation.replace(/section\:/, "");
	
	// display this initial location
    Notebook.displayLocation(currentLocation, null);
}
/** Handles history change events. */
function handleHistoryChange(newLocation, historyData) {	
	// if there is no location then display the default, which is the INBOX
    if (newLocation === "") {
        newLocation = "section:search:0*0!0";
    }
    if (historyData === null) {
        historyData = historyStorage.get(newLocation);
    }
	
	// extract the section to display from the location change; newLocation will begin with the word "section:" 
    newLocation = newLocation.replace(/section\:/, "");
	
	// update the browser to respond to this DHTML history change
    Notebook.displayLocation(newLocation, historyData);
}
function Notebook() {	
	this.ACTION = "";
	this.NAME = 0;
	this.UID = 0;
	this.selectedAll = true;

    /** Displays the given location in the right-hand side content area. 
     *  el format de una newLocation ve donar per //action:name*uid!extra
      */
    this.displayLocation = function (newLocation, sectionData) {
    	hideInfo();
    	
    	setTimeout("window.frames['session'].REFRESH.selectSession(false)", 500);
    
    	//tinyMCE.execCommand("mceRemoveControl", false, "taReplyBody");
    
	    idx = newLocation.indexOf(':');
		
		this.ACTION = newLocation;
		this.NAME = 0;
		this.UID = 0;		
		this.EXTRA = 0;
		
		if (idx > -1) {		
			//en el cas del search tenim que el folder es un queryString
			aux1 = newLocation.indexOf('*');
			aux2 = newLocation.indexOf('!');
			this.EXTRA = newLocation.substring(aux2 + 1, newLocation.length);	
			this.UID = newLocation.substring(aux1 + 1, aux2);	
			this.NAME = newLocation.substring(idx + 1, aux1);
			this.ACTION = newLocation.substring(0, idx);
		}
		if (this.ACTION == "search") {
			var token = document.getElementById("token").value;
			this.search(token, this.NAME, this.UID, this.EXTRA);
		} else if (this.ACTION == "advancedSearch") {
		} else if (this.ACTION == "testFilter") {
		} else if (startsWith(this.ACTION, "label")) {
			var keyword = this.ACTION.substring(5, this.ACTION.length);	
			this.searchKeyword(this.ACTION, "keywords:(\"" + keyword + "\")", this.NAME, this.UID, this.EXTRA);
		} else if (this.ACTION == "compose") {
		} else if (this.ACTION == "read") {
		} else if (this.ACTION == "reply") {
		} else if (this.ACTION == "replyToAll") {
		} else if (this.ACTION == "forward") {
		} else {
			//El cas per defecte indica que volem llistar els missatges
			this.keywords(this.ACTION, this.NAME, this.UID, this.EXTRA);
		}
		
		dhtmlHistory.add("section:" + this.ACTION + ":" + this.NAME + "*" + this.UID + "!" + this.EXTRA, null);
		
		window.scroll(0,0);
    };
    
    this.search = function(token, page, order, desc) {    
    	var results = document.getElementById("results");
		if (results) {
			results.className = "show";
		}
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("token", token);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	
    	ajaxContents.selectContents("notebook/search.drt", false, "results");
    	this.parseRight(null, 0);
    };    
    
    this.searchKeyword = function(action, token, page, order, desc) {    
    	var results = document.getElementById("results");
		if (results) {
			results.className = "show";
		}
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("token", token);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	
    	ajaxContents.selectContents("notebook/search.drt", false, "results");
    	this.parseRight(action, 0);
    };    
    
    this.parseRight = function(action, idint) {		
    	// clear out the old selected menu item
		var menu = document.getElementById("labels");
		if (menu != null) {
			for (var i = 0; i < menu.childNodes.length; i++) {
				var currentElement = menu.childNodes[i];
				// see if this is a DOM Element node
				if (currentElement.nodeType == 1) {
					// clear any class name
					currentElement.className = "folder";
				}													
			} 
		}
    
		if (action == null) {
			return;
		}
		
		if (!startsWith(action, "label")) {
			
		} else {			
			idint = action.substring(5, action.length);
			var selectedElement = document.getElementById('lab' + idint);
			if (selectedElement != null) {		
				// cause the new selected menu item to appear differently in the UI
				selectedElement.className = "folderSelected";	
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
    	ajaxContents.selectContents("notebook/properties.drt?mid=" + mid, false, "msgProperties");
    };
    
    this.flag = function(action) {
    	showLoading();
    
    	if (action) {
    		document.messagesForm.folder.value = "FLAGGED";
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.unflag = function(action) {
    	showLoading();
    
    	if (action) {
    		document.messagesForm.folder.value = "RECENT";
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.deleteNotebook = function(action, obj) {
    	showLoading();
    
    	if (action) {
    		document.messagesForm.folder.value = obj;
    		document.messagesForm.action = action;
    	}
    	
    	document.messagesForm.submit();
    };
    
    this.selectAll = function() {
    	if (document.messagesForm.mid.length == null) {
			document.messagesForm.mid.checked = this.selectedAll;
		} else {
			for(var i = 0; i < document.messagesForm.mid.length; i++) {
				document.messagesForm.mid[i].checked = this.selectedAll;
			}
		}
		
		this.selectedAll = !this.selectedAll;
    };
    
    this.infoFolders = function (inboxCount, spamCount, htmlQuota) {
		var aux = document.getElementById("infoTopINBOX");
		if (aux && inboxCount > 0) {
			aux.innerHTML = "&nbsp;(" + inboxCount + ")";	
		} else {
			aux.innerHTML = "";
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
		var aux1 = document.getElementById("labels");
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
}

