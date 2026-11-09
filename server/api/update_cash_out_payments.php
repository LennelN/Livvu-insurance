<?php
   
include 'functions.php';
$file_name='update_cash_out_payments';

$response = array();
//$_POST['username']='0779455025';
//$_POST['cashed_out_transaction_no']='1';
//$_POST['paid_out_date']='08/09/2019';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['cashed_out_transaction_no']) && isset($_POST['paid_out_date']) && isset($_POST['merchant_code'])) {
   
	$username = $_POST["username"];
	$cashed_out_transaction_no = $_POST["cashed_out_transaction_no"];
	$paid_out_date = $_POST["paid_out_date"];
	$merchant_code = $_POST["merchant_code"];
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
			$date_paid_out = $dt->createFromFormat('d/m/Y', $paid_out_date);
			$newDate = $date_paid_out->format('Y-m-d');
			$todayDate = date('Y-m-d');
			if($newDate > $todayDate)
			{
						
				$success = "false";
				$message = "Paid Out Date Cannot Be Greater Than Today...!!!";
				array_push($response,array("success"=>$success,"message"=>$message));
				echo json_encode($response);
			}else{


		
					$sql= "UPDATE tbl_merchant_amount_cashed_out SET amount_paid_out_date = '$newDate',amount_paid_out_indicator = 'Y' WHERE merchant_phone_number = :field_val AND cashed_out_transaction_no = :field_val1 AND merchant_code = :field_val2"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $cashed_out_transaction_no, PDO::PARAM_STR);
					$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR);
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$success = "true";
						$message = "You Have Successfully Confirmed Your Payment. ";
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
					}else{
						
						$success = "false";
						$message = "You Have No Outstanding Points Cashed Out...!!!";
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
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
