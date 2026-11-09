<?php
   
include 'functions.php';
$file_name='get_cost_per_patient_per_month';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['country']) && isset($_POST['merchant_code'])) {


	$username = ($_POST["username"]);
	$startdate = $_POST["start_date"];
	$enddate = $_POST["end_date"];
	$country = $_POST["country"];
	$merchant_code = $_POST["merchant_code"];

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dt   = new DateTime();
	$date_paid_out = $dt->createFromFormat('d/m/Y', $startdate);
	$newStartDate = $date_paid_out->format('Y-m-d');
		

	$dt2= new DateTime();
	$date_paid_out2 = $dt2->createFromFormat('d/m/Y', $enddate);
	$newEndDate = $date_paid_out2->format('Y-m-d');

	$dataRow = "";
	$merchant_region="";
	$merchant_region_data_processed = "";
	$total_count = 0;

	$len = 0;
	$in_clause="";

	$sql = "SELECT admission_tariff_code FROM tbl_country_admissions_tariff_codes WHERE country_name = :field_val ORDER BY 1";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
									
	if($total > 0 )
	{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row)
		{	
				
			$admission_tariff_code = $row['admission_tariff_code'];

			$in_clause = $in_clause."'".$admission_tariff_code."',";
		}
	}
	$len = strlen($in_clause);
	$len = $len - 1;
	$tariff_code_in = substr($in_clause, 0, $len);


	$sql = "SELECT tariff_code,count(tariff_code) AS tariffCount,tariff_description,phone_number_billed,SUM(`tariff_total_amount`)  AS totalAmount FROM `tbl_bill_tariff_merchant_records` where tariff_code IN($tariff_code_in) AND (transaction_date BETWEEN '$newStartDate' AND '$newEndDate') AND merchant_phone_number = :field_val  AND merchant_code = :field_val1 GROUP BY phone_number_billed";
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					$num = 0;
					
					if($total > 0 )
					{
						$total_points_cashed_out=0;
						$total_points_paid=0;
						$balance=0;
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
						{
							$tariff_code = $row['tariff_code'];
	
							$total_count = $row['tariffCount'];
							$tariff_description = $row['tariff_description'];
							$phone_number_billed = $row['phone_number_billed'];
							$totalAmount = $row['totalAmount'];
							$num = $num + 1;

							$temp["success"] = true;  
							$temp["tariff_code"] = $tariff_code;
							$temp["tariff_description"] = $tariff_description;
							$temp["phone_number_billed"] = $phone_number_billed;
							$temp["totalAmount"] = $totalAmount;
							$temp["count"] = $total_count;
							$message='Successful';
							$temp["message"] = $message;
							array_push($response, $temp);

							
						}
					}

					if($num == 0)
					{				
						$success = "false";
						$message = "No Admissions During This Period From ".$startdate." To ".$enddate;
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);				
					}else{

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

