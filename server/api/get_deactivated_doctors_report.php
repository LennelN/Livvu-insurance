<?php

include 'functions.php';
$file_name='get_deactivated_doctors_report';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code'])) {
   require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$merchant_phone_number = $_POST["merchant_phone_number"];
	$merchant_code = $_POST["merchant_code"];
 
					$sql = "SELECT date_joined,fullname,phone_number,profession,registration_no,date_deactivated,reason_deactivated FROM tbl_doctor_details WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1 AND doctor_active = 'N' ORDER BY 1 ASC";
	
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
$stmt->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    
								$success = "true";							
								$temp["success"] = $success;							
								$date_joined = $row['date_joined']; 
								$timestamp = strtotime($date_joined); 
								$new_date = date('d-m-Y', $timestamp);
								$temp["date_joined"] = $new_date;
								$dt   = new DateTime();
								$phone_number = $row['phone_number'];
								$temp["phone_number"] = $phone_number;
								$full_name = $row['fullname'];
								$temp["full_name"] = $full_name;
								$profession = $row['profession'];
								$temp["profession"] = $profession;
								$registration_no = $row['registration_no'];								
								$temp["registration_no"] = $registration_no;
								$date_deactivated = $row['date_deactivated'];
								$temp["date_deactivated"] = $date_deactivated;
								$reason_deactivated = $row['reason_deactivated'];							
								$temp["reason_deactivated"] = $reason_deactivated;
								$message='Successful';
								$temp["message"] = $message;
								array_push($response, $temp);
							}												
												
						echo json_encode($response);	
					}else{
						
								$success = "false";
								$message = "There Are No Active Doctors In The System.";
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