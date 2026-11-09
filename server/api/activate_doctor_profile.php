<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='activate_doctor_profile';
//error_log(strip_tags("comingin".$file_name), 0);
// check for required fields
if (isset($_POST['username']) && isset($_POST['doctor_deactivate_phone']) && isset($_POST['doctor_deactivate_name']) && isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code']) && isset($_POST['country'])){

	$username = $_POST["username"];
 
	$doctor_deactivate_phone = $_POST["doctor_deactivate_phone"];


	$doctor_deactivate_name = $_POST["doctor_deactivate_name"];
	
	$merchant_phone_number = $_POST["merchant_phone_number"];
	$merchant_code = $_POST["merchant_code"];
	$country = $_POST["country"];
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

	$sql= "SELECT phone_number,fullname,dob,idno,gender,profession,registration_no FROM tbl_doctor_details WHERE phone_number = :field_val AND doctor_active = 'N' AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $doctor_deactivate_phone, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0){

		
					$db->beginTransaction();

					//Check whether the phone has already been registered as a merchant
						try
						{

							$dt   = new DateTime();
							$todayDate = date('Y-m-d');
					
							$sql = "UPDATE tbl_doctor_details SET doctor_active = 'Y',date_deactivated = '$todayDate', reason_deactivated = NULL,doctor_deactivated_by = NULL  WHERE phone_number = :field_val AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2";
							$stmt_update1 = $db->prepare($sql);
							$stmt_update1->bindParam(':field_val', $doctor_deactivate_phone, PDO::PARAM_STR);
							$stmt_update1->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt_update1->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR);  
							// execute the query
							$stmt_update1->execute();
							$total_update1 =  $stmt_update1->rowCount();
							if($total_update1 > 0 )//update 1 after
							{

										$success = "true";
										$message = "You Have Successfully Activated Doctor.".$doctor_deactivate_name." Thank You.";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
									
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
