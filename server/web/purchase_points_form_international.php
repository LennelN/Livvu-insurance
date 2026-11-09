<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

$phone_number = ($_SESSION["user_name"]);

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}


if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	$submitmenu= isset($_POST['Main']);
	//$logout
	if($submitbutton){
		$choice  = trim($_POST['choice']);
		$trans_no = mt_rand(0, 9999);
		switch ($choice){

        		case 1: // 1 //
            				$_SESSION["transaction_type"] = "Ecocash";
				$_SESSION["transaction_number"] = "EcoInt".$trans_no;
				$fgmembersite->RedirectToURL("process_points_purchased.php");
				break;

			case 2:
				$_SESSION["transaction_type"] = "AirTime";
				$_SESSION["transaction_number"] = "AirTI".$trans_no;
				$fgmembersite->RedirectToURL("process_points_purchased.php");
				break;

			case 3:
				$_SESSION["transaction_number"] = "CRInt".$trans_no;
		            		$_SESSION["transaction_type"] = "Credit Card";		
				$fgmembersite->RedirectToURL("paylane_credit_card.php");
				break;

			case 4:
				$_SESSION["transaction_number"] = "OneInt".$trans_no;
	            			$_SESSION["transaction_type"] = "OneMoney";			
				$fgmembersite->RedirectToURL("process_points_purchased.php");
				break;

			default:
				$fgmembersite->HandleError("You Entered An Invalid Choice");
				break;

			}
	}else{
		if($submitmenu){

			$main_menu = $_SESSION["main_menu"];
			
			$fgmembersite->RedirectToURL("main.php");
		
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
    					<label for='choice' >1. Debit Card:</label><br/>
					<label for='choice' >2. PayPal:</label><br/>
					<label for='choice' >3. Credit Card:</label><br/>
					<label for='choice' >4. PayNow International:</label><br/>
    				
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
