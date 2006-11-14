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
var control_logout = false;
var idle_time = 300000;

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
var titlebarBlinker = false;
var defaultTitle = parent.frames.document.title;
var blinkerTimer;
var pingTimer;
var idleTimer;
var buddyList = {};
var buddyListWin;

window.onunload = function () {
    logout();
};

function fixBuddyList() {
}

function durotyLogin() {
    window.onresize = fixBuddyList;
    setTimeout(ping, 250);
    pingTimer = setInterval(ping, pingFrequency);
    Dialog.closeInfo();
    var divStatusSettings = $("statusSettings");
    if (divStatusSettings) {
        divStatusSettings.innerHTML += "<input type=\"text\" id=\"customStatus\" onkeypress=\"processCustomAway(event);\" style=\"display:none\" onblur=\"if($('customStatus').style.display != 'none') { $('customStatus').style.display = 'none'; $('curStatus').style.display = 'block'; }\" />";
    }
    showOfflineBuddies = true;
}

function login() {
	var ajaxSack = new sack();
    var url = pingTo + "?call=login";
    ajaxSack.requestFile = url;
	ajaxSack.runAJAX();
	
    setTimeout(ping, 250);
    pingTimer = setInterval(ping, pingFrequency);
    control_logout = false;
    showOfflineBuddies = true;
}

function signin() {
	var ajaxSack = new sack();
    var url = pingTo + "?call=signin";
    ajaxSack.requestFile = url;
	ajaxSack.runAJAX();
	
    control_logout = false;
    showOfflineBuddies = true;
}

function logout() {
	var ajaxSack = new sack();
    var url = pingTo + "?call=logout";
    ajaxSack.requestFile = url;
	ajaxSack.runAJAX();
	
	clearTimeout(pingTimer);
	clearTimeout(idleTimer);
	for (var name in IMWindows) {
        if (typeof ($(name + "_im")) != "undefined") {
            IMWindows[name].destroy();
        }
    }
    showOfflineBuddies = false;
    control_logout = true;
}

function signout() {
	var ajaxSack = new sack();
    var url = pingTo + "?call=signout";
    ajaxSack.requestFile = url;
	ajaxSack.runAJAX();
	
	clearTimeout(pingTimer);
    clearTimeout(idleTimer);    
    $("dbuddy").innerHTML = "";
    control_logout = true;
    showOfflineBuddies = false;
}

function setState(state, away_msg) {
	var ajaxSack = new sack();
    var url = pingTo + "?call=state&state=" + state + "&awayMessage=" + encodeURIComponent(away_msg);
    ajaxSack.requestFile = url;
	ajaxSack.runAJAX();
}

function ping() {	
    var ajaxSack = new sack();
    var url = pingTo + "?call=ping&from=" + user;
    ajaxSack.requestFile = url;
    ajaxSack.onCompletion = function () {    	        
        var i;
        if (ajaxSack.response == "not_logged_in") {
            $("curStatus").innerHTML = $("state6").innerHTML;
            return;
        }
        var response = ajaxSack.response.parseJSON();
        
        var lastState = response.lastState;
        var state = response.state;
        var awayMessage = response.awayMessage;
        
        if (state == 1 && lastState == 1 && titlebarBlinker == true) {
        	//available and default login
        	//si no tinc el focus (estem en blur) titlebarBlinker = true setState(3);
        	clearTimeout(idleTimer);
            setTimeout("setIdle()", 100);
        } else if (state == 3 && lastState == 1 && titlebarBlinker == false) {
        	//idle and default login
        	//si no tinc el blur (blur) titlebarBlinker = false sigin;
        	clearTimeout(idleTimer);
            setTimeout("setIdle()", 100);
        } else {
        	if (state == 0) {
				//login
		        $("curStatus").innerHTML = $("state6").innerHTML;
			} else if (state == 1) {
				//login
		        $("curStatus").innerHTML = $("state1").innerHTML;
			} else if (state == 2) {
				//away
				$("curStatus").innerHTML = '<img src="images/user_busy.gif" border="0" />&nbsp;' + awayMessage;
			} else if (state == 3) {
				//idle
		        $("curStatus").innerHTML = '<img src="images/user_idle.gif" border="0" />' + $("state7").innerHTML;
			} else {
				//login
		        $("curStatus").innerHTML = $("state1").innerHTML;
			}
        }
        
        var from, data;
        for (i = 0; i < response.numMessages; i++) {
            from = response.messages[i].sender;
            data = response.messages[i].message;
            if (!$(from + "_im")) {
                createIMWindow(from, from);
            } else {
                if (!IMWindows[from].isVisible()) {
                    IMWindows[from].show();
                    setTimeout("scrollToBottom('" + from + "_rcvd')", 125);
                }
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
                sendMessage(from);
            }
            if (Windows.getFocusedWindow().getId() != from + "_im" && pulsateTitles == true) {
                new Effect.Pulsate(from + "_im_top");
            }
            if (titlebarBlinker == true && useBlinker == true) {
                clearTimeout(blinkerTimer);
                setTimeout("titlebarBlink('" + from + "', \"" + data.replace(/\"/, "\"").replace(/<([^>]+)>/ig, "") + "\", 0)", blinkSpeed);
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
                addBuddyToList(name, buddy.username, group);
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
}

function sendMessage(winName) {
    var xhConn = new XHConn();
    var isBold = ($(winName + "_sendBox").style.fontWeight == "400" ? "false" : "true");
    var isItalic = ($(winName + "_sendBox").style.fontStyle == "normal" ? "false" : "true");
    var isUnderline = ($(winName + "_sendBox").style.textDecoration == "none" ? "false" : "true");
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
            rcvdBox.scrollTop = rcvdBox.scrollHeight - rcvdBox.clientHeight;
        } else {
            if (xh.responseText == "not_online") {
                var rcvdBox = $(winName + "_rcvd");
                rcvdBox.innerHTML = rcvdBox.innerHTML + "<span style=\"color:#FF0000\"><b>Error: Your message could not be sent because the recipient is not logged in.</b></span><br>";
                rcvdBox.scrollTop = rcvdBox.scrollHeight - $(winName + "_im").clientHeight;
            } else if (xh.responseText == "not_accept_chat") {
            	var rcvdBox = $(winName + "_rcvd");
                rcvdBox.innerHTML = rcvdBox.innerHTML + "<span style=\"color:#007200\"><b>Error: Your message could not be sent because the recipient is not accept chat.</b></span><br>";
                rcvdBox.scrollTop = rcvdBox.scrollHeight - $(winName + "_im").clientHeight;
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
}

function newIMWindow() {
    if ($("sendto").value.replace(/^\s*|\s*$/g, "").length > 0) {
        var toWhom = $("sendto").value;
        if (!$(toWhom + "_im")) {
            createIMWindow(toWhom, toWhom);
        } else {
            if (!IMWindows[toWhom].isVisible()) {
                IMWindows[toWhom].show();
                setTimeout("scrollToBottom('" + toWhom + "_rcvd')", 125);
            }
        }
        Dialog.closeInfo();
        IMWindows[toWhom].toFront();
        setTimeout("$('" + toWhom + "_sendBox').focus()", 125);
    } else {
        $("newim_error_msg").innerHTML = "Please enter a proper username!";
        $("newim_error_msg").show();
        Dialog.win.updateHeight();
    }
}

function createIMWindow(name, imTitle) {
    var imLeft = Math.round(Math.random() * (browserWidth() - 360)) + "px";
    var imTop = Math.round(Math.random() * (browserHeight() - 400)) + "px";
    IMWindows[name] = new Window(name + "_im", {className:"dialog", width:310, height:335, top:imTop, left:imLeft, zIndex:200, resizable:true, title:imTitle, draggable:true, minWidth:150, minHeight:75, showEffectOptions:{duration:0}, hideEffectOptions:{duration:0}});
    IMWindows[name].getContent().innerHTML = "<div class=\"rcvdMessages\" id=\"" + name + "_rcvd\"></div>" + "\n" + "<div class=\"imToolbar\" id=\"" + name + "_toolbar\"><img src=\"images/" + windowCSS + "/bold_off.png\" onclick=\"toggleBold('" + name + "');\" alt=\"Bold\" id=\"" + name + "_bold\" /> " + "<img src=\"images/" + windowCSS + "/italic_off.png\" onclick=\"toggleItalic('" + name + "');\" alt=\"Italic\" id=\"" + name + "_italic\" /> " + "<img src=\"images/" + windowCSS + "/underline_off.png\" onclick=\"toggleUnderline('" + name + "');\" alt=\"Underline\" id=\"" + name + "_underline\" /></div>" + "\n" + "<input type=\"text\" class=\"inputText\" id=\"" + name + "_sendBox\" onfocus=\"blinkerOn(false);\" onkeypress=\"keyHandler(event," + "'" + name + "'" + ");\" />";
    $(name + "_rcvd").style.height = (IMWindows[name].getSize().height - 73) + "px";
    $(name + "_rcvd").style.width = (IMWindows[name].getSize().width - 10) + "px";
    $(name + "_toolbar").style.top = (IMWindows[name].getSize().height - 43) + "px";
    $(name + "_toolbar").style.width = (IMWindows[name].getSize().width - 10) + "px";
    $(name + "_sendBox").style.top = (IMWindows[name].getSize().height - 15) + "px";
    $(name + "_sendBox").style.width = (IMWindows[name].getSize().width - 16) + "px";
    $(name + "_sendBox").style.fontWeight = "400";
    $(name + "_sendBox").style.fontStyle = "normal";
    $(name + "_sendBox").style.textDecoration = "none";
    IMWindows[name].show();
}

function keyHandler(e, name) {
    var asc = document.all ? event.keyCode : e.which;
    if (asc == 13) {
        sendMessage(name);
    }
    return asc != 13;
}

function loginHandler(e) {
    var asc = document.all ? event.keyCode : e.which;
    if (asc == 13) {
        login();
    }
    return asc != 13;
}

function destroyIMWindow(name) {
    var toKill = $(name);
    toKill.parentNode.removeChild(toKill);
}

function addBuddyToList(name, username, groupname) {
    var merda = $("dbuddy");
    merda.innerHTML += "<div id=\"" + username + "_blItem\" class=\"buddy\" onmousedown=\"return false;\" onselectstart=\"return false;\" onmouseover=\"selectBuddy(this, '" + username + "', true);\" onmouseout=\"selectBuddy(this, '" + username + "', false);\" ondblclick=\"onBuddyDblClick();\" style=\"overflow: hidden; text-overflow:ellipsis; border-top: 1px solid #CCCCCC; cursor: pointer;\"><img src=\"images/user_available.gif\" width=\"16\" height=\"16\" alt=\"\" id=\"" + username + "_blImg\">&nbsp;" + name + "</div>";
}

function addGroupToList(groupname) {
}

function selectBuddy(sel, username, selected) {
    if (selected === false) {
        sel.style.background = "#FFFFFF";
        sel.style.color = "#000";
        curSelected = "";
    } else {
        sel.style.background = "#e1ebf7";
        sel.style.color = "#000";
        curSelected = username;
    }
}

function onBuddyDblClick() {
    if (curSelected.length > 0) {
        if (!$(curSelected + "_im")) {
            createIMWindow(curSelected, curSelected);
        } else {
            if (!IMWindows[curSelected].isVisible()) {
                IMWindows[curSelected].show();
                setTimeout("scrollToBottom('" + curSelected + "_rcvd')", 125);
            }
        }
    }
}

function scrollToBottom(id) {
    $(id).scrollTop = $(id).scrollHeight - $(id).clientHeight;
}

function deleteBuddyFromList(username) {
    if (username.indexOf("_group") != -1) {
        deleteGroupFromList(username.substring(0, username.length - 6));
        return;
    }
    var ingroup = username.substring(username.indexOf("(") + 1, username.indexOf(")"));
    var usernam = username.replace(/\(.*\)/, "");
    var loc = username.substring(username.indexOf("[") + 1, username.indexOf("]"));
    usernam = usernam.replace(/\[.*\]/, "");
    var buddyToRmv = $(usernam + "_blItem");
    if (typeof (buddyToRmv) !== "undefined") {
        buddyToRmv.parentNode.removeChild(buddyToRmv);
        buddyList[ingroup].splice(loc, 1);
        saveBuddyList();
        Dialog.closeInfo();
    } else {
        $("deletebuddy_error_msg").innerHTML = "No such username on buddylist!";
        $("deletebuddy_error_msg").show();
        Dialog.win.updateHeight();
    }
}

function deleteGroupFromList(groupname) {
}

function addNewBuddyToList(username, groupname) {    
}

function toggleOffline() {
}

function trim(text) {
    if (text == null) {
        return null;
    }
    return text.replace(/^[ \t]+|[ \t]+$/g, "");
}

function saveBuddyList() {
}

function resetPass() {
}

function changePass() {
}

function setStatus(status, away_msg, state) {	
	if (status == 0) {
		//logout
		signout();
        $("curStatus").innerHTML = $("state" + state).innerHTML;
	} else if (status == 1) {
		//login
		login();
        $("curStatus").innerHTML = $("state" + state).innerHTML;
	} else if (status == 2) {
		//away
		setState(2, away_msg);
		$("curStatus").innerHTML = $("state" + state).innerHTML;
	} else if (status == 3) {
		//idle
		away_msg = $("state" + state).innerHTML;
		setState(3, away_msg);
        $("curStatus").innerHTML = '<img src="images/user_idle.gif" border="0" />' + $("state" + state).innerHTML;
	} else {
		//login
		login();
		away_msg = "";
        $("curStatus").innerHTML = $("state" + state).innerHTML;
	}
	
    $("statusList").style.display = "none";
}

function customAway() {
    $("curStatus").style.display = "none";
    $("customStatus").style.display = "block";
    $("customStatus").focus();
}

function processCustomAway(e) {
    var asc = document.all ? event.keyCode : e.which;
    if (asc == 13) {
        //isAway = 1;
        var away_msg = $("customStatus").value;
        setState(2, away_msg);
        $("curStatus").innerHTML = "<img src=\"images/user_busy.gif\" border=\"0\" />&nbsp;" + away_msg.substring(0, 30) + (away_msg.length > 30 ? "..." : "");
        $("curStatus").style.display = "block";
        $("customStatus").style.display = "none";
    }
    return asc != 13;
}

function toggleBold(name) {
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
}

function toggleItalic(name) {
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
}

function toggleUnderline(name) {
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
}

function setIdle() {
    if (titlebarBlinker == false) {    
    	idleTimer = setTimeout("signin()", 1000);
    } else {
    	idleTimer = setTimeout("setStatus(3, null, 7)", idle_time);
	}
}

function titlebarBlink(name, message, alter) {
    if (titlebarBlinker == false) {
        parent.frames.document.title = defaultTitle;
        return;
    }
    
    if (alter == 0) {
        parent.frames.document.title = name + "!";
        blinkerTimer = setTimeout("titlebarBlink('" + name + "', '" + message + "', 1)", 1000);
    } else {
        if (alter == 1) {
            parent.frames.document.title = "\"" + message.substring(0, 10) + (message.length > 10 ? "..." : "") + "\"";
            blinkerTimer = setTimeout("titlebarBlink('" + name + "', '" + message + "', 2)", 1000);
        } else {
            if (alter == 2) {
                parent.frames.document.title = defaultTitle;
                blinkerTimer = setTimeout("titlebarBlink('" + name + "', '" + message + "', 0)", 1000);
            }
        }
    }
}

function blinkerOn(onoff) {
    if (onoff == true) {
        titlebarBlinker = true;
    } else {
        titlebarBlinker = false;
    }
}

function browserWidth() {
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
}

function browserHeight() {
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
}

function randomNumber(max) {
    return Math.round(Math.random() * max);
}

function checkEmailAddr(email) {
    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (filter.test(email)) {
        return true;
    } else {
        return false;
    }
}

function getElementsByCondition(condition, container) {
    container = container || document;
    var all = container.all || container.getElementsByTagName("*");
    var arr = [];
    for (var k = 0; k < all.length; k++) {
        var elm = all[k];
        if (condition(elm, k)) {
            arr[arr.length] = elm;
        }
    }
    return arr;
}

function getElementsByClass(node, searchClass, tag) {
    var classElements = [];
    var els = node.getElementsByTagName(tag); // use "*" for all elements
    var elsLen = els.length;
    var pattern = new RegEx("\\b" + searchClass + "\\b");
    for (i = 0, j = 0; i < elsLen; i++) {
        if (pattern.test(els[i].className)) {
            classElements[j] = els[i];
            j++;
        }
    }
    return classElements;
}

function isDefined(variable) {
    return (typeof (variable) == "undefined") ? false : true;
}

// Returns true if the passed value is found in the
// array.  Returns false if it is not.
function inArray(arr, value) {
    var i;
    for (var group in arr) {
     // Matches identical (===), not just similar (==).
        for (i = 0; i < arr[group].length; i++) {
            if (arr[group][i] === value) {
                return true;
            }
        }
    }
    return false;
}
function addOption(selectbox, text, value) {
    var optn = document.createElement("OPTION");
    optn.text = text;
    optn.value = value;
    selectbox.options.add(optn);
}
function removeItems(array, item) {
    var i = 0;
    while (i < array.length) {
        if (array[i] == item) {
            array.splice(i, 1);
        } else {
            i++;
        }
    }
    return array;
}
function removeAllOptions(selectbox) {
    var i;
    for (i = selectbox.options.length - 1; i >= 0; i--) {
        selectbox.remove(i);
    }
}
function removeOptions(selectbox, val) {
    var i;
    for (i = selectbox.options.length - 1; i >= 0; i--) {
        if (selectbox.options[i].value === val) {
            selectbox.remove(i);
        }
    }
}
String.prototype.isAlphaNumeric = function () {
    return /^[a-z0-9_\d]+$/.test(this);
};
function Debug() {
    this.create = function () {
        createIMWindow("Debug", "Debug");
    };
    this.write = function (text) {
        $("Debug_rcvd").innerHTML += text + "<br>\n";
    };
}

