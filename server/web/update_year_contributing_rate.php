<?php
   
session_start();
$response = array();
$response_back = array();
$file_name='update_year_contributing_rate';
require_once("./include/membersite_config.php");
include("./include/functions.php");


	$country_selected = $_SESSION["selected_country_contribution"];
	$contributing_amount= $_POST["contributing_amount"];
	$country= $_POST["contributing_country"];
	$username = trim($_SESSION["user_name"]);

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	$ddate = Date("Y-m-d h:i:s");

	try{

			$sql= "UPDATE tbl_year_country_contributions SET amount = $contributing_amount WHERE country = :field_val"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
					
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$success = "true";
						$message = "You Have Successfully Updated Year Contributing Rate For Country ".$country." Thank You ";
						$_SESSION["message_create"]=$message;
						$db->commit();
						return true;	
					}else{
						
						$success = "false";
						$message = "Contributing Rate For Country ".$country." Was Not Updated. Please Check And Try Again.";
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
