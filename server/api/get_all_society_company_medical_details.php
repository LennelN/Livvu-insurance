<?php
   
include 'functions.php';
$file_name='get_all_society_company_medical_details';

$response = array();
//$_POST['username']='0999999999';
//error_log(strip_tags("comingin"), 0);
if (isset($_POST['username']) && isset($_POST['merchantCode'])) {
   
	$username = $_POST["username"];
	$merchantCode = $_POST["merchantCode"];

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
	$no_of_bill_records = 0;
    	$db = new DB_CONNECT();
 
				$sql= "SELECT member_phone_number,member_phone_country from tbl_society_company_members WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchantCode, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){  
							$member_phone_number = trim($row['member_phone_number']);
							$member_phone_country = trim($row['member_phone_country']);

				$sql_details= "SELECT  date_bill_created,bill_number,merchant_name,total_bill_amount,bill_amount,bill_paid_indicator  FROM tbl_bill_client_records WHERE phone_number = :field_val AND country = :field_val1 ORDER BY 6 DESC "; 
							$stmt_details = $db->sth->prepare($sql_details);
							$stmt_details->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR);
							$stmt_details->bindParam(':field_val1', $member_phone_country, PDO::PARAM_STR);
							$stmt_details->execute();
							$total_details = $stmt_details->rowCount();

							if($total_details > 0)
							{
  							
								$result_details = $stmt_details->fetchAll(PDO::FETCH_ASSOC); 
    								foreach($result_details as $row){  
    										//$success = true;
									$temp["success"] = true; 
									$temp["member_phone_number"]=$member_phone_number;
										$date_bill_created = $row['date_bill_created']; 
										$timestamp = strtotime($date_bill_created); 
										$new_date = date('d-m-Y', $timestamp);
									$temp["date_bill_created"] = $new_date;
									$temp["bill_number"]=$row['bill_number'];
									$temp["merchant_name"]=$row['merchant_name'];
									$temp["total_bill_amount"]=$row['total_bill_amount'];
									$temp["total_no_of_points"]=$row['bill_amount'];
									$temp["bill_paid_indicator"]=$row['bill_paid_indicator'];
										$message='Successful';
									$temp["message"] = $message;
									$no_of_bill_records = $no_of_bill_records + 1;
									array_push($response, $temp);
								}
							}
						}


						if($no_of_bill_records > 0 )
							{
									
								echo json_encode($response);

							}else{
								$success = "false";
								$message = "There Are No Bill Records To Display !!";
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
							}
						}else{
							$success = "false";
							$message = "There Are No Members In The System...Please Register Members !!";
							array_push($response,array("success"=>$success,"message"=>$message));
							echo json_encode($response);
						}
    
 }else {

    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}

	
?>
