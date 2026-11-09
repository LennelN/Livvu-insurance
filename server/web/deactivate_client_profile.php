<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='deactivate_client_profile';
require_once("./include/membersite_config.php");
include("./include/functions.php");

	$username = $_SESSION["user_name"];
 
	$user_deactivated_id = $_POST["user_deactivated_id"];
 
   	$reason_deactivated = $_POST["reason_deactivated"];
	$client_country_deactivated = $_POST["client_country_deactivated"];
	date_default_timezone_set('Africa/Harare');
	$time_deactivated = date('H:i:s');

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

	$sql= "SELECT phone_number FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $client_country_deactivated, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{
							  
						$db->beginTransaction();
					
						try
						{

							$dt   = new DateTime();
							//$date_paid_out = $dt->createFromFormat('d/m/Y', $deactivated_date);
							//$newDate = $date_paid_out->format('Y-m-d');
							$todayDate = date('Y-m-d');
					
							$sql = "UPDATE tbl_clients SET member_active = 'N' WHERE phone_number = :field_val AND country_name = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $client_country_deactivated, PDO::PARAM_STR); 
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{

								$stmt = $db->prepare("INSERT INTO tbl_client_deactivation_details(phone_number, date_deactivated,time_deactivated,reason_deactivated,client_deactivated_by,country) VALUES (:phone_number, :date_deactivated, :time_deactivated, :reason_deactivated, :client_deactivated_by,:country)");

								$stmt->bindParam(':phone_number', $user_deactivated_id);
								$stmt->bindParam(':date_deactivated', $todayDate);
								$stmt->bindParam(':time_deactivated', $time_deactivated);
								$stmt->bindParam(':reason_deactivated', $reason_deactivated);
								$stmt->bindParam(':client_deactivated_by', $username);
								$stmt->bindParam(':country', $client_country_deactivated);
										
								$stmt->execute();
								$total =  $stmt->rowCount();
	
								if($total > 0){
									$success = "true";
									$message = "You Have Successfully Deactivated Client Phone Number.".$user_deactivated_id." Thank You.";
									$_SESSION["message_create"]=$message;
									$db->commit();
									return true;
								}
							}
							//$db->commit();
						}catch(PDOException $e){
					
								if ($e->errorInfo[1] == 1062) 
									{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
										$error_message=$e->getMessage();
										$_SESSION["message_create"]=$error_message;
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
								$message = "Client Phone Number .".$user_deactivated_id." Does Not Exist In The System. Please Check Phone Number And Try Again.";
								$_SESSION["message_create"]=$message;
								return false;

					}	
			

?>
