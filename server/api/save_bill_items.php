<?php 
$response = array();
$response_back = array();
include 'functions.php';
$file_name='save_bill_items';

// check for required fields
if ( isset($_POST['merchant_phone_number']) && isset($_POST['bill_processed_by']) && isset($_POST['merchant_code']) && isset($_POST['name_of_doctor']) && isset($_POST['phone_bill_no']) && isset($_POST['total_bill_amount']) && isset($_POST['array']) )
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
	$date_bill_created = date('Y-m-d H:i:s');
	$receiver_data=$_POST['array'];
	$new_array=json_decode($receiver_data,true);
	$total_no_of_points = 0;
	$bill_processed_by = $_POST["bill_processed_by"];
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
		 						$error_message=$e->getMessage();
		 						$error_type='exception';
		 						handler($error_message,$file_name,$success,$error_type);
		 						$db->rollBack();
								$result=false;
								array_push($response_back,array("success"=>$success,"message"=>$message));
								echo json_encode($response_back);
								//$db->rollBack();
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
		$date = $dt->createFromFormat('d/m/Y', $date_of_transaction);
		$newDate = $date->format('Y-m-d');
		$temp["transaction_date"] = $newDate;
		$temp["merchant_phone_number"] = $merchant_phone_number;
		$temp["merchant_code"] = $merchant_code;
		$temp["merchant_doctor"] = $name_of_doctor;
		$temp["bill_number"] = $bill_number;
		$temp["phone_number_billed"] = $phone_number;
		$temp["tariff_description"] = $row['description'];
		$temp["tariff_amount"]=$row['price'];
		$temp["tariff_total_amount"]=$row['total_price'];
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
							array_push($response_back,array("success"=>$success,"message"=>$message));
							echo json_encode($response_back);
						
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