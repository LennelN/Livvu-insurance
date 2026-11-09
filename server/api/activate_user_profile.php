<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='activate_user_profile';
//error_log(strip_tags("comingin"), 0);
// check for required fields
if (isset($_POST['user_login_id']) && isset($_POST['user_country'])){

	$user_login_id = $_POST["user_login_id"];
	$user_country = $_POST["user_country"];
 
	$date_user_deactivated = NULL;

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	
	$field_val = ($user_login_id);
	$sql= "SELECT user_login_id FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{
							$db->beginTransaction();
							try
							{
								
								$sql = "UPDATE tbl_users SET user_active = 'Y',user_deactivated = 'N',date_deactivated=NULL,reason_deactivated = NULL,user_deactivated_by=NULL WHERE user_login_id = :field_val AND country = :field_val1";
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $user_login_id, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 		
								
							// execute the query
								$stmt->execute();
								$total_update =  $stmt->rowCount();
								if($total_update > 0 )//update 1 after
								{
									$success = "true";
									$message = "You Have Successfully Activated User Login ID ".$user_login_id;
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
								$message = "User Id.".$user_login_id." Does Not Exist In The System. Please Check User Id.";
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
