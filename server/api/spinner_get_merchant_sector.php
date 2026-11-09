<?php
    
include 'functions.php';
$file_name='spinner_get_merchant_sector';

$response = array();

	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();
	$qry = "SELECT name_of_sector FROM tbl_merchant_sector  ORDER BY 1 ASC";

	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){
			$temp["success"] = true;  
        
			$temp["name_of_sector"] = $row['name_of_sector'];
    
			 array_push($response, $temp);
		}
		
    echo json_encode($response);

	
?>
