// This code was written by Tyler Akins and has been placed in the
// public domain.  It would be nice if you left this header intact.
// Base64 code from Tyler Akins -- http://rumkin.com
var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
var loadingTitle = parent.frames.document.title;
function encode64(input) {
    var output = "";
    var chr1, chr2, chr3;
    var enc1, enc2, enc3, enc4;
    var i = 0;
    do {
        chr1 = input.charCodeAt(i++);
        chr2 = input.charCodeAt(i++);
        chr3 = input.charCodeAt(i++);
        enc1 = chr1 >> 2;
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
        enc4 = chr3 & 63;
        if (isNaN(chr2)) {
            enc3 = enc4 = 64;
        } else {
            if (isNaN(chr3)) {
                enc4 = 64;
            }
        }
        output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) + keyStr.charAt(enc3) + keyStr.charAt(enc4);
    } while (i < input.length);
    return output;
}
function decode64(input) {
    var output = "";
    var chr1, chr2, chr3;
    var enc1, enc2, enc3, enc4;
    var i = 0;

   // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
    input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
    do {
        enc1 = keyStr.indexOf(input.charAt(i++));
        enc2 = keyStr.indexOf(input.charAt(i++));
        enc3 = keyStr.indexOf(input.charAt(i++));
        enc4 = keyStr.indexOf(input.charAt(i++));
        chr1 = (enc1 << 2) | (enc2 >> 4);
        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
        chr3 = ((enc3 & 3) << 6) | enc4;
        output = output + String.fromCharCode(chr1);
        if (enc3 != 64) {
            output = output + String.fromCharCode(chr2);
        }
        if (enc4 != 64) {
            output = output + String.fromCharCode(chr3);
        }
    } while (i < input.length);
    return output;
}
function maxLengthTextArea(_textarea, _info) {
    var mlength = _textarea.getAttribute ? parseInt(_textarea.getAttribute("maxlength")) : "";
    if (_textarea.getAttribute && _textarea.value.length > mlength) {
        _textarea.value = _textarea.value.substring(0, mlength);
    }
    var divInfo = document.getElementById(_info);
    if (_textarea.getAttribute && divInfo) {
        divInfo.innerHTML = mlength - _textarea.value.length;
    }
}
function showHideElement(id) {
    var element = document.getElementById(id);
    if (element != null) {
        var cl = element.className;
        if (cl == "hide") {
            element.className = "show";
        } else {
            element.className = "hide";
        }
    }
}
function showHideElementByObject(element) {
    if (element != null) {
        var cl = element.className;
        if (cl == "hide") {
            element.className = "show";
        } else {
            element.className = "hide";
        }
    }
}
function hideElement(id) {
    var element = document.getElementById(id);
    if (element != null) {
        element.className = "hide";
    }
}
function showHideMenu(id, parent, idItem, parentItem) {
    // clear out the old selected menu item
    var contents = document.getElementById(parent);
    if (contents) {
        contents.className = "hide";
        contents.className = "show";
        for (var i = 0; i < contents.childNodes.length; i++) {
            var currentElement = contents.childNodes[i];
		// see if this is a DOM Element node
            if (currentElement.nodeType == 1) {
			// clear any class name
                currentElement.className = "hide";
            }
        }
    }
    var selectedContents = document.getElementById(id);
    if (selectedContents) {
        selectedContents.className = "show";
    }
    var menu = document.getElementById("menu");
    var items = menu.getElementsByTagName("DIV");
    for (var i = 0; i < items.length; i++) {
        if (items[i].className == "itemSelected") {
            items[i].className = "item";
        }
    }
    var selectedItem = document.getElementById(idItem);
    if (selectedItem) {
        selectedItem.className = "itemSelected";
    }
    window.scroll(0, 0);
}
//opens a new window with parameter URL, Windowname (free choosable), width and height
function openDialog(url, name, w, h, centered) {
    if (url != "#") {
        var left = 10;
        var top = 10;
        if (centered) {
            left = parseInt((screen.availWidth / 2) - (w / 2));
            top = parseInt((screen.availHeight / 2) - (h / 2));
        }
        if (w == 0) {
            w = screen.availWidth - 20;
        }
        if (h == 0) {
            h = screen.availHeight - 100;
        }
        var encodedurl = encodeUrl(url);
        var workplace = window.open(encodedurl, name, "toolbar=no,location=,directories=no,status=yes,menubar=1,scrollbars=yes,resizable=yes,width=" + w + ",height=" + h + ",left=" + left + ",top=" + top + ",screenX=" + left + ",screenY=" + top);
        if (workplace != null) {
            if (workplace.opener == null) {
                workplace.opener = self;
            }
            workplace.focus();
        }
    }
    return workplace;
}
function openwin(url, name, w, h) {
    if (url != "#") {
        if (w == 0) {
            w = screen.availWidth - 20;
        }
        if (h == 0) {
            h = screen.availHeight - 100;
        }
        encodedurl = encodeUrl(url);
        workplace = window.open(encodedurl, name, "toolbar=no,location=no,directories=no,status=yes,menubar=0,scrollbars=yes,resizable=yes,width=" + w + ",height=" + h);
        //workplace.moveTo(0, 0);
        if (workplace != null) {
            if (workplace.opener == null) {
                workplace.opener = self;
            }
            workplace.focus();
        }
    }
    //return workplace;
}
function encodeUrl(url) {
	return url;

    encodedurl = url;
    asteriskIdx = url.indexOf("?");
    if (asteriskIdx > -1 && asteriskIdx < (url.length - 1)) {
        encodedurl = url.substring(0, asteriskIdx) + "?" + encodeURIComponent(url.substring(asteriskIdx + 1));
    }
    return encodedurl;
}
function AjaxContents() {
    this.ajax = false;
    this.obj = false;
    this.name = false;
    this.toShow = false;
    this.toHide = false;
    this.init = function () {
        this.ajax = new sack();
    };
    this.setVar = function (name, value) {
        this.ajax.setVar(name, value);
    };
    this.selectContents = function (_url, _obj, _name, _toShow, _toHide) {
        showLoading();
        var self = this;
        this.obj = _obj;
        this.name = _name;
        this.toShow = _toShow;
        this.toHide = _toHide;

        this.ajax.requestFile = _url;
        this.ajax.onCompletion = function () {
            self.selectContentsOnCompletion();
        };
        this.ajax.runAJAX();
    };
    this.selectContentsOnCompletion = function () {    
    	try {	        
	        if (Mail) {
	        	Mail.removeTinyMce("taBody");
				Mail.removeTinyMce("taReplyBody");
	        }
        } catch (e) {
        }
    
    	if (this.toHide && this.toHide.length > 0) {
        	for (var i = 0; i < this.toHide.length; i++) {        		
        		this.toHide[i].className = "hide";
        		if (this.toHide[i].id == "messages") {
	        		this.toHide[i].innerHTML = "";
	        	} else if (this.toHide[i].id == "message") {	        		
	        		this.toHide[i].innerHTML = "";
	        	}	        	
        	}
        }
        
        if (this.toShow) {
        	this.toShow.className = "show";
        }
    
        if (this.obj) {
            this.obj.innerHTML = this.ajax.response;
        } else {
            if (this.name) {
                var aux = document.getElementById(this.name);
                if (aux) {
                    aux.innerHTML = this.ajax.response;
                }
            }
        }
        
        try {
	        if (Mail && Mail.hash != null && Mail.hash.length > 0) {
	        	for (var i = 0; i < Mail.hash.length; i++) {
	        		var item = Mail.hash.getAtPos(i);
	        		var checkbox = document.getElementById(item);
	        		if (checkbox) {
	        			checkbox.checked = true;
	        		}
	        	}
	        }
        } catch (e) {
        }
        
        
        
        hideLoading();
    };
}
function showLoading() {
	parent.frames.document.title = loadingTitle + " - " + document.getElementById("cornerLoading").innerHTML;

    document.body.style.cursor = "wait";
    document.getElementById("cornerLoading").style.display = "block";
}
function hideLoading() {
	parent.frames.document.title = loadingTitle;

    document.body.style.cursor = "default";
    document.getElementById("cornerLoading").style.display = "none";
}
function hideInfo() {
    var info = document.getElementById("divInfo");
    if (info != null) {
        info.className = "hide";
    }
}
function decodeEntities(text) {
    if (text == null) {
        return;
    }
    if (!text) {
        return;
    }
    text = replaceString(text, "&#33;", "!");
    text = replaceString(text, "&quot;", "\"");
    text = replaceString(text, "&#35;", "#");
    text = replaceString(text, "&#36;", "$");
    text = replaceString(text, "&#37;", "%");
    text = replaceString(text, "&amp;", "&");
    text = replaceString(text, "&#39;", "");
    text = replaceString(text, "&#40;", "(");
    text = replaceString(text, "&#41;", ")");
    text = replaceString(text, "&#42;", "*");
    text = replaceString(text, "&#43;", "+");
    text = replaceString(text, "&#44;", ",");
    text = replaceString(text, "&#45;", "-");
    text = replaceString(text, "&#46;", ".");
    text = replaceString(text, "&#47;", "/");
    text = replaceString(text, "&#58;", ":");
    text = replaceString(text, "&#59;", ";");
    text = replaceString(text, "&lt;", "<");
    text = replaceString(text, "&#61;", "=");
    text = replaceString(text, "&gt;", ">");
    text = replaceString(text, "&amp;gt;", ">");
    text = replaceString(text, "&#63;", "?");
    text = replaceString(text, "&#64;", "@");
    text = replaceString(text, "&#91;", "[");
    text = replaceString(text, "&#92;", "\\");
    text = replaceString(text, "&#93;", "]");
    text = replaceString(text, "&#94;", "^");
    text = replaceString(text, "&#96;", "`");
    text = replaceString(text, "&#123;", "{");
    text = replaceString(text, "&#124;", "|");
    text = replaceString(text, "&#125;", "}");
    text = replaceString(text, "&#126;", "~");
    text = replaceString(text, "&#128;", "?");
    text = replaceString(text, "&#129;", "?");
    text = replaceString(text, "&#130;", "?");
    text = replaceString(text, "&#131;", "?");
    text = replaceString(text, "&#132;", "?");
    text = replaceString(text, "&#133;", "?");
    text = replaceString(text, "&#134;", "?");
    text = replaceString(text, "&#135;", "?");
    text = replaceString(text, "&#136;", "?");
    text = replaceString(text, "&#137;", "?");
    text = replaceString(text, "&#138;", "?");
    text = replaceString(text, "&#139;", "?");
    text = replaceString(text, "&#140;", "?");
    text = replaceString(text, "&#141;", "?");
    text = replaceString(text, "&#142;", "?");
    text = replaceString(text, "&#143;", "?");
    text = replaceString(text, "&#144;", "?");
    text = replaceString(text, "&#145;", "?");
    text = replaceString(text, "&#146;", "?");
    text = replaceString(text, "&#147;", "?");
    text = replaceString(text, "&#148;", "?");
    text = replaceString(text, "&#149;", "?");
    text = replaceString(text, "&#150;", "?");
    text = replaceString(text, "&#151;", "?");
    text = replaceString(text, "&#152;", "?");
    text = replaceString(text, "&#153;", "?");
    text = replaceString(text, "&#154;", "?");
    text = replaceString(text, "&#155;", "?");
    text = replaceString(text, "&#156;", "?");
    text = replaceString(text, "&#157;", "?");
    text = replaceString(text, "&#158;", "?");
    text = replaceString(text, "&#159;", "?");
    text = replaceString(text, "&nbsp;", "\xa0");
    text = replaceString(text, "&iexcl;", "\xa1");
    text = replaceString(text, "&cent;", "\xa2");
    text = replaceString(text, "&pound;", "\xa3");
    text = replaceString(text, "&curren;", "?");
    text = replaceString(text, "&yen;", "\xa5");
    text = replaceString(text, "&brvbar;", "?");
    text = replaceString(text, "&sect;", "\xa7");
    text = replaceString(text, "&uml;", "?");
    text = replaceString(text, "&copy;", "\xa9");
    text = replaceString(text, "&ordf;", "\xaa");
    text = replaceString(text, "&laquo;", "\xab");
    text = replaceString(text, "&not;", "\xac");
    text = replaceString(text, "&shy;", "");
    text = replaceString(text, "&reg;", "\xae");
    text = replaceString(text, "&macr;", "\xaf");
    text = replaceString(text, "&deg;", "\xb0");
    text = replaceString(text, "&plusmn;", "\xb1");
    text = replaceString(text, "&sup2;", "\xb2");
    text = replaceString(text, "&sup3;", "\xb3");
    text = replaceString(text, "&acute;", "?");
    text = replaceString(text, "&micro;", "\xb5");
    text = replaceString(text, "&para;", "\xb6");
    text = replaceString(text, "&middot;", "\xb7");
    text = replaceString(text, "&cedil;", "?");
    text = replaceString(text, "&sup1;", "\xb9");
    text = replaceString(text, "&ordm;", "\xba");
    text = replaceString(text, "&raquo;", "\xbb");
    text = replaceString(text, "&frac14;", "?");
    text = replaceString(text, "&frac12;", "?");
    text = replaceString(text, "&frac34;", "?");
    text = replaceString(text, "&iquest;", "\xbf");
    text = replaceString(text, "&Agrave;", "\xc0");
    text = replaceString(text, "&Aacute;", "\xc1");
    text = replaceString(text, "&Acirc;", "\xc2");
    text = replaceString(text, "&Atilde;", "\xc3");
    text = replaceString(text, "&Auml;", "\xc4");
    text = replaceString(text, "&Aring;", "\xc5");
    text = replaceString(text, "&AElig;", "\xc6");
    text = replaceString(text, "&Ccedil;", "\xc7");
    text = replaceString(text, "&Egrave;", "\xc8");
    text = replaceString(text, "&Eacute;", "\xc9");
    text = replaceString(text, "&Ecirc;", "\xca");
    text = replaceString(text, "&Euml;", "\xcb");
    text = replaceString(text, "&Igrave;", "\xcc");
    text = replaceString(text, "&Iacute;", "\xcd");
    text = replaceString(text, "&Icirc;", "\xce");
    text = replaceString(text, "&Iuml;", "\xcf");
    text = replaceString(text, "&ETH;", "\xd0");
    text = replaceString(text, "&Ntilde;", "\xd1");
    text = replaceString(text, "&Ograve;", "\xd2");
    text = replaceString(text, "&Oacute;", "\xd3");
    text = replaceString(text, "&Ocirc;", "\xd4");
    text = replaceString(text, "&Otilde;", "\xd5");
    text = replaceString(text, "&Ouml;", "\xd6");
    text = replaceString(text, "&times;", "\xd7");
    text = replaceString(text, "&Oslash;", "\xd8");
    text = replaceString(text, "&Ugrave;", "\xd9");
    text = replaceString(text, "&Uacute;", "\xda");
    text = replaceString(text, "&Ucirc;", "\xdb");
    text = replaceString(text, "&Uuml;", "\xdc");
    text = replaceString(text, "&Yacute;", "\xdd");
    text = replaceString(text, "&THORN;", "\xde");
    text = replaceString(text, "&szlig;", "\xdf");
    text = replaceString(text, "&agrave;", "\xe0");
    text = replaceString(text, "&aacute;", "\xe1");
    text = replaceString(text, "&acirc;", "\xe2");
    text = replaceString(text, "&atilde;", "\xe3");
    text = replaceString(text, "&auml;", "\xe4");
    text = replaceString(text, "&aring;", "\xe5");
    text = replaceString(text, "&aelig;", "\xe6");
    text = replaceString(text, "&ccedil;", "\xe7");
    text = replaceString(text, "&egrave;", "\xe8");
    text = replaceString(text, "&eacute;", "\xe9");
    text = replaceString(text, "&ecirc;", "\xea");
    text = replaceString(text, "&euml;", "\xeb");
    text = replaceString(text, "&igrave;", "\xec");
    text = replaceString(text, "&iacute;", "\xed");
    text = replaceString(text, "&icirc;", "\xee");
    text = replaceString(text, "&iuml;", "\xef");
    text = replaceString(text, "&eth;", "\xf0");
    text = replaceString(text, "&ntilde;", "\xf1");
    text = replaceString(text, "&ograve;", "\xf2");
    text = replaceString(text, "&oacute;", "\xf3");
    text = replaceString(text, "&ocirc;", "\xf4");
    text = replaceString(text, "&otilde;", "\xf5");
    text = replaceString(text, "&ouml;", "\xf6");
    text = replaceString(text, "&divide;", "\xf7");
    text = replaceString(text, "&oslash;", "\xf8");
    text = replaceString(text, "&ugrave;", "\xf9");
    text = replaceString(text, "&uacute;", "\xfa");
    text = replaceString(text, "&ucirc;", "\xfb");
    text = replaceString(text, "&uuml;", "\xfc");
    text = replaceString(text, "&yacute;", "\xfd");
    text = replaceString(text, "&thorn;", "\xfe");
    text = replaceString(text, "&yuml;", "\xff");
    return text;
}
function replaceString(string, text, by) {
		// Replaces text with by in string
    if (!string) {
        return "";
    }
    var strLength = string.length, txtLength = text.length;
    if ((strLength == 0) || (txtLength == 0)) {
        return string;
    }
    if (!string.indexOf) {
        return;
    }
    var i = string.indexOf(text);
    if ((!i) && (text != string.substring(0, txtLength))) {
        return string;
    }
    if (i == -1) {
        return string;
    }
    var newstr = string.substring(0, i) + by;
    if (i + txtLength < strLength) {
        newstr += replaceString(string.substring(i + txtLength, strLength), text, by);
    }
    return newstr;
}
function trim(str) {
    return str.replace(/^(\s+)?(\S*)(\s+)?$/, "$2");
}
function ltrim(str) {
    return str.replace(/^\s*/, "");
}
function rtrim(str) {
    return str.replace(/\s*$/, "");
}
function increaseFont(id) {
    var divId = document.getElementById(id);
    if (divId != null) {
        var aux = divId.style.fontSize;
        if (aux == null || aux == "") {
            aux = "13px";
        }
        divId.style.fontSize = parseInt(aux) + 2 + "px";
        recursiveI(divId, parseInt(aux));
    }
}
function recursiveI(divId, px) {
    for (var i = 0; i < divId.childNodes.length; i++) {
        var current = divId.childNodes[i];
		// see if this is a DOM Element node
        if (current.nodeType == 1) {
			// clear any class name
            current.style.fontSize = px + 2 + "px";
            recursiveD(current, px);
        }
    }
}
function decreaseFont(id) {
    var divId = document.getElementById(id);
    if (divId != null) {
        var aux = divId.style.fontSize;
        if (aux == null || aux == "") {
            aux = "13px";
        }
        divId.style.fontSize = parseInt(aux) - 2 + "px";
        recursiveD(divId, parseInt(aux));
    }
}
function recursiveD(divId, px) {
    for (var i = 0; i < divId.childNodes.length; i++) {
        var current = divId.childNodes[i];
		// see if this is a DOM Element node
        if (current.nodeType == 1) {
			// clear any class name
            current.style.fontSize = px - 2 + "px";
            recursiveD(current, px);
        }
    }
}
function startsWith(text, prefix) {
    return (text.indexOf(prefix) === 0);
}
function endsWith(text, suffix) {
    var startPos = text.length - suffix.length;
    if (startPos < 0) {
        return false;
    }
    return (text.lastIndexOf(suffix, startPos) == startPos);
}

function checkEmailAddr(email) {
    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (filter.test(email)) {
        return true;
    } else {
        return false;
    }
}

function randomNumber(max) {
    return Math.round(Math.random() * max);
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

function isDefined(variable) {
    return (typeof (variable) == "undefined") ? false : true;
}