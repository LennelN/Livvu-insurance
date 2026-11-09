<?php

$file_name='register_individual';

$phone_number = isset($_SESSION["user_name"]);
error_log(strip_tags("option in check balance"), 0);
error_log(strip_tags($phone_number), 0);

$response = array();

if (isset($_POST['username'])) {
   
	$username = $_POST["username"];
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();

	//Check whether the username exists and return message telling the client to register

	$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

 			if($total <= 0){
				
				$no_of_points = 0;
    		$success = "false";
				$message = "Your Account Balance Is Zero. Please Go Ahead And Purchase Points For Your Account.";
				array_push($response,array("success"=>$success,"message"=>$message,"no_of_points"=>$no_of_points));
				echo json_encode($response);
 			}else{
				$success = "true";
				$no_of_points = $stmt->fetchColumn();
				$message = "You Have ". $no_of_points. " Points In Your Account";
				array_push($response,array("success"=>$success,"message"=>$message,"no_of_points"=>$no_of_points));
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
