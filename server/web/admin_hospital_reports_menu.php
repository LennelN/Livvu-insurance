<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

$phone_number = ($_SESSION["user_name"]);


if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	$submitmenu= isset($_POST['Main']);
	//$logout
	if($submitbutton){
		$choice  = trim($_POST['choice']);

		switch ($choice){

        		case 1: 
            			$_SESSION["admin_hospital_report_menu"] = "option_1";
				$fgmembersite->RedirectToURL("select_country_for_reports.php");
				
				break;

			case 2: 
				$_SESSION["admin_hospital_report_menu"] = "option_2";
				$fgmembersite->RedirectToURL("select_continent_for_reports.php");
				
				break;

			case 3: 
				$_SESSION["admin_hospital_report_menu"] = "option_3";
				$fgmembersite->RedirectToURL("select_country_for_reports.php");
				
				break;

			case 4:
				$_SESSION["admin_hospital_report_menu"] = "option_4";
				$fgmembersite->RedirectToURL("select_continent_for_reports.php");
				
				break;

			case 5: 
				$_SESSION["admin_hospital_report_menu"] = "option_5";
				$fgmembersite->RedirectToURL("select_country_for_reports.php");
				
				break;

				
			case 6: 
				$_SESSION["admin_hospital_report_menu"] = "option_6";
				$fgmembersite->RedirectToURL("select_country_date_for_reports.php");
				
				break;

			case 7: 
				$_SESSION["admin_hospital_report_menu"] = "option_7";
				$fgmembersite->RedirectToURL("select_country_date_for_reports.php");
				
				break;

			case 8: 
				$_SESSION["admin_hospital_report_menu"] = "option_8";
				$fgmembersite->RedirectToURL("select_continent_date_for_reports.php");
				
				break;

			case 9: 
				$_SESSION["admin_hospital_report_menu"] = "option_9";
				$fgmembersite->RedirectToURL("select_continent_date_for_reports.php");
				
				break;

			case 10: 
				$_SESSION["admin_hospital_report_menu"] = "option_10";
				$fgmembersite->RedirectToURL("select_country_date_for_reports.php");
				
				break;

			case 11: 
				$_SESSION["admin_hospital_report_menu"] = "option_11";
				$fgmembersite->RedirectToURL("select_continent_date_for_reports.php");
				
				break;

			case 12: 
				$_SESSION["admin_hospital_report_menu"] = "option_12";
				$fgmembersite->RedirectToURL("select_tariff_country_date_for_reports.php");
				
				break;

			case 13: 
				$_SESSION["admin_hospital_report_menu"] = "option_13";
				$fgmembersite->RedirectToURL("select_tariff_continent_date_for_reports.php");
				
				break;


			default:
				$fgmembersite->HandleError("You Entered An Invalid Choice");
				break;

			}
	}else{
		if($submitmenu){

			$main_menu = $_SESSION["main_menu"];
			if($main_menu=="main_admin"){
				$fgmembersite->RedirectToURL("main_admin.php");
			}else if($main_menu=="main_livvu"){
				$fgmembersite->RedirectToURL("main_livvu.php");
			}else{
				$fgmembersite->RedirectToURL("main_admin_accounts.php");
			}

		}else{
			$fgmembersite->RedirectToURL("logout.php");
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

			<form id='main' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset >
				<legend>Please Select</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
    					<label for='choice' >1. Bill Payment Schedule - Country:</label><br/>
					<label for='choice' >2. Bill Payment Schedule - Continent:</label><br/>
					<label for='choice' >3. Hospital Demographic Count - Country:</label><br/>
					<label for='choice' >4. Hospital Demographic Count - Continent:</label><br/>
					<label for='choice' >5. Hospital Demographic Count - CountryRegion:</label><br/>
					<label for='choice' >6. Number Of Admissions Per Month Report - Country:</label><br/>
					<label for='choice' >7. Number Of Patients Attended Per Month Report - Country:</label><br/>
					<label for='choice' >8. Number Of Admissions Per Month Report - Continent:</label><br/>
					<label for='choice' >9. Number Of Patients Attended Per Month Report - Continent:</label><br/>
					<label for='choice' >10. Cost Of Medicines Per Month Report - Country:</label><br/>
    					<label for='choice' >11. Cost Of Medicines Per Month Report - Continent:</label><br/>
					<label for='choice' >12. Cost Of Tariff Code Per Month Report - Country:</label><br/>
    					<label for='choice' >13. Cost Of Tariff Code Per Month Report - Continent:</label><br/>
		
    					<input type='text' name='choice' id='choice' value='<?php echo $fgmembersite->SafeDisplay('choice') ?>' maxlength="10" /><br/>
    					<span id='main_choice_errorloc' class='error'></span>
				</div>



				<div div class="col-xs-12 text-center">
					<br/>
    					<input type='submit' name='Submit' value='Submit' />
					<input type='submit' name='Main' value='Main Menu' />
					<input type='submit' name='Logout' value='Logout' /><br/><br/>
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
