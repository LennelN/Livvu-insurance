<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='reset_password';
//error_log(strip_tags("comingin"), 0);
// check for required fields
if (isset($_POST['user_login_id']) && isset($_POST['password']) && isset($_POST['login_country'])){

	$user_login_id = $_POST["user_login_id"];
 
	$password = $_POST["password"];
	$login_country = $_POST["login_country"];




	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($user_login_id);

				
					$sql= "SELECT user_login_id,user_active FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR);
					$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
					$stmt->execute();
					$total = $stmt->rowCount();

	 				// If the SQL query is succesfully performed ($result not false)
  		
					if($total > 0) 
					{

						try
						{
								$hash = sodium_crypto_pwhash_str(
    									$password,
    									SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    									SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
								);
					
							$sql = "UPDATE tbl_users SET user_active = 'Y', user_deactivated = 'N', password = '$hash',date_deactivated=NULL,reason_deactivated = NULL,user_deactivated_by=NULL WHERE user_login_id = :field_val AND country = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{


								$qry = "SELECT phone_number FROM tbl_reset_passwords_requests WHERE request_completed = 'N' AND request_processed = 'Y' AND phone_number = :field_val AND country = :field_val1";

								$stmt = $db->prepare($qry);
								$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
        							$stmt->execute();
								$total = $stmt->rowCount();
								if($total > 0)
								{	
		
									$sql = "UPDATE tbl_reset_passwords_requests SET request_completed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND request_processed = 'Y' AND country = :field_val1";
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
									// execute the query
									$stmt->execute();
									$total_update_tbl_reset =  $stmt->rowCount();
									if($total_update_tbl_reset > 0 )
									{
										$success = "true";
										$message = "You Have Successfully Renewed Your Password. Go Ahead And Log Into The System.";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									}
								}else{

									$success = "true";
									$message = "You Have Successfully Renewed Your Password. Go Ahead And Log Into The System.";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
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
							
						$sql= "SELECT user_login_id,user_active FROM tbl_merchant_users WHERE user_login_id = :field_val AND country = :field_val1"; 
						$stmt = $db->prepare($sql);
						$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR);
						$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
						
						$stmt->execute();
						$total = $stmt->rowCount();

	 					// If the SQL query is succesfully performed ($result not false)
  		
						if($total > 0) 
						{

							try
							{
								$hash = sodium_crypto_pwhash_str(
    									$password,
    									SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    									SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
								);
					
								$sql = "UPDATE tbl_merchant_users SET user_active = 'Y', user_deactivated = 'N', password = '$hash',date_deactivated=NULL,reason_deactivated = NULL,user_deactivated_by=NULL WHERE user_login_id = :field_val AND country = :field_val1";
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
								
								// execute the query
								$stmt->execute();
								$total_update =  $stmt->rowCount();
								if($total_update > 0 )//update 1 after
								{

									
									$qry = "SELECT phone_number FROM tbl_reset_passwords_requests WHERE request_completed = 'N' AND request_processed = 'Y' AND phone_number = :field_val AND country = :field_val1";

									$stmt = $db->prepare($qry);
									$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
        									$stmt->execute();
									$total = $stmt->rowCount();
									if($total > 0)
									{	
		
										$sql = "UPDATE tbl_reset_passwords_requests SET request_completed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND request_processed = 'Y' AND country = :field_val1";
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
										// execute the query
										$stmt->execute();
										$total_update_tbl_reset =  $stmt->rowCount();
										if($total_update_tbl_reset > 0 )
										{
											$success = "true";
											$message = "You Have Successfully Renewed Your Password. Go Ahead And Log Into The System.";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
										}
									}else{
											$success = "true";
											$message = "You Have Successfully Renewed Your Password. Go Ahead And Log Into The System.";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
					
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

							$sql= "SELECT phone_number,member_active FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR);
							$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
							
							$stmt->execute();
							$total = $stmt->rowCount();

	 						// If the SQL query is succesfully performed ($result not false)
  		
							if($total > 0) 
							{

								try
								{
									$hash = sodium_crypto_pwhash_str(
    										$password,
    										SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    										SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
									);
					
									$sql = "UPDATE tbl_clients SET member_active = 'Y', password = '$hash' WHERE phone_number = :field_val AND country_name = :field_val1";
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
									
									// execute the query
									$stmt->execute();
									$total_update =  $stmt->rowCount();
									if($total_update > 0 )//update 1 after
									{

										$qry = "SELECT phone_number FROM tbl_reset_passwords_requests WHERE request_completed = 'N' AND request_processed = 'Y' AND phone_number = :field_val AND country = :field_val1";

										$stmt = $db->prepare($qry);
										$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
        									$stmt->execute();
										$total = $stmt->rowCount();
										if($total > 0)
										{	
		
											$sql = "UPDATE tbl_reset_passwords_requests SET request_completed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND request_processed = 'Y' AND country = :field_val1";
											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
											// execute the query
											$stmt->execute();
											$total_update_tbl_reset =  $stmt->rowCount();
											if($total_update_tbl_reset > 0 )
											{
												$success = "true";
												$message = "You Have Successfully Renewed Your Password. Go Ahead And Log Into The System.";
												array_push($response,array("success"=>$success,"message"=>$message));
												echo json_encode($response);
											}
										}else{
											$success = "true";
											$message = "You Have Successfully Renewed Your Password. Go Ahead And Log Into The System.";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
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

								$sql= "SELECT merchant_phone_number,status_flag FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR);
								$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
							
								$stmt->execute();
								$total = $stmt->rowCount();

	 							// If the SQL query is succesfully performed ($result not false)
  		
								if($total > 0) 
								{

									try
									{
										$hash = sodium_crypto_pwhash_str(
    											$password,
    											SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    											SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
										);
					
										$sql = "UPDATE tbl_merchant_details SET status_flag = 'Y', password = '$hash' WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
										// execute the query
										$stmt->execute();
										$total_update =  $stmt->rowCount();
										if($total_update > 0 )//update 1 after
										{
						
											$qry = "SELECT phone_number FROM tbl_reset_passwords_requests WHERE request_completed = 'N' AND request_processed = 'Y' AND phone_number = :field_val AND country = :field_val1";

											$stmt = $db->prepare($qry);
											$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);
        										$stmt->execute();
											$total = $stmt->rowCount();
											if($total > 0)
											{	
		
												$sql = "UPDATE tbl_reset_passwords_requests SET request_completed = 'Y' WHERE phone_number = :field_val AND request_completed = 'N' AND request_processed = 'Y' AND country = :field_val1";
												$stmt = $db->prepare($sql);
												$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
												$stmt->bindParam(':field_val1', $login_country, PDO::PARAM_STR);

												// execute the query
												$stmt->execute();
												$total_update_tbl_reset =  $stmt->rowCount();
												if($total_update_tbl_reset > 0 )
												{
													$success = "true";
													$message = "You Have Successfully Renewed Your Password. Go Ahead And Log Into The System.";
													array_push($response,array("success"=>$success,"message"=>$message));
													echo json_encode($response);
												}
											}else{

													$success = "true";
													$message = "You Have Successfully Renewed Your Password. Go Ahead And Log Into The System.";
													array_push($response,array("success"=>$success,"message"=>$message));
													echo json_encode($response);
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


								}

							}
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
