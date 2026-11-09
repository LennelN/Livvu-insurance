<?php
   
include 'functions.php';
$file_name='bill_get_client_bill_statement';

$response = array();
$_POST['bill_number']='Z43278';
$_POST['phone_no']='0772455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['bill_number']) && isset($_POST['phone_no'])) {

	$bill_number = $_POST["bill_number"];
	$phone_number = $_POST["phone_no"];


	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

		$qry = "Select name, surname from tbl_clients where phone_number = '$phone_number'";
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
			}

			$client_name = $name." ".$surname;
				
$sql="Select transaction_date,tariff_code,tariff_description,tariff_code_frequency,tariff_amount,tariff_total_amount,no_of_points,merchant_doctor,merchant_phone_number FROM tbl_bill_tariff_merchant_records WHERE bill_number = :field_val ORDER BY 1";

				
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 					
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true;  
												$date_of_bill = $row['transaction_date'];
												$timestamp = strtotime($date_of_bill); 
												$new_date = date('d-m-Y', $timestamp);
												$temp["transaction_date"] = $new_date;
												$temp["tariff_code"] = $row['tariff_code'];
												$temp["tariff_description"] = $row['tariff_description'];
												$temp["tariff_code_frequency"] = $row['tariff_code_frequency'];	
												$temp["tariff_amount"] = $row['tariff_amount'];	
												$temp["tariff_total_amount"] = $row['tariff_total_amount'];	
												$temp["no_of_points"] = $row['no_of_points'];	
												$temp["merchant_doctor"] = $row['merchant_doctor'];
												$temp["merchant_phone_number"] = $row['merchant_phone_number'];	
												$temp["client_name"] = $client_name;
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
								}				
								//error_log(strip_tags($response), 0);
								echo json_encode($response);
						
					}else{
						
								$success = "false";
								$message = "No Details Found For Bill Number.".$bill_number;
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
