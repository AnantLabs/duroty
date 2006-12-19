String.prototype.isAlphaNumeric = function () {
    return /^[a-z0-9_\d]+$/.test(this);
};

///////////////////////////////////
//          ajax im v2           //
//    AJAX Instant Messenger     //
//      Copyright (c) 2006       //
// unwieldy studios/Joshua Gross //
//  http://unwieldy.net/ajaxim/  //
//   Do not remove this notice   //
///////////////////////////////////

// Configurable Options //

// Notification // 
var useBlinker = true;           // Show new message in titlebar when window isn't active.
var blinkSpeed = 1000;           // How fast to change between the titles when "blinking" (in milliseconds).
var pulsateTitles = true;           // Pulsate (blink) IM window titles on new IM when they are not the active window.
var idle_time = 15000;
// Server //
var pingFrequency = 2500;           // How often to ping the server (in milliseconds). Best range between 2500 and 3500 ms.
var pingTo = "chat/index.drt";  // The file that is the "server".

// Windows //
var imWidth = 310;            // Default IM window width
var imHeight = 335;            // Default IM window height
var imHistory = true;           // "Save" conversations with buddies throughout the session

// Other //
var alertCSS = "alphacube";    // CSS file for alerts and login/add buddy/IM anyone windows
var alertWidth = 400;            // Alert window width (see above)
var windowCSS = "default";      // CSS file for all other windows (buddylist, IMs)
var buddyListLoc = 1;              // Buddylist location: 0=left, 1=right

// End Configurable Options //

// Begin Code                                   //
// Note: Do not edit below this line unless you //
//       know what you are doing!               //
var curSelected = "";
var showOfflineBuddies = true;
var IMWindows = {};
var titlebarBlinker = true;
var defaultTitle = parent.frames.document.title;
var buddyList = {};
var buddyListWin;

function Chat () {
	var self = this;

	this.pingTimer = null;
	this.blinkerTimer = null;
	this.idleTimer = null;
	this.control_logout = false;
	this.lastState = false;
	
	this.ring = 0;
	
	this.durotyLogin = function(aux) {
		clearTimeout(self.pingTimer);
		clearTimeout(self.idleTimer);
	    self.pingTimer = setInterval("Chat.ping()", pingFrequency);
	    
	    if (aux) {
		    Dialog.closeInfo();
		    var divStatusSettings = $("statusSettings");
		    if (divStatusSettings) {
		        divStatusSettings.innerHTML += "<input type=\"text\" id=\"customStatus\" onkeypress=\"Chat.processCustomAway(event);\" style=\"display:none\" onblur=\"if($('customStatus').style.display != 'none') { $('customStatus').style.display = 'none'; $('curStatus').style.display = 'block'; }\" />";
	    	}
	    }
	    
	    showOfflineBuddies = true;
	};
	
	this.login = function() {
		var ajaxSack = new sack();
	    var url = pingTo + "?call=login";
	    ajaxSack.requestFile = url;
		ajaxSack.runAJAX();
		
		self.durotyLogin();
	    
	    self.control_logout = false;
	    showOfflineBuddies = true;
	};
	
	this.logout = function() {
		var ajaxSack = new sack();
	    var url = pingTo + "?call=logout";
	    ajaxSack.requestFile = url;
		ajaxSack.runAJAX();
		
		clearTimeout(self.pingTimer);
		clearTimeout(self.idleTimer);
		for (var name in IMWindows) {
	        if (typeof ($(name + "_im")) != "undefined") {
	            IMWindows[name].destroy();
	        }
	    }
	    showOfflineBuddies = false;
	    self.control_logout = true;
	};
	
	this.signin = function() {
		var ajaxSack = new sack();
	    var url = pingTo + "?call=signin";
	    ajaxSack.requestFile = url;
		ajaxSack.runAJAX();
		
	    self.control_logout = false;
	    showOfflineBuddies = true;
	};
	
	this.signout = function() {
		var ajaxSack = new sack();
	    var url = pingTo + "?call=signout";
	    ajaxSack.requestFile = url;
		ajaxSack.runAJAX();
		
		clearTimeout(self.pingTimer);
	    clearTimeout(self.idleTimer);
	    
	    $("dbuddy").innerHTML = "";
	    self.control_logout = true;
	    showOfflineBuddies = false;
	};
	
	this.setState = function(state, away_msg) {
		var ajaxSack = new sack();
	    //var url = pingTo + "?call=state&state=" + state + "&awayMessage=" + encodeURIComponent(away_msg);
	    var url = pingTo;
	    ajaxSack.setVar("call", "state");
	    ajaxSack.setVar("state", state);
	    ajaxSack.setVar("awayMessage", away_msg);
	    
	    ajaxSack.requestFile = url;
		ajaxSack.runAJAX();
	};
	
	this.ping = function() {	
	    var ajaxSack = new sack();
	    var url = pingTo + "?call=ping&from=" + user;
	    ajaxSack.requestFile = url;
	    ajaxSack.onCompletion = function () {	      	        
	        var i;
	        if (ajaxSack.response == "not_logged_in") {
	        	self.lastState = false;
	            $("curStatus").innerHTML = $("state6").innerHTML;
	            return;
	        }
	        
	        var response = ajaxSack.response.parseJSON();	        
	        var lastState = response.lastState;
	        var state = response.state;
	        self.lastState = state;
	        var awayMessage = response.awayMessage;
	        
	        if (state == 1 && lastState == 1 && titlebarBlinker == true) {
	        	//available and default login
	        	//si no tinc el focus (estem en blur) titlebarBlinker = true setState(3);
	        	clearTimeout(self.pingTimer);
	        	clearTimeout(self.idleTimer);
	            setTimeout("Chat.setIdle()", 500);
	        } else if (state == 3 && lastState == 1 && titlebarBlinker == false) {
	        	//idle and default login
	        	//si no tinc el blur (blur) titlebarBlinker = false sigin;
	        	clearTimeout(self.pingTimer);
	        	clearTimeout(self.idleTimer);	        	
	            setTimeout("Chat.setIdle()", 500);
	        } else {
	        	if (state == 0) {
					//login
					if ($("curStatus").innerHTML != $("state6").innerHTML) {
				        $("curStatus").innerHTML = $("state6").innerHTML;
					}
				} else if (state == 1) {
					//login
			        if ($("curStatus").innerHTML != $("state1").innerHTML) {
				        $("curStatus").innerHTML = $("state1").innerHTML;
					}
				} else if (state == 2) {
					//away
					if ($("curStatus").innerHTML != ('<img src="images/user_busy.gif" border="0">&nbsp;' + awayMessage)) {
				        $("curStatus").innerHTML = '<img src="images/user_busy.gif" border="0">&nbsp;' + awayMessage;
					}	
				} else if (state == 3) {
					//idle
					if ($("curStatus").innerHTML != ('<img src="images/user_idle.gif" border="0">' + $("state7").innerHTML)) {
				        $("curStatus").innerHTML = '<img src="images/user_idle.gif" border="0">' + $("state7").innerHTML;
					}
				} else {
					//login
			        if ($("curStatus").innerHTML != $("state1").innerHTML) {
				        $("curStatus").innerHTML = $("state1").innerHTML;
					}
				}
	        }
	        
	        var from, data;
	        for (i = 0; i < response.numMessages; i++) {
	            from = response.messages[i].sender;
	            data = response.messages[i].message;
	            if (!$(from + "_im")) {
	                self.createIMWindow(from, from);
	            } else {
	                if (!IMWindows[from].isVisible()) {
	                    IMWindows[from].show();
	                    setTimeout("Chat.scrollToBottom('" + from + "_rcvd')", 125);
	                } else {
	                	if (!IMWindows[from].isMaximize()) {
	                		IMWindows[from].minimize();
	                	}
	                }
	                
	                //setTimeout("Flash_embedSWF('bicycle_bell.swf', false)", 500);
	                
	                self.changeZIndex(from + "_im");
	            }
	            Stamp = new Date();
	            var h = String(Stamp.getHours());
	            var m = String(Stamp.getMinutes());
	            var s = String(Stamp.getSeconds());
	            h = (h.length > 1) ? h : "0" + h;
	            m = (m.length > 1) ? m : "0" + m;
	            var curIM = $(from + "_rcvd");
	            data = data.replace(/(\s|\n|^)(\w+:\/\/[^\s\n]+)/, "$1<a href=\"$2\" target=\"_new\">$2</a>");
	            curIM.innerHTML = curIM.innerHTML + "<b class=\"userB\">[" + h + ":" + m + "] " + from + ":</b> " + data + "<br>\n";
	            curIM.scrollTop = curIM.scrollHeight - curIM.clientHeight;
	            if (curIM.innerHTML.toLowerCase().indexOf(user.toLowerCase() + ":</b> (auto-reply:)") == -1 && state >= 2) {
	                $(from + "_sendBox").value = "(Auto-Reply:) " + awayMessage;
	                self.sendMessage(from);
	            }
	            if (titlebarBlinker == true && useBlinker == true) {	            	
	                clearTimeout(self.blinkerTimer);
	                setTimeout("Chat.titlebarBlink('" + from + "', \"" + data.replace(/\"/, "\"").replace(/<([^>]+)>/ig, "") + "\", 0)", blinkSpeed);
	            }
	        }
	        
	        var buddy;
	        if (!response.buddy) {
	            return;
	        }
	        var group = "";
	        if (!buddyList[group]) {
	            buddyList[group] = [];
	        }
	        for (i = 0; i < response.buddy.length; i++) {
	            buddy = response.buddy[i];
	            buddyList[group][i] = buddy.username;
	            if (!$(buddy.username + "_blItem")) {
	                var name = buddy.name;
	                if (!name) {
	                    name = buddy.username;
	                }
	                self.addBuddyToList(name, buddy.username, group);
	            }
	            if (buddy.is_online == 0) {
	                if (showOfflineBuddies == false) {
	                    $(buddy.username + "_blItem").style.display = "none";
	                } else {
	                    $(buddy.username + "_blItem").style.display = "block";
	                }
	                $(buddy.username + "_blImg").src = "images/user_offline.gif";
	            } else {
	                if (buddy.is_online == 2) {
	                    $(buddy.username + "_blItem").style.display = "block";
	                    $(buddy.username + "_blImg").src = "images/user_busy.gif";
	                } else {
	                    if (buddy.is_online == 3) {
	                        $(buddy.username + "_blItem").style.display = "block";
	                        $(buddy.username + "_blImg").src = "images/user_idle.gif";
	                    } else {
	                        $(buddy.username + "_blItem").style.display = "block";
	                        $(buddy.username + "_blImg").src = "images/user_available.gif";
	                    }
	                }
	            }
	        }
	    };
	    ajaxSack.runAJAX();
	};
		
		
	this.sendMessage = function(winName) {
	    var xhConn = new XHConn();
	    var isBold = false;
	    var isItalic = false;
	    var isUnderline = false;
	    xhConn.connect(pingTo, "POST", "call=send&from=" + user + "&recipient=" + winName + "&bold=" + isBold + "&italic=" + isItalic + "&underline=" + isUnderline + "&msg=" + encodeURIComponent($(winName + "_sendBox").value.replace(/&/g, "<amp>")), function (xh) {
	        if (xh.responseText == "sent") {
	            var sentText = $(winName + "_sendBox").value;
	            $(winName + "_sendBox").value = "";
	            var rcvdBox = $(winName + "_rcvd");
	            Stamp = new Date();
	            var h = String(Stamp.getHours());
	            var m = String(Stamp.getMinutes());
	            var s = String(Stamp.getSeconds());
	            h = (h.length > 1) ? h : "0" + h;
	            m = (m.length > 1) ? m : "0" + m;
	            rcvdBox.innerHTML = rcvdBox.innerHTML + "<b class=\"userA\">[" + h + ":" + m + "] " + user + ":</b> " + (isBold == "true" ? "<b>" : "") + (isItalic == "true" ? "<i>" : "") + (isUnderline == "true" ? "<u>" : "") + sentText.replace(/<([^>]+)>/ig, "").replace(/(\s|\n|^)(\w+:\/\/[^\s\n]+)/, "$1<a href=\"$2\" target=\"_new\">$2</a>") + (isBold == "true" ? "</b>" : "") + (isItalic == "true" ? "</i>" : "") + (isUnderline == "true" ? "</u>" : "") + "<br>\n";
	            rcvdBox.scrollTop = 300 + rcvdBox.scrollHeight - rcvdBox.clientHeight;
	        } else {
	            if (xh.responseText == "not_online") {
	                var rcvdBox = $(winName + "_rcvd");
	                rcvdBox.innerHTML = rcvdBox.innerHTML + "<span style=\"color:#FF0000\"><b>Error: Your message could not be sent because the recipient is not logged in.</b></span><br>";
	                rcvdBox.scrollTop = 300 + rcvdBox.scrollHeight - $(winName + "_im").clientHeight;
	            } else if (xh.responseText == "not_accept_chat") {
	            	var rcvdBox = $(winName + "_rcvd");
	                rcvdBox.innerHTML = rcvdBox.innerHTML + "<span style=\"color:#007200\"><b>Error: Your message could not be sent because the recipient is not accept chat.</b></span><br>";
	                rcvdBox.scrollTop = 300 + rcvdBox.scrollHeight - $(winName + "_im").clientHeight;
	            } else {
	                if (xh.responseText == "not_logged_in") {
	                    logout();
	                } else {
	                    alert("An error occured while sending your message.");
	                }
	            }
	        }
	        $(winName + "_sendBox").focus();
	    });
	};
	
	this.createIMWindow = function(name, imTitle) {
		IMWindows[name] = new ChatRoom();
		IMWindows[name].create(name, imTitle, "chatRoom", 50);
		IMWindows[name].show();
	
	    /*var imLeft = Math.round(Math.random() * (this.browserWidth() - 360)) + "px";
	    var imTop = Math.round(Math.random() * (this.browserHeight() - 400)) + "px";
	    IMWindows[name] = new Window(name + "_im", {className:"dialog", width:310, height:335, top:imTop, left:imLeft, zIndex:200, resizable:true, title:imTitle, draggable:true, minWidth:150, minHeight:75, showEffectOptions:{duration:0}, hideEffectOptions:{duration:0}});
	    IMWindows[name].getContent().innerHTML = "<div class=\"rcvdMessages\" id=\"" + name + "_rcvd\"></div>" + "\n" + "<div class=\"imToolbar\" id=\"" + name + "_toolbar\"><img src=\"images/" + windowCSS + "/bold_off.png\" onclick=\"Chat.toggleBold('" + name + "');\" alt=\"Bold\" id=\"" + name + "_bold\" /> " + "<img src=\"images/" + windowCSS + "/italic_off.png\" onclick=\"Chat.toggleItalic('" + name + "');\" alt=\"Italic\" id=\"" + name + "_italic\" /> " + "<img src=\"images/" + windowCSS + "/underline_off.png\" onclick=\"Chat.toggleUnderline('" + name + "');\" alt=\"Underline\" id=\"" + name + "_underline\" /></div>" + "\n" + "<input type=\"text\" class=\"inputText\" id=\"" + name + "_sendBox\" onfocus=\"Chat.blinkerOn(false);\" onkeypress=\"Chat.keyHandler(event," + "'" + name + "'" + ");\" />";
	    $(name + "_rcvd").style.height = (IMWindows[name].getSize().height - 73) + "px";
	    $(name + "_rcvd").style.width = (IMWindows[name].getSize().width - 10) + "px";
	    $(name + "_toolbar").style.top = (IMWindows[name].getSize().height - 43) + "px";
	    $(name + "_toolbar").style.width = (IMWindows[name].getSize().width - 10) + "px";
	    $(name + "_sendBox").style.top = (IMWindows[name].getSize().height - 15) + "px";
	    $(name + "_sendBox").style.width = (IMWindows[name].getSize().width - 16) + "px";
	    $(name + "_sendBox").style.fontWeight = "400";
	    $(name + "_sendBox").style.fontStyle = "normal";
	    $(name + "_sendBox").style.textDecoration = "none";
	    IMWindows[name].show();*/
	};
	
	this.closeRoom = function(name) {
		if (IMWindows[name]) {
			IMWindows[name].hide();
		}
	};
	
	this.minimizeRoom = function(name) {
		if (IMWindows[name]) {
			IMWindows[name].minimize();
		}
	};
	
	this.keyHandler = function(e, name) {
	    var asc = document.all ? event.keyCode : e.which;
	    if (asc == 13) {
	        self.sendMessage(name);
	    }
	    return asc != 13;
	};
	
	this.addBuddyToList = function(name, username, groupname) {
	    $("dbuddy").innerHTML += "<div id=\"" + username + "_blItem\" class=\"buddy\" onmousedown=\"return false;\" onselectstart=\"return false;\" onmouseover=\"Chat.selectBuddy(this, '" + username + "', true);\" onmouseout=\"Chat.selectBuddy(this, '" + username + "', false);\" ondblclick=\"Chat.onBuddyDblClick();\" style=\"overflow: hidden; text-overflow:ellipsis; border-top: 1px solid #CCCCCC; cursor: pointer;\"><img src=\"images/user_available.gif\" width=\"16\" height=\"16\" alt=\"\" id=\"" + username + "_blImg\">&nbsp;" + name + "</div>";
	};
	
	this.selectBuddy = function(sel, username, selected) {
	    if (selected === false) {
	        sel.style.background = "#FFFFFF";
	        sel.style.color = "#000";
	        curSelected = "";
	    } else {
	        sel.style.background = "#e1ebf7";
	        sel.style.color = "#000";
	        curSelected = username;
	    }
	};
	
	this.onBuddyDblClick = function() {
	    if (curSelected.length > 0) {
	        if (!$(curSelected + "_im")) {
	            self.createIMWindow(curSelected, curSelected);	            
	        } else {
	            if (!IMWindows[curSelected].isVisible()) {
	                IMWindows[curSelected].show();
	                setTimeout("Chat.scrollToBottom('" + curSelected + "_rcvd')", 125);
	            } else {
                	if (!IMWindows[curSelected].isMaximize()) {
                		IMWindows[curSelected].minimize();
                	}
                }
                self.changeZIndex(curSelected + "_im");
	        }
	        if ($(curSelected + "_sendBox")) {
	        	try {
		        	$(curSelected + "_sendBox").focus();
		        } catch (e) {
		        }
	        }
	    }
	};
	
	this.scrollToBottom = function(id) {		
	    $(id).scrollTop = 300 + $(id).scrollHeight + $(id).clientHeight;
	};
	
	this.trim = function(text){
	    if (text == null) {
	        return null;
	    }
	    return text.replace(/^[ \t]+|[ \t]+$/g, "");
	};
	
	this.setStatus = function(status, away_msg, state) {	
		if (status == 0) {
			//logout
			self.signout();
	        $("curStatus").innerHTML = $("state" + state).innerHTML;
		} else if (status == 1) {
			//login
			self.login();
	        $("curStatus").innerHTML = $("state" + state).innerHTML;
		} else if (status == 2) {
			//away
			self.setState(2, away_msg);
			$("curStatus").innerHTML = $("state" + state).innerHTML;
		} else if (status == 3) {
			//idle
			away_msg = $("state" + state).innerHTML;
			self.setState(3, away_msg);
	        $("curStatus").innerHTML = '<img src="images/user_idle.gif" border="0" />' + $("state" + state).innerHTML;
		} else {
			//login
			self.login();
			away_msg = "";
	        $("curStatus").innerHTML = $("state" + state).innerHTML;
		}
		
	    $("statusList").style.display = "none";
	};	
	
	this.customAway = function() {
	    $("curStatus").style.display = "none";
	    $("customStatus").style.display = "block";
	    $("customStatus").focus();
	};
	
	this.processCustomAway = function(e) {
	    var asc = document.all ? event.keyCode : e.which;
	    if (asc == 13) {
	        //isAway = 1;
	        var away_msg = $("customStatus").value;
	        self.setState(2, away_msg);
	        $("curStatus").innerHTML = "<img src=\"images/user_busy.gif\" border=\"0\" />&nbsp;" + away_msg.substring(0, 30) + (away_msg.length > 30 ? "..." : "");
	        $("curStatus").style.display = "block";
	        $("customStatus").style.display = "none";
	    }
	    return asc != 13;
	};
	
	this.toggleBold = function(name) {
	    $(name + "_sendBox").style.display = "none"; // horrah weird Opera 9 input refresh!
	    if ($(name + "_sendBox").style.fontWeight == "400") {
	        $(name + "_bold").src = "images/" + windowCSS + "/bold_on.png";
	        $(name + "_sendBox").style.fontWeight = "700";
	    } else {
	        $(name + "_sendBox").style.fontWeight = "400";
	        $(name + "_bold").src = "images/" + windowCSS + "/bold_off.png";
	    }
	    $(name + "_sendBox").style.display = "block"; // horrah weird Opera 9 input refresh!
	    $(name + "_sendBox").focus();
	};
	
	this.toggleItalic = function(name) {
	    $(name + "_sendBox").style.display = "none"; // horrah weird Opera 9 input refresh!
	    if ($(name + "_sendBox").style.fontStyle == "normal") {
	        $(name + "_sendBox").style.fontStyle = "italic";
	        $(name + "_italic").src = "images/" + windowCSS + "/italic_on.png";
	    } else {
	        $(name + "_sendBox").style.fontStyle = "normal";
	        $(name + "_italic").src = "images/" + windowCSS + "/italic_off.png";
	    }
	    $(name + "_sendBox").style.display = "block"; // horrah weird Opera 9 input refresh!
	    $(name + "_sendBox").focus();
	};
	
	this.toggleUnderline = function(name) {
	    $(name + "_sendBox").style.display = "none"; // horrah weird Opera 9 input refresh!
	    if ($(name + "_sendBox").style.textDecoration == "none") {
	        $(name + "_sendBox").style.textDecoration = "underline";
	        $(name + "_underline").src = "images/" + windowCSS + "/underline_on.png";
	    } else {
	        $(name + "_sendBox").style.textDecoration = "none";
	        $(name + "_underline").src = "images/" + windowCSS + "/underline_off.png";
	    }
	    $(name + "_sendBox").style.display = "block"; // horrah weird Opera 9 input refresh!
	    $(name + "_sendBox").focus();
	};
	
	this.setIdle = function() {
	    if (titlebarBlinker == false) {    
	    	setTimeout("Chat.parseSignin()", 100);
	    } else {
	    	setTimeout("Chat.parseIdle()", idle_time);
		}
	};
	
	this.parseSignin = function() {	
		clearTimeout(self.pingTimer);
       	clearTimeout(self.idleTimer);
	
		self.idleTimer = setTimeout("Chat.signin()", 100);
	    self.pingTimer = setInterval("Chat.ping()", pingFrequency);
	};
	
	this.parseIdle = function() {	
		if (titlebarBlinker == true) {
			clearTimeout(self.pingTimer);
    	   	clearTimeout(self.idleTimer);
	
			self.idleTimer = setTimeout("Chat.setStatus(3, null, 7)", 100);
    		self.pingTimer = setInterval("Chat.ping()", pingFrequency);
    	} else {
    		self.setIdle();
    	}
	};
	
	this.titlebarBlink = function(name, message, alter) {		
	    if (titlebarBlinker == false) {	    	
	        parent.frames.document.title = defaultTitle;
	        self.ring = 0;	        
	        return;
	    }
	    
	    if (alter == 0) {
	        parent.frames.document.title = name + "!";
	        self.blinkerTimer = setTimeout("Chat.titlebarBlink('" + name + "', '" + message + "', 1)", 1000);
	    } else {
	        if (alter == 1) {
	            parent.frames.document.title = "\"" + message.substring(0, 10) + (message.length > 10 ? "..." : "") + "\"";
	            self.blinkerTimer = setTimeout("Chat.titlebarBlink('" + name + "', '" + message + "', 2)", 1000);
	        } else {
	            if (alter == 2) {
	                parent.frames.document.title = defaultTitle;
	                self.blinkerTimer = setTimeout("Chat.titlebarBlink('" + name + "', '" + message + "', 0)", 1000);
	            }
	        }
	    }
	    
	    //Call ring flash
	    if (self.ring == 0) {
	    	self.ring++;
		    setTimeout("Flash_embedSWF('bicycle_bell.swf', false)", 1000);
		    setTimeout("Flash_embedSWF('bicycle_bell.swf', false)", 3000);		    
		} else {
			
		}		   
	};
	
	this.blinkerOn = function(onoff) {
	    if (onoff == true) {
	        titlebarBlinker = true;
	    } else {
	        titlebarBlinker = false;
	    }
	};
	
	this.browserWidth = function() {
	    if (self.innerWidth) {
	        return self.innerWidth;
	    } else {
	        if (document.documentElement && document.documentElement.clientWidth) {
	            return document.documentElement.clientWidth;
	        } else {
	            if (document.body) {
	                return document.body.clientWidth;
	            }
	        }
	    }
	    return 630;
	};
	
	this.browserHeight = function() {
	    if (self.innerWidth) {
	        return self.innerHeight;
	    } else {
	        if (document.documentElement && document.documentElement.clientWidth) {
	            return document.documentElement.clientHeight;
	        } else {
	            if (document.body) {
	                return document.body.clientHeight;
	            }
	        }
	    }
	    return 470;
	};
	
	this.changeZIndex = function(name) {
		zIndex = zIndex + 10;
		$(name).style.zIndex = zIndex;
	}
}
