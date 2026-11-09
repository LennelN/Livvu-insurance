<?php
    
include 'functions.php';
$file_name='spinner_continent';

$response = array();

	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();
	$qry = "Select DISTINCT(continent) from tbl_region_continent ORDER BY 1 ASC";

	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){
			$temp["success"] = true;  
        
			$temp["continent_name"] = $row['continent'];
  
  				array_push($response, $temp);
		}
		
    echo json_encode($response);

	
?>
