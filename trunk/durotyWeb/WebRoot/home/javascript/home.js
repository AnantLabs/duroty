window.onload = initialize;
var Home = new Home();

/** Our function that initializes when the page is finished loading. */
function initialize() {
	parent.document.body.rows = "100%,0,0";
	
	// initialize the DHTML History framework
    dhtmlHistory.initialize();	
   
	// add ourselves as a DHTML History listener
    dhtmlHistory.addListener(handleHistoryChange);	
	
	// determine what our initial location is by retrieving it from the browser's location after the hash
    var currentLocation = dhtmlHistory.getCurrentLocation();
		
	// if there is no location then display
	// the default, which is the inbox
    if (currentLocation === "") {
        currentLocation = "section:HOME:0*0!0";
    }
	
	// extract the section to display from the initial location 
    currentLocation = currentLocation.replace(/section\:/, "");
    
    initDragableBoxesScript();
    
    Home.refresh();
}
/** Handles history change events. */
function handleHistoryChange(newLocation, historyData) {	
	// if there is no location then display the default, which is the INBOX
    if (newLocation === "") {
        newLocation = "section:HOME:0*0!0";
    }
    if (historyData === null) {
        historyData = historyStorage.get(newLocation);
    }
	
	// extract the section to display from the location change; newLocation will begin with the word "section:" 
    newLocation = newLocation.replace(/section\:/, "");
	
	// update the browser to respond to this DHTML history change
//    Bookmark.displayLocation(newLocation, historyData);
}
function Home() {	
	this.ACTION = "";
	this.NAME = 0;
	this.UID = 0;
	this.selectedAll = true;    
    
    this.infoFolders = function (inboxCount, spamCount, htmlQuota) {
		var aux = document.getElementById("infoTopINBOX");
		if (aux && inboxCount > 0) {
			aux.innerHTML = "&nbsp;(" + inboxCount + ")";	
		} else {
			aux.innerHTML = "";
		}
	};
	
	this.refresh = function () {
		setTimeout("window.frames['session'].REFRESH.selectSession(true)", 10);
	};
	
	this.searchMail = function() {
	}
	
	this.searchBookmark = function() {
	}
}

