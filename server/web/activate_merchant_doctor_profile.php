<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='activate_merchant_doctor_profile';
require_once("./include/membersite_config.php");
include("./include/functions.php");

	$username = $_SESSION["user_name"];
 
	$doctor_deactivated_phone_no = $_POST["doctor_deactivated_phone_no"];
	$doctor_name = $_POST["doctor_name"];
	$country=$_SESSION["merchant_country"];
	$merchant_phone_number=$_SESSION["merchant_phone_number"];
	$merchant_code=$_SESSION["merchant_code"];
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

	$sql= "SELECT phone_number FROM tbl_doctor_details WHERE phone_number = :field_val AND doctor_active = 'N' AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $doctor_deactivated_phone_no, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0){

					$db->beginTransaction();

					
						try
						{

							$dt   = new DateTime();
							//$date_paid_out = $dt->createFromFormat('d/m/Y', $deactivated_date);
							//$newDate = $date_paid_out->format('Y-m-d');
							$todayDate = date('Y-m-d');
					
							$sql2 = "UPDATE tbl_doctor_details SET doctor_active = 'Y',date_deactivated = '$todayDate', reason_deactivated = NULL,doctor_deactivated_by = NULL  WHERE phone_number = :field_val AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2";
							$stmt2 = $db->prepare($sql2);
							$stmt2->bindParam(':field_val', $doctor_deactivated_phone_no, PDO::PARAM_STR); 
							$stmt2->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt2->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR); 
							// execute the query
							$stmt2->execute();
							$total_update =  $stmt2->rowCount();

							if($total_update > 0 )//update 1 after
							{

								$success = "true";
								$message = "You Have Successfully Activated Doctor .".$doctor_name." Thank You.";
								$_SESSION["message_create"]=$message;
								$db->commit();
								return true;
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
