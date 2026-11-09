<?php
  
$response = array();
$file_name='register_billing_request';
include 'functions.php';

if (isset($_POST['username']) && isset($_POST['country']) && isset($_POST['country_name']) && isset($_POST['region']) && isset($_POST['nameOfHealth']) && isset($_POST['merchantCode']) && isset($_POST['merchantPhoneNo'])) {
	$username = $_POST['username'];
	$usercountry = $_POST['country'];
	$nameofcountry_health= $_POST["country_name"];
	$nameofregion_health= $_POST['region'];
	$nameofhealth_institution=$_POST['nameOfHealth'];
	$codeofhealth_institution=$_POST['merchantCode'];
	$phonenoofhealth_institution=$_POST['merchantPhoneNo'];

	error_log(strip_tags("cominginhealth is ".$nameofcountry_health."and region is ".$nameofregion_health." and ".$nameofhealth_institution), 0);
	error_log(strip_tags("cominginhealth2 is ".$codeofhealth_institution."and phone is ".$phonenoofhealth_institution." and username is ".$username." and ".$usercountry), 0);
	$ddate = Date("Y-m-d");
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
   	 // set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();


	$sql_name= "SELECT name,surname FROM tbl_clients WHERE phone_number = :field_val AND country_name =  :field_val1"; 
	$stmt_name = $db->prepare($sql_name);
	$stmt_name->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt_name->bindParam(':field_val1', $usercountry, PDO::PARAM_STR); 
	$stmt_name->execute();
	$total_name = $stmt_name->rowCount();
	
	if($total_name > 0){
		$result_name = $stmt_name->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result_name as $row)
		{    							
			$fname = $row['name'];	
			$lname = $row['surname'];	

		}						
	
	}
	$fullname = $fname." ".$lname;			

	$sql_billing= "SELECT date_bill_created,merchant_name,bill_number,total_bill_amount FROM tbl_bill_client_records WHERE phone_number = :field_val AND country = :field_val1 AND bill_paid_indicator = 'N'"; 
	$stmt_billing = $db->prepare($sql_billing);
	$stmt_billing->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt_billing->bindParam(':field_val1', $usercountry, PDO::PARAM_STR); 

	$stmt_billing->execute();
	$total_billing = $stmt_billing->rowCount();
					
	if($total_billing > 0)
	{

		$result_billing = $stmt_billing->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result_billing as $row)
		{    							
			$client_date_bill_returned=$row['date_bill_created'];
			$client_merchant_name_returned=$row['merchant_name'];
			$client_bill_number_returned=$row['bill_number'];
			$client_total_bill_returned=$row['total_bill_amount'];

		}
		$success = "false";
		$message = " You Have An Outstanding Bill From ".$client_merchant_name_returned." Amount Is ".$client_total_bill_returned." Billed On The ".$client_date_bill_returned. " Please Contact Livvu Office For More Information";
		array_push($response,array("success"=>$success,"message"=>$message));
		echo json_encode($response);

	}else{
	$sql= "SELECT phone_number,MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val1 AND purchase_country = :field_val2"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val1', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val2', $usercountry, PDO::PARAM_STR);

	$stmt->execute();
	$total = $stmt->rowCount();

	if($total > 0) 
	{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row)
		{    							
			$client_phone_number_returned=$row['phone_number'];
			$client_due_date_returned=$row['MAX(due_date)'];
		}

		error_log(strip_tags("comingin due date is ".$client_due_date_returned), 0);
		$sql_year= "SELECT phone_number,MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2"; 
		$stmt_year = $db->prepare($sql_year);
		$stmt_year->bindParam(':field_val1', $username, PDO::PARAM_STR); 
		$stmt_year->bindParam(':field_val2', $usercountry, PDO::PARAM_STR);

		$stmt_year->execute();
		$total_year = $stmt_year->rowCount();

		if($total_year > 0) 
		{
			$result_year = $stmt_year->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result_year as $row)
			{    							
				$client_phone_number_returned=$row['phone_number'];
				$client_end_date_returned=$row['MAX(end_date)'];
			}
		}
		
		error_log(strip_tags("comingin due date is2 ".$client_end_date_returned), 0);
		if($ddate > $client_due_date_returned){
			$success = "false";
			$message = " Your Account Is Not Up To Date. Please Pay Your Subscriptions To Update Your Account And Get Treatment";
			array_push($response,array("success"=>$success,"message"=>$message));
			echo json_encode($response);
		}else{

			$sql_request= "SELECT merchant_name FROM tbl_billing_requests WHERE phone_number = :field_val AND country =  :field_val1 AND bill_processed = 'N' AND bill_valid = 'Y'"; 
			$stmt_request = $db->prepare($sql_request);
			$stmt_request->bindParam(':field_val', $username, PDO::PARAM_STR); 
			$stmt_request->bindParam(':field_val1', $usercountry, PDO::PARAM_STR); 
			$stmt_request->execute();
			$total_request = $stmt_request->rowCount();
	error_log(strip_tags("comingin merchant bill name is1  "), 0);
			if($total_request > 0){
			
				$merchant_name_bill = $stmt_request->fetchColumn();
				error_log(strip_tags("comingin merchant bill name is  ".$merchant_name_bill), 0);
				if($merchant_name_bill == $nameofhealth_institution)
				{
					error_log(strip_tags("comingin merchant bill name is2  ".$merchant_name_bill), 0);
					$success = "false";
					$message = " You Already Have A Billing Number For Institution called ".$merchant_name_bill." Please Go Ahead And Get Treatment ";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
				}else{
					//delete the record from the previous billing request and enter a new record
error_log(strip_tags("comingin merchant bill name is 3 "), 0);
					try
					{
						$sql_delete = "DELETE from tbl_billing_requests WHERE phone_number = :field_val AND country =  :field_val1 AND bill_processed = 'N' AND bill_valid = 'Y'";
    	                					$stmt_delete = $db->prepare($sql_delete);
						$stmt_delete->bindParam(':field_val', $username, PDO::PARAM_STR); 
						$stmt_delete->bindParam(':field_val1', $usercountry, PDO::PARAM_STR);

	                					$stmt_delete->execute();                                               
                        					$total_delete =  $stmt_delete->rowCount();
						
					}catch(PDOException $e){
						$success = "false";
		 				$message = "An Error Occurred Processing Transaction Please Try Again Later";
						$_SESSION["message_create"] = $message;
		 				$error_message=$e->getMessage();
		 				$db->rollBack();
						$result=false;
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);							
					}

				$sql_balance= "SELECT phone_number,amount FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2 AND end_date = :field_val3"; 
				$stmt_balance = $db->prepare($sql_balance);
				$stmt_balance->bindParam(':field_val1', $username, PDO::PARAM_STR); 
				$stmt_balance->bindParam(':field_val2', $usercountry, PDO::PARAM_STR);
				$stmt_balance->bindParam(':field_val3', $client_end_date_returned, PDO::PARAM_STR);

				$stmt_balance->execute();
				$total_balance = $stmt_balance->rowCount();

				if($total_balance > 0) 
				{
					$result_balance = $stmt_balance->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result_balance as $row)
					{    							
						$client_phone_number_returned=$row['phone_number'];
						$client_balance_returned=$row['amount'];
					}
				}

				$sql= "SELECT merchant_phone_number,merchant_code FROM tbl_merchant_details WHERE merchant_country  = :field_val AND merchant_region =  :field_val1 AND merchant_name = :field_val2 AND type_of_registration = 'Health'"; 
				$stmt = $db->prepare($sql);
				$stmt->bindParam(':field_val', $nameofcountry_health, PDO::PARAM_STR); 
				$stmt->bindParam(':field_val1', $nameofregion_health, PDO::PARAM_STR); 
				$stmt->bindParam(':field_val2', $nameofhealth_institution, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();
	
				if($total > 0){
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result as $row)
					{    							
						$merchant_phone_number_to_bill = $row['merchant_phone_number'];	
						$merchant_code_to_bill = $row['merchant_code'];	

					}						
	
				}					

error_log(strip_tags("cominginhealth phone no is ".$merchant_phone_number_to_bill."and code is ".$merchant_code_to_bill." and ".$nameofhealth_institution), 0);

				$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				$bill_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
				$bill_number_generated = mt_rand(0, 999999);
				$bill_number=$bill_letter.$bill_number_generated;

				error_log(strip_tags("comingin bill number generated is ".$bill_number), 0);
	

				do
				{
					try
					{
						
						//$db->beginTransaction();
						$stmt = $db->prepare("INSERT INTO tbl_bill_numbers (bill_number) 
						VALUES (:bill_number)");
						$stmt->bindParam(':bill_number', $bill_number);
						$stmt->execute();
						$total =  $stmt->rowCount();
						if($total > 0)
						{
							$result=true;
						}
						//$db->commit();
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
							$error_message=$e->getMessage();
							$db->rollBack();
							$result=false;
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
						
						}
					}
				
				} while ($result == false);


				$sql_livvu_user= "SELECT user_login_id  FROM tbl_users WHERE user_role = 'Livvu'"; 
				$stmt_livvu_user = $db->prepare($sql_livvu_user);
				$stmt_livvu_user->execute();
				$total_livvu = $stmt_livvu_user->rowCount();

				if($total_livvu > 0) {

					$bill_processed_by=$stmt_livvu_user->fetchColumn();
				}


	
				$bill_processed="N";
				$bill_completed="N";
				$bill_valid="Y";
				$transaction_id_insert=0;
error_log(strip_tags("comingin livvu username is ".$bill_processed_by), 0);
				try
				{
					$stmt_create_bill = $db->prepare("INSERT INTO tbl_billing_requests(transaction_id, 
								phone_number, 
								name, 
								country, 
								bill_number, 
								bill_date_requested, 
								bill_processed, 
								bill_valid, 
								bill_completed, 
								merchant_phone_no, 
								merchant_code,
								merchant_name, 
								bill_generated_by) 
					VALUES (:transaction_id, 
						:phone_number, 
						:name, 
						:country, 
						:bill_number, 
						:bill_date_requested, 
						:bill_processed, 
						:bill_valid, 
						:bill_completed, 
						:merchant_phone_no, 
						:merchant_code,
						:merchant_name, 
						:bill_generated_by)");
					
					$stmt_create_bill->bindParam(':transaction_id', $transaction_id_insert);
					$stmt_create_bill->bindParam(':phone_number', $username);
					$stmt_create_bill->bindParam(':name', $fullname);
					$stmt_create_bill->bindParam(':country', $usercountry);
					$stmt_create_bill->bindParam(':bill_number', $bill_number);
					$stmt_create_bill->bindParam(':bill_date_requested', $ddate);
					$stmt_create_bill->bindParam(':bill_processed', $bill_processed);
					$stmt_create_bill->bindParam(':bill_valid', $bill_valid);
					$stmt_create_bill->bindParam(':bill_completed', $bill_completed);
					$stmt_create_bill->bindParam(':merchant_phone_no', $merchant_phone_number_to_bill);
					$stmt_create_bill->bindParam(':merchant_code', $merchant_code_to_bill);
					$stmt_create_bill->bindParam(':merchant_name', $nameofhealth_institution);
					$stmt_create_bill->bindParam(':bill_generated_by', $bill_processed_by);
					$stmt_create_bill->execute();
					$total_create_bill =  $stmt_create_bill->rowCount();
		error_log(strip_tags("comingin livvu username is 2 ".$bill_processed_by), 0);
					if($total_create_bill > 0)
					{	

						$success = "true";
						$message = "Your Billing Request Has Been Successfully Processed. You Can Go Ahead And Get Treatment At ".$nameofhealth_institution;
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
					}

					$db->commit();
									
				}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Insert A Duplicate Value";
							//$message = $e->getMessage();
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							$error_message=$e->getMessage();
							$error_type='duplicate';
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later Duplicate");
							$db->rollBack();
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							
						
						}else{	
	
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later ");
							$message=$e->getMessage();
							error_log(strip_tags($message), 0);
							$error_type='exception';
							$db->rollBack();
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							//echo json_encode($response);
							
							
						}
				}
					

				}

			}else{

				$sql_balance= "SELECT phone_number,amount FROM tbl_year_contributions WHERE phone_number = :field_val1 AND country = :field_val2 AND end_date = :field_val3"; 
				$stmt_balance = $db->prepare($sql_balance);
				$stmt_balance->bindParam(':field_val1', $username, PDO::PARAM_STR); 
				$stmt_balance->bindParam(':field_val2', $login_country, PDO::PARAM_STR);
				$stmt_balance->bindParam(':field_val3', $client_end_date_returned, PDO::PARAM_STR);

				$stmt_balance->execute();
				$total_balance = $stmt_balance->rowCount();

				if($total_balance > 0) 
				{
					$result_balance = $stmt_balance->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result_balance as $row)
					{    							
						$client_phone_number_returned=$row['phone_number'];
						$client_balance_returned=$row['amount'];
					}
				}

				$sql= "SELECT merchant_phone_number,merchant_code FROM tbl_merchant_details WHERE merchant_country  = :field_val AND merchant_region =  :field_val1 AND merchant_name = :field_val2 AND type_of_registration = 'Health'"; 
				$stmt = $db->prepare($sql);
				$stmt->bindParam(':field_val', $nameofcountry_health, PDO::PARAM_STR); 
				$stmt->bindParam(':field_val1', $nameofregion_health, PDO::PARAM_STR); 
				$stmt->bindParam(':field_val2', $nameofhealth_institution, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();
	
				if($total > 0){
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result as $row)
					{    							
						$merchant_phone_number_to_bill = $row['merchant_phone_number'];	
						$merchant_code_to_bill = $row['merchant_code'];	

					}						
	
				}					

error_log(strip_tags("cominginhealth phone no is ".$merchant_phone_number_to_bill."and code is ".$merchant_code_to_bill." and ".$nameofhealth_institution), 0);

				$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				$bill_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
				$bill_number_generated = mt_rand(0, 999999);
				$bill_number=$bill_letter.$bill_number_generated;

				error_log(strip_tags("comingin bill number generated is ".$bill_number), 0);
	

				do
				{
					try
					{
						
						//$db->beginTransaction();
						$stmt = $db->prepare("INSERT INTO tbl_bill_numbers (bill_number) 
						VALUES (:bill_number)");
						$stmt->bindParam(':bill_number', $bill_number);
						$stmt->execute();
						$total =  $stmt->rowCount();
						if($total > 0)
						{
							$result=true;
						}
						//$db->commit();
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
							///$_SESSION["message_create"] = $message;
							$error_message=$e->getMessage();
							$db->rollBack();
							$result=false;
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
						
						}
					}
				
				} while ($result == false);


				$sql_livvu_user= "SELECT user_login_id  FROM tbl_users WHERE user_role = 'Livvu'"; 
				$stmt_livvu_user = $db->prepare($sql_livvu_user);
				$stmt_livvu_user->execute();
				$total_livvu = $stmt_livvu_user->rowCount();

				if($total_livvu > 0) {

					$bill_processed_by=$stmt_livvu_user->fetchColumn();
				}


	
				$bill_processed="N";
				$bill_completed="N";
				$bill_valid="Y";
				$transaction_id_insert=0;
error_log(strip_tags("comingin livvu username is ".$bill_processed_by), 0);
				try
				{
					$stmt_create_bill = $db->prepare("INSERT INTO tbl_billing_requests(transaction_id, 
								phone_number, 
								name, 
								country, 
								bill_number, 
								bill_date_requested, 
								bill_processed, 
								bill_valid, 
								bill_completed, 
								merchant_phone_no, 
								merchant_code, 
								merchant_name,
								bill_generated_by) 
					VALUES (:transaction_id, 
						:phone_number, 
						:name, 
						:country, 
						:bill_number, 
						:bill_date_requested, 
						:bill_processed, 
						:bill_valid, 
						:bill_completed, 
						:merchant_phone_no, 
						:merchant_code, 
						:merchant_name,
						:bill_generated_by)");
					
					$stmt_create_bill->bindParam(':transaction_id', $transaction_id_insert);
					$stmt_create_bill->bindParam(':phone_number', $username);
					$stmt_create_bill->bindParam(':name', $fullname);
					$stmt_create_bill->bindParam(':country', $usercountry);
					$stmt_create_bill->bindParam(':bill_number', $bill_number);
					$stmt_create_bill->bindParam(':bill_date_requested', $ddate);
					$stmt_create_bill->bindParam(':bill_processed', $bill_processed);
					$stmt_create_bill->bindParam(':bill_valid', $bill_valid);
					$stmt_create_bill->bindParam(':bill_completed', $bill_completed);
					$stmt_create_bill->bindParam(':merchant_phone_no', $merchant_phone_number_to_bill);
					$stmt_create_bill->bindParam(':merchant_code', $merchant_code_to_bill);
					$stmt_create_bill->bindParam(':merchant_name', $nameofhealth_institution);
					$stmt_create_bill->bindParam(':bill_generated_by', $bill_processed_by);
					$stmt_create_bill->execute();
					$total_create_bill =  $stmt_create_bill->rowCount();
		error_log(strip_tags("comingin livvu username is 2 ".$bill_processed_by), 0);
					if($total_create_bill > 0)
					{	

						$success = "true";
						$message = "Your Billing Request Has Been Successfully Processed. You Can Go Ahead And Get Treatment At ".$nameofhealth_institution;
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
					}

					$db->commit();
									
				}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Insert A Duplicate Value";
							//$message = $e->getMessage();
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							$error_message=$e->getMessage();
							$error_type='duplicate';
							$db->rollBack();
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later Duplicate");
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							
						
						}else{	
	
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later ");
							$message=$e->getMessage();
							//error_log(strip_tags($message), 0);
							$error_type='exception';
							$db->rollBack();
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
							//echo json_encode($response);
							
							
						}
				}
			}

		}
	}else{
		$success = "false";
		$message = " Your Account Is Not Up To Date. Please Pay Your Monthly Subscriptions And Update Your Account And Get Treatment";
		array_push($response,array("success"=>$success,"message"=>$message));
		echo json_encode($response);
	}
	}
}else{

$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);

}

?>