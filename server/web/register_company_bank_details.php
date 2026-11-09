<?php
    
session_start();
$response = array();
$response_back = array();
$file_name='register_company_bank_details';
require_once("./include/membersite_config.php");
include("./include/functions.php");

	$merchant_code = $_SESSION["merchant_code"];
    
	$username = $_SESSION["merchant_phone_number"];
    
	$bank_account_no = $_POST["bank_acc_no"];
    
	$bank_name = $_POST["bank_name"];
    
	$bank_branch = $_POST["bank_branch"];
    
	$bank_address = $_POST["bank_address"];
    
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	 $date_registered = date('Y-m-d');
	$transaction_id_insert=0;
	$field_val = ($bank_account_no);

	$sql= "SELECT merchant_bank_account_no FROM tbl_merchant_bank_details WHERE merchant_bank_account_no = :field_val"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $bank_account_no, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

	 // If the SQL query is succesfully performed ($result not false)
  	if($total > 0) {
		
		$success = "false";
		$message = "Merchant Bank Account Number Already Exists In The Database.";
		$_SESSION["message_enter"]=$message;
		//echo json_encode($message);
		return false;
	}else{	


		try 
		{
//$bank_account_no='12345678';
			//$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);	        
			$stmt = $db->prepare("INSERT INTO tbl_merchant_bank_details (
			transaction_id,
			merchant_code,
			merchant_phone_no,
			merchant_bank_account_no,
                			merchant_bank_name,
			merchant_bank_branch,
			merchant_bank_address,
			date_registered) 
    			VALUES (:transaction_id,:merchant_code,:merchant_phone_no,:merchant_bank_account_no,:merchant_bank_name,:merchant_bank_branch,:merchant_bank_address,:date_registered)");
    			$stmt->bindParam(':transaction_id', $transaction_id_insert);
    			$stmt->bindParam(':merchant_code', $merchant_code);
			$stmt->bindParam(':merchant_phone_no', $username);
			$stmt->bindParam(':merchant_bank_account_no', $bank_account_no);
			$stmt->bindParam(':merchant_bank_name', $bank_name);
    			$stmt->bindParam(':merchant_bank_branch', $bank_branch);
			$stmt->bindParam(':merchant_bank_address', $bank_address);
			$stmt->bindParam(':date_registered',  $date_registered);
		
			$stmt->execute();
	
			$total =  $stmt->rowCount();
	
			if($total > 0)
			{
				$success = "true";
				$message = "Congratulations You Have Successfully Entered The Banking Details Into The System.";
				$_SESSION["message_enter"]=$message;
				$db->commit();
				return true;
			}
			
		}catch(PDOException $e){
					
					if ($e->errorInfo[1] == 1062) 
							{
								$success = "false";
		 						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 						$error_message=$e->getMessage();
		 						$db->rollBack();
								$_SESSION["message_enter"]=$error_message;
								return false;
								
							}else{
								$success = "false";
		 						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
								$error_message=$e->getMessage();
								$db->rollBack();
								$_SESSION["message_enter"]=$error_message;
								return false;
							}
		}

	}	

?>
