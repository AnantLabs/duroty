window.onload = initialize;

var REFRESH = new UpdateSession();

function UpdateSession() {	
	var self = this;
	this.date = new Date();
    this.sessionDOM = false;
    this.refreshLocation = false;
    
    this.selectSession = function (_refreshLocation) {
    	if (!this.sessionDOM) {
	    	this.refreshLocation = _refreshLocation;
    	    this.sessionDOM = Sarissa.getDomDocument();
        	this.sessionDOM.async = true; // this line can be ommited - true is the default		
	        this.sessionDOM.load("mail/refresh.drt");
    	    this.sessionDOM.onreadystatechange = this.selectSessionHandler;
		}
    };
    
    this.selectSessionHandler = function () {
        if (self.sessionDOM && self.sessionDOM.readyState == 4) {
            self.selectSessionOnCompletion();
        }
    };
    
    this.selectSessionOnCompletion = function () {
	    try {
	    	var inboxCount = 0;
			var spamCount = 0;
			var htmlQuota = false;
			
			var _xmlString = this.sessionDOM;
		
			if (!_xmlString) {
				parent.hideLoading();
				this.sessionDOM = false;
				return;
			}
			
			if (_xmlString.parseError != 0) {
				parent.hideLoading();
				this.sessionDOM = false;
				return false;
			}
								
			var countInbox = _xmlString.getElementsByTagName("newMessageCountInbox");
			if (countInbox && countInbox.length == 1) {		
				inboxCount = countInbox[0].getAttribute("value");
			} else {
				inboxCount = 0;
			}											
			
			var countSpam = _xmlString.getElementsByTagName("newMessageCountSpam");
			if (countSpam && countSpam.length == 1) {		
				spamCount = countSpam[0].getAttribute("value");
			} else {
				spamCount = 0;
			}	
			
			var quota = _xmlString.getElementsByTagName("quota");
			if (quota && quota.length == 1) {									
				htmlQuota = Sarissa.getText(quota[0], 1);
			} else {
				htmlQuota = "";
			}			
			
			parent.Mail.infoFolders(inboxCount, spamCount, htmlQuota);	
			
			parent.Mail.infoLabelsToNull();
			
			var newLabels = _xmlString.getElementsByTagName("newLabels");
			
			if (newLabels && newLabels.length == 1) {
				for (var j = 0; j < newLabels[0].childNodes.length; j++) {
					var aux = newLabels[0].childNodes[j].tagName;
					if (aux && aux == 'label') {
						//body = Sarissa.getText(elements[i].childNodes[j], 1);
						var labIdint = newLabels[0].childNodes[j].getAttribute("idint");
						var labCount = newLabels[0].childNodes[j].getAttribute("count");
						if (labCount) {
							parent.Mail.infoLabels(labIdint, labCount);
						}
					}
				}
			}
			
			if (this.refreshLocation) {
				parent.handleHistoryChange(parent.dhtmlHistory.getCurrentLocation(), null);
			}
			
			//parent.hideLoading();
	    
	        self.sessionDOM = false;
		} catch (e) {
			parent.hideLoading();
		}
		
		var now = new Date();
		if ((now.getTime() - this.date.getTime()) >= 3600000) {
			//logout
			top.location.href = "login/logout.drt";
		}
    };    
}

function initialize() {		
	setInterval('REFRESH.selectSession(false)', 300000);
};