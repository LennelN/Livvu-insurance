<?php
    
include 'functions.php';
$file_name='spinner_health_institutions';

$response = array();
//error_log(strip_tags("comingin 2"), 0);
if (isset($_POST['country_name']) && isset($_POST['region_name'])) {
error_log(strip_tags("comingin 22"), 0);
	$country_name = $_POST["country_name"];
	$region_name = $_POST["region_name"];
//error_log(strip_tags("comingin 2 posted data is  ".$country_name." and ".$region_name), 0);
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();
	$qry = "Select merchant_name,merchant_code,merchant_phone_number,merchant_region from tbl_merchant_details WHERE merchant_country = :field_val AND merchant_region = :field_val1 AND type_of_registration = 'Health' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
	$stmt->bindParam(':field_val', $country_name, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $region_name, PDO::PARAM_STR); 
        	$stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0){
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){
			$temp["success"] = true; 
			$temp["message"] = "Health Institutions Available"; 
        			$merchant_name_data_retrieved = $row['merchant_name'];
			$temp["merchant_name_data"] = $merchant_name_data_retrieved;
			$merchant_code_data_retrieved = $row['merchant_code'];
			$temp["merchant_code_data"] = $merchant_code_data_retrieved;
			$merchant_phone_number_data_retrieved = $row['merchant_phone_number'];
			$temp["merchant_phone_number_data"] = $merchant_phone_number_data_retrieved;
			$merchant_region_data_retrieved = $row['merchant_region'];
			$temp["merchant_region_data"] = $merchant_region_data_retrieved;
  			//error_log(strip_tags("comingin data retieved is ".$merchant_name_data_retrieved." and code is ".$merchant_code_data_retrieved." and phoneno is //".$merchant_phone_number_data_retrieved."and region is ".$merchant_region_data_retrieved),  0);
			array_push($response, $temp);
		}
		
	}else{
		$success = "false";
    		$message = "No Health Institutions Found  For Country ".$country_name." And Region  ".$region_name;
		array_push($response,array("success"=>$success,"message"=>$message));

	}
    echo json_encode($response);

}else{

$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);

}
	
?>
