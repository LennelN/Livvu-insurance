<?php
   
include 'functions.php';
$file_name='bill_get_current_client_statement';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['phone_no']) && isset($_POST['report_country'])) {

	$phone_number = $_POST["phone_no"];
	$report_country = $_POST["report_country"];
	//$bill_number = $_POST["bill_number"];
//error_log(strip_tags("Error logging in. missing fields phone_number ".$phone_number." and country ".$report_country), 0);
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$qry = "Select name, surname from tbl_clients where phone_number = '$phone_number' AND country_name = '$report_country'";
	$stmt = $db->sth->prepare($qry);
        	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
	$total =  $stmt->rowCount();
	if($total>0)
	{
    		foreach($result as $row)
		{
			$name = $row['name'];
   			$surname = $row['surname'];
        
		}

		$client_name = $name." ".$surname;
			$sql="Select transaction_date,tariff_code,tariff_description,tariff_total_amount,0 As no_of_points_paid,phone_number_billed,bill_number FROM tbl_bill_tariff_merchant_records WHERE phone_number_billed  = :field_val AND  phone_number_billed_country  = :field_val1 ORDER BY 1";
	
				
			$stmt = $db->sth->prepare($sql);
			$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
			$stmt->bindParam(':field_val1', $report_country, PDO::PARAM_STR); 
			$stmt->execute();
			$total = $stmt->rowCount();
					
			if($total > 0)
			{					
				$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    				foreach($result as $row)
				{    							
   					//$success = true;
					$temp["success"] = true;  
					$date_of_bill = $row['transaction_date'];
					$timestamp = strtotime($date_of_bill); 
					$new_date = date('d-m-Y', $timestamp);
					$temp["transaction_date"] = $new_date;
					$temp["tariff_code"] = $row['tariff_code'];
					$temp["tariff_description"] = $row['tariff_description'];
					$temp["no_of_points"] = $row['tariff_total_amount'];
					$temp["no_of_points_paid"] = $row['no_of_points_paid'];
					$bill_number = $row['bill_number'];
					$temp["client_name"] = $client_name;
					$message='Successful';
					$temp["message"] = $message;
					array_push($response, $temp);
										
											
				}

				$sql = "SELECT date_paid,'',transaction_type,0,amount_paid,phone_number from tbl_merchant_bill_payments where bill_number = :field_val1 AND country = :field_val2 ORDER BY 1";
			
				$stmt = $db->sth->prepare($sql);
					
				$stmt->bindParam(':field_val1', $bill_number, PDO::PARAM_STR); 
				$stmt->bindParam(':field_val2', $report_country, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();
					
				if($total > 0)
				{					
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result as $row)
					{    							
    						//$success = true;
						$temp["success"] = true;  
						$date_of_payment = $row['date_paid'];
						$timestamp = strtotime($date_of_payment); 
						$new_date_payment = date('d-m-Y', $timestamp);
						$temp["transaction_date"] = $new_date_payment;
						$temp["tariff_code"] = $row['phone_number'];
						$temp["tariff_description"] = $row['transaction_type'];
						$temp["no_of_points"] = 0;
						$temp["no_of_points_paid"] = $row['amount_paid'];
						$message='Successful';
						$temp["message"] = $message;
						array_push($response, $temp);
									
									
					}
				}

				//error_log(strip_tags($response), 0);
				echo json_encode($response);
						
			}else{
			
				$success = "false";
				$message = "Phone Number Has No Current Bill To View.";
				array_push($response,array("success"=>$success,"message"=>$message));
				echo json_encode($response);
			}
	
	}else{

			$success = "false";
			$message = "Phone Number Does Not Exist! Please Register And Try Again";
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
