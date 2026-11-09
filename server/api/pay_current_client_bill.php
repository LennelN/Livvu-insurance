<?php

// array for JSON response
$response = array();
include 'functions.php';
$file_name='pay_current_client_bill';
$payment_details = array();
$contributing_max_number=3;
$contributing_max_reached = false;
//$_POST['merchantCode']='64888';
//$_POST['merchantName']='Mpilo Hospital';
////$_POST['bill_number']='G6984';
//$_POST['phone_no']='0772455025';

if (isset($_POST['merchantCode']) && isset($_POST['merchantName']) && isset($_POST['bill_number']) && isset($_POST['phone_no']) && isset($_POST['total_no_of_points'])){

	$merchant_code = $_POST["merchantCode"];    
	$merchant_name = $_POST["merchantName"];    
	$bill_number = $_POST["bill_number"]; 
	$total_no_of_points =  $_POST["total_no_of_points"];
	$phone_no = $_POST["phone_no"];
	$date_paid = date('Y-m-d H:i:s');
	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$payment_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$payment_number_generated = mt_rand(0, 999999);
	$payment_number=$payment_letter.$payment_number_generated;
	$transaction_type='Payment';
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($phone_no);
	$field_val1 = ($bill_number);

		$sql = "SELECT DISTINCT(bill_number),merchant_phone_number,merchant_code FROM `tbl_bill_items_tariff_records` WHERE phone_number_billed = :field_val ORDER BY 1 ASC";
					
		$stmt = $db->prepare($sql);
		$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 	
		$stmt->execute();
		$total = $stmt->rowCount();
					
		if($total > 0)
		{	
			
			try
				{
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
					foreach($result as $row){   
						$bill_number = $row['bill_number'];
						$merchant_phone_number = $row['merchant_phone_number'];
						$merchant_code = $row['merchant_code'];

					}
				
					//Get the number of points
					$sql_client_points= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
					$stmt_client_points = $db->prepare($sql_client_points);
					$stmt_client_points->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
					$stmt_client_points->execute();
					$total_client_points = $stmt_client_points->rowCount();

					if($total_client_points > 0)
					{//Dont Have Points	

						$no_of_points_client = $stmt_client_points->fetchColumn();
														
						if($no_of_points_client >= $total_no_of_points)
						{//close 1
							$no_of_points_client_balance = $no_of_points_client - $total_no_of_points;
							
							if($no_of_points_client_balance == 0 )
							{
								$sql = "DELETE from tbl_no_of_points Where phone_number='$phone_no'";
    	                                			$stmt_delete = $db->prepare($sql);
	                                    			$stmt_delete->execute();    	                                                               
                                      				$total_delete =  $stmt_delete->rowCount();
                                                
							}
							
							
							if($no_of_points_client_balance > 0)
							{
								$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_balance' Where phone_number='$phone_no'";
    	                                          		$stmt_update = $db->prepare($sql);
	                                              		$stmt_update->execute();
                                                		$total_update =  $stmt_update->rowCount();
							}

							$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
							$stmt_beneficiary = $db->prepare($sql);
							$stmt_beneficiary->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt_beneficiary->execute();
							$total_beneficiary = $stmt_beneficiary->rowCount();
							if($total_beneficiary <= 0)
							{				
								$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
												phone_number,
												no_of_points) 
										VALUES(:phone_number,:no_of_points)");
										$stmt_insert->bindParam(':phone_number', $merchant_phone_number);
										$stmt_insert->bindParam(':no_of_points', $total_no_of_points);
										$stmt_insert->execute();																		$total_insert =  $stmt_insert->rowCount();
							}else{
									$no_of_points_client_beneficiary = $stmt_beneficiary->fetchColumn();
									$no_of_points_client_beneficiary = 													$no_of_points_client_beneficiary + $total_no_of_points;        
				$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_beneficiary' Where phone_number='$merchant_phone_number'";
    	                                          				$stmt_update = $db->prepare($sql);
	                                              				$stmt_update->execute();
                                                				$total_update =  $stmt_update->rowCount();
							}

										//Insert Into tbl_merchant_payment
										$stmt_insert = $db->prepare("INSERT INTO tbl_merchant_bill_payments (
										merchant_phone_number,
										merchant_code,
										phone_number,
										bill_number,
										no_of_points_paid,
										date_paid,
										payment_number,
										transaction_type) 
					VALUES(:merchant_phone_number,:merchant_code,:phone_number,:bill_number,:no_of_points_paid,:date_paid,:payment_number,:transaction_type)");
										$stmt_insert->bindParam(':merchant_phone_number', $merchant_phone_number);
										$stmt_insert->bindParam(':merchant_code', $merchant_code);
										$stmt_insert->bindParam(':phone_number', $phone_no);
										$stmt_insert->bindParam(':bill_number', $bill_number);
										$stmt_insert->bindParam(':no_of_points_paid', $total_no_of_points);
										$stmt_insert->bindParam(':date_paid', $date_paid);
										$stmt_insert->bindParam(':payment_number', $payment_number);
										$stmt_insert->bindParam(':transaction_type', $transaction_type);
										$stmt_insert->execute();
										$total_insert =  $stmt_insert->rowCount();
										if($total_insert > 0)
										{
											$success = "true";
								$message = "You Have Successfully Paid Bill Number ". $bill_number . " ". "For Merchant ". $merchant_name. " ". "Your Balance is ". $no_of_points_client_balance." Points. ";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
																															}							
										

						}else{
								//error_log(strip_tags("in else points are less than total"), 0);
								//error_log(strip_tags($phone_no), 0);
								$no_of_points_client_balance = 0;
								$sql = "DELETE from tbl_no_of_points Where phone_number='$phone_no'";
    	                                          		$stmt_delete = $db->prepare($sql);
	                                              		$stmt_delete->execute();
    	                                                               
                                                		$total_delete =  $stmt_delete->rowCount();
                                                		if($total_delete > 0)//clse delete 
                                                		{
																		
									$total_no_of_points_balance = $total_no_of_points - $no_of_points_client;
										
									$no_of_points_contributed = 0;
									$no_of_points_contributed_other_groups = 0;
				    																										$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
									$stmt_beneficiary = $db->prepare($sql);
									$stmt_beneficiary->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
									$stmt_beneficiary->execute();
									$total_beneficiary = $stmt_beneficiary->rowCount();
									if($total_beneficiary <= 0)
									{				
											//do an insert
										$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
													phone_number,
													no_of_points) 
											VALUES(:phone_number,:no_of_points)");
											$stmt_insert->bindParam(':phone_number', $merchant_phone_number);
											$stmt_insert->bindParam(':no_of_points', $no_of_points_client);
											$stmt_insert->execute();
											$total_insert =  $stmt_insert->rowCount();
									}else{
											$no_of_points_client_beneficiary = $stmt_beneficiary->fetchColumn();
											$no_of_points_client_beneficiary = $no_of_points_client_beneficiary + $no_of_points_client;
		                                                			$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_beneficiary' Where phone_number='$merchant_phone_number'";
		    	                                          				$stmt_update = $db->prepare($sql);
			                                              				$stmt_update->execute();
		    	                                                               		$total_update =  $stmt_update->rowCount();		   											}
				    				}//update 2 after
		    			$stmt_insert = $db->prepare("INSERT INTO tbl_merchant_bill_payments (
											merchant_phone_number,
											merchant_code,
											phone_number,
											bill_number,
											no_of_points_paid,
											date_paid,
											payment_number,
											transaction_type) 
									VALUES					(:merchant_phone_number,:merchant_code,:phone_number,:bill_number,:no_of_points_paid,:date_paid,:payment_number,:transaction_type)");
						$stmt_insert->bindParam(':merchant_phone_number', $merchant_phone_number);
								$stmt_insert->bindParam(':merchant_code', $merchant_code);
								$stmt_insert->bindParam(':phone_number', $phone_no);
								$stmt_insert->bindParam(':bill_number', $bill_number);
								$stmt_insert->bindParam(':no_of_points_paid', $no_of_points_client);
								$stmt_insert->bindParam(':date_paid', $date_paid);
								$stmt_insert->bindParam(':payment_number', $payment_number);
								$stmt_insert->bindParam(':transaction_type', $transaction_type);
								$stmt_insert->execute();
								$total_insert =  $stmt_insert->rowCount();
								if($total_insert > 0)
								{
									$success = "true";
									$message = "You Have Successfully Paid Bill Number ". $bill_number . " ". " For Merchant ". $merchant_name. " ". "Your Balance is ". $no_of_points_client_balance." Points. ";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								}																																								}//update 1 after

			}else{
						$success = "false";
						$message = "You Do Not Have Points To Pay The Bill. Please Purchase Points For You To Be Able To Pay Your Bill";
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
			}//dont have points
$db->commit();
			}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
					{																						$success = "false";													
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
						$error_message=$e->getMessage();
						$error_type='duplicate';
						handler($error_message,$file_name,$success,$error_type);
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
						$db->rollBack();
					}else{		
						$success = "false";
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";												$error_message=$e->getMessage();
						$error_type='exception';
						handler($error_message,$file_name,$success,$error_type);
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
						$db->rollBack();
					}
		}//try

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