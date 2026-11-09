<?php
   
include 'functions.php';
$file_name='get_cost_per_common_disease_country';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['country'])) {


	
	$startdate = $_POST["start_date"];
	$enddate = $_POST["end_date"];
	$country = $_POST["country"];

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
	$average_cost=0;

	$len_phone_nos = 0;
	$in_clause_phone_nos="";
	$sql = "SELECT merchant_phone_number FROM tbl_merchant_details WHERE merchant_country = :field_val ORDER BY 1";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
									
	if($total > 0 )
	{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row)
		{	
				
			$merchant_phone_no = $row['merchant_phone_number'];

			$in_clause_phone_nos = $in_clause_phone_nos."'".$merchant_phone_no."',";
		}
	}

	$len_phone_nos = strlen($in_clause_phone_nos);
	$len_phone_nos = $len_phone_nos - 1;
	$merchant_phone_numbers = substr($in_clause_phone_nos, 0, $len_phone_nos);

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


	$sql = "SELECT tariff_code,count(tariff_code) AS tariffCount,tariff_description,SUM(`tariff_total_amount`)  AS totalAmount FROM `tbl_bill_tariff_merchant_records` where tariff_code NOT IN($tariff_code_in) AND (transaction_date BETWEEN '$newStartDate' AND '$newEndDate') AND merchant_phone_number IN($merchant_phone_numbers)  GROUP BY tariff_code";
					$stmt = $db->sth->prepare($sql);
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
							$totalAmount = $row['totalAmount'];
							$num = $num + 1;
							$average_cost = round($totalAmount / $total_count);
							$temp["success"] = true;  
							$temp["tariff_code"] = $tariff_code;
							$temp["tariff_description"] = $tariff_description;
							$temp["averageCost"] = $average_cost;
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
						$message = "No Costs Of Medicines During This Period From ".$startdate." To ".$enddate;
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

