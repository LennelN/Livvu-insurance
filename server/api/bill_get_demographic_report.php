<?php
   
include 'functions.php';
$file_name='bill_get_demographic_report';

$response = array();
if (isset($_POST['country'])) {
   	$country = $_POST["country"];
$data_found = 0;
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
$genderCount=0;
			$sql="SELECT gender,Count(tbl_clients.gender) AS CountOfclient_gender FROM tbl_clients WHERE member_active = 'Y' AND country_name = :field_val GROUP BY gender";	
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
								$gender = $row['gender'];
								$genderCount = $row['CountOfclient_gender'];
								$temp["category"] = $gender;
								$temp["count"] = $genderCount;
								$message='Successful';
								$temp["message"] = $message;
								if($genderCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
								//array_push($response, $temp);
							}
					}
					
			$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND merchant_country = :field_val GROUP BY type_of_registration";
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
								$type_of_registration = $row['type_of_registration'];
								$registrationClientCount = $row['CountOfclient_registration'];
								$temp["category"] = $type_of_registration;
								$temp["count"] = $registrationClientCount;
								$message='Successful';
								$temp["message"] = $message;
								if($genderCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
												
												
							}
						}
								//error_log(strip_tags($response), 0);
					if($data_found == 0){
						$message = "No Demographic Data For Country ".$country;
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
