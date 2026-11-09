<?php
    

// array for JSON response
$response = array();
include 'functions.php';
$file_name='register_user_profile';
//sset($_POST['phone_no'])='0772455025';
//error_log(strip_tags("comingin"), 0);
// check for required fields
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['user_role']) && isset($_POST['phone_no']) && isset($_POST['idno']) && isset($_POST['password']) && isset($_POST['branch']) && isset($_POST['address']) && isset($_POST['country']) && isset($_POST['username'])){
//error_log(strip_tags("comingin 2"), 0);

	$name = $_POST["name"];
    
	$email = $_POST["email"];
    
	$user_role = $_POST["user_role"];
    
	$phone_no = $_POST["phone_no"];
 
   	$idno = $_POST["idno"];
 
	$password = $_POST["password"];

   
	$branch = $_POST["branch"];

	$country = $_POST["country"];

	$address = $_POST["address"];

	$username = $_POST["username"];
	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$user_id_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$user_id_generated = mt_rand(0, 999999);
	$phone_prefix = substr($phone_no,0,1);
	$phone_no_length = strlen($phone_no);
//error_log(strip_tags("comingin prefix is ".$phone_prefix), 0);
	if($phone_prefix == '0'){
		$phone_less_prefix = substr($phone_no,1,$phone_no_length);
		$user_login_id="9".$phone_less_prefix;
	}else{
		$user_login_id="9".$phone_no;
	}
	//error_log(strip_tags("comingin user login id  is ".$user_login_id), 0);

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
								
			$stmt = $db->prepare("INSERT INTO tbl_users (user_login_id, name, email, phone_number, password, confirmcode, idno, branch, user_role, date_registered, user_active, country, address, user_deactivated,user_created_by) VALUES (:user_login_id, :name, :email, :phone_number, :password, :confirmcode, :idno, :branch, :user_role, :date_registered, :user_active, :country, :address, :user_deactivated,:user_created_by)");
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
				$stmt->bindParam(':user_created_by', $username);
				$stmt->execute();
				$total =  $stmt->rowCount();
	
				if($total > 0)
				{
					$success = "true";
					$message = "Congratulations You Have Successfully Registered. Go Ahead And LogIn And Complete Your Registration.";
					array_push($response,array("success"=>$success,"message"=>$message,"confirmcode"=>$confirmcode,"user_login_id"=>$user_login_id));
					echo json_encode($response);
				}
		
				$db->commit();
			}catch(PDOException $e){
					
				if ($e->errorInfo[1] == 1062) 
				{
					$success = "false";
					$error_message=$e->getMessage();
		 			$message = "Error Entering Duplicate value ".$error_message;
		 			$error_type='duplicate';
		 			//handler($error_message,$file_name,$success,$error_type);
					$db->rollBack();
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
    // required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}


?>
