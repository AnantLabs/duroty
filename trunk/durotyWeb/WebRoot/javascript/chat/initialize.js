var Chat = new Chat();

window.onunload = function () {
    Chat.logout();
};

var internet_explorer = false;
if (navigator.userAgent.indexOf("MSIE") >= 0 && navigator.userAgent.indexOf("Opera") < 0) {
    internet_explorer = true;        
    window.onscroll = resizeRooms;	
	window.onresize = resizeRooms;
}

function resizeRooms() {
	for (var name in IMWindows) {
        if (typeof ($(name + "_im")) != "undefined") {
            if (IMWindows[name].isVisible()) {
            	IMWindows[name].resize();
            }
        }
    }
}

function initializeAIM() {
    Windows.addObserver({onResize:handleResize});
    Windows.addObserver({onClose:handleClose});
    Windows.addObserver({onMaximize:handleResize});
    
    if (isDefined(window.onfocus) && isDefined(window.onblur)) {
        window.onfocus = function () {
            titlebarBlinker = false;
        };
        window.onblur = function () {
            titlebarBlinker = true;
        };
    } else {
        document.onfocus = function () {
            titlebarBlinker = false;
        };
        document.onblur = function () {
            titlebarBlinker = true;
        };
    }
   
   //Quan fem un onblur podem activar una funció per a que controli el idle
    Chat.durotyLogin(true);
}

function handleResize(eventName, win) {
    if (win.getId().indexOf("_im") != -1) {    	
        var mastername = win.getId().replace(/_im/, "");
        $(mastername + "_rcvd").style.height = (win.getSize()["height"] - 73) + "px";
        $(mastername + "_rcvd").style.width = (win.getSize()["width"] - 10) + "px";
        $(mastername + "_toolbar").style.top = (win.getSize()["height"] - 43) + "px";
        $(mastername + "_toolbar").style.width = (win.getSize()["width"] - 10) + "px";
        $(mastername + "_sendBox").style.top = (win.getSize()["height"] - 15) + "px";
        $(mastername + "_sendBox").style.width = (win.getSize()["width"] - 16) + "px";
    }
}

function handleClose(eventName, win) {
    if (win.getId().indexOf("_im") == -1) {
        return;
    }
    
    var rcvdBox = $(win.getId().replace(/_im/, "") + "_rcvd");
    if (imHistory == true) {
        rcvdBox.innerHTML = "<span class=\"imHistory\">" + rcvdBox.innerHTML.replace(/\(Auto-Reply:\)/g, "Auto-Reply:").replace(/<(?![Bb][Rr] ?\/?)([^>]+)>/ig, "") + "</span>\n";
    } else {
        rcvdBox.innerHTML = "";
    }
}

function toggleStatusList() {
    if ($("statusList").style.display == "none") {
        $("statusList").style.left = 15 + getLeftPos($("statusSettings")) + "px";
        $("statusList").style.top = getTopPos($("statusSettings")) + 50 + "px";
        $("statusList").style.zIndex = 1200;
        $("statusList").style.display = "block";
    } else {
        $("statusList").style.display = "none";
    }
}

function getTopPos(inputObj) {
    var returnValue = inputObj.offsetTop;
    while ((inputObj = inputObj.offsetParent) != null) {
        if (inputObj.tagName != "HTML") {
            returnValue += inputObj.offsetTop;
        }
    }
    return returnValue;
}

function getLeftPos(inputObj) {
    var returnValue = inputObj.offsetLeft;
    while ((inputObj = inputObj.offsetParent) != null) {
        if (inputObj.tagName != "HTML") {
            returnValue += inputObj.offsetLeft;
        }
    }
    return returnValue;
}

function isDefined(variable) {
    return (typeof (variable) == "undefined") ? false : true;
}