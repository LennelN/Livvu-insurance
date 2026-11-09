<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='reset_user_profile';
//error_log(strip_tags("comingin"), 0);
// check for required fields
if (isset($_POST['username']) && isset($_POST['user_deactivated_id']) && isset($_POST['login_country'])){
error_log(strip_tags("comingin".$file_name), 0);
	$username = $_POST["username"];
	$user_deactivated_id = $_POST["user_deactivated_id"];
	$login_country = $_POST["login_country"];
   	$reset_password = mt_rand(0, 9999);
	$len_password = strlen($reset_password);
error_log(strip_tags("password is reset password ".$reset_password ." and length is ".$len_password), 0);
	if($len_password < 4) {
	error_log(strip_tags("password is less than 4 digits "), 0);
		do{
			error_log(strip_tags("password is less than 4 digits dpig  "), 0);
			$reset_password = mt_rand(0, 9999);
			$len_password_2 =  strlen($reset_password);
			error_log(strip_tags("now password generated is ".$reset_password." and length is ".$len_password_2), 0);
			if($len_password_2 == 4){
				error_log(strip_tags("now password is equal to 4 digits "), 0);
				$result = true;
			}
		} while ($result == false);

	}
	$phone_no_length = strlen($user_deactivated_id);
	$phone_less_prefix = substr($user_deactivated_id,1,$phone_no_length);


	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

					$sql= "SELECT user_login_id FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{
							
						$db->beginTransaction();

				
						try
						{

							$dt   = new DateTime();
							//$date_paid_out = $dt->createFromFormat('d/m/Y', $deactivated_date);
							//$newDate = $date_paid_out->format('Y-m-d');
							$todayDate = date('Y-m-d');

							$hash = sodium_crypto_pwhash_str(
    									$reset_password,
    									SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    									SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
								);
					
							$sql = "UPDATE tbl_users SET user_active = 'R', password = '$hash' WHERE user_login_id = :field_val AND country = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{

								$sql = "UPDATE tbl_reset_passwords_requests SET request_processed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND country = :field_val1";
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
								// execute the query
								$stmt->execute();
								$total_update_tbl_reset =  $stmt->rowCount();
								if($total_update_tbl_reset > 0 )
								{

									$qry = " SELECT country_dialling_code from tbl_country_diallling_codes WHERE country_name = :field_val";
									$stmt_qry = $db->prepare($qry);
									$stmt_qry->bindParam(':field_val', $login_country, PDO::PARAM_STR); 
									// execute the query
									$stmt_qry->execute();
									$total_qry =  $stmt_qry->rowCount();
									if($total_qry > 0)
									{
										$country_dialling_code = $stmt_qry->fetchColumn();
										$country_dialling_code= $country_dialling_code.$phone_less_prefix;
										error_log(strip_tags("comingin sms phone is ".$country_dialling_code." and country is ".$login_country), 0);
										$success = "true";
										$message = "You Have Successfully Activated Password Reset For User .".$user_deactivated_id." Thank You.";
										array_push($response,array("success"=>$success,"message"=>$message,"smsphone"=>$country_dialling_code,"resetpassword"=>$reset_password));
										echo json_encode($response);
									}
								}
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
								$message = "User Id.".$user_deactivated_id." Does Not Exist In The System. Please Check User Id.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);


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
