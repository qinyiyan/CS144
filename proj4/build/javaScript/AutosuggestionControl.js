function AutoSuggestionControl(oTextbox, oProvider){
	//use "cur" to indicate the current suggestion
	this.cur = -1;

	//use the layer property to store the <div/>
	this.layer = null;
	
	this.provider = oProvider;
	
	this.textbox = oTextbox;
	
	this.init();
}

//Initialization of the control
AutoSuggestionControl.prototype.init = function () {
	
	var oThis = this;
	
	this.textbox.onkeyup = function (oEvent) {
		if (!oEvent) {
			oEvent = window.event;
		}
		oThis.handleKeyUp(oEvent);
	};

	this.textbox.onkeydown = function (oEvent) {

		if (!oEvent) {
			oEvent = window.event;
		}

		oThis.handleKeyDown(oEvent);
	};

	this.textbox.onblur = function() {
		oThis.hideSuggestions();
	};

	this.createDropDown();
};

//Text Selection
AutoSuggestionControl.prototype.selectRange = function (iStart, iLength) {
	//For IE
	if (this.textbox.createTextRange) {
		var oRange = this.textbox.createTextRange();
		oRange.moveStart("Character", iStart);
		oRange.moveEnd("Character", iLength - this.textbox.value.length);
		oRange.select();
	} else if (this.textbox.setSelectionRange) { //For Mozilla
		this.textbox.setSelectionRange(iStart, iLength);
	}

	this.textbox.focus();
};


//Implementing Type Ahead
AutoSuggestionControl.prototype.typeAhead = function (sSuggestion) {
	//Check whether it is IE or Mozilla who support typeAhead function
	if (this.textbox.createTextRange || this.textbox.setSelectionRange) {
		var iLen = this.textbox.value.length;
		this.textbox.value = sSuggestion;
		this.selectRange(iLen, sSuggestion.length);
	}
};


//The autosuggest() Method
AutoSuggestionControl.prototype.autosuggest = function (aSuggestions, bTypeAhead) {
	
	if (aSuggestions.length > 0) {
		if (bTypeAhead){
			this.typeAhead(aSuggestions[0]);
		}
		this.showSuggestions(aSuggestions);
	} else {
		this.hideSuggestions();
	}
};

//Handle the autosuggest using keyup action
//Use keyCode to restrict the reaction for only character keys
AutoSuggestionControl.prototype.handleKeyUp = function (oEvent) {
	var iKeyCode = oEvent.keyCode;
	if (iKeyCode == 8 || iKeyCode == 46) { //backspace and delete keys
		this.provider.requestSuggestions(this, false); 
	} else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode <= 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {
		//ignore the action
	} else {
		//for all the character keys, request suggestions
		this.provider.requestSuggestions(this, true);
	}
};

AutoSuggestionControl.prototype.handleKeyDown = function (oEvent) {
	switch(oEvent.keyCode) {
		case 38://up arrow
			this.previousSuggestion();
			break;
		case 40://down arrow
			this.nextSuggestion();
			break;
		case 13://enter
			this.hideSuggestions();
			break;
	}
};


AutoSuggestionControl.prototype.hideSuggestions = function() {
	this.layer.style.visibility = "hidden";
};


AutoSuggestionControl.prototype.highlightSuggestion = function (oSuggestionNode) {

	for (var i = 0; i < this.layer.childNodes.length; i++) {
		var oNode = this.layer.childNodes[i];
		if (oNode == oSuggestionNode) {
			oNode.className = "current";
		} else if (oNode.className == "current") {
			oNode.className = "";
		}
	}
};


AutoSuggestionControl.prototype.createDropDown = function() {

	this.layer = document.createElement("div");
	this.layer.className = "suggestions";
	this.layer.style.visibility = "hidden";
	this.layer.style.width = this.textbox.offsetWidth;
	document.body.appendChild(this.layer);

	//
	var oThis = this;

	this.layer.onmousedown = this.layer.onmouseup = this.layer.onmouseover = function (oEvent) {
		oEvent = oEvent || window.event;
		oTarget = oEvent.target || oEvent.srcElement;

		if (oEvent.type == "mousedown") {
			oThis.textbox.value = oTarget.firstChild.nodeValue;
			oThis.hideSuggestions();
		} else if (oEvent.type == "mouseover") {
			oThis.highlightSuggestion(oTarget);
		} else {
			oThis.textbox.focus();
		}
	};
};


//Positioning the Dropdown List
AutoSuggestionControl.prototype.getLeft = function () {

	var oNode = this.textbox;
	var iLeft = 0;

	while (oNode.tagName != "BODY"){
		iLeft += oNode.offsetLeft;
		oNode = oNode.offsetParent;
	}

	return iLeft;
};


AutoSuggestionControl.prototype.getTop = function() {

	var oNode = this.textbox;
	var iTop = 0;

	while (oNode.tagName != "BODY"){
		iTop += oNode.offsetTop;
		oNode = oNode.offsetParent;
	}

	return iTop;
};

//Adding and Display Suggesions
AutoSuggestionControl.prototype.showSuggestions = function (aSuggestions) {

	var oDiv = null;
	this.layer.innerHTML = "";

	for  (var i = 0; i < aSuggestions.length; i++) {
		oDiv = document.createElement("div");
		oDiv.appendChild(document.createTextNode(aSuggestions[i]));
		this.layer.appendChild(oDiv);
	}

	this.layer.style.left = this.getLeft() + "px";
	this.layer.style.top = (this.getTop() + this.textbox.offsetHeight) + "px";
	this.layer.style.visibility = "visible";
};


AutoSuggestionControl.prototype.nextSuggestion = function() {
	var cSuggestionNodes = this.layer.childNodes;

	if (cSuggestionNodes.length > 0 && this.cur < cSuggestionNodes.length - 1){
		var oNode = cSuggestionNodes[++this.cur];
		this.highlightSuggestion(oNode);
		this.textbox.value = oNode.firstChild.nodeValue;
	}
};


AutoSuggestionControl.prototype.previousSuggestion = function() {
	var cSuggestionNodes = this.layer.childNodes;

	if (cSuggestionNodes.length > 0 && this.cur > 0) {
		var oNode = cSuggestionNodes[--this.cur];
		this.highlightSuggestion(oNode);
		this.textbox.value = oNode.firstChild.nodeValue;
	}
};
















