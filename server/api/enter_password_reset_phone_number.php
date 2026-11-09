<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='enter_password_reset_phone_number';
//error_log(strip_tags("comingin"), 0);login_country
// check for required fields
if (isset($_POST['phone_no']) && isset($_POST['login_country'])){
	
	$phone_no = $_POST["phone_no"];
 	$login_country = $_POST['login_country'];
	date_default_timezone_set('Africa/Harare');
	$time_activated = date('H:i:s');
$time_logged_in= date('H:i:s');
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($phone_no);
	$request_completed='N';	
	$request_processed='N';		
	$todayDate = date('Y-m-d');
$date_logged_in= date('Y-m-d');
//error_log(strip_tags("comingin 1 "), 0);
$reason_deactivated = "Forgot Password Submit Activated. Reset Of Password Required";
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
							{

								//deactivate client
								$sql = "UPDATE tbl_clients SET member_active = 'N' WHERE phone_number = :field_val AND country_name = :field_val1";
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
								// execute the query
								$stmt->execute();
								$total_update =  $stmt->rowCount();
								if($total_update > 0 )//update 1 after
								{


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
									{

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
										{
											$success = "true";
											$message = "You Have Successfully Submitted Your Phone Number For Password Reset. Please Wait For Instructions To Reset Your Password: ";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
										}
								
									}
								}


							}else{

								$sql= "SELECT status_flag FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
								$stmt->execute();
								$total = $stmt->rowCount();			
					
								if($total > 0)
								{
					
									$sql = "UPDATE tbl_merchant_details SET status_flag = 'N' WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR);
									$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
									// execute the query
									$stmt->execute();
									$total_update =  $stmt->rowCount();
									if($total_update > 0 )//update 1 after
									{


										$qry = "Select merchant_name, merchant_code from tbl_merchant_details where merchant_phone_number = '$phone_no' and merchant_country='$login_country'";
	 					
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
										{

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
											{
												$success = "true";
												$message = "You Have Successfully Submitted Your Phone Number For Password Reset. Please Wait For Instructions To Reset Your Password: ";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
											}

										}

									}
								}else{

									$sql= "SELECT user_login_id FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
									$stmt->execute();
									$total = $stmt->rowCount();

									if($total > 0)
									{

										$sql = "UPDATE tbl_users SET user_active = 'N',date_deactivated = '$date_logged_in', reason_deactivated = '$reason_deactivated',user_deactivated_by = '$phone_no',user_deactivated = 'Y' WHERE user_login_id = :field_val AND country = :field_val1";
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
										// execute the query
										$stmt->execute();
										$total_update =  $stmt->rowCount();
										if($total_update > 0 )//update 1 after
										{

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
											{
												$success = "true";
												$message = "You Have Successfully Submitted Your Phone Number For Password Reset. Please Wait For Instructions To Reset Your Password: ";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
											}

										}

									}else{

										$sql= "SELECT user_active FROM tbl_merchant_users WHERE user_login_id = :field_val AND country = :field_val1"; 
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
										$stmt->execute();
										$total = $stmt->rowCount();

										if($total > 0)
										{
 
    									error_log(strip_tags("comingin17"), 0);
											$sql = "UPDATE tbl_merchant_users SET user_active = 'N',date_deactivated = '$date_logged_in', reason_deactivated = '$reason_deactivated',user_deactivated_by = '$phone_no',user_deactivated = 'Y' WHERE user_login_id = :field_val AND country = :field_val1";
											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR); 
											// execute the query
											$stmt->execute();
											$total_update =  $stmt->rowCount();
											if($total_update > 0 )//update 1 after
											{
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
												{
													$success = "true";
													$message = "You Have Successfully Submitted Your Phone Number For Password Reset. Please Wait For Instructions To Reset Your Password: ";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
												}
											}
										}else{

											$success = "false";
													$message = "Phone Number does not Exist. Please Check And Retry: ";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
}
									}
								}
							}					
$db->commit();
						}catch(PDOException $e){
					
								if ($e->errorInfo[1] == 1062) 
									{

										$success = "false";
		 								$message = "You Have Already Submitted Your Phone Number For Password Reset Today. Please Wait For Instructions To Reset.";
		 								$error_message=$e->getMessage();
		 								$error_type='duplicate';
		 								//handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);	
								
									}else{

										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='exception';
		 								//handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);	
									}
						}//catch
}else{
	$success = "false";
	$message = "Your Phone Number Has Already Been Submitted For Password Reset. Please Wait For Instructions To Reset.";
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
