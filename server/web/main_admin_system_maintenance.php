<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	//$logout
	if($submitbutton){

		$choice  = trim($_POST['choice']);

		switch ($choice){

        	case 1: // 1 //
            		
			$fgmembersite->RedirectToURL("main_admin_system_maintenance_tariff.php");
			break;

		case 2:
			$fgmembersite->RedirectToURL("main_admin_system_maintenance_currency.php");
			break;
		
		case 3:
			$fgmembersite->RedirectToURL("mantain_country_code_details.php");
			break;

		case 4:
			$fgmembersite->RedirectToURL("mantain_type_health_institution.php");
			break;

		case 5:
			$fgmembersite->RedirectToURL("main_admin_system_maintenance_contributing_rate.php");
			break;

		case 6:
			$fgmembersite->RedirectToURL("mantain_country_region_continent_details.php");
			break;

		case 7:
			$fgmembersite->RedirectToURL("main_user_maintenance.php");
			break;

		case 8:
			$fgmembersite->RedirectToURL("main_user_merchant_maintenance.php");
			break;

		case 9:
			$fgmembersite->RedirectToURL("main_client_account_maintenance.php");
			break;

		case 10:
			$fgmembersite->RedirectToURL("get_reset_passwords_phone_numbers.php");
			break;

		default:
			
			$fgmembersite->HandleError("You Entered An Invalid Option");
			break;

		}
	}else{
		$main_menu = $_SESSION["main_menu"];
			if($main_menu=="main_admin"){
				$fgmembersite->RedirectToURL("main_admin.php");
			}else if($main_menu=="main_livvu"){
				$fgmembersite->RedirectToURL("main_livvu.php");
			}else{
				$fgmembersite->RedirectToURL("main_admin_accounts.php");
			}
	
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

			<form id='login' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset >
				<legend>Please Select</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
    					<label for='choice' >1. Ancillary Tariff Maintenance</label><br/>
					<label for='choice' >2. Currency Rates Maintenance</label><br/>
    					<label for='choice' >3. Country Code Maintenance</label><br/>
					<label for='choice' >4. Health Institution Maintenance</label><br/>
					<label for='choice' >5. Country Year Contributions Maintenance</label><br/>
					<label for='choice' >6. Country Region Maintenance</label><br/>
					<label for='choice' >7. User System Maintenance</label><br/>
					<label for='choice' >8. Merchant System Maintenance</label><br/>
					<label for='choice' >9. Client Account System Maintenance</label><br/>
					<label for='choice' >10. Password Reset Request Numbers</label><br/>
    					<input type='text' name='choice' id='choice' value='<?php echo $fgmembersite->SafeDisplay('choice') ?>' maxlength="10" /><br/>
    					<span id='login_username_errorloc' class='error'></span>
				</div>


				<div div class="col-xs-12 text-center">
					<br/>
    					<input type='submit' name='Submit' value='Submit' />
					<input type='submit' name='Main' value='Main Menu' /><br/><br/>
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
