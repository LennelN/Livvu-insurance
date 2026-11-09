<?php
   
include 'functions.php';
$file_name='get_cashed_points_details';

$response = array();
//$_POST['username']='0779455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username'])) {
   
	$username = $_POST["username"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

$todayDate =  date('Y-m-d');
$todaysdate = strtotime($todayDate);

					$sql= "SELECT amount_cashed_out_date,merchant_phone_number,amount_cashed_out,cashed_out_transaction_no FROM tbl_merchant_amount_cashed_out WHERE amount_cashed_out_indicator = 'N' ORDER BY 1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
								$temp["success"] = true; 
								$date_cashed_out = $row['amount_cashed_out_date']; 
								$timestamp = strtotime($date_cashed_out); 
								$new_date = date('d-m-Y', $timestamp);
								$temp["date_cashed_out"] = $new_date;
								$temp["merchant_phone_number"] = $row['merchant_phone_number'];
								$temp["no_of_points_cashed_out"] = $row['amount_cashed_out'];
								$temp["cashed_out_transaction_no"] = $row['cashed_out_transaction_no'];
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
								$message = "You Have No Outstanding Amount Cashed Out...!!!";
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
