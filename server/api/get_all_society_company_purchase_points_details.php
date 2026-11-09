<?php
   
include 'functions.php';
$file_name='get_all_society_company_purchase_points_details';

$response = array();
//$_POST['username']='0711222222';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username'])) {
   
	$username = $_POST["username"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$item_count = 0;
							$sql_details= "SELECT tbl_purchase_points_details.date_of_purchase,transaction_type,purchase_amount,no_of_points_purchased,transaction_number FROM tbl_purchase_points_details WHERE tbl_purchase_points_details.phone_number = :field_val ORDER BY 1 DESC "; 
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
										$date_of_purchase = $row['date_of_purchase']; 
										$timestamp = strtotime($date_of_purchase); 
										$new_date = date('d-m-Y', $timestamp);
									$temp["date_of_purchase"] = $new_date;
									$temp["transaction_type"] = $row['transaction_type'];
									$temp["purchase_amount"] = $row['purchase_amount'];
									$temp["no_of_points_purchased"] = $row['no_of_points_purchased'];
									$temp["transaction_number"] = $row['transaction_number'];

									$message='Successful';
									$temp["message"] = $message;
									$item_count = $item_count + 1;
									array_push($response, $temp);
								}					
						 				
						echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "There Are No Purchase Details To Display...!!!";
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
