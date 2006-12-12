var dcalendarLastDate = new Date();

Page = {};

Page.resize = function()
{
	var header = document.getElementById('header');
	var footer = document.getElementById('footer');
	var content = document.getElementById('content');
	var merda = document.getElementById('merda');
	var merda2 = document.getElementById('merda2');
	var left = document.getElementById('left');
	var right = document.getElementById('right');
	
	var contentHeight = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + 6) +"px";
	var merdaHeight = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + 55) +"px";
	var merda2Height = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + 55) +"px";
	content.style.height = contentHeight;
	merda.style.height = merdaHeight;
	merda2.style.height = merda2Height;
	left.style.height = contentHeight;
	right.style.height = contentHeight;
}

window.onload = initialize;
window.onresize = Page.resize;

/** Our function that initializes when the page is finished loading. */
function initialize() {
	var header = document.getElementById('header');
	var footer = document.getElementById('footer');
	var content = document.getElementById('content');
	var merda = document.getElementById('merda');
	var merda2 = document.getElementById('merda2');
	var left = document.getElementById('left');
	var right = document.getElementById('right');
	
	var contentHeight = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + 6) +"px";
	var merdaHeight = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + 55) +"px";
	var merda2Height = document.body.offsetHeight - (header.offsetHeight + footer.offsetHeight + 55) +"px";
	content.style.height = contentHeight;
	merda.style.height = merdaHeight;
	merda2.style.height = merda2Height;
	left.style.height = contentHeight;
	right.style.height = contentHeight;
	
	createCalendar();
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