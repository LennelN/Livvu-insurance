
<?php

session_start();
$response = array();
$response_back = array();
$file_name='enter_mobile_operator';
require_once("./include/membersite_config.php");
include("./include/functions.php");


$name_of_mobile_operator = trim($_POST["name_of_mobile_operator"]);
$username = trim($_SESSION["user_name"]);
$contact_phone = trim($_POST["contact_phone"]);
$physical_address = trim($_POST["physical_address"]);
$country_of_operation = $_POST["country_of_operation"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");

	try{

		$stmt = $db->prepare("INSERT INTO tbl_mobile_operators (
									name_of_mobile_operator,
									country_of_operation,
									contact_phone,
									physical_address) 
    						VALUES 																			(:name_of_mobile_operator,:country_of_operation,:contact_phone,:physical_address)");
    									$stmt->bindParam(':name_of_mobile_operator', $name_of_mobile_operator);
									$stmt->bindParam(':country_of_operation', $country_of_operation);
									$stmt->bindParam(':contact_phone', $contact_phone);
									$stmt->bindParam(':physical_address', $physical_address);
									
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Entered Mobile Operator.";
										$_SESSION["message_create"]=$message;
										$db->commit();
										return true;		
								
									}
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Mobile Operator ".$name_of_mobile_operator. " For Country: ".$country_of_operation." Already Exist In The System. Please Check Again.";
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