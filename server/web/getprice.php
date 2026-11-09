<?php
require_once("./include/membersite_config.php");
include("./include/functions.php");
require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$response = array();
   
					$tariffcode = trim($_POST["tariffcode"]);
//error_log(strip_tags($tariffcode), 0);
					$sql= "SELECT tariff_price,tariff_description FROM tbl_ancilliary_tariffs where tariff_code = :field_val"; 
					//$sql = "SELECT tariff_price FROM tbl_ancilliary_tariffs where tariff_code = '".$tariffcode ."'";
					$stmt_price = $db->prepare($sql);
					$stmt_price->bindParam(':field_val', $tariffcode, PDO::PARAM_STR); 
					$stmt_price->execute();
					$total_price = $stmt_price->rowCount();

					if($total_price > 0)
					{
					
						
						$result = $stmt_price->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){
												$temp["price"] = $row['tariff_price'];;  
												$temp["description"] = $row['tariff_description'];
												array_push($response, $temp);
												echo json_encode($response);
return true;
								}

						
					}

?>