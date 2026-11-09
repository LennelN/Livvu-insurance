<?php
   
include 'functions.php';
$file_name='spinner_get_active_system_users';
$response = array();
//$_POST['country']='SOUTH AFRICA';
if (isset($_POST['country'])) {

	$country = $_POST["country"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();


	$qry = "SELECT merchant_phone_number FROM tbl_merchant_details WHERE status_flag = 'Y' AND merchant_country = :field_val";
	$stmt = $db->sth->prepare($qry);
	$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 

        $stmt->execute();
	$total = $stmt->rowCount();
					
					if($total > 0)
					{
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){
							$temp["success"] = true;  
							$temp["merchant_phone_number"] = $row['merchant_phone_number'];
							array_push($response, $temp);
						}
					}else{
						$success = "false";
    						$message = "There Are No Phone Numbers To Display.";
						array_push($response,array("success"=>$success,"message"=>$message));
					}
		
    echo json_encode($response);


}else{
// required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);

}
	
?>
