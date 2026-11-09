<?php
   
include 'functions.php';
$file_name='bil_get_merchant_payments_client_date_range';

$response = array();

if (isset($_POST['username']) && isset($_POST['phone_no']) && isset($_POST['startDate']) && isset($_POST['endDate']) && isset($_POST['merchant_code']) && isset($_POST['country_name'])) {
   
	$username = $_POST["username"];
	$startDate = $_POST["startDate"];
	$endDate = $_POST["endDate"];
	$phone_number = $_POST["phone_no"];
	$merchant_code = $_POST["merchant_code"];
	$country_name = $_POST["country_name"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
		
			$dt   = new DateTime();
			$date_paid_out = $dt->createFromFormat('d/m/Y', $startDate);
			$newDate = $date_paid_out->format('Y-m-d');
			//echo $newDate;

			$dt2= new DateTime();
			$date_paid_out2 = $dt2->createFromFormat('d/m/Y', $endDate);
			$newDate2 = $date_paid_out2->format('Y-m-d');
		
	$sql= "SELECT date_paid,phone_number,bill_number,amount_paid FROM tbl_merchant_bill_payments WHERE merchant_phone_number = :field_val AND (date_paid BETWEEN '$newDate' AND '$newDate2') AND phone_number = :field_val1 AND merchant_code = :field_val2 AND country = :field_val3 ORDER BY 1 DESC"; 
			
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $phone_number, PDO::PARAM_STR);
					$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR);
					$stmt->bindParam(':field_val3', $country_name, PDO::PARAM_STR);
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    							
								$temp["success"] = true; 
								$date_of_payment = $row['date_paid']; 
								$timestamp = strtotime($date_of_payment); 
								$new_date = date('d-m-Y', $timestamp);
								$dt   = new DateTime();
								$temp["date_bill_paid"] = $new_date;
								$temp["phone_number"] = $row['phone_number'];
								$temp["bill_number"] = $row['bill_number'];
								$temp["amount_paid"] = $row['amount_paid'];															$message='Successful';
								$temp["message"] = $message;
								array_push($response, $temp);			
							}
							echo json_encode($response);
					}else{
						
							$success = "false";
							$message = "You Have No Payments To View.";
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
