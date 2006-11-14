var width = 275;
var height = 300;
var zIndex = 0;
var postLeft = 0;

function ChatRoom() {
	this.frame = false;
	this.room = null;
	this.contents = null;
	this.image1 = null;
	this.image2 = null;
	this.maximize = true;
	this.name = null;

	this.create = function (name, title, className, left) {
		this.name = name;
	
		if (postLeft == 0) {
			postLeft = 25;
		} else {
			postLeft = postLeft + 100;
		}
		var imLeft = postLeft + "px";
		
		this.room = document.createElement("div");
		this.room.id = name + "_im";
		
		this.room.className = className;
		
		this.room.style.right = imLeft;
		this.room.style.bottom = 0;	
		
		this.room.style.width = width + "px";
		this.room.style.height = height + "px"; 
		
		this.room.style.display = "none";
		this.room.style.visibility = "hidden";
		
		this.room.style.zIndex = 10 + zIndex;
		
		document.body.appendChild(this.room);	
		
		if (internet_explorer) {
            this.frame = document.createElement("iframe");
            this.frame.frameborder = "0px";
            this.frame.style.width = width + "px";
			this.frame.style.height = height + 10 + "px"; 
            this.frame.id = name + "_iframe";
            this.frame.style.position = "absolute";
            this.frame.style.display = "none";
            this.frame.style.right = imLeft;
			this.frame.style.bottom = 0;	     
			this.room.style.zIndex = 9 + zIndex;       
            document.body.appendChild(this.frame);
        }
		
		var htmlData = "";
		htmlData += '<div id="chatTitle" title="' + name + '" class="' + className + 'Title" onclick="Chat.changeZIndex(\'' + name + '_im\')">';
		htmlData += '<table width="100%" border="0" cellspacing="0" cellpadding="0">';
		htmlData += '<tr>';
		htmlData += '<td width="100%" nowrap="nowrap">';
		htmlData += title;
		htmlData += '</td>';
		htmlData += '<td nowrap="nowrap">';
		htmlData += '<img src="images/minimize_mole.gif" id="' + name + '_image1" onclick="Chat.minimizeRoom(\'' + name + '\');" style="cursor: pointer"; />&nbsp;<img src="images/close_mole.gif" id="' + name + '_image2" onclick="Chat.closeRoom(\'' + name + '\');" style="cursor: pointer"; />';
		htmlData += '</td>';
		htmlData += '</tr>';
		htmlData += '</table>';
		htmlData += '</div>';
		
		htmlData += "<div id=\"" + name + "_contents\" onclick=\"Chat.changeZIndex('" + name + "_im')\">";
		htmlData += "<div class=\"chatRoomContents\" id=\"" + name + "_rcvd\"></div>";
		
		htmlData += "<input type=\"text\" class=\"inputText\" id=\"" + name + "_sendBox\" onfocus=\"Chat.blinkerOn(false);\" onkeypress=\"Chat.keyHandler(event," + "'" + name + "'" + ");\" />"
		htmlData += "</div>";
		
		this.room.innerHTML = htmlData;
		this.contents = $(name + "_contents");
		this.image1 = $(name + "_image1");
		this.image2 = $(name + "_image2");
		
		$(name + "_rcvd").style.height = (height - 73) + "px";
	    $(name + "_sendBox").style.width = (width - 18) + "px";
	};
	
	this.show = function () {			
		this.maximize = false;
		
		this.room.style.display = "block";
		this.room.style.visibility = "visible";
		
		if (this.frame) {		
			this.frame.style.display = "inline";   
			this.frame.style.visibility = "visible";        
        }
		
		this.minimize();
	};
	
	this.hide = function () {
		this.room.style.display = "none";
		this.room.style.visibility = "hidden";
		
		if (this.frame) {		
			this.frame.style.display = "none";   
			this.frame.style.visibility = "hidden";        
        }
        
        if (imHistory == true) {
	        $(this.name + "_rcvd").innerHTML = "<span class=\"imHistory\">" + $(this.name + "_rcvd").innerHTML.replace(/\(Auto-Reply:\)/g, "Auto-Reply:").replace(/<(?![Bb][Rr] ?\/?)([^>]+)>/ig, "") + "</span>\n";
	    } else {
	        $(this.name + "_rcvd").innerHTML = "";
	    }
	};
	
	this.minimize = function () {	
		if (this.maximize) {		
			this.contents.style.display = "none";
			this.contents.style.visibility = "hidden";
		
			this.room.style.height = 30 + "px";
			
			if (this.frame) {		
				this.frame.style.height = 30 + "px";     
	        }
		
			this.image1.src = "images/maximize_mole.gif"
			this.image2.src = "images/close_mole_hover.gif"
			
			this.maximize = false;
			
			if (internet_explorer) {
				this.room.style.bottom = 0;
				this.room.style.top = ( -0 - this.room.offsetHeight + ( document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight ) + ( ignoreMe = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop ) ) + 'px';
				if (this.frame) {
					this.frame.style.bottom = 0;
					this.frame.style.top = ( -0 - this.room.offsetHeight + ( document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight ) + ( ignoreMe = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop ) ) + 'px';
				}
			}
		} else {
			zIndex = zIndex + 10;
			this.room.style.zIndex = 10 + zIndex;
		
			this.contents.style.display = "block";
			this.contents.style.visibility = "visible";
		
			this.room.style.height = height + "px";
			
			if (this.frame) {		
				this.frame.style.height = height + 10 + "px";     
	        }
		
			this.image1.src = "images/minimize_mole.gif"
			this.image2.src = "images/close_mole.gif"
		
			this.maximize = true;
			
			if (internet_explorer) {
				this.room.style.bottom = 0;
				this.room.style.top = ( -0 - this.room.offsetHeight + ( document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight ) + ( ignoreMe = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop ) ) + 'px';
				if (this.frame) {
					this.frame.style.bottom = 0;
					this.frame.style.top = ( -0 - this.room.offsetHeight + ( document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight ) + ( ignoreMe = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop ) ) + 'px';
				}
			}
		}
	}
	
	this.isVisible = function() {
		if (this.room.style.display == "none") {
			return false;
		} else {
			return true;
		}
	};
	
	this.isMaximize = function() {
		return this.maximize;
	}
	
	this.appendText = function (text) {		
	};
	
	this.destroy = function() {
		try {
			document.body.removeChild(this.room);
			document.body.removeChild(this.frame);
		} catch (e) {}
		
		this.room.innerHTML = "";				
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
	
	this.resize = function() {
		this.room.style.bottom = 0;
		this.room.style.top = ( -0 - this.room.offsetHeight + ( document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight ) + ( ignoreMe = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop ) ) + 'px';
		if (this.frame) {
			this.frame.style.bottom = 0;
			this.frame.style.top = ( -0 - this.room.offsetHeight + ( document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight ) + ( ignoreMe = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop ) ) + 'px';
		}
	}
}