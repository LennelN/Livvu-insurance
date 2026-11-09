<?php
session_start();
$response = array();
$response_back = array();
$file_name='create';
require_once("./include/membersite_config.php");
include("./include/functions.php");

$merchant_code = ($_SESSION['merchant_code']);
$merchant_phone_number = ($_SESSION["merchant_phone_number"]);
$bill_processed_by = ($_SESSION["user_name"]);

require_once __DIR__ . '/db_config.php';
$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	$phone_no_of_doctor = trim($_POST["phone_no_of_doctor"]);
	$phone_number = trim($_POST["phone_number"]);
	$bill_number = trim($_POST["bill_number"]);
	$patient_country= trim($_POST["patient_country"]);
	$bill_number_flag = false;
	//$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	//$bill_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	//$bill_number_generated = mt_rand(0, 999999);
	//$bill_number=$bill_letter.$bill_number_generated;
	$date_bill_created = date('Y-m-d H:i:s');
	$total_bill=0;
	$total_bill_amount=0;
	$tableData = stripcslashes($_POST['pTableData']);

	$bill_paid_indicator='N';	
	$new_array=json_decode($tableData,true);
	$total_no_of_points = 0;
	$transaction_id_insert = 0;
	$total_no_of_points_paid = 0;
	$points_paid=0;

		if( sizeof($new_array) != 0 )
		{
				foreach($new_array as $row)
					{
						
						$date_of_transaction = $row['bill_date'];
						$quantity = $row['sNo'];
						$tariffcode = $row['code'];
						$description = $row['description'];
						$price = $row['price'];
						$totalprice = $row['total_price'];
						$total_bill = $total_bill + $totalprice;
						
					}


		} 

	$sql_merchant_country= "SELECT merchant_country FROM tbl_merchant_details WHERE merchant_code = :field_val AND merchant_phone_number = :field_val1"; 
	$stmt_merchant_country = $db->prepare($sql_merchant_country);
	$stmt_merchant_country->bindParam(':field_val', $merchant_code, PDO::PARAM_STR); 
	$stmt_merchant_country->bindParam(':field_val1', $merchant_phone_number, PDO::PARAM_STR); 
	$stmt_merchant_country->execute();
	$total_merchant_country = $stmt_merchant_country->rowCount();
	if($total_merchant_country > 0)
	{
	
		$merchant_country = $stmt_merchant_country->fetchColumn();

	}



	$sql= "SELECT name,surname,country_name FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $patient_country, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

	if($total > 0)
	{

		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		foreach($result as $row){
			$name = $row['name'];
			$surname = $row['surname'];
			$country = $row['country_name'];
			}	
	

		try
		{
						
			$db->beginTransaction();
			$first_last_name = $name. " " .$surname;
		
			$total_no_of_points=0;
			foreach($new_array as $row)
			{
				$date_of_transaction = $row['bill_date'];
				$dt   = new DateTime();
				//$date = $dt->createFromFormat('d/m/Y', $date_of_transaction);
				$timestamp = strtotime($date_of_transaction); 
				$new_date = date('Y-m-d', $timestamp);
				$temp["transaction_id"] = $transaction_id_insert;
				$temp["transaction_date"] = $new_date;
				$temp["merchant_phone_number"] = $merchant_phone_number;
				$temp["merchant_code"] = $merchant_code;
				$temp["merchant_country"] = $merchant_country;
				$temp["merchant_doctor"] = $phone_no_of_doctor;
				$temp["bill_number"] = $bill_number;
				$temp["phone_number_billed"] = $phone_number;
				$temp["phone_number_billed_country"] = $patient_country;
				$temp["tariff_description"] = $row['description'];
				$temp["tariff_amount"]=$row['price'];
				$temp["tariff_total_amount"]=$row['total_price'];
				$temp["tariff_code"] = $row['code'];
				$temp["tariff_code_frequency"] = $row['sNo'];
				$temp["date_bill_created"]=$date_bill_created;
				$temp["no_of_points"]=$row['total_price'];
				//$total_no_of_points = $total_no_of_points + $no_of_points;
				$total_bill_amount = $total_bill_amount + $row['total_price'];		
				array_push($response, $temp);
			}


				//$db->beginTransaction();
				$sql = "INSERT INTO tbl_bill_tariff_merchant_records (transaction_id,transaction_date, merchant_phone_number, merchant_code,merchant_country, merchant_doctor, bill_number, phone_number_billed,phone_number_billed_country, tariff_description, tariff_amount, tariff_total_amount, tariff_code, tariff_code_frequency, date_bill_created,no_of_points) VALUES ";

				$paramArray = array();

				$sqlArray = array();

				foreach($response as $row)
				{
    					$sqlArray[] = '(' . implode(',', array_fill(0, count($row), '?')) . ')';

    					foreach($row as $element)
    					{
    
        					$paramArray[] = $element;
    					}
				}			

				$sql .= implode(',', $sqlArray);

				$stmt = $db->prepare($sql);

				$stmt->execute($paramArray);
				$total =  $stmt->rowCount();
				if($total > 0)
				{//insert into tbl_bill_merchant_records	
					$bill_paid_indicator = 'N';

				
					$stmt = $db->prepare("INSERT INTO tbl_bill_merchant_records (
						merchant_phone_number,
						phone_number_billed, 
						phone_number_billed_country,
						date_bill_created, 
						merchant_code, 
						merchant_country,
						merchant_doctor, 
						bill_number, 
						total_bill_amount,
						bill_amount, 
						bill_paid_indicator,
						bill_processed_by) 
					VALUES (:merchant_phone_number,:phone_number_billed,:phone_number_billed_country,:date_bill_created,:merchant_code,:merchant_country,:merchant_doctor,:bill_number,:total_bill_amount,:bill_amount,:bill_paid_indicator,:bill_processed_by)");
					$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);
					$stmt->bindParam(':phone_number_billed', $phone_number);
					$stmt->bindParam(':phone_number_billed_country', $patient_country);
					$stmt->bindParam(':date_bill_created', $date_bill_created);
					$stmt->bindParam(':merchant_code', $merchant_code);
					$stmt->bindParam(':merchant_country', $merchant_country);
					$stmt->bindParam(':merchant_doctor', $phone_no_of_doctor);
    					$stmt->bindParam(':bill_number', $bill_number);
					$stmt->bindParam(':total_bill_amount', $total_bill_amount);
					$stmt->bindParam(':bill_amount', $total_bill_amount);
					$stmt->bindParam(':bill_paid_indicator', $bill_paid_indicator);
					$stmt->bindParam(':bill_processed_by', $bill_processed_by);
					$stmt->execute();
					$total =  $stmt->rowCount();
					if($total > 0)
					{		

							$field_val = ($merchant_phone_number);
							$sql_name= "SELECT merchant_name FROM tbl_merchant_details WHERE merchant_code = :field_val"; 
							$stmt_name = $db->prepare($sql_name);
							$stmt_name->bindParam(':field_val', $merchant_code, PDO::PARAM_STR); 
							$stmt_name->execute();
							$total_name = $stmt_name->rowCount();
							if($total_name > 0)
							{
									$bill_paid_indicator_client = 'Y';
									$merchant_name = $stmt_name->fetchColumn();
									$stmt = $db->prepare("INSERT INTO tbl_bill_client_records (
											date_bill_created,
											phone_number,
											country,
											merchant_phone_number, 
											merchant_code, 
											merchant_name,
											merchant_country, 
											bill_number, 
											total_bill_amount,
											bill_amount,  
											bill_paid_indicator,
											bill_processed_by) 
									VALUES (:date_bill_created,:phone_number,:country,:merchant_phone_number,:merchant_code,:merchant_name,:merchant_country,:bill_number,:total_bill_amount,:bill_amount,:bill_paid_indicator,:bill_processed_by)");
    									$stmt->bindParam(':date_bill_created', $date_bill_created);
    									$stmt->bindParam(':phone_number', $phone_number);
									$stmt->bindParam(':country', $patient_country);
    									$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);												$stmt->bindParam(':merchant_code', $merchant_code);
									$stmt->bindParam(':merchant_name', $merchant_name);
									$stmt->bindParam(':merchant_country', $merchant_country);
    									$stmt->bindParam(':bill_number', $bill_number);
									$stmt->bindParam(':total_bill_amount', $total_bill_amount);
									$stmt->bindParam(':bill_amount', $total_bill_amount);
									$stmt->bindParam(':bill_paid_indicator', $bill_paid_indicator);
									$stmt->bindParam(':bill_processed_by', $bill_processed_by);
									$stmt->execute();
									$total =  $stmt->rowCount();
															
									if($total > 0)
									{
										
										$sql_delete = "DELETE from tbl_billing_requests WHERE phone_number = :field_val AND country =  :field_val1 AND bill_number = :field_val2";
    	                									$stmt_delete = $db->prepare($sql_delete);
										$stmt_delete->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
										$stmt_delete->bindParam(':field_val1', $patient_country, PDO::PARAM_STR);
										$stmt_delete->bindParam(':field_val2', $bill_number, PDO::PARAM_STR);

					            					$stmt_delete->execute();                                               
                  									$total_delete =  $stmt_delete->rowCount();
										//error_log(strip_tags(" error 4 ".$total_delete." and file name is ".$file_name), 0);
										$success = "true";
										//error_log(strip_tags(" error 4aa "), 0);
										$message = "Congratulations You Have Successfully Billed "." ".$first_last_name." ";
										//error_log(strip_tags(" error 4bb "), 0);
										$_SESSION["message_create"] = $message;
																				//error_log(strip_tags(" error 4cc "), 0);
										$db->commit();
																				//error_log(strip_tags(" error 4dd "), 0);
										return true;
																
									}
							}
						}
				}
			
			}catch(PDOException $e){
					
					if ($e->errorInfo[1] == 1062) 
							{
								$success = "false";
		 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
								$error_message=$e->getMessage();
								//error_log(strip_tags(" error 3 ".$error_message." and file name is ".$file_name), 0);
		 						$_SESSION["message_create"] = $message;
		 						$db->rollBack();
								return false;
							}else{
								$success = "false";
		 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
								$error_message=$e->getMessage();
								//error_log(strip_tags(" error 4 why ".$error_message." and file name is ".$file_name), 0);
		 						$_SESSION["message_create"] = $message;
		 						$db->rollBack();
								return false;
								
							}
			}
	
}
?>