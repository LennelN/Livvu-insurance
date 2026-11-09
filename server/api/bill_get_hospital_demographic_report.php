<?php
   
include 'functions.php';
$file_name='bill_get_hospital_demographic_report';
//$_POST["country"]= 'ZIMBABWE';
$response = array();
if (isset($_POST['country'])) {
   
	$country = $_POST["country"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	//SELECT terms_status_flag,count(terms_status_flag) FROM `tbl_merchant_details` where type_of_registration='Health' GROUP BY 1				
			$sql = "SELECT terms_status_flag,Count(tbl_merchant_details.terms_status_flag) AS CountOfclient_registration FROM tbl_merchant_details WHERE merchant_country = :field_val AND status_flag = 'Y' AND type_of_registration='Health' GROUP BY terms_status_flag";
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{    							
								$temp["success"] = true;  
								$type_of_registration = $row['terms_status_flag'];
								$registrationClientCount = $row['CountOfclient_registration'];
								$temp["category"] = $type_of_registration;
								$temp["count"] = $registrationClientCount;
								$message='Successful';
								$temp["message"] = $message;
								array_push($response, $temp);
												
												
							}
						}
								//error_log(strip_tags($response), 0);
						echo json_encode($response);

}else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}



							
?>
