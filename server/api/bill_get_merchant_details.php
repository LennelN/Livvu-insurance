<?php
   
include 'functions.php';
$file_name='bil_get_merchant_details';

$response = array();
//$_POST["username"] = '0779455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username'])) {
   
	$username = $_POST["username"];
//$username ='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
$todayDate =  date('Y-m-d');
$todaysdate = strtotime($todayDate);
				$sql= "SELECT date_bill_created,phone_number_billed,bill_number,total_bill_amount FROM tbl_bill_merchant_records WHERE merchant_phone_number = :field_val AND bill_paid_indicator = 'N' ORDER BY 1 ASC"; 
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
												$bill_number = $row['bill_number'];
												$temp["bill_number"] = $bill_number;
												$temp["total_no_of_points"] = $row['total_bill_amount'];
												$noofdays = $todaysdate - $timestamp;
												$noofdays = round($noofdays/(60*60*24));
												$temp["no_of_days"] = $noofdays;
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
