<?php
   
include 'functions.php';
$file_name='bill_get_demographic_report';

$response = array();

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

					
			$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' GROUP BY type_of_registration";
						$stmt = $db->sth->prepare($sql);
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
								array_push($response, $temp);
												
												
							}
						}
								//error_log(strip_tags($response), 0);
						echo json_encode($response);
							
?>
