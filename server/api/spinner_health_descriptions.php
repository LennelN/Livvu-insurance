<?php
    
include 'functions.php';
$file_name='spinner_health_descriptions';

$response = array();

	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();
	$qry = "Select type_of_institution from tbl_health_descriptions ORDER BY 1";

	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){
			$temp["success"] = true;  
        
			$temp["type_of_institution"] = $row['type_of_institution'];
  
  			array_push($response, $temp);
		}
		
    echo json_encode($response);

	
?>
