<?php
   
session_start();
$response = array();
$response_back = array();
$file_name='update_currency_rate';
require_once("./include/membersite_config.php");
include("./include/functions.php");


	$country_currency = $_SESSION["selected_country"];
	$currency_type= $_POST["currency_type"];
	$currency_amount= $_POST["currency_amount"];
	$no_of_points= $_POST["no_of_points"];
	$username = trim($_SESSION["user_name"]);

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	$ddate = Date("Y-m-d h:i:s");

	try{

			$sql= "UPDATE tbl_currency_rates SET type_of_currency = '$currency_type',amount = '$currency_amount',no_of_points = '$no_of_points',currency_created_by='$username' WHERE country_currency = :field_val"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $country_currency, PDO::PARAM_STR); 
					
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$success = "true";
						$message = "You Have Successfully Updated Currency Rate Details For Country ".$country_currency." Thank You ";
						$_SESSION["message_create"]=$message;
						$db->commit();
						return true;	
					}else{
						
						$success = "false";
						$message = "Currency Code ".$currency_type. " For Country ".$currency_country." Was Not Updated. Please Check And Try Again.";
						$_SESSION["message_create"]=$message;
						$db->rollBack();
						return false;
					}

							
								$db->commit();
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Update A Duplicate Value";
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
