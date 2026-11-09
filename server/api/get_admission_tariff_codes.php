<?php
   
include 'functions.php';
$file_name='get_admission_tariff_codes';
$response = array();
//$_POST['country']='ZIMBABWE';
if (isset($_POST['country'])) {
//error_log(strip_tags("coming in "), 0);
	$country = $_POST["country"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();

	$len = 0;
	$in_clause="";

	$sql = "SELECT admission_tariff_code FROM tbl_country_admissions_tariff_codes WHERE country_name = :field_val ORDER BY 1";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
									
	if($total > 0 )
	{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row)
		{	
				
			$admission_tariff_code = $row['admission_tariff_code'];

			$in_clause = $in_clause."'".$admission_tariff_code."',";
		}
	}
	$len = strlen($in_clause);
	$len = $len - 1;
	$tariff_code_in = substr($in_clause, 0, $len);
if($tariff_code_in != null){
	$qry = "Select tariff_code,tariff_description,tariff_price,tariff_category,tariff_type from tbl_ancillary_tariffs WHERE tariff_country = '$country' AND tariff_code IN($tariff_code_in) ORDER BY 1";
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
}
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
