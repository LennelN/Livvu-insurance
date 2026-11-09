<?php
   
include 'functions.php';
$file_name='get_country_dialling_code';
$response = array();

if (isset($_POST['country'])) {
//error_log(strip_tags("coming in "), 0);
	$country = $_POST["country"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();

	$qry = "Select country_dialling_code FROM tbl_country_diallling_codes WHERE country_name = '$country' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$total = $stmt->rowCount();
					
	if($total > 0)
	{
			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row)
			{
				$temp["success"] = true;  
				$temp["country_dialling_code"] = $row['country_dialling_code'];
				$message='Successful';
				$temp["message"] = $message;
				array_push($response, $temp);
			}
		
    			echo json_encode($response);

	}else{
		$success = "false";
		$message = "No Dialling Code Found For Country ".$country." Please Add Dialling Code ";
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
