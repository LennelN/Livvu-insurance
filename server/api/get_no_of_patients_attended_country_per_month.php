<?php
   
include 'functions.php';
$file_name='get_no_of_patients_attended_country_per_month';

$response = array();


//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['country']) && isset($_POST['start_date']) && isset($_POST['end_date'])) {
   
	$country = $_POST["country"];
	$startdate = $_POST["start_date"];
	$enddate = $_POST["end_date"];

	$dt   = new DateTime();
	$date_paid_out = $dt->createFromFormat('d/m/Y', $startdate);
	$newStartDate = $date_paid_out->format('Y-m-d');
		

	$dt2= new DateTime();
	$date_paid_out2 = $dt2->createFromFormat('d/m/Y', $enddate);
	$newEndDate = $date_paid_out2->format('Y-m-d');


	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$merchant_region="";
	$merchant_region_data_processed = "";
	$total_count = 0;
	$message = "Number Of Patients Attended: From ".$startdate." To ".$enddate;
	$len = 0;
	$in_clause="";
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

			$in_clause = $in_clause."'".$merchant_phone_no."',";
		}
	}
	$len = strlen($in_clause);
	$len = $len - 1;
	$merchant_phone_numbers = substr($in_clause, 0, $len);
	$sql = "SELECT count(DISTINCT(phone_number_billed)) AS noCount FROM `tbl_bill_tariff_merchant_records` where (transaction_date BETWEEN '$newStartDate' AND '$newEndDate') AND merchant_phone_number IN ($merchant_phone_numbers)";
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
								//$dataRow = $dataRow."<tr><td>$message</td><td>$total_count</td></tr>";		
							}
						}
					}

				if($num == 0)
			{				
				$success = "false";
				$message = "No Patients Attended During This Period From ".$startdate." To ".$enddate." For ".$country;
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

