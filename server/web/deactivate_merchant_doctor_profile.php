<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='deactivate_merchant_doctor_profile';
require_once("./include/membersite_config.php");
include("./include/functions.php");
//error_log(strip_tags("now entering "), 0);
	$username = $_SESSION["user_name"];
	$doctor_deactivated_phone_no = $_POST["doctor_deactivated_phone_no"];
	$doctor_name = $_POST["doctor_name"];
 
   	$reason_deactivated = $_POST["reason_deactivated"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$country=$_SESSION["merchant_country"];
	$merchant_phone_number=$_SESSION["merchant_phone_number"];
	$merchant_code=$_SESSION["merchant_code"];

    	//$db = new DB_CONNECT();

	$sql= "SELECT phone_number,fullname,dob,idno,gender,profession,registration_no FROM tbl_doctor_details WHERE phone_number = :field_val AND doctor_active = 'Y' AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $doctor_deactivated_phone_no, PDO::PARAM_STR); 
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
					
						try
						{

							$dt   = new DateTime();
							$todayDate = date('Y-m-d');
					
							$sql2 = "UPDATE tbl_doctor_details SET doctor_active = 'N',date_deactivated = '$todayDate', reason_deactivated = '$reason_deactivated',doctor_deactivated_by = '$username'  WHERE phone_number = :field_val AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2";
							$stmt2 = $db->prepare($sql2);
							$stmt2->bindParam(':field_val', $doctor_deactivated_phone_no, PDO::PARAM_STR); 
							$stmt2->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt2->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR); 
							// execute the query
							$stmt2->execute();
							$total_update2 =  $stmt2->rowCount();
							if($total_update2 > 0 )//update 1 after
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

    									$stmt_update->bindParam(':phone_number', $doctor_deactivated_phone_no);
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
	
									$total_stmt_update =  $stmt_update->rowCount();
	
									if($total_stmt_update > 0)
									{

										$success = "true";
										$message = "You Have Successfully Deactivated Doctor .".$doctor_name." Thank You.";
										$_SESSION["message_create"]=$message;
										$db->commit();
										return true;
									}
							}
							
						}catch(PDOException $e){
					
								if ($e->errorInfo[1] == 1062) 
									{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
										$error_message=$e->getMessage();
		 								$_SESSION["message_create"]=$message;
										$db->rollBack();
										return false;
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$_SESSION["message_create"]=$error_message;
										$db->rollBack();
										return false;
									}
						}//catch
			}else{
								$success = "false";
								$message = "Doctor Name.".$doctor_name." Does Not Exist In The System. Please Check And Try Again.";
								$_SESSION["message_create"]=$message;
								return false;

			}				

?>
