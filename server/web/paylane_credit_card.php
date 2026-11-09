<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$sessvar = $_SESSION["session_id"];

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}



if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	$createbutton= isset($_POST['Create']);
	//$logout
	if($submitbutton){
			$main_menu = $_SESSION["main_menu"];
			if($main_menu=="main_admin"){
				$fgmembersite->RedirectToURL("main_admin.php");
			}else{
				$fgmembersite->RedirectToURL("main.php");
			}
	}else{
		
			$fgmembersite->RedirectToURL("logout.php");
	}

}

if(isset($_SESSION["session_id"])) {
	
	$current_time = time(); 
	if(isLoginSessionExpired()) {
		$message = "Login Session is Expired. Please Login Again";
		header("Location:logout.php?session_expired=1");
	}
	$_SESSION['loggedin_time'] = time();
}else{

header("Location:logout.php?session_new_created=1");
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
<script src="js/jquery.min.js"></script>
  	<script src="js/bootstrap.min.js"></script>
<script src="js/PayLane.js"></script>
</head>
<body>

<div id="main-wrap">
	<div id="page-wrap">
	<div>
		<?php include("./include/declaration.php");?>
	</div>	
<div class="row">
		<div class="col-xs-12 text-center">    

			<form id='checkout_form' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset >
				<legend>Process PayLane Payments</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='first-name' >Enter First Name*:</label>
    					<input class="col-xs-9 text-center" type='text' name='first-name' id='first-name' value='<?php echo $fgmembersite->SafeDisplay('first-name') ?>' maxlength="30" /><br/>
    					<span id='checkout_form_first_name_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='last-name' >Enter Last Name*:</label>
    					<input class="col-xs-9 text-center" type='text' name='last-name' id='last-name' value='<?php echo $fgmembersite->SafeDisplay('last-name') ?>' maxlength="30" /><br/>
    					<span id='checkout_form_last_name_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='email' >Enter Email Address*:</label>
    					<input class="col-xs-9 text-center" type='text' name='email' id='email' value='<?php echo $fgmembersite->SafeDisplay('email') ?>' maxlength="30" /><br/>
    					<span id='checkout_form_email_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='address' >Enter Address*:</label>
    					<input class="col-xs-9 text-center" type='text' name='address' id='address' value='<?php echo $fgmembersite->SafeDisplay('address') ?>' maxlength="30" /><br/>
    					<span id='checkout_form_address_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='cc-number' >Enter Credit Card Number*:</label>
    					<input class="col-xs-9 text-center" data-paylane="cc-number" type='text' name='cc-number' id='cc-number' value='<?php echo $fgmembersite->SafeDisplay('cc-number') ?>' maxlength="16" /><br/>
    					<span id='checkout_form_cc_number_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='cc-expiry-month' >Enter Expiry Month*:</label>
    					<input class="col-xs-9 text-center" type='text' data-paylane="cc-expiry-month" name='cc-expiry-month' id='cc-expiry-month' value='<?php echo $fgmembersite->SafeDisplay('cc-expiry-month') ?>' maxlength="20" /><br/>
    					<span id='checkout_form_cc_expiry_month_errorloc' class='error'></span>
				</div>


				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='cc-expiry-year' >Enter Expiry Year*:</label>
    					<input class="col-xs-9 text-center" type='text' data-paylane="cc-expiry-year" name='cc-expiry-year' id='cc-expiry-year' value='<?php echo $fgmembersite->SafeDisplay('cc-expiry-year') ?>' maxlength="20" /><br/>
    					<span id='checkout_form_cc_expiry_year_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='cc-cvv' >Enter CVV Number*:</label>
    					<input class="col-xs-9 text-center" type='text' data-paylane="cc-cvv" name='cc-cvv' id='cc-cvv' value='<?php echo $fgmembersite->SafeDisplay('cc-cvv') ?>' maxlength="20" /><br/>
    					<span id='checkout_form_cc_cvv_errorloc' class='error'></span>
				</div>

				
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='cc-name-on-card' >Enter Name On Card*:</label>
    					<input class="col-xs-9 text-center" type='text' data-paylane="cc-name-on-card" name='cc-name-on-card' id='cc-name-on-card' value='<?php echo $fgmembersite->SafeDisplay('cc-name-on-card') ?>' maxlength="30" /><br/>
    					<span id='checkout_form_cc_name_on_card_errorloc' class='error'></span>
				</div>


				<div div class="col-xs-12 text-center">
					<br/>
    					<input  type='button' name='Create' value='Process Payment' onclick='getCellData()'/><br/>
					<input type='submit' name='Submit' value='Main Menu' />
					<input type='submit' name='Logout' value='Logout' /><br/><br/>

				</div>


			</fieldset>
		</form>
	</div>
</div>

</div>
</div>
<script>
    

function getCellData(){
	try
    
	{
      
		
		var client = new PayLaneClient({
            
			publicApiKey : 'YOUR_PAYLANE_PUBLIC_KEY',
            
			paymentForm  : 'checkout_form',
        
			});
    

	}
    
	catch (e)
    
	{
 
		     
		console.log(e); // exceptions are fatal
    
	}

}
 
</script>

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
