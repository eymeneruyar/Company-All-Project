// JQUERY
$(function() {
	
	var images = ['https://wallpapercave.com/wp/wp5320845.jpg', 'https://wallpapercave.com/wp/wp5320845.jpg'];

   $('#container').append('<style>#container, .acceptContainer:before, #logoContainer:before {background: url(' + images[Math.floor(Math.random() * images.length)] + ') center fixed }');
	
});