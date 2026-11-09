<?php
session_start();
require_once("./include/membersite_config.php");
include("./include/functions.php");

require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$response = array();
$username = trim($_POST["phoneno"]);
//$country=$_SESSION["merchant_country"];
	$merchant_phone_number=$_SESSION["merchant_phone_number"];
	$merchant_code=$_SESSION["merchant_code"];

$tariff_total_bill = 0;

   
					$sql = "SELECT fullname FROM tbl_doctor_details WHERE phone_number = :field_val AND doctor_active = 'Y' AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2";
	
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    							
							$full_name = $row['fullname'];
							$temp["full_name"] = $full_name;
							array_push($response, $temp);
						}

						echo json_encode(['response' => true,'ddd' => $response]);			
					}else{

						$temp["full_name"] = "No Name";
						array_push($response, $temp);
						echo json_encode(['response' => false,'ddd' => $response]);

					}
?>