<?php
   
include 'functions.php';
$file_name='get_ancillary_tariffs';
$response = array();
//$_POST['country']='ZIMBABWE';
if (isset($_POST['country'])) {
//error_log(strip_tags("coming in "), 0);
	$country = $_POST["country"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();

	$qry = "Select tariff_code,tariff_description,tariff_price,tariff_category,tariff_type from tbl_ancillary_tariffs WHERE tariff_country = '$country' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
$total = $stmt->rowCount();
					
					if($total > 0){
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row){
			$temp["success"] = true;  
			$temp["tariff_code"] = $row['tariff_code'];
			$temp["tariff_description"] = $row['tariff_description'];
			$temp["tariff_price"] = $row['tariff_price'];
			
			$temp["tariff_category"] = $row['tariff_category'];
			$temp["tariff_type"] = $row['tariff_type'];
			$message='Successful';
			$temp["message"] = $message;
			array_push($response, $temp);
		}
		
    echo json_encode($response);

}else{
$success = "false";
								$message = "There Are No Ancillary Details For Country ".$country." Please Add Tariffs ";
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
