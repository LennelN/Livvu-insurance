<?php
   
include 'functions.php';
$file_name='get_selected_society_company_member_details';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['array']) ) {
   
	$username = $_POST["username"];
	$receiver_data=$_POST['array'];
	$new_array=json_decode($receiver_data,true);
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

					foreach($new_array as $row)
							{  
								$member_phone_number = $row['phone'];

								$sql_details= "SELECT tbl_clients.name,tbl_clients.surname,tbl_clients.date_joined FROM tbl_clients WHERE tbl_clients.phone_number = :field_val AND tbl_clients.member_active = 'Y' "; 
								$stmt_details = $db->sth->prepare($sql_details);
								$stmt_details->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR); 
								$stmt_details->execute();
								$total_details = $stmt_details->rowCount();
								if($total_details > 0)
								{
  							
									$result_details = $stmt_details->fetchAll(PDO::FETCH_ASSOC); 
    									foreach($result_details as $row)
									{  
    										//$success = true;
										$temp["success"] = true; 
										$client_name = $row['name']; 
										$client_surname = $row['surname']; 
										$name = $client_name." ".$client_surname;
										$temp["name_of_member"]=$name;
										$date_joined = $row['date_joined']; 
										$timestamp = strtotime($date_joined); 
										$new_date = date('d-m-Y', $timestamp);
										$temp["date_joined"] = $new_date;

										$sql_points = "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val "; 
										$stmt_points = $db->sth->prepare($sql_points);
										$stmt_points->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR); 
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
 }else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}

	
?>
