<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='reset_profiles';
require_once("./include/membersite_config.php");
include("./include/functions.php");
  
	$phone_no = $_POST["user_login_id_to_update"];
	$user_country = $_POST["user_country"];
 
	date_default_timezone_set('Africa/Harare');
	$time_activated = date('H:i:s');
	$date_logged_in = date('Y-m-d');
	$time_logged_in = date('H:i:s');
	$todayDate = date('Y-m-d');
	//$reset_password = "1234";
	$reset_password = mt_rand(0, 9999);
	$len_password = strlen($reset_password);
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
	$reason_deactivated = "Forgot Password Submit Activated. Reset Of Password Required";
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($phone_no);
	$request_completed='N';	
	$request_processed='N';		

	$qry = "SELECT phone_number FROM tbl_reset_passwords_requests WHERE phone_number = :field_val AND request_completed = 'N' AND request_processed = 'N' AND country = :field_val1";
	$stmt = $db->prepare($qry);
	$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
        	$stmt->execute();
	$total = $stmt->rowCount();

	if($total > 0)
	{	

						try
						{

							$todayDate = date('Y-m-d');

							$sql= "SELECT phone_number FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
							$stmt->execute();
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
							$total = $stmt->rowCount();

 							if($total > 0)
							{

								$hash = sodium_crypto_pwhash_str(
    									$reset_password,
    									SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    									SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
								);
					
								$sql = "UPDATE tbl_clients SET member_active = 'R', password = '$hash' WHERE phone_number = :field_val AND country_name = :field_val1";
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
								// execute the query
								$stmt->execute();
								$total_update =  $stmt->rowCount();
								if($total_update > 0 )//update 1 after
								{

									$sql = "UPDATE tbl_reset_passwords_requests SET request_processed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND country = :field_val1";
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
									// execute the query
									$stmt->execute();
									$total_update_tbl_reset =  $stmt->rowCount();
									if($total_update_tbl_reset > 0 )
									{

										//get the country code and ssend an sms with the password

										$phone_prefix = substr($phone_no,0,1);
										$phone_no_length = strlen($phone_no);																//error_log(strip_tags("comingin prefix is ".$phone_prefix), 0);
										if($phone_prefix == '0'){
											$phone_less_prefix = substr($phone_no,1,$phone_no_length);
										}else{
											$phone_less_prefix=$phone_no;
										}

										$qry = " SELECT country_dialling_code from tbl_country_diallling_codes WHERE country_name = :field_val";
										$stmt_qry = $db->prepare($qry);
										$stmt_qry->bindParam(':field_val', $user_country, PDO::PARAM_STR); 
										// execute the query
										$stmt_qry->execute();
										$total_qry =  $stmt_qry->rowCount();
										if($total_qry > 0)
										{
											$country_dialling_code = $stmt_qry->fetchColumn();
											$country_dialling_code= $country_dialling_code.$phone_less_prefix;
											error_log(strip_tags("comingin sms phone is ".$country_dialling_code." and country is ".$user_country), 0);
											$success = "true";
											$message = "You Have Successfully Activated Password Reset For Client Number .".$phone_no." Thank You.";
											$_SESSION["message_create"]=$message;
											$db->commit();
											return true;
										}
									}
								
								}

							}else{

								$sql= "SELECT status_flag FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
								$stmt->execute();
								$total = $stmt->rowCount();			
					
								if($total > 0)
								{
					
									$hash = sodium_crypto_pwhash_str(
    										$reset_password,
    										SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    										SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
									);
					

									$sql = "UPDATE tbl_merchant_details SET status_flag = 'R',password = '$hash' WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
									// execute the query
									$stmt->execute();
									$total_update =  $stmt->rowCount();
									if($total_update > 0 )//update 1 after
									{

										$sql = "UPDATE tbl_reset_passwords_requests SET request_processed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND country = :field_val1";
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
										// execute the query
										$stmt->execute();
										$total_update_tbl_reset =  $stmt->rowCount();
										if($total_update_tbl_reset > 0 )
										{
											
											$phone_prefix = substr($phone_no,0,1);
											$phone_no_length = strlen($phone_no);																//error_log(strip_tags("comingin prefix is ".$phone_prefix), 0);
											if($phone_prefix == '0'){
												$phone_less_prefix = substr($phone_no,1,$phone_no_length);
											}else{
												$phone_less_prefix=$phone_no;
											}

											$qry = " SELECT country_dialling_code from tbl_country_diallling_codes WHERE country_name = :field_val";
											$stmt_qry = $db->prepare($qry);
											$stmt_qry->bindParam(':field_val', $user_country, PDO::PARAM_STR); 
											// execute the query
											$stmt_qry->execute();
											$total_qry =  $stmt_qry->rowCount();
											if($total_qry > 0)
											{
												$country_dialling_code = $stmt_qry->fetchColumn();
												$country_dialling_code= $country_dialling_code.$phone_less_prefix;
												error_log(strip_tags("comingin sms phone is ".$country_dialling_code." and country is ".$login_country), 0);																										$success = "true";
												$message = "You Have Successfully Activated Password Reset For User .".$phone_no." Thank You.";
												$_SESSION["message_create"]=$message;
												$db->commit();
												return true;
											}
										}

									}
								}else{

									$sql= "SELECT user_login_id FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
									$stmt->execute();
									$total = $stmt->rowCount();

									if($total > 0)
									{

										$hash = sodium_crypto_pwhash_str(
    											$reset_password,
    											SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    											SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
										);
					
										$sql = "UPDATE tbl_users SET user_active = 'R', password = '$hash' WHERE user_login_id = :field_val AND country = :field_val1";
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
										// execute the query
										$stmt->execute();
										$total_update =  $stmt->rowCount();
										if($total_update > 0 )//update 1 after
										{
											$sql = "UPDATE tbl_reset_passwords_requests SET request_processed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND country = :field_val1";
											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
											// execute the query
											$stmt->execute();
											$total_update_tbl_reset =  $stmt->rowCount();
											if($total_update_tbl_reset > 0 )
											{

												//$phone_prefix = substr($phone_no,0,1);
												$phone_no_length = strlen($phone_no);																//error_log(strip_tags("comingin prefix is ".$phone_prefix), 0);
												$phone_less_prefix = substr($phone_no,1,$phone_no_length);	

												$qry = " SELECT country_dialling_code from tbl_country_diallling_codes WHERE country_name = :field_val";
												$stmt_qry = $db->prepare($qry);
												$stmt_qry->bindParam(':field_val', $user_country, PDO::PARAM_STR); 
												// execute the query
												$stmt_qry->execute();
												$total_qry =  $stmt_qry->rowCount();
												if($total_qry > 0)
												{
													$country_dialling_code = $stmt_qry->fetchColumn();
													$country_dialling_code= $country_dialling_code.$phone_less_prefix;
													error_log(strip_tags("comingin sms phone is ".$country_dialling_code." and country is ".$login_country), 0);


													$success = "true";
													$message = "You Have Successfully Activated Password Reset For User .".$phone_no." Thank You.";
													$_SESSION["message_create"]=$message;
													$db->commit();
													return true;
												}
											}

										}

									}else{

										$sql= "SELECT user_active FROM tbl_merchant_users WHERE user_login_id = :field_val AND country = :field_val1"; 
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR);
										$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR);  
										$stmt->execute();
										$total = $stmt->rowCount();

										if($total > 0)
										{
 
    									
											$hash = sodium_crypto_pwhash_str(
    												$reset_password,
    												SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    												SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
											);

											$sql = "UPDATE tbl_merchant_users SET user_active = 'R',password = '$hash' WHERE user_login_id = :field_val AND country = :field_val1";
											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
											// execute the query
											$stmt->execute();
											$total_update =  $stmt->rowCount();
											if($total_update > 0 )//update 1 after
											{

												$sql = "UPDATE tbl_reset_passwords_requests SET request_processed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND country = :field_val1";
												$stmt = $db->prepare($sql);
												$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
												$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
												// execute the query
												$stmt->execute();
												$total_update_tbl_reset =  $stmt->rowCount();
												if($total_update_tbl_reset > 0 )
												{

													$phone_no_length = strlen($phone_no);																//error_log(strip_tags("comingin prefix is ".$phone_prefix), 0);
													$phone_less_prefix = substr($phone_no,1,$phone_no_length);	

													$qry = " SELECT country_dialling_code from tbl_country_diallling_codes WHERE country_name = :field_val";
													$stmt_qry = $db->prepare($qry);
													$stmt_qry->bindParam(':field_val', $user_country, PDO::PARAM_STR); 
													// execute the query
													$stmt_qry->execute();
													$total_qry =  $stmt_qry->rowCount();
													if($total_qry > 0)
													{
														$country_dialling_code = $stmt_qry->fetchColumn();	
														$country_dialling_code= $country_dialling_code.$phone_less_prefix;
														error_log(strip_tags("comingin sms phone is ".$country_dialling_code." and country is ".$login_country), 0);

														$success = "true";
														$message = "You Have Successfully Activated Password Reset For User .".$phone_no." Thank You.";
														$_SESSION["message_create"]=$message;
														$db->commit();
														return true;
													}
												}
											}
										}
									}
								}
							}					
						}catch(PDOException $e){
					
								if ($e->errorInfo[1] == 1062) 
									{
										$success = "false";
		 								$message = "You Have Already Submitted Your Phone Number For Password Reset Today. Please Wait For Instructions To Reset.";
		 								$error_message=$e->getMessage();
		 								$error_type='duplicate';
		 								$_SESSION["message_create"]=$error_message;
										$db->rollBack();
										return false;
								
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$_SESSION["message_create"]=$error_message;
										$db->rollBack();
										return false;
									}
						}//catch

}else{
	$success = "false";
	$message = "Your Phone Number Has Already Been Reset For Password Change. Contact Livvu (Pvt) Ltd.";
	$_SESSION["message_create"]=$message;
	return false;

}
?>
