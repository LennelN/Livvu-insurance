<?php
    
include 'functions.php';
$file_name='login';
   
$response = array();

if (isset($_POST['username']) && isset($_POST['password']) && isset($_POST['loginCountry'])) {


   
    date_default_timezone_set('Africa/Harare');
	$username = $_POST["username"];
    
	$password = $_POST["password"];
	$loginCountry = $_POST["loginCountry"];
   
	$loginmachine= 'IMEINo';
	$date_logged_in = date('Y-m-d');
	$time_logged_in = date('H:i:s');
	$logoutdate = date("Y-m-d");
	$logouttime = date("H:i:s");
	$loginstatus = 'Yes';
	$logoutstatus = "No";
	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';

	//$db = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$char = '1JRy6pCl77pWYfq';
	$key = mt_rand(0, 999);
	$rand_key = $char . $key;
        $retvar = md5($rand_key);
	$retvar = 'usr_'.date('Ymd').mt_rand(0, 9999).substr($retvar,0,5);
	$mediaused = 'Mobile';
	$no_of_attempts = 1;
	$max_no_of_login_attempts = 3;
	$request_completed='N';	
	$request_processed='N';	

	$qry_status = "SELECT user_login_id,sessionid,mediaused FROM tbl_user_login_history WHERE user_login_id = :field_val AND loginstatus = 'Yes' AND country = :field_val1"; 

	$stmt = $db->prepare($qry_status);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0){
		
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		
		foreach($result as $row){		
			$user_login_id = $row['user_login_id'];

			$sessionID = $row['sessionid'];

			$mediaLoggedIn = $row['mediaused'];

		
		}
	
	
		$loginstatus = "Yes";
		$logoutstatus = "No";
		$sql = "Update tbl_user_login_history Set logoutdate ='$logoutdate', logouttime='$logouttime', loginstatus='$logoutstatus' Where  user_login_id ='$user_login_id' And sessionid = '$sessionID' AND country='$loginCountry'";
    		$stmt = $db->prepare($sql);
		$stmt->execute();
    		$cnt = $stmt->rowCount();
	
		if ($cnt <= 0 ) {
			$success = "false";
			$message = "An Error Occurred Processing Transaction Please Try Again Later";
	    		array_push($response,array("success"=>$success,"message"=>$message));
			echo json_encode($response);
        	}else{
			$success = "false";
			$message = "You Are Logged In On Machine ".$mediaLoggedIn." Please Logout And Use One Session";
			array_push($response,array("success"=>$success,"message"=>$message));
			echo json_encode($response);
		}

	}else{

		$sql= "SELECT password,member_active FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
		$stmt = $db->prepare($sql);
		$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
		$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR);  
		$stmt->execute();
		$total = $stmt->rowCount();

 		if($total <= 0)
		{
				
				$sql= "SELECT password,status_flag FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
				$stmt = $db->prepare($sql);
				$stmt->bindParam(':field_val', $username, PDO::PARAM_STR);
				$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR);  
				$stmt->execute();
				$total = $stmt->rowCount();			
				
				if($total <= 0)
					{

						//Check in tbl_users for livvu company employees

						$sql= "SELECT password,user_active FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
						$stmt = $db->prepare($sql);
						$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
						$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR);  
						$stmt->execute();
						$total = $stmt->rowCount();

						if($total <=0)
							{

								$sql= "SELECT password,user_active FROM tbl_merchant_users WHERE user_login_id = :field_val AND country = :field_val1"; 
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR);  
								$stmt->execute();
								$total = $stmt->rowCount();

								if($total <=0){
 
    									$success = "false";
									$message = "Phone Number ".$username." Does Not Exist..Please Click Register!!";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}else{
									$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
									foreach($result as $row){
											$password_returned = $row['password'];
											$user_active = $row['user_active'];
										}

									
									if(sodium_crypto_pwhash_str_verify($password_returned,$password))
									{
							
										if($user_active == 'R')
										{
											$success = "true";
											$message = "Reset Password"; 
											$type="4";
            										array_push($response,array("success"=>$success,"message"=>$message,"phone_number"=>$username,"type"=>$type,"session_id"=>$retvar));
											echo json_encode($response);

										}else{

										
											$qry = "Select name, phone_number,user_role,country,user_active,user_deactivated,merchant_phone_number,merchant_code from tbl_merchant_users where user_login_id = '$username' and password='$password_returned' and country = '$loginCountry'";
	 					
											$stmt = $db->prepare($qry);
        										$stmt->execute();
											$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
											$total =  $stmt->rowCount();
											if($total<=0){
												$success = "false";
												$message = "Account Is No Longer Active.Please Contact Livvu Pvt Ltd For More Details";
												array_push($response,array("success"=>$success,"message"=>$message));
												echo json_encode($response);
							
											}else{
								
    												foreach($result as $row){
													$name = $row['name'];
   
													$phone_number = $row['phone_number'];
  			      
													$user_role=$row['user_role'];
  			      
													$country=$row['country'];
  			      
													$user_active = $row['user_active'];
													$user_deactivated = $row['user_deactivated'];
													$merchant_phone_number = $row['merchant_phone_number'];
													$merchant_code = $row['merchant_code'];
												}


												$sql_name = "SELECT merchant_name,merchant_country FROM tbl_merchant_details WHERE merchant_code = :field_val "; 
												$stmt_name = $db->prepare($sql_name);
												$stmt_name->bindParam(':field_val', $merchant_code, PDO::PARAM_STR); 
												$stmt_name->execute();
												$result_name = $stmt_name->fetchAll(PDO::FETCH_ASSOC); 
												$total_name = $stmt_name->rowCount();
												foreach($result_name as $row){
													$merchant_name = $row['merchant_name'];

													$user_country = $row['merchant_country'];

												}

												if($user_active=='N' And $user_deactivated == 'N')
												{
													$success = "true";
													$message = "Complete Registration"; 
													$type="4";
            												array_push($response,array("success"=>$success,"message"=>$message,"name"=>$name,"phone_number"=>$phone_number,"type"=>$type,"user_role"=>$user_role,"country"=>$country,"user_login_id"=>$username,"merchant_phone_number"=>$merchant_phone_number,"merchant_code"=>$merchant_code,"merchant_name"=>$merchant_name,"session_id"=>$retvar));
													echo json_encode($response);	
												}else{		
													if($user_active=='Y' And $user_deactivated == 'N')
													{
														try
														{
											
															$db->beginTransaction();

															$qry_status = "SELECT phone_number FROM tbl_login_fail_attempts WHERE phone_number = :field_val AND country = :field_val1"; 

															$stmt = $db->prepare($qry_status);
															$stmt->bindParam(':field_val', $username, PDO::PARAM_STR);
															$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR);  
															$stmt->execute();
															$total = $stmt->rowCount();

															if($total > 0)
															{

																//delete from tbl_login_fail_attempts
																$sql = "DELETE from tbl_login_fail_attempts Where phone_number='$username' AND country='$loginCountry'";
    	                                											$stmt_delete = $db->prepare($sql);
	                                    											$stmt_delete->execute();    	                                
                                      												$total_delete =  $stmt_delete->rowCount();
															}
											
															$stmt = $db->prepare("INSERT INTO tbl_user_login_history(user_login_id, username, logindate, logintime, loginstatus, loginmachine, country,sessionid,mediaused) VALUES (:user_login_id, :username, :logindate, :logintime, :loginstatus, :loginmachine, :country, :sessionid, :mediaused)");

															$stmt->bindParam(':user_login_id', $username);
															$stmt->bindParam(':username', $name);
															$stmt->bindParam(':logindate', $date_logged_in);
															$stmt->bindParam(':logintime', $time_logged_in);
															$stmt->bindParam(':loginstatus', $loginstatus);
															$stmt->bindParam(':loginmachine', $loginmachine);
															$stmt->bindParam(':country', $country);
															$stmt->bindParam(':sessionid', $retvar);
    															$stmt->bindParam(':mediaused', $mediaused);

															$stmt->execute();
															$total =  $stmt->rowCount();
	
															if($total > 0)
															{
	
																$success = "true";
																$message = "Successful Login"; 
																$type="4";
            															array_push($response,array("success"=>$success,"message"=>$message,"name"=>$name,"phone_number"=>$phone_number,"type"=>$type,"user_role"=>$user_role,"country"=>$country,"user_login_id"=>$username,"merchant_phone_number"=>$merchant_phone_number,"merchant_code"=>$merchant_code,"merchant_name"=>$merchant_name,"session_id"=>$retvar));
																echo json_encode($response);
															}
															$db->commit();
														}catch(PDOException $e){
					
															if ($e->errorInfo[1] == 1062) 
															{
																$success = "false";
		 														$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 														$error_message=$e->getMessage();
		 														$error_type='duplicate';
		 														handler($error_message,$file_name,$success,$error_type);
		 														$db->rollBack();
																array_push($response_back,array("success"=>$success,"message"=>$message));
																echo json_encode($response_back);
								
															}else{
																$success = "false";
		 														$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 														$error_message=$e->getMessage();
		 														$error_type='exception';
		 														handler($error_message,$file_name,$success,$error_type);
		 														$db->rollBack();
																array_push($response_back,array("success"=>$success,"message"=>$message));
																echo json_encode($response_back);
										
															}
														}
							
									   				}else{

														$success = "false";
														$message = "Account Is Deactivated. Please Contact Livvu Pvt Ltd";
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);


													}//y
												}//else
							  			}
									}
							}else{
    	
									if($user_active=='Y')
									{
								
											$qry_status = "SELECT phone_number,login_date,no_of_attempts FROM tbl_login_fail_attempts WHERE phone_number = :field_val AND login_date = :field_val1 AND country = :field_val2"; 

											$stmt = $db->prepare($qry_status);
											$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $date_logged_in, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val2', $loginCountry, PDO::PARAM_STR); 
											$stmt->execute();
											$total = $stmt->rowCount();
											if($total > 0)
											{

												$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		
												foreach($result as $row){		
													$phone_number_logging_in = $row['phone_number'];

													$date_logging_in = $row['login_date'];

													$no_of_attempts_tried = $row['no_of_attempts'];

							
												}

												$no_of_attempts = $no_of_attempts_tried + 1;

												//update
												if($no_of_attempts < $max_no_of_login_attempts)
												{
													$sql = "UPDATE tbl_login_fail_attempts SET no_of_attempts = $no_of_attempts WHERE phone_number = :field_val AND country = :field_val1";
																// Prepare statement
    													$stmt = $db->prepare($sql);
													$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
													$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
    													$stmt->execute();
    													$total_update =  $stmt->rowCount();
													$success = "false";
														$no_of_attempts = $max_no_of_login_attempts - $no_of_attempts;
														$message = "Phone Number And Password Credentials Are Not Correct! Try Again."." You Have ".$no_of_attempts." Login Attempts Remaining. ";
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);
												}else{

													if($no_of_attempts == $max_no_of_login_attempts)
													{
														//deactivate account and send a message that account is deactivated
														$sql = "UPDATE tbl_login_fail_attempts SET no_of_attempts = $no_of_attempts WHERE phone_number = :field_val AND country = :field_val1";
																// Prepare statement
    														$stmt = $db->prepare($sql);
														$stmt->bindParam(':field_val', $username, PDO::PARAM_STR);
														$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR);  
    														$stmt->execute();

														$reason_deactivated = "Failed To Login 3 Times. Wrong Password Entered";
														$sql = "UPDATE tbl_merchant_users SET user_active = 'N',date_deactivated = '$date_logged_in', reason_deactivated = '$reason_deactivated',user_deactivated_by = '$username',user_deactivated = 'Y' WHERE user_login_id = :field_val AND country = :field_val1";
														$stmt = $db->prepare($sql);
														$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
														$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR);  
														// execute the query
														$stmt->execute();
														$total_update =  $stmt->rowCount();
														if($total_update > 0 )//update 1 after
														{

															$todayDate = date('Y-m-d');
								
															$stmt = $db->prepare("INSERT INTO tbl_reset_passwords_requests(phone_number, request_date, request_processed, request_completed, request_processed_by,country) VALUES (:phone_number,:request_date,:request_processed,:request_completed,:request_processed_by,:country)");

															$stmt->bindParam(':phone_number', $username);
															$stmt->bindParam(':request_date', $todayDate);
															$stmt->bindParam(':request_processed', $request_processed);
															$stmt->bindParam(':request_completed', $request_completed);
															$stmt->bindParam(':request_processed_by', $username);
															$stmt->bindParam(':country', $loginCountry);
										
															$stmt->execute();
															$total =  $stmt->rowCount();
	
															if($total > 0)
															{
																$success = "false";
																$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
																array_push($response,array("success"=>$success,"message"=>$message));
																echo json_encode($response);
															}
														}

													}else{

														$success = "false";
														$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);

													}

												}
											}else{

													$stmt = $db->prepare("INSERT INTO tbl_login_fail_attempts(phone_number, login_date, no_of_attempts, country) VALUES (:phone_number, :login_date, :no_of_attempts, :country)");

													$stmt->bindParam(':phone_number', $username);
													$stmt->bindParam(':login_date', $date_logged_in);
													$stmt->bindParam(':no_of_attempts', $no_of_attempts);
													$stmt->bindParam(':country', $loginCountry);
											

													$stmt->execute();
													$total =  $stmt->rowCount();
	
													if($total > 0){
							
														$success = "false";
														$no_of_attempts = $max_no_of_login_attempts - $no_of_attempts;
														$message = "Phone Number And Password Credentials Are Not Correct! Try Again."." You Have ".$no_of_attempts." Login Attempts Remaining. ";
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);
													}

											}
										}else{
											if($user_active=='N')
											{
												$success = "false";
												$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
												array_push($response,array("success"=>$success,"message"=>$message));
												echo json_encode($response);
											}else{
												$success = "false";
												$message = "Phone Number And Password Credentials Are Not Correct! Try Again";
												array_push($response,array("success"=>$success,"message"=>$message));
												echo json_encode($response);
											}

										}

									}
									
								}		

							}else{
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
								foreach($result as $row){
										$password_returned = $row['password'];
										$user_active = $row['user_active'];
									}

							
									//$password_returned = $stmt->fetchColumn();
								if(sodium_crypto_pwhash_str_verify($password_returned,$password))
								{

									if($user_active == 'R')
									{
										$success = "true";
										$message = "Reset Password"; 
										$type="3";
            									array_push($response,array("success"=>$success,"message"=>$message,"phone_number"=>$username,"type"=>$type,"session_id"=>$retvar));
										echo json_encode($response);

									}else{
										$qry = "Select name, phone_number,user_role,country,user_active,user_deactivated from tbl_users where user_login_id = '$username' and password='$password_returned' and country='$loginCountry'";
	 					
										$stmt = $db->prepare($qry);
        									$stmt->execute();
										$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
										$total =  $stmt->rowCount();
										if($total<=0){
											$success = "false";
											$message = "Account Is No Longer Active.Please Contact Livvu Pvt Ltd For More Details";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
							
										}else{
								
    											foreach($result as $row){
												$name = $row['name'];
   
												$phone_number = $row['phone_number'];
  			      
												$user_role=$row['user_role'];
  			      
												$country=$row['country'];
  			      
												$user_active = $row['user_active'];
												$user_deactivated = $row['user_deactivated'];
												}
											if($user_active=='N' And $user_deactivated == 'N')
											{
												$success = "true";
												$message = "Complete Registration"; 
												$type="3";
            											array_push($response,array("success"=>$success,"message"=>$message,"name"=>$name,"phone_number"=>$phone_number,"type"=>$type,"user_role"=>$user_role,"country"=>$country,"user_login_id"=>$username,"session_id"=>$retvar));
												echo json_encode($response);	
											}else{		
												if($user_active=='Y' And $user_deactivated == 'N')
												{
													try
													{
											
														$db->beginTransaction();

														$qry_status = "SELECT phone_number FROM tbl_login_fail_attempts WHERE phone_number = :field_val AND country = :field_val1"; 
														$stmt = $db->prepare($qry_status);
														$stmt->bindParam(':field_val', $username, PDO::PARAM_STR);
														$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR);  
														$stmt->execute();
														$total = $stmt->rowCount();

														if($total > 0)
														{

															//delete from tbl_login_fail_attempts
															$sql = "DELETE from tbl_login_fail_attempts Where phone_number='$username' AND country='$loginCountry'";
    	                                										$stmt_delete = $db->prepare($sql);
	                                    										$stmt_delete->execute();    	                                
                                      											$total_delete =  $stmt_delete->rowCount();
														}
														$stmt = $db->prepare("INSERT INTO tbl_user_login_history(user_login_id, username, logindate, logintime, loginstatus, loginmachine, country, sessionid, mediaused) VALUES (:user_login_id, :username, :logindate, :logintime, :loginstatus, :loginmachine, :country, :sessionid, :mediaused)");

														$stmt->bindParam(':user_login_id', $username);
														$stmt->bindParam(':username', $name);
														$stmt->bindParam(':logindate', $date_logged_in);
														$stmt->bindParam(':logintime', $time_logged_in);
														$stmt->bindParam(':loginstatus', $loginstatus);
														$stmt->bindParam(':loginmachine', $loginmachine);
														$stmt->bindParam(':country', $country);
    														$stmt->bindParam(':sessionid', $retvar);
														$stmt->bindParam(':mediaused', $mediaused);
														$stmt->execute();
														$total =  $stmt->rowCount();
	
														if($total > 0)
														{
	
															$success = "true";
															$message = "Successful Login"; 
															$type="3";
            														array_push($response,array("success"=>$success,"message"=>$message,"name"=>$name,"phone_number"=>$phone_number,"type"=>$type,"user_role"=>$user_role,"country"=>$country,"user_login_id"=>$username,"session_id"=>$retvar));
															echo json_encode($response);
														}
														$db->commit();
													}catch(PDOException $e){
					
														if ($e->errorInfo[1] == 1062) 
														{
															$success = "false";
		 													$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 													$error_message=$e->getMessage();
		 													$error_type='duplicate';
		 													handler($error_message,$file_name,$success,$error_type);
		 													$db->rollBack();
															array_push($response_back,array("success"=>$success,"message"=>$message));
															echo json_encode($response_back);
								
														}else{
															$success = "false";
		 													$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 													$error_message=$e->getMessage();
		 													$error_type='exception';
		 													handler($error_message,$file_name,$success,$error_type);
		 													$db->rollBack();
															array_push($response_back,array("success"=>$success,"message"=>$message));
															echo json_encode($response_back);
										
														}
													}
							
									   			}else{

													$success = "false";
													$message = "Account Is Deactivated. Please Contact Livvu Pvt Ltd";
													array_push($response,array("success"=>$success,"message"=>$message));
													echo json_encode($response);


												}
											}
							  			}
								}
							}else{
    

								if($user_active=='Y')
								{
								
									$qry_status = "SELECT phone_number,login_date,no_of_attempts FROM tbl_login_fail_attempts WHERE phone_number = :field_val AND login_date = :field_val1 AND country = :field_val2"; 

									$stmt = $db->prepare($qry_status);
									$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $date_logged_in, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val2', $loginCountry, PDO::PARAM_STR); 
									$stmt->execute();
									$total = $stmt->rowCount();
									if($total > 0)
									{

										$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		
										foreach($result as $row){		
											$phone_number_logging_in = $row['phone_number'];

											$date_logging_in = $row['login_date'];

											$no_of_attempts_tried = $row['no_of_attempts'];

							
											}

										$no_of_attempts = $no_of_attempts_tried + 1;

												//update
										if($no_of_attempts < $max_no_of_login_attempts)
										{
											$sql = "UPDATE tbl_login_fail_attempts SET no_of_attempts = $no_of_attempts WHERE phone_number = :field_val AND country = :field_val1";
											// Prepare statement
    											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
    											$stmt->execute();
    											$total_update =  $stmt->rowCount();
											$success = "false";
											$no_of_attempts = $max_no_of_login_attempts - $no_of_attempts;
											$message = "Phone Number And Password Credentials Are Not Correct! Try Again."." You Have ".$no_of_attempts." Login Attempts Remaining. ";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
										}else{

											if($no_of_attempts == $max_no_of_login_attempts)
											{
												//deactivate account and send a message that account is deactivated
												$sql = "UPDATE tbl_login_fail_attempts SET no_of_attempts = $no_of_attempts WHERE phone_number = :field_val AND country = :field_val1";
												// Prepare statement
    												$stmt = $db->prepare($sql);
												$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
												$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
    												$stmt->execute();

												$reason_deactivated = "Failed To Login 3 Times. Wrong Password Entered";
												$sql = "UPDATE tbl_users SET user_active = 'N',date_deactivated = '$date_logged_in', reason_deactivated = '$reason_deactivated',user_deactivated_by = '$username',user_deactivated = 'Y' WHERE user_login_id = :field_val AND country = :field_val1";
												$stmt = $db->prepare($sql);
												$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
												$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
														// execute the query
												$stmt->execute();
												$total_update =  $stmt->rowCount();
												if($total_update > 0 )//update 1 after
												{

					
													$todayDate = date('Y-m-d');
								
													$stmt = $db->prepare("INSERT INTO tbl_reset_passwords_requests(phone_number, request_date, request_processed, request_completed, request_processed_by,country) VALUES (:phone_number,:request_date,:request_processed,:request_completed,:request_processed_by,:country)");

													$stmt->bindParam(':phone_number', $username);
													$stmt->bindParam(':request_date', $todayDate);
													$stmt->bindParam(':request_processed', $request_processed);
													$stmt->bindParam(':request_completed', $request_completed);
													$stmt->bindParam(':request_processed_by', $username);
													$stmt->bindParam(':country', $loginCountry);
										
													$stmt->execute();
													$total =  $stmt->rowCount();
	
													if($total > 0)
													{
														$success = "false";
														$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);
													}
												}

											}else{

												$success = "false";
												$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
												array_push($response,array("success"=>$success,"message"=>$message));
												echo json_encode($response);

											}

										}
									}else{

										$stmt = $db->prepare("INSERT INTO tbl_login_fail_attempts(phone_number, login_date, no_of_attempts,country) VALUES (:phone_number, :login_date, :no_of_attempts,:country)");

										$stmt->bindParam(':phone_number', $username);
										$stmt->bindParam(':login_date', $date_logged_in);
										$stmt->bindParam(':no_of_attempts', $no_of_attempts);
										$stmt->bindParam(':country', $loginCountry);
											

										$stmt->execute();
										$total =  $stmt->rowCount();
	
										if($total > 0)
										{
							
											$success = "false";
											$no_of_attempts = $max_no_of_login_attempts - $no_of_attempts;
											$message = "Phone Number And Password Credentials Are Not Correct! Try Again."." You Have ".$no_of_attempts." Login Attempts Remaining. ";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
										}

									}
							}else{

								if( $user_active=='N')
											{
												$success = "false";
												$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
												array_push($response,array("success"=>$success,"message"=>$message));
												echo json_encode($response);
											}else{
												$success = "false";
												$message = "Phone Number And Password Credentials Are Not Correct! Try Again";
												array_push($response,array("success"=>$success,"message"=>$message));
												echo json_encode($response);
											}
							}
							
						}
					}	
				}else{
				
						//$password_returned = $stmt->fetchColumn();
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
						foreach($result as $row){
							$password_returned = $row['password'];
							$status_flag = $row['status_flag'];
						}
						if(sodium_crypto_pwhash_str_verify($password_returned,$password))
						{
							$qry = "Select merchant_name, merchant_code,type_of_registration,merchant_country,status_flag from tbl_merchant_details where merchant_phone_number = '$username' and password='$password_returned' and merchant_country='$loginCountry'";
	 					
							$stmt = $db->prepare($qry);
        						$stmt->execute();
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
							$total =  $stmt->rowCount();
							if($total<=0)
							{
								$success = "false";
								$message = "Merchant Account Is No Longer Active.Please Contact Livvu Pvt Ltd For More details";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);

							}else{
								
    								foreach($result as $row){
									$merchant_name = $row['merchant_name'];
   
									$merchant_code = $row['merchant_code'];
  			      
									$type_of_registration = $row['type_of_registration'];
  			
									$merchant_country = $row['merchant_country'];
									$status_flag = $row['status_flag'];
   
      
									}	
								if($status_flag=='N')
								{
									$success = "false";
									$message = "Merchant Account Is No Longer Active.Please Contact Livvu Pvt Ltd For More details";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}else{


									if($status_flag=='R')
									{
										$success = "true";
										$message = "Reset Password"; 
										$type="2";
        									array_push($response,array("success"=>$success,"message"=>$message,"type"=>$type,"session_id"=>$retvar,"phone_number"=>$username));
										echo json_encode($response);

									}else{
										try
										{
											
											$db->beginTransaction();
											$qry_status = "SELECT phone_number FROM tbl_login_fail_attempts WHERE phone_number = :field_val AND country= :field_val1"; 
											$stmt = $db->prepare($qry_status);
											$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
											$stmt->execute();
											$total = $stmt->rowCount();

											if($total > 0)
											{

												//delete from tbl_login_fail_attempts
												$sql = "DELETE from tbl_login_fail_attempts Where phone_number='$username' and country='$loginCountry'";
    	                                							$stmt_delete = $db->prepare($sql);
	                                    							$stmt_delete->execute();    	                                
                                      								$total_delete =  $stmt_delete->rowCount();
											}
											$stmt = $db->prepare("INSERT INTO tbl_user_login_history(user_login_id, username, logindate, logintime, loginstatus, loginmachine, country, sessionid, mediaused) VALUES (:user_login_id, :username, :logindate, :logintime, :loginstatus, :loginmachine, :country, :sessionid, :mediaused)");

											$stmt->bindParam(':user_login_id', $username);
											$stmt->bindParam(':username', $merchant_name);
											$stmt->bindParam(':logindate', $date_logged_in);
											$stmt->bindParam(':logintime', $time_logged_in);
											$stmt->bindParam(':loginstatus', $loginstatus);
											$stmt->bindParam(':loginmachine', $loginmachine);
											$stmt->bindParam(':country', $merchant_country);
    											$stmt->bindParam(':sessionid', $retvar);
											$stmt->bindParam(':mediaused', $mediaused);
											$stmt->execute();
											$total =  $stmt->rowCount();
	
											if($total > 0)
											{
												$success = "true";
												$message = "Successful Login"; 
												$type="2";

            											array_push($response,array("success"=>$success,"message"=>$message,"merchant_name"=>$merchant_name,"merchant_code"=>$merchant_code,"type"=>$type,"type_of_registration"=>$type_of_registration,"merchant_country"=>$merchant_country,"session_id"=>$retvar,"phone_number"=>$username));
												echo json_encode($response);
											}
											$db->commit();
										}catch(PDOException $e){
					
											if ($e->errorInfo[1] == 1062) 
											{
												$success = "false";
		 										$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 										$error_message=$e->getMessage();
		 										$error_type='duplicate';
		 										handler($error_message,$file_name,$success,$error_type);
		 										$db->rollBack();
												array_push($response_back,array("success"=>$success,"message"=>$message));
												echo json_encode($response_back);
								
											}else{
												$success = "false";
		 										$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 										$error_message=$e->getMessage();
		 										$error_type='exception';
		 										handler($error_message,$file_name,$success,$error_type);
		 										$db->rollBack();
												array_push($response_back,array("success"=>$success,"message"=>$message));
												echo json_encode($response_back);
												//$db->rollBack();
											}
										}
									}
								}
							}
							
						}else{
    


							if($status_flag=='Y')
							{
								
								$qry_status = "SELECT phone_number,login_date,no_of_attempts FROM tbl_login_fail_attempts WHERE phone_number = :field_val AND login_date = :field_val1 AND country = :field_val2"; 

								$stmt = $db->prepare($qry_status);
								$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $date_logged_in, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val2', $loginCountry, PDO::PARAM_STR); 
								$stmt->execute();
								$total = $stmt->rowCount();
								if($total > 0)
								{

									$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		
									foreach($result as $row){		
										$phone_number_logging_in = $row['phone_number'];

										$date_logging_in = $row['login_date'];

										$no_of_attempts_tried = $row['no_of_attempts'];

							
										}

									$no_of_attempts = $no_of_attempts_tried + 1;

												//update
									if($no_of_attempts < $max_no_of_login_attempts)
									{
										$sql = "UPDATE tbl_login_fail_attempts SET no_of_attempts = $no_of_attempts WHERE phone_number = :field_val AND country = :field_val1";
										// Prepare statement
    										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
    										$stmt->execute();
    										$total_update =  $stmt->rowCount();
										$success = "false";
										$no_of_attempts = $max_no_of_login_attempts - $no_of_attempts;
										$message = "Phone Number And Password Credentials Are Not Correct! Try Again."." You Have ".$no_of_attempts." Login Attempts Remaining. ";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									}else{

										if($no_of_attempts == $max_no_of_login_attempts)
										{
											//deactivate account and send a message that account is deactivated
											$sql = "UPDATE tbl_login_fail_attempts SET no_of_attempts = $no_of_attempts WHERE phone_number = :field_val AND country = :field_val1";
											// Prepare statement
    											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
    											$stmt->execute();

											$reason_deactivated = "Failed To Login 3 Times. Wrong Password Entered";
											$dt   = new DateTime();
											$todayDate = date('Y-m-d');
											$time_deactivated = date('H:i:s');
					
											$sql = "UPDATE tbl_merchant_details SET status_flag = 'N' WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
											// execute the query
											$stmt->execute();
											$total_update =  $stmt->rowCount();
											if($total_update > 0 )//update 1 after
											{

												$qry = "Select merchant_name, merchant_code from tbl_merchant_details where merchant_phone_number = '$username' and password='$password_returned' and merchant_country='$loginCountry'";
	 					
												$stmt = $db->prepare($qry);
        											$stmt->execute();
												$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
												$total =  $stmt->rowCount();
												foreach($result as $row){
													$merchant_name = $row['merchant_name'];
   
													$merchant_code = $row['merchant_code'];
 
													}	
												
												$stmt = $db->prepare("INSERT INTO tbl_merchant_deactivation_details(merchant_phone_number, merchant_name, merchant_code, date_deactivated,time_deactivated,reason_deactivated,user_deactivated_by,country) VALUES (:merchant_phone_number, :merchant_name, :merchant_code, :date_deactivated, :time_deactivated, :reason_deactivated, :user_deactivated_by,:country)");

												$stmt->bindParam(':merchant_phone_number', $username);
												$stmt->bindParam(':merchant_name', $merchant_name);
												$stmt->bindParam(':merchant_code', $merchant_code);
												$stmt->bindParam(':date_deactivated', $todayDate);
												$stmt->bindParam(':time_deactivated', $time_deactivated);
												$stmt->bindParam(':reason_deactivated', $reason_deactivated);
												$stmt->bindParam(':user_deactivated_by', $username);
												$stmt->bindParam(':country', $loginCountry);
										
												$stmt->execute();
												$total =  $stmt->rowCount();

												if($total > 0 )//update 1 after
												{

													$todayDate = date('Y-m-d');
								
													$stmt = $db->prepare("INSERT INTO tbl_reset_passwords_requests(phone_number, request_date, request_processed, request_completed, request_processed_by,country) VALUES (:phone_number,:request_date,:request_processed,:request_completed,:request_processed_by,:country)");

													$stmt->bindParam(':phone_number', $username);
													$stmt->bindParam(':request_date', $todayDate);
													$stmt->bindParam(':request_processed', $request_processed);
													$stmt->bindParam(':request_completed', $request_completed);
													$stmt->bindParam(':request_processed_by', $username);
													$stmt->bindParam(':country', $loginCountry);
										
													$stmt->execute();
													$total =  $stmt->rowCount();
	
													if($total > 0)
													{
														$success = "false";
														$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);
													}
												}
											}

										}else{

											$success = "false";
											$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);

										}

									}
								}else{

										$stmt = $db->prepare("INSERT INTO tbl_login_fail_attempts(phone_number, login_date, no_of_attempts,country) VALUES (:phone_number, :login_date, :no_of_attempts, :country)");

										$stmt->bindParam(':phone_number', $username);
										$stmt->bindParam(':login_date', $date_logged_in);
										$stmt->bindParam(':no_of_attempts', $no_of_attempts);
										$stmt->bindParam(':country', $loginCountry);
										$stmt->execute();
										$total =  $stmt->rowCount();
	
										if($total > 0)
										{
							
											$success = "false";
											$no_of_attempts = $max_no_of_login_attempts - $no_of_attempts;
											$message = "Phone Number And Password Credentials Are Not Correct! Try Again."." You Have ".$no_of_attempts." Login Attempts Remaining. ";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
										}

								}
							}else{
								if($status_flag=='N')
								{
									$success = "false";
									$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}else{
									$success = "false";
									$message = "Phone Number And Password Credentials Are Not Correct! Try Again";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}

							}

						}
				}

 	}else{
	
		//$password_returned = $stmt->fetchColumn();
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		foreach($result as $row){
			$password_returned = $row['password'];
			$member_active = $row['member_active'];
		}

		if(sodium_crypto_pwhash_str_verify($password_returned,$password))
		{
			
			$qry = "Select name, surname,country_name,member_active from tbl_clients where phone_number = '$username' and password='$password_returned' and country_name='$loginCountry'";
	 
			$stmt = $db->prepare($qry);
        		$stmt->execute();
			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
			$total =  $stmt->rowCount();
			if($total<=0)
			{
					
				$success = "false";
				$message = "Client Account Is No Longer Active.Please Contact Livvu Pvt Ltd For More details";
				array_push($response,array("success"=>$success,"message"=>$message));
				echo json_encode($response);

			}else{

			
    				foreach($result as $row){
					$name = $row['name'];
   
					$surname = $row['surname'];
  			      
					$country_name = $row['country_name'];
 
					$member_active =  $row['member_active'];
 			      
				}
			
				$fullname=$name." ".$surname;

				if($member_active=='N')
				{

					$success = "false";
					$message = "Client Account Is No Longer Active.Please Contact Livvu Pvt Ltd For More details";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);

				}else{

					if($member_active=='R')
					{
						
						$success = "true";
						$message = "Reset Password"; 
						$type="1";
        					array_push($response,array("success"=>$success,"message"=>$message,"type"=>$type,"session_id"=>$retvar,"phone_number"=>$username));
						echo json_encode($response);

					}else{
						try
						{
											
							$db->beginTransaction();

							$qry_status = "SELECT phone_number FROM tbl_login_fail_attempts WHERE phone_number = :field_val AND country= :field_val1"; 
							$stmt = $db->prepare($qry_status);
							$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
							$stmt->execute();
							$total = $stmt->rowCount();

							if($total > 0)
							{

								//delete from tbl_login_fail_attempts
								$sql = "DELETE from tbl_login_fail_attempts Where phone_number='$username' and country='$loginCountry'";
    	                                			$stmt_delete = $db->prepare($sql);
	                                    			$stmt_delete->execute();    	                                
                                      				$total_delete =  $stmt_delete->rowCount();
							}

							$stmt = $db->prepare("INSERT INTO tbl_user_login_history(user_login_id, username, logindate, logintime, loginstatus, loginmachine, country, sessionid, mediaused) VALUES (:user_login_id, :username, :logindate, :logintime, :loginstatus, :loginmachine, :country, :sessionid, :mediaused)");

							$stmt->bindParam(':user_login_id', $username);
							$stmt->bindParam(':username', $fullname);
							$stmt->bindParam(':logindate', $date_logged_in);
							$stmt->bindParam(':logintime', $time_logged_in);
							$stmt->bindParam(':loginstatus', $loginstatus);
							$stmt->bindParam(':loginmachine', $loginmachine);
							$stmt->bindParam(':country', $country_name);
    							$stmt->bindParam(':sessionid', $retvar);
							$stmt->bindParam(':mediaused', $mediaused);

							$stmt->execute();
							$total =  $stmt->rowCount();
	
							if($total > 0)
							{

								$success = "true";
								$message = "Successful Login"; 
								$type="1";
								$type_of_registration="Client";
        							array_push($response,array("success"=>$success,"message"=>$message,"name"=>$name,"surname"=>$surname,"type"=>$type,"type_of_registration"=>$type_of_registration,"country_name"=>$country_name,"session_id"=>$retvar,"phone_number"=>$username));
								echo json_encode($response);
							}
							$db->commit();
						}catch(PDOException $e){
					
									if ($e->errorInfo[1] == 1062) 
									{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='duplicate';
		 								handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										array_push($response_back,array("success"=>$success,"message"=>$message));
										echo json_encode($response_back);
								
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='exception';
		 								handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										array_push($response_back,array("success"=>$success,"message"=>$message));
										echo json_encode($response_back);
										//$db->rollBack();
									}
						}
					}
				}
			}
		}else{


					if($member_active=='Y')
					{
							
							$qry_status = "SELECT phone_number,login_date,no_of_attempts FROM tbl_login_fail_attempts WHERE phone_number = :field_val AND login_date = :field_val1 AND country = :field_val2"; 

							$stmt = $db->prepare($qry_status);
							$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $date_logged_in, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val2', $loginCountry, PDO::PARAM_STR); 
							$stmt->execute();
							$total = $stmt->rowCount();
							if($total > 0)
							{

								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		
								foreach($result as $row){		
									$phone_number_logging_in = $row['phone_number'];

									$date_logging_in = $row['login_date'];

									$no_of_attempts_tried = $row['no_of_attempts'];

							
									}

									$no_of_attempts = $no_of_attempts_tried + 1;

												//update
									if($no_of_attempts < $max_no_of_login_attempts)
									{
										$sql = "UPDATE tbl_login_fail_attempts SET no_of_attempts = $no_of_attempts WHERE phone_number = :field_val AND country = :field_val1";
										// Prepare statement
    										$stmt = $db->prepare($sql);
										$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
										$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
    										$stmt->execute();
    										$total_update =  $stmt->rowCount();
										$success = "false";
										$no_of_attempts = $max_no_of_login_attempts - $no_of_attempts;
										$message = "Phone Number And Password Credentials Are Not Correct! Try Again."." You Have ".$no_of_attempts." Login Attempts Remaining. ";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									}else{

										if($no_of_attempts == $max_no_of_login_attempts)
										{
											//deactivate account and send a message that account is deactivated
											$sql = "UPDATE tbl_login_fail_attempts SET no_of_attempts = $no_of_attempts WHERE phone_number = :field_val AND country = :field_val1";
											// Prepare statement
    											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
    											$stmt->execute();

											$reason_deactivated = "Failed To Login 3 Times. Wrong Password Entered";
											$sql = "UPDATE tbl_clients SET member_active = 'N' WHERE phone_number = :field_val AND country_name = :field_val1";
											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $loginCountry, PDO::PARAM_STR); 
											// execute the query
											$stmt->execute();
											$total_update =  $stmt->rowCount();
											if($total_update > 0 )//update 1 after
											{

												$stmt = $db->prepare("INSERT INTO tbl_client_deactivation_details(phone_number, date_deactivated,time_deactivated,reason_deactivated,client_deactivated_by,country) VALUES (:phone_number, :date_deactivated, :time_deactivated, :reason_deactivated, :client_deactivated_by,:country)");

												$stmt->bindParam(':phone_number', $username);
												$stmt->bindParam(':date_deactivated', $date_logged_in);
												$stmt->bindParam(':time_deactivated', $time_logged_in);
												$stmt->bindParam(':reason_deactivated', $reason_deactivated);
												$stmt->bindParam(':client_deactivated_by', $username);
												$stmt->bindParam(':country', $loginCountry);
										
												$stmt->execute();
												$total =  $stmt->rowCount();
												if($total > 0)
												{

													$todayDate = date('Y-m-d');
								
													$stmt = $db->prepare("INSERT INTO tbl_reset_passwords_requests(phone_number, request_date, request_processed, request_completed, request_processed_by,country) VALUES (:phone_number,:request_date,:request_processed,:request_completed,:request_processed_by,:country)");

													$stmt->bindParam(':phone_number', $username);
													$stmt->bindParam(':request_date', $todayDate);
													$stmt->bindParam(':request_processed', $request_processed);
													$stmt->bindParam(':request_completed', $request_completed);
													$stmt->bindParam(':request_processed_by', $username);
													$stmt->bindParam(':country', $loginCountry);
										
													$stmt->execute();
													$total =  $stmt->rowCount();
	
													if($total > 0)
													{
														$success = "false";
														$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);
													}
												}
											}

										}else{

											$success = "false";
											$message = "Your Account Has Been Deactivated. Please Contact Your Company For More Details. Thank You.";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);

										}

									}
								}else{

										$stmt = $db->prepare("INSERT INTO tbl_login_fail_attempts(phone_number, login_date, no_of_attempts,country) VALUES (:phone_number, :login_date, :no_of_attempts,:country)");

										$stmt->bindParam(':phone_number', $username);
										$stmt->bindParam(':login_date', $date_logged_in);
										$stmt->bindParam(':no_of_attempts', $no_of_attempts);
										$stmt->bindParam(':country', $loginCountry);
										$stmt->execute();
										$total =  $stmt->rowCount();
	
										if($total > 0)
										{
							
											$success = "false";
											$no_of_attempts = $max_no_of_login_attempts - $no_of_attempts;
											$message = "Phone Number And Password Credentials Are Not Correct! Try Again."." You Have ".$no_of_attempts." Login Attempts Remaining. ";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);
										}

								}
							}else{
								if($member_active=='N')
								{

									$success = "false";
									$message = "Client Account Is No Longer Active.Please Contact Livvu Pvt Ltd For More details";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}else{
									$success = "false";
									$message = "Phone Number And Password Credentials Are Not Correct! Try Again";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}

							}
		}
	}
}
} else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    echo json_encode($response);
}

	
?>
