<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='complete_register_user_profile';
//error_log(strip_tags("comingin"), 0);
// check for required fields
if (isset($_POST['user_login_id']) && isset($_POST['registration_code']) && isset($_POST['password']) && isset($_POST['user_login_key'])){

	$user_login_id = $_POST["user_login_id"];
 
   	$registration_code = $_POST["registration_code"];

	$password = $_POST["password"];

	$user_login_key = $_POST["user_login_key"];
  



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
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
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
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
								
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='exception';
		 								handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									}
						}//catch
					}else{
							
							$success = "false";
							$message = "Your Registration Code Is Not Correct. Please Contact Your Company For A New Registration Code.";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);		
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
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
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
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
								
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='exception';
		 								handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									}
						}//catch
					}else{
							
							$success = "false";
							$message = "Your Registration Code Is Not Correct. Please Contact Your Company For A New Registration Code.";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);		
					}


}

} else {
    // required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}


?>
