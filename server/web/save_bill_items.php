<?php 
session_start();
$response = array();
$response_back = array();
$file_name='save_bill_items';
require_once("./include/membersite_config.php");
include("./include/functions.php");
//error_log(strip_tags("Error logging in. missing fields"), 0);
$merchant_code = ($_SESSION['merchant_code']);
$merchant_phone_number = ($_SESSION["merchant_phone_number"]);
$bill_processed_by = ($_SESSION["user_name"]);

require_once __DIR__ . '/db_config.php';
$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	
	//Check whether the phone number being billed exists and its active
 		
	$name_of_doctor = trim($_POST["name_of_doctor"]);
	$phone_number = trim($_POST["phone_number"]);
error_log(strip_tags("Error logging in. missing fields phone number is ".$phone_number), 0);
	$date_bill_created = date('Y-m-d H:i:s');
	$bill_number_flag = false;
	$total_bill=0;
	$total_bill_amount=0;
	$tableData = stripcslashes($_POST['pTableData']);

	$new_array=json_decode($tableData,true);
	$total_bill_amount = $_POST["total_bill_amount"];
	$total_no_of_points = 0;
	$tariff_amount=0;
	$tariff_total_amount=0;
	$sql= "SELECT name,surname,country_name FROM tbl_clients WHERE phone_number = :field_val"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
	$stmt->execute();

	$total = $stmt->rowCount();

 	if($total <= 0){
			
			//Check whether its a society or company that needs to have a bill created
				$sql= "SELECT merchant_name,merchant_country FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
				$stmt = $db->prepare($sql);
				$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();			
				
				if($total <= 0){
					$success = "false";
					$message = "Phone Number Does Not Exist..Please Check And Try Again!!";
					$_SESSION["message_create"] = $message;
					return false;
					
				}else{
					$success = "false";
					$message = "Phone Number Cannot Be Billed..Please Check And Try Again!!";
					$_SESSION["message_create"] = $message;
					return false;
					
				}//big else
	}else{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		foreach($result as $row){
			$name = $row['name'];
			$surname = $row['surname'];
			$country = $row['country_name'];
			}	
			

			$sql = "SELECT DISTINCT(bill_number) FROM `tbl_bill_items_tariff_records` WHERE phone_number_billed = :field_val ORDER BY 1 ASC";
	
			$stmt = $db->prepare($sql);
			$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
			$stmt->execute();
			$total = $stmt->rowCount();

			if($total > 0)
			{

				$bill_number = $stmt->fetchColumn();

				$bill_number_flag = true;

			}else{

					
					$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
					$bill_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
					$bill_number_generated = mt_rand(0, 999999);
					$bill_number=$bill_letter.$bill_number_generated;
		
					do
					{
						try
						{
						
							$db->beginTransaction();
							$stmt = $db->prepare("INSERT INTO tbl_bill_numbers (bill_number) 
									VALUES (:bill_number)");
    							$stmt->bindParam(':bill_number', $bill_number);
							$stmt->execute();
							$total =  $stmt->rowCount();
							if($total > 0)
							{
								$result=true;
							}
							$db->commit();
						}catch(PDOException $e){
					
							if ($e->errorInfo[1] == 1062) 
							{
								$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
								$bill_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
								$bill_number_generated = mt_rand(0, 999999);
								$bill_number=$bill_letter.$bill_number_generated;
								$result=false;
							}else{
								$success = "false";
		 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 						$_SESSION["message_create"] = $message;
		 						$db->rollBack();
								return false;
							}
						}
				
					} while ($result == false);

			}	


		if($bill_number_flag == true){


			$sql = "DELETE from tbl_bill_items_tariff_records Where bill_number='$bill_number'";
    	                $stmt_delete = $db->prepare($sql);
	                $stmt_delete->execute();                                               
                        $total_delete =  $stmt_delete->rowCount();
                                                		
		}							
		//error_log(strip_tags("Error logging in. missing fields"), 0); 
		$first_last_name = $name. " " .$surname;
															
		//Get the currency as per country and no_of_points
		$sql_country= "SELECT amount,no_of_points FROM tbl_currency_rates WHERE country_currency = :field_val"; 
		$stmt_country = $db->prepare($sql_country);
		$stmt_country->bindParam(':field_val', $country, PDO::PARAM_STR); 
		$stmt_country->execute();
		$total_country = $stmt_country->rowCount();
		if($total_country > 0)
		{
			$result = $stmt_country->fetchAll(PDO::FETCH_ASSOC);
			foreach($result as $row){
			$rate_amount = $row['amount'];
			$no_of_points_rate= $row['no_of_points'];				
			}	
															
		}
		//error_log(strip_tags("Error logging in. missing fields 2"), 0); 
		$total_no_of_points=0;


	foreach($new_array as $row)
	{
		$date_of_transaction = $row['bill_date'];

		$dt   = new DateTime();
		$timestamp = strtotime($date_of_transaction); 
		$new_date = date('Y-m-d', $timestamp);
		//$date = $dt->createFromFormat('d-m-Y', $date_of_transaction);
		//$newDate = $date->format('Y-m-d');
		//$temp["transaction_date"] = $date_of_transaction;
		$temp["transaction_date"] = $new_date;
		$temp["merchant_phone_number"] = $merchant_phone_number;
		$temp["merchant_code"] = $merchant_code;
		$temp["merchant_doctor"] = $name_of_doctor;
		$temp["bill_number"] = $bill_number;
		$temp["phone_number_billed"] = $phone_number;
		$temp["tariff_description"] = $row['description'];
		$tariff_amount=$row['price'];
	error_log(strip_tags("Error logging in. miamount  ".$tariff_amount), 0); 
		$temp["tariff_amount"]=$tariff_amount;
		$tariff_total_amount=$row['total_price'];
error_log(strip_tags("Error logging in. total amount  ".$tariff_total_amount), 0); 
		$temp["tariff_total_amount"]=$tariff_total_amount;
		$temp["tariff_code"] = $row['code'];
		$temp["tariff_code_frequency"] = $row['sNo'];
		$no_of_points = $row['total_price'] / $rate_amount;
		$no_of_points = round($no_of_points,0);
		$temp["no_of_points"]=$no_of_points;
		array_push($response, $temp);
	}

	try
		{

			$db->beginTransaction();
			$sql = "INSERT INTO tbl_bill_items_tariff_records (transaction_date, merchant_phone_number, merchant_code, merchant_doctor, bill_number, phone_number_billed, tariff_description, tariff_amount, tariff_total_amount, tariff_code, tariff_code_frequency,no_of_points) VALUES ";

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
							
							$success = "true";
							$message = "Congratulations You Have Saved The Bill Items For ."." ".$first_last_name." ";
							$_SESSION["message_create"] = $message;
							$db->commit();
							return true;
						
			}
			//$db->commit();
			}catch(PDOException $e){
					
					if ($e->errorInfo[1] == 1062) 
							{

								$success = "false";
		 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
								$error_message=$e->getMessage();
								error_log(strip_tags("Error logging in. missing fields 2 ".$error_message), 0); 
		 						$_SESSION["message_create"] = $message;
		 						$db->rollBack();
								return false;
								
							}else{

								$success = "false";
		 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
								$error_message=$e->getMessage();
								error_log(strip_tags("Error logging in. missing fields 3 ".$error_message), 0); 
		 						$_SESSION["message_create"] = $message;
		 						$db->rollBack();
								return false;
							}
			}
	}

?>