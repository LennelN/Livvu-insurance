<?php 
session_start();
$response = array();
$response_back = array();
$file_name='insert_society_company_members';
require_once("./include/membersite_config.php");
include("./include/functions.php");

$merchant_code = ($_SESSION['merchant_code']);
$merchant_phone_number = ($_SESSION["user_name"]);
$_SESSION["message_create"] =" ";
$message2="The Following Numbers Are Already Registered For The Society ";
require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->beginTransaction();
   // include("./include/config.php");
		$phone_number = trim($_POST["phone_number"]);
		$tableData = stripcslashes($_POST['pTableData']);

		//$tableData = json_decode($tableData,TRUE);
		$new_array=json_decode($tableData,true);
		$total_no_of_points = 0;
		$member_phone_number_does_not_exist="false";
		$member_phone_number_does_not_exist_in_database="false";

		if( sizeof($new_array) != 0 )
		{
				
			foreach($new_array as $row)
			{
				$temp["merchant_phone_number"] = $merchant_phone_number;
				$temp["merchant_code"] = $merchant_code;

						
				$phone_no = $row['phone_no'];
				$phone_country = $row['phone_country'];
				


				$sql_details= "SELECT phone_number FROM tbl_clients WHERE tbl_clients.phone_number = :field_val AND tbl_clients.member_active = 'Y' AND tbl_clients.country_name = :field_val1"; 
				$stmt_details = $db->prepare($sql_details);
				$stmt_details->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
				$stmt_details->bindParam(':field_val1', $phone_country, PDO::PARAM_STR); 
				$stmt_details->execute();
				$total_details = $stmt_details->rowCount();
				if($total_details > 0)
				{
										
					$sql_registered= "SELECT member_phone_number FROM tbl_society_company_members WHERE member_phone_number = :field_val AND member_phone_country = :field_val1 AND merchant_phone_number = :field_val2"; 
					$stmt_registered = $db->prepare($sql_registered);
					$stmt_registered->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
					$stmt_registered->bindParam(':field_val1', $phone_country, PDO::PARAM_STR); 
					$stmt_registered->bindParam(':field_val2', $merchant_phone_number, PDO::PARAM_STR); 
					$stmt_registered->execute();
					$total_registered = $stmt_registered->rowCount();
					if($total_registered > 0)
					{
						
						$member_phone_number_does_not_exist = "true";
						//$message2 = $message2.$phone_no;
						break;
					}else{
						
						$member_phone_number_does_not_exist = "false";
						$temp["member_phone_number"] = $phone_no;
						$temp["member_phone_country"] = $phone_country;
						array_push($response, $temp);
					}
				}else{
					$member_phone_number_does_not_exist_in_database="true";
					$member_phone_number_does_not_exist = "true";
					break;
				}
			}


			if($member_phone_number_does_not_exist == "false")
			{

				try
				{
					
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
						//array_push($response_back,array("success"=>$success,"message"=>$message));
						$_SESSION["message_create"] = $message;
						//echo json_encode($message);
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
						for ($i = 0; $i < $message_duplicate; $i++) 
						{
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
 						//handler($error_message,$file_name,$success,$error_type);
 						$db->rollBack();
						//array_push($response_back,array("success"=>$success,"message"=>$message));
						$_SESSION["message_create"] = $message;
						//echo json_encode($message);
								
					}else{
						$success = "false";
 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
						$error_message=$e->getMessage();
						$error_type='exception';
						//handler($error_message,$file_name,$success,$error_type);
						$db->rollBack();
						//array_push($response_back,array("success"=>$success,"message"=>$message));
						$_SESSION["message_create"] = $message;
						//echo json_encode($message);
						//$db->rollBack();
					}
				}
			}else{
				
				if($member_phone_number_does_not_exist_in_database=="true"){
					$message = "Phone Number  ".$phone_no."  Does Not Exist. Please Check Your Phone Number And Try Again ";
					$_SESSION["message_create"] = $message;
					$db->rollBack();
				}else{
					$message = "Phone Number  ".$phone_no."  Is Already Registered In The System As Members Of A Society Or Company";
					$_SESSION["message_create"] = $message;
					$db->rollBack();
				}

			}
	}
?>