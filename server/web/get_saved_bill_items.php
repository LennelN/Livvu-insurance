<?php

require_once("./include/membersite_config.php");
include("./include/functions.php");

require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$response = array();
$username = trim($_POST["phone"]);

$tariff_total_bill = 0;

   
					$sql = "SELECT `transaction_date`, `merchant_doctor`, `bill_number`, `phone_number_billed`, `tariff_description`, `tariff_amount`, `tariff_total_amount`, `tariff_code`, `tariff_code_frequency` FROM `tbl_bill_items_tariff_records` WHERE phone_number_billed = :field_val ORDER BY 1 ASC";
	
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;

												//$temp["response"] = true; 
							
												$transaction_date = $row['transaction_date'];																$timestamp = strtotime($transaction_date); 
												$new_date = date('d-m-Y', $timestamp);
												$dt   = new DateTime();
												$date_bill_created = $new_date;
												$merchant_doctor = $row['merchant_doctor'];
												$bill_number = $row['bill_number'];
												$phone_number_billed = $row['phone_number_billed'];
												$tariff_description = $row['tariff_description'];
												$tariff_amount = $row['tariff_amount'];
												$tariff_total_amount = $row['tariff_total_amount'];
												$tariff_code = $row['tariff_code'];
												$tariff_code_frequency = $row['tariff_code_frequency'];
												$tariff_total_bill = $tariff_total_bill + $tariff_total_amount;
												$temp["transaction_date"] = $date_bill_created;  
												$temp["merchant_doctor"] = $merchant_doctor;
												$temp["bill_number"] = $bill_number;  
												$temp["tariff_description"] = $tariff_description;
												$temp["tariff_amount"] = $tariff_amount;  
												$temp["tariff_total_amount"] = $tariff_total_amount;
												$temp["tariff_code"] = $tariff_code;  
												$temp["tariff_code_frequency"] = $tariff_code_frequency;
												$temp["tariff_total_bill"] = $tariff_total_bill;
												array_push($response, $temp);
								
								}

					
					echo json_encode(['response' => true,'ddd' => $response]);			
					}
?>