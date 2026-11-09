<?php
   
include 'functions.php';
$file_name='bil_get_client_tariff_details';

$response = array();

if (isset($_POST['username']) && isset($_POST['merchantCode']) && isset($_POST['merchantUserName']) && isset($_POST['bill_number'])) {
   
	$username = $_POST["username"];
	$code = $_POST["merchantCode"];
	$merchant_phone_number = $_POST["merchantUserName"];
	$bill_number = $_POST["bill_number"];
	
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();


					$sql= "SELECT transaction_date,tariff_code_frequency,tariff_code,tariff_description,tariff_amount,tariff_total_amount,no_of_points FROM tbl_bill_tariff_merchant_records 
									WHERE bill_number = :field_val"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){
												$temp["success"] = true;  
												$temp["transaction_date"] = $row['transaction_date'];
												$temp["tariff_code_frequency"] = $row['tariff_code_frequency'];
												$temp["tariff_code"] = $row['tariff_code'];
																	$temp["tariff_description"] = $row['tariff_description'];
												$temp["tariff_amount"] = $row['tariff_amount'];
												$temp["tariff_total_amount"] = $row['tariff_total_amount'];
												$temp["no_of_points"] = $row['no_of_points'];
												array_push($response, $temp);
												echo json_encode($response);
								}
					}else{
						
								$success = "false";
								$message = "You Have No Outstanding Bills To Be Paid At The Moment.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
					}
    
 }else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}

	
?>
