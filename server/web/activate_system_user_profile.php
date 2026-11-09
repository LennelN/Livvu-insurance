<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='activate_system_user_profile';
require_once("./include/membersite_config.php");
include("./include/functions.php");
  
	$username = $_SESSION["user_name"];
 
	$user_activated_id = $_POST["user_login_id_to_update"];
 
$user_country_activated = $_POST["user_country_activated"];
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

	$sql= "SELECT user_login_id FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_activated_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $user_country_activated, PDO::PARAM_STR); 
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

							$sql = "UPDATE tbl_users SET user_active = 'Y',user_deactivated = 'N',date_deactivated=NULL,reason_deactivated = NULL,user_deactivated_by=NULL WHERE user_login_id = :field_val AND country = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_activated_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $user_country_activated, PDO::PARAM_STR); 
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{
								$success = "true";
								$message = "You Have Successfully Activated User Id.".$user_activated_id." Thank You.";
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
								$message = "User Id.".$user_activated_id." Does Not Exist In The System. Please Check User Id.";
								$_SESSION["message_create"]=$message;
								return false;

			}				

?>
