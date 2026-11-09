<?php
    
include 'functions.php';
$file_name='spinner_country';

$response = array();

	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();
	$qry = "Select country_name,country_code,no_phone_digits from tbl_country_codes ORDER BY 1 ASC";

	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){
			$temp["success"] = true;  
        
			$temp["country_name"] = $row['country_name'];
  
			$temp["country_code"] = $row['country_code'];
  
			$temp["no_phone_digits"] = $row['no_phone_digits'];

  				array_push($response, $temp);
		}
		
    echo json_encode($response);

	
?>
