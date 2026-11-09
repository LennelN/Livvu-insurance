<?php
   
session_start();
$response = array();
$response_back = array();
$file_name='update_cashed_out_points';
require_once("./include/membersite_config.php");
include("./include/functions.php");
  
	$username = $_SESSION["user_name"];
	$cash_out_transaction_no = $_SESSION["cashed_out_transaction_no"];
	$cashed_out_transaction_no = $_POST["transno"];
	$cashed_out_transaction_type = $_POST["transtype"];
	$cashed_out_transaction_amount = $_POST["transamount"];
	$cashed_out_transaction_date = $_POST["transdate"];
	$balance = 0;
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
			//$date_paid_out = $dt->createFromFormat('d/m/Y', $cashed_out_transaction_date);
			//$newDate = $date_paid_out->format('Y-m-d');
			$todayDate = date('Y-m-d');
			if($cashed_out_transaction_date > $todayDate)
			{
						
				$success = "false";
				$message = "Paid Out Date Cannot Be Greater Than Today...!!!";
				$_SESSION["message_create"]=$message;
				return false;
			}else{
					
					$sql= "SELECT merchant_phone_number,merchant_code FROM tbl_merchant_amount_cashed_out WHERE cashed_out_transaction_no = :field_val"; 
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $cash_out_transaction_no, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();			
				
					if($total > 0)
					{
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC);						
							foreach($result as $row){
								$merchant_phone_number = $row['merchant_phone_number'];
								$merchant_code = $row['merchant_code'];
							}

							//get the amount from table
							$sql_amount= "SELECT amount_cashed_out FROM tbl_merchant_amount_cashed_out WHERE cashed_out_transaction_no = :field_val"; 
							$stmt_amount = $db->prepare($sql_amount);
							$stmt_amount->bindParam(':field_val', $cash_out_transaction_no, PDO::PARAM_STR); 
							$stmt_amount->execute();
							$total_amount = $stmt_amount->rowCount();			
				
							if($total_amount > 0)
							{
								$cashed_out_amount = $stmt_amount->fetchColumn();
								
								if($cashed_out_transaction_amount == $cashed_out_amount)
								{
									$sql= "UPDATE tbl_merchant_amount_cashed_out SET amount_cashed_out_indicator = 'Y' WHERE cashed_out_transaction_no = :field_val "; 
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $cash_out_transaction_no, PDO::PARAM_STR);
									$stmt->execute();
									$total = $stmt->rowCount();
					
									if($total > 0)
									{	

										$stmt = $db->prepare("INSERT INTO tbl_cashed_amount_details(transaction_id,merchant_phone_number, merchant_code, transaction_type, transaction_number, transaction_amount, transaction_date, cash_out_transaction_number,transaction_processed_by) VALUES (:transaction_id,:merchant_phone_number, :merchant_code, :transaction_type, :transaction_number, :transaction_amount, :transaction_date, :cash_out_transaction_number, :transaction_processed_by)");
										$stmt->bindParam(':transaction_id', $transaction_id_insert);
										$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);
										$stmt->bindParam(':merchant_code', $merchant_code);
										$stmt->bindParam(':transaction_type', $cashed_out_transaction_type);
										$stmt->bindParam(':transaction_number', $cashed_out_transaction_no);
										$stmt->bindParam(':transaction_amount', $cashed_out_transaction_amount);
										$stmt->bindParam(':transaction_date', $cashed_out_transaction_date);
										$stmt->bindParam(':cash_out_transaction_number', $cash_out_transaction_no);
										$stmt->bindParam(':transaction_processed_by', $username);
										$stmt->execute();
										$total =  $stmt->rowCount();
	
										if($total > 0)
										{
											$success = "true";
											$message = "You Have Successfully Cashed Out Amount For Phone Number. ".$merchant_phone_number;
											$_SESSION["message_create"]=$message;
											$db->commit();
											return true;
										}
						
									}

								
								}else{

									$balance = $cashed_out_amount - $cashed_out_transaction_amount;
								
									$sql= "UPDATE tbl_merchant_amount_cashed_out SET amount_cashed_out = '$balance' WHERE cashed_out_transaction_no = :field_val "; 
									$stmt = $db->prepare($sql);
									$stmt->bindParam(':field_val', $cash_out_transaction_no, PDO::PARAM_STR);
									$stmt->execute();
									$total = $stmt->rowCount();
						
									if($total > 0)
									{	

										$stmt = $db->prepare("INSERT INTO tbl_cashed_amount_details(transaction_id,merchant_phone_number, merchant_code,transaction_type, transaction_number, transaction_amount, transaction_date, cash_out_transaction_number,transaction_processed_by) VALUES (:transaction_id,:merchant_phone_number, :merchant_code,:transaction_type, :transaction_number, :transaction_amount, :transaction_date, :cash_out_transaction_number, :transaction_processed_by)");
										$stmt->bindParam(':transaction_id', $transaction_id_insert);
										$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);
										$stmt->bindParam(':merchant_code', $merchant_code);
										$stmt->bindParam(':transaction_type', $cashed_out_transaction_type);
										$stmt->bindParam(':transaction_number', $cashed_out_transaction_no);
										$stmt->bindParam(':transaction_amount', $cashed_out_transaction_amount);
										$stmt->bindParam(':transaction_date', $cashed_out_transaction_date);
										$stmt->bindParam(':cash_out_transaction_number', $cash_out_transaction_no);
										$stmt->bindParam(':transaction_processed_by', $username);
										$stmt->execute();
										$total =  $stmt->rowCount();
	
										if($total > 0)
										{
											$success = "true";
											$message = "You Have Successfully Cashed Out Amount For Phone Number. ".$merchant_phone_number;	
											$_SESSION["message_create"]=$message;
											$db->commit();
											return true;
										}
				
									}
								}
							}
					}
					
			}	
	}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
					{																						$success = "false";													
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
						$db->rollBack();
						$_SESSION["message_create"]=$message;
						return false;
						
					}else{		
						$success = "false";
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";												$db->rollBack();
						$_SESSION["message_create"]=$message;
						return false;
					}
	}


?>
