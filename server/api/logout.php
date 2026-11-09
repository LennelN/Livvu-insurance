<?php 
$response = array();
$response_back = array();
include 'functions.php';
$file_name='logout';
//$_POST['username']='0772455025';
//$_POST['name']='Francis';
// check for required fields
if(isset($_POST['username']) && isset($_POST['name']))
{

	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';
	date_default_timezone_set('Africa/Harare');
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	//Check whether the phone number being billed exists and its active
 	$username = $_POST["username"];	
	$name = $_POST["name"];	
	$date_logged_out = date('Y-m-d H:i:s');
	$time_logged_out = date('H:i:s');
	$logoutstatus = 'No';

				try
					{
							$db->beginTransaction();
							$sql = "UPDATE tbl_user_login_history SET loginstatus = '$logoutstatus',logoutdate = '$date_logged_out',logouttime= '$time_logged_out' WHERE user_login_id = :field_val AND loginstatus = 'Yes'";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{	
			
									$success = "true";
									$message = "You Have Successfully Logged Out"." ".$name." GoodBye!";
									array_push($response_back,array("success"=>$success,"message"=>$message));
									echo json_encode($response_back);
							
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

} else {
    //required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
		array_push($response_back,array("success"=>$success,"message"=>$message));
    echo json_encode($response_back);
}

?>