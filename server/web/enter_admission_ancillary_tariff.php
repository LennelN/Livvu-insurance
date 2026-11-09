
<?php

session_start();
$response = array();
$response_back = array();
$file_name='enter_admission_ancillary_tariff';
require_once("./include/membersite_config.php");
include("./include/functions.php");

//error_log(strip_tags("coming in "), 0);
$tariff_code = trim($_POST["tariff_code"]);
$tariff_country = $_POST["tariff_country"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");
	$qry = "Select tariff_code from tbl_ancillary_tariffs WHERE tariff_code = '$tariff_code'";
	$stmt = $db->prepare($qry);
        $stmt->execute();
	$total = $stmt->rowCount();
	$num=0;
	if($total > 0){


				try{

					$stmt = $db->prepare("INSERT INTO tbl_country_admissions_tariff_codes (
								country_name,
								admission_tariff_code) 
    								VALUES 																			(:country_name,:admission_tariff_code)");
    							$stmt->bindParam(':country_name', $tariff_country);
							$stmt->bindParam(':admission_tariff_code', $tariff_code);
									
										
							$stmt->execute();
	
							$total =  $stmt->rowCount();
	
							if($total > 0)
							{
								$success = "true";
								$message = "Congratulations You Have Successfully Entered Admission Ancillary Tariff Code.";
								$_SESSION["message_create"]=$message;
								$db->commit();
								return true;		
								
							}
							
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Code: ".$tariff_code." Already Exist In The System As An Admission Code. Please Check Again.";
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
		}else{

			$success = "false";
			$message = "Code: ".$tariff_code." Does Not Exist In The System As An Admission Code. Please Check Again.";
			$_SESSION["message_create"]=$message;
			$db->commit();
			return false;

		}
	
?>