<?php
   
include 'functions.php';
$file_name='bil_get_client_details';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username'])) {
   
	$username = $_POST["username"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();


					$sql= "SELECT date_bill_created,merchant_phone_number,merchant_code,merchant_name,bill_number,total_bill_amount,bill_amount,phone_number,country FROM tbl_bill_client_records WHERE bill_paid_indicator = 'N' ORDER BY 1 DESC"; 
					$stmt = $db->sth->prepare($sql);
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
												$temp["merchant_phone_number"] = $row['merchant_phone_number'];
												$temp["merchant_code"] = $row['merchant_code'];
												$temp["merchant_name"] = $row['merchant_name'];
																$temp["bill_number"] = $row['bill_number'];
												$temp["total_bill_amount"] = $row['total_bill_amount'];
												$temp["bill_amount"] = $row['bill_amount'];
												$temp["phone_number"] = $row['phone_number'];
												$temp["country"] = $row['country'];
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								}
								echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "You Have No Outstanding Bills To Pay.";
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
