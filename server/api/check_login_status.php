<?php

include 'functions.php';
$file_name='check_login_status';
//error_log(strip_tags("Error logging in. missing fields"), 0);
$response = array();

if (isset($_POST['username']) && isset($_POST['ssid'])) {
   
	$username = $_POST["username"];
	$session_id = $_POST["ssid"];

	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();

	//Check whether the username exists and return message telling the client to register

	date_default_timezone_set('Africa/Harare');

	$date_logged_in = date('Y-m-d H:i:s');
	$time_logged_in = date('H:i:s');
	$logoutdate = date("Y-m-d");
	$logouttime = date("h:i:s");
	$loginstatus = 'Yes';
	$logoutstatus = "No";
	$logindate = date("Y-m-d");


	$sql= "SELECT user_login_id,sessionid,mediaused FROM tbl_user_login_history WHERE user_login_id = :field_val AND sessionid = :field_val1 AND logindate = :field_val2 AND loginstatus = 'Yes'"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR);
	$stmt->bindParam(':field_val1', $session_id, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val2', $logindate, PDO::PARAM_STR);  
	$stmt->execute();
	$total = $stmt->rowCount();

 			if($total > 0){

					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		
					foreach($result as $row){
						$user_login_id = $row['user_login_id'];

						$sessionID = $row['sessionid'];

						$mediaID = $row['mediaused'];

					}


					$success = "true";
					$message = "Login Session Still Valid.";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
			}else{
				
				$success = "false";
				$message = "Your Session Is No Longer Valid. It Might Have Been Cancelled By Another Attempt To Login Using Your Credentials. Try Again";
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
