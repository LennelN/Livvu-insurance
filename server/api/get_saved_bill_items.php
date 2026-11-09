<?php

include 'functions.php';
$file_name='get_saved_bill_items';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username'])) {
   require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$username = $_POST["username"];
   
					$sql = "SELECT `transaction_date`, `merchant_doctor`, `bill_number`, `phone_number_billed`, `tariff_description`, `tariff_amount`, `tariff_total_amount`, `tariff_code`, `tariff_code_frequency` FROM `tbl_bill_items_tariff_records` WHERE phone_number_billed = :field_val ORDER BY 1 ASC";
	
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true; 
							
												$transaction_date = $row['transaction_date'];																$timestamp = strtotime($transaction_date); 
												$new_date = date('d/m/Y', $timestamp);
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
												$temp["transaction_date"] = $date_bill_created;  
												$temp["merchant_doctor"] = $merchant_doctor;
												$temp["bill_number"] = $bill_number;  
												$temp["tariff_description"] = $tariff_description;
												$temp["tariff_amount"] = $tariff_amount;  
												$temp["tariff_total_amount"] = $tariff_total_amount;
												$temp["tariff_code"] = $tariff_code;  
												$temp["tariff_code_frequency"] = $tariff_code_frequency;
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												


								}
							echo json_encode($response);	
					}else{
						
								$success = "false";
								$message = "There Are No Current Bill Records.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
					}

}else {
error_log(strip_tags("Error logging in. missing fields ".$file_name), 0);
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}


?>