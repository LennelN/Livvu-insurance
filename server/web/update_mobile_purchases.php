<?php
   
session_start();
$response = array();
$response_back = array();
$file_name='update_ancillary_tariff';
require_once("./include/membersite_config.php");
include("./include/functions.php");

   
	$username = $_SESSION["user_name"];
	$purchase_transaction_no = $_SESSION["purchase_transaction_no"];
	$transaction_no = $_POST["transaction_no"];
	$transaction_type = $_POST["transaction_type"];
	$transaction_amount = $_POST["transaction_amount"];
	$transaction_date = $_POST["transaction_date"];
	$mobile_phone_no = $_SESSION["mobile_phone_no"];
	$mobile_operator = $_SESSION["selected_mobile"];
	$mobile_country = $_SESSION["selected_country"];

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
			$todayDate = date('Y-m-d');
			if($transaction_date > $todayDate)
			{
						
					$success = "false";
					$message = "Paid Out Date Cannot Be Greater Than Today...!!!";
					$_SESSION["message_create"]=$message;
					return false;
			}else{
		$sql= "UPDATE tbl_purchase_points_details SET purchase_status = 'Y' WHERE transaction_number = :field_val AND purchase_country = :field_val1 AND purchase_done_by = :field_val2 AND phone_number = :field_val3"; 
						$stmt = $db->prepare($sql);
						$stmt->bindParam(':field_val', $purchase_transaction_no, PDO::PARAM_STR);
						$stmt->bindParam(':field_val1', $mobile_country, PDO::PARAM_STR);
						$stmt->bindParam(':field_val2', $mobile_operator, PDO::PARAM_STR);
						$stmt->bindParam(':field_val3', $mobile_phone_no, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{	
						
							$stmt = $db->prepare("INSERT INTO tbl_mobile_purchase_details(phone_number, transaction_type,purchase_transaction_number, transaction_number, transaction_amount, transaction_date, mobile_operator, transaction_processed_by,transaction_country) VALUES (:phone_number, :transaction_type, :purchase_transaction_number,:transaction_number, :transaction_amount, :transaction_date, :mobile_operator, :transaction_processed_by,:transaction_country)");
							$stmt->bindParam(':phone_number', $mobile_phone_no);
							$stmt->bindParam(':transaction_type', $transaction_type);
							$stmt->bindParam(':purchase_transaction_number', $purchase_transaction_no);
							$stmt->bindParam(':transaction_number', $transaction_no);
							$stmt->bindParam(':transaction_amount', $transaction_amount);
							$stmt->bindParam(':transaction_date', $transaction_date);
							$stmt->bindParam(':mobile_operator', $mobile_operator);
							$stmt->bindParam(':transaction_processed_by', $username);
							$stmt->bindParam(':transaction_country', $mobile_country);
							$stmt->execute();
							$total =  $stmt->rowCount();
	
							if($total > 0){
					
								$success = "true";
								$message = "You Have Successfully Updated The Transaction.";
								$_SESSION["message_create"]=$message;
								$db->commit();
								return true;
							}
				
						}
					
			
			}
			}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
					{																						$success = "false";													
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
						$_SESSION["message_create"]=$message;
							$db->rollBack();
							return false;
					}else{		
						$success = "false";
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";												$error_message=$e->getMessage();
						$_SESSION["message_create"]=$error_message;
							$db->rollBack();
							return false;
					}
			}//

    
	
?>
