<?php
    
session_start();
$response = array();
$response_back = array();
$file_name='reset_merchant_account_profile';
require_once("./include/membersite_config.php");
include("./include/functions.php");

	$username = $_SESSION["user_name"];
 
	$user_deactivated_id = $_POST["user_login_id_to_update"];
 	$merchant_country_activated = $_POST["merchant_country_activated"];
	date_default_timezone_set('Africa/Harare');
	$time_deactivated = date('H:i:s');
	//$reset_password = "1234";
	$reset_password = mt_rand(0, 9999);
	$len_password = strlen($reset_password);
	if($len_password < 4) {
	error_log(strip_tags("password is less than 4 digits "), 0);
		do{
			error_log(strip_tags("password is less than 4 digits dpig  "), 0);
			$reset_password = mt_rand(0, 9999);
			$len_password_2 =  strlen($reset_password);
			error_log(strip_tags("now password generated is ".$reset_password." and length is ".$len_password_2), 0);
			if($len_password_2 == 4){
				error_log(strip_tags("now password is equal to 4 digits "), 0);
				$result = true;
			}
		} while ($result == false);

	}
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

	$sql= "SELECT merchant_code,merchant_name FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_country_activated, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{

						$result_details = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result_details as $row)
						{
							$merchant_code=$row['merchant_code'];
							$merchant_name=$row['merchant_name'];
						}
							  
						$db->beginTransaction();

					
						try
						{

							$dt   = new DateTime();
							//$date_paid_out = $dt->createFromFormat('d/m/Y', $deactivated_date);
							//$newDate = $date_paid_out->format('Y-m-d');
							$todayDate = date('Y-m-d');
					
							$hash = sodium_crypto_pwhash_str(
    									$reset_password,
    									SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    									SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
								);
					

							$sql = "UPDATE tbl_merchant_details SET status_flag = 'R',password = '$hash' WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $merchant_country_activated, PDO::PARAM_STR); 
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{
								$success = "true";
								$message = "You Have Successfully Activated Password Reset For User .".$user_deactivated_id." Thank You.";
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
								$message = "Merchant Phone Number .".$user_deactivated_id." Does Not Exist In The System. Please Check Merchant Phone Number.";
								$_SESSION["message_create"]=$error_message;
										
							return false;

					}				

?>
