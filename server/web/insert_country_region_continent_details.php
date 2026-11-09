<?php 
session_start();
$response = array();
$response_back = array();
$file_name='insert_country_region_continent_details';
require_once("./include/membersite_config.php");
include("./include/functions.php");

	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';
	
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	//Check whether the phone number being billed exists and its active
 		
		$country_region = $_POST["country_region"];
		$country_name = $_POST["country_name"];

			try
			{
				$db->beginTransaction();
				$stmt = $db->prepare("INSERT INTO tbl_country_regions (
									name_of_country,
									name_of_region)  
						VALUES 																						(:name_of_country,:name_of_region)");
							$stmt->bindParam(':name_of_country', $country_name);
    							$stmt->bindParam(':name_of_region', $country_region);
							$stmt->execute();
	
							$total =  $stmt->rowCount();
	
							if($total > 0)
							{
								$success = "true";
								$message = "Congratulations You Have Successfully Entered The Country Region Details.";
								$_SESSION["message_create"]=$message;
								$db->commit();
								return true;		
								
							}
							
					$db->commit();
									
				}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Region: ".$country_region." And Country ".$country_name." Already Exist In The System. Please Check Again.";
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