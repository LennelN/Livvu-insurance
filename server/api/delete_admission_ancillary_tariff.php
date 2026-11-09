
<?php

$response = array();
include 'functions.php';
$file_name='delete_admission_ancillary_tariff';
$purchase_details = array();

if (isset($_POST['tariff_code']) && isset($_POST['tariff_country']) && isset($_POST['username'])){

//error_log(strip_tags("coming in "), 0);
$tariff_code = trim($_POST["tariff_code"]);
$username = trim($_POST["username"]);
$tariff_country = $_POST["tariff_country"];


	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	$ddate = Date("Y-m-d h:i:s");


	
				try{

							$sql = "DELETE from tbl_country_admissions_tariff_codes WHERE admission_tariff_code = :field_val AND country_name = :field_val1 "; 
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $tariff_code, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $tariff_country, PDO::PARAM_STR);
							$stmt->execute();
							$total = $stmt->rowCount();
							if($total > 0){
								$success = "true";
								$message = "You Have Successfully Removed Admission Tariff Code ".$tariff_code." As An Admission Tariff Code From The System ";
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
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
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