<?php
    
include 'functions.php';
$file_name='get_patient_billing_status';
   
$response = array();
//$_POST['username']='0778797979';
//$_POST['password']='1970';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['username']) && isset($_POST['country_name_client'])) {
   
    
	date_default_timezone_set('Africa/Harare');
	$username = $_POST["username"];
  	$country_name_client=$_POST["country_name_client"];
	$no_of_points_contributed_other_groups=0;  
	$no_of_points_contributed=0;
	$no_of_points_contributed_society=0;
	$no_of_points_contributed_other_groups_society=0;  
	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';
	//$db = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);


	$sql= "SELECT name,surname,dob,idno,gender,date_joined,member_active,country_name,group_category FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $country_name_client, PDO::PARAM_STR); 
	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
	$total = $stmt->rowCount();

 	if($total > 0){
					foreach($result as $row){
									$name = $row['name'];
   
						$surname = $row['surname'];
  			      
						$fullname=$name." ".$surname; 
						$temp["success"] = true;
						$temp["name"] = $fullname;
						$temp["dob"] = $row['dob'];
						$temp["idno"] = $row['idno'];
						$temp["gender"] = $row['gender'];
						$date_of_joining = $row['date_joined'];
						$timestamp = strtotime($date_of_joining); 
						$new_date = date('d-m-Y', $timestamp);
						$temp["date_joined"] = $new_date;
						$temp["member_active"] = $row['member_active'];
						$temp["country_name"] = $row['country_name'];

						$group_category= $row['group_category'];
						$temp["group_category"] = $group_category;

						$message='Successful';
						$temp["message"] = $message;
								$type='Client';
									$temp["type"] = $type;
									//array_push($response, $temp);
								      
						}

					$sql_client_points= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
					$stmt_client_points = $db->prepare($sql_client_points);
					$stmt_client_points->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt_client_points->bindParam(':field_val1', $country_name_client, PDO::PARAM_STR); 
					$stmt_client_points->execute();
					$total_client_points = $stmt_client_points->rowCount();

					if($total_client_points > 0)
					{//Dont Have Points	

						$amount_client = $stmt_client_points->fetchColumn();
						$temp["amount"] = $amount_client;
						//$temp["bonus_points"] = 0;

						$sql_client_up_to_date= "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val AND purchase_country = :field_val1"; 
						$stmt_client_up_to_date = $db->prepare($sql_client_up_to_date);
						$stmt_client_up_to_date->bindParam(':field_val', $username, PDO::PARAM_STR); 
						$stmt_client_up_to_date->bindParam(':field_val1', $country_name_client, PDO::PARAM_STR); 
						$stmt_client_up_to_date->execute();
						$total_client_up_to_date = $stmt_client_up_to_date->rowCount();

						if($total_client_up_to_date > 0)
						{
							$client_due_date=$stmt_client_up_to_date->fetchColumn();
							//$date_of_joining = $row['date_joined'];
							$timestamp = strtotime($client_due_date); 
							$new_date_due = date('d-m-Y', $timestamp);
							$todayDate = date('Y-m-d');
							$temp["due_date"] = $new_date_due;
						}
						//array_push($response, $temp);
					}else{
						$no_of_points_client = 0;
						$temp["amount"] = $no_of_points_client;
						$temp["due_date"] = "None";
						
					}
			
					array_push($response, $temp);
					echo json_encode($response);
	
		}else{

		  			$success = "false";
					$message = "Phone Number Does Not Exist! Please Register And Try Again";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);

		}
	
	} else {
    // required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    echo json_encode($response);
}

	
?>
