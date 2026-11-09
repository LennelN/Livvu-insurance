<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	$submitlogin= isset($_POST['Cancel']);
	//$logout
	if($submitbutton){
		$choice  = trim($_POST['choice']);

		switch ($choice){

        		case 1: // 1 //
       
				$fgmembersite->RedirectToURL("register_individual.php");
				break;

			case 2:
				//error_log(strip_tags("option 2"), 0);
				$fgmembersite->RedirectToURL("register_health_institutions.php");
				break;
		
			case 3:
				//error_log(strip_tags("option 3"), 0);
				$fgmembersite->RedirectToURL("register_company.php");
				break;

			case 4:
				//error_log(strip_tags("option 4"), 0);
				$fgmembersite->RedirectToURL("register_society_group.php");
				break;


			default:
				//error_log(strip_tags("Invalid Option"), 0);
				$fgmembersite->HandleError("Invalid Option");
				break;

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

			<form id='login' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			
			<fieldset >
				<legend>Please Select</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
    					<label for='choice' >1. Individual:</label><br/>
					<label for='choice' >2. Health Institutions:</label><br/>
    					<label for='choice' >3. Company:</label><br/>
    					<label for='choice' >4. Society/Groups:</label><br/>

    					<input type='text' name='choice' id='choice' value='<?php echo $fgmembersite->SafeDisplay('choice') ?>' maxlength="10" /><br/>
    					<span id='login_username_errorloc' class='error'></span>
				</div>


				<div div class="col-xs-12 text-center">
					<br/>
    					<input type='submit' name='Submit' value='Submit' />
					<input type='submit' name='Cancel' value='Cancel' />
					
					<br/><br/>
				</div>


			</fieldset>
		</form>
	</div>
</div>

</div>
</div>


</body>
</html>
