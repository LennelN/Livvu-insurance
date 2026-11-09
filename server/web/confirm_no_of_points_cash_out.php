<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}


$phone_number = ($_SESSION["user_name"]);
//$transfer_phone_number = ($_SESSION["transfer_phone_no"]);
$no_of_points_cash_out = ($_SESSION["no_of_points_cash_out"]);

$message = "Are Yo Sure You Want To Cash Out ". $no_of_points_cash_out . ". Enter 1 To Confirm 2 To Cancel";


if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	//$logout
	if($submitbutton){
		$choice  = trim($_POST['choice']);
		if(strlen($choice) > 0 )
		{
			if($choice == 0)
			{
				$fgmembersite->HandleError("Enter Valid Choice");
			}else{

				if($choice == 1)
				{
					$fgmembersite->RedirectToURL("cash_out_points.php");
				}else{
					
					$main_menu = $_SESSION["main_menu"];
						
					if($main_menu=="main_merchant")
					{
						$fgmembersite->RedirectToURL("main_merchant.php");
					}else{
						$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");
					}
				}
			}
		}else{
			$fgmembersite->HandleError("Enter Valid Choice");
		}

		
	}else{

			$main_menu = $_SESSION["main_menu"];
			error_log(strip_tags("main menu passed is".$main_menu), 0);	
			if($main_menu=="main_merchant")
			{
				$fgmembersite->RedirectToURL("main_merchant.php");
			}else{
				$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");
			}
		//$fgmembersite->RedirectToURL("main_merchant.php");
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

			<form id='confirm' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset disabled>

				<legend>Confirm Cash Out</legend>
				<br/>
				<div class="col-xs-12 text-center">
    					<label><?php echo $message ?> </label><br/>
					<br/>
				</div>

			</fieldset>

			<fieldset >
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">

					<label for='choice' >1. Yes</label><br/>
					<label for='choice' >2. No</label><br/>

    					<input type='text' name='choice' id='choice' value='<?php echo $fgmembersite->SafeDisplay('choice') ?>' maxlength="10" /><br/>
    					<span id='confirm_choice_errorloc' class='error'></span>
				</div>

<!-- client-side Form Validations:
Uses the excellent form validation script from JavaScript-coder.com-->

<script type='text/javascript'>
// <![CDATA[
   // var pwdwidget = new PasswordWidget('thepwddiv','password');
    //pwdwidget.MakePWDWidget();
    var frmvalidator  = new Validator("confirm");
    frmvalidator.EnableOnPageErrorDisplay();
    frmvalidator.EnableMsgsTogether();

    	frmvalidator.addValidation("choice","num","Please Enter A Valid Choice");
	//frmvalidator.addValidation("choice","req","Please Enter A Valid Phone Number");
    
// ]]>
</script>


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
