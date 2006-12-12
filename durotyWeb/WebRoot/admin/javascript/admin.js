window.onload = initialize;
var Admin = new Admin();

/** Our function that initializes when the page is finished loading. */
function initialize() {
	parent.document.body.rows = "100%,*,*";

	// initialize the DHTML History framework
    dhtmlHistory.initialize();	
   
	// add ourselves as a DHTML History listener
    dhtmlHistory.addListener(handleHistoryChange);	
	
	// determine what our initial location is by retrieving it from the browser's location after the hash
    var currentLocation = dhtmlHistory.getCurrentLocation();
		
	// if there is no location then display
	// the default, which is the inbox
    if (currentLocation === "") {
        currentLocation = "section:searchUsers:0*0!0";
    }
	
	// extract the section to display from the initial location 
    currentLocation = currentLocation.replace(/section\:/, "");
	
	// display this initial location
    Admin.displayLocation(currentLocation, null);
}
/** Handles history change events. */
function handleHistoryChange(newLocation, historyData) {	
	// if there is no location then display the default, which is the INBOX
    if (newLocation === "") {
        newLocation = "section:searchUsers:0*0!0";
    }
    if (historyData === null) {
        historyData = historyStorage.get(newLocation);
    }
	
	// extract the section to display from the location change; newLocation will begin with the word "section:" 
    newLocation = newLocation.replace(/section\:/, "");
	
	// update the browser to respond to this DHTML history change
    Admin.displayLocation(newLocation, historyData);
}

function Admin() {
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
		
		if (this.ACTION == "searchUsers") {
			var token = document.getElementById("token").value;
						
			this.searchUsers(token, this.NAME, this.UID, this.EXTRA);
		} else if (this.ACTION == "searchGroups") {
			var token = document.getElementById("token").value;
						
			this.searchGroups(token, this.NAME, this.UID, this.EXTRA);
		} 
		
		dhtmlHistory.add("section:" + this.ACTION + ":" + this.NAME + "*" + this.UID + "!" + this.EXTRA, null);
		
		window.scroll(0,0);
    };
    
    this.searchUsers = function(token, page, order, desc) {    
    	toShow = document.getElementById("users");
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("token", token);
    	ajaxContents.setVar("page", page);
    	ajaxContents.setVar("byPage", "20");
    	ajaxContents.selectContents("admin/searchUsers.drt", false, "users", toShow, null, true);
    };
    
    this.selectAll = function(_form) {
    	if (_form.idints.length == null) {
			_form.idints.checked = this.selectedAll;
		} else {
			for(var i = 0; i < _form.idints.length; i++) {
				_form.idints[i].checked = this.selectedAll;
			}
		}
		
		this.selectedAll = !this.selectedAll;
    };
    
    this.insertUser = function() {    	    
    	toShow = document.getElementById("insertUser");
    	var toHide = new Array();
    	toHide[0] = document.getElementById("updateUser");
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.selectContents("admin/formUser.drt", false, "insertUser", toShow, toHide, true);
    };
    
    this.updateUser = function(idint) {    	    
    	toShow = document.getElementById("updateUser");
    	var toHide = new Array();
    	toHide[0] = document.getElementById("insertUser");
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.setVar("idint", idint);
    	ajaxContents.selectContents("admin/getUser.drt", false, "updateUser", toShow, toHide, true);
    };
}