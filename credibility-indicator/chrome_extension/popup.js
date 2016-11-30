function getCurrentTabUrl(callback) {
  // Query filter to be passed to chrome.tabs.query - see
  // https://developer.chrome.com/extensions/tabs#method-query
  var queryInfo = {
    active: true,
    currentWindow: true
  };

  chrome.tabs.query(queryInfo, function(tabs) {
    var tab = tabs[0];
    var url = tab.url;
    console.assert(typeof url == 'string', 'tab.url should be a string');

    callback(url);
  });
}

function getTrust(searchTerm, callback) {
  var settings = {
    "url": "http://localhost:8000/credibility?url=" + searchTerm,
    "method": "GET",
    success: function (response) {
      //alert('its alive', JSON.stringify(response))
      //$("#output").text(response.text);
      var credibility = JSON.stringify(response.credibility);
      var text = (JSON.stringify(response.credibility).slice(0,4));
      callback(text, credibility);
      console.log(response);
    },
    error: function (response) {
      //alert('its dead', JSON.stringify(response))
      console.log(response);
      //$("#output").text(response.text);
      callback('Error: Uups, something went wrong.\n\n');
    },
  }
  //$.ajaxSetup(settings),
  console.log('before');
  return $.ajax(settings);
  console.log('after');

}

//document.addEventListener('DOMContentLoaded', function() {

//asdf
$( document ).ready(function(){
  getCurrentTabUrl(function(url) {
    // Put the image URL in Google search.
    $("#processing").text('Computing credibility for: \n\n' + url);
    getTrust(url, function(text, credibility) {
      $("#processing").css("display", "none")
      $("#output").text(text);
      var el = $("#output");
      if (credibility > 0.5) {
          el.css("color", "green");
      } else if (credibility > 0.25) {
          el.css("color", "orange");
      } else
          el.css("color", "gray");
    });
  });
});

jQuery(document).ready(function(){
        jQuery('#hideshow').on('click', function(event) {
             jQuery('#explanation').toggle('show');
        });
    });
