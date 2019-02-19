
		var config = {
	    apiKey: "AIzaSyC1mQuCZL1QmVJpHzLyRt05ehFI96mxjcU",
	    authDomain: "cimb2-53164.firebaseapp.com",
	    databaseURL: "https://cimb2-53164.firebaseio.com",
	    projectId: "cimb2-53164",
	    storageBucket: "cimb2-53164.appspot.com",
	    messagingSenderId: "722590458766"
	  };
	  firebase.initializeApp(config);

var myFirebase = firebase.database().ref();
var website = myFirebase.child("Benjamin");

var locationPhoto = document.getElementById('locationPhoto');  
var submitButton = document.getElementById('submitButton');
var locationMenu = document.getElementById('locationMenu');
var locationChange = document.getElementById('locationChange');

submitButton.onclick = function() {
	submitButton.style.display = 'none';  
    locationPhoto.style.display = 'block';
	locationChange.style.display = 'block';   
	locationItem(0);
}

locationChange.onclick = function() {
		var min=0; 
    	var max=3;  
    	var random = Math.floor(Math.random() * (+max - +min)) + +min; 
    	locationItem(random);
	} 

function locationItem(random) {
	if (locationMenu.value === 'cafe') {
		var cafe = ["cup", "knife", "spoon"];
		website.set({"target": cafe[random]});
		locationPhoto.innerText = "Please submit a picture of a " + cafe[random];
	} else if (locationMenu.value === 'garden') {
		var garden = ["vase", "bench", "pottedplant"];
		website.set({"target": garden[random]});
		locationPhoto.innerText = "Please submit a picture of a " + garden[random];
	} else if (locationMenu.value === 'home') {
		var home = ["chair", "fork", "bowl"];
		website.set({"target": home[random]});
		locationPhoto.innerText = "Please submit a picture of a " + home[random];
	} else if (locationMenu.value === 'office') {
		var office = ["chair", "keyboard", "mouse"];
		website.set({"target": office[random]});
		locationPhoto.innerText = "Please submit a picture of a " + office[random];
	}      
}

var result = website.child("result");
result.on("value", function(snapshot) {
	console.log(snapshot.val());
   if (snapshot.val() === 1) {
   		window.location.href = 'TransactionSummary1.html';
   } else if (snapshot.val() === 0) {
   		window.location.href = 'TransactionFailed.html';
   }
}, function (error) {
   console.log("Error: " + error.code);
});




