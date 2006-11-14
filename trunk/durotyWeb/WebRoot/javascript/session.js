window.onload = initialize;

var UPDATE_SESSION = false;

function UpdateSession() {
    this.sessionDOM = false;
    this.selectSession = function () {
        this.sessionDOM = Sarissa.getDomDocument();
        this.sessionDOM.async = true; // this line can be ommited - true is the default		
        this.sessionDOM.load("login/updateSession.drt");
        this.sessionDOM.onreadystatechange = this.selectSessionHandler;
    };
    this.selectSessionHandler = function () {
        if (UPDATE_SESSION.sessionDOM && UPDATE_SESSION.sessionDOM.readyState == 4) {
            UPDATE_SESSION.selectSessionOnCompletion();
        }
    };
    this.selectSessionOnCompletion = function () {
        UPDATE_SESSION.sessionDOM = false;
    };    
}

function initialize() {	
	UPDATE_SESSION = new UpdateSession();
	setInterval('UPDATE_SESSION.selectSession()', 30000);
};