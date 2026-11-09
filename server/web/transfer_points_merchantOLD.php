<?php
    
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$response = array();
$file_name='transfer_points';

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
	
	$submitbutton= isset($_POST['Submit']);
	//$logout
		if($submitbutton){
			$main_menu = $_SESSION["main_menu"];

			if($main_menu=="main_merchant")
			{
				$fgmembersite->RedirectToURL("main_merchant.php");
			}else{
				$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");
			}
		}else{

			$fgmembersite->RedirectToURL("logout.php");
		}

	
}else{

	$username = ($_SESSION["merchant_phone_number"]);
	$transfer_processed_by=($_SESSION["user_name"]);
	$phone_no = ($_SESSION["transfer_phone_no"]);
	$no_of_points_to_transfer = ($_SESSION["no_of_points"]);
	$no_of_points_to_transfer_int = intval($no_of_points_to_transfer);
 	$media = "Internet";
	$transaction_type='Transfer';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	//Check whether the username exists and return message telling the client to register

	$sql= "SELECT name, surname FROM tbl_clients WHERE phone_number = :field_val"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

 	if($total <= 0){
 
 			//Check whether the phone number is a merchant number
 			
 					$sql_merchant = "SELECT merchant_code, merchant_name FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
					$stmt_merchant = $db->sth->prepare($sql_merchant);
					$stmt_merchant->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
					$stmt_merchant->execute();
					$total_merchant = $stmt_merchant->rowCount();
					if($total_merchant <= 0)
					{
						$success = "false";
						$message = "Phone Number Does Not Exist..Please Register Before You Can Transfer Points!!";
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($response);
					}else{							
						$result = $stmt_merchant->fetchAll(PDO::FETCH_ASSOC);						
						foreach($result as $row){
							$code = $row['merchant_code'];
							$name = $row['merchant_name'];
						}						
	
						$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
						$stmt->execute();
						$total = $stmt->rowCount();
 						if($total <= 0)
						{				
							$no_of_points = 0; 
    							$success = "false";
							$message = "Your Account Balance Is Zero. Please Go Ahead And Purchase Points For Your Account To Be Able To Transfer Points.";
							//array_push($response,array("success"=>$success,"message"=>$message));
							//echo json_encode($response);
 						}else{
							$success = "true";
							$message = "Account Has A Credit Balance In Merchant";
							$no_of_points = $stmt->fetchColumn();
							try
							{
								$db->sth->beginTransaction();
								if($no_of_points >= $no_of_points_to_transfer_int)
								{

									//Check balance of points of the beneficiary																$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
									$stmt_beneficiary = $db->sth->prepare($sql);
									$stmt_beneficiary->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 													$stmt_beneficiary->execute();																		$total_beneficiary = $stmt_beneficiary->rowCount();
									if($total_beneficiary <= 0)																		{				  																		$stmt_insert = $db->sth->prepare("INSERT INTO tbl_no_of_points (																phone_number,
													no_of_points) 
										VALUES(:phone_number,:no_of_points)");
										$stmt_insert->bindParam(':phone_number', $phone_no);
										$stmt_insert->bindParam(':no_of_points', $no_of_points_to_transfer_int);
										$stmt_insert->execute();
										$total_insert =  $stmt_insert->rowCount();
																															if($total_insert > 0)
										{										
											//Update the table
											$balance_of_points = $no_of_points - $no_of_points_to_transfer_int;
											if($balance_of_points == 0)
											{																						//delete entry we do not need zero points on table
												$sql = "DELETE from tbl_no_of_points Where phone_number='$username'";
									    	                $stmt_delete = $db->sth->prepare($sql);
										                $stmt_delete->execute();
									    	                 $total_delete =  $stmt_delete->rowCount();
									                         if ($total_delete > 0)																				{				 																	//Do an insert into tbl_points_transfers
													$first_last_name = $code. " " .$name;
													$date_transfered = date('Y-m-d H:i:s');
													$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (
														phone_number,
														phone_number_recipient,
														no_of_points_transfered,
														name_of_beneficiary,
														date_transfered,
														media_used,
														transaction_type,
														transfer_processed_by) 
					    								VALUES												(:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
					    						$stmt_insert_history->bindParam(':phone_number', $username);
					    						$stmt_insert_history->bindParam(':phone_number_recipient', $phone_no);
									$stmt_insert_history->bindParam(':no_of_points_transfered', $no_of_points_to_transfer_int);
											$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
											$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
											$stmt_insert_history->bindParam(':media_used', $media);
											$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
											$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
											$stmt_insert_history->execute();
											$total_insert_history =  $stmt_insert_history->rowCount();																if($total_insert_history > 0){
														$success = "true";
														$balance_of_points_string = strval($balance_of_points);															$message = "Congratulations You Have Successfully Transfered " . $no_of_points_to_transfer . " Points To Phone Number " . $phone_no . " Your Balance Is Now " . $balance_of_points_string . " Points.";
												
													}
																	  																}
									                
									             	}else{
											$sql = "Update tbl_no_of_points Set no_of_points ='$balance_of_points' Where phone_number='$username'";
    	                                                                        	$stmt_update = $db->sth->prepare($sql);
	                                                                            	$stmt_update->execute();
                                                                                	$total_update =  $stmt_update->rowCount();
 																																if ($total_update > 0)																			{
									//Do an insert into tbl_points_transfers
 											$first_last_name = $code. " " .$name;
 											$date_transfered = date('Y-m-d H:i:s');
											$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (
														phone_number,
														phone_number_recipient,
														no_of_points_transfered,
														name_of_beneficiary,
														date_transfered,
														media_used,
														transaction_type,
														transfer_processed_by) 
					    							VALUES(:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
					    					$stmt_insert_history->bindParam(':phone_number', $username);
					    					$stmt_insert_history->bindParam(':phone_number_recipient', $phone_no);
								$stmt_insert_history->bindParam(':no_of_points_transfered', $no_of_points_to_transfer_int);
								$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
								$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
								$stmt_insert_history->bindParam(':media_used', $media);
								$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
								$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
								$stmt_insert_history->execute();
								$total_insert_history =  $stmt_insert_history->rowCount();
							if($total_insert_history > 0){
											$success = "true";
											$balance_of_points_string = strval($balance_of_points);															$message = "Congratulations You Have Successfully Transfered " . $no_of_points_to_transfer . " Points To Phone Number " . $phone_no . " Your Balance Is Now " . $balance_of_points_string . " Points.";
									
											}
										}
								  }
							}
																						}else{
																								$no_of_points_of_beneficiary= $stmt_beneficiary->fetchColumn();
																							$new_balance_of_points = $no_of_points_of_beneficiary + $no_of_points_to_transfer_int;											$sql = "Update tbl_no_of_points Set no_of_points ='$new_balance_of_points' Where phone_number='$phone_no'";
    	        $stmt_update_beneficiary = $db->sth->prepare($sql);
	        $stmt_update_beneficiary->execute();
                $total_update_beneficiary =  $stmt_update_beneficiary->rowCount();
 																							if ($total_update_beneficiary > 0){
				//do an update
				$balance_of_points = $no_of_points - $no_of_points_to_transfer_int;													//error_log(strip_tags($balance_of_points), 0);
			if($balance_of_points == 0)
				{																						$sql = "DELETE from tbl_no_of_points Where phone_number='$username'";
					$stmt_delete = $db->sth->prepare($sql);
					$stmt_delete->execute();
					$total_delete =  $stmt_delete->rowCount();
					if ($total_delete > 0){ 																			$first_last_name = $code. " " .$name;
						$date_transfered = date('Y-m-d H:i:s');
						$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (
										phone_number,
										phone_number_recipient,
										no_of_points_transfered,
										name_of_beneficiary,
										date_transfered,
										media_used,
										transaction_type,
										transfer_processed_by) 
					VALUES(:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
								$stmt_insert_history->bindParam(':phone_number', $username);
								$stmt_insert_history->bindParam(':phone_number_recipient', $phone_no);
								$stmt_insert_history->bindParam(':no_of_points_transfered', $no_of_points_to_transfer_int);
								$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
								$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
								$stmt_insert_history->bindParam(':media_used', $media);
								$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
								$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
								$stmt_insert_history->execute();
								$total_insert_history =  $stmt_insert_history->rowCount();
								if($total_insert_history > 0){
									$success = "true";
									$balance_of_points_string = strval($balance_of_points);															$message = "Congratulations You Have Successfully Transfered " . $no_of_points_to_transfer . " Points To Phone Number " . $phone_no . " Your Balance Is Now " . $balance_of_points_string . " Points.";
								
								}			
			  		}	
				}else{																							$sql = "Update tbl_no_of_points Set no_of_points ='$balance_of_points' Where phone_number='$username'";
    	                                        $stmt_update_username = $db->sth->prepare($sql);
	                                        $stmt_update_username->execute();
						$total_update_username =  $stmt_update_username->rowCount();
 						if ($total_update_username > 0){
							$first_last_name = $code. " " .$name;
							$date_transfered = date('Y-m-d H:i:s');
							$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (
									phone_number,
									phone_number_recipient,
									no_of_points_transfered,
									name_of_beneficiary,
									date_transfered,
									media_used,
									transaction_type,
									transfer_processed_by) 
					VALUES(:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
								$stmt_insert_history->bindParam(':phone_number', $username);
								$stmt_insert_history->bindParam(':phone_number_recipient', $phone_no);
								$stmt_insert_history->bindParam(':no_of_points_transfered', $no_of_points_to_transfer_int);
								$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
								$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
								$stmt_insert_history->bindParam(':media_used', $media);
								$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
								$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
								$stmt_insert_history->execute();
								$total_insert_history =  $stmt_insert_history->rowCount();
							if($total_insert_history > 0){
									$success = "true";
									$balance_of_points_string = strval($balance_of_points);															$message = "Congratulations You Have Successfully Transfered " . $no_of_points_to_transfer . " Points To Phone Number " . $phone_no . " Your Balance Is Now " . $balance_of_points_string . " Points.";
									//array_push($response,array("success"=>$success,"message"=>$message));
									//echo json_encode($response);
									}			
			  				}								
					}		
										  														}				
																						}
	}else{
	//error float above balance
	$success = "false";
	$message = "Your Transfer Is Above Your Balance. Please Buy Points Before You Transfer";
	//array_push($response,array("success"=>$success,"message"=>$message));
	//echo json_encode($response);
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
			//array_push($response,array("success"=>$success,"message"=>$message));
			//echo json_encode($response);
		}else{
			$success = "false";
		 	$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 	$error_message=$e->getMessage();
		 	$error_type='exception';
		 	handler($error_message,$file_name,$success,$error_type);
		 	$db->sth->rollBack();
			//array_push($response,array("success"=>$success,"message"=>$message));
			//echo json_encode($response);
		}	
	}				
	}	
	}
		}else{
				$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
				foreach($result as $row){
					$name = $row['name'];
					$surname = $row['surname'];
							}	
	

				$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
				$stmt = $db->sth->prepare($sql);
				$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();

 				if($total <= 0){				
					$no_of_points = 0; 
    					$success = "false";
					$message = "Your Account Balance Is Zero. Please Go Ahead And Purchase Points For Your Account To Be Able To Transfer Points.";
					//array_push($response,array("success"=>$success,"message"=>$message));
					//echo json_encode($response);
 				}else{
					$success = "true";
					$message = "Account Has A Credit Balance Client";
					$no_of_points = $stmt->fetchColumn();

					try
					{
						$db->sth->beginTransaction();
						if($no_of_points >= $no_of_points_to_transfer_int)
						{
								//Check balance of points of the beneficiary
								$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
								$stmt_beneficiary = $db->sth->prepare($sql);
								$stmt_beneficiary->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
								$stmt_beneficiary->execute();
								$total_beneficiary = $stmt_beneficiary->rowCount();
					
								if($total_beneficiary <= 0)
								{				
  								//do an insert
										$stmt_insert = $db->sth->prepare("INSERT INTO tbl_no_of_points (
										phone_number,
										no_of_points) 
    								VALUES(:phone_number,:no_of_points)");
    								$stmt_insert->bindParam(':phone_number', $phone_no);
										$stmt_insert->bindParam(':no_of_points', $no_of_points_to_transfer_int);
								
										$stmt_insert->execute();
										$total_insert =  $stmt_insert->rowCount();
	
										if($total_insert > 0)
										{
											
											//Update the table
											$balance_of_points = $no_of_points - $no_of_points_to_transfer_int;
											
											if($balance_of_points == 0)
												{																					//delete entry we do not need zero points on table
												$sql = "DELETE from tbl_no_of_points Where phone_number='$username'";
									    	  		$stmt_delete = $db->sth->prepare($sql);
										      		$stmt_delete->execute();									    	                                                               
									        		$total_delete =  $stmt_delete->rowCount();
									        
									        		if($total_delete > 0)
									        		{
									        			$first_last_name = $name. " " .$surname;
 													$date_transfered = date('Y-m-d H:i:s');
 											$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (													phone_number,
												phone_number_recipient,																			no_of_points_transfered,
												name_of_beneficiary,																			date_transfered,
												media_used,
												transaction_type,
												transfer_processed_by) 
    												VALUES				(:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
    												$stmt_insert_history->bindParam(':phone_number', $username);
    												$stmt_insert_history->bindParam(':phone_number_recipient', $phone_no);										$stmt_insert_history->bindParam(':no_of_points_transfered', $no_of_points_to_transfer_int);
									$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
									$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
									$stmt_insert_history->bindParam(':media_used', $media);
									$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
									$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
									$stmt_insert_history->execute();
									$total_insert_history =  $stmt_insert_history->rowCount();
									if($total_insert_history > 0){
										$success = "true";
										$balance_of_points_string = strval($balance_of_points);
										$message = "Congratulations You Have Successfully Transfered " . $no_of_points_to_transfer . " Points To Phone Number " . $phone_no . " Your Balance Is Now " . $balance_of_points_string . " Points.";
												}
									        	
									               }									       
									}else{//balance > 0

								$sql = "Update tbl_no_of_points Set no_of_points ='$balance_of_points' Where phone_number='$username'";
    	                                                                        $stmt_update = $db->sth->prepare($sql);
	                                                                         $stmt_update->execute();
    	                                                               
                                                                                $total_update =  $stmt_update->rowCount();
 										if ($total_update > 0)
 										{
 										//Do an insert into tbl_points_transfers
 										$first_last_name = $name. " " .$surname;
										$date_transfered = date('Y-m-d H:i:s');
 										$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (
											phone_number,
											phone_number_recipient,
											no_of_points_transfered,
											name_of_beneficiary,
											date_transfered,
											media_used,
											transaction_type,
											transfer_processed_by) 
    											VALUES				(:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
    											$stmt_insert_history->bindParam(':phone_number', $username);
    											$stmt_insert_history->bindParam(':phone_number_recipient', $phone_no);
									$stmt_insert_history->bindParam(':no_of_points_transfered', $no_of_points_to_transfer_int);
											$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
											$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
											$stmt_insert_history->bindParam(':media_used', $media);
											$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
											$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
											$stmt_insert_history->execute();
											$total_insert_history =  $stmt_insert_history->rowCount();
											if($total_insert_history > 0){
												$success = "true";
												$balance_of_points_string = strval($balance_of_points);
									
												$message = "Congratulations You Have Successfully Transfered " . $no_of_points_to_transfer . " Points To Phone Number " . $phone_no . " Your Balance Is Now " . $balance_of_points_string . " Points.";
																																	}
										}//totalupdate>0
									}//else balance > 0
									}//total insert > 0
								}else{//beneficiary > 0
								
									$no_of_points_of_beneficiary= $stmt_beneficiary->fetchColumn();
									$new_balance_of_points = $no_of_points_of_beneficiary + $no_of_points_to_transfer_int;
							$sql = "Update tbl_no_of_points Set no_of_points ='$new_balance_of_points' Where phone_number='$phone_no'";
    	                                                                $stmt_update_beneficiary = $db->sth->prepare($sql);
	                                                                $stmt_update_beneficiary->execute();
    	                                                               
                                                                        $total_update_beneficiary =  $stmt_update_beneficiary->rowCount();
 									if ($total_update_beneficiary > 0){

										//do an update
										$balance_of_points = $no_of_points - $no_of_points_to_transfer_int;
													
										if($balance_of_points == 0)
											{							
													//delete entry we do not need zero points on table
												$sql = "DELETE from tbl_no_of_points Where phone_number='$username'";
									    	  		$stmt_delete = $db->sth->prepare($sql);
										      		$stmt_delete->execute();									    	                                                                $total_delete =  $stmt_delete->rowCount();
									        		if($total_delete > 0)
									        		{
									        			$first_last_name = $name. " " .$surname;
 													$date_transfered = date('Y-m-d H:i:s');
 													$stmt_insert_history = $db->sth->prepare("INSERT INTO 												tbl_points_transfers (
														phone_number,
														phone_number_recipient,
														no_of_points_transfered,
														name_of_beneficiary,
														date_transfered,
														media_used,
														transaction_type,
														transfer_processed_by) 
    													VALUES				(:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
    												$stmt_insert_history->bindParam(':phone_number', $username);
    												$stmt_insert_history->bindParam(':phone_number_recipient', $phone_no);
									$stmt_insert_history->bindParam(':no_of_points_transfered', $no_of_points_to_transfer_int);
										$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
												$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
												$stmt_insert_history->bindParam(':media_used', $media);
											$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
												$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
												$stmt_insert_history->execute();
												$total_insert_history =  $stmt_insert_history->rowCount();
												if($total_insert_history > 0){
													$success = "true";
													$balance_of_points_string = strval($balance_of_points);
												$message = "Congratulations You Have Successfully Transfered " . $no_of_points_to_transfer . " Points To Phone Number " . $phone_no . " Your Balance Is Now " . $balance_of_points_string . " Points.";
														}
									  	        }									     
									      
									    }else{  //balance > 0
										$sql = "Update tbl_no_of_points Set no_of_points ='$balance_of_points' Where phone_number='$username'";
    	                                                                        $stmt_update_username = $db->sth->prepare($sql);
	                                                                        $stmt_update_username->execute();
    	                                                               
                                                                                $total_update_username =  $stmt_update_username->rowCount();
 										if ($total_update_username > 0){
 											$first_last_name = $name. " " .$surname;
 											$date_transfered = date('Y-m-d H:i:s');
 										$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_points_transfers (
											phone_number,
											phone_number_recipient,
											no_of_points_transfered,
											name_of_beneficiary,
											date_transfered,
											media_used,
											transaction_type,
											transfer_processed_by) 
    										VALUES				(:phone_number,:phone_number_recipient,:no_of_points_transfered,:name_of_beneficiary,:date_transfered,:media_used,:transaction_type,:transfer_processed_by)");
    										$stmt_insert_history->bindParam(':phone_number', $username);
    										$stmt_insert_history->bindParam(':phone_number_recipient', $phone_no);
									$stmt_insert_history->bindParam(':no_of_points_transfered',$no_of_points_to_transfer_int);
										$stmt_insert_history->bindParam(':name_of_beneficiary',$first_last_name);
										$stmt_insert_history->bindParam(':date_transfered', $date_transfered);
										$stmt_insert_history->bindParam(':media_used', $media);
										$stmt_insert_history->bindParam(':transaction_type', $transaction_type);
										$stmt_insert_history->bindParam(':transfer_processed_by', $transfer_processed_by);
										$stmt_insert_history->execute();
										$total_insert_history =  $stmt_insert_history->rowCount();
										if($total_insert_history > 0){
											$success = "true";
											$balance_of_points_string = strval($balance_of_points);
											$message = "Congratulations You Have Successfully Transfered " . $no_of_points_to_transfer . " Points To Phone Number " . $phone_no . " Your Balance Is Now " . $balance_of_points_string . " Points.";
												}
										}
									}
								}
							}
						}else{
							//error float above balance
							$success = "false";
							$message = "Your Transfer Is Above Your Balance. Please Buy Points Before You Transfer";
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
								}else{
									$success = "false";
		 							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 							$error_message=$e->getMessage();
		 							$error_type='exception';
		 							handler($error_message,$file_name,$success,$error_type);
		 							$db->sth->rollBack();
								}
					}			
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
					<input type='submit' name='Submit' value='Main Menu' />
					<input type='submit' name='Logout' value='Logout' /><br/><br/>
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

