<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_individual_client_otp';

$response = array();

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
   
if ($_SERVER["REQUEST_METHOD"] == "POST") {
	

	$submitbutton= isset($_POST['Submit']);
	
	//$logout
	if($submitbutton){
		error_log(strip_tags("login seesion password now is ".$_SESSION["otp_login_password"]), 0);	
		$choice  = trim($_POST['choice']);
		if(strlen($choice) > 0 )
		{
			$_SESSION["main_menu"] = "main";
			if($choice == $_SESSION["otp_login_password"])
			{ 			
				$fgmembersite->RedirectToURL("main.php");
			}else{

				$_SESSION['message_create']="One Time Password Entered Does Not Match The Password Entered:";
				$fgmembersite->RedirectToURL("confirm_login_messages.php");
			}
		}else{
			$_SESSION['message_create']="Please Enter One Time Password Sent Through SMS";
			$fgmembersite->RedirectToURL("confirm_login_messages.php");
		}

	}else{
		
			$fgmembersite->RedirectToURL("logout.php");
	}

}else{

//error_log(strip_tags("Error logging in. missing fields"), 0);
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$reset_password = mt_rand(0, 9999);
	$len_password = strlen($reset_password);
	
	if($len_password < 4) {
	
		do{
			
			$reset_password = mt_rand(0, 9999);
			$len_password_2 =  strlen($reset_password);
			
			if($len_password_2 == 4){
				
				$result = true;
			}
		} while ($result == false);

	}
	$username = $_SESSION["user_name"];
	$country_name=$_SESSION["country_name"]; 
	$login_country=$_SESSION["login_country"];
	$name_of_user=$_SESSION["name_of_user"];
	//error_log(strip_tags("session variables is ".$username." and country name is ".$country_name." and login country ".$login_country." and name of user is ".$name_of_user), 0);

	$ddate = Date("Y-m-d");
	$sql= "SELECT phone_number,MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val1 AND purchase_country = :field_val2"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val1', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val2', $login_country, PDO::PARAM_STR);

	$stmt->execute();
	$total = $stmt->rowCount();

	if($total > 0) 
	{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row)
		{    							
			$client_phone_number_returned=$row['phone_number'];
			$client_due_date_returned=$row['MAX(due_date)'];
		}

		//error_log(strip_tags("comingin due date is ".$client_due_date_returned), 0);
		$sql_year= "SELECT phone_number,MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2"; 
		$stmt_year = $db->prepare($sql_year);
		$stmt_year->bindParam(':field_val1', $username, PDO::PARAM_STR); 
		$stmt_year->bindParam(':field_val2', $login_country, PDO::PARAM_STR);

		$stmt_year->execute();
		$total_year = $stmt_year->rowCount();

		if($total_year > 0) 
		{
			$result_year = $stmt_year->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result_year as $row)
			{    							
				$client_phone_number_returned=$row['phone_number'];
				$client_end_date_returned=$row['MAX(end_date)'];
			}
		}
		
		//error_log(strip_tags("comingin due date is ".$client_end_date_returned), 0);
		if($ddate > $client_due_date_returned){
			$message = " Your Account Is Not Up To Date. Please Pay Your Subscriptions To Update Your Account";
		}else{
			//$sql_balance= "SELECT phone_number,amount FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2 AND end_date = //:field_val3"; 
//			$stmt_balance = $db->prepare($sql_balance);
//			$stmt_balance->bindParam(':field_val1', $username, PDO::PARAM_STR); 
//			$stmt_balance->bindParam(':field_val2', $login_country, PDO::PARAM_STR);
//			$stmt_balance->bindParam(':field_val3', $client_end_date_returned, PDO::PARAM_STR);

			$sql_balance= "SELECT phone_number,amount FROM tbl_no_of_points WHERE phone_number = :field_val1 AND country = :field_val2"; 
			$stmt_balance = $db->prepare($sql_balance);
			$stmt_balance->bindParam(':field_val1', $username, PDO::PARAM_STR); 
			$stmt_balance->bindParam(':field_val2', $login_country, PDO::PARAM_STR);


			$stmt_balance->execute();
			$total_balance = $stmt_balance->rowCount();

			if($total_balance > 0) 
			{
				$result_balance = $stmt_balance->fetchAll(PDO::FETCH_ASSOC); 
    				foreach($result_balance as $row)
				{    							
					$client_phone_number_returned=$row['phone_number'];
					$client_balance_returned=$row['amount'];
				}
			}

			
			$message = " Your Account Is Up To Date. Thank You. Your balance is ".$client_balance_returned;
		}
	}else{

		$message = " Your Subscription Is Not Up To Date. Please Pay Your Subscriptions To Update Your Account.";
	}
	


	$_SESSION["otp_login_password"] = $reset_password;	
	error_log(strip_tags("login passord is ".$reset_password), 0);
	//sms:+263772455025?body=Hello%20World!";
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
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend><br/>

				<legend><?php echo $message ?></legend><br/>

			</fieldset>

			<fieldset >
				<legend>Please Enter One Time Password Sent Through SMS</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
    					<input type='text' name='choice' id='choice' value='<?php echo $fgmembersite->SafeDisplay('choice') ?>' maxlength="4" /><br/>
    					<span id='phone_choice_errorloc' class='error'></span>
				</div>


<!-- client-side Form Validations:
Uses the excellent form validation script from JavaScript-coder.com-->

<script type='text/javascript'>
// <![CDATA[
   // var pwdwidget = new PasswordWidget('thepwddiv','password');
    //pwdwidget.MakePWDWidget();
    var frmvalidator  = new Validator("phone");
    frmvalidator.EnableOnPageErrorDisplay();
    frmvalidator.EnableMsgsTogether();

    	frmvalidator.addValidation("choice","num","Please Enter One Time Password");
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



