<?php
   
include 'functions.php';
$file_name='get_all_society_company_member_details';

$response = array();
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['merchant_code'])) {
   
	$username = $_POST["username"];
	$merchant_code = $_POST["merchant_code"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();


					$sql= "SELECT member_phone_number,member_phone_country from tbl_society_company_members WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){  
							$member_phone_number = trim($row['member_phone_number']);
							$member_phone_country = trim($row['member_phone_country']);
							$sql_details= "SELECT tbl_clients.name,tbl_clients.surname,tbl_clients.date_joined,MAX(tbl_purchase_points_details.due_date) As due_date FROM tbl_clients INNER JOIN tbl_purchase_points_details ON(tbl_clients.phone_number = tbl_purchase_points_details.phone_number AND tbl_clients.phone_number = :field_val AND tbl_clients.country_name = :field_val1 AND tbl_clients.member_active = 'Y') ORDER BY 1 DESC "; 
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
										$client_name = $row['name']; 
										$client_surname = $row['surname']; 
										$name = $client_name." ".$client_surname;
									$temp["name_of_member"]=$name;
										$date_joined = $row['date_joined']; 
										$timestamp = strtotime($date_joined); 
										$new_date = date('d-m-Y', $timestamp);
									$temp["date_joined"] = $new_date;
										$due_date = $row['due_date'];
										if($due_date == NULL)
									{
										$due_date=0;
										$temp["due_date"] = $due_date;
									}else{
										$temp["due_date"] = $due_date;
									}									 
									$sql_points = "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1 "; 
										$stmt_points = $db->sth->prepare($sql_points);
										$stmt_points->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR); 
										$stmt_points->bindParam(':field_val1', $member_phone_country, PDO::PARAM_STR); 
										$stmt_points->execute();
										$total_points = $stmt_points->rowCount();
										if($total_points > 0)
										{
											$no_of_points = $stmt_points->fetchColumn();
											$temp["no_of_points"] = $no_of_points;
										}else{
											$temp["no_of_points"] = 0;
										}
										$message='Successful';
									$temp["message"] = $message;
									array_push($response, $temp);
								}
						

							}
												
						  }					
						echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "There Are No Members To Display. Please Add Members To Your Account...!!!";
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
