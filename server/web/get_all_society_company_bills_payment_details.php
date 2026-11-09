<?php
   
include 'functions.php';
$file_name='get_all_society_company_bills_payment_details';

$response = array();
//$_POST['username']='197564';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username'])) {
   
	$username = $_POST["username"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$item_count = 0;
					$sql_details= "SELECT phone_number,no_of_points_paid,date_paid FROM tbl_merchant_bill_payments WHERE bill_number = :field_val ORDER BY 3 DESC "; 
							$stmt_details = $db->sth->prepare($sql_details);
							$stmt_details->bindParam(':field_val', $username, PDO::PARAM_STR);
							$stmt_details->execute();
							$total_details = $stmt_details->rowCount();
							if($total_details > 0)
							{
								$result_details = $stmt_details->fetchAll(PDO::FETCH_ASSOC); 
    								foreach($result_details as $row){  
    										//$success = true;
									if($item_count==5){break;}
									$temp["success"] = true; 
									$temp["phone_number"] = $row['phone_number'];
									$temp["no_of_points_paid"] = $row['no_of_points_paid'];
										$date_paid = $row['date_paid']; 
										$timestamp = strtotime($date_paid); 
										$new_date = date('d-m-Y', $timestamp);
									$temp["date_paid"] = $new_date;
									$message='Successful';
									$temp["message"] = $message;
									$item_count = $item_count + 1;
									array_push($response, $temp);
								}					
						 				
						echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "There Are No Bill Payment Details To Display...!!!";
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
