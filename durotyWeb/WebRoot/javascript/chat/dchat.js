var DCHAT = new DChat();

function intervalObject(_id, _object) {
	this.id = _id;
	this.object = _object;
}

function activeRoomObject(_id, _to) {
	this.id = _id;
	this.to = _to;
}

function DChat() {
	this.activeRooms = new Array();
	this.intervals = new Array();
	this.checkDom = false;
	this.connectedDom = false;
	this.sendDom = false;
	this.getMessagesDom = false;
	
	/** Funció que inicialitza els listeners per recuperar missatges quan algú arranca un chat **/
	this.init = function() {
		setInterval('DCHAT.check()', 5000);
		//this.check();
	}
	
	/** Funció que fa check de les rooms i mira si estan activades o no **/
	this.check = function() {
		this.checkDom = Sarissa.getDomDocument();		
		this.checkDom.async = true; // this line can be ommited - true is the default
		this.checkDom.load("dchat/check.drt");	
		this.checkDom.onreadystatechange = this.checkHandler;
	}
	
	/** On check rooms completion handler **/
	this.checkHandler = function() {					
		if (!DCHAT.checkDom) {
			return false;
		}
		
		if (DCHAT.checkDom && DCHAT.checkDom.readyState != 4) {
		    return false;
		}
		
		if (DCHAT.checkDom.parseError != 0) {			
			return false;
		}
		
		var rooms = DCHAT.checkDom.getElementsByTagName("room");	
		if (rooms && rooms.length > 0) {
			for (var i = 0; i < rooms.length; i++) {
				var roomId = rooms[i].getAttribute("id");
				var from = rooms[i].getAttribute("from");
				var alertControl = true;
				for (var j = 0; j < DCHAT.activeRooms.length; j++) {
					if (DCHAT.activeRooms[j] && DCHAT.activeRooms[j].id == roomId) {
						alertControl = false;
						break;
					}
				}
				
				if (alertControl) {
					/*** Cal crear una room amb create element ***/
					DCHAT.createRoom(new activeRoomObject(roomId, from));
					
					if (document.forms['formu' + roomId] && document.forms['formu' + roomId].message) {
						document.forms['formu' + roomId].message.focus();
					}
				}
			}
		} else {
		}
		
		DCHAT.checkDom = false;
	}
	
	this.connect = function(_to) {
		this.connectedDom = Sarissa.getDomDocument();		
		this.connectedDom.async = true; // this line can be ommited - true is the default
		this.connectedDom.load("dchat/connect.drt?to=" + _to);	
		this.connectedDom.onreadystatechange = this.connectHandler;
	}
	
	this.connectHandler = function() {	
		if (!DCHAT.connectedDom) {
			return false;
		}
		
		if (DCHAT.connectedDom && DCHAT.connectedDom.readyState != 4) {
		    return false;
		}
		
		if (DCHAT.connectedDom.parseError != 0) {			
			return false;
		}
		
		var chat = DCHAT.connectedDom.getElementsByTagName("chat");	
		if (chat && chat.length == 1) {
			var connected = chat[0].getAttribute("connected");
			if (connected == "true") {
				var roomId = chat[0].getAttribute("room");
				var to = chat[0].getAttribute("to");
				DCHAT.createRoom(new activeRoomObject(roomId, to));
				
				if (document.forms['formu' + roomId] && document.forms['formu' + roomId].message) {
					document.forms['formu' + roomId].message.focus();
				}
			} else {
				alert("This user now is offline, refresh page");
			}
		} else {
		}
		
		DCHAT.connectedDom = false;
	}
	
	this.disconnect = function(_room) {
		this.connectedDom = Sarissa.getDomDocument();		
		this.connectedDom.async = true; // this line can be ommited - true is the default
		this.connectedDom.load("dchat/disconnect.drt?room=" + _room);	
	}
	
	this.createRoom = function(_activeRoomObject) {
		for (var i = 0; i < this.activeRooms.length; i++) {
			if ((_activeRoomObject.id == this.activeRooms[i].id) && (_activeRoomObject.to == this.activeRooms[i].to)) {
				return false;
			}
		}
	
		var zIndex = 5 + (this.activeRooms.length * 10) + "px";
	
		this.activeRooms[this.activeRooms.length] = _activeRoomObject;
		
		var divContents = document.createElement("div");
		divContents.setAttribute("id", _activeRoomObject.id);
		
		document.getElementsByTagName("body")[0].appendChild(divContents);
		document.getElementById(_activeRoomObject.id).style.display = "block";
		document.getElementById(_activeRoomObject.id).style.zIndex  = "100" + this.activeRooms.length;
		document.getElementById(_activeRoomObject.id).style.right = zIndex;
		document.getElementById(_activeRoomObject.id).className = "chatRoom";
		document.getElementById(_activeRoomObject.id).soundEffect = "http://10.0.0.65:8080/public/images/bicycle_bell.wav";
		
		htmlData = "";				
		
		htmlData += '<div class="chatRoomTitle">';
		
		htmlData += '<table width="100%" border="0" cellspacing="0" cellpadding="0">';
		htmlData += '<tr>';
		htmlData += '<td width="100%" nowrap="nowrap">';
		htmlData += _activeRoomObject.to;
		htmlData += '</td>';
		htmlData += '<td nowrap="nowrap">';
		htmlData += '<img alt="Desconectar" title="Desconectar" src="/public/images/cancel.gif" border="0" onclick="DCHAT.closeRoom(\'' + _activeRoomObject.id + '\')" style="cursor:pointer" />';
		htmlData += '</td>';
		htmlData += '</tr>';
		htmlData += '</table>';
		htmlData += '</div>';
		
		htmlData += '<div class="chatRoomContents"><div id="' + _activeRoomObject.id + 'Contents" class="chatRoomConversation"></div></div>';
		
		htmlData += '<div class="chatRoomFoot">';
		htmlData += '<FORM action="" name="formu' + _activeRoomObject.id + '" onsubmit="DCHAT.sendMessage(\'' + _activeRoomObject.id + '\', this.message.value, \'' + _activeRoomObject.to + '\');this.message.value = \'\';return false;" style="margin: 0px; padding: 0px;">';
		htmlData += '<input type="text" size="30" name="message" maxlength="100" style="width: 98%"/>';
		htmlData += '</FORM>';
		htmlData += '</div>';
		
		//htmlData += '<div style="height: 0px;">';
		htmlData += '<embed src="/public/images/bicycle_bell.wav" autostart="true" hidden="true" loop="false" style="width:0px; height:0px;">';
		htmlData += '</embed>';
		//htmlData += '</div>';
		
		document.getElementById(_activeRoomObject.id).innerHTML = htmlData;
		
		var peca = function() {
			DCHAT.getMessages(_activeRoomObject.id);
		};
		
		this.intervals[this.intervals.length] = new intervalObject(_activeRoomObject.id, peca);
		
		setInterval(peca, 3000);
	}
	
	this.closeRoom = function(_id) {		
		for (var i = 0; i < this.activeRooms.length; i++) {
			if (this.activeRooms[i] && this.activeRooms[i].id == _id) {
				for (var j = 0; j < this.intervals.length; j++) {
					if (this.intervals[j].id == _id) {
						clearInterval(this.intervals[i].object);
						this.intervals[i] = false;
						break;
					}
				}
				this.activeRooms[i] = false;
				this.disconnect(_id);
				document.getElementsByTagName("body")[0].removeChild(document.getElementById(_id));
				break;
			}
		}
	}
	
	this.sendMessage = function(_room, _msg, _to) {
		this.sendDom = Sarissa.getDomDocument();		
		this.sendDom.async = true; // this line can be ommited - true is the default
		this.sendDom.load("dchat/send.drt?room=" + _room + "&to=" + _to + "&body=" + _msg);	
		this.sendDom.onreadystatechange = this.sendHandler;
	}
	
	this.sendHandler = function() {
		if (!DCHAT.sendDom) {
			return false;
		}
		
		if (DCHAT.sendDom && DCHAT.sendDom.readyState != 4) {
		    return false;
		}
		
		if (DCHAT.sendDom.parseError != 0) {			
			return false;
		}
		
		var error = DCHAT.sendDom.getElementsByTagName("error");	
		if (error && error.length == 1) {
			var auxRoom = error[0].getAttribute("room");
			var errorMsg = Sarissa.getText(error[0], 1);			
			DCHAT.appendMessage(auxRoom, "<font style='color:red;'>server> </font>", "", errorMsg);
		} else {
		}
		
		var message = DCHAT.sendDom.getElementsByTagName("message");	
		if (message && message.length == 1) {
			var room = message[0].getAttribute("room");
			var from =  message[0].getAttribute("from");
			from = from.substring(0, from.indexOf('@'));
			from = "<font style='color:#cb6e0a;'>" + from + "> </font>";
			var to = message[0].getAttribute("to");
			var body = message[0].getAttribute("body");				
			DCHAT.appendMessage(room, from, to, body);
		} else {
		}
		
		DCHAT.sendDom = false;
	}
	
	this.getMessages = function(_room) {
		this.getMessagesDom = Sarissa.getDomDocument();		
		this.getMessagesDom.async = true; // this line can be ommited - true is the default
		this.getMessagesDom.load("dchat/messages.drt?room=" + _room);	
		this.getMessagesDom.onreadystatechange = this.getMessagesHandler;
	}
	
	this.getMessagesHandler = function() {
		if (!DCHAT.getMessagesDom) {
			return false;
		}
		
		if (DCHAT.getMessagesDom && DCHAT.getMessagesDom.readyState != 4) {
		    return false;
		}
		
		if (DCHAT.getMessagesDom.parseError != 0) {			
			return false;
		}
		
		var message = DCHAT.getMessagesDom.getElementsByTagName("message");	
		if (message && message.length > 0) {
			for (var i = 0; i < message.length; i++) {
				var room = message[i].getAttribute("room");
				var from =  message[i].getAttribute("from");
				if (from) {
					from = from.substring(0, from.indexOf('@'));
					from = "<font style='color:blue;'>" + from + "> </font>";
				}
				var to = message[i].getAttribute("to");
				var body = message[i].getAttribute("body");
				
				if (room && from && to && body) {
					DCHAT.appendMessage(room, from, to, body);
				}
			}			
		} else {
		}
		
		this.getMessagesDom = false;
	}
	
	
	this.appendMessage = function(_room, _from, _to, _body) {			
		var aux = document.getElementById(_room + "Contents");
		if (!aux) {
			alert("room inactive, close and reconnect");
			return false;
		}
		
		var extrahtml = "<br/>";
		
		if (aux.innerHTML == "") {
			extrahtml = "";
		}
		
		aux.innerHTML = _from + _body + extrahtml + aux.innerHTML;
	}
}