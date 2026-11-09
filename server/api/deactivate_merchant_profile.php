<?php
    


// array for JSON response
$response = array();
include 'functions.php';
$file_name='deactivate_merchant_profile';

// check for required fields
if (isset($_POST['user_login_id']) && isset($_POST['user_deactivated_id']) && isset($_POST['reason_deactivated']) && isset($_POST['country_deactivated'])){

	$username = $_POST["user_login_id"];
 
	$user_deactivated_id = $_POST["user_deactivated_id"];
 
   	$reason_deactivated = $_POST["reason_deactivated"];
	$country_deactivated=$_POST['country_deactivated'];
	date_default_timezone_set('Africa/Harare');
	$time_deactivated = date('H:i:s');

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();

	$sql= "SELECT merchant_code,merchant_name FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $country_deactivated, PDO::PARAM_STR); 
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
					
							$sql = "UPDATE tbl_merchant_details SET status_flag = 'N' WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1";
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $user_deactivated_id, PDO::PARAM_STR);
							$stmt->bindParam(':field_val1', $country_deactivated, PDO::PARAM_STR);  
							// execute the query
							$stmt->execute();
							$total_update =  $stmt->rowCount();
							if($total_update > 0 )//update 1 after
							{

								$stmt = $db->prepare("INSERT INTO tbl_merchant_deactivation_details(merchant_phone_number, merchant_name, merchant_code, date_deactivated,time_deactivated,reason_deactivated,user_deactivated_by,merchant_country) VALUES (:merchant_phone_number, :merchant_name, :merchant_code, :date_deactivated, :time_deactivated, :reason_deactivated, :user_deactivated_by,:merchant_country)");

								$stmt->bindParam(':merchant_phone_number', $user_deactivated_id);
								$stmt->bindParam(':merchant_name', $merchant_name);
								$stmt->bindParam(':merchant_code', $merchant_code);
								$stmt->bindParam(':date_deactivated', $todayDate);
								$stmt->bindParam(':time_deactivated', $time_deactivated);
								$stmt->bindParam(':reason_deactivated', $reason_deactivated);
								$stmt->bindParam(':user_deactivated_by', $username);
								$stmt->bindParam(':merchant_country', $country_deactivated);
										
								$stmt->execute();
								$total =  $stmt->rowCount();
	
								if($total > 0){
									$success = "true";
									$message = "You Have Successfully Deactivated Merchant Phone Number.".$user_deactivated_id." Thank You.";
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
		 								array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
										$db->rollBack();
										return false;
									}else{
										$success = "false";
		 								$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 								$error_message=$e->getMessage();
		 								array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);
										$db->rollBack();
										return false;
									}
						}//catch
					}else{
								$success = "false";
								$message = "Merchant Phone Number .".$user_deactivated_id." Does Not Exist In The System. Please Check Phone Number.";
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
