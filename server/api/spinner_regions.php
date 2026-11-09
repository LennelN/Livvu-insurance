<?php
    
include 'functions.php';
$file_name='spinner_regions';

$response = array();

if (isset($_POST['country_name'])) {

	$country_name = $_POST["country_name"];

	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();
	$qry = "Select name_of_region from tbl_country_regions WHERE name_of_country = :field_val ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
	$stmt->bindParam(':field_val', $country_name, PDO::PARAM_STR); 
        $stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0){
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){
			$temp["success"] = true; 
			$temp["message"] = "Region Data Available"; 
        
			$temp["name_of_region"] = $row['name_of_region'];
  
			array_push($response, $temp);
		}
		
	}else{
		$success = "false";
    		$message = "Country Region Not Found. Please Enter Regions For The Country ".$country_name;
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
