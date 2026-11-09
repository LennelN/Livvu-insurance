<?php
    
$response = array();
include 'functions.php';
$file_name='cash_out_points';

if (isset($_POST['username']) && isset($_POST['amount_to_transfer']) && isset($_POST['media']) && isset($_POST['points_processed_by']) && isset($_POST['merchant_code'])) {
	$username = $_POST["username"];		
	$amount_to_transfer = $_POST["amount_to_transfer"];
	$amount_to_transfer_int =floatval($amount_to_transfer);
 	$media = $_POST["media"];
	$points_processed_by = $_POST["points_processed_by"];
	$merchant_code = $_POST["merchant_code"];
	$transaction_type='CashOut';
	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$payment_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$payment_number_generated = mt_rand(0, 999999);
	$payment_number=$payment_letter.$payment_number_generated;
	$points_cashed_out_indicator = 'N';
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT(); 			
 	$sql_merchant = "SELECT merchant_code, merchant_name,merchant_licence_number,merchant_country FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1"; 
	$stmt_merchant = $db->sth->prepare($sql_merchant);
	$stmt_merchant->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt_merchant->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
	$stmt_merchant->execute();
	$total_merchant = $stmt_merchant->rowCount();
	if($total_merchant <= 0)
	{
		$success = "false";
		$message = "Phone Number Does Not Exist..Please Register Before You Can Cash Out Points!!";
		array_push($response,array("success"=>$success,"message"=>$message));
		echo json_encode($response);
	}else{	
		$result = $stmt_merchant->fetchAll(PDO::FETCH_ASSOC);						
		foreach($result as $row)
		{
			$code = $row['merchant_code'];
			$name = $row['merchant_name'];
			$licence_no = $row['merchant_licence_number'];
			$merchant_country = $row['merchant_country'];
		}						

		$sql= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
		$stmt = $db->sth->prepare($sql);
		$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
		$stmt->bindParam(':field_val1', $merchant_country, PDO::PARAM_STR); 
		$stmt->execute();
		$total = $stmt->rowCount();
 		if($total <= 0)
		{
			$amount = 0; 
    			$success = "false";
			$message = "Your Account Balance Is Zero.";
			array_push($response,array("success"=>$success,"message"=>$message));
			echo json_encode($response);
 		}else{
			$success = "true";
			$message = "Account Has A Credit Balance";
			$amount = $stmt->fetchColumn();
			try
			{
				$db->sth->beginTransaction();
				if($amount >= $amount_to_transfer_int)
				{
					$bill_paid_indicator = 'N';
					$date_transfered = date('Y-m-d H:i:s');
					$balance_of_points = $amount - $amount_to_transfer_int;
					if($balance_of_points == 0)
					{
						$sql = "Update tbl_no_of_points Set amount ='$balance_of_points' Where phone_number='$username' AND country = '$merchant_country'";
    	                                                                        	$stmt_update = $db->sth->prepare($sql);
	                                                                            	$stmt_update->execute();
                                                                               	 	$total_update =  $stmt_update->rowCount();
 																									if ($total_update > 0)																		{
						
							$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_merchant_amount_cashed_out (
									merchant_phone_number,
									merchant_code,
									amount_cashed_out,
									amount_cashed_out_date,
									amount_paid_out_date,
									amount_paid_out_indicator,
									media_used_to_cash_out,
									cashed_out_transaction_no,
									amount_cashed_out_indicator,
									amount_processed_by) 
					    		VALUES												(:merchant_phone_number,:merchant_code,:amount_cashed_out,:amount_cashed_out_date,:amount_paid_out_date,:amount_paid_out_indicator,:media_used_to_cash_out,:cashed_out_transaction_no,:amount_cashed_out_indicator,:amount_processed_by)");
					    				$stmt_insert_history->bindParam(':merchant_phone_number', $username);
					    				$stmt_insert_history->bindParam(':merchant_code', $merchant_code);
									$stmt_insert_history->bindParam(':amount_cashed_out', $amount_to_transfer_int);
									$stmt_insert_history->bindParam(':amount_cashed_out_date',$date_transfered);
									$stmt_insert_history->bindParam(':amount_paid_out_date', $date_transfered);
									$stmt_insert_history->bindParam(':amount_paid_out_indicator', $bill_paid_indicator);
									$stmt_insert_history->bindParam(':media_used_to_cash_out', $media);
									$stmt_insert_history->bindParam(':cashed_out_transaction_no', $payment_number);
									$stmt_insert_history->bindParam(':amount_cashed_out_indicator', $points_cashed_out_indicator);
									$stmt_insert_history->bindParam(':amount_processed_by', $points_processed_by);
									$stmt_insert_history->execute();
									$total_insert_history =  $stmt_insert_history->rowCount();														if($total_insert_history > 0)
									{
										$success = "true";
										$balance_of_points_string = strval($balance_of_points);														$message = "Congratulations You Have Successfully Cashed Out " . $amount_to_transfer . " Your Balance Is Now " . $balance_of_points_string;
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									}
															  										}
									                
					}else{
						$date_transfered = date('Y-m-d H:i:s');
						$bill_paid_indicator = 'N';
						//$balance_of_points = $amount - $amount_to_transfer_int;
						$sql = "Update tbl_no_of_points Set amount ='$balance_of_points' Where phone_number='$username' AND country = '$merchant_country'";
    	                                                                        	$stmt_update = $db->sth->prepare($sql);
	                                                                            	$stmt_update->execute();
                                                                               	 	$total_update =  $stmt_update->rowCount();
 																									if ($total_update > 0)																		{
							//Do an insert into tbl_points_transfers
							$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_merchant_amount_cashed_out (
									merchant_phone_number,
									merchant_code,
									amount_cashed_out,
									amount_cashed_out_date,
									amount_paid_out_date,
									amount_paid_out_indicator,
									media_used_to_cash_out,
									cashed_out_transaction_no,
									amount_cashed_out_indicator,
									amount_processed_by) 
					    		VALUES												(:merchant_phone_number,:merchant_code,:amount_cashed_out,:amount_cashed_out_date,:amount_paid_out_date,:amount_paid_out_indicator,:media_used_to_cash_out,:cashed_out_transaction_no,:amount_cashed_out_indicator,:amount_processed_by)");
					    			$stmt_insert_history->bindParam(':merchant_phone_number', $username);
					    			$stmt_insert_history->bindParam(':merchant_code', $merchant_code);
								$stmt_insert_history->bindParam(':amount_cashed_out', $amount_to_transfer_int);
								$stmt_insert_history->bindParam(':amount_cashed_out_date',$date_transfered);
								$stmt_insert_history->bindParam(':amount_paid_out_date', $date_transfered);
								$stmt_insert_history->bindParam(':amount_paid_out_indicator', $bill_paid_indicator);
								$stmt_insert_history->bindParam(':media_used_to_cash_out', $media);
								$stmt_insert_history->bindParam(':cashed_out_transaction_no', $payment_number);
								$stmt_insert_history->bindParam(':amount_cashed_out_indicator', $points_cashed_out_indicator);
								$stmt_insert_history->bindParam(':amount_processed_by', $points_processed_by);
								$stmt_insert_history->execute();
								$total_insert_history =  $stmt_insert_history->rowCount();														if($total_insert_history > 0)
								{
									$success = "true";
									$balance_of_points_string = strval($balance_of_points);														$message = "Congratulations You Have Successfully Cashed Out " . $amount_to_transfer . " Your Balance Is Now " . $balance_of_points_string;
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}
																	  								}
					}
				}else{
					$success = "false";
					$amount_string = strval($amount);
					$message = "You Are Trying To Cash Out More Than What You Have In Your Account. Your Balance Is " .$amount_string;
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
 }else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    echo json_encode($response);
}

	
?>