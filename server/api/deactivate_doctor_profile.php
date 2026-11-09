<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='deactivate_doctor_profile';
//error_log(strip_tags("comingin".$file_name), 0);
// check for required fields
if (isset($_POST['username']) && isset($_POST['doctor_deactivate_phone']) && isset($_POST['reason_deactivated']) && isset($_POST['doctor_deactivate_name']) && isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code']) && isset($_POST['country'])){

	$username = $_POST["username"];
 
	$doctor_deactivate_phone = $_POST["doctor_deactivate_phone"];
 
   	$reason_deactivated = $_POST["reason_deactivated"];

	$doctor_deactivate_name = $_POST["doctor_deactivate_name"];
	
	$merchant_phone_number = $_POST["merchant_phone_number"];
	$merchant_code = $_POST["merchant_code"];
	$country = $_POST["country"];
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

	$sql= "SELECT phone_number,fullname,dob,idno,gender,profession,registration_no FROM tbl_doctor_details WHERE phone_number = :field_val AND doctor_active = 'Y' AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $doctor_deactivate_phone, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0){
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    							
								$fullname = $row['fullname']; 
								$dob = $row['dob'];
								$idno = $row['idno'];
								$gender = $row['gender'];
								$profession = $row['profession'];
								$registration_no = $row['registration_no'];
								
							}
		
					$db->beginTransaction();

					//Check whether the phone has already been registered as a merchant
						try
						{

							$dt   = new DateTime();
							$todayDate = date('Y-m-d');
					
							$sql = "UPDATE tbl_doctor_details SET doctor_active = 'N',date_deactivated = '$todayDate', reason_deactivated = '$reason_deactivated',doctor_deactivated_by = '$username'  WHERE phone_number = :field_val AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2";
							$stmt_update1 = $db->prepare($sql);
							$stmt_update1->bindParam(':field_val', $doctor_deactivate_phone, PDO::PARAM_STR);
							$stmt_update1->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt_update1->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR);  
							// execute the query
							$stmt_update1->execute();
							$total_update1 =  $stmt_update1->rowCount();
							if($total_update1 > 0 )//update 1 after
							{

								$stmt_update = $db->prepare("INSERT INTO tbl_doctor_deactivation_details (
									phone_number,
									fullname,
                									dob,
									idno,
									gender,
                									profession,
									registration_no,
									date_deactivated,
									merchant_phone_number,
									merchant_code,
									reason_deactivated, doctor_deactivated_by,country) 
    									VALUES (:phone_number,:fullname,:dob,:idno,:gender,:profession,:registration_no,
									:date_deactivated,
									:merchant_phone_number,
									:merchant_code,
									:reason_deactivated, :doctor_deactivated_by,:country)");

    									$stmt_update->bindParam(':phone_number', $doctor_deactivate_phone);
									$stmt_update->bindParam(':fullname', $fullname);
									$stmt_update->bindParam(':dob', $dob);
									$stmt_update->bindParam(':idno', $idno);
    									$stmt_update->bindParam(':gender', $gender);
									$stmt_update->bindParam(':profession', $profession);
									$stmt_update->bindParam(':registration_no',$registration_no);
									$stmt_update->bindParam(':date_deactivated', $todayDate);
									$stmt_update->bindParam(':merchant_phone_number', $merchant_phone_number);
									$stmt_update->bindParam(':merchant_code', $merchant_code);
									$stmt_update->bindParam(':reason_deactivated', $reason_deactivated);
									$stmt_update->bindParam(':doctor_deactivated_by', $username);
									$stmt_update->bindParam(':country', $country);
									
									$stmt_update->execute();
	
									$total_update2 =  $stmt_update->rowCount();
	
									if($total_update2 > 0)
									{

										$success = "true";
										$message = "You Have Successfully Deactivated Doctor.".$doctor_deactivate_name." Thank You.";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									}
							}
							$db->commit();
						}catch(PDOException $e){
					
								if ($e->errorInfo[1] == 1062) 
									{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
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
			}else{

								$success = "false";
								$message = "Doctor ".$doctor_deactivate_name." Does Not Exist In The System. Please Check Phone Number.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);


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
