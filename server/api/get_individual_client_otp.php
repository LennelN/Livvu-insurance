<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='get_individual_client_otp';
//error_log(strip_tags("comingin"), 0);
// check for required fields
//$_POST['username']="0785451417";
//$_POST['login_country']='ZIMBABWE';

if (isset($_POST['username']) && isset($_POST['login_country'])){

	$username = $_POST["username"];
	$login_country = $_POST["login_country"];
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
	 // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$reset_password = mt_rand(0, 9999);
	$len_password = strlen($reset_password);
	
	if($len_password < 4) {
	
		do{
			
			$reset_password = mt_rand(0, 9999);
			$len_password_2 =  strlen($reset_password);
			
			if($len_password_2 == 4){
				
				$result = true;
			}
		} while ($result == false);

	}
	$ddate = Date("Y-m-d");
	$sql= "SELECT phone_number,MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val1 AND purchase_country = :field_val2"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val1', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val2', $login_country, PDO::PARAM_STR);

	$stmt->execute();
	$total = $stmt->rowCount();

	if($total > 0) 
	{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row)
		{    							
			$client_phone_number_returned=$row['phone_number'];
			$client_due_date_returned=$row['MAX(due_date)'];
		}

		//error_log(strip_tags("comingin due date is ".$client_due_date_returned), 0);
		$sql_year= "SELECT phone_number,MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2"; 
		$stmt_year = $db->prepare($sql_year);
		$stmt_year->bindParam(':field_val1', $username, PDO::PARAM_STR); 
		$stmt_year->bindParam(':field_val2', $login_country, PDO::PARAM_STR);

		$stmt_year->execute();
		$total_year = $stmt_year->rowCount();

		if($total_year > 0) 
		{
			$result_year = $stmt_year->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result_year as $row)
			{    							
				$client_phone_number_returned=$row['phone_number'];
				$client_end_date_returned=$row['MAX(end_date)'];
			}
		}
		
		//error_log(strip_tags("comingin due date is ".$client_end_date_returned), 0);
		if($ddate > $client_due_date_returned){
			$message = " Your Account Is Not Up To Date. Please Pay Your Subscriptions To Update Your Account";
		}else{
			$sql_balance= "SELECT phone_number,amount FROM tbl_no_of_points WHERE phone_number = :field_val1 AND country = :field_val2"; 
			$stmt_balance = $db->prepare($sql_balance);
			$stmt_balance->bindParam(':field_val1', $username, PDO::PARAM_STR); 
			$stmt_balance->bindParam(':field_val2', $login_country, PDO::PARAM_STR);


			$stmt_balance->execute();
			$total_balance = $stmt_balance->rowCount();

			if($total_balance > 0) 
			{
				$result_balance = $stmt_balance->fetchAll(PDO::FETCH_ASSOC); 
    				foreach($result_balance as $row)
				{    							
					$client_phone_number_returned=$row['phone_number'];
					$client_balance_returned=$row['amount'];
				}
			}

			
			$message = " Your Account Is Up To Date. Thank You. Your balance is ".$client_balance_returned;
		}
	}else{

		$message = " Your Subscription Is Not Up To Date. Please Pay Your Subscriptions To Update Your Account.";
	}
	error_log(strip_tags("login passord is ".$reset_password), 0);
	$success = "true";
	array_push($response,array("success"=>$success,"message"=>$message,"otppassword"=>$reset_password));
	echo json_encode($response);
				
} else {
    // required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}


?>
