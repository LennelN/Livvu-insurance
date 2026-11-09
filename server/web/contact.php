<?php 
    

session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");

if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	
	if($submitbutton)
	{

				$fgmembersite->RedirectToURL("index.php");

			
	}
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Livvu Investments (Pvt) Ltd">
  <meta name="keywords" content="Livvu Investments (Pvt) Ltd,Livvu,livvu">
  <meta name="author" content="Moyet Tech Systems">
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="css/bootstrap-responsive.css">
	<link rel="STYLESHEET" type="text/css" href="css/fg_membersite.css" />
  	<link rel="stylesheet" type="text/css" href="css/main.css">
	<script src="js/jquery.min.js"></script>
  	<script src="js/bootstrap.min.js"></script>
</head>
<body>
	<div id="main-wrap">
	<div id="page-wrap">
		<div><?php include("./include/declaration.php");?></div>

	<div class="row">
		<div class="col-xs-12 text-center" >    

			<form id='result' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset >
				<div class="col-xs-12 text-center">
    
				<br/>					
				<p ><u> Physical Address:</u></p>
				
						
				<p>9056/33 Pumula East</p>
						
				<p>P. O. Pumula</p>
				<p>Bulawayo</p>

									
				<p>Zimbabwe</p>	
						
				<br>
				<p><u> CONTACT NUMBERS:</u></p>			
						
				<p><a href='contact_sms.php'><span class="glyphicon glyphicon-phone"></span> <?php echo $_SESSION["contact_phone_no"]; ?> Click To Send SMS</a></p>						
				<p><a href='contact_email.php'><span class="glyphicon glyphicon-envelope"></span>livvu@gmail.com Click To Send Email</a></p>

				<p><a href='https://www.livvu.com' target="_blank"><span class="glyphicon glyphicon-envelope" ></span>www.livvu.com Click To Go To Livvu WebSite</a></p>
				
				</div>

	



	
				<div class="col-xs-12 text-center">
					<br/>
    					<input  type='submit' name='Submit' value='Main Menu' />

				</div>
 			</fieldset>
			</form>
	</div>
</div>


		

<div>   <?php include("./include/footer.php");?>
</div> 



</div>
</div>
<script>
function displayCurrentTime() 
{
  

	currentTime = new Date();
	hours = currentTime.getHours();
  
	minutes = currentTime.getMinutes();
  
	seconds = currentTime.getSeconds();
  
	amOrPm = hours < 12 ? "AM" : "PM";

  
	hours = hours === 0 ? 12 : hours > 12 ? hours - 12 : hours;
  
	hours = addZero(hours);
  
	minutes = addZero(minutes);
  
	seconds = addZero(seconds);

 
	timeString = `${hours} : ${minutes} : ${seconds} ${amOrPm}`;

  
	document.getElementById("clock").innerText = timeString;
  
	timer = setTimeout(displayCurrentTime, 1000);


}



function addZero(component) 
{
  
	return component < 10 ? "0" + component : component;

}

displayCurrentTime();



</script>
		

</body>
</html>