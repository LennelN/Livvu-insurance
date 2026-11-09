<?php
   
include 'functions.php';
$file_name='bil_get_merchant_details';

$response = array();
//$_POST['username']='0779455025';
//$_POST['startDate']='01/12/2019';
//$_POST['endDate']='31/12/2019';

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['startDate']) && isset($_POST['endDate'])) {
   
	$username = $_POST["username"];
	$startDate = $_POST["startDate"];
	$endDate = $_POST["endDate"];

//$username ='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

$date2 = "01/01/1970"; 
//echo $date2;
//$newDate = $date2->format('Y-m-d');
//echo $newDate;


			$dt   = new DateTime();
			$date_paid_out = $dt->createFromFormat('d/m/Y', $startDate);
			$newDate = $date_paid_out->format('Y-m-d');
			//echo $newDate;

			$dt2= new DateTime();
			$date_paid_out2 = $dt2->createFromFormat('d/m/Y', $endDate);
			$newDate2 = $date_paid_out2->format('Y-m-d');
			//echo $newDate2;
			//$dt   = new DateTime();
			//$date_range_start = $dt->createFromFormat('d/m/Y', $startDate);
			//$newStartDate = $date_range_start->format('Y-m-d');
			//$date_range_end = $dt->createFromFormat('d/m/Y', $endDate);
			//$newEndDate = $date_range_end->format('Y-m-d');

			$todayDate = date('Y-m-d');
			$startDate1 = date('Y-m-d');
			

				//$sql= "SELECT date_bill_created,phone_number_billed,bill_number,total_no_of_points,(CURRENT_DATE-date_bill_created)As NoOfDays FROM //tbl_bill_merchant_records WHERE merchant_phone_number = :field_val AND bill_paid_indicator = 'N' AND (date_bill_created BETWEEN $newStartDate AND $newEndDate)  ORDER //BY 1 DESC"; 
			

$sql= "SELECT date_bill_created,phone_number_billed,bill_number,total_no_of_points,(CURRENT_DATE-date_bill_created)As NoOfDays FROM tbl_bill_merchant_records WHERE merchant_phone_number = :field_val AND (date_bill_created BETWEEN '$newDate' AND '$newDate2') AND bill_paid_indicator = 'N' ORDER BY NoOfDays DESC"; 
//echo $sql;

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
												$date_of_bill = $row['date_bill_created']; 
												$timestamp = strtotime($date_of_bill); 
												$new_date = date('d-m-Y', $timestamp);
												$dt   = new DateTime();
												$temp["date_bill_created"] = $new_date;
												$temp["phone_number_billed"] = $row['phone_number_billed'];
												$temp["bill_number"] = $row['bill_number'];
												$temp["total_no_of_points"] = $row['total_no_of_points'];
												$temp["no_of_days"] = $row['NoOfDays'];
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
    
 }else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}

	
?>
