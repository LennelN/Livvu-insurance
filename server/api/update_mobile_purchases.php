<?php
   
include 'functions.php';
$file_name='update_mobile_purchases';

$response = array();
//$_POST['username']='0779455025';
//$_POST['cashed_out_transaction_no']='1';
//$_POST['paid_out_date']='08/09/2019';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['purchase_transaction_no']) && isset($_POST['transaction_no']) && isset($_POST['transaction_type']) && isset($_POST['transaction_amount']) && isset($_POST['transaction_date']) && isset($_POST['mobile_phone_no']) && isset($_POST['mobile_operator']) && isset($_POST['mobile_country'])) {
   
	$username = $_POST["username"];
	$purchase_transaction_no = $_POST["purchase_transaction_no"];
	$transaction_no = $_POST["transaction_no"];
	$transaction_type = $_POST["transaction_type"];
	$transaction_amount = $_POST["transaction_amount"];
	$transaction_date = $_POST["transaction_date"];
	$mobile_phone_no = $_POST["mobile_phone_no"];
	$mobile_operator = $_POST["mobile_operator"];
	$mobile_country = $_POST["mobile_country"];
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
			$date_paid_out = $dt->createFromFormat('d/m/Y', $transaction_date);
			$newDate = $date_paid_out->format('Y-m-d');
			$todayDate = date('Y-m-d');
			if($newDate > $todayDate)
			{
						
					$success = "false";
					$message = "Paid Out Date Cannot Be Greater Than Today...!!!";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
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
							$stmt->bindParam(':transaction_date', $newDate);
							$stmt->bindParam(':mobile_operator', $mobile_operator);
							$stmt->bindParam(':transaction_processed_by', $username);
							$stmt->bindParam(':transaction_country', $mobile_country);
							$stmt->execute();
							$total =  $stmt->rowCount();
	
							if($total > 0){
					
								$success = "true";
								$message = "You Have Successfully Updated The Transaction.";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
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
