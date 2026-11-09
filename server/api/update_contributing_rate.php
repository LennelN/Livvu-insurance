<?php
   
include 'functions.php';
$file_name='update_contributing_rate';
$response = array();

if (isset($_POST['contributing_country']) && isset($_POST['contributing_amount']) && isset($_POST['contributing_id'])  && isset($_POST['username'])){


	$contributing_country = $_POST["contributing_country"];
	$contributing_amount= $_POST["contributing_amount"];
	$contributing_id= $_POST["contributing_id"];
	$username = trim($_POST["username"]);

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	$ddate = Date("Y-m-d h:i:s");

	try{

			$sql= "UPDATE tbl_year_country_contributions SET amount = '$contributing_amount',processed_by='$username' WHERE country = :field_val AND id = :field_val1"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $contributing_country, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $contributing_id, PDO::PARAM_STR); 
					
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$success = "true";
						$message = "You Have Successfully Updated Contributing Rate Details For Country ".$contributing_country." Thank You ";
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
					}else{
						
						$success = "false";
						$message = "Contributing Rate For Country ".$contributing_country." Was Not Updated. Please Check And Try Again.";
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
					}

							
								$db->commit();
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Update A Duplicate Value";
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

}else{
// required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);

}
	
?>
