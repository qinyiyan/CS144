if (window.XMLHttpRequest) {
	var xmlHttp = new XMLHttpRequest();
} else if (window.ActiveXObject) {
    var xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
} else {
    alert("Error!");
}

function StateSuggestions() {
}

StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
	var sTextboxValue = oAutoSuggestControl.textbox.value;
    var request = "suggest?q=" + encodeURI(sTextboxValue);


    xmlHttp.open("GET", request);
    xmlHttp.onreadystatechange = this.provideSuggestion(oAutoSuggestControl, bTypeAhead);
    xmlHttp.send(null);
};


StateSuggestions.prototype.provideSuggestion = function (oAutoSuggestControl, bTypeAhead) {
	return function() {
		if (xmlHttp.readyState == 4) {
			
			var aSuggestions = [];
			var completeSuggestion = xmlHttp.responseXML.getElementsByTagName("CompleteSuggestion");

			for (var i = 0; i < completeSuggestion.length; ++i) {
				var text = completeSuggestion[i].childNodes[0].getAttribute("data");
				aSuggestions.push(text);
			}

			if (aSuggestions.length> 0) {
				oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
			} else {
				oAutoSuggestControl.hideSuggestions();
			}
			
		}
	}
};
