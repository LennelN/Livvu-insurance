<?php
    
session_start();
$response = array();
require_once("./include/membersite_config.php");
include("./include/functions.php");
include("./include/config.php");

	$merchant_phone_number = $_SESSION["merchant_phone_number"];
	$merchant_code = $_SESSION["merchant_code"];
	$user_login_id = $_POST["user_login_id_to_update"];
 
	
	$date_user_deactivated = NULL;

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($user_login_id);
	

						try
						{
								
							$sql = "UPDATE tbl_merchant_users SET user_active = 'Y',user_deactivated = 'N',date_deactivated=NULL,reason_deactivated = NULL,user_deactivated_by=NULL WHERE user_login_id = :field_val AND merchant_phone_number = :field_val1 AND merchant_code = :field_val2";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR);
							$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR);
							
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{
								$success = "true";
								$message = "You Have Successfully Activated User Login ID ".$user_login_id;
								$_SESSION['message_create']=$message;	
							}
							$db->commit();
						}catch(PDOException $e){
					
								if ($e->errorInfo[1] == 1062) 
									{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='duplicate';
		 								//handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										$_SESSION['message_create']=$message;	
								
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='exception';
		 								//handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										$_SESSION['message_create']=$message;	
									}
						}//catch
				


?>
