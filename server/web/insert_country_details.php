<?php 
session_start();
$response = array();
$response_back = array();
$file_name='insert_country_details';
require_once("./include/membersite_config.php");
include("./include/functions.php");

	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';
	
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	
 		
		$country_name = $_POST["country_name"];
		$country_code = $_POST["country_code"];
		$no_phone_digits=$_POST["no_phone_digits"];
		$country_continent = $_POST["country_continent"];
		$country_region = $_POST["country_region"];

			try
			{
				$db->beginTransaction();
				$stmt = $db->prepare("INSERT INTO tbl_country_codes (
									country_name,
									country_code,
                							no_phone_digits,
									region,
									continent)   
						VALUES 																						(:country_name,:country_code,:no_phone_digits,:region,:continent)");
    							$stmt->bindParam(':country_name', $country_name);
							$stmt->bindParam(':country_code', $country_code);
							$stmt->bindParam(':no_phone_digits', $no_phone_digits);
							$stmt->bindParam(':region', $country_region);
							$stmt->bindParam(':continent', $country_continent);
							$stmt->execute();
	
							$total =  $stmt->rowCount();
	
							if($total > 0)
							{

								$stmt = $db->prepare("INSERT INTO tbl_region_continent (
									name,
									region,
									continent)  
									VALUES 																						(:name,:region,:continent)");
										$stmt->bindParam(':name', $country_name);
    										$stmt->bindParam(':region', $country_region);
										$stmt->bindParam(':continent', $country_continent);
										$stmt->execute();
	
										$total =  $stmt->rowCount();
	
										if($total > 0)
										{
											$success = "true";
											$message = "Congratulations You Have Successfully Entered The Country Details.";
											$_SESSION["message_create"]=$message;
											$db->commit();
											return true;		
								
										}
							}
							
					$db->commit();
									
				}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Country: ".$country_name." Already Exist In The System. Please Check Again.";
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