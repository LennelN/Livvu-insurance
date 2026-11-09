<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='enter_password_reset_phone_number';
require_once("./include/membersite_config.php");
include("./include/functions.php");
  
	$phone_no = $_POST["user_login_id_to_update"];
	$login_country = $_POST["user_login_country"];
// error_log(strip_tags("now entering fgfgfg login_country  ".$login_country), 0); 
	date_default_timezone_set('Africa/Harare');
	$time_activated = date('H:i:s');
	$date_logged_in = date('Y-m-d');
	$time_logged_in = date('H:i:s');
	$todayDate = date('Y-m-d');
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

	$qry = "SELECT phone_number FROM tbl_reset_passwords_requests WHERE phone_number = :field_val AND request_completed = 'N' AND country = :field_val1";
	$stmt = $db->prepare($qry);
	$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
        $stmt->execute();
	$total = $stmt->rowCount();

	if($total <= 0)
	{	
	
						try
						{

							$todayDate = date('Y-m-d');

							$sql= "SELECT phone_number FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
							$stmt->execute();
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
							$total = $stmt->rowCount();

 							if($total > 0)
							{//1

								//deactivate client
								$sql = "UPDATE tbl_clients SET member_active = 'N' WHERE phone_number = :field_val AND country_name = :field_val1";
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
								// execute the query
								$stmt->execute();
								$total_update =  $stmt->rowCount();
								if($total_update > 0 )//update 1 after
								{//2

									$stmt = $db->prepare("INSERT INTO tbl_client_deactivation_details(phone_number, date_deactivated,time_deactivated,reason_deactivated,client_deactivated_by,country) VALUES (:phone_number, :date_deactivated, :time_deactivated, :reason_deactivated, :client_deactivated_by,:country)");

									$stmt->bindParam(':phone_number', $phone_no);
									$stmt->bindParam(':date_deactivated', $date_logged_in);
									$stmt->bindParam(':time_deactivated', $time_logged_in);
									$stmt->bindParam(':reason_deactivated', $reason_deactivated);
									$stmt->bindParam(':client_deactivated_by', $phone_no);
									$stmt->bindParam(':country', $login_country);
										
									$stmt->execute();
									$total =  $stmt->rowCount();
									if($total > 0)
									{//3
										$stmt = $db->prepare("INSERT INTO tbl_reset_passwords_requests(phone_number, request_date, request_processed, request_completed, request_processed_by,country) VALUES (:phone_number,:request_date,:request_processed,:request_completed,:request_processed_by,:country)");

										$stmt->bindParam(':phone_number', $phone_no);
										$stmt->bindParam(':request_date', $todayDate);
										$stmt->bindParam(':request_processed', $request_processed);
										$stmt->bindParam(':request_completed', $request_completed);
										$stmt->bindParam(':request_processed_by', $phone_no);
										$stmt->bindParam(':country', $login_country);
										
										$stmt->execute();
										$total =  $stmt->rowCount();
	
										if($total > 0)
										{//4
											$success = "true";
											$message = "You Have Successfully Submitted Your Phone Number For Password Reset. Please Wait For Instructions To Reset Your Password: ";
											$_SESSION["message_create"]=$message;
											$db->commit();
											return true;
										}//4
								
									}//3
								}//2


							}else{//1a

								$sql= "SELECT status_flag FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
								$stmt->execute();
								$total = $stmt->rowCount();			
					
								if($total > 0)
								{//2a
					
									$sql = "UPDATE tbl_merchant_details SET status_flag = 'N' WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
									// execute the query
									$stmt->execute();
									$total_update =  $stmt->rowCount();
									if($total_update > 0 )//update 1 after
									{//2b

										$qry = "Select merchant_name, merchant_code from tbl_merchant_details where merchant_phone_number = '$phone_no' AND merchant_country = '$login_country'";
	 					
										$stmt = $db->prepare($qry);
        										$stmt->execute();
										$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
										$total =  $stmt->rowCount();
										foreach($result as $row){
											$merchant_name = $row['merchant_name'];
   
											$merchant_code = $row['merchant_code'];
 
											}	
												
										$stmt = $db->prepare("INSERT INTO tbl_merchant_deactivation_details(merchant_phone_number, merchant_name, merchant_code, date_deactivated,time_deactivated,reason_deactivated,user_deactivated_by,merchant_country) VALUES (:merchant_phone_number, :merchant_name, :merchant_code, :date_deactivated, :time_deactivated, :reason_deactivated, :user_deactivated_by,:merchant_country)");

										$stmt->bindParam(':merchant_phone_number', $phone_no);
										$stmt->bindParam(':merchant_name', $merchant_name);
										$stmt->bindParam(':merchant_code', $merchant_code);
										$stmt->bindParam(':date_deactivated', $todayDate);
										$stmt->bindParam(':time_deactivated', $time_logged_in);
										$stmt->bindParam(':reason_deactivated', $reason_deactivated);
										$stmt->bindParam(':user_deactivated_by', $phone_no);
										$stmt->bindParam(':merchant_country', $login_country);
										
										$stmt->execute();
										$total =  $stmt->rowCount();

										if($total > 0 )//update 1 after
										{//2c
											$stmt = $db->prepare("INSERT INTO tbl_reset_passwords_requests(phone_number, request_date, request_processed, request_completed, request_processed_by,country) VALUES (:phone_number,:request_date,:request_processed,:request_completed,:request_processed_by,:country)");

											$stmt->bindParam(':phone_number', $phone_no);
											$stmt->bindParam(':request_date', $todayDate);
											$stmt->bindParam(':request_processed', $request_processed);
											$stmt->bindParam(':request_completed', $request_completed);
											$stmt->bindParam(':request_processed_by', $phone_no);
											$stmt->bindParam(':country', $login_country);
										
											$stmt->execute();
											$total =  $stmt->rowCount();
	
											if($total > 0)
											{//2d
												$success = "true";
												$message = "You Have Successfully Submitted Your Phone Number For Password Reset. Please Wait For Instructions To Reset Your Password: ";
												$_SESSION["message_create"]=$message;
												$db->commit();
												return true;
											}//2d

										}//2c

									}//2b
								}else{//2a

									$sql= "SELECT user_login_id FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
									$stmt->execute();
									$total = $stmt->rowCount();

									if($total > 0)
									{//3a

										$sql = "UPDATE tbl_users SET user_active = 'N',date_deactivated = '$date_logged_in', reason_deactivated = '$reason_deactivated',user_deactivated_by = '$phone_no',user_deactivated = 'Y' WHERE user_login_id = :field_val AND country = :field_val1";
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR);
										$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);  
										// execute the query
										$stmt->execute();
										$total_update =  $stmt->rowCount();
										if($total_update > 0 )//update 1 after
										{//3b
											$stmt = $db->prepare("INSERT INTO tbl_reset_passwords_requests(phone_number, request_date, request_processed, request_completed, request_processed_by,country) VALUES (:phone_number,:request_date,:request_processed,:request_completed,:request_processed_by,:country)");

											$stmt->bindParam(':phone_number', $phone_no);
											$stmt->bindParam(':request_date', $todayDate);
											$stmt->bindParam(':request_processed', $request_processed);
											$stmt->bindParam(':request_completed', $request_completed);
											$stmt->bindParam(':request_processed_by', $phone_no);
											$stmt->bindParam(':country', $login_country);
										
											$stmt->execute();
											$total =  $stmt->rowCount();
	
											if($total > 0)
											{//3c
												$success = "true";
												$message = "You Have Successfully Submitted Your Phone Number For Password Reset. Please Wait For Instructions To Reset Your Password: ";
												$_SESSION["message_create"]=$message;
												$db->commit();
												return true;
											}//3c

										}//3b

									}else{//3a

										$sql= "SELECT user_active FROM tbl_merchant_users WHERE user_login_id = :field_val AND country=:field_val1 "; 
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR);
										$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);  
										$stmt->execute();
										$total = $stmt->rowCount();

										if($total > 0)
										{//4a
 
    									
											$sql = "UPDATE tbl_merchant_users SET user_active = 'N',date_deactivated = '$date_logged_in', reason_deactivated = '$reason_deactivated',user_deactivated_by = '$phone_no',user_deactivated = 'Y' WHERE user_login_id = :field_val AND country = :field_val1";
											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
											// execute the query
											$stmt->execute();
											$total_update =  $stmt->rowCount();
											if($total_update > 0 )//update 1 after
											{//4b

												$stmt = $db->prepare("INSERT INTO tbl_reset_passwords_requests(phone_number, request_date, request_processed, request_completed, request_processed_by,country) VALUES (:phone_number,:request_date,:request_processed,:request_completed,:request_processed_by,:country)");

												$stmt->bindParam(':phone_number', $phone_no);
												$stmt->bindParam(':request_date', $todayDate);
												$stmt->bindParam(':request_processed', $request_processed);
												$stmt->bindParam(':request_completed', $request_completed);
												$stmt->bindParam(':request_processed_by', $phone_no);
												$stmt->bindParam(':country', $login_country);
										
												$stmt->execute();
												$total =  $stmt->rowCount();
	
												if($total > 0)
												{//4c
													$success = "true";
													$message = "You Have Successfully Submitted Your Phone Number For Password Reset. Please Wait For Instructions To Reset Your Password: ";
													$_SESSION["message_create"]=$message;
													$db->commit();
													return true;
												}//4c
											}//4b
										}else{
											$success = "false";
											$message = "Phone Number Does Not Exist In The System. Please Check And Try Again: ";
											$_SESSION["message_create"]=$message;
											return false;

										}
									}//3a
								}//2a
							}//1a					
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
	$message = "Your Phone Number Has Already Been Submitted For Password Reset. Please Wait For Instructions To Reset.";
	$_SESSION["message_create"]=$message;
	return false;
}


?>
