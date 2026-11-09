
<?php

session_start();
$response = array();
$response_back = array();
$file_name='create_patient_medical_details';
require_once("./include/membersite_config.php");
include("./include/functions.php");

//error_log(strip_tags("coming in "), 0);

$len_body_allergies = 0;
$len_blood_type = 0;
$len_other_details = 0;
$transaction_id_insert=0;
$body_temperature = trim($_POST["body_temperature"]);
$username = $_SESSION["user_name"];
$country = trim($_POST["nameofcountry"]);
$bp_level = trim($_POST["bp_level"]);
$body_pulse = trim($_POST["body_pulse"]);
$body_allergies = trim($_POST["known_allergies"]);
$blood_type = $_POST["blood_type"];
$other_details = $_POST["other_details"];
$phone_number = trim($_POST["phone_number"]);
$merchant_code = ($_SESSION['merchant_code']);
$merchant_phone_number = ($_SESSION["merchant_phone_number"]);
$merchant_name = ($_SESSION["merchant_name"]);

$len_body_allergies = strlen($body_allergies);
$len_blood_type = strlen($blood_type);
$len_other_details = strlen($other_details);

if($len_body_allergies == 0)
{
$body_allergies = "Not Available";
}

if($len_blood_type == 0)
{
$blood_type = "Not Available";
}

if($len_other_details == 0)
{
$other_details = "Not Available";
}


	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	$date_record_created = date('Y-m-d H:i:s');
	$ddate = Date("Y-m-d h:i:s");


					try{


						$stmt = $db->prepare("INSERT INTO tbl_patient_medical_history(transaction_id,date_record_created, 
										phone_number,
										country, 
										merchant_phone_number, 
										merchant_code, 
										merchant_name, 
										body_temperature, 
										blood_pressure, 
										body_pulse, 
										body_allergies, 
										blood_type, 
										other_details, 
										record_processed_by) 
							VALUES (:transaction_id,:date_record_created, :phone_number, :country, :merchant_phone_number, :merchant_code, :merchant_name, :body_temperature, :blood_pressure, :body_pulse, :body_allergies, :blood_type, :other_details, :record_processed_by)");

    									$stmt->bindParam(':transaction_id', $transaction_id_insert);
    									$stmt->bindParam(':date_record_created', $date_record_created);
									$stmt->bindParam(':phone_number', $phone_number);
									$stmt->bindParam(':country', $country);
									$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);
									$stmt->bindParam(':merchant_code', $merchant_code);
									$stmt->bindParam(':merchant_name', $merchant_name);
    									$stmt->bindParam(':body_temperature', $body_temperature);
									$stmt->bindParam(':blood_pressure', $bp_level);
									$stmt->bindParam(':body_pulse', $body_pulse);
									$stmt->bindParam(':body_allergies', $body_allergies);
									$stmt->bindParam(':blood_type', $blood_type);
    									$stmt->bindParam(':other_details', $other_details);
									$stmt->bindParam(':record_processed_by', $username);
										
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Recorded The Patient Medical Details.";
										$_SESSION["message_create"]=$message;
										$db->commit();
										return true;		
								
									}
							
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							$message = "Duplicate Error Please Check Again.";
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