
/************************************************************************************************************
	(C) www.dhtmlgoodies.com, April 2006
	
	This is a script from www.dhtmlgoodies.com. You will find this and a lot of other scripts at our website.	
	
	Terms of use:
	You are free to use this script as long as the copyright message is kept intact. However, you may not
	redistribute, sell or repost it without our permission.
	
	Thank you!
	
	www.dhtmlgoodies.com
	Alf Magne Kalleland
	
	************************************************************************************************************/
var ajaxBox_offsetX = 0;
var ajaxBox_offsetY = 0;
var minimumLettersBeforeLookup = 2;	// Number of letters entered before a lookup is performed.
var ajax_list_objects = new Array();
var ajax_list_cachedLists = new Array();
var ajax_list_activeInput = false;
var ajax_list_activeItem;
var ajax_list_optionDivFirstItem = false;
var ajax_list_currentLetters = new Array();
var ajax_optionDiv = false;
var ajax_optionDiv_iframe = false;
var ajax_list_MSIE = false;
if (navigator.userAgent.indexOf("MSIE") >= 0 && navigator.userAgent.indexOf("Opera") < 0) {
    ajax_list_MSIE = true;
}
function ajax_getTopPos(inputObj) {
	try {
	    var returnValue = inputObj.offsetTop;
	    while ((inputObj = inputObj.offsetParent) != null) {
	        returnValue += inputObj.offsetTop;
	    }
	    return returnValue;
    } catch (e) {
    	return 0;
    }
}
function ajax_list_cancelEvent() {
    return false;
}
function ajax_getLeftPos(inputObj) {
	try {
	    var returnValue = inputObj.offsetLeft;
	    while ((inputObj = inputObj.offsetParent) != null) {
	        returnValue += inputObj.offsetLeft;
	    }
   	 	return returnValue;
    } catch (e) {
    	return 0;
    }
}
function ajax_option_setValue(e, inputObj) {
    if (!inputObj) {
        inputObj = this;
    }
    var tmpValue = inputObj.innerHTML;
    if (ajax_list_MSIE) {
        tmpValue = inputObj.innerText;
    } else {
        tmpValue = inputObj.textContent;
    }
    if (!tmpValue) {
        tmpValue = inputObj.innerHTML;
    }    
    
    tmpValue = decodeEntities(tmpValue);
    
    //Aquí al lloro ja que només s'utilitza pel mail suggest contacts
    if (endsWith(tmpValue, " <GROUP>")) {
    	var emails = replaceString(tmpValue, " <GROUP>", "");
    	if (groups[emails]) {
    		tmpValue = groups[emails];
    	}
    } else {
	    tmpValue = tmpValue + ", ";
    }
    
    var aux = "";
	if (ajax_list_activeInput.value != null && ajax_list_activeInput.value != "") {
		var index = ajax_list_activeInput.value.lastIndexOf(', ');
		
		if (index > -1) {
			aux = ajax_list_activeInput.value.substring(0, index);
			ajax_list_activeInput.value = aux + ", " + tmpValue;
		} else {
			ajax_list_activeInput.value = tmpValue;
		}
	} else {
		ajax_list_activeInput.value = tmpValue;
	}
	
    ajax_options_hide();
}
function ajax_options_hide() {
    ajax_optionDiv.style.display = "none";
    if (ajax_optionDiv_iframe) {
        ajax_optionDiv_iframe.style.display = "none";
    }
}
function ajax_options_rollOverActiveItem(item, fromKeyBoard) {
    if (ajax_list_activeItem) {
        ajax_list_activeItem.className = "optionDiv";
    }
    item.className = "optionDivSelected";
    ajax_list_activeItem = item;
    if (fromKeyBoard) {
        if (ajax_list_activeItem.offsetTop > ajax_optionDiv.offsetHeight) {
            ajax_optionDiv.scrollTop = ajax_list_activeItem.offsetTop - ajax_optionDiv.offsetHeight + ajax_list_activeItem.offsetHeight + 2;
        }
        if (ajax_list_activeItem.offsetTop < ajax_optionDiv.scrollTop) {
            ajax_optionDiv.scrollTop = 0;
        }
    }
}
function ajax_option_list_buildList(letters, paramToExternalFile) {
    ajax_optionDiv.innerHTML = "";
    ajax_list_activeItem = false;
    if (ajax_list_cachedLists[paramToExternalFile][letters].length <= 1) {
        ajax_options_hide();
        return;
    }
    ajax_list_optionDivFirstItem = false;
    var optionsAdded = false;
    for (var no = 0; no < ajax_list_cachedLists[paramToExternalFile][letters].length; no++) {
        if (ajax_list_cachedLists[paramToExternalFile][letters][no].length == 0) {
            continue;
        }
        optionsAdded = true;
        var div = document.createElement("DIV");
        var items = ajax_list_cachedLists[paramToExternalFile][letters][no].split(/###/gi);
        div.innerHTML = items[items.length - 1];
        div.id = items[0];
        div.className = "optionDiv";
        div.onmouseover = function () {
            ajax_options_rollOverActiveItem(this, false);
        };
        div.onclick = ajax_option_setValue;
        if (!ajax_list_optionDivFirstItem) {
            ajax_list_optionDivFirstItem = div;
        }
        ajax_optionDiv.appendChild(div);
    }
    if (optionsAdded) {
        ajax_optionDiv.style.display = "block";
        if (ajax_optionDiv_iframe) {
            ajax_optionDiv_iframe.style.display = "inline";            
        }
    }
}
function ajax_option_list_showContent(ajaxIndex, inputObj, paramToExternalFile) {
    var letters = inputObj.value;
    var content = ajax_list_objects[ajaxIndex].response;
    var elements = content.split("|");
    ajax_list_cachedLists[paramToExternalFile][letters] = elements;
    ajax_option_list_buildList(letters, paramToExternalFile);
}
function ajax_option_resize(inputObj) {
    ajax_optionDiv.style.top = (ajax_getTopPos(inputObj) + inputObj.offsetHeight + ajaxBox_offsetY) + "px";
    ajax_optionDiv.style.left = (ajax_getLeftPos(inputObj) + ajaxBox_offsetX) + "px";
    if (ajax_optionDiv_iframe) {
        ajax_optionDiv_iframe.style.left = ajax_optionDiv.style.left;
        ajax_optionDiv_iframe.style.top = ajax_optionDiv.style.top;
    }
}
function ajax_showOptions(ajax_list_externalFile, inputObj, paramToExternalFile, e) {
    if (ajax_list_currentLetters[inputObj.name] == inputObj.value) {
        return;
    }
    if (!ajax_list_cachedLists[paramToExternalFile]) {
        ajax_list_cachedLists[paramToExternalFile] = new Array();
    }
    
    //calcular
	var inp_value = inputObj.value.toLowerCase();
	var cpos = get_caret_pos(inputObj);
    var p = inp_value.lastIndexOf(',', cpos-1);
    var q = inp_value.substring(p+1, cpos);

    // trim query string
    q = q.replace(/(^\s+|\s+$)/g, '').toLowerCase();
    
    ajax_list_currentLetters[inputObj.name] = inputObj.value;

    if (!ajax_optionDiv) {
        ajax_optionDiv = document.createElement("DIV");
        ajax_optionDiv.id = "ajax_listOfOptions";
        document.body.appendChild(ajax_optionDiv);
        if (ajax_list_MSIE) {
            ajax_optionDiv_iframe = document.createElement("iframe");
            ajax_optionDiv_iframe.frameborder = "0";
            ajax_optionDiv_iframe.style.width = ajax_optionDiv.clientWidth + "px";
            ajax_optionDiv_iframe.style.height = ajax_optionDiv.clientHeight + "px";
            ajax_optionDiv_iframe.id = "ajax_listOfOptions_iframe";
            ajax_optionDiv_iframe.style.position = "absolute";
            ajax_optionDiv_iframe.style.display = "none";
            document.body.appendChild(ajax_optionDiv_iframe);
        }
        var allInputs = document.getElementsByTagName("INPUT");
        for (var no = 0; no < allInputs.length; no++) {
            if (!allInputs[no].onkeyup) {
                allInputs[no].onfocus = ajax_options_hide;
            }
        }
        var allSelects = document.getElementsByTagName("SELECT");
        for (var no = 0; no < allSelects.length; no++) {
            allSelects[no].onfocus = ajax_options_hide;
        }
        var oldonkeydown = document.body.onkeydown;
        if (typeof oldonkeydown != "function") {
            document.body.onkeydown = ajax_option_keyNavigation;
        } else {
            document.body.onkeydown = function () {
                oldonkeydown();
                ajax_option_keyNavigation();
            };
        }
        var oldonresize = document.body.onresize;
        if (typeof oldonresize != "function") {
            document.body.onresize = function () {
                ajax_option_resize(inputObj);
            };
        } else {
            document.body.onresize = function () {
                oldonresize();
                ajax_option_resize(inputObj);
            };
        }
    }
    if (q.length < minimumLettersBeforeLookup) {
        ajax_options_hide();
        return;
    }
    ajax_optionDiv.style.top = (ajax_getTopPos(inputObj) + inputObj.offsetHeight + ajaxBox_offsetY) + "px";
    ajax_optionDiv.style.left = (ajax_getLeftPos(inputObj) + ajaxBox_offsetX) + "px";
    if (ajax_optionDiv_iframe) {
        ajax_optionDiv_iframe.style.left = ajax_optionDiv.style.left;
        ajax_optionDiv_iframe.style.top = ajax_optionDiv.style.top;
    }
    ajax_list_activeInput = inputObj;
    ajax_optionDiv.onselectstart = ajax_list_cancelEvent;
    if (ajax_list_cachedLists[paramToExternalFile][inputObj.value]) {
        ajax_option_list_buildList(inputObj.value, paramToExternalFile);
    } else {
        ajax_optionDiv.innerHTML = "";
        var ajaxIndex = ajax_list_objects.length;
        ajax_list_objects[ajaxIndex] = new sack();
        var url = ajax_list_externalFile + "?" + paramToExternalFile + "=1&token=" + q;
        ajax_list_objects[ajaxIndex].requestFile = url;	// Specifying which file to get
        ajax_list_objects[ajaxIndex].onCompletion = function () {
            ajax_option_list_showContent(ajaxIndex, inputObj, paramToExternalFile);
        };	// Specify function that will be executed after file has been found
        ajax_list_objects[ajaxIndex].runAJAX();		// Execute AJAX function		
    }
}
function ajax_option_keyNavigation(e) {
    if (document.all) {
        e = event;
    }
    if (!ajax_optionDiv) {
        return;
    }
    if (ajax_optionDiv.style.display == "none") {
        return;
    }
    if (e.keyCode == 38) {	// Up arrow
        if (!ajax_list_activeItem) {
            return;
        }
        if (ajax_list_activeItem && !ajax_list_activeItem.previousSibling) {
            return;
        }
        ajax_options_rollOverActiveItem(ajax_list_activeItem.previousSibling, true);
    }
    if (e.keyCode == 40) {	// Down arrow
        if (!ajax_list_activeItem) {
            ajax_options_rollOverActiveItem(ajax_list_optionDivFirstItem, true);
        } else {
            if (!ajax_list_activeItem.nextSibling) {
                return;
            }
            ajax_options_rollOverActiveItem(ajax_list_activeItem.nextSibling, true);
        }
    }
    if (e.keyCode == 13 || e.keyCode == 9) {	// Enter key or tab key
        if (ajax_list_activeItem && ajax_list_activeItem.className == "optionDivSelected") {
            ajax_option_setValue(false, ajax_list_activeItem);
        }
        if (e.keyCode == 13) {
            return false;
        } else {
            return true;
        }
    }
    if (e.keyCode == 27) {	// Escape key
        ajax_options_hide();
    }
}
function get_caret_pos(obj) {
    if (typeof (obj.selectionEnd) != "undefined") {
        return obj.selectionEnd;
    } else {
        if (document.selection && document.selection.createRange) {
            var range = document.selection.createRange();
            if (range.parentElement() != obj) {
                return 0;
            }
            var gm = range.duplicate();
            if (obj.tagName == "TEXTAREA") {
                gm.moveToElementText(obj);
            } else {
                gm.expand("textedit");
            }
            gm.setEndPoint("EndToStart", range);
            var p = gm.text.length;
            return p <= obj.value.length ? p : -1;
        } else {
            return obj.value.length;
        }
    }
}
