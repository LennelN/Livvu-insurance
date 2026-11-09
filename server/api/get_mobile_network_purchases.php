<?php
   
include 'functions.php';
$file_name='get_mobile_network_purchases';

$response = array();
//$_POST['mobile_operator']='Telecel';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['mobile_operator'])) {
   
	$mobile_operator = $_POST["mobile_operator"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
$todayDate =  date('Y-m-d');
$todaysdate = strtotime($todayDate);


					$sql= "SELECT date_of_purchase,phone_number,purchase_amount,purchase_done_by,transaction_number FROM tbl_purchase_points_details WHERE transaction_type ='Airtime' AND purchase_done_by = :field_val AND purchase_status = 'N' ORDER BY 1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $mobile_operator, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true; 
												$date_of_purchase = $row['date_of_purchase']; 
												$timestamp = strtotime($date_of_purchase); 
												$new_date = date('d-m-Y', $timestamp);
												$temp["date_of_purchase"] = $new_date;
												$temp["phone_number"] = $row['phone_number'];
												$temp["purchase_amount"] = $row['purchase_amount'];
												$temp["purchase_done_by"] = $row['purchase_done_by'];
												$noofdays = $todaysdate - $timestamp;
												$noofdays = round($noofdays/(60*60*24));
												$temp["no_of_days"] = $noofdays;
												$temp["transaction_number"] = $row['transaction_number'];
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								}
								echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "You Have No Outstanding Mobile Network Purchases...!!!";
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
