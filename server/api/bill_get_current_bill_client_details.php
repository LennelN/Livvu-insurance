<?php
   
include 'functions.php';
$file_name='bill_get_current_bill_client_details';

$response = array();


//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username'])) {
   
	$username = $_POST["username"];
	$total_no_of_points_paid = 0;

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

		$sql = "SELECT DISTINCT(bill_number),merchant_phone_number,merchant_code FROM `tbl_bill_items_tariff_records` WHERE phone_number_billed = :field_val ORDER BY 1 ASC";
					
		$stmt = $db->sth->prepare($sql);
		$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					
		$stmt->execute();
		$total = $stmt->rowCount();
					
		if($total > 0)
		{	

			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row){   
				$bill_number = $row['bill_number'];
				$merchant_phone_number = $row['merchant_phone_number'];
				$merchant_code = $row['merchant_code'];

			}

			$sql= "SELECT SUM(amount_paid) As amount_paid FROM `tbl_merchant_bill_payments` WHERE bill_number = :field_val";
			$stmt = $db->sth->prepare($sql);
			$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
			$stmt->execute();
			$total = $stmt->rowCount();
					
			if($total > 0)
			{					
				$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    				foreach($result as $row){   
					$total_no_of_points_paid = $row['amount_paid'];
				}
			}


			$sql= "SELECT SUM(no_of_points) As total_no_of_points,SUM(tariff_total_amount) As total_bill_amount FROM tbl_bill_items_tariff_records WHERE bill_number = :field_val"; 
			$stmt = $db->sth->prepare($sql);
			$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
			$stmt->execute();
			$total = $stmt->rowCount();
					
			if($total > 0)
			{					
				$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    				foreach($result as $row){   
					$total_no_of_points = $row['total_no_of_points'];
					$total_bill_amount = $row['total_bill_amount'];
				}
			}
			
			$sql= "SELECT merchant_name FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
			$stmt = $db->sth->prepare($sql);
			$stmt->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
			$stmt->execute();
			$total = $stmt->rowCount();
					
			if($total > 0)
			{		
				$merchant_name = $stmt->fetchColumn();
			}


				
												
    										//$success = true;
												$temp["success"] = true; 
												$new_date = date('d-m-Y');
												$dt   = new DateTime();
												$temp["date_bill_created"] = $new_date;
												$temp["merchant_phone_number"] = $merchant_phone_number;
												$temp["merchant_code"] = $merchant_code;
												$temp["merchant_name"] = $merchant_name;
																$temp["bill_number"] = $bill_number;
												$temp["total_bill_amount"] = $total_bill_amount;
												$temp["total_no_of_points"] = $total_no_of_points - $total_no_of_points_paid;
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								
								echo json_encode($response);
	}else{
						
		$success = "false";
		$message = "You Have No Current Bill To Pay.";
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
