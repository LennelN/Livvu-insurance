<?php 
session_start();
$response = array();
$response_back = array();
$file_name='remove_society_company_members';
require_once("./include/membersite_config.php");
include("./include/functions.php");

$merchant_code = ($_SESSION['merchant_code']);
$merchant_phone_number = ($_SESSION["user_name"]);



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
	

		if( sizeof($new_array) != 0 )
		{
				//error_log(strip_tags("array not empty"), 0);
				foreach($new_array as $row)
				{
					$member_phone_number_array = $row['phone_no'];
					$member_phone_country_array = $row['phone_country'];

					$sql = "SELECT member_phone_number,member_phone_country FROM tbl_society_company_members where merchant_phone_number = :field_val AND member_phone_number = :field_val1 AND member_phone_country = :field_val2";
					$stmt = $db->prepare($sql);
					$stmt->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR);  
					$stmt->bindParam(':field_val1', $member_phone_number_array, PDO::PARAM_STR);  
					$stmt->bindParam(':field_val2', $member_phone_country_array, PDO::PARAM_STR);  
					$stmt->execute();
					$total = $stmt->rowCount();
					if($total > 0)
					{
						
						try
						{
								
							$sql_delete = "DELETE from tbl_society_company_members WHERE member_phone_number = :field_val AND member_phone_country =  :field_val1 AND merchant_phone_number = :field_val2";
                							$stmt_delete = $db->prepare($sql_delete);
							$stmt_delete->bindParam(':field_val', $member_phone_number_array, PDO::PARAM_STR); 
							$stmt_delete->bindParam(':field_val1', $member_phone_country_array, PDO::PARAM_STR);
							$stmt_delete->bindParam(':field_val2', $merchant_phone_number, PDO::PARAM_STR);
				            			$stmt_delete->execute();                                               
                 							$total_delete =  $stmt_delete->rowCount();
							if($total_delete > 0)
							{
							
								$success = "true";
							}else{
								
								$success = "false";
							}

						}catch(PDOException $e){
			
							$success = "false";
	 						$message = "An Error Occurred Processing Transaction Please Try Again Later";
	 						$error_message=$e->getMessage();
	 						$error_type='exception';
	 						handler($error_message,$file_name,$success,$error_type);
							//error_log(strip_tags("array not empty deleteed kumber lana "), 0);
	 						$db->rollBack();
							$_SESSION["message_create"] = $message;
							echo json_encode($message);
							//$db->rollBack();
						}
					}else{
						
						$success = "false";
						
						break;
						
					}
				}
		}

		if($success == "true")
		{//insert into tbl_bill_merchant_records
			$success = "true";
			$message = "Congratulations You Have Successfully Removed Members From The Company/Society ";
			$_SESSION["message_create"] = $message;
			$db->commit();
			echo json_encode($message);
		}else{

$message = "You Tried To Remove A Phone Number That Is Not Registered ".$member_phone_number_array;
						$_SESSION["message_create"] = $message;
						$db->rollBack();
						echo json_encode($message);
		}
?>