<?php
session_start();
include("./include/functions.php");
require_once("./include/membersite_config.php");
require_once("./include/config.php");

require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$response = array();

$phone_number = trim($_POST["phoneno"]);
//error_log(strip_tags("coming in ".$phone_number), 0);
	$country=($_SESSION["merchant_country"]);
	$merchant_phone_number=($_SESSION["merchant_phone_number"]);
	$merchant_code=($_SESSION["merchant_code"]);
//error_log(strip_tags("coming in ".$merchant_phone_number." and code is ".$merchant_code), 0);

$sql= "SELECT fullname FROM tbl_doctor_details where phone_number  = :field_val AND merchant_phone_number = :field_val1  AND merchant_code = :field_val2  ORDER BY 1";

					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
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