<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$payment_details = array();
$contributing_max_number=4;
$contributing_max_reached = false;
$response = array();
$file_name = "pay_bill";
$transaction_id_insert = 0;
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

			if($main_menu=="main_livvu")
			{
				$fgmembersite->RedirectToURL("main_livvu.php");
			}else if ($main_menu == "main_admin_accounts"){
				$fgmembersite->RedirectToURL("main_admin_accounts.php");
			}else{
				$fgmembersite->RedirectToURL("main_admin.php");
			}
	}else{

		$fgmembersite->RedirectToURL("logout.php");
	}

}else{

	$transaction_id_insert = 0;
	$bill_number = ($_SESSION['bill_number']);
	$merchant_code = ($_SESSION['merchant_code']);
	$merchant_name = ($_SESSION['merchant_name']);
	//$phone_no = ($_SESSION["user_name"]);

	$ddate= date('Y-m-d');
	$date_paid = date('Y-m-d H:i:s');
	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$payment_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$payment_number_generated = mt_rand(0, 999999);
	$payment_number=$payment_letter.$payment_number_generated;
	$transaction_type='Payment';
	$total_amount_paid=0;
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	//$field_val = ($phone_no);
	//$field_val1 = ($bill_number);

	$sql= "SELECT merchant_phone_number,merchant_name,total_bill_amount,bill_amount,country,phone_number,merchant_country FROM tbl_bill_client_records WHERE bill_number = :field_val1 AND bill_paid_indicator = 'N'"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val1', $bill_number, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

	 // If the SQL query is succesfully performed ($result not false)
  	if($total > 0) 
	{
			
		try
		{
			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
			foreach($result as $row){
				$merchant_phone_number = $row['merchant_phone_number'];  
        				$merchant_name = $row['merchant_name'];
				$total_bill_amount = $row['total_bill_amount'];
				$total_no_of_points= $row['bill_amount'];
				$client_country= $row['country'];
				$phone_no= $row['phone_number'];
				$merchant_country= $row['merchant_country'];
			}	
			//Get the number of points
			$sql_client_amount= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
			$stmt_client_amount = $db->prepare($sql_client_amount);
			$stmt_client_amount->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
			$stmt_client_amount->bindParam(':field_val1', $client_country, PDO::PARAM_STR); 
			$stmt_client_amount->execute();
			$total_client_amount = $stmt_client_amount->rowCount();

			if($total_client_amount > 0)
			{//Dont Have Points	

				$amount_client = $stmt_client_amount->fetchColumn();
	
				if($amount_client >= $total_bill_amount)
				{//close 1
					
					$amount_client_balance = $amount_client - $total_bill_amount;
		
					$sql = "UPDATE tbl_bill_client_records SET total_bill_amount = 0,bill_paid_indicator = 'Y' WHERE bill_number = :field_val AND phone_number = :field_val1";
																// Prepare statement
    					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $phone_no, PDO::PARAM_STR); 
					// execute the query
    					$stmt->execute();
    					$total_update =  $stmt->rowCount();
    														
    					if($total_update > 0)//update done 0
					{
							
						$sql = "UPDATE tbl_bill_merchant_records SET total_bill_amount = 0,bill_paid_indicator = 'Y' WHERE bill_number = :field_val AND merchant_phone_number = :field_val1";
						// Prepare statement
						$stmt = $db->prepare($sql);
						$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
						$stmt->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
						// execute the query
    						$stmt->execute();
						$total_update =  $stmt->rowCount();
    														
						if($total_update > 0)//update done 1
						{    

							if($amount_client_balance > 0)
							{
								$sql = "Update tbl_no_of_points Set amount ='$amount_client_balance' Where phone_number='$phone_no'  AND country = '$client_country'";
                     							$stmt_update = $db->prepare($sql);
                                              						$stmt_update->execute();
                                                						$total_update =  $stmt_update->rowCount();
							}

							$sql= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
							$stmt_beneficiary = $db->prepare($sql);
							$stmt_beneficiary->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt_beneficiary->bindParam(':field_val1', $merchant_country, PDO::PARAM_STR); 
							$stmt_beneficiary->execute();
							$total_beneficiary = $stmt_beneficiary->rowCount();
							if($total_beneficiary <= 0)
							{				
								$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
									phone_number,
									no_of_points,
									amount,
									country) 
								VALUES(:phone_number,:no_of_points,:amount,:country)");
									$stmt_insert->bindParam(':phone_number', $merchant_phone_number);
									$stmt_insert->bindParam(':no_of_points', $total_bill_amount);
									$stmt_insert->bindParam(':amount', $total_bill_amount);
									$stmt_insert->bindParam(':country', $merchant_country);
									$stmt_insert->execute();																	$total_insert =  $stmt_insert->rowCount();
							}else{
								$no_of_points_client_beneficiary = $stmt_beneficiary->fetchColumn();
								$no_of_points_client_beneficiary = 																$no_of_points_client_beneficiary + $total_bill_amount;        
								$sql = "Update tbl_no_of_points Set amount ='$no_of_points_client_beneficiary' Where phone_number='$merchant_phone_number' AND country = '$merchant_country'";
    	                                          					$stmt_update = $db->prepare($sql);
	                                              					$stmt_update->execute();
                                                						$total_update =  $stmt_update->rowCount();
							  }

							//Insert Into tbl_merchant_payment
							$stmt_insert = $db->prepare("INSERT INTO tbl_merchant_bill_payments (
									transaction_id,
									merchant_phone_number,
									merchant_code,
									phone_number,
									country,
									bill_number,
									amount_paid,
									date_paid,
									payment_number,
									transaction_type) 
								VALUES(:transaction_id,:merchant_phone_number,:merchant_code,:phone_number,:country,:bill_number,:amount_paid,:date_paid,:payment_number,:transaction_type)");
									$stmt_insert->bindParam(':transaction_id', $transaction_id_insert);
									$stmt_insert->bindParam(':merchant_phone_number', $merchant_phone_number);	
									$stmt_insert->bindParam(':merchant_code', $merchant_code);
									$stmt_insert->bindParam(':phone_number', $phone_no);
									$stmt_insert->bindParam(':country', $client_country);
									$stmt_insert->bindParam(':bill_number', $bill_number);
									$stmt_insert->bindParam(':amount_paid', $total_bill_amount);
									$stmt_insert->bindParam(':date_paid', $date_paid);
									$stmt_insert->bindParam(':payment_number', $payment_number);
									$stmt_insert->bindParam(':transaction_type', $transaction_type);
									$stmt_insert->execute();
									$total_insert =  $stmt_insert->rowCount();
									if($total_insert > 0)
									{
										$success = "true";
										$message = "You Have Successfully Paid Bill Number ". $bill_number . " ". "For Merchant ". $merchant_name;
										//array_push($response,array("success"=>$success,"message"=>$message));
										//echo json_encode($message);
																												}							
						}//update done 1

					}//update done 0


				}else{//close1
					
					if($amount_client  > 0)
					{
						
						$amount_client_balance = $total_bill_amount - $amount_client;
						
						$total_amount_paid=$total_amount_paid + $amount_client;
						
						$stmt_insert = $db->prepare("INSERT INTO tbl_merchant_bill_payments (
									transaction_id,
									merchant_phone_number,
									merchant_code,
									phone_number,
									country,
									bill_number,
									amount_paid,
									date_paid,
									payment_number,
									transaction_type) 
								VALUES(:transaction_id,:merchant_phone_number,:merchant_code,:phone_number,:country,:bill_number,:amount_paid,:date_paid,:payment_number,:transaction_type)");
									$stmt_insert->bindParam(':transaction_id', $transaction_id_insert);
									$stmt_insert->bindParam(':merchant_phone_number', $merchant_phone_number);	
									$stmt_insert->bindParam(':merchant_code', $merchant_code);
									$stmt_insert->bindParam(':phone_number', $phone_no);
									$stmt_insert->bindParam(':country', $client_country);
									$stmt_insert->bindParam(':bill_number', $bill_number);
									$stmt_insert->bindParam(':amount_paid', $amount_client);
									$stmt_insert->bindParam(':date_paid', $date_paid);
									$stmt_insert->bindParam(':payment_number', $payment_number);
									$stmt_insert->bindParam(':transaction_type', $transaction_type);
									$stmt_insert->execute();
									$total_insert =  $stmt_insert->rowCount();
									if($total_insert > 0)
									{
					
										$amount_client=0;
										$sql = "Update tbl_no_of_points Set amount ='$amount_client' Where phone_number='$phone_no' AND country = '$client_country'";
                     									$stmt_update = $db->prepare($sql);
                                              								$stmt_update->execute();
                                                								$total_update =  $stmt_update->rowCount();
										
									}
					}else{
						$amount_client_balance = $total_bill_amount - $amount_client;
					
					}

					if($amount_client_balance > 0)
					{
						//get the available balance
						$sql_year= "SELECT phone_number,MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2"; 
						$stmt_year = $db->prepare($sql_year);
						$stmt_year->bindParam(':field_val1', $phone_no, PDO::PARAM_STR); 
						$stmt_year->bindParam(':field_val2', $client_country, PDO::PARAM_STR);

						$stmt_year->execute();
						$total_year = $stmt_year->rowCount();

						if($total_year > 0) 
						{
							$result_year = $stmt_year->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result_year as $row)
							{    							
								$client_phone_number_returned=$row['phone_number'];
								$client_end_date_returned=$row['MAX(end_date)'];
							}
						}
						
						$sql_balance= "SELECT phone_number,available_balance FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2 AND end_date = :field_val3"; 
						$stmt_balance = $db->prepare($sql_balance);
						$stmt_balance->bindParam(':field_val1', $phone_no, PDO::PARAM_STR); 
						$stmt_balance->bindParam(':field_val2', $client_country, PDO::PARAM_STR);
						$stmt_balance->bindParam(':field_val3', $client_end_date_returned, PDO::PARAM_STR);

						$stmt_balance->execute();
						$total_balance = $stmt_balance->rowCount();

						if($total_balance > 0) 
						{
							$result_balance = $stmt_balance->fetchAll(PDO::FETCH_ASSOC); 
   					 		foreach($result_balance as $row)
							{    							
								$client_phone_number_returned=$row['phone_number'];
								$client_balance_returned=$row['available_balance'];
							}
						}

						if($client_balance_returned > 0)
						{

							//Get the country contributing amount

							$qry_currency = "Select amount,type_of_currency from tbl_currency_rates where country_currency = :field_val";
							$stmt_currency = $db->prepare($qry_currency);
							$stmt_currency->bindParam(':field_val', $client_country, PDO::PARAM_STR); 
							$stmt_currency->execute();
							$total_currency =  $stmt_currency->rowCount();

							 if($total_currency > 0)
							{
								
								$result_currency = $stmt_currency->fetchAll(PDO::FETCH_ASSOC); 
    								foreach($result_currency as $row)
								{    
															
									$amount_per_point=$row['amount'];
									$currency=$row['type_of_currency'];
								}
							}

							$sql_contribute = "SELECT phone_number,amount FROM tbl_no_of_points WHERE phone_number != :field_val AND country = :field_val1 AND amount > :field_val2"; 
							$stmt_contribute = $db->prepare($sql_contribute);
							$stmt_contribute->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
							$stmt_contribute->bindParam(':field_val1', $client_country, PDO::PARAM_STR); 
							$stmt_contribute->bindParam(':field_val2', $amount_per_point, PDO::PARAM_STR); 
							$stmt_contribute->execute();
							$total_contribute = $stmt_contribute->rowCount();
		
							if($total_contribute > 0)
							{
								
								$result_contribute = $stmt_contribute->fetchAll(PDO::FETCH_ASSOC); 
    								foreach($result_contribute as $row)
								{    							
									$phone_number_to_contribute=$row['phone_number'];
									$amount_to_contribute=$row['amount'];																//check whether the client is up to date

									$sql_due_date= "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val1 AND purchase_country = :field_val2"; 
									$stmt_due_date = $db->prepare($sql_due_date);
									$stmt_due_date->bindParam(':field_val1', $phone_number_to_contribute, PDO::PARAM_STR); 
									$stmt_due_date->bindParam(':field_val2', $client_country, PDO::PARAM_STR);

									$stmt_due_date->execute();
									$total_due_date = $stmt_due_date->rowCount();

									if($total_due_date > 0) 
									{
										$client_due_date_returned = $stmt_due_date->fetchColumn();
											
										if($client_due_date_returned > $ddate)
										{
											
											if($amount_client_balance > $amount_per_point)
											{ 
												$client_balance_returned = $client_balance_returned - $amount_per_point;	
	
												$amount_client_balance = $amount_client_balance - $amount_per_point;																		
											
												$amount_to_contribute = $amount_to_contribute - $amount_per_point;
												$sql_update_phone = "Update tbl_no_of_points Set amount='$amount_to_contribute' Where phone_number='$phone_number_to_contribute' AND country = '$client_country'";							  													$stmt_update_phone = $db->prepare($sql_update_phone);
												$stmt_update_phone->execute();	
												$total_update_phone =  $stmt_update_phone->rowCount();
												if($total_update_phone > 0)
												{
													$total_amount_paid=$total_amount_paid + $amount_per_point;
													$temp["transaction_id"] = $transaction_id_insert;
													$temp["merchant_phone_number"]=$merchant_phone_number;
													$temp["merchant_code"]=$merchant_code;	
													$temp["phone_number"]=$phone_number_to_contribute;	
													$temp["country"]=$client_country;
													$temp["bill_number"]=$bill_number;
													$temp["amount_paid"]=$amount_per_point;
													$temp["date_paid"]=$date_paid;
													$temp["payment_number"]=$payment_number;	
													$temp["transaction_type"]=$transaction_type;
													array_push($payment_details, $temp);
				
												}
					
											}else{

												$client_balance_returned = $client_balance_returned - $$amount_client_balance;

												$amount_to_contribute = $amount_to_contribute - $amount_client_balance;
												$sql_update_phone = "Update tbl_no_of_points Set amount='$amount_to_contribute' Where phone_number='$phone_number_to_contribute' AND country = '$client_country'";							  													$stmt_update_phone = $db->prepare($sql_update_phone);
												$stmt_update_phone->execute();	
												$total_update_phone =  $stmt_update_phone->rowCount();
												if($total_update_phone > 0)
												{
													$total_amount_paid=$total_amount_paid + $amount_client_balance;
													$temp["transaction_id"] = $transaction_id_insert;
													$temp["merchant_phone_number"]=$merchant_phone_number;
													$temp["merchant_code"]=$merchant_code;	
													$temp["phone_number"]=$phone_number_to_contribute;	
													$temp["country"]=$client_country;
													$temp["bill_number"]=$bill_number;
													$temp["amount_paid"]=$amount_client_balance;
													$temp["date_paid"]=$date_paid;
													$temp["payment_number"]=$payment_number;	
													$temp["transaction_type"]=$transaction_type;
													array_push($payment_details, $temp);
				
												}
												$amount_client_balance = 0;
											}
				
											if($amount_client_balance == 0)
											{
												break;
											}
				
										}
									}
								}
							
							}
						}
					}							
							

						if( sizeof($payment_details) != 0 ) 
						{

							$stmt_insert_other = "INSERT INTO tbl_merchant_bill_payments (transaction_id,merchant_phone_number,merchant_code,phone_number,country,bill_number,amount_paid,date_paid,payment_number,transaction_type) VALUES ";
							$paramArray = array();

							$sqlArray = array();

							foreach($payment_details as $row)
							{
    							$sqlArray[] = '(' . implode(',', array_fill(0, count($row), '?')) . ')';

    								foreach($row as $element)
    								{
    
        								$paramArray[] = $element;
    								}
							}			

							$stmt_insert_other .= implode(',', $sqlArray);

							$stmt = $db->prepare($stmt_insert_other);

							$stmt->execute($paramArray);
							$total =  $stmt->rowCount();

						}
						
//
		

                                                	if($amount_client_balance == 0)
                                                	{                                                
                                                		$sql = "UPDATE tbl_bill_client_records SET total_bill_amount = 0,bill_paid_indicator = 'Y' WHERE bill_number = :field_val AND phone_number = :field_val1";
                                                	}else{
                                                		$sql = "UPDATE tbl_bill_client_records SET total_bill_amount = '$amount_client_balance' WHERE bill_number = :field_val AND phone_number = :field_val1";
                                                	}	
			
			$stmt = $db->prepare($sql);
			$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
			$stmt->bindParam(':field_val1', $phone_no, PDO::PARAM_STR); 
							// execute the query
			$stmt->execute();
			$total_update =  $stmt->rowCount();
	
			if($total_update > 0 )//update 1 after
			{
				
				$total_number_of_points_paid = 0;
				//update tbl_bill_merchant_records
				if($amount_client_balance == 0)
                                                		{
					
					$sql_2 = "UPDATE tbl_bill_merchant_records SET total_bill_amount = 0,bill_paid_indicator = 'Y' WHERE bill_number = :field_val AND merchant_phone_number = :field_val1";
				}else{
					
					$sql_2 = "UPDATE tbl_bill_merchant_records SET total_bill_amount = '$amount_client_balance' WHERE bill_number = :field_val AND merchant_phone_number = :field_val1";
				}	    																// Prepare statement
				$stmt_2 = $db->prepare($sql_2);
				$stmt_2->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
				$stmt_2->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
				// execute the query
				$stmt_2->execute();
				$total_update_2 =  $stmt_2->rowCount();
	
				if($total_update_2 > 0)//update 2 after
				{
					
						$sql_update_contributions = "UPDATE tbl_year_contributions SET available_balance = '$client_balance_returned' WHERE  phone_number = :field_val1 AND country = :field_val2 AND end_date = :field_val3";
						$stmt_update_contributions = $db->prepare($sql_update_contributions);
						$stmt_update_contributions->bindParam(':field_val1', $phone_no, PDO::PARAM_STR); 
						$stmt_update_contributions->bindParam(':field_val2', $client_country, PDO::PARAM_STR);
						$stmt_update_contributions->bindParam(':field_val3', $client_end_date_returned, PDO::PARAM_STR);

						$stmt_update_contributions->execute();
						$total_update_contributions = $stmt_update_contributions->rowCount();
	
						if($total_update_contributions > 0) 
						{

							//do the merchant update

							$sql_merchant= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
							$stmt_beneficiary = $db->prepare($sql_merchant);
							$stmt_beneficiary->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt_beneficiary->bindParam(':field_val1', $merchant_country, PDO::PARAM_STR); 
							$stmt_beneficiary->execute();
							$total_beneficiary = $stmt_beneficiary->rowCount();
							if($total_beneficiary <= 0)
							{			
								//do an insert
								$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
										phone_number,
										no_of_points,
										amount,
										country) 
									VALUES(:phone_number,:no_of_points,:amount,:country)");
								$stmt_insert->bindParam(':phone_number', $merchant_phone_number);
								$stmt_insert->bindParam(':no_of_points', $total_number_of_points_paid);
								$stmt_insert->bindParam(':amount', $total_amount_paid);
								$stmt_insert->bindParam(':country', $merchant_country);
								$stmt_insert->execute();
								$total_insert =  $stmt_insert->rowCount();
							}else{
								$amount_client_beneficiary = $stmt_beneficiary->fetchColumn();
								$amount_client_beneficiary = $amount_client_beneficiary + $total_amount_paid;
		                                    				$sql_update_merchant = "Update tbl_no_of_points Set amount ='$amount_client_beneficiary' Where phone_number='$merchant_phone_number' AND country = '$merchant_country'";
						    	                  $stmt_update_merchant = $db->prepare($sql_update_merchant);
			                  				$stmt_update_merchant->execute();
						    	                  $total_update_merchant =  $stmt_update_merchant->rowCount();
							}
																										$success = "true";
							$message = "You Have Successfully Paid Bill Number ". $bill_number . " ". " For Merchant ". $merchant_name;
							//array_push($response,array("success"=>$success,"message"=>$message));
							//echo json_encode($message);
						}else{//total update 2																		
							
							$sql_merchant= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
							$stmt_beneficiary = $db->prepare($sql_merchant);
							$stmt_beneficiary->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt_beneficiary->bindParam(':field_val1', $merchant_country, PDO::PARAM_STR); 
							$stmt_beneficiary->execute();
							$total_beneficiary = $stmt_beneficiary->rowCount();
							if($total_beneficiary <= 0)
							{			
								//do an insert
								$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
										phone_number,
										no_of_points,
										amount,
										country) 
									VALUES(:phone_number,:no_of_points,:amount,:country)");
								$stmt_insert->bindParam(':phone_number', $merchant_phone_number);
								$stmt_insert->bindParam(':no_of_points', $total_number_of_points_paid);
								$stmt_insert->bindParam(':amount', $total_amount_paid);
								$stmt_insert->bindParam(':country', $merchant_country);
								$stmt_insert->execute();
								$total_insert =  $stmt_insert->rowCount();
							}else{
								$amount_client_beneficiary = $stmt_beneficiary->fetchColumn();
								$amount_client_beneficiary = $amount_client_beneficiary + $total_amount_paid;
		                                    				$sql_update_merchant = "Update tbl_no_of_points Set amount ='$amount_client_beneficiary' Where phone_number='$merchant_phone_number' AND country = '$merchant_country'";
						    	                  $stmt_update_merchant = $db->prepare($sql_update_merchant);
			                  				$stmt_update_merchant->execute();
						    	                  $total_update_merchant =  $stmt_update_merchant->rowCount();
							}
																										$success = "true";
							$message = "You Have Successfully Paid Bill Number ". $bill_number . " ". " For Merchant ". $merchant_name;
							//array_push($response,array("success"=>$success,"message"=>$message));
							//echo json_encode($message);
		
						}
				}
			
			}else{
				
				$success = "false";
				$message = "Payment For Bill Number ". $bill_number . " ". " For Merchant ". $merchant_name." Not Successful";
				//array_push($response,array("success"=>$success,"message"=>$message));
				//echo json_encode($message);
			}//update 1 after
                                	
		}//close 1

		}else{
			$success = "false";
			$message = "There Is Not Enough To Pay This Bill";
			//array_push($response,array("success"=>$success,"message"=>$message));
			//echo json_encode($message);
		}//dont have points
		$db->commit();
		}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
					{																						$success = "false";													
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
						$error_message=$e->getMessage();
						$error_type='duplicate';
						
						handler($error_message,$file_name,$success,$error_type);
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($message);
						$db->rollBack();
					}else{		
						$success = "false";
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";												$error_message=$e->getMessage();
						$error_type='exception';
						
						handler($error_message,$file_name,$success,$error_type);
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($message);
						$db->rollBack();
					}
		}//try

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

</head>
<body>

<div id="main-wrap">
	<div id="page-wrap">
	<div>
		<?php include("./include/declaration.php");?>
	</div>	
<div class="row">
		<div class="col-xs-12 text-center">    

			<form id='confirm' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset disabled>

				<legend>Payment Result</legend>
				<br/>
				<div class="col-xs-12 text-center">
    					<label><?php echo $message ?> </label><br/>
					<br/>
				</div>

			</fieldset>

			<fieldset >
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				
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


