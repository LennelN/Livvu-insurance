
<?php

$response = array();
include 'functions.php';
$file_name='create_contributing_rate';
$purchase_details = array();

if (isset($_POST['contributing_rate']) && isset($_POST['contributing_country']) && isset($_POST['username'])){

$username = trim($_POST["username"]);
$contributing_rate = trim($_POST["contributing_rate"]);
$contributing_country = $_POST["contributing_country"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	
	$ddate = Date("Y-m-d h:i:s");

	try{

		$stmt = $db->prepare("INSERT INTO tbl_year_country_contributions(id, amount, country, processed_by) 
    						VALUES 																		(:id, :amount, :country, :processed_by)");
    						$stmt->bindParam(':id', $id_insert);
						$stmt->bindParam(':amount', $contributing_rate);
						$stmt->bindParam(':country', $contributing_country);
						$stmt->bindParam(':processed_by', $username);
    									
						$stmt->execute();
	
						$total =  $stmt->rowCount();
	
									if($total > 0)
									{
										$success = "true";
										$message = "Congratulations You Have Successfully Created The Year Contributing Rate For ".$contributing_country;
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
							$message = "Contributing Amount For Country: ".$contributing_country." Already Exist In The System. Please Check Again.";
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