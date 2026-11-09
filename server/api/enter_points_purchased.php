
<?php

$response = array();
include 'functions.php';
$file_name='enter_points_purchased';
$purchase_details = array();

if (isset($_POST['phone_number']) && isset($_POST['username']) && isset($_POST['transaction_type']) && isset($_POST['transaction_number']) && isset($_POST['purchase_amount']) && isset($_POST['purchase_currency']) && isset($_POST['purchase_date'])){

//error_log(strip_tags("coming in "), 0);
$phone_number = trim($_POST["phone_number"]);
$username = trim($_POST["username"]);
$transtype = trim($_POST["transaction_type"]);
$transnum = trim($_POST["transaction_number"]);
$currency = trim($_POST["purchase_currency"]);
$amount = $_POST["purchase_amount"];
$dateOfPurchase = $_POST["purchase_date"];
$processedDate = date('Y-m-d H:i:s');
$purchase_status = 'Y';

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$bill_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$bill_number_generated = mt_rand(0, 999999);
	$bill_number=$bill_letter.$bill_number_generated;
	$date_bill_created = date('Y-m-d H:i:s');
	$total_bill=0;
	$total_bill_amount=0;
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	//$transnum = $formvars['transnum'];
 	$sql= "SELECT phone_number,group_category,country_name FROM tbl_clients WHERE phone_number = :field_val "; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR);  
	$stmt->execute();
	$total = $stmt->rowCount();

	if($total <= 0) {
					$success = "false";
					$message = "Phone Number Entered ".$phone_number." Does Not Exist In The System. Please Check Again";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
			}else{

					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result as $row)
						{    							
							$client_phone_number_returned=$row['phone_number'];
							$client_group_category_returned=$row['group_category'];
							$client_country_name_returned=$row['country_name'];
						}
		
					$sql= "SELECT phone_number FROM tbl_purchase_points_details WHERE transaction_number = :field_val AND phone_number = :field_val1 AND transaction_type != 'Cash' AND purchase_country = :field_val2"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $transnum, PDO::PARAM_STR);
					$stmt->bindParam(':field_val1', $phone_number, PDO::PARAM_STR);
					$stmt->bindParam(':field_val2', $client_country_name_returned, PDO::PARAM_STR);   
					$stmt->execute();
					$total = $stmt->rowCount();

					if($total > 0) 
						{
		
							$success = "false";
							$message = "Payment Transaction Number ". $transnum ." Is Already Processed. Please Check Client Transaction History.";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
						}else{
	
							$ddate = Date("Y-m-d h:i:s");

							try{
			
							
									$date_joined = date('Y-m-d H:i:s');
									$media='mobile';
									$type='Individual';
									//$currency = $formvars['currency'];
									$date_deactivated = date('Y-m-d H:i:s');
									$qry = "Select amount from tbl_currency_rates where type_of_currency = :field_val AND country_currency = :field_val1";
									$stmt = $db->prepare($qry);
									$stmt->bindParam(':field_val', $currency, PDO::PARAM_STR);
									$stmt->bindParam(':field_val1', $client_country_name_returned, PDO::PARAM_STR);  
    									$stmt->execute();
									$total =  $stmt->rowCount();

 									if($total <= 0)
									{
										$success = "false";
										$message = "Type Of Currency Does Not Exist...Please Enter Type Of Currency!";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);	
 									}else{
	    									
										$amount_per_point = $stmt->fetchColumn();
										$no_of_points_purchased = $amount / $amount_per_point;
										
										if($no_of_points_purchased < 10)
						{
							$success = "false";
							$message = "Number Of Points Purchased Should Be More Than 10. Please Try Again";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);	
						}else{
										$dt   = new DateTime();
										$date = $dt->createFromFormat('d/m/Y', $dateOfPurchase);
										$newDate = $date->format('Y-m-d');
										$sql_client_up_to_date= "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val"; 
										$stmt_client_up_to_date = $db->prepare($sql_client_up_to_date);
										$stmt_client_up_to_date->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
										$stmt_client_up_to_date->execute();
										$total_client_up_to_date = $stmt_client_up_to_date->rowCount();

										if($total_client_up_to_date > 0)
										{
											$client_due_date=$stmt_client_up_to_date->fetchColumn();
											$dt   = new DateTime();
											$dueDate = date('Y-m-d',strtotime($client_due_date.'+30days'));
										}else{
											$dueDate = date('Y-m-d',strtotime($newDate.'+30days'));
										}
									
								
										$stmt = $db->prepare("INSERT INTO tbl_purchase_points_details (
											phone_number,
											transaction_type,
                									type_of_currency,
                									purchase_amount,
											no_of_points_purchased,
											date_of_purchase,
                									time_of_purchase,
											transaction_number,
											media_used,
											due_date,
											processed_date,
											processed_by,
											purchase_status,
											purchase_done_by,
											purchase_country) 
    											VALUES 												(:phone_number,:transaction_type,:type_of_currency,:purchase_amount,:no_of_points_purchased,
											:date_of_purchase,:time_of_purchase,:transaction_number,:media_used,
											:due_date,:processed_date,:processed_by,:purchase_status,:purchase_done_by,:purchase_country)");
    											$stmt->bindParam(':phone_number', $phone_number);
											$stmt->bindParam(':transaction_type', $transtype);
											$stmt->bindParam(':type_of_currency', $currency);
											$stmt->bindParam(':purchase_amount', $amount);
											$stmt->bindParam(':no_of_points_purchased', $no_of_points_purchased);
    											$stmt->bindParam(':date_of_purchase', $newDate);
											$stmt->bindParam(':time_of_purchase', $ddate);
											$stmt->bindParam(':transaction_number', $transnum);
											$stmt->bindParam(':media_used', $media);
											$stmt->bindParam(':due_date', $dueDate);
											$stmt->bindParam(':processed_date', $processedDate);
											$stmt->bindParam(':processed_by', $username);
											$stmt->bindParam(':purchase_status', $purchase_status);
											$stmt->bindParam(':purchase_done_by', $phone_number);
											$stmt->bindParam(':purchase_country', $client_country_name_returned);
											$stmt->execute();
	
											$total =  $stmt->rowCount();
	
											if($total > 0)
											{
												$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
												$stmt_beneficiary = $db->prepare($sql);
												$stmt_beneficiary->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
												$stmt_beneficiary->execute();
												$total_beneficiary = $stmt_beneficiary->rowCount();
												if($total_beneficiary <= 0)
												{				
											//do an insert
													$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
													phone_number,
													no_of_points) 
													VALUES(:phone_number,:no_of_points)");
													$stmt_insert->bindParam(':phone_number', $phone_number);
													$stmt_insert->bindParam(':no_of_points', $no_of_points_purchased);
													$stmt_insert->execute();
													$total_insert =  $stmt_insert->rowCount();
												}else{
													$no_of_points_client_beneficiary = $stmt_beneficiary->fetchColumn();
													$no_of_points_client_beneficiary = $no_of_points_client_beneficiary + $no_of_points_purchased;
		                                                					$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_beneficiary' Where phone_number='$phone_number'";
		    	                                          					$stmt_update = $db->prepare($sql);
			                                              					$stmt_update->execute();
		    	                                                               			$total_update =  $stmt_update->rowCount();		   													}
				    						

											$success = "true";
											$message = "Congratulations You Have Successfully Purchased ". $no_of_points_purchased . " Points";
											array_push($response,array("success"=>$success,"message"=>$message));
											echo json_encode($response);		
										}
									}
							}
								$db->commit();
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Insert A Duplicate Value";
							//$message = $e->getMessage();
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							$error_message=$e->getMessage();
							$error_type='duplicate';
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later Duplicate");
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							$db->rollBack();
						
						}else{	
	
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later ");
							$message=$e->getMessage();
							error_log(strip_tags($message), 0);
							$error_type='exception';
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							//echo json_encode($response);
							$db->rollBack();
							
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