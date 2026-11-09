<?php
    

session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$response = array();
$file_name='complete_register_user_profile';

//$message = $_SESSION['message_enter'];

if ($_SERVER["REQUEST_METHOD"] == "POST") {
	
	$submitbutton= isset($_POST['Submit']);
	//$logout
		if($submitbutton){
			$fgmembersite->RedirectToURL("login.php");
		}else{

			$fgmembersite->RedirectToURL("login.php");
		}

	
}else{

	$merchant_phone_number = ($_SESSION["merchant_phone_number"]);
	$merchant_code= ($_SESSION["merchant_code"]);
	$user_login_id = ($_SESSION["user_name"]);
 
	$registration_code = ($_SESSION["code"]);

	$password = ($_SESSION["passwd"]);

	$user_login_key = ($_SESSION["type_of_user"]);
  
	


	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($user_login_id);

					//Check whether the phone has already been registered as a merchant
if($user_login_key=="LivvuUser"){
				
			$sql= "SELECT user_login_id,confirmcode,user_active FROM tbl_users WHERE user_login_id = :field_val And confirmcode = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR);
					$stmt->bindParam(':field_val1', $registration_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();

	 				// If the SQL query is succesfully performed ($result not false)
  		
					if($total > 0) {

						try
						{
								$hash = sodium_crypto_pwhash_str(
    									$password,
    									SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    									SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
								);
					
							$sql = "UPDATE tbl_users SET user_active = 'Y',confirmcode = 'Y', password = '$hash' WHERE user_login_id = :field_val AND confirmcode = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $registration_code, PDO::PARAM_STR); 
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{
								$success = "true";
								$message = "You Have Successfully Completed Your Registration.";
								$_SESSION["message_enter"]=$message;
							}
							$db->commit();
						}catch(PDOException $e){
					
								if ($e->errorInfo[1] == 1062) 
									{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='duplicate';
		 								handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										$_SESSION["message_enter"]=$message;
								
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='exception';
		 								handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										$_SESSION["message_enter"]=$message;
									}
						}//catch
					}else{
							
							$success = "false";
							$message = "Your Registration Code Is Not Correct. Please Contact Your Company For A New Registration Code.";
							$_SESSION["message_enter"]=$message;	
					}
}else{

$sql= "SELECT user_login_id,confirmcode,user_active FROM tbl_merchant_users WHERE user_login_id = :field_val And confirmcode = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR);
					$stmt->bindParam(':field_val1', $registration_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();

	 				// If the SQL query is succesfully performed ($result not false)
  		
					if($total > 0) {

						try
						{
								$hash = sodium_crypto_pwhash_str(
    									$password,
    									SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    									SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
								);
					
							$sql = "UPDATE tbl_merchant_users SET user_active = 'Y',confirmcode = 'Y', password = '$hash' WHERE user_login_id = :field_val AND confirmcode = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $registration_code, PDO::PARAM_STR); 
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{
								$success = "true";
								$message = "You Have Successfully Completed Your Registration.";
								$_SESSION["message_enter"]=$message;
							}
							$db->commit();
						}catch(PDOException $e){
					
								if ($e->errorInfo[1] == 1062) 
									{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='duplicate';
		 								handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										$_SESSION["message_enter"]=$message;
								
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='exception';
		 								handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										$_SESSION["message_enter"]=$message;
									}
						}//catch
					}else{
							
							$success = "false";
							$message = "Your Registration Code Is Not Correct. Please Contact Your Company For A New Registration Code.";
							$_SESSION["message_enter"]=$message;		
					}


}
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

			<form id='result' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset >
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">

					<label><?php echo $message ?></label><br/>
				</div>


				<div div class="col-xs-12 text-center">
					<br/>
					<input type='submit' name='Submit' value='Login' />
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
