<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	//$logout
	if($submitbutton){
		$code  = trim($_POST['code']);
		$passwd  = trim($_POST['passwd']);
		$cpasswd  = trim($_POST['cpasswd']);

		if((strlen($code) > 0) && (strlen($passwd) > 0) && (strlen($cpasswd)>0))
		{
		
			$_SESSION["code"] = $code;
			$_SESSION["passwd"] = $passwd;
			$_SESSION["cpasswd"] = $cpasswd;
			if($passwd != $cpasswd)
			{
				$fgmembersite->HandleError("Passwords Are Not The Same. Please ReEnter Passwords");
			}else{
				//error_log(strip_tags("comingin code is  33"), 0);
				$fgmembersite->RedirectToURL("complete_register_user_profile.php");
			}
		}else{
			$fgmembersite->HandleError("Enter All Fields");
		}
	}else{

		$fgmembersite->RedirectToURL("login.php");
	}

}


if(isset($_SESSION["user_id"])) {
	$current_time = time(); 
	if(isLoginSessionExpired()) {
		header("Location:logout.php?session_expired=1");
	}
	$_SESSION['loggedin_time'] = time();
}

if(isset($_GET["session_expired"])) {
	$message = "Login Session is Expired. Please Login Again";
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
</head>
<body>

<div id="main-wrap">
	<div id="page-wrap">
	<div>
		<?php include("./include/declaration.php");?>
	</div>	
<div class="row">
		<div class="col-xs-12 text-center">    

			<form id='phone' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset >
				<legend>Complete Merchant User Registration</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
					<label style="text-align: center;" for='code' >Enter Registration Code*:</label>
    					<input type='text' name='code' id='code' value='<?php echo $fgmembersite->SafeDisplay('code') ?>' maxlength="10" /><br/><br/>
    					<span id='code_errorloc' class='error'></span>
				</div>


				<div class="col-xs-12 text-center">
					<label style="text-align: center;" for='passwd' >Enter New Password - 4 Digit PIN*:</label>
    					<input type='text' name='passwd' id='passwd' value='<?php echo $fgmembersite->SafeDisplay('passwd') ?>' maxlength="10" /><br/><br/>
    					<span id='passwd_errorloc' class='error'></span>
				</div>


				<div class="col-xs-12 text-center">
					<label style="text-align: center;" for='cpasswd' >Confirm New Password - 4 Digit PIN*:</label>
    					<input type='text' name='cpasswd' id='cpasswd' value='<?php echo $fgmembersite->SafeDisplay('cpasswd') ?>' maxlength="10" /><br/><br/>
    					<span id='cpasswd_errorloc' class='error'></span>
				</div>



				<div div class="col-xs-12 text-center">
					<br/>
    					<input type='submit' name='Submit' value='Submit' />
					<input type='submit' name='Logout' value='Cancel' /><br/><br/>
				</div>


			</fieldset>
		</form>
	</div>
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


<div>   <?php include("./include/footer.php");?>
</div>

</body>
</html>
