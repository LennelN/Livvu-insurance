<?php
	session_start();
	require_once("./include/membersite_config.php");
	include("./include/functions.php");

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}




if (isset($_POST['username']) && isset($_POST['password'])) {
   
    
	$username = $_POST["username"];
    
	$password = $_POST["password"];
   
	
	error_log(strip_tags("Error logging in. missing fieldsinside inside"), 0);
	error_log(strip_tags($username), 0);

	if($fgmembersite->Login())
   	{
	
		if(!isLoginSessionExpired()) {
        		//$fgmembersite->RedirectToURL("main.php");
		}else{
			header("Location:logout.php?session_expired=1");
		}
	
   	}else{

$fgmembersite->RedirectToURL("700.html");
}
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {

$choice  = trim($_POST['choice']);

	switch ($choice){

        	case 1: // 1 //
            		error_log(strip_tags("option 1"), 0);
			break;

		case 2:
			error_log(strip_tags("option 2"), 0);
			break;
		
		case 3:
			error_log(strip_tags("option 3"), 0);
			break;

		case 4:
			error_log(strip_tags("option 4"), 0);
			$phone_number = isset($_SESSION["user_name"]);
			error_log(strip_tags("option in main"), 0);
			error_log(strip_tags($phone_number), 0);
			$phone_number2 = isset($_SESSION['user_name']);
			error_log(strip_tags("option in main2"), 0);
			error_log(strip_tags($phone_number2), 0);

			$fgmembersite->RedirectToURL("check_points_balance_html.php");
			break;

		case 5:
			error_log(strip_tags("option 5"), 0);
			break;

		default:
			error_log(strip_tags("Invalid Option"), 0);
			break;

		}

//$fgmembersite->RedirectToURL("balance.php");
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
				<div class="col-xs-12 text-center">
    					<label for='username' >1. Phone Number:</label><br/>    				
    					<input type='text' name='username' id='username' value="<?php echo $username ?>" maxlength="10" /><br/>
    					<span id='login_username_errorloc' class='error'></span>
					<br/>
				</div>

			</fieldset>

			<fieldset >
				<legend>Please Select</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				
				<div class="col-xs-12 text-center">
					<br/>
    					<label for='choice' >1. Purchase Points:</label><br/>
					<label for='choice' >2. Transfer Points:</label><br/>
    					<label for='choice' >3. Pay Your Bill:</label><br/>
    					<label for='choice' >4. Check Balance:</label><br/>
    					<label for='choice' >5. Account Statement:</label><br/>
    				
    					<input type='text' name='choice' id='choice' value='<?php echo $fgmembersite->SafeDisplay('choice') ?>' maxlength="10" /><br/>
    					<span id='login_username_errorloc' class='error'></span>
				</div>


				<div div class="col-xs-12 text-center">
    					<input type='submit' name='Submit' value='Submit' /><br/>
				</div>


			</fieldset>
		</form>
	</div>
</div>

</div>
</div>

<div>   <?php include("./include/footer.php");?>
</div>
</body>
</html>
