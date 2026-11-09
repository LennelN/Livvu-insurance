<?php
session_start();
require_once("./include/membersite_config.php");
include("./include/functions.php");

require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$response = array();
$username = trim($_POST["phone"]);
$country = trim($_POST["nameofcountry"]);
$ddate = Date("Y-m-d");
$tariff_total_bill = 0;
$client_amount=0;
$amount_contribution = 0;
$total_balance = 0;
   
					$sql = "SELECT phone_number, name, surname FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1 ORDER BY 1 ASC";
	
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $country, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    							$fname = $row['name'];
							$lname =  $row['surname'];
							$full_name = $fname." ".$lname;
							$temp["full_name"] = $full_name;

							$sql_client_amount= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
							$stmt_client_amount = $db->prepare($sql_client_amount);
							$stmt_client_amount->bindParam(':field_val', $username, PDO::PARAM_STR); 
							$stmt_client_amount->bindParam(':field_val1', $country, PDO::PARAM_STR); 
							$stmt_client_amount->execute();
							$total_client_amount = $stmt_client_amount->rowCount();

							if($total_client_amount > 0)
							{//Dont Have Points	

								$client_amount = $stmt_client_amount->fetchColumn();
								//$temp["no_of_points"] = $no_of_points_client;
							}else{
							
								//$temp["no_of_points"] = 0;
								$client_amount=0;
					
							}

error_log(strip_tags("comingin 25a client amount ".$client_amount), 0);							
							$sql_points= "SELECT phone_number,MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val1 AND purchase_country = :field_val2"; 
							$stmt_points = $db->prepare($sql_points);
							$stmt_points->bindParam(':field_val1', $username, PDO::PARAM_STR); 
							$stmt_points->bindParam(':field_val2', $country, PDO::PARAM_STR);

							$stmt_points->execute();
							$total_points = $stmt_points->rowCount();

							if($total_points > 0) 
							{
								$result_points = $stmt_points->fetchAll(PDO::FETCH_ASSOC); 
    								foreach($result_points as $row)
								{    							
									$client_phone_number_returned=$row['phone_number'];
									$client_due_date_returned=$row['MAX(due_date)'];
								}

								if($ddate > $client_due_date_returned){
									error_log(strip_tags("comingin 25b client due date returned ".$client_due_date_returned), 0);							
									$amount_contribution = 0;
								}else{
									error_log(strip_tags("comingin 25c client due date returned ".$client_due_date_returned), 0);		
									$sql_year= "SELECT phone_number,MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2"; 
									$stmt_year = $db->prepare($sql_year);
									$stmt_year->bindParam(':field_val1', $username, PDO::PARAM_STR); 
									$stmt_year->bindParam(':field_val2', $country, PDO::PARAM_STR);

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
									error_log(strip_tags("comingin 25d client end date returned ".$client_end_date_returned), 0);		
									$sql_balance= "SELECT available_balance FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2 AND end_date = :field_val3"; 
									$stmt_balance = $db->prepare($sql_balance);
									$stmt_balance->bindParam(':field_val1', $username, PDO::PARAM_STR); 
									$stmt_balance->bindParam(':field_val2', $country, PDO::PARAM_STR);
									$stmt_balance->bindParam(':field_val3', $client_end_date_returned, PDO::PARAM_STR);

									$stmt_balance->execute();
									$total_balance = $stmt_balance->rowCount();

									if($total_balance > 0) 
									{
																			error_log(strip_tags("comingin 25b client due date returned ".$client_due_date_returned), 0);		
										$amount_contribution = $stmt_balance->fetchColumn();
										
									}
					
								}								


							}

							error_log(strip_tags("comingin 25at aomunt contrbution".$amount_contribution), 0);
							//$client_amount = floatval($client_amount);
							//$amount_contribution = floatval($amount_contribution);
							$total_balance = $amount_contribution + $client_amount;
							//$total_balance = floatval($total_balance);
							$temp["total_balance"] = $total_balance;

							$sql_request= "SELECT bill_number FROM tbl_billing_requests WHERE phone_number = :field_val AND country =  :field_val1 AND bill_processed = 'N' AND bill_valid = 'Y'"; 
							$stmt_request = $db->prepare($sql_request);
							$stmt_request->bindParam(':field_val', $username, PDO::PARAM_STR); 
							$stmt_request->bindParam(':field_val1', $country, PDO::PARAM_STR); 
							$stmt_request->execute();
							$total_request = $stmt_request->rowCount();
//	error_log(strip_tags("comingin merchant bill name is1  "), 0);
							if($total_request > 0){
			
								$merchant_bill_no = $stmt_request->fetchColumn();
								$temp["bill_no"] = $merchant_bill_no;
								array_push($response, $temp);
								echo json_encode(['response' => true,'ddd' => $response]);
							}else{
								$temp["bill_no"] = 0;
								array_push($response, $temp);
								echo json_encode(['response' => false,'ddd' => $response]);
							}
							
						}

					
									
					}else{

						$temp["full_name"] = "No Name";
						$temp["no_of_points"] = 0;
						$temp["bill_no"] = 0;
						array_push($response, $temp);
						echo json_encode(['response' => false,'ddd' => $response]);

					}
?>