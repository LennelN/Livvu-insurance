
<?php

session_start();
$response = array();
$response_back = array();
$file_name='delete_ancillary_tariff';
require_once("./include/membersite_config.php");
include("./include/functions.php");

//error_log(strip_tags("coming in "), 0);
$tariff_code = trim($_POST["tariff_code"]);
$username = trim($_SESSION["user_name"]);
$tariff_country = $_SESSION["selected_country"];


	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	$ddate = Date("Y-m-d h:i:s");


	
				try{
			
					
							$sql = "DELETE from tbl_ancillary_tariffs WHERE tariff_code = :field_val AND tariff_country = :field_val1 "; 
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $tariff_code, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $tariff_country, PDO::PARAM_STR);
							$stmt->execute();
							$total = $stmt->rowCount();
							if($total > 0){
								$success = "true";
								$message = "You Have Successfully Deleted Tariff Code ".$tariff_code." From The System ";
								$_SESSION["message_create"]=$message;
								$db->commit();
								return true;	
							}
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Insert A Duplicate Value";
							//$message = $e->getMessage();
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
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