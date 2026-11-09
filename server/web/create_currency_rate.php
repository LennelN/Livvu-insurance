
<?php

session_start();
$response = array();
$response_back = array();
$file_name='create_ancillary_tariff';
require_once("./include/membersite_config.php");
include("./include/functions.php");


$currency_type = trim($_POST["currency_type"]);
$username = trim($_SESSION["user_name"]);
$no_of_points = trim($_POST["no_of_points"]);
$currency_rate = trim($_POST["currency_rate"]);
$currency_country = $_POST["currency_country"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");

	try{

		$stmt = $db->prepare("INSERT INTO tbl_currency_rates (
									country_currency,
									type_of_currency,
									amount,
									no_of_points,
									currency_created_by) 
    						VALUES 																			(:country_currency,:type_of_currency,:amount,:no_of_points,:currency_created_by)");
    									$stmt->bindParam(':country_currency', $currency_country);
									$stmt->bindParam(':type_of_currency', $currency_type);
									$stmt->bindParam(':amount', $currency_rate);
									$stmt->bindParam(':no_of_points', $no_of_points);
									$stmt->bindParam(':currency_created_by', $username);
    									
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Created Currency Rate.";
										$_SESSION["message_create"]=$message;
										$db->commit();
										return true;
									}
							
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Code For Country: ".$currency_country." Already Exist In The System. Please Check Again.";
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