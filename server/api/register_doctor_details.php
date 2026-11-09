<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='register_doctor_details';
//sset($_POST['phone_no'])='0772455025';
//error_log(strip_tags("comingin"), 0);
// check for required fields
if (isset($_POST['name']) && isset($_POST['surname']) && isset($_POST['idno']) && isset($_POST['phone_no']) && isset($_POST['dob']) && isset($_POST['gender']) && isset($_POST['profession']) && isset($_POST['regno']) && isset($_POST['country']) && isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code']) && isset($_POST['user_created_by'])){

	$name = $_POST["name"];
    	$surname = $_POST["surname"];
	$idno = $_POST["idno"];
	$phone_no = $_POST["phone_no"];
	$dob = $_POST["dob"];
	$gender = $_POST["gender"]; 
	$profession = $_POST["profession"];
	$regno = $_POST["regno"];
	$country = $_POST["country"];
	$merchant_phone_number = $_POST["merchant_phone_number"];
	$merchant_code = $_POST["merchant_code"];
	$user_created_by = $_POST["user_created_by"];
	$fullname=$name." ".$surname;

	$dt   = new DateTime();
	$date = $dt->createFromFormat('d/m/Y', $dob);
	$newDate = $date->format('Y-m-d');

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$transaction_id_insert = 0;
    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($phone_no);
					//Check whether the phone has already been registered as a merchant
						try 
							{
							
								$date_registered = date('Y-m-d');
								$doctor_active = 'Y';
								
								
								$stmt = $db->prepare("INSERT INTO tbl_doctor_details (transaction_id,
									phone_number,
									name,
                									surname,
									fullname,
                									dob,
									idno,
									gender,
                									profession,
									registration_no,
									doctor_active,
									country,
									date_joined,
									date_deactivated,
									merchant_phone_number,
									merchant_code) 
    									VALUES 																		(:transaction_id,:phone_number,:name,:surname,:fullname,:dob,:idno,:gender,:profession,:registration_no,
									:doctor_active,
									:country,
									:date_joined,
									:date_deactivated,
									:merchant_phone_number,
									:merchant_code)");

    									$stmt->bindParam(':transaction_id', $transaction_id_insert);
    									$stmt->bindParam(':phone_number', $phone_no);
									$stmt->bindParam(':name', $name);
									$stmt->bindParam(':surname', $surname);
									$stmt->bindParam(':fullname', $fullname);
									$stmt->bindParam(':dob', $newDate);
									$stmt->bindParam(':idno', $idno);
    									$stmt->bindParam(':gender', $gender);
									$stmt->bindParam(':profession', $profession);
									$stmt->bindParam(':registration_no', $regno);
									$stmt->bindParam(':doctor_active', $doctor_active);
									$stmt->bindParam(':country', $country);
									$stmt->bindParam(':date_joined', $date_registered);
									$stmt->bindParam(':date_deactivated', $date_registered);
									$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);
									$stmt->bindParam(':merchant_code', $merchant_code);
									
									
									$stmt->execute();
	
									$total =  $stmt->rowCount();

				
				if($total > 0)
				{
					$success = "true";
					$message = "Congratulations You Have Successfully Registered The Doctor.";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
				}
		
				$db->commit();
			}catch(PDOException $e){
					
				if ($e->errorInfo[1] == 1062) 
				{
					$success = "false";
					$error_message=$e->getMessage();
		 			$message = "Error Entering Duplicate value ".$error_message;
		 			$error_type='duplicate';
		 			handler($error_message,$file_name,$success,$error_type);
		 			$db->rollBack();
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);

								
				}else{
					$success = "false";
		 			$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 			$error_message=$e->getMessage();
		 			$error_type='exception';
		 			handler($error_message,$file_name,$success,$error_type);
		 			$db->rollBack();
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
				}
			}//catch
} else {
    // required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}


?>
