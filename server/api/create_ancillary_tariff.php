
<?php

$response = array();
include 'functions.php';
$file_name='create_ancillary_tariff';
$purchase_details = array();

if (isset($_POST['tariff_code']) && isset($_POST['tariff_description']) && isset($_POST['tariff_category']) && isset($_POST['tariff_price']) && isset($_POST['tariff_type']) && isset($_POST['tariff_country']) && isset($_POST['username'])){

//error_log(strip_tags("coming in "), 0);
$tariff_code = trim($_POST["tariff_code"]);
$username = trim($_POST["username"]);
$tariff_description = trim($_POST["tariff_description"]);
$tariff_category = trim($_POST["tariff_category"]);
$tariff_price = trim($_POST["tariff_price"]);
$tariff_type = $_POST["tariff_type"];
$tariff_country = $_POST["tariff_country"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");

	try{

		$stmt = $db->prepare("INSERT INTO tbl_ancillary_tariffs (
									tariff_code,
									tariff_description,
                							tariff_price,
                							tariff_category,
									tariff_type,
									tariff_country,
                							tariff_processed_by) 
    						VALUES 																			(:tariff_code,:tariff_description,:tariff_price,:tariff_category,:tariff_type,:tariff_country,:tariff_processed_by)");
    									$stmt->bindParam(':tariff_code', $tariff_code);
									$stmt->bindParam(':tariff_description', $tariff_description);
									$stmt->bindParam(':tariff_price', $tariff_price);
									$stmt->bindParam(':tariff_category', $tariff_category);
									$stmt->bindParam(':tariff_type', $tariff_type);
    									$stmt->bindParam(':tariff_country', $tariff_country);
									$stmt->bindParam(':tariff_processed_by', $username);
										
									$stmt->execute();
	
									$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Created Ancillary Tariff Code.";
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
							$message = "Code: ".$tariff_code." Already Exist In The System. Please Check Again.";
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