<?php
   
include 'functions.php';
$file_name='spinner_get_ancilliary_tariffs';
$response = array();
//$_POST['country']='ZIMBABWE';
if (isset($_POST['username'])) {

	$username = $_POST["username"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();

	$sql= "SELECT merchant_country FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0){
							$country = $stmt->fetchColumn();

	$qry = "Select tariff_code,tariff_description,tariff_price,tariff_category,tariff_type from tbl_ancillary_tariffs WHERE tariff_country = '$country' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$total_2 = $stmt->rowCount();
		if($total_2 > 0){
			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row){
				$temp["success"] = true;  
				$temp["message"] = "Tariff Data Available"; 
				$temp["tariff_code"] = $row['tariff_code'];
				$temp["tariff_description"] = $row['tariff_description'];
				$temp["tariff_price"] = $row['tariff_price'];
			
				$temp["tariff_category"] = $row['tariff_category'];
				$temp["tariff_type"] = $row['tariff_type'];
				array_push($response, $temp);
			}
		}else{

			$success = "false";
    			$message = "No Tariff Data For Country ".$country." Please Enter Tariff Data ";
			array_push($response,array("success"=>$success,"message"=>$message));

		}
		
    echo json_encode($response);

}
}else{
error_log(strip_tags("Error logging in. missing fields ".$file_name), 0);
// required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);

}
	
?>
