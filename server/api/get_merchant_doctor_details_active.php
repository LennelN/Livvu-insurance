<?php
$response = array();
include 'functions.php';
$file_name='get_merchant_doctor_details_active';
//$_POST['merchant_phone_number']='0779455025';
//$_POST['merchant_code']='64888';

if (isset($_POST['merchant_phone_number'])  && isset($_POST['merchant_code'])) {
	
	$merchant_phone_number=$_POST['merchant_phone_number'];
	$merchant_code = $_POST['merchant_code'];
	require_once __DIR__ . '/db_connect.php';
//error_log(strip_tags("comingin 2 kanti ".$merchant_phone_number." and tranfse country is ".$merchant_code), 0);
	// connecting to db
    	$db = new DB_CONNECT();

   
	$sql = "SELECT phone_number,fullname,profession,registration_no FROM tbl_doctor_details WHERE doctor_active = 'Y' AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2";
	
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0)
	{					
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){    
			$temp["success"] = true;  							
    			$phone_number = $row['phone_number'];	
			$temp["phone_number"] = $phone_number;
			$full_name = $row['fullname'];
			$temp["full_name"] = $full_name;
			$temp["profession"] = $row['profession'];
			$temp["registration_no"] = $row['registration_no'];
			$message='Successful';
			$temp["message"] = $message;
			array_push($response, $temp);
		}

		 echo json_encode($response);		
	}else{
		$success = "false";
		$message = "There Are No Active Doctors Registered For Merchant. Please Register Doctors Before Billing.";
		array_push($response,array("success"=>$success,"message"=>$message));
		echo json_encode($response);	

	}

 }else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    echo json_encode($response);
}

?>