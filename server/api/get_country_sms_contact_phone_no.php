<?php
   
include 'functions.php';
$file_name='get_country_sms_contact_phone_no';
$response = array();

if (isset($_POST['country'])) {
//error_log(strip_tags("coming in "), 0);
	$country = $_POST["country"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();

	$qry = "Select contact_phone_no FROM tbl_country_sms_contact_phone_no WHERE country_name = '$country' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$total = $stmt->rowCount();
					
	if($total > 0)
	{
			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row)
			{
				$temp["success"] = true;  
				$temp["contact_phone_no"] = $row['contact_phone_no'];
				$message='Successful';
				$temp["message"] = $message;
				array_push($response, $temp);
			}
		
    			echo json_encode($response);

	}else{
		$success = "false";
		$message = "No SMS Contact Phone Number Found For Country ".$country." Please Add SMS Contact Phone Number ";
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
