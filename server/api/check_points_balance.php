<?php

include 'functions.php';
$file_name='check_points_balance';

$response = array();

if (isset($_POST['username']) && $_POST['country_name']) {
   
	$username = $_POST["username"];
	$country_name = $_POST["country_name"];
	require_once __DIR__ . '/db_connect.php';
//error_log(strip_tags("username is ".$username."and country_name is ".$country_name), 0);
	// connecting to db
    	$db = new DB_CONNECT();

	$sql_country= "SELECT type_of_currency,country_currency FROM tbl_currency_rates WHERE country_currency = :field_val"; 
		$stmt_country = $db->sth->prepare($sql_country);
		$stmt_country->bindParam(':field_val', $country_name, PDO::PARAM_STR); 
		$stmt_country->execute();
		$total_country = $stmt_country->rowCount();
		if($total_country > 0)
		{
		$result = $stmt_country->fetchAll(PDO::FETCH_ASSOC);
			foreach($result as $row){
			$type_of_currency = $row['type_of_currency'];
			$country_currency= $row['country_currency'];				
			}	
															
		}

	//Check whether the username exists and return message telling the client to register

	$sql= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $country_name, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

 			if($total <= 0){
				
				$amount = 0;
    		
				$success = "false";
				$message = "Your Account Balance Is Zero. Please Go Ahead And Pay Your Subscription.";
				array_push($response,array("success"=>$success,"message"=>$message,"amount"=>$amount));
				echo json_encode($response);
 			}else{
				$success = "true";
				$amount = $stmt->fetchColumn();
				$message = "Your Account Balance Is ". $type_of_currency." ".$amount;
				array_push($response,array("success"=>$success,"message"=>$message,"amount"=>$amount));
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
