var FilesLeft = new FilesLeft();
window.onload = initialize;

/** Our function that initializes when the page is finished loading. */
function initialize() {
	FilesLeft.getLabels();
}

function FilesLeft() {
	this.getLabels = function() {
    	var toHide = false;
    
    	var toShow = document.getElementById("labels");
    
    	ajaxContents = new AjaxContents();
    	ajaxContents.init();
    	ajaxContents.selectContents("files/getLabels.drt", false, "labels", toShow, toHide, true);
    };
    
    this.parseLeft = function(id) {
    	var idLabels = document.getElementById("labels");
    	if (idLabels) {
    		for (var i = 0; i < idLabels.childNodes.length; i++) {
				var currentElement = idLabels.childNodes[i];
				// see if this is a DOM Element node
				if (currentElement.nodeType == 1) {
					// clear any class name
					currentElement.className = "label";
				}													
			} 		
    	}
    	
    	var idLabel = document.getElementById(id);
    	if (idLabel) {
    		idLabel.className = "labelSelected";
    	}    	
    }    
    
    this.createLabel = function(name) {    
    	var self = this;    
    	var ajax = new sack();    	
    	ajax.setVar("name", name);    	
    	showLoading();    	
    	ajax.requestFile = "files/createLabel.drt";
        ajax.onCompletion = function () {
            self.getLabels();
            parent.frames['body'].Files.listLabels();
        };        
        ajax.runAJAX();
    }
}