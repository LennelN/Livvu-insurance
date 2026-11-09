<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";


if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	$submitmenu= isset($_POST['Main']);
	//$logout
	if($submitbutton){
		if($fgmembersite->RegisterIndividualSession())
   		{
        		$fgmembersite->RedirectToURL("thank-you.html");
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
<script src="js/jquery.min.js"></script>
  	<script src="js/bootstrap.min.js"></script>
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
			
			<fieldset >
				<legend>Press Accept To Accept The Terms And Conditions Or Press Decline To Cancel Registration</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
    					<label for='choice' >1. You Agree To Contribute Monthly:</label><br/>
					<label for='choice' >2. You Agree To Pay A Penalty:</label><br/>
    					<label for='choice' >3. You Agree To Pay 1 point To Help Other Members In Your Group:</label><br/>
    			
    					<span id='main_choice_errorloc' class='error'></span>
				</div>



				<div div class="col-xs-12 text-center">
					<br/>
    					<input type='submit' name='Submit' value='Accept' />
					<input type='submit' name='Decline' value='Decline' />
<br/>
<br/>
				</div>


			</fieldset>
		</form>
	</div>
</div>

</div>
</div>


</body>
</html>
