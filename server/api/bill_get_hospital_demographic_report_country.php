<?php
   
include 'functions.php';
$file_name='bill_get_group_count_report';

$response = array();
//$_POST["country"]='ZIMBABWE';

if (isset($_POST['country'])) {

   	$country = $_POST["country"];
	$data_found = 0;
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
		
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
								$terms_status_flag = $row['terms_status_flag'];
								$terms_status_flagCount = $row['CountOfclient_registration'];
								$temp["terms_status_flag"] = $terms_status_flag;
								$temp["count"] = $terms_status_flagCount;
								$message='Successful';
								$temp["message"] = $message;
								if($terms_status_flagCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}						
												
							}
						}



						if($data_found == 0){
							$message = "No Hospitals Registered For ".$country;
							$success = "false";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
						}else{
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
