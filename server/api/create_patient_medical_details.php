
<?php


$response = array();
include 'functions.php';
$file_name='create_ancillary_tariff';
$purchase_details = array();

if (isset($_POST['phone_number']) && isset($_POST['body_temperature']) && isset($_POST['blood_pressure']) && isset($_POST['body_pulse']) && isset($_POST['known_allergies']) && isset($_POST['blood_type']) && isset($_POST['other_details']) && isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code']) && isset($_POST['merchant_name']) && isset($_POST['record_processed_by']) && ($_POST["country_name_client"])){

$phone_number = trim($_POST["phone_number"]);
$body_temperature = trim($_POST["body_temperature"]);
$bp_level = trim($_POST["blood_pressure"]);
$body_pulse = trim($_POST["body_pulse"]);
$body_allergies = trim($_POST["known_allergies"]);
$blood_type = $_POST["blood_type"];
$other_details = $_POST["other_details"];
$merchant_phone_number = $_POST["merchant_phone_number"];
$merchant_code = $_POST['merchant_code'];
$merchant_name = $_POST["merchant_name"];
$record_processed_by = $_POST["record_processed_by"];
$country_name_client=$_POST["country_name_client"];
$len_body_allergies = strlen($body_allergies);
$len_blood_type = strlen($blood_type);
$len_other_details = strlen($other_details);
$transaction_id_insert=0;
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


						$stmt = $db->prepare("INSERT INTO tbl_patient_medical_history(
										transaction_id,
										date_record_created, 
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
									$stmt->bindParam(':country', $country_name_client);
									$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);
									$stmt->bindParam(':merchant_code', $merchant_code);
									$stmt->bindParam(':merchant_name', $merchant_name);
    									$stmt->bindParam(':body_temperature', $body_temperature);
									$stmt->bindParam(':blood_pressure', $bp_level);
									$stmt->bindParam(':body_pulse', $body_pulse);
									$stmt->bindParam(':body_allergies', $body_allergies);
									$stmt->bindParam(':blood_type', $blood_type);
    									$stmt->bindParam(':other_details', $other_details);
									$stmt->bindParam(':record_processed_by', $record_processed_by);
										
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Recorded The Patient Medical Details.";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);			
								
									}
							$db->commit();
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$message=$e->getMessage();
							$success = "false";
							$error_type='duplicate';
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later Duplicate");
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							$db->rollBack();
						
						}else{	
	
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later ");
							$message=$e->getMessage();
							error_log(strip_tags($message), 0);
							$error_type='exception';
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							//echo json_encode($response);
							$db->rollBack();
							
						}
					}

} else {
    // required field is missing

    	$success = "false";
    	$message = "Required field(s) Are Missing...Please Complete All Fields";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    	echo json_encode($response);
}



	
?>