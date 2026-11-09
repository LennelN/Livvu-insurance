
<?php

session_start();
$response = array();
$response_back = array();
$file_name='create_ancillary_tariff';
require_once("./include/membersite_config.php");
include("./include/functions.php");

//error_log(strip_tags("coming in "), 0);
$tariff_code = trim($_POST["tariff_code"]);
$username = $_SESSION["user_name"];
 
$tariff_description = trim($_POST["tariff_description"]);
$tariff_category = trim($_POST["tariff_category"]);
$tariff_price = trim($_POST["tariff_price"]);
$tariff_type = $_POST["tariff_type"];
$tariff_country = $_POST["tariff_country"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");

	try{

		$stmt = $db->prepare("INSERT INTO tbl_ancillary_tariffs (
									tariff_code,
									tariff_description,
                							tariff_price,
                							tariff_category,
									tariff_type,
									tariff_country,
                							tariff_processed_by) 
    						VALUES 																			(:tariff_code,:tariff_description,:tariff_price,:tariff_category,:tariff_type,:tariff_country,:tariff_processed_by)");
    									$stmt->bindParam(':tariff_code', $tariff_code);
									$stmt->bindParam(':tariff_description', $tariff_description);
									$stmt->bindParam(':tariff_price', $tariff_price);
									$stmt->bindParam(':tariff_category', $tariff_category);
									$stmt->bindParam(':tariff_type', $tariff_type);
    									$stmt->bindParam(':tariff_country', $tariff_country);
									$stmt->bindParam(':tariff_processed_by', $username);
										
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Created Ancillary Tariff Code.";
										$_SESSION["message_create"]=$message;
										$db->commit();
										return true;		
								
									}
							
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Code: ".$tariff_code." Already Exist In The System. Please Check Again.";
							$_SESSION["message_create"]=$message;
							$db->rollBack();
							return false;
						
						}else{	
	
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							$message=$e->getMessage();
							$_SESSION["message_create"]=$message;
							$db->rollBack();
							return false;
							
						}
					}
	
?>