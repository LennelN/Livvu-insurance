<?php
   
$response = array();
include 'functions.php';
$file_name='create_ancillary_tariff';
$purchase_details = array();

if (isset($_POST['phone_number']) && isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['report_type']) && isset($_POST['report_country']))
{

   
	$username = $_POST["phone_number"];
	$report_type = $_POST["report_type"];
	$start_date = $_POST["start_date"];
	$end_date = $_POST["end_date"];
	$report_country = $_POST["report_country"];
	

	$num = 0;
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$dataRow="";
	$sql= "SELECT name,surname FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $report_country, PDO::PARAM_STR); 
	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
	$total = $stmt->rowCount();

 	if($total > 0)
	{
			

	
			if($report_type=='PhoneNo')
			{	

				$sql= "SELECT `date_record_created`, `merchant_phone_number`, `merchant_name`, `body_temperature`, `blood_pressure`, `body_pulse`, `body_allergies`, `blood_type`, `other_details` FROM `tbl_patient_medical_history` WHERE phone_number = :field_val AND country = :field_val1 ORDER BY 1 DESC";

	
			}else{

$dt   = new DateTime();
			$startDate = $dt->createFromFormat('d/m/Y', $start_date);
			$newStartDate = $startDate->format('Y-m-d');
			//echo $newDate;

			$dt2= new DateTime();
			$endDate = $dt2->createFromFormat('d/m/Y', $end_date);
			$newEndDate = $endDate->format('Y-m-d');


				$sql= "SELECT `date_record_created`, `merchant_phone_number`, `merchant_name`, `body_temperature`, `blood_pressure`, `body_pulse`, `body_allergies`, `blood_type`, `other_details` FROM `tbl_patient_medical_history` WHERE phone_number = :field_val AND country = :field_val1 AND (date_record_created BETWEEN '$newStartDate' AND '$newEndDate') ORDER BY 1 DESC";
		
	
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
								$num = $num + 1;
								$transaction_date = $row['date_record_created'];
								$timestamp = strtotime($transaction_date); 
								$new_date = date('d-m-Y', $timestamp);
								$dt   = new DateTime(); 
								$merchant_phone_number = $row['merchant_phone_number'];
								$merchant_name = $row['merchant_name'];
								$body_temperature = $row['body_temperature'];	
								$blood_pressure = $row['blood_pressure'];	
								$body_pulse = $row['body_pulse'];	
								$body_allergies = $row['body_allergies'];	
								$blood_type = $row['blood_type'];
								$other_details = $row['other_details'];	
								$temp["success"] = true;  
								$temp["transaction_date"] = $new_date;
								$temp["merchant_phone_number"] = $merchant_phone_number;
								$temp["merchant_name"] = $merchant_name;
								$temp["body_temperature"] = $body_temperature;
								$temp["blood_pressure"] = $blood_pressure;
								$temp["body_pulse"] = $body_pulse;
								$temp["body_allergies"] = $body_allergies;
								$temp["blood_type"] = $blood_type;
								$temp["other_details"] = $other_details;
								$message='Successful';
								$temp["message"] = $message;
								array_push($response, $temp);
											
							}
					}else{
						
							$success = "false";
							$message = "There Is No Medical History To View For Patient.";
							array_push($response,array("success"=>$success,"message"=>$message));
    							// echoing JSON response
    							echo json_encode($response);
					}
			}else{

					$success = "false";
					$message = "Phone Number Does Not Exist! Please Register And Try Again.";
					array_push($response,array("success"=>$success,"message"=>$message));
    					// echoing JSON response
					echo json_encode($response);

	}   

	 
 		if($num > 0){ echo json_encode($response);}

}else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}


	
?>
