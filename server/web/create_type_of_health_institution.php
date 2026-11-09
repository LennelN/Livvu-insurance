
<?php

session_start();
$response = array();
$response_back = array();
$file_name='insert_country_details';
require_once("./include/membersite_config.php");
include("./include/functions.php");

//error_log(strip_tags("coming in "), 0);
$type_of_institution = trim($_POST["type_of_institution"]);

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");

	try{

		$stmt = $db->prepare("INSERT INTO tbl_health_descriptions (type_of_institution) 
    						VALUES 																			(:type_of_institution)");
    					$stmt->bindParam(':type_of_institution', $type_of_institution);
										
					$stmt->execute();
	
					$total =  $stmt->rowCount();
	
					if($total > 0)
							{
								$success = "true";
								$message = "Congratulations You Have Successfully Entered Type Of Institution.";
								$_SESSION["message_create"]=$message;
								$db->commit();
								return true;		
								
							}
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Type: ".$type_of_institution." Already Exist In The System. Please Check Again.";
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