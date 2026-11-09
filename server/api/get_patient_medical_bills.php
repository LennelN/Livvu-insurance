<?php
   
include 'functions.php';
$file_name='get_patient_medical_bills';

$response = array();
//$_POST["username"] = '0775455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['report_type']) && isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['report_country'])) {
   error_log(strip_tags("Error logging in. missing fields ykho yini "), 0);
	$username = $_POST["username"];
	$report_type = $_POST["report_type"];
	$start_date = $_POST["start_date"];
	$end_date = $_POST["end_date"];
	$report_country = $_POST["report_country"];

//$username ='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();



if($report_type=='PhoneNo'){

				$sql= "SELECT date_bill_created,bill_number,total_bill_amount,bill_amount,merchant_phone_number,merchant_name,bill_paid_indicator FROM tbl_bill_client_records WHERE phone_number = :field_val AND country = :field_val1 ORDER BY 1 DESC"; 
$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $report_country, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true; 
												$date_of_bill = $row['date_bill_created']; 
												$timestamp = strtotime($date_of_bill); 
												$new_date = date('d-m-Y', $timestamp);
												$dt   = new DateTime();
												$temp["date_bill_created"] = $new_date;
												$temp["bill_number"] = $row['bill_number'];
												$temp["total_bill_amount"] = $row['total_bill_amount'];
												$temp["bill_amount"] = $row['bill_amount'];
												$temp["merchant_phone_number"] = $row['merchant_phone_number'];
												$temp["merchant_name"] = $row['merchant_name'];
												$temp["bill_paid_indicator"] = $row['bill_paid_indicator'];
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								}
								echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "You Have No Outstanding Bills To View.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
					}
    

}else{

			$dt   = new DateTime();
			$startDate = $dt->createFromFormat('d/m/Y', $start_date);
			$newStartDate = $startDate->format('Y-m-d');
			//echo $newDate;

			$dt2= new DateTime();
			$endDate = $dt2->createFromFormat('d/m/Y', $end_date);
			$newEndDate = $endDate->format('Y-m-d');

				$sql= "SELECT date_bill_created,bill_number,total_bill_amount,bill_amount,merchant_phone_number,merchant_name,bill_paid_indicator FROM tbl_bill_client_records WHERE phone_number = :field_val AND country = :field_val1 AND date_bill_created BETWEEN '$newStartDate' AND '$newEndDate' ORDER BY 1 DESC"; 



					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $report_country, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true; 
												$date_of_bill = $row['date_bill_created']; 
												$timestamp = strtotime($date_of_bill); 
												$new_date = date('d-m-Y', $timestamp);
												$dt   = new DateTime();
												$temp["date_bill_created"] = $new_date;
												$temp["bill_number"] = $row['bill_number'];
												$temp["total_bill_amount"] = $row['total_bill_amount'];
												$temp["bill_amount"] = $row['bill_amount'];
												$temp["merchant_phone_number"] = $row['merchant_phone_number'];
												$temp["merchant_name"] = $row['merchant_name'];
												$temp["bill_paid_indicator"] = $row['bill_paid_indicator'];
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								}
								echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "You Have No Bills To View.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
					}
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
