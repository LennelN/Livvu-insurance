<?php
   
include 'functions.php';
$file_name='get_society_company_member_details';

$response = array();
//$_POST["username"] = '0779455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['merchantCode'])) {
   
	$username = $_POST["username"];
	$merchantCode = $_POST["merchantCode"];
//$username ='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();


				$sql= "SELECT member_phone_number FROM tbl_society_company_members WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1 ORDER BY 1 DESC"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchantCode, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true; 
												$temp["member_phone_number"] = $row['member_phone_number'];
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								}
								echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "You Have No Registered Members At The Moment. Please Register.";
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
