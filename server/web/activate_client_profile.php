<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='activate_client_profile';
require_once("./include/membersite_config.php");
include("./include/functions.php");

	$username = $_SESSION["user_name"];
 
	$user_login_id = $_POST["user_login_id_to_update"];
 $client_country_activated = $_POST["client_country_activated"];
	date_default_timezone_set('Africa/Harare');
	$time_activated = date('H:i:s');
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($user_login_id);

			$sql= "SELECT phone_number FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $client_country_activated, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{	  
					
						try
						{
							$todayDate = date('Y-m-d');
								
							$sql = "UPDATE tbl_clients SET member_active = 'Y' WHERE phone_number = :field_val AND country_name = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $client_country_activated, PDO::PARAM_STR); 
							
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{
								$stmt = $db->prepare("INSERT INTO tbl_client_activation_details(phone_number, date_activated, time_activated, client_activated_by,country) VALUES (:phone_number,:date_activated,:time_activated,:client_activated_by,:country)");

								$stmt->bindParam(':phone_number', $user_login_id);
								$stmt->bindParam(':date_activated', $todayDate);
								$stmt->bindParam(':time_activated', $time_activated);
								$stmt->bindParam(':client_activated_by', $username);
								$stmt->bindParam(':country',  $client_country_activated);
										
								$stmt->execute();
								$total =  $stmt->rowCount();
	
								if($total > 0){
									$success = "true";
									$message = "You Have Successfully Activated Client Phone Number: ".$user_login_id;
									$_SESSION["message_create"]=$message;
									$db->commit();
									return true;	
								}
							}
							$db->commit();
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
								$message = "Client Phone Number .".$user_login_id." Does Not Exist In The System. Please Check Phone Number And Try Again.";
								$_SESSION["message_create"]=$message;
								return false;

			}	


?>
