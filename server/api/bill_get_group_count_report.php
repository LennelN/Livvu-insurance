<?php
   
include 'functions.php';
$file_name='bill_get_group_count_report';

$response = array();
if (isset($_POST['country'])) {
   	$country = $_POST["country"];
$data_found = 0;
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

			$sql="SELECT group_category,Count(tbl_clients.group_category) AS CountOfCategory FROM tbl_clients WHERE member_active = 'Y' AND country_name = :field_val GROUP BY group_category";	
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
								$group_category = $row['group_category'];
								$groupCount = $row['CountOfCategory'];
								$temp["category"] = $group_category;
								$temp["count"] = $groupCount;
								$message='Successful';
								$temp["message"] = $message;
								if($groupCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
							}
					}
					
					if($data_found == 0){
						$message = "No Group Categories For ".$country;
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
