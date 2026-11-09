<?php
   
include 'functions.php';
$file_name='get_patient_medical_details_client';

$response = array();
//$_POST['username']='0775455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['report_type']) && isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['report_country'])) {
   error_log(strip_tags("Error logging in. missing fields".$file_name), 0);
	$username = $_POST["username"];
	$report_type = $_POST["report_type"];
	$start_date = $_POST["start_date"];
	$end_date = $_POST["end_date"];
	$report_country = $_POST["report_country"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	
	//error_log(strip_tags("Error logging in. missing fields report_type is  ".$report_type." and username is ".$username." and country is ".$report_country." and dates ".$start_date." and end //date is ".$end_date), 0);
	if($report_type=='PhoneNo'){	
	$sql=  "SELECT transaction_date,tariff_code,tariff_description,tariff_code_frequency,tariff_amount,tariff_total_amount,no_of_points,merchant_doctor,merchant_phone_number FROM tbl_bill_tariff_merchant_records where phone_number_billed =:field_val and phone_number_billed_country =:field_val1 ORDER BY 1 DESC"; 
}else{

			$dt   = new DateTime();
			$date_paid_out = $dt->createFromFormat('d/m/Y', $start_date);
			$newStartDate = $date_paid_out->format('Y-m-d');
		

			$dt2= new DateTime();
			$date_paid_out2 = $dt2->createFromFormat('d/m/Y', $end_date);
			$newEndDate = $date_paid_out2->format('Y-m-d');
//error_log(strip_tags("Error logging in. missing fields report_type is  ".$report_type." and username is ".$username." and country is ".$report_country." and dates ".$newStartDate." and end date is //".$newEndDate), 0);
	$sql= "SELECT transaction_date,tariff_code,tariff_description,tariff_code_frequency,tariff_amount,tariff_total_amount,no_of_points,merchant_doctor,merchant_phone_number FROM tbl_bill_tariff_merchant_records where phone_number_billed =:field_val and phone_number_billed_country =:field_val1 and transaction_date BETWEEN '$newStartDate' AND '$newEndDate' ORDER BY 1 DESC"; 

}
			
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $report_country, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    							
								$temp["success"] = true; 
								$transaction_date = $row['transaction_date']; 
								$timestamp = strtotime($transaction_date); 
								$new_date = date('d-m-Y', $timestamp);
								$dt   = new DateTime();
								$temp["transaction_date"] = $new_date;
								$temp["tariff_code"] = $row['tariff_code'];
								$temp["tariff_description"] = $row['tariff_description'];
								$temp["tariff_code_frequency"] = $row['tariff_code_frequency'];	
								$temp["tariff_amount"] = $row['tariff_amount'];	
								$temp["tariff_total_amount"] = $row['tariff_total_amount'];	
								$temp["no_of_points"] = $row['no_of_points'];	
								$temp["merchant_doctor"] = $row['merchant_doctor'];
								$temp["merchant_phone_number"] = $row['merchant_phone_number'];	
								$message='Successful';
								$temp["message"] = $message;
								array_push($response, $temp);			
							}
							echo json_encode($response);
					}else{
						
							$success = "false";
							$message = "You Have No Medical Records To View.";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
					}
    
 }else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}

	
?>
