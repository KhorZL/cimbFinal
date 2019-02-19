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

var result = website.child("tempresult");
result.on("value", function(snapshot) {
	console.log(snapshot.val());
   if (snapshot.val() === 1) {
        website.child("tempresult").remove();
   		window.location.href = 'TransactionSummary.html';
   } else if (snapshot.val() === 0) {
        website.child("tempresult").remove();
   		window.location.href = 'TransactionFailedSame.html';
   }
}, function (error) {
    website.child("tempresult").remove();
   console.log("Error: " + error.code);
});




