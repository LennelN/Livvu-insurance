
<?php

$response = array();
include 'functions.php';
$file_name='create_currency_rate';
$purchase_details = array();

if (isset($_POST['currency_type']) && isset($_POST['no_of_points']) && isset($_POST['currency_rate']) && isset($_POST['currency_country']) && isset($_POST['username'])){

$currency_type = trim($_POST["currency_type"]);
$username = trim($_POST["username"]);
$no_of_points = trim($_POST["no_of_points"]);
$currency_rate = trim($_POST["currency_rate"]);
$currency_country = $_POST["currency_country"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");

	try{

		$stmt = $db->prepare("INSERT INTO tbl_currency_rates (
									country_currency,
									type_of_currency,
									amount,
									no_of_points,
									currency_created_by) 
    						VALUES 																			(:country_currency,:type_of_currency,:amount,:no_of_points,:currency_created_by)");
    									$stmt->bindParam(':country_currency', $currency_country);
									$stmt->bindParam(':type_of_currency', $currency_type);
									$stmt->bindParam(':amount', $currency_rate);
									$stmt->bindParam(':no_of_points', $no_of_points);
									$stmt->bindParam(':currency_created_by', $username);
    									
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Created Currency Rate.";
										array_push($response,array("success"=>$success,"message"=>$message));
										echo json_encode($response);		
								
									}
							
								$db->commit();
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Insert A Duplicate Value";
							//$message = $e->getMessage();
							$message = "Code For Country: ".$currency_country." Already Exist In The System. Please Check Again.";
							$error_message=$e->getMessage();
							$error_type='duplicate';
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later Duplicate");
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							$db->rollBack();
						
						}else{	
	
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later ");
							$message=$e->getMessage();
							error_log(strip_tags($message), 0);
							$error_type='exception';
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							//echo json_encode($response);
							$db->rollBack();
							
						}
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