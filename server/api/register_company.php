<?php
    

// array for JSON response
$response = array();
$group_total = 200;
include 'functions.php';
$file_name='register_company';

// check for required fields
if (isset($_POST['name']) && isset($_POST['licence_number']) && isset($_POST['description']) && isset($_POST['address']) && isset($_POST['region']) && isset($_POST['media']) && isset($_POST['username']) && isset($_POST['password']) && isset($_POST['country_name']) && isset($_POST['type'])){

	$name = $_POST["name"];
	$licenceNo = $_POST["licence_number"];
	$description = $_POST["description"];
	$address = $_POST["address"];
	$username = $_POST["username"];
	$password = $_POST["password"];
	$country = $_POST["country_name"];
	$region = $_POST["region"];
	$media = $_POST["media"];
	$type = $_POST["type"];
	
	require_once __DIR__ . '/db_config.php';
	//$db = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	

	$field_val = ($username);

	$sql= "SELECT merchant_phone_number FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

	 // If the SQL query is succesfully performed ($result not false)
  	if($total > 0) 
	{
		
		$success = "false";
		$message = "Phone Number ". $username. " Is Already Registered In The System.";
		array_push($response,array("success"=>$success,"message"=>$message));
		echo json_encode($response);
	}else{	
			
					//Check whether the phone number is already registered as an individual client in the system
			
					$sql= "SELECT phone_number FROM tbl_clients WHERE phone_number = :field_val"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();

	 				// If the SQL query is succesfully performed ($result not false)
  					if($total > 0) 
					{
		
							$success = "false";
							$message = "Phone Number ". $username ." Is Already Registered In The System.";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
					}else{
						try
						{
							
									$merchant_date_joined = date('Y-m-d H:i:s');
									$status_flag = 'Y';
									$merchant_date_resigned = date('Y-m-d H:i:s');
									$qry = "Select country_code from tbl_country_codes where country_name = :field_val";
									$stmt = $db->prepare($qry);
									$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
    									$stmt->execute();
									$total =  $stmt->rowCount();

 									if($total <= 0){
										$country_code = 'Unknown';
 									}else{
	    									$country_code = $stmt->fetchColumn();
									}	
		
									$terms_status_flag = 'Company';	
									$hash = sodium_crypto_pwhash_str(
    										$password,
    										SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    										SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
									);

									$merchantCode = mt_rand(0, 999999);
									//$merchantCode = '64888';
						$stmt = $db->prepare("INSERT INTO tbl_merchant_details (
									merchant_phone_number,
									merchant_licence_number,
									merchant_code,
                							merchant_name,
									merchant_description,
									merchant_address,
                							password,
									merchant_date_joined,
									merchant_date_resigned,
									media_used,
									terms_status_flag,
									status_flag,
									merchant_country,
									merchant_region,
									country_code,
									type_of_registration) 
    						VALUES (:merchant_phone_number,:merchant_licence_number,:merchant_code,:merchant_name,:merchant_description,:merchant_address,:password,:merchant_date_joined,:merchant_date_resigned,:media_used,:terms_status_flag,:status_flag,:merchant_country,:merchant_region,:country_code,:type_of_registration)");
							$stmt->bindParam(':merchant_phone_number', $username);
							$stmt->bindParam(':merchant_licence_number', $licenceNo);
							$stmt->bindParam(':merchant_code', $merchantCode);
							$stmt->bindParam(':merchant_name', $name);
							$stmt->bindParam(':merchant_description', $description);
							$stmt->bindParam(':merchant_address', $address);
							$stmt->bindParam(':password', $hash);
							$stmt->bindParam(':merchant_date_joined', $merchant_date_joined);
							$stmt->bindParam(':merchant_date_resigned', $merchant_date_resigned);
							$stmt->bindParam(':media_used', $media);
							$stmt->bindParam(':terms_status_flag', $terms_status_flag);
							$stmt->bindParam(':status_flag', $status_flag);
							$stmt->bindParam(':merchant_country', $country);
							$stmt->bindParam(':merchant_region', $region);
							$stmt->bindParam(':country_code', $country_code);
							$stmt->bindParam(':type_of_registration', $type);
							$stmt->execute();
							$total =  $stmt->rowCount();
							if($total > 0)
							{
								$success = "true";
								$message = "Congratulations You Have Successfully Registered. Your Merchant Code Is ".$merchantCode;
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
							}
							$db->commit();
			}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							$success = "false";
							//$message = "An error occurred. Trying To Insert A Duplicate Value";
							//$message = $e->getMessage();
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							$error_message=$e->getMessage();
							$error_type='duplicate';
							handler($error_message,$file_name,$success,$error_type);
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							$db->rollBack();
						}else{		
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							//$message=$e->getMessage();
							$error_message=$e->getMessage();
							$error_type='exception';
							handler($error_message,$file_name,$success,$error_type);
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							$db->rollBack();
						}
			}
		}
	}
} else {
    // required field is missing
    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
		array_push($response,array("success"=>$success,"message"=>$message));
    echo json_encode($response);
}


?>
