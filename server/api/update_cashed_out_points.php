<?php
   
include 'functions.php';
$file_name='update_cashed_out_points';

$response = array();
//$_POST['username']='0779455025';
//$_POST['cashed_out_transaction_no']='1';
//$_POST['paid_out_date']='08/09/2019';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['cash_out_transaction_no']) && isset($_POST['cashed_out_transaction_no']) && isset($_POST['cashed_out_transaction_type']) && isset($_POST['cashed_out_transaction_amount']) && isset($_POST['cashed_out_transaction_date'])) {
   
	$username = $_POST["username"];
	$cash_out_transaction_no = $_POST["cash_out_transaction_no"];
	$cashed_out_transaction_no = $_POST["cashed_out_transaction_no"];
	$cashed_out_transaction_type = $_POST["cashed_out_transaction_type"];
	$cashed_out_transaction_amount = $_POST["cashed_out_transaction_amount"];
	$cashed_out_transaction_date = $_POST["cashed_out_transaction_date"];

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	//$db = new DB_CONNECT();
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();

	try
	{
			$dt   = new DateTime();
			$date_paid_out = $dt->createFromFormat('d/m/Y', $cashed_out_transaction_date);
			$newDate = $date_paid_out->format('Y-m-d');
			$todayDate = date('Y-m-d');
			if($newDate > $todayDate)
			{
						
				$success = "false";
				$message = "Paid Out Date Cannot Be Greater Than Today...!!!";
				array_push($response,array("success"=>$success,"message"=>$message));
				echo json_encode($response);
			}else{

					$sql= "SELECT merchant_phone_number FROM tbl_merchant_no_of_points_cashed_out WHERE cashed_out_transaction_no = :field_val"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $cash_out_transaction_no, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();			
				
					if($total > 0){
						$merchant_phone_number= $stmt->fetchColumn();
						$sql= "UPDATE tbl_merchant_no_of_points_cashed_out SET points_cashed_out_indicator = 'Y' WHERE cashed_out_transaction_no = :field_val "; 
						$stmt = $db->prepare($sql);
						$stmt->bindParam(':field_val', $cash_out_transaction_no, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{	

						
							$stmt = $db->prepare("INSERT INTO tbl_cashed_points_details(merchant_phone_number, transaction_type, transaction_number, transaction_amount, transaction_date, transaction_processed_by) VALUES (:merchant_phone_number, :transaction_type, :transaction_number, :transaction_amount, :transaction_date, :transaction_processed_by)");
							$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);
							$stmt->bindParam(':transaction_type', $cashed_out_transaction_type);
							$stmt->bindParam(':transaction_number', $cashed_out_transaction_no);
							$stmt->bindParam(':transaction_amount', $cashed_out_transaction_amount);
							$stmt->bindParam(':transaction_date', $newDate);
							$stmt->bindParam(':transaction_processed_by', $username);
							$stmt->execute();
							$total =  $stmt->rowCount();
	
							if($total > 0){
					
								$success = "true";
								$message = "You Have Successfully Cashed Out Points For Phone Number. ".$merchant_phone_number;
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
							}
				
						}
					}
					$db->commit();
			}
			}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
					{																						$success = "false";													
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
						$error_message=$e->getMessage();
						$error_type='duplicate';
						handler($error_message,$file_name,$success,$error_type);
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
						$db->rollBack();
					}else{		
						$success = "false";
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";												$error_message=$e->getMessage();
						$error_type='exception';
						handler($error_message,$file_name,$success,$error_type);
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
						$db->rollBack();
					}
		}//

    
 }else {
    // required field is missing


    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}

	
?>
