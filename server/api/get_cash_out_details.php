<?php
   
include 'functions.php';
$file_name='get_cash_out_details';

$response = array();
//$_POST['username']='0779455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['merchant_code'])) {
   
	$username = $_POST["username"];
	$merchant_code = $_POST["merchant_code"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
$todayDate =  date('Y-m-d');
$todaysdate = strtotime($todayDate);

					$sql= "SELECT amount_cashed_out_date,merchant_phone_number,amount_cashed_out,cashed_out_transaction_no,(CURRENT_DATE-amount_cashed_out_date)As NoOfDays FROM tbl_merchant_amount_cashed_out WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1 AND amount_paid_out_indicator = 'N' ORDER BY 1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
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
												$temp["amount_cashed_out"] = $row['amount_cashed_out'];
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
