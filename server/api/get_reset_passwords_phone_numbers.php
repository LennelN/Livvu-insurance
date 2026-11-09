<?php
    
	include 'functions.php';
	$file_name='get_reset_passwords_phone_numbers';

	$response = array();

if (isset($_POST['username'])) {
   error_log(strip_tags("comingin 2"), 0);
	$username = $_POST["username"];
	
	require_once __DIR__ . '/db_connect.php';
 
	// connecting to db
    	$db = new DB_CONNECT();
	$qry = "SELECT phone_number, request_date, request_processed, request_completed FROM tbl_reset_passwords_requests WHERE request_completed = 'N' ORDER BY 2 ASC";

	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0)
	{	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		    		

		foreach($result as $row)
		{
			$temp["success"] = true; 
			$phone_number = $row['phone_number'];
			$temp["phone_number"] = $row['phone_number'];
  
			$temp["request_date"] = $row['request_date'];
 
			$temp["request_processed"] = $row['request_processed']; 
			$temp["request_completed"] = $row['request_completed'];

			//array_push($response, $temp);

			
			$sql= "SELECT name,surname,dob,idno,gender,date_joined,member_active,country_name,group_category FROM tbl_clients WHERE phone_number = :field_val"; 
			$stmt = $db->sth->prepare($sql);
			$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
			$stmt->execute();
			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
			$total = $stmt->rowCount();

 			if($total <= 0)
			{
				
				$sql= "SELECT merchant_name,merchant_licence_number,merchant_code,merchant_description,merchant_date_joined,status_flag,merchant_country,type_of_registration FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
				$stmt = $db->sth->prepare($sql);
				$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();			
				
				if($total <= 0)
					{

						//Check in tbl_users for livvu company employees

						$sql= "SELECT name, email, idno, branch, date_registered, user_active, country,user_role FROM tbl_users WHERE user_login_id = :field_val"; 
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
						$stmt->execute();
						$total = $stmt->rowCount();

						if($total <=0)
							{//tbl_users else

								$sql= "SELECT name, email, idno, branch, date_registered, user_active,country,user_role FROM tbl_merchant_users WHERE user_login_id = :field_val"; 
								$stmt = $db->sth->prepare($sql);
								$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
								$stmt->execute();
								$total = $stmt->rowCount();

								if($total <=0){
 
    									$success = "false";
									$message = "Phone Number ".$phone_number." Does Not Exist In The System.!!";
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);		
								}else{
									$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
									foreach($result as $row)
									{
							
										$temp["name"] = $row['name'];
										$temp["dob_licence_no_email"] = $row['email'];
										$temp["idno_merchant_code"] = $row['idno'];
										$temp["gender_branch_description"] = $row['branch'];
										$date_of_joining = $row['date_registered'];
										$timestamp = strtotime($date_of_joining); 
										$new_date = date('d-m-Y', $timestamp);
										$temp["date_joined"] = $new_date;
										$temp["member_active"] = $row['user_active'];
										$temp["country_name"] = $row['country'];

										$temp["group_category_user_role"] = $row['user_role'];
										$temp["category"] = "MerchantUser";
										$message='Successful';
										$temp["message"] = $message;
										array_push($response, $temp);
									}
								
								}
							}else{
									$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
									foreach($result as $row)
									{
										$temp["name"] = $row['name'];
										$temp["dob_licence_no_email"] = $row['email'];
										$temp["idno_merchant_code"] = $row['idno'];
										$temp["gender_branch_description"] = $row['branch'];
										$date_of_joining = $row['date_registered'];
										$timestamp = strtotime($date_of_joining); 
										$new_date = date('d-m-Y', $timestamp);
										$temp["date_joined"] = $new_date;
										$temp["member_active"] = $row['user_active'];
										$temp["country_name"] = $row['country'];

										$temp["group_category_user_role"] = $row['user_role'];
										$temp["category"] = "LivvuUser";
										$message='Successful';
										$temp["message"] = $message;
										array_push($response, $temp);
									}
							}
					}else{//merchantdetails
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
						foreach($result as $row)
						{
							
							$temp["name"] = $row['merchant_name'];
							$temp["dob_licence_no_email"] = $row['merchant_licence_number'];
							$temp["idno_merchant_code"] = $row['merchant_code'];
							$temp["gender_branch_description"] = $row['merchant_description'];
							$date_of_joining = $row['merchant_date_joined'];
							$timestamp = strtotime($date_of_joining); 
							$new_date = date('d-m-Y', $timestamp);
							$temp["date_joined"] = $new_date;
							$temp["member_active"] = $row['status_flag'];
							$temp["country_name"] = $row['merchant_country'];

							$group_category= $row['type_of_registration'];
							$temp["group_category_user_role"] = $group_category;

							$temp["category"] = "Merchant";
							$message='Successful';
							$temp["message"] = $message;
							array_push($response, $temp);
						}


					}
			}else{//clients 

				foreach($result as $row)
				{
						$name = $row['name'];
   
						$surname = $row['surname'];
  			      
						$fullname=$name." ".$surname; 
						$temp["name"] = $fullname;
						$temp["dob_licence_no_email"] = $row['dob'];
						$temp["idno_merchant_code"] = $row['idno'];
						$temp["gender_branch_description"] = $row['gender'];
						$date_of_joining = $row['date_joined'];
						$timestamp = strtotime($date_of_joining); 
						$new_date = date('d-m-Y', $timestamp);
						$temp["date_joined"] = $new_date;
						$temp["member_active"] = $row['member_active'];
						$temp["country_name"] = $row['country_name'];

						$group_category= $row['group_category'];
						$temp["group_category_user_role"] = $group_category;

						$temp["category"] = "Client";
						$message='Successful';
						$temp["message"] = $message;
						array_push($response, $temp);
				}
				
			}

		}
		
		echo json_encode($response);
	}else{
		$success = "false";
		$message = "There Are No Phone Numbers To View For Password Reset.";
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
