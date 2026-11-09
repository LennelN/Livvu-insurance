<?php
   
include 'functions.php';
$file_name='get_currency_rate';
$response = array();

if (isset($_POST['country'])) {
//error_log(strip_tags("coming in "), 0);
	$country = $_POST["country"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();

	$qry = "Select country_currency,type_of_currency,amount,no_of_points from tbl_currency_rates WHERE country_currency = '$country' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
$total = $stmt->rowCount();
					
					if($total > 0){
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){
			$temp["success"] = true;  
			$temp["country_currency"] = $row['country_currency'];
			$temp["type_of_currency"] = $row['type_of_currency'];
			$temp["amount"] = $row['amount'];
			
			$temp["no_of_points"] = $row['no_of_points'];
			$message='Successful';
			$temp["message"] = $message;
			array_push($response, $temp);
		}
		
    echo json_encode($response);

}else{
$success = "false";
								$message = "There Are No Currency Rates For Country ".$country." Please Add Currency Rates ";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
}
}else{
// required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);

}
	
?>
