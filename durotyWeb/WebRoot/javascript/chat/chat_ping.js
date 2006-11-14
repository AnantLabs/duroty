var ChatPing = new ChatPing();

var pingFrequency = 2500;           // How often to ping the server (in milliseconds). Best range between 2500 and 3500 ms.

function ChatPing() {
	this.pingTimer = null;
		
	this.setPingTimer = function() {
		this.pingTimer = setInterval("ChatPing.pingTo()", pingFrequency);		
	}
	
	this.removePingTimer = function() {
		clearTimeout(ChatPing.pingTimer);
	}

	this.pingTo = function() {	
	    parent.window.frames['main'].Chat.ping();
	};
}