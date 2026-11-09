<?php
   
include 'functions.php';
$file_name='get_no_of_patients_attended_country_per_month';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['merchant_code'])) {

	$username = ($_POST["username"]);
	$startdate = $_POST["start_date"];
	$enddate = $_POST["end_date"];
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
	$message = "Number Of Patients Attended: From ".$startdate." To ".$enddate;
	$sql = "SELECT count(DISTINCT(phone_number_billed)) AS noCount FROM `tbl_bill_tariff_merchant_records` where (transaction_date BETWEEN '$newStartDate' AND '$newEndDate') AND merchant_phone_number = :field_val AND merchant_code = :field_val1";
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
							$total_count = $row['noCount'];
							if($total_count > 0)
							{
								$num = $num + 1;
								$temp["success"] = true;  
								$temp["description"] = $message;
								$temp["count"] = $total_count;
								$message='Successful';
								$temp["message"] = $message;
								array_push($response, $temp);
							}
	
						}
					}

				if($num>0)
				{
					echo json_encode($response);
				}else{
					$success = "false";
					$message = "No Patients Attended During This Period From ".$startdate." To ".$enddate;
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

