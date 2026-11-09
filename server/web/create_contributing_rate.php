
<?php

session_start();
$response = array();
$response_back = array();
$file_name='create_ancillary_tariff';
require_once("./include/membersite_config.php");
include("./include/functions.php");

$id_insert=0;
$username = trim($_SESSION["user_name"]);
$contributing_rate = trim($_POST["country_rate"]);
$contributing_country = $_POST["contributing_country"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");

	try{

		$stmt = $db->prepare("INSERT INTO tbl_year_country_contributions(
									id,
									amount,
									country,
									processed_by) 
    						VALUES 																			(:id,:amount,:country,:processed_by)");
    									$stmt->bindParam(':id', $id_insert);
									$stmt->bindParam(':amount', $contributing_rate);
									$stmt->bindParam(':country', $contributing_country);
									$stmt->bindParam(':processed_by', $username);
    									
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Created Contributing Year Rate For Country ".$contributing_country;
										$_SESSION["message_create"]=$message;
										$db->commit();
										return true;
									}
							
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Countributing Year Rate For Country: ".$contributing_country." Already Exist In The System. Please Check Again.";
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