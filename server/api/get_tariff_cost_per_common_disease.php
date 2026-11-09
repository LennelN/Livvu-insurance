<?php
   
include 'functions.php';
$file_name='get_tariff_cost_per_common_disease';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['tariff_code']) && isset($_POST['merchant_code'])) {


	$username = ($_POST["username"]);
	$startdate = $_POST["start_date"];
	$enddate = $_POST["end_date"];
	$tariff_code = $_POST["tariff_code"];
	$merchant_code = $_POST["merchant_code"];
error_log(strip_tags("Error logging in. missing fields username is ".$username." and merch ".$merchant_code), 0);
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


	$sql = "SELECT tariff_code,count(tariff_code) AS tariffCount,tariff_description,SUM(`tariff_total_amount`)  AS totalAmount FROM `tbl_bill_tariff_merchant_records` where tariff_code = :field_val AND (transaction_date BETWEEN '$newStartDate' AND '$newEndDate') AND merchant_phone_number = :field_val1 GROUP BY tariff_code";
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $tariff_code, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $username, PDO::PARAM_STR);
					
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
						$message = "No Costs For Tariff Code ".$tariff_code." During This Period From ".$startdate." To ".$enddate;
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

