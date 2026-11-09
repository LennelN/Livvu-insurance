
<?php

$response = array();
include 'functions.php';
$file_name='enter_admission_ancillary_tariff';
$purchase_details = array();

if (isset($_POST['tariff_code']) && isset($_POST['tariff_country'])){

	$tariff_code = trim($_POST["tariff_code"]);
	$tariff_country = $_POST["tariff_country"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");
	$qry = "Select tariff_code from tbl_ancillary_tariffs WHERE tariff_code = '$tariff_code' AND tariff_country = '$tariff_country'";
	$stmt = $db->prepare($qry);
        $stmt->execute();
	$total = $stmt->rowCount();
	$num=0;
	if($total > 0){


				try{

					$stmt = $db->prepare("INSERT INTO tbl_country_admissions_tariff_codes (
								country_name,
								admission_tariff_code) 
    								VALUES 																			(:country_name,:admission_tariff_code)");
    							$stmt->bindParam(':country_name', $tariff_country);
							$stmt->bindParam(':admission_tariff_code', $tariff_code);
									
										
							$stmt->execute();
	
							$total =  $stmt->rowCount();
	
							if($total > 0)
							{

								$success = "true";
								$message = "Congratulations You Have Successfully Entered Admission Ancillary Tariff Code. ";
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
							$message = "Code: ".$tariff_code." Already Exist In The System As An Admission Code. Please Check Again.";
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
		}else{

			$success = "false";
			$message = "Code: ".$tariff_code." Does Not Exist In The System As An Admission Code For Country ".$tariff_country." Please Check Again.";
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