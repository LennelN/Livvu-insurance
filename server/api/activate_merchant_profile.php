<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='activate_merchant_profile';
//error_log(strip_tags("comingin"), 0);
// check for required fields
if (isset($_POST['user_login_id']) && isset($_POST['user_activated_id']) && isset($_POST['country_activated'])){
	
	$user_login_id = $_POST["user_activated_id"];
 
	$username = $_POST["user_login_id"];
 $country_activated=$_POST['country_activated'];
	date_default_timezone_set('Africa/Harare');
	$time_activated = date('H:i:s');
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($user_login_id);

			$sql= "SELECT merchant_code,merchant_name FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $country_activated, PDO::PARAM_STR); 
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
							  
					
						try
						{
							$todayDate = date('Y-m-d');
								
							$sql = "UPDATE tbl_merchant_details SET status_flag = 'Y' WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $country_activated, PDO::PARAM_STR); 
							
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{
								$stmt = $db->prepare("INSERT INTO tbl_merchant_activation_details(merchant_phone_number, merchant_name, merchant_code, date_activated, time_activated, user_activated_by,merchant_country) VALUES (:merchant_phone_number, :merchant_name, :merchant_code, :date_activated,:time_activated,:user_activated_by,:merchant_country)");

								$stmt->bindParam(':merchant_phone_number', $user_login_id);
								$stmt->bindParam(':merchant_name', $merchant_name);
								$stmt->bindParam(':merchant_code', $merchant_code);
								$stmt->bindParam(':date_activated', $todayDate);
								$stmt->bindParam(':time_activated', $time_activated);
								$stmt->bindParam(':user_activated_by', $username);
								$stmt->bindParam(':merchant_country', $country_activated);
										
								$stmt->execute();
								$total =  $stmt->rowCount();
	
								if($total > 0){
									$success = "true";
									$message = "You Have Successfully Activated Merchant Phone Number: ".$user_login_id;
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
		 								//handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);	
								
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								$error_type='exception';
		 								//handler($error_message,$file_name,$success,$error_type);
		 								$db->rollBack();
										array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);	
									}
						}//catch
				
				}else{
								$success = "false";
								$message = "Merchant Phone Number .".$user_login_id." Does Not Exist In The System. Please Check Phone Number.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
								return false;

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
