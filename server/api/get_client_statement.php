<?php
   
include 'functions.php';
$file_name='get_client_statement';

$response = array();
//$_POST['phone_number']='0772455025';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['phone_number'])) {
	$phone_number = $_POST["phone_number"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

$sql="SELECT transaction_type,date_transfered,phone_number,phone_number_recipient,no_of_points_transfered, 0 As Credit from tbl_points_transfers WHERE phone_number=:field_val UNION SELECT transaction_type,date_transfered,phone_number_recipient,phone_number,0, no_of_points_transfered from tbl_points_transfers WHERE phone_number_recipient=:field_val UNION SELECT transaction_type,date_of_purchase,phone_number,transaction_number,0,purchase_amount FROM tbl_purchase_points_details WHERE phone_number=:field_val UNION SELECT merchant_name,date_bill_created,bill_number,merchant_phone_number,bill_amount,0 As Credit FROM tbl_bill_client_records WHERE phone_number=:field_val UNION SELECT transaction_type,date_paid,bill_number,merchant_phone_number,amount_paid,0 As Credit FROM tbl_merchant_bill_payments WHERE phone_number=:field_val ORDER BY 2 DESC";

					$statement_count = 0;
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
					
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{	
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										$statement_count = $statement_count + 1;
												$temp["success"] = true;  
												$temp["transaction_type"] = $row['transaction_type'];
												$date_paid = $row['date_transfered']; 
												$timestamp = strtotime($date_paid); 
												$new_date = date('d-m-Y', $timestamp);
												$temp["date_paid"] = $new_date;
												$temp["phone_number"] = $row['phone_number'];
												$temp["merchant_phone_number"] = $row['phone_number_recipient'];
												$temp["no_of_points_paid"] = $row['no_of_points_transfered'];
												$temp["Credit"] = $row['Credit'];
												$message='Successful';
												$temp["message"] = $message;
												if($statement_count > 5){break;}
												array_push($response, $temp);
										
												
												
								}
								echo json_encode($response);
						
					}else{
								$success = "false";
								$message = "No Transactions To View At The Moment.";
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
