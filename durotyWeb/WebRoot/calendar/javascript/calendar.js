var dcalendarLastDate = new Date();

Page = {};

Page.resize = function()
{
	var header = document.getElementById('header');
	var footer = document.getElementById('footer');
	var content = document.getElementById('content');
	var contentsMesh = document.getElementById('contentsMesh');
	var borderHead = document.getElementById("borderHead");
	var contentsHead = document.getElementById('contentsHead');
	var borderFoot = document.getElementById("borderFoot");	
	var contentsBody = document.getElementById('contentsBody');
	var left = document.getElementById('left');
	var right = document.getElementById('right');
			
	var contentsHeadHeight = contentsHead.offsetHeight;
	var borderHeadHeight = borderHead.offsetHeight;
	var borderFootHeight = borderFoot.offsetHeight;
	
	//alert(borderHeadHeight);
	//alert(contentsHeadHeight);
	//alert(borderFootHeight);
	
	var contentHeight = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + 0) + "px";
	var contentsMeshHeight = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + borderHeadHeight + borderFootHeight + 0) +"px";	
	var contentsBodyHeight = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + borderHeadHeight + borderFootHeight + contentsHeadHeight + 0) +"px";
	content.style.height = contentHeight;
	contentsMesh.style.height = contentsMeshHeight;
	contentsBody.style.height = contentsBodyHeight;
	
	left.style.height = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + 50) + "px";
	right.style.height = contentHeight;
};

window.onload = initialize;
window.onresize = Page.resize;

/** Our function that initializes when the page is finished loading. */
function initialize() {
	Page.resize();
	
	createCalendar();
	
	RoundedTop("div#day","#fff","#e8eef7");
	RoundedTop("div#week","#fff","#e8eef7");
	RoundedTop("div#month","#fff","#e8eef7");
}

function createCalendar(_change) {
	var aux = document.getElementById("calendar-container");
	if (!aux) {
		return;
	}
	
	aux.innerHTML = "";
	
	Calendar.setup( {			
			flat         : "calendar-container", // ID of the parent element
			flatCallback : dateChanged          // our callback function
			
		}
	);	
	
	if (!_change) {
		dateChanged(Calendar);
	}
}

function dateChanged(calendar) {	
	if (calendar.dateClicked) {      
		/*merdaDate2 = calendar.date;      
		displayWeek(merdaDate2);
		merdaDate1 = new Date(merdaDate2.getTime());
		dcalendarLastDate = new Date(calendar.date.getTime());*/
		alert(new Date(calendar.date.getTime()));
    }
}

function getTopPos(inputObj) {
    var returnValue = inputObj.offsetTop;
    while ((inputObj = inputObj.offsetParent) != null) {
        if (inputObj.tagName != "HTML") {
            returnValue += inputObj.offsetTop;
        }
    }
    return returnValue;
}

function addEvent() {
}