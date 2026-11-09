<?php
   
include 'functions.php';
$file_name='get_mobile_network_companies';

$response = array();
//$_POST['country']='South Afirca';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['country_name'])) {

	$country_name = $_POST["country_name"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();


					$sql= "SELECT name_of_mobile_operator FROM tbl_mobile_operators WHERE country_of_operation = :field_val ORDER BY 1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $country_name, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){  
												$temp["success"] = true; 
												$temp["name_of_mobile_operator"] = $row['name_of_mobile_operator'];
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								}
								echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "You Have No Mobile Network Companies Set Up On The System ]...!!!";
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
