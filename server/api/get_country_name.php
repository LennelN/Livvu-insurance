<?php
   
include 'functions.php';
$file_name='get_country_name';
$response = array();

if (isset($_POST['phone_number'])) {

	$username = $_POST["phone_number"];
	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';
	//$db = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql= "SELECT country_name FROM tbl_clients WHERE phone_number = :field_val"; 
		$stmt = $db->prepare($sql);
		$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
		$stmt->execute();
		$total = $stmt->rowCount();

 		if($total <= 0)
		{
				
				$sql= "SELECT merchant_country FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
				$stmt = $db->prepare($sql);
				$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();			
				
				if($total <= 0)
					{

						//Check in tbl_users for livvu company employees

						$sql= "SELECT country FROM tbl_users WHERE user_login_id = :field_val"; 
						$stmt = $db->prepare($sql);
						$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
						$stmt->execute();
						$total = $stmt->rowCount();

						if($total <=0)
							{

								$sql= "SELECT country FROM tbl_merchant_users WHERE user_login_id = :field_val"; 
								$stmt = $db->prepare($sql);
								$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
								$stmt->execute();
								$total = $stmt->rowCount();

								if($total <=0)
								{
 
    									$success = "false";
									$user_country = "UnKnown";
									$message ="Country Name Is Not Available For Phone Number ".$username." Please Check And Update ";
									array_push($response,array("success"=>$success,"message"=>$message,"user_country"=>$user_country));
									echo json_encode($response);
								}else{
									$user_country = $stmt->fetchColumn();
									$success = "true";
									$message = "Successful"; 
									array_push($response,array("success"=>$success,"message"=>$message,"user_country"=>$user_country));
									echo json_encode($response);
								}
							}else{
								$success = "true";
								$message = "Successful";
								$user_country = $stmt->fetchColumn();
								array_push($response,array("success"=>$success,"message"=>$message,"user_country"=>$user_country));
								echo json_encode($response);

							}
					}else{
						
						$success = "true";
						$message = "Successful";
						$user_country = $stmt->fetchColumn();
						array_push($response,array("success"=>$success,"message"=>$message,"user_country"=>$user_country));
						echo json_encode($response);

					}
		}else{
			$success = "true";
			$message = "Successful";
			$user_country = $stmt->fetchColumn();
			array_push($response,array("success"=>$success,"message"=>$message,"user_country"=>$user_country));
			echo json_encode($response);
		}

}else{
	// required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    	// echoing JSON response
    	echo json_encode($response);

}
	
?>
