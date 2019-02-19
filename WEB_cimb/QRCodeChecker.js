var config = {
      apiKey: "AIzaSyC1mQuCZL1QmVJpHzLyRt05ehFI96mxjcU",
      authDomain: "cimb2-53164.firebaseapp.com",
      databaseURL: "https://cimb2-53164.firebaseio.com",
      projectId: "cimb2-53164",
      storageBucket: "cimb2-53164.appspot.com",
      messagingSenderId: "722590458766"
    };
    firebase.initializeApp(config);

var delayInMilliseconds = 5000; //5 second

var myFirebase = firebase.database().ref();
var website = myFirebase.child("Benjamin");

var result = website.child("same");
result.on("value", function(snapshot) {
  console.log(snapshot.val());
   if (snapshot.val() === 1) {
       setTimeout(function() {
        //your code to be executed after 1 second
        }, delayInMilliseconds);
       window.location.href = 'SameNetwork.html';
   } else if (snapshot.val() === 0) {
       setTimeout(function() {
        //your code to be executed after 1 second
        }, delayInMilliseconds);
       window.location.href = 'DiffNetwork.html';
   }
}, function (error) {
        setTimeout(function() {
         //your code to be executed after 1 second
        }, delayInMilliseconds);
   console.log("Error: " + error.code);
});
website.child("same").remove();