
winIEpass = ((navigator.appName.indexOf("Microsoft") != -1) && (navigator.appVersion.indexOf("Windows") != -1)) && (parseFloat(navigator.appVersion) >= 4) ? true : false;
NNpass = ((navigator.appName == "Netscape") && (navigator.userAgent.indexOf("Mozilla") != -1) && (parseFloat(navigator.appVersion) >= 4) && (navigator.javaEnabled())) ? true : false;
supportedBrowser = (winIEpass || NNpass) ? true : false;

// check for Flash Plug-in in Mac or Win Navigator. Get plug-in version.
minPlayer = 4;
function Flash_checkForPlugIn() {
    var plugin = (navigator.mimeTypes && navigator.mimeTypes["application/x-shockwave-flash"]) ? navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin : 0;
    if (plugin) {
        var pluginversion = parseInt(plugin.description.substring(plugin.description.indexOf(".") - 1));
        if (pluginversion >= minPlayer) {
            return true;
        }
    }
    return false;
}
// vbscript check for Flash ActiveX control in windows IE
if (supportedBrowser && winIEpass) {
    document.write("<script language=VBScript>" + "\n" + "Function Flash_checkForActiveX()" + "\n" + "Dim hasPlayer, playerversion" + "\n" + "hasPlayer = false" + "\n" + "playerversion = 10" + "\n" + "Do While playerversion >= minPlayer" + "\n" + "On Error Resume Next" + "\n" + "hasPlayer = (IsObject(CreateObject(\"ShockwaveFlash.ShockwaveFlash.\" & playerversion & \"\")))" + "\n" + "If hasPlayer = true Then Exit Do" + "\n" + "playerversion = playerversion - 1" + "\n" + "Loop" + "\n" + "Flash_checkForActiveX = hasPlayer" + "\n" + "End Function" + "\n" + "</script>");
}
function Flash_checkForMinPlayer() {
    if (!supportedBrowser) {
        return false;
    }
    if (NNpass) {
        return (Flash_checkForPlugIn());
    }
    if (winIEpass) {
        return (Flash_checkForActiveX());
    }
}
function Flash_embedSWF(srcURL, swfbgColor) {
    if (!Flash_checkForMinPlayer()) {
        return;
    }
    
    
    var ring = document.getElementById("myFlashMovie");
    if (!ring) {
	    var defaultColor = "#ffffff";
	    var bgcolor = defaultColor;
	    var aux = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0" id="myFlashMovie" width="1" height="1">';
			aux += '<param name="movie" value="images/bicycle_bell.swf" />';
			aux += '<param name="quality" value=high />';
			aux += '<param name="play" value="false" />';
			aux += '<param name="bgcolor" value="#FFFFFF" />';
			aux += '<param name="loop" value="false">';
			aux += '<embed play="false" swliveconnect="true" name="myFlashMovie" src="images/bicycle_bell.swf" quality="high" bgcolor="#FFFFFF" width="1" height="1" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash">';
			aux += '</embed>';
		aux += '</object>';
	    
	    var sound = document.getElementById("sound");
	    sound.innerHTML = aux;
    }
    
    PlayFlashMovie();
}
function sonified_flash(myFrame) {
    if (!Flash_checkForMinPlayer()) {
        return;
    }
    mySwf = window.document.sonify;
    if (mySwf.PercentLoaded() < 100) {
        return;
    }
    mySwf.GotoFrame(myFrame);
    mySwf.GotoFrame(0);
}
function getFlashMovieObject(movieName) {
    if (window.document[movieName]) {
        return window.document[movieName];
    }
    if (navigator.appName.indexOf("Microsoft Internet") == -1) {
        if (document.embeds && document.embeds[movieName]) {
            return document.embeds[movieName];
        }
    } else { // if (navigator.appName.indexOf("Microsoft Internet")!=-1)
    }
    return document.getElementById(movieName);
}
function StopFlashMovie() {
    var flashMovie = getFlashMovieObject("myFlashMovie");
    flashMovie.StopPlay();
}
function PlayFlashMovie() {
    var flashMovie = getFlashMovieObject("myFlashMovie");    
    flashMovie.Play();
	//embed.nativeProperty.anotherNativeMethod();
}
function RewindFlashMovie() {
    var flashMovie = getFlashMovieObject("myFlashMovie");
    flashMovie.Rewind();
}
function NextFrameFlashMovie() {
    var flashMovie = getFlashMovieObject("myFlashMovie");
	// 4 is the index of the property for _currentFrame
    var currentFrame = flashMovie.TGetProperty("/", 4);
    var nextFrame = parseInt(currentFrame);
    if (nextFrame >= 10) {
        nextFrame = 0;
    }
    flashMovie.GotoFrame(nextFrame);
}
function ZoominFlashMovie() {
    var flashMovie = getFlashMovieObject("myFlashMovie");
    flashMovie.Zoom(90);
}
function ZoomoutFlashMovie() {
    var flashMovie = getFlashMovieObject("myFlashMovie");
    flashMovie.Zoom(110);
}
function SendDataToFlashMovie() {
    var flashMovie = getFlashMovieObject("myFlashMovie");
    flashMovie.SetVariable("/:message", document.controller.Data.value);
}
function ReceiveDataFromFlashMovie() {
    var flashMovie = getFlashMovieObject("myFlashMovie");
    var message = flashMovie.GetVariable("/:message");
    document.controller.Data.value = message;
}

