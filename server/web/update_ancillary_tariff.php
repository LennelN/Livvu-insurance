
<?php
session_start();
$response = array();
$response_back = array();
$file_name='update_ancillary_tariff';
require_once("./include/membersite_config.php");
include("./include/functions.php");

$tariff_code = trim($_POST["tariff_code"]);
$username = trim($_SESSION["user_name"]);
$tariff_description = trim($_POST["tariff_description"]);
$tariff_category = trim($_POST["tariff_category"]);
$tariff_price = trim($_POST["tariff_price"]);
$tariff_type = $_POST["tariff_type"];
$tariff_country = $_SESSION["selected_country"];


	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	$ddate = Date("Y-m-d h:i:s");

	try{

			$sql= "UPDATE tbl_ancillary_tariffs SET tariff_description = '$tariff_description',tariff_price = '$tariff_price',tariff_category = '$tariff_category',tariff_type = '$tariff_type',tariff_processed_by='$username' WHERE tariff_code = :field_val AND tariff_country = :field_val1 "; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $tariff_code, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $tariff_country, PDO::PARAM_STR);
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$success = "true";
						$message = "You Have Successfully Updated Tariff Code ".$tariff_code." Thank You ";
						$_SESSION["message_create"]=$message;
						$db->commit();
						return true;	
					}else{
						
						$success = "false";
						$message = "Tariff Code ".$tariff_code. " Was Not Updated. Please Check And Try Again.";
						$_SESSION["message_create"]=$message;
						return false;
					}
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Update A Duplicate Value";
							//$message = $e->getMessage();
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							$error_message=$e->getMessage();
							$_SESSION["message_create"]=$message;
							$db->rollBack();
							return false;
						}else{	
	
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later ");
							$message=$e->getMessage();
							$_SESSION["message_create"]=$message;
							$db->rollBack();
							return false;
							
						}
					}
?>