window.onload = initialize;
var Contacts = new Contacts();

/** Our function that initializes when the page is finished loading. */
function initialize() {
	// initialize the DHTML History framework
    dhtmlHistory.initialize();	
   
	// add ourselves as a DHTML History listener
    dhtmlHistory.addListener(handleHistoryChange);	
	
	// determine what our initial location is by retrieving it from the browser's location after the hash
    var currentLocation = dhtmlHistory.getCurrentLocation();
		
	// if there is no location then display
	// the default, which is the inbox
    if (currentLocation === "") {
        currentLocation = "section:searchContacts:0*0!0";
    }
	
	// extract the section to display from the initial location 
    currentLocation = currentLocation.replace(/section\:/, "");
	
	// display this initial location
    Contacts.displayLocation(currentLocation, null);
    
    if (document.getElementById("loading") !== null) {
        document.getElementById("loading").className = "hide";
    }
    if (document.getElementById("mesh") !== null) {
        document.getElementById("mesh").className = "show";
    }
}
/** Handles history change events. */
function handleHistoryChange(newLocation, historyData) {	
	// if there is no location then display the default, which is the INBOX
    if (newLocation === "") {
        newLocation = "section:searchContacts:0*0!0";
    }
    if (historyData === null) {
        historyData = historyStorage.get(newLocation);
    }
	
	// extract the section to display from the location change; newLocation will begin with the word "section:" 
    newLocation = newLocation.replace(/section\:/, "");
	
	// update the browser to respond to this DHTML history change
    Contacts.displayLocation(newLocation, historyData);
}

function Contacts() {
	this.ACTION = "";
	this.NAME = 0;
	this.UID = 0;
	this.EXTRA = 0;
	this.selectedAll = true;
	
	/** Displays the given location in the right-hand side content area. 
     *  el format de una newLocation ve donar per //action:name*uid!extra
      */
    this.displayLocation = function (newLocation, sectionData) {
    	idx = newLocation.indexOf(':');
		
		this.ACTION = newLocation;
		this.NAME = 0;
		this.UID = 0;		
		this.EXTRA = 0;
		
		if (idx > -1) {		
			//en el cas del search tenim que el folder es un queryString
			aux1 = newLocation.indexOf('*');
			aux2 = newLocation.indexOf('!');
			this.EXTRA = newLocation.substring(aux2 + 1, newLocation.length);	
			this.UID = newLocation.substring(aux1 + 1, aux2);	
			this.NAME = newLocation.substring(idx + 1, aux1);
			this.ACTION = newLocation.substring(0, idx);
		}
		
		if (this.ACTION == "searchContacts") {
			var token = document.getElementById("token").value;
						
			this.searchContacts(token, this.NAME, this.UID, this.EXTRA);
		} else if (this.ACTION == "searchGroups") {
			var token = document.getElementById("token").value;
						
			this.searchGroups(token, this.NAME, this.UID, this.EXTRA);
		} 
		
		dhtmlHistory.add("section:" + this.ACTION + ":" + this.NAME + "*" + this.UID + "!" + this.EXTRA, null);
		
		window.scroll(0,0);
    };
    
    this.searchContacts = function(token, page, order, desc) {    	    
    	var contacts = document.getElementById("contacts");
		if (contacts) {
			contacts.className = "show";
		}
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("token", token);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	ajaxContents.selectContents("mail/preferences/searchContacts.drt", false, "contacts", null, null, true);
    };
    
    this.searchGroups = function(token, page, order, desc) {    	    
    	var contacts = document.getElementById("contacts");
		if (contacts) {
			contacts.className = "show";
		}
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("token", token);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("order", order);
    	ajaxContents.setVar("extra", desc);
    	ajaxContents.selectContents("mail/preferences/searchGroups.drt", false, "contacts", null, null, true);
    };
    
    this.selectAll = function(_form) {
    	if (_form.idint.length == null) {
			_form.idint.checked = this.selectedAll;
		} else {
			for(var i = 0; i < _form.idint.length; i++) {
				_form.idint[i].checked = this.selectedAll;
			}
		}
		
		this.selectedAll = !this.selectedAll;
    };
}