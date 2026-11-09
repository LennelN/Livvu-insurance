<?php
    

session_start();
$response = array();
$response_back = array();
$file_name='enter_payment';
require_once("./include/membersite_config.php");
include("./include/functions.php");

$merchant_phone_number = ($_SESSION["merchant_phone_number"]);
$merchant_code= ($_SESSION["merchant_code"]);
$username = $_SESSION["user_name"];

require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);


	$name = $_POST["name"];
    
	$email = $_POST["email"];
    
	$user_role = $_POST["user_role"];
    
	$phone_no = $_POST["phone_no"];
 
   	$idno = $_POST["idno"];
 
	$password = $_POST["password"];

   
	$branch = $_POST["branch"];

	$country = $_POST["country"];

	$address = $_POST["address"];

	
	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$user_id_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$user_id_generated = mt_rand(0, 999999);
	$phone_no_length = strlen($phone_no);
	$phone_less_prefix = substr($phone_no,1,$phone_no_length);
	$user_login_id="8".$phone_less_prefix;
	

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($phone_no);
					//Check whether the phone has already been registered as a merchant
						try 
							{
							
								$date_registered = date('Y-m-d H:i:s');
								$user_active = 'N';
								$user_deactivated = 'N';
								$hash = sodium_crypto_pwhash_str(
    									$password,
    									SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    									SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
								);
				
								$dt   = new DateTime();
								$confirmcode = mt_rand(0, 999999);
								//$username='0772455025';
								
			$stmt = $db->prepare("INSERT INTO tbl_merchant_users (user_login_id, name, email, phone_number, password, confirmcode, idno, branch, user_role, date_registered, user_active, country, address, user_deactivated, merchant_phone_number, merchant_code, user_created_by) VALUES (:user_login_id, :name, :email, :phone_number, :password, :confirmcode, :idno, :branch, :user_role, :date_registered, :user_active, :country, :address, :user_deactivated,:merchant_phone_number, :merchant_code, :user_created_by)");
				$stmt->bindParam(':user_login_id', $user_login_id);
				$stmt->bindParam(':name', $name);
				$stmt->bindParam(':email', $email);
				$stmt->bindParam(':phone_number', $phone_no);
				$stmt->bindParam(':password', $hash);
				$stmt->bindParam(':confirmcode', $confirmcode);
				$stmt->bindParam(':idno', $idno);
    				$stmt->bindParam(':branch', $branch);
				$stmt->bindParam(':user_role', $user_role);
				$stmt->bindParam(':date_registered', $date_registered);
				$stmt->bindParam(':user_active', $user_active);
				$stmt->bindParam(':country', $country);
				$stmt->bindParam(':address', $address);
				$stmt->bindParam(':user_deactivated', $user_deactivated);
				$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);
				$stmt->bindParam(':merchant_code', $merchant_code);
				$stmt->bindParam(':user_created_by', $username);
				$stmt->execute();
				$total =  $stmt->rowCount();
	
				if($total > 0)
				{
					$success = "true";
					$message = "Congratulations You Have Successfully Registered. Go Ahead And LogIn And Complete Your Registration.";
$_SESSION["message_enter"]=$message;
					//array_push($response,array("success"=>$success,"message"=>$message,"confirmcode"=>$confirmcode,"user_login_id"=>$user_login_id));
					//echo json_encode($response);
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
$_SESSION["message_enter"]=$message;
					//array_push($response,array("success"=>$success,"message"=>$message));
					//echo json_encode($response);
								
				}else{
					$success = "false";
		 			$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 			$error_message=$e->getMessage();
		 			$error_type='exception';
		 			handler($error_message,$file_name,$success,$error_type);
		 			$db->rollBack();
$_SESSION["message_enter"]=$message;
					//array_push($response,array("success"=>$success,"message"=>$message));
					//echo json_encode($response);
				}
			}//catch

?>
