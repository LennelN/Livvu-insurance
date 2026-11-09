<?php
   
include 'functions.php';
$file_name='get_deactivated_doctor_name';
$response = array();

if (isset($_POST['phone_no']) && isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code'])) {
//error_log(strip_tags("coming in "), 0);
	$phone_no = $_POST["phone_no"];
	$merchant_phone_number = $_POST["merchant_phone_number"];
	$merchant_code = $_POST["merchant_code"];
//error_log(strip_tags("coming in ".$phone_no), 0);
	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();
	$qry= "SELECT fullname FROM tbl_doctor_details WHERE phone_number = :field_val AND doctor_active = 'N' AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2"; 
	//$qry = "Select country_currency,type_of_currency,amount,no_of_points from tbl_currency_rates WHERE country_currency = '$country' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
	$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR); 
        	$stmt->execute();
	$total = $stmt->rowCount();
					
	if($total > 0){
			$fullname = $stmt->fetchColumn();
			$temp["success"] = true;  
			$temp["full_name"] = $fullname;
			$message='Successful';
			$temp["message"] = $message;
			array_push($response, $temp);
		
    		echo json_encode($response);

	}else{
		$success = "false";
		$fullname = "No Name";
		$message = "Phone Number Is Not Deactivated In The System. ";
		array_push($response,array("success"=>$success,"full_name"=>$fullname,"message"=>$message));
		echo json_encode($response);
	}
}else{
// required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);

}
	
?>
