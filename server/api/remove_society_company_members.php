<?php 
$response = array();
$response_back = array();
include 'functions.php';
$file_name='remove_society_company_members';

if (isset($_POST['merchant_phone_number']) && isset($_POST['merchant_code'])  && isset($_POST['array']) )
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
			$temp["member_phone_number"] = $row['sPhoneNo'];
			$temp["member_phone_country"] = $row['sCountry'];
			array_push($response, $temp);
		}

		try
		{
			$db->beginTransaction();
			$sql = "DELETE FROM tbl_society_company_members WHERE member_phone_number IN ";
			
			$paramArray = array();

			$sqlArray = array();

			foreach($response as $row)
				{
					$what = $row['member_phone_number'];
    					$sqlArray[] = implode($row, ',');
		
				}			

			//$db->execute('DELETE from tbl_society_company_members WHERE member_phone_number IN ?', [implode(',' ,$paramArray)]);
 
			$sql .= '('.implode(',', $sqlArray).')';


			$stmt = $db->prepare($sql);

			//$stmt->execute($paramArray);
			$stmt->execute();
			$total =  $stmt->rowCount();
		
			if($total > 0)
			{//insert into tbl_bill_merchant_records
				$success = "true";
				$message = "Congratulations You Have Successfully Removed Members From The Company/Society ";
				array_push($response_back,array("success"=>$success,"message"=>$message));
				echo json_encode($response_back);
			}else{

				$success = "false";
				$message = "You Tried To Remove A Phone Number That Is Not Registered ";
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

} else {
    // required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
		array_push($response_back,array("success"=>$success,"message"=>$message));
    echo json_encode($response_back);
}

?>