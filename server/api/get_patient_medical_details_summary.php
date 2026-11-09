<?php
   
include 'functions.php';
$file_name='get_patient_medical_details_summary';

$response = array();
//$_POST['username']='0775455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['report_type']) && isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['report_country'])) {
   
	$username = $_POST["username"];
	$report_type = $_POST["report_type"];
	$start_date = $_POST["start_date"];
	$end_date = $_POST["end_date"];
	$report_country = $_POST["report_country"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	
	
	if($report_type=='PhoneNo'){	

	$sql= "Select tbl_bill_tariff_merchant_records.tariff_code,tbl_bill_tariff_merchant_records.transaction_date,tbl_bill_tariff_merchant_records.tariff_description,tbl_bill_tariff_merchant_records.merchant_doctor,tbl_bill_tariff_merchant_records.merchant_phone_number,SUM(tbl_bill_tariff_merchant_records.tariff_code_frequency) AS sum_tariff_frequency,tariff_amount,SUM(tariff_total_amount) AS sum_total_tariff_amount,SUM(no_of_points) As sum_no_of_points from tbl_bill_tariff_merchant_records WHERE tbl_bill_tariff_merchant_records.phone_number_billed =:field_val AND tbl_bill_tariff_merchant_records.phone_number_billed_country =:field_val1 GROUP BY 1"; 
}else{

			$dt   = new DateTime();
			$startDate = $dt->createFromFormat('d/m/Y', $start_date);
			$newStartDate = $startDate->format('Y-m-d');
			//echo $newDate;

			$dt2= new DateTime();
			$endDate = $dt2->createFromFormat('d/m/Y', $end_date);
			$newEndDate = $endDate->format('Y-m-d');

	$sql="Select tbl_bill_tariff_merchant_records.tariff_code,tbl_bill_tariff_merchant_records.transaction_date,tbl_bill_tariff_merchant_records.tariff_description,tbl_bill_tariff_merchant_records.merchant_doctor,tbl_bill_tariff_merchant_records.merchant_phone_number,SUM(tbl_bill_tariff_merchant_records.tariff_code_frequency) AS sum_tariff_frequency,tariff_amount,SUM(tariff_total_amount) AS sum_total_tariff_amount,SUM(no_of_points) As sum_no_of_points from tbl_bill_tariff_merchant_records WHERE tbl_bill_tariff_merchant_records.phone_number_billed =:field_val AND tbl_bill_tariff_merchant_records.phone_number_billed_country =:field_val1 AND transaction_date BETWEEN '$newStartDate' AND '$newEndDate' GROUP BY 1"; 


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
								$temp["tariff_code"] = $row['tariff_code'];
								$temp["transaction_date"] = $row['transaction_date'];
								$temp["tariff_description"] = $row['tariff_description'];
								$temp["merchant_doctor"] = $row['merchant_doctor'];
								$temp["merchant_phone_number"] = $row['merchant_phone_number'];	
								$temp["sum_tariff_frequency"] = $row['sum_tariff_frequency'];	
								$temp["tariff_amount"] = $row['tariff_amount'];	
								$temp["sum_total_tariff_amount"] = $row['sum_total_tariff_amount'];	
								$temp["sum_no_of_points"] = $row['sum_no_of_points'];	
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
