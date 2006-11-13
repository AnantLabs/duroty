window.onload = initialize;

var REFRESH = new UpdateSession();;

function UpdateSession() {
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
        if (REFRESH.sessionDOM && REFRESH.sessionDOM.readyState == 4) {
            REFRESH.selectSessionOnCompletion();
        }
    };
    
    this.selectSessionOnCompletion = function () {
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
		
		parent.Notebook.infoFolders(inboxCount, spamCount, htmlQuota);
		
		if (this.refreshLocation) {
			parent.handleHistoryChange(parent.dhtmlHistory.getCurrentLocation(), null);			
		}
    
        REFRESH.sessionDOM = false;
    };    
}

function initialize() {		
	setInterval('REFRESH.selectSession(false)', 300000);
};