
<?php

$response = array();
include 'functions.php';
$file_name='enter_part_society_company_points_purchased';
$purchase_details = array();
//$_POST['merchant_phone_number']='0999999999';
//$_POST['merchant_code']='788899';
//$_POST['transaction_type']='Cash';
//$_POST['transaction_number']='067345';
//$_POST['purchase_amount']='60';
//$_POST['purchase_currency']='ZWL';
//$_POST['purchase_date']='30/07/2020';
//$_POST['array'] = '[{"phone":"0772455025"},{"phone":"0773455025"},{"phone":"088888"}]';
$contributing_max_reached = false;
if (isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code']) && isset($_POST['transaction_type']) && isset($_POST['transaction_number']) && isset($_POST['purchase_amount']) && isset($_POST['array']) && isset($_POST['purchase_by'])){

//error_log(strip_tags("coming in ".$file_name), 0);
$phone_number = trim($_POST["merchant_phone_number"]);
$merchant_code = trim($_POST["merchant_code"]);
$transtype = trim($_POST["transaction_type"]);
$transnum = trim($_POST["transaction_number"]);
//$currency = trim($_POST["purchase_currency"]);
$amount = $_POST["purchase_amount"];
//$dateOfPurchase = $_POST["purchase_date"];
$processedDate = date('Y-m-d H:i:s');
$purchase_by = $_POST["purchase_by"];
$purchase_status = 'Y';
$member_phone_number_does_not_exist = false;
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
	$member_count = 0;
	$receiver_data=$_POST['array'];
	$new_array=json_decode($receiver_data,true);
	$member_count = sizeof($new_array);
$transaction_id_insert=0;

	$sql= "SELECT merchant_phone_number,merchant_code,merchant_country FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

	if($total <= 0) 
	{
		$success = "false";
		$message = "Phone Number ".$phone_number." Does Not Exist In The System. Please Check Again";
		array_push($response,array("success"=>$success,"message"=>$message));
		echo json_encode($response);
	}else{//1

		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		foreach($result as $row)
		{    							
			$client_phone_number_returned=$row['merchant_phone_number'];
			$client_merchant_code_returned=$row['merchant_code'];
			$client_country_name_returned=$row['merchant_country'];
		}

		$sql_year_contributions= "SELECT amount  FROM tbl_year_country_contributions WHERE country = :field_val"; 
		$stmt_year_contributions = $db->prepare($sql_year_contributions);
		$stmt_year_contributions->bindParam(':field_val', $client_country_name_returned, PDO::PARAM_STR);  
		$stmt_year_contributions->execute();
		$total_year_contributions = $stmt_year_contributions->rowCount();

		if($total_year_contributions > 0) {
			$contributionYearAmount=$stmt_year_contributions->fetchColumn();
		}else{
			$contributionYearAmount=0;
		}


		if($member_count == 0) 
		{
			$success = "false";
			$message = "There Are No Members Registered For Phone Number ". $phone_number ." Please Register Your Members In The System.";
			array_push($response,array("success"=>$success,"message"=>$message));
			echo json_encode($response);
		}else{//2

			$ddate = Date("Y-m-d h:i:s");

			try
			{

				$date_joined = date('Y-m-d H:i:s');
				$media='mobile';
				$type='Individual';
				//$currency = $formvars['currency'];
				$date_deactivated = date('Y-m-d H:i:s');
				$qry = "Select amount,type_of_currency from tbl_currency_rates where country_currency = :field_val";
				$stmt = $db->prepare($qry);
				$stmt->bindParam(':field_val', $client_country_name_returned, PDO::PARAM_STR); 
				$stmt->execute();
				$total =  $stmt->rowCount();

				if($total <= 0)
				{
					$success = "false";
					$message = "Type Of Currency Does Not Exist...Please Enter Type Of Currency!";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);	
				}else{

					$amount_per_member = $amount / $member_count;
					//$amount_per_point = $stmt->fetchColumn();
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
					foreach($result as $row)
					{    							
						$amount_per_point=$row['amount'];
						$currency=$row['type_of_currency'];
					}

					//$no_of_points_purchased = $amount_per_member / $amount_per_point;
					$no_of_points_purchased = intdiv($amount_per_member , $amount_per_point);
					$dt   = new DateTime();
					//$date = $dt->createFromFormat('d/m/Y', $dateOfPurchase);
					//$newDate = $date->format('Y-m-d');

					$newDate = date('Y-m-d H:i:s');
					foreach($new_array as $row)
					{
											
						$phone = $row['sPhoneNo'];
						$member_phone_country_array = $row['sCountry'];
						$sql = "SELECT member_phone_number,member_phone_country FROM tbl_society_company_members where merchant_phone_number = :field_val AND member_phone_number = :field_val1 AND member_phone_country = :field_val2";
						$stmt = $db->prepare($sql);
						$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR);  
						$stmt->bindParam(':field_val1', $phone, PDO::PARAM_STR);  
						$stmt->bindParam(':field_val2', $member_phone_country_array, PDO::PARAM_STR);  
						$stmt->execute();
						$total = $stmt->rowCount();
						if($total > 0)
						{
							//$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

							$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
							
							foreach($result as $row)
							{
								$member_phone_number = $row['member_phone_number'];
								$member_phone_country  = $row['member_phone_country'];
							}
							//$member_phone_number = $stmt->fetchColumn(); 
		
							$sql_client_up_to_date= "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val AND purchase_country = :field_val1";
							$stmt_client_up_to_date = $db->prepare($sql_client_up_to_date);
							$stmt_client_up_to_date->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR); 
							$stmt_client_up_to_date->bindParam(':field_val1', $member_phone_country, PDO::PARAM_STR); 
							$stmt_client_up_to_date->execute();
							$total_client_up_to_date = $stmt_client_up_to_date->rowCount();

							if($total_client_up_to_date > 0)
							{
								$client_due_date=$stmt_client_up_to_date->fetchColumn();
								$dt   = new DateTime();
								for($i=0 ; $i<$no_of_points_purchased;$i++)
								{
					
									if($i==0)
									{
							
   										$dueDate = date('Y-m-d',strtotime($client_due_date.'+30days'));
									}else{
										//error_log(strip_tags("now entering 1i is not zero ".$i), 0); 
										$dueDate = date('Y-m-d',strtotime($dueDate.'+30days'));
									}
								}
							}else{
								for($i=0 ; $i<$no_of_points_purchased;$i++)
								{
			
									if($i==0)
									{
						
   						 				$dueDate = date('Y-m-d',strtotime($newDate.'+30days'));
									}else{
							
										$dueDate = date('Y-m-d',strtotime($dueDate.'+30days'));
									}
								}
							}
									
								
								$stmt = $db->prepare("INSERT INTO tbl_purchase_points_details (
										transaction_id,
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
    									VALUES 										(:transaction_id,:phone_number,:transaction_type,:type_of_currency,:purchase_amount,:no_of_points_purchased,:date_of_purchase,:time_of_purchase,:transaction_number,:media_used,		:due_date,:processed_date,:processed_by,:purchase_status,:purchase_done_by,:purchase_country)");
									$stmt->bindParam(':transaction_id', $transaction_id_insert);
    									$stmt->bindParam(':phone_number', $member_phone_number);
									$stmt->bindParam(':transaction_type', $transtype);
									$stmt->bindParam(':type_of_currency', $currency);
									$stmt->bindParam(':purchase_amount', $amount_per_member);
									$stmt->bindParam(':no_of_points_purchased', $no_of_points_purchased);
    									$stmt->bindParam(':date_of_purchase', $newDate);
									$stmt->bindParam(':time_of_purchase', $ddate);
									$stmt->bindParam(':transaction_number', $transnum);
									$stmt->bindParam(':media_used', $media);
									$stmt->bindParam(':due_date', $dueDate);
									$stmt->bindParam(':processed_date', $processedDate);
									$stmt->bindParam(':processed_by', $phone_number);
									$stmt->bindParam(':purchase_status', $purchase_status);
									$stmt->bindParam(':purchase_done_by', $purchase_by);
									$stmt->bindParam(':purchase_country', $member_phone_country);
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$sql= "SELECT no_of_points,amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
									$stmt_beneficiary = $db->prepare($sql);
									$stmt_beneficiary->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR); 
									$stmt_beneficiary->bindParam(':field_val1', $member_phone_country, PDO::PARAM_STR); 
									$stmt_beneficiary->execute();
									$total_beneficiary = $stmt_beneficiary->rowCount();
									if($total_beneficiary <= 0)
									{	
		
										$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
												phone_number,
												no_of_points,
												amount,country) 
											VALUES(:phone_number,:no_of_points,:amount,:country)");
										$stmt_insert->bindParam(':phone_number', $member_phone_number);
											$stmt_insert->bindParam(':no_of_points', $no_of_points_purchased);
											$stmt_insert->bindParam(':amount', $amount_per_member);
											$stmt_insert->bindParam(':country', $member_phone_country);
											$stmt_insert->execute();
											$total_insert =  $stmt_insert->rowCount();
									}else{

										$result_beneficiary = $stmt_beneficiary->fetchAll(PDO::FETCH_ASSOC); 
    										foreach($result_beneficiary as $row)
										{    							
											$no_of_points_client_beneficiary=$row['no_of_points'];
											$amount_client_beneficiary=$row['amount'];
										}
										//$no_of_points_client_beneficiary = $stmt_beneficiary->fetchColumn();
										$no_of_points_client_beneficiary = $no_of_points_client_beneficiary + $no_of_points_purchased;
										$amount_client_beneficiary = $amount_client_beneficiary + $amount_per_member;
		                                                						$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_beneficiary', amount = '$amount_client_beneficiary' Where phone_number='$member_phone_number' AND country ='$member_phone_country'";
		    	                                          					$stmt_update = $db->prepare($sql);
			                                              					$stmt_update->execute();
		    	                                                               				$total_update =  $stmt_update->rowCount();		   												}

									$sql_year= "SELECT no_of_points,amount,MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val AND country = :field_val1"; 
									$stmt_year_contributions = $db->prepare($sql_year);
									$stmt_year_contributions->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR); 
									$stmt_year_contributions->bindParam(':field_val1', $member_phone_country, PDO::PARAM_STR); 
									$stmt_year_contributions->execute();
									$total_year_contributions = $stmt_year_contributions->rowCount();
									if($total_year_contributions <= 0)
									{
										$todayDate = date('Y-m-d');
										$endDate = date('Y-m-d',strtotime($todayDate.'+365days'));
										$stmt_insert_year = $db->prepare("INSERT INTO tbl_year_contributions (
												phone_number,
												no_of_points,
												amount,available_balance,country,start_date,end_date) 
										VALUES									(:phone_number,:no_of_points,:amount,:available_balance,:country,:start_date,:end_date)");
										$stmt_insert_year->bindParam(':phone_number', $member_phone_number);
										$stmt_insert_year->bindParam(':no_of_points', $no_of_points_purchased);
										$stmt_insert_year->bindParam(':amount', $contributionYearAmount);
										$stmt_insert_year->bindParam(':available_balance', $contributionYearAmount);
										$stmt_insert_year->bindParam(':country', $member_phone_country);
										$stmt_insert_year->bindParam(':start_date', $todayDate);
										$stmt_insert_year->bindParam(':end_date', $endDate);
										$stmt_insert_year->execute();
										$total_insert_year =  $stmt_insert_year->rowCount();
									}else{

										$todayDate = date('Y-m-d');
										$result_year_contributions = $stmt_year_contributions->fetchAll(PDO::FETCH_ASSOC); 
    										foreach($result_year_contributions as $row)
										{    							
											$points_stmt_year_contributions=$row['no_of_points'];
											$amount_stmt_year_contributions=$row['amount'];
											$end_stmt_year_contributions=$row['MAX(end_date)'];
										}

										if($todayDate > $end_stmt_year_contributions)
										{
											$endDate = date('Y-m-d',strtotime($todayDate.'+365days'));
											$stmt_insert_year = $db->prepare("INSERT INTO tbl_year_contributions (
													phone_number,
													no_of_points,
													amount,available_balance,country,start_date,end_date) 
											VALUES																			(:phone_number,:no_of_points,:amount,:available_balance,:country,:start_date,:end_date)");
											$stmt_insert_year->bindParam(':phone_number', $member_phone_number);
											$stmt_insert_year->bindParam(':no_of_points', $no_of_points_purchased);
											$stmt_insert_year->bindParam(':amount', $contributionYearAmount);
											$stmt_insert_year->bindParam(':available_balance', $contributionYearAmount);
											$stmt_insert_year->bindParam(':country', $member_phone_country);
											$stmt_insert_year->bindParam(':start_date', $todayDate);
											$stmt_insert_year->bindParam(':end_date', $endDate);
											$stmt_insert_year->execute();
											$total_insert_year =  $stmt_insert_year->rowCount();
										}
				    						
									}
								}
																				
							}else{
								$member_phone_number_does_not_exist="true";
								break;
							}//total>0
						}//foreach

					if($member_phone_number_does_not_exist==true)
					{
																									$success = "false";
						$message = "Phone Number ".$phone. " Is Not Registered As Your Member. Please Register And Try Again.";
						array_push($response,array("success"=>$success,"message"=>$message));
						$db->rollBack();
						echo json_encode($response);
					}else{
											
						$success = "true";
						$message = "Congratulations You Have Successfully Paid Your Subscriptions";
						array_push($response,array("success"=>$success,"message"=>$message));
						$db->commit();
						echo json_encode($response);	
					}
								

			}//currency	
									
			}catch(PDOException $e){
				if ($e->errorInfo[1] == 1062) 
									{
							
										$error_message=$e->getMessage();
										$success = "false";
										$message = "An Error Occurred Processing Transaction. Please Try Again Later";
										$error_message=$e->getMessage();
										$error_type='duplicate';
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
										$db->rollBack();
						
									}else{	
	
										$success = "false";
										$message = "An Error Occurred Processing Transaction. Please Try Again Later";
										$message=$e->getMessage();
										//error_log(strip_tags($message), 0);
										$error_type='exception';
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
										$db->rollBack();
							
									}
			}//catch
		}//2
	}//1
} else {
    // required field is missing

    	$success = "false";
    	$message = "Required field(s) Are Missing...Please Complete All Fields";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    	echo json_encode($response);
}


?>