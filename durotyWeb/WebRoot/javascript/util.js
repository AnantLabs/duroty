// This code was written by Tyler Akins and has been placed in the
// public domain.  It would be nice if you left this header intact.
// Base64 code from Tyler Akins -- http://rumkin.com
var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
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
    
    window.scroll(0,0);
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
function openwin(url, name, w, h) //opens a new window with parameter URL, Windowname (free choosable), width and height
{
    if (url != '#') {
        if(w==0) w=screen.availWidth-20;
        if(h==0) h=screen.availHeight-100;
        encodedurl = encodeUrl(url);
        workplace = window.open(encodedurl, name, 'toolbar=no,location=no,directories=no,status=yes,menubar=0,scrollbars=yes,resizable=yes,width=' + w + ',height=' + h);
        workplace.moveTo(0,0);
        if(workplace != null) {
              if (workplace.opener == null)
              {
                 workplace.opener = self;
              }
              workplace.focus();
        }        
    }
    //return workplace;
}	
function encodeUrl(url) {
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
    
    this.init = function() {
    	this.ajax = new sack();
    };
    
    this.setVar = function(name, value){
		this.ajax.setVar(name, value);
	};
    
    this.selectContents = function (_url, _obj, _name) {
    	showLoading();
    
    	var self = this;
    	
        this.obj = _obj;
        this.name = _name;
        //this.ajax = new sack();
        this.ajax.requestFile = _url;
        this.ajax.onCompletion = function () {
            self.selectContentsOnCompletion();
        };
        
        this.ajax.runAJAX();
    };
    
    this.selectContentsOnCompletion = function () {
        if (this.obj) {
            this.obj.innerHTML = this.ajax.response;
        } else if (this.name) {
            var aux = document.getElementById(this.name);
            if (aux) {
                aux.innerHTML = this.ajax.response;
            }
        }
        
        hideLoading();
    };
}

function showLoading() {
	document.body.style.cursor = "wait";
	
	document.getElementById("cornerLoading").style.display = "block";
}

function hideLoading() {
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
	text = replaceString(text,'&#33;','!');
	text = replaceString(text,'&quot;','"');
	text = replaceString(text,'&#35;','#');
	text = replaceString(text,'&#36;','$');
	text = replaceString(text,'&#37;','%');
	text = replaceString(text,'&amp;','&');
	text = replaceString(text,'&#39;','');
	text = replaceString(text,'&#40;','(');
	text = replaceString(text,'&#41;',')');
	text = replaceString(text,'&#42;','*');
	text = replaceString(text,'&#43;','+');
	text = replaceString(text,'&#44;',',');
	text = replaceString(text,'&#45;','-');
	text = replaceString(text,'&#46;','.');
	text = replaceString(text,'&#47;','/');
	text = replaceString(text,'&#58;',':');
	text = replaceString(text,'&#59;',';');
	text = replaceString(text,'&lt;','<');
	text = replaceString(text,'&#61;','=');
	text = replaceString(text,'&gt;','>');
	text = replaceString(text,'&amp;gt;','>');
	text = replaceString(text,'&#63;','?');
	text = replaceString(text,'&#64;','@');
	text = replaceString(text,'&#91;','[');
	text = replaceString(text,'&#92;','\\');
	text = replaceString(text,'&#93;',']');
	text = replaceString(text,'&#94;','^');
	text = replaceString(text,'&#96;','`');
	text = replaceString(text,'&#123;','{');
	text = replaceString(text,'&#124;','|');
	text = replaceString(text,'&#125;','}');
	text = replaceString(text,'&#126;','~');
	text = replaceString(text,'&#128;','?');
	text = replaceString(text,'&#129;','?');
	text = replaceString(text,'&#130;','?');
	text = replaceString(text,'&#131;','?');
	text = replaceString(text,'&#132;','?');
	text = replaceString(text,'&#133;','?');
	text = replaceString(text,'&#134;','?');
	text = replaceString(text,'&#135;','?');
	text = replaceString(text,'&#136;','?');
	text = replaceString(text,'&#137;','?');
	text = replaceString(text,'&#138;','?');
	text = replaceString(text,'&#139;','?');
	text = replaceString(text,'&#140;','?');
	text = replaceString(text,'&#141;','?');
	text = replaceString(text,'&#142;','?');
	text = replaceString(text,'&#143;','?');
	text = replaceString(text,'&#144;','?');
	text = replaceString(text,'&#145;','?');
	text = replaceString(text,'&#146;','?');
	text = replaceString(text,'&#147;','?');
	text = replaceString(text,'&#148;','?');
	text = replaceString(text,'&#149;','?');
	text = replaceString(text,'&#150;','?');
	text = replaceString(text,'&#151;','?');
	text = replaceString(text,'&#152;','?');
	text = replaceString(text,'&#153;','?');
	text = replaceString(text,'&#154;','?');
	text = replaceString(text,'&#155;','?');
	text = replaceString(text,'&#156;','?');
	text = replaceString(text,'&#157;','?');
	text = replaceString(text,'&#158;','?');
	text = replaceString(text,'&#159;','?');
	text = replaceString(text,'&nbsp;',' ');
	text = replaceString(text,'&iexcl;','¡');
	text = replaceString(text,'&cent;','¢');
	text = replaceString(text,'&pound;','£');
	text = replaceString(text,'&curren;','?');
	text = replaceString(text,'&yen;','¥');
	text = replaceString(text,'&brvbar;','?');
	text = replaceString(text,'&sect;','§');
	text = replaceString(text,'&uml;','?');
	text = replaceString(text,'&copy;','©');
	text = replaceString(text,'&ordf;','ª');
	text = replaceString(text,'&laquo;','«');
	text = replaceString(text,'&not;','¬');
	text = replaceString(text,'&shy;','­');
	text = replaceString(text,'&reg;','®');
	text = replaceString(text,'&macr;','¯');
	text = replaceString(text,'&deg;','°');
	text = replaceString(text,'&plusmn;','±');
	text = replaceString(text,'&sup2;','²');
	text = replaceString(text,'&sup3;','³');
	text = replaceString(text,'&acute;','?');
	text = replaceString(text,'&micro;','µ');
	text = replaceString(text,'&para;','¶');
	text = replaceString(text,'&middot;','·');
	text = replaceString(text,'&cedil;','?');
	text = replaceString(text,'&sup1;','¹');
	text = replaceString(text,'&ordm;','º');
	text = replaceString(text,'&raquo;','»');
	text = replaceString(text,'&frac14;','?');
	text = replaceString(text,'&frac12;','?');
	text = replaceString(text,'&frac34;','?');
	text = replaceString(text,'&iquest;','¿');
	text = replaceString(text,'&Agrave;','À');
	text = replaceString(text,'&Aacute;','Á');
	text = replaceString(text,'&Acirc;','Â');
	text = replaceString(text,'&Atilde;','Ã');
	text = replaceString(text,'&Auml;','Ä');
	text = replaceString(text,'&Aring;','Å');
	text = replaceString(text,'&AElig;','Æ');
	text = replaceString(text,'&Ccedil;','Ç');
	text = replaceString(text,'&Egrave;','È');
	text = replaceString(text,'&Eacute;','É');
	text = replaceString(text,'&Ecirc;','Ê');
	text = replaceString(text,'&Euml;','Ë');
	text = replaceString(text,'&Igrave;','Ì');
	text = replaceString(text,'&Iacute;','Í');
	text = replaceString(text,'&Icirc;','Î');
	text = replaceString(text,'&Iuml;','Ï');
	text = replaceString(text,'&ETH;','Ð');
	text = replaceString(text,'&Ntilde;','Ñ');
	text = replaceString(text,'&Ograve;','Ò');
	text = replaceString(text,'&Oacute;','Ó');
	text = replaceString(text,'&Ocirc;','Ô');
	text = replaceString(text,'&Otilde;','Õ');
	text = replaceString(text,'&Ouml;','Ö');
	text = replaceString(text,'&times;','×');
	text = replaceString(text,'&Oslash;','Ø');
	text = replaceString(text,'&Ugrave;','Ù');
	text = replaceString(text,'&Uacute;','Ú');
	text = replaceString(text,'&Ucirc;','Û');
	text = replaceString(text,'&Uuml;','Ü');
	text = replaceString(text,'&Yacute;','Ý');
	text = replaceString(text,'&THORN;','Þ');
	text = replaceString(text,'&szlig;','ß');
	text = replaceString(text,'&agrave;','à');
	text = replaceString(text,'&aacute;','á');
	text = replaceString(text,'&acirc;','â');
	text = replaceString(text,'&atilde;','ã');
	text = replaceString(text,'&auml;','ä');
	text = replaceString(text,'&aring;','å');
	text = replaceString(text,'&aelig;','æ');
	text = replaceString(text,'&ccedil;','ç');
	text = replaceString(text,'&egrave;','è');
	text = replaceString(text,'&eacute;','é');
	text = replaceString(text,'&ecirc;','ê');
	text = replaceString(text,'&euml;','ë');
	text = replaceString(text,'&igrave;','ì');
	text = replaceString(text,'&iacute;','í');
	text = replaceString(text,'&icirc;','î');
	text = replaceString(text,'&iuml;','ï');
	text = replaceString(text,'&eth;','ð');
	text = replaceString(text,'&ntilde;','ñ');
	text = replaceString(text,'&ograve;','ò');
	text = replaceString(text,'&oacute;','ó');
	text = replaceString(text,'&ocirc;','ô');
	text = replaceString(text,'&otilde;','õ');
	text = replaceString(text,'&ouml;','ö');
	text = replaceString(text,'&divide;','÷');
	text = replaceString(text,'&oslash;','ø');
	text = replaceString(text,'&ugrave;','ù');
	text = replaceString(text,'&uacute;','ú');
	text = replaceString(text,'&ucirc;','û');
	text = replaceString(text,'&uuml;','ü');
	text = replaceString(text,'&yacute;','ý');
	text = replaceString(text,'&thorn;','þ');
	text = replaceString(text,'&yuml;','ÿ');
	
	return text;
}

function replaceString(string,text,by) {
		// Replaces text with by in string
		if (!string) {
			return "";
		}
		
	    var strLength = string.length, txtLength = text.length;
	    if ((strLength == 0) || (txtLength == 0)) return string;
	
		if (!string.indexOf) {
			return;
		}
	
	    var i = string.indexOf(text);
	    if ((!i) && (text != string.substring(0,txtLength))) return string;
	    if (i == -1) return string;
	
	    var newstr = string.substring(0,i) + by;
	
	    if (i+txtLength < strLength)
	        newstr += replaceString(string.substring(i+txtLength,strLength),text,by);
	        
	    return newstr;
}

function trim(str) {
    return str.replace(/^(\s+)?(\S*)(\s+)?$/, '$2');
}

function ltrim(str) {
    return str.replace(/^\s*/, '');
}

function rtrim(str) {
    return str.replace(/\s*$/, '');
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
			recursiveD(current, px)
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
			recursiveD(current, px)
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