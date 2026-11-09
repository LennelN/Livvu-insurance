<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='update_cashed_out_points';
require_once("./include/membersite_config.php");
include("./include/functions.php");
  
	$username = $_SESSION["user_name"];
 
	$user_deactivated_id = $_POST["user_deactivated_id"];
 
   	$reason_deactivated = $_POST["reason_deactivated"];

	$user_country_deactivated = $_POST["user_country_deactivated"];



	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

	$sql= "SELECT user_login_id FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $user_country_deactivated, PDO::PARAM_STR); 
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

					
							$sql = "UPDATE tbl_users SET user_active = 'N',date_deactivated = '$todayDate', reason_deactivated = '$reason_deactivated',user_deactivated_by = '$username',user_deactivated='Y'  WHERE user_login_id = :field_val AND country = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $user_country_deactivated, PDO::PARAM_STR); 
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{
								$success = "true";
								$message = "You Have Successfully Deactivated User Id.".$user_deactivated_id." Thank You.";
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
								$message = "User Id.".$user_deactivated_id." Does Not Exist In The System. Please Check User Id.";
								$_SESSION["message_create"]=$message;
								return false;

			}				

?>
