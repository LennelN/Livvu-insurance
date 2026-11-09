<?php 
$response = array();
$response_back = array();
$group_total = 200;
include 'functions.php';
$file_name='insert_society_company_members';
$member_phone_number_does_not_exist = false;
if (isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code'])  && isset($_POST['array']))
{

	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';
	
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	//Check whether the phone number being billed exists and its active
 		
		$merchant_phone_number = $_POST["merchant_phone_number"];
		$merchant_code = $_POST["merchant_code"];
		$receiver_data=$_POST['array'];
		$new_array=json_decode($receiver_data,true);

		foreach($new_array as $row)
		{
			$temp["merchant_phone_number"] = $merchant_phone_number;
			$temp["merchant_code"] = $merchant_code;
			$member_phone_number = trim($row['sPhoneNo']);
			$member_country= trim($row['sCountry']);
			$sql_details= "SELECT phone_number FROM tbl_clients WHERE tbl_clients.phone_number = :field_val AND tbl_clients.member_active = 'Y' AND tbl_clients.country_name = :field_val1"; 
							$stmt_details = $db->prepare($sql_details);
							$stmt_details->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR); 
							$stmt_details->bindParam(':field_val1', $member_country, PDO::PARAM_STR); 
							$stmt_details->execute();
							$total_details = $stmt_details->rowCount();
							if($total_details > 0)
							{
								$temp["member_phone_number"] = $member_phone_number;
								$temp["member_phone_country"] = $member_country;
							}else{
								$member_phone_number_does_not_exist = true;
								break;
							}
							
			array_push($response, $temp);
		}

		if($member_phone_number_does_not_exist == false){

		try
		{
			$db->beginTransaction();
			$sql = "INSERT INTO tbl_society_company_members (merchant_phone_number, merchant_code, member_phone_number,member_phone_country) VALUES ";

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
				$message = "Congratulations You Have Successfully Registered Your Members";
				array_push($response_back,array("success"=>$success,"message"=>$message));
				echo json_encode($response_back);
			}
			$db->commit();
			}catch(PDOException $e){
					
					if ($e->errorInfo[1] == 1062) 
							{
								$success = "false";
		 						$error_message=$e->getMessage();
								$p_len = strlen($error_message);
								$message_duplicate = $p_len - 70;
								$p_test= substr($error_message,70,$message_duplicate);
								$y=1;
								$num="";
								for ($i = 0; $i < $message_duplicate; $i++) {
									$duplicate_key_2 = substr($p_test,$y,1);
									if($duplicate_key_2 == "'")
									{
										break;
									}
									$num = $num.$duplicate_key_2;
									$y++;
							}
								$message = "Phone Number ".$num." Has Already Been Added Into The Group. Please Verify Phone Number";
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
}else{

	 $success = "false";
	//$db->rollBack();
    	$message = "Phone Number ".$member_phone_number." Is Not Yet Registered In The System. ";
	array_push($response_back,array("success"=>$success,"message"=>$message));
    	echo json_encode($response_back);
}

} else {
    // required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
		array_push($response_back,array("success"=>$success,"message"=>$message));
    echo json_encode($response_back);
}

?>