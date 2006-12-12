var Files = new Files();
window.onload = initialize;

/** Our function that initializes when the page is finished loading. */
function initialize() {
	Files.getFiles("FILES", 0, 0, 0, 'ASC', '');
	
	Files.listLabels();
}

function Files() {
	this.getFiles = function(folder, label, page, order, orderType, token) {	
		this.infoCounters();
	
		if (folder && folder == "TRASH") {
			document.getElementById("buttonFolderFiles").style.fontWeight = "normal";
	    	document.getElementById("buttonAllFiles").style.fontWeight = "normal";
		}
	
		var formHistoryData = parent.frames['head'].document.historyData;
				
		if (folder == -1) {
			folder = formHistoryData.folder.value;
		} else {
			formHistoryData.folder.value = folder;
		}
		
		if (folder == "FILES") {
			document.getElementById("buttonFolderFiles").style.fontWeight = "bold";
	    	document.getElementById("buttonAllFiles").style.fontWeight = "normal";
		} else {
			document.getElementById("buttonFolderFiles").style.fontWeight = "normal";
	    	document.getElementById("buttonAllFiles").style.fontWeight = "bold";
		}
		
		if (label == -1) {
			label = formHistoryData.label.value;
		} else {
			formHistoryData.label.value = label;
		}
		
		if (page == -1) {
			page = formHistoryData.page.value;
		} else {
			formHistoryData.page.value = page;
		}
		
		if (order == -1) {
			order = formHistoryData.order.value;
		} else {
			formHistoryData.order.value = order;
		}	
		
		if (token == -1) {
			token = formHistoryData.token.value;
		} else {
			formHistoryData.token.value = token;
		}					
		
		if (orderType == -2) {
			orderType = formHistoryData.orderType.value;
		} else {
			if (orderType == -1) {
				if (formHistoryData.orderType.value && formHistoryData.orderType.value == 'DESC') {
					orderType = 'ASC';
				} else {
					orderType = 'DESC';
				}
			} 
			
			formHistoryData.orderType.value = orderType;
		}
	
		if (!token) {			
	    	var toHide = new Array();
	    	toHide[0] = document.getElementById("searchFiles");
	    
	    	var toShow = document.getElementById("files");
	    
	    	ajaxContents = new AjaxContents();
	    	ajaxContents.init();    	    	
	    	ajaxContents.setVar("folder", folder);
	    	ajaxContents.setVar("label", label);
	    	ajaxContents.setVar("page", page);
	    	ajaxContents.setVar("order", order);
	    	ajaxContents.setVar("orderType", orderType);	    	
	    	
	    	ajaxContents.selectContents("files/getFiles.drt", false, "files", toShow, toHide, true);
	    	
	    	this.pagination(folder, label, page);
    	} else {
    		if (token == -2) {
    			var toHide = new Array();
		    	toHide[0] = document.getElementById("files");
		    	toHide[1] = document.getElementById("pagination");
		    
		    	var toShow = document.getElementById("searchFiles");
		    
		    	ajaxContents = new AjaxContents();
		    	ajaxContents.init();    	    	
		    	ajaxContents.setVar("folder", folder);
		    	ajaxContents.setVar("label", label);
		    	ajaxContents.setVar("page", page);
		    	ajaxContents.setVar("order", order);
		    	ajaxContents.setVar("orderType", orderType);
		    	
   		    	this.parseAdvancedSearch(ajaxContents);
		    	
		    	ajaxContents.selectContents("files/advancedSearch.drt", false, "searchFiles", toShow, toHide, true);
    		} else {    		
	    		var toHide = new Array();
		    	toHide[0] = document.getElementById("files");
		    	toHide[1] = document.getElementById("pagination");
		    
		    	var toShow = document.getElementById("searchFiles");
		    
		    	ajaxContents = new AjaxContents();
		    	ajaxContents.init();    	    	
		    	ajaxContents.setVar("token", token);
		    	ajaxContents.setVar("folder", folder);
		    	ajaxContents.setVar("label", label);
		    	ajaxContents.setVar("page", page);
		    	ajaxContents.setVar("order", order);
		    	ajaxContents.setVar("orderType", orderType);
		    	
		    	ajaxContents.selectContents("files/simpleSearch.drt", false, "searchFiles", toShow, toHide, true);
	    	}
    	}
    };
    
    this.pagination = function(folder, label, page) {	
    	var toHide = false;
    
    	var toShow = document.getElementById("pagination");
    
    	ajaxContents = new AjaxContents();    	
    	ajaxContents.init();    	    	
    	ajaxContents.setVar("folder", folder);
    	ajaxContents.setVar("label", label);
    	ajaxContents.setVar("page", page);
    	
    	ajaxContents.selectContents("files/pagination.drt", false, "pagination", toShow, toHide, false);
    };
    
    this.viewAll = function() {
    	this.getFiles("", -1, 0, 0, 'ASC', "");
    	
    	document.getElementById("buttonAllFiles").style.fontWeight = "bold";
    	document.getElementById("buttonFolderFiles").style.fontWeight = "normal";
    };
    
    this.viewFolderFiles = function() {
    	this.getFiles("FILES", -1, 0, 0, 'ASC', "");
    	
    	document.getElementById("buttonFolderFiles").style.fontWeight = "bold";
    	document.getElementById("buttonAllFiles").style.fontWeight = "normal";
    };
    
    this.checkTrash = function() {
    	var formHistoryData = parent.frames['head'].document.historyData;
				
		if (formHistoryData.folder.value && formHistoryData.folder.value == 'TRASH') {
			formHistoryData.folder.value = "";
			document.getElementById("buttonFolderFiles").style.fontWeight = "normal";
	    	document.getElementById("buttonAllFiles").style.fontWeight = "bold";
		} else {
		}		
    };
    
    this.flag = function(idints) {
    	var self = this;    
    	var ajax = new sack();    	
    	ajax.setVar("idints", idints);    	
    	showLoading();    	
    	ajax.requestFile = "files/flag.drt";
        ajax.onCompletion = function () {
            self.getFiles(-1, -1, -1, -1, -2, -1);
        };        
        ajax.runAJAX();
    };
    
    this.deleteFile = function(idints) {
    	var self = this;    
    	var ajax = new sack();    	
    	ajax.setVar("idints", idints);    	
    	showLoading();    	
    	ajax.requestFile = "files/delete.drt";
        ajax.onCompletion = function () {
            self.getFiles(-1, -1, -1, -1, -2, -1);
        };        
        ajax.runAJAX();
    };
    
    this.restoreFile = function(idints) {
    	var self = this;    
    	var ajax = new sack();    	
    	ajax.setVar("idints", idints);    	
    	showLoading();    	
    	ajax.requestFile = "files/restore.drt";
        ajax.onCompletion = function () {
            self.getFiles(-1, -1, -1, -1, -2, -1);
        };        
        ajax.runAJAX();
    };
    
    this.listLabels = function() {	
    	var toHide = false;
    
    	var toShow = document.getElementById("listLabels");
    
    	ajaxContents = new AjaxContents();    	
    	ajaxContents.init();
    	
    	ajaxContents.selectContents("files/listLabels.drt", false, "listLabels", toShow, toHide, true);
    };
    
    this.applyLabel = function(label) {    
    	var idints = "";
    
    	if (document.attachments.idints.length == null) {
    		if (document.attachments.idints.checked) {
	    		idints = document.attachments.idints.value;
	    	}
		} else {
			for(var i = 0; i < document.attachments.idints.length; i++) {
				if (document.attachments.idints[i].checked) {
					if (idints.length > 0) {
						idints = idints + ",";
					}
					idints = idints + document.attachments.idints[i].value;
				}
			}
		}
    
    
    	var self = this;    
    	var ajax = new sack();    	
    	ajax.setVar("label", label);
    	ajax.setVar("idints", idints);
    	showLoading();    	
    	ajax.requestFile = "files/applyLabel.drt";
        ajax.onCompletion = function () {
            self.getFiles(-1, -1, -1, -1, -2, -1);
        };        
        ajax.runAJAX();
    };
    
    this.selectAll = function() {
    	if (document.attachments.idints.length == null) {
    		if (document.attachments.idints.checked) {
	    		document.attachments.idints.checked = false;
	    	} else {
	    		document.attachments.idints.checked = true;
	    	}
		} else {
			for(var i = 0; i < document.attachments.idints.length; i++) {
				if (document.attachments.idints[i].checked) {
					document.attachments.idints[i].checked = false;
				} else {
					document.attachments.idints[i].checked = true;
				}
			}
		}
    };
    
    this.attachFile = function(obj, init) {
		//var aux = document.getElementById("attachFile");	
		var aux = document.getElementById(obj);	
		var divInfo = document.getElementById("divInfo");
		if (aux != null) {
			if (init) {
				try {				
					divInfo.innerHTML = "";
					divInfo.className = "hide";
				} catch(e) {
				}
				aux.innerHTML = "";
			}
		
			var newEl = document.createElement('div');
			var i = this.countAttach++;
			newEl.id = "divAttach" + i;
			newEl.innerHTML = newEl.innerHTML + "<input type='file' name='attachments' size='30'/>&nbsp;&nbsp;<img src='images/cancel.gif' border='0' style='cursor: pointer;' onclick='javascript:Files.removeAttach(\"" + newEl.id + "\", \"" + obj + "\");' />";
			
			
			aux.appendChild(newEl);
		}
	};
	
	this.removeAttach = function(id, obj) {
		//var aux = document.getElementById("attachFile");
		var aux = document.getElementById(obj);
		if (aux != null) {
			var child = document.getElementById(id);
			if (child != null) {
				aux.removeChild(child);
			}
		}
	};
	
	this.parseAdvancedSearch = function (ajaxContents) {
		var _hasWords = document.formAdvancedSearch.hasWords.value;
		var _filetype = document.formAdvancedSearch.filetype.options[document.formAdvancedSearch.filetype.selectedIndex].value;
		var _fixDate = document.formAdvancedSearch.fixDate.options[document.formAdvancedSearch.fixDate.selectedIndex].value;		
		var _startDate = document.formAdvancedSearch.startDate.value;
		var _endDate = document.formAdvancedSearch.endDate.value;
		
		if (_hasWords != null && _hasWords != "") {
			ajaxContents.setVar("hasWords", _hasWords);			
		}	
		
		if (_filetype != null && _filetype != "") {
			ajaxContents.setVar("filetype", _filetype);
		}		
		
		if (_fixDate != null && _fixDate > 0) {
			ajaxContents.setVar("fixDate", _fixDate);
		}
		
		if (_startDate != null && _startDate != "") {
			ajaxContents.setVar("startDate", _startDate);
		}
		
		if (_endDate != null && _endDate != "") {
			ajaxContents.setVar("endDate", _endDate);
		}
	};
	
	this.infoCounters = function() {	
		var self = this;    
    	var ajax = new sack();    	
    	ajax.requestFile = "files/infoCounters.drt";
        ajax.onCompletion = function () {
            parent.frames['foot'].document.getElementById("quotaLayer").innerHTML = ajax.response;
        };        
        ajax.runAJAX();
    };
    
    this.infoFolders = function (inboxCount, spamCount, htmlQuota) {
		var aux = parent.frames['head'].document.getElementById("infoTopINBOX");
		if (aux && inboxCount > 0) {
			aux.innerHTML = "&nbsp;(" + inboxCount + ")";	
		} else {
			aux.innerHTML = "";
		}
	};
}