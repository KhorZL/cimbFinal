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
		console.log("Clicked");
		var min=0; 
    	var max=3;  
    	var random = Math.floor(Math.random() * (+max - +min)) + +min; 
    	locationItem(random);
    	console.log(random);
	} 

function locationItem(random) {
	if (locationMenu.value === 'cafe') {
		var cafe = ["cup", "knife", "spoon"];
		locationPhoto.innerText = "Please submit a picture of a " + cafe[random];
	} else if (locationMenu.value === 'garden') {
		var garden = ["vase", "bench", "pottedplant"];
		locationPhoto.innerText = "Please submit a picture of a " + garden[random];
	} else if (locationMenu.value === 'home') {
		var home = ["chair", "fork", "bowl"];
		locationPhoto.innerText = "Please submit a picture of a " + home[random];
	} else if (locationMenu.value === 'office') {
		var office = ["chair", "keyboard", "mouse"];
		locationPhoto.innerText = "Please submit a picture of a " + office[random];
	}      
}




