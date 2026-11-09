<?php
    

$response = array();
include 'functions.php';
$file_name='transfer_points';


if (isset($_POST['username']) && isset($_POST['phone_no']) && isset($_POST['amount_to_transfer']) && isset($_POST['media']) && isset($_POST['transfer_processed_by']) && isset($_POST['country'])  && isset($_POST['transfer_country'])) {
	$username = $_POST["username"];
	$transfer_phone_no  = $_POST["phone_no"];		
	$amount_to_transfer = $_POST["amount_to_transfer"];
	$amount_to_transfer_int = floatval($amount_to_transfer);
 	$media = $_POST["media"];
	$transfer_processed_by = $_POST["transfer_processed_by"];
	$country = $_POST["country"];
	$transfer_country = $_POST["transfer_country"];
	$transaction_type='Transfer';
	$transaction_id=0;
 
 
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();
	//Check whether the username exists and return message telling the client to register

	$sql= "SELECT name, surname FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $transfer_phone_no, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $transfer_country, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

 	if($total <= 0)
	{
		//check whether its a merchant being transfered to
		//error_log(strip_tags("comingin 2"), 0);
		$sql_merchant = "SELECT merchant_code, merchant_name FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
		$stmt_merchant = $db->sth->prepare($sql_merchant);
		$stmt_merchant->bindParam(':field_val', $transfer_phone_no, PDO::PARAM_STR); 
		$stmt_merchant->bindParam(':field_val1', $transfer_country, PDO::PARAM_STR); 
		$stmt_merchant->execute();
		$total_merchant = $stmt_merchant->rowCount();
		if($total_merchant <= 0)
		{
 			$success = "false";
			$message = "Phone Number Entered To Receive Transfer Does Not Exist..Please Register Before You Can Receive Any Transfers!!";
			array_push($response,array("success"=>$success,"message"=>$message));
			echo json_encode($response);
		}else{
			$result = $stmt_merchant->fetchAll(PDO::FETCH_ASSOC);						
			foreach($result as $row)
			{
				$code = $row['merchant_code'];
				$name = $row['merchant_name'];
			}
			$first_last_name = $name;
			
			$sql_due_date = "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val AND purchase_country = :field_val1";
			$stmt_due_date = $db->sth->prepare($sql_due_date);
			$stmt_due_date->bindParam(':field_val', $username, PDO::PARAM_STR); 
			$stmt_due_date->bindParam(':field_val1', $country, PDO::PARAM_STR); 
			$stmt_due_date->execute();
			$total_due_date = $stmt_due_date->rowCount();
//error_log(strip_tags("comingin 3".$first_last_name), 0);
 			if($total_due_date <= 0)
			{
				$success = "false";
				$message = "Your Account Is Not Up To Date. Please Go Ahead And Pay Your Subscriptions For Your Account To Be Up To Date And Be Able To Make Transfers.";
				array_push($response,array("success"=>$success,"message"=>$message));
				echo json_encode($response);							
			}else{

				$due_date = $stmt_due_date->fetchColumn();
				$todayDate = date('Y-m-d');
				if($todayDate > $due_date){
					
					$success = "false";
					$message = "Your Account Is Not Up To Date. Please Go Ahead And Pay Your Subscriptions For Your Account To Be Up To Date And Be Able To Make Transfers.";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);							
				}else{

					$sql_username_due_date= "SELECT MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val AND country = :field_val1"; 
					$stmt_username_due_date = $db->sth->prepare($sql_username_due_date);
					$stmt_username_due_date->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt_username_due_date->bindParam(':field_val1', $country, PDO::PARAM_STR); 
					$stmt_username_due_date->execute();
					$total_username_due_date = $stmt_username_due_date->rowCount();

					if($total_username_due_date > 0)
					{
						$username_due_date = $stmt_username_due_date->fetchColumn();
					}

					$sql_amount= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
					$stmt_amount = $db->sth->prepare($sql_amount);
					$stmt_amount->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt_amount->bindParam(':field_val1', $country, PDO::PARAM_STR); 

					$stmt_amount->execute();
					$total_amount = $stmt_amount->rowCount();

 					if($total_amount <= 0)
					{	
						//error_log(strip_tags("comingin 8".$first_last_name), 0);			
						$amount = 0;
						$success = "false";
						$message = "Your Account Balance Is Zero. Please Go Ahead And Pay Your Subscriptions For Your Account To Be Up To Date And Be Able To Make Transfers.";
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
 					}else{
						$amount = $stmt_amount->fetchColumn();						
						try
						{
							$db->sth->beginTransaction();
							if($amount >= $amount_to_transfer_int)
							{

								$sql_due_date_client = "SELECT status_flag FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
								$stmt_due_date_client = $db->sth->prepare($sql_due_date_client);
								$stmt_due_date_client->bindParam(':field_val', $transfer_phone_no, PDO::PARAM_STR); 
								$stmt_due_date_client->bindParam(':field_val1', $transfer_country, PDO::PARAM_STR); 
								$stmt_due_date_client->execute();
								$total_due_date_client = $stmt_due_date_client->rowCount();

								if($total_due_date_client <= 0){

									$success = "false";
									$message = "Phone Number ".$transfer_phone_no." Account Does Not Exist And Cannot Receive Any Transfers.";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);							

								}else{

									$status_flag = $stmt_due_date_client->fetchColumn();
									$todayDate = date('Y-m-d');
									if($status_flag == 'N'){
									//error_log(strip_tags("comingin 12".$first_last_name), 0);
										$success = "false";
										$message =  "Phone Number ".$transfer_phone_no." Account Is Deactivated And Cannot Be Able To Receive Transfers.";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									}else{
										//do the transfers....

										//$sql_client_due_date= "SELECT MAX(end_date) FROM tbl_year_contributions WHERE //phone_number = :field_val AND country = :field_val1"; 
										//$stmt_client_due_date = $db->sth->prepare($sql_client_due_date);
										//$stmt_client_due_date->bindParam(':field_val', $transfer_phone_no, PDO::PARAM_STR); 
										//$stmt_client_due_date->bindParam(':field_val1', $transfer_country, PDO::PARAM_STR); 
										//$stmt_client_due_date->execute();
										//$total_client_due_date = $stmt_client_due_date->rowCount();

										if($status_flag == 'Y')
										{
											//$client_due_date = $stmt_client_due_date->fetchColumn();	
										
											$sql_client_amount= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
											$stmt_client_amount = $db->sth->prepare($sql_client_amount);
											$stmt_client_amount->bindParam(':field_val', $transfer_phone_no, PDO::PARAM_STR); 
											$stmt_client_amount->bindParam(':field_val1', $transfer_country, PDO::PARAM_STR); 
											$stmt_client_amount->execute();
											$total_client_amount = $stmt_client_amount->rowCount();

											if($total_client_amount > 0)
											{

												$client_amount = $stmt_client_amount->fetchColumn();
												$client_amount = floatval($client_amount);
												$client_amount = $client_amount + $amount_to_transfer_int;
											
												$sql_update = "Update tbl_no_of_points Set amount ='$client_amount' WHERE phone_number = '$transfer_phone_no' AND country = '$transfer_country'";
    	                                                                        							$stmt_update = $db->sth->prepare($sql_update);
	                                                                            							$stmt_update->execute();
                                                                                								$total_update =  $stmt_update->rowCount();

												if($total_update > 0)
												{
													
													$amount_balance =floatval( $amount ) - $amount_to_transfer_int;
													$sql_update_username = "Update tbl_no_of_points Set amount ='$amount_balance' WHERE phone_number = '$username' AND country = '$country'";
    	                                                                        								$stmt_update_username = $db->sth->prepare($sql_update_username);
	                              		                                              							$stmt_update_username->execute();
                                                                        	        								$total_update_username =  $stmt_update_username->rowCount();

													if($total_update_username > 0)
													{

														$date_transfered = date('Y-m-d H:i:s');
 																																	$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (	
														transaction_id,
														phone_number,
														phone_number_recipient,
														no_of_points_transfered,
														name_of_beneficiary,
														date_transfered,
														media_used,
														transaction_type,
														transfer_processed_by) 
    													VALUES(:transaction_id,:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
														$stmt_insert_history->bindParam(':transaction_id', $transaction_id);												
    														$stmt_insert_history->bindParam(':phone_number', $username);																															$stmt_insert_history->bindParam(':phone_number_recipient', $transfer_phone_no);
														$stmt_insert_history->bindParam(':no_of_points_transfered', $amount_to_transfer_int);																												$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
														$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
														$stmt_insert_history->bindParam(':media_used', $media);
														$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
														$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
								
														$stmt_insert_history->execute();
														$total_insert_history =  $stmt_insert_history->rowCount();
														if($total_insert_history > 0)
														{

															$success = "true";
															$amount_balance_string = strval($amount_balance);
															$message = "Congratulations You Have Successfully Transfered " . $amount_to_transfer . " To Phone Number " . $transfer_phone_no . " Your Balance Is Now " . $amount_balance_string;
															array_push($response,array("success"=>$success,"message"=>$message));
															echo json_encode($response);

														}
													
												}											

											}

										}else{

											//new insert
											$stmt_insert = $db->sth->prepare("INSERT INTO tbl_no_of_points (
												phone_number,
												no_of_points,
												amount,country) 
											VALUES(:phone_number,:no_of_points,:amount,:country)");
												$stmt_insert->bindParam(':phone_number', $transfer_phone_no);
												$stmt_insert->bindParam(':no_of_points', $amount_to_transfer_int);
												$stmt_insert->bindParam(':amount', $amount_to_transfer_int);
												$stmt_insert->bindParam(':country', $transfer_country);
												$stmt_insert->execute();
												$total_insert =  $stmt_insert->rowCount();

											if($total_insert > 0)
											{
												$amount_balance =floatval( $amount ) - $amount_to_transfer_int;
													$sql_update_username = "Update tbl_no_of_points Set amount ='$amount_balance' WHERE phone_number = '$username' AND country = '$country'";
    	                                                                        								$stmt_update_username = $db->sth->prepare($sql_update_username);
	                              		                                              							$stmt_update_username->execute();
                                                                        	        								$total_update_username =  $stmt_update_username->rowCount();

													if($total_update_username > 0)
													{

														$date_transfered = date('Y-m-d H:i:s');
 																																	$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (	
														transaction_id,
														phone_number,
														phone_number_recipient,
														no_of_points_transfered,
														name_of_beneficiary,
														date_transfered,
														media_used,
														transaction_type,
														transfer_processed_by) 
    													VALUES(:transaction_id,:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
														$stmt_insert_history->bindParam(':transaction_id', $transaction_id);												
    														$stmt_insert_history->bindParam(':phone_number', $username);																															$stmt_insert_history->bindParam(':phone_number_recipient', $transfer_phone_no);
														$stmt_insert_history->bindParam(':no_of_points_transfered', $amount_to_transfer_int);																												$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
														$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
														$stmt_insert_history->bindParam(':media_used', $media);
														$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
														$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
								
														$stmt_insert_history->execute();
														$total_insert_history =  $stmt_insert_history->rowCount();
														if($total_insert_history > 0)
														{

															$success = "true";
															$amount_balance_string = strval($amount_balance);
															$message = "Congratulations You Have Successfully Transfered " . $amount_to_transfer . " To Phone Number " . $transfer_phone_no . " Your Balance Is Now " . $amount_balance_string;
															array_push($response,array("success"=>$success,"message"=>$message));
															echo json_encode($response);

														}
												}

											}

										}

									}										
									
								}
							}

						}else{
							//error float above balance
							$success = "false";
							$message = "Your Transfer Amount Is Above Your Balance. Please Check And Try Again";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
						}
						$db->sth->commit();
					}catch(PDOException $e){
					
						if ($e->errorInfo[1] == 1062) 
						{
							
							$success = "false";
		 					$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 					$error_message=$e->getMessage();
		 					$error_type='duplicate';
		 					handler($error_message,$file_name,$success,$error_type);
		 					$db->sth->rollBack();
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
														
						}else{
							$success = "false";
		 					$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 					$error_message=$e->getMessage();
		 					$error_type='exception';
		 					handler($error_message,$file_name,$success,$error_type);
		 					$db->sth->rollBack();
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
																
						}
					}		
					
	
					}
				}
			}

		}
	}else{//1							
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		foreach($result as $row)
		{
			$name = $row['name'];
			$surname = $row['surname'];
	
		}	
		$first_last_name = $name. " " .$surname;
	
		$sql_due_date = "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val AND purchase_country = :field_val1";
		$stmt_due_date = $db->sth->prepare($sql_due_date);
		$stmt_due_date->bindParam(':field_val', $username, PDO::PARAM_STR); 
		$stmt_due_date->bindParam(':field_val1', $country, PDO::PARAM_STR); 
		$stmt_due_date->execute();
		$total_due_date = $stmt_due_date->rowCount();

 		if($total_due_date <= 0)
		{
			$success = "false";
			$message = "Your Account Is Not Up To Date. Please Go Ahead And Pay Your Subscriptions For Your Account To Be Up To Date And Be Able To Make Transfers.";
			array_push($response,array("success"=>$success,"message"=>$message));
			echo json_encode($response);							
		}else{
			$due_date = $stmt_due_date->fetchColumn();
			$todayDate = date('Y-m-d');
			if($todayDate > $due_date){
				$success = "false";
				$message = "Your Account Is Not Up To Date. Please Go Ahead And Pay Your Subscriptions For Your Account To Be Up To Date And Be Able To Make Transfers.";
				array_push($response,array("success"=>$success,"message"=>$message));
				echo json_encode($response);							
			}else{

				$sql_username_due_date= "SELECT MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val AND country = :field_val1"; 
				$stmt_username_due_date = $db->sth->prepare($sql_username_due_date);
				$stmt_username_due_date->bindParam(':field_val', $username, PDO::PARAM_STR); 
				$stmt_username_due_date->bindParam(':field_val1', $country, PDO::PARAM_STR); 
				$stmt_username_due_date->execute();
				$total_username_due_date = $stmt_username_due_date->rowCount();

				if($total_username_due_date > 0)
				{
					$username_due_date = $stmt_username_due_date->fetchColumn();
				}

				$sql_amount= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
				$stmt_amount = $db->sth->prepare($sql_amount);
				$stmt_amount->bindParam(':field_val', $username, PDO::PARAM_STR); 
				$stmt_amount->bindParam(':field_val1', $country, PDO::PARAM_STR); 

				$stmt_amount->execute();
				$total_amount = $stmt_amount->rowCount();

 				if($total_amount <= 0)
				{				
					$amount = 0;
					$success = "false";
					$message = "Your Account Balance Is Zero. Please Go Ahead And Pay Your Subscriptions For Your Account To Be Up To Date And Be Able To Make Transfers.";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
 				}else{
					$amount = $stmt_amount->fetchColumn();						
					try
					{
						$db->sth->beginTransaction();
						if($amount >= $amount_to_transfer_int)
						{

							$sql_due_date_client = "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val AND purchase_country = :field_val1";
							$stmt_due_date_client = $db->sth->prepare($sql_due_date_client);
							$stmt_due_date_client->bindParam(':field_val', $transfer_phone_no, PDO::PARAM_STR); 
							$stmt_due_date_client->bindParam(':field_val1', $transfer_country, PDO::PARAM_STR); 
							$stmt_due_date_client->execute();
							$total_due_date_client = $stmt_due_date_client->rowCount();

							if($total_due_date_client <= 0){

								$success = "false";
								$message = "Phone Number ".$transfer_phone_no." Account Is Not Up To Date And Cannot Receive Any Transfers.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);							

							}else{

								$due_date_client = $stmt_due_date_client->fetchColumn();
								$todayDate = date('Y-m-d');
								if($todayDate > $due_date_client){
									$success = "false";
									$message =  "Phone Number ".$transfer_phone_no." Account Is Not Up To Date. Please Go Ahead And Pay Your Subscriptions For Your Account To Be Up To Date And Be Able To Receive Transfers.";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}else{
									//do the transfers....

									$sql_client_due_date= "SELECT MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val AND country = :field_val1"; 
									$stmt_client_due_date = $db->sth->prepare($sql_client_due_date);
									$stmt_client_due_date->bindParam(':field_val', $transfer_phone_no, PDO::PARAM_STR); 
									$stmt_client_due_date->bindParam(':field_val1', $transfer_country, PDO::PARAM_STR); 
									$stmt_client_due_date->execute();
									$total_client_due_date = $stmt_client_due_date->rowCount();

									if($total_client_due_date > 0)
									{
										$client_due_date = $stmt_client_due_date->fetchColumn();	
										
										$sql_client_amount= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
										$stmt_client_amount = $db->sth->prepare($sql_client_amount);
										$stmt_client_amount->bindParam(':field_val', $transfer_phone_no, PDO::PARAM_STR); 
										$stmt_client_amount->bindParam(':field_val1', $transfer_country, PDO::PARAM_STR); 

										$stmt_client_amount->execute();
										$total_client_amount = $stmt_client_amount->rowCount();

										if($total_client_amount > 0)
										{
											$client_amount = $stmt_client_amount->fetchColumn();
											$client_amount = floatval($client_amount);
											$client_amount = $client_amount + $amount_to_transfer_int;
											
											$sql_update = "Update tbl_no_of_points Set amount ='$client_amount' WHERE phone_number = '$transfer_phone_no' AND country = '$transfer_country'";
    	                                                                        						$stmt_update = $db->sth->prepare($sql_update);
	                                                                            						$stmt_update->execute();
                                                                                							$total_update =  $stmt_update->rowCount();

											if($total_update > 0)
											{
												$amount_balance =floatval( $amount ) - $amount_to_transfer_int;
												$sql_update_username = "Update tbl_no_of_points Set amount ='$amount_balance' WHERE phone_number = '$username' AND country = '$country'";
    	                                                                        							$stmt_update_username = $db->sth->prepare($sql_update_username);
	                              		                                              						$stmt_update_username->execute();
                                                                        	        							$total_update_username =  $stmt_update_username->rowCount();

												if($total_update_username > 0)
												{
													$date_transfered = date('Y-m-d H:i:s');
 																																$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (												
													transaction_id,
													phone_number,
													phone_number_recipient,
													no_of_points_transfered,
													name_of_beneficiary,
													date_transfered,
													media_used,
													transaction_type,
													transfer_processed_by) 
    												VALUES(:transaction_id,:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
													$stmt_insert_history->bindParam(':transaction_id', $transaction_id);												
    													$stmt_insert_history->bindParam(':phone_number', $username);																															$stmt_insert_history->bindParam(':phone_number_recipient', $transfer_phone_no);
													$stmt_insert_history->bindParam(':no_of_points_transfered', $amount_to_transfer_int);																											$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
													$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
													$stmt_insert_history->bindParam(':media_used', $media);
													$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
													$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
								
													$stmt_insert_history->execute();
													$total_insert_history =  $stmt_insert_history->rowCount();
													if($total_insert_history > 0)
													{

														$success = "true";
														$amount_balance_string = strval($amount_balance);
														$message = "Congratulations You Have Successfully Transfered " . $amount_to_transfer . " To Phone Number " . $transfer_phone_no . " Your Balance Is Now " . $amount_balance_string;
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);

													}
													
												}											

											}

										}else{

											$stmt_insert = $db->sth->prepare("INSERT INTO tbl_no_of_points (
												phone_number,
												no_of_points,
												amount,country) 
											VALUES(:phone_number,:no_of_points,:amount,:country)");
												$stmt_insert->bindParam(':phone_number', $transfer_phone_no);
												$stmt_insert->bindParam(':no_of_points', $amount_to_transfer_int);
												$stmt_insert->bindParam(':amount', $amount_to_transfer_int);
												$stmt_insert->bindParam(':country', $transfer_country);
												$stmt_insert->execute();
												$total_insert =  $stmt_insert->rowCount();
											if($total_insert > 0)
											{

											
											$amount_balance =floatval( $amount ) - $amount_to_transfer_int;
												$sql_update_username = "Update tbl_no_of_points Set amount ='$amount_balance' WHERE phone_number = '$username' AND country = '$country'";
    	                                                                        							$stmt_update_username = $db->sth->prepare($sql_update_username);
	                              		                                              						$stmt_update_username->execute();
                                                                        	        							$total_update_username =  $stmt_update_username->rowCount();

												if($total_update_username > 0)
												{
													$date_transfered = date('Y-m-d H:i:s');
 																																$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (												
													transaction_id,
													phone_number,
													phone_number_recipient,
													no_of_points_transfered,
													name_of_beneficiary,
													date_transfered,
													media_used,
													transaction_type,
													transfer_processed_by) 
    												VALUES(:transaction_id,:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
													$stmt_insert_history->bindParam(':transaction_id', $transaction_id);												
    													$stmt_insert_history->bindParam(':phone_number', $username);																															$stmt_insert_history->bindParam(':phone_number_recipient', $transfer_phone_no);
													$stmt_insert_history->bindParam(':no_of_points_transfered', $amount_to_transfer_int);																											$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
													$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
													$stmt_insert_history->bindParam(':media_used', $media);
													$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
													$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
								
													$stmt_insert_history->execute();
													$total_insert_history =  $stmt_insert_history->rowCount();
													if($total_insert_history > 0)
													{

														$success = "true";
														$amount_balance_string = strval($amount_balance);
														$message = "Congratulations You Have Successfully Transfered " . $amount_to_transfer . " To Phone Number " . $transfer_phone_no . " Your Balance Is Now " . $amount_balance_string;
														array_push($response,array("success"=>$success,"message"=>$message));
														echo json_encode($response);

													}
													
												}							
											}

										}

									}										
									
								}
							}

						}else{
							//error float above balance
							$success = "false";
							$message = "Your Transfer Amount Is Above Your Balance. Please Check And Try Again";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
						}
						$db->sth->commit();
					}catch(PDOException $e){
					
						if ($e->errorInfo[1] == 1062) 
						{
							$success = "false";
		 					$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 					$error_message=$e->getMessage();
		 					$error_type='duplicate';
		 					handler($error_message,$file_name,$success,$error_type);
		 					$db->sth->rollBack();
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
														
						}else{
							$success = "false";
		 					$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 					$error_message=$e->getMessage();
		 					$error_type='exception';
		 					handler($error_message,$file_name,$success,$error_type);
		 					$db->sth->rollBack();
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
																
						}
					}		
					

				}
			}
		}

	}//1
 }else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    echo json_encode($response);
}

	
?>