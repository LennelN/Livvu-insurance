<?php 
$response = array();
$response_back = array();
$group_total = 200;
include 'functions.php';
$file_name='create_bill_record';

// check for required fields
if (isset($_POST['merchant_phone_number']) && isset($_POST['bill_processed_by']) && isset($_POST['merchant_code']) && isset($_POST['name_of_doctor']) && isset($_POST['phone_bill_no']) && isset($_POST['total_bill_amount']) && isset($_POST['array']) && isset($_POST['doctor_phone_no']) && isset($_POST['bill_no']) && isset($_POST['country_phone_no']) && isset($_POST['account_balance']))
{
	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';
	//$db = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	//Check whether the phone number being billed exists and its active
 		
	$merchant_phone_number = $_POST["merchant_phone_number"];
	$merchant_code = $_POST["merchant_code"];
	$name_of_doctor = $_POST["name_of_doctor"];
	$phone_number = $_POST["phone_bill_no"];
	$total_bill_amount = $_POST["total_bill_amount"];
	$bill_number_flag = false;
	$doctor_phone_no= $_POST["doctor_phone_no"];
	$bill_no = $_POST["bill_no"];
	$country_phone_no=$_POST["country_phone_no"];
	$account_balance = $_POST['account_balance'];
	//$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	//$bill_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	//$bill_number_generated = mt_rand(0, 999999);
	//$bill_number=$bill_letter.$bill_number_generated;
	$date_bill_created = date('Y-m-d H:i:s');
	$receiver_data=$_POST['array'];
	$new_array=json_decode($receiver_data,true);
	$total_no_of_points = 0;
	$total_no_of_points_paid=0;
 $transaction_id_insert=0;
	$bill_processed_by = $_POST["bill_processed_by"];

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
	$stmt->bindParam(':field_val1', $country_phone_no, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

 	if($total <= 0){
			
			//Check whether its a society or company that needs to have a bill created
				$sql= "SELECT merchant_name,merchant_country FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
				$stmt = $db->prepare($sql);
				$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
				$stmt->bindParam(':field_val1', $country_phone_no, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();			
				
				if($total <= 0){
					$success = "false";
					$message = "Phone Number Does Not Exist..Please Check And Try Again!!";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
				}else{
					$success = "false";
					$message = "Phone Number Cannot Be Billed..Please Check And Try Again!!";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
					
				}//big else
	}else{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		foreach($result as $row){
			$name = $row['name'];
			$surname = $row['surname'];
			$country = $row['country_name'];
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
				//$date = $dt->createFromFormat('d/m/Y', $date_of_transaction);
				$timestamp = strtotime($date_of_transaction); 
				$new_date = date('Y-m-d', $timestamp);
				$temp["transaction_id"] = $transaction_id_insert;
				$temp["transaction_date"] = $new_date;
				$temp["merchant_phone_number"] = $merchant_phone_number;
				$temp["merchant_code"] = $merchant_code;
				$temp["merchant_country"] = $merchant_country;
				$temp["merchant_doctor"] = $doctor_phone_no;
				$temp["bill_number"] = $bill_no;
				$temp["phone_number_billed"] = $phone_number;
				$temp["phone_number_billed_country"] = $country_phone_no;
				$temp["tariff_description"] = $row['description'];
				$temp["tariff_amount"]=$row['price'];
				$temp["tariff_total_amount"]=$row['total_price'];
				$temp["tariff_code"] = $row['code'];
				$temp["tariff_code_frequency"] = $row['sNo'];
				$temp["date_bill_created"]=$date_bill_created;
				$temp["no_of_points"]=$row['total_price'];
				//$total_no_of_points = $total_no_of_points + $no_of_points;
				//$total_bill_amount = $total_bill_amount + $row['total_price'];		
				array_push($response, $temp);
			}


	try
		{
			$db->beginTransaction();
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
				//$bill_number = '881429';


				
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
					$stmt->bindParam(':phone_number_billed_country', $country_phone_no);
					$stmt->bindParam(':date_bill_created', $date_bill_created);
					$stmt->bindParam(':merchant_code', $merchant_code);
					$stmt->bindParam(':merchant_country', $merchant_country);
					$stmt->bindParam(':merchant_doctor', $doctor_phone_no);
    					$stmt->bindParam(':bill_number', $bill_no);
					$stmt->bindParam(':total_bill_amount', $total_bill_amount);
					$stmt->bindParam(':bill_amount', $total_bill_amount);
					$stmt->bindParam(':bill_paid_indicator', $bill_paid_indicator);
					$stmt->bindParam(':bill_processed_by', $bill_processed_by);

					$stmt->execute();
					$total =  $stmt->rowCount();
					if($total > 0)
						{			
							$field_val = ($merchant_phone_number);
							$sql= "SELECT merchant_name FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1"; 
							$stmt = $db->prepare($sql);
							$stmt->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
							$stmt->execute();
							$total = $stmt->rowCount();
					
							if($total > 0){
									$merchant_name = $stmt->fetchColumn();
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
									$stmt->bindParam(':country', $country_phone_no);
    									$stmt->bindParam(':merchant_phone_number', $merchant_phone_number);												$stmt->bindParam(':merchant_code', $merchant_code);
									$stmt->bindParam(':merchant_name', $merchant_name);
									$stmt->bindParam(':merchant_country', $merchant_country);
    									$stmt->bindParam(':bill_number', $bill_no);
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
										$stmt_delete->bindParam(':field_val1', $country_phone_no, PDO::PARAM_STR);
										$stmt_delete->bindParam(':field_val2', $bill_no, PDO::PARAM_STR);

					            					$stmt_delete->execute();                                               
                  									$total_delete =  $stmt_delete->rowCount();				
										if($total_delete > 0)
										{
											$success = "true";
											$message = "Congratulations You Have Successfully Billed "." ".$first_last_name." ";
											array_push($response_back,array("success"=>$success,"message"=>$message,"total_amount_billed"=>$total_bill_amount));
											echo json_encode($response_back);
										}
									}
								}
							}
				}
			$db->commit();
			}catch(PDOException $e){
					
					if ($e->errorInfo[1] == 1062) 
							{
								$success = "false";
		 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 							$error_message=$e->getMessage();
		 							$error_type='duplicate';
		 							handler($error_message,$file_name,$success,$error_type);
		 						$db->rollBack();
								array_push($response_back,array("success"=>$success,"message"=>$message));
								echo json_encode($response_back);
								
							}else{
								$success = "false";
		 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
		 							$error_message=$e->getMessage();
		 							$error_type='exception';
		 							handler($error_message,$file_name,$success,$error_type);
		 						$db->rollBack();
								array_push($response_back,array("success"=>$success,"message"=>$message));
								echo json_encode($response_back);
								//$db->rollBack();
							}
			}
	}
} else {
    // required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
		array_push($response_back,array("success"=>$success,"message"=>$message));
    echo json_encode($response_back);
}

?>