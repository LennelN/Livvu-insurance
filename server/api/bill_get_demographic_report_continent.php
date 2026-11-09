<?php
   
include 'functions.php';
$file_name='bill_get_demographic_report_continent';

$response = array();
//$_POST["continent"]= 'Africa';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['continent'])) {
   
	$continent = $_POST["continent"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$dataRow = "";
	$type_of_registration = 0;
	$registrationClientCount = 0;
	$gender = 0;
	$genderCount = 0;
	
	$data_found = 0;
	$region_processing = "";
	$sql = "SELECT name,region FROM `tbl_region_continent` WHERE continent = :field_val order by 2";
	//$sql = "SELECT name,region FROM `tbl_region_continent` Order by 2";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $continent, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0){

			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row){
				$country = $row['name'];
				$region = $row['region'];

				if($region_processing == "")
				{
					$region_processing = $region;
				}else{
					if($region_processing != $region)
					{
						$region_processing = $region;
					}else{
						$region="";
					}
				}
				
				//$data_found = 0;
				$sql = "SELECT gender,Count(tbl_clients.gender) AS CountOfclient_gender FROM tbl_clients WHERE member_active = 'Y' AND gender = 'Male' AND country_name = :field_val";

					$stmt_male = $db->sth->prepare($sql);
					$stmt_male->bindParam(':field_val', $country, PDO::PARAM_STR);	
					$stmt_male->execute();
					$total_male = $stmt_male->rowCount();
					
					if($total_male > 0)
					{	
									
						$result = $stmt_male->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{   
								$temp["success"] = true;  
								$temp["region"] = $region;
								$temp["country"] = $country;
								$gender = $row['gender'];
								$temp["category"] = $gender;
								$genderCount = $row['CountOfclient_gender'];
								$temp["count"] = $genderCount;
								$message='Successful';
								$temp["message"] = $message;
								if($genderCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
								
								

							}
					}


$sql_female = "SELECT gender,Count(tbl_clients.gender) AS CountOfclient_gender FROM tbl_clients WHERE member_active = 'Y' AND gender = 'Female' AND country_name = :field_val";

					$stmt_female = $db->sth->prepare($sql_female);
					$stmt_female->bindParam(':field_val', $country, PDO::PARAM_STR);	
					$stmt_female->execute();
					$total_female = $stmt_female->rowCount();
					
					if($total_female > 0)
					{					
						$result = $stmt_female->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    	
								$temp["success"] = true;  
								$temp["region"] = $region;
								$temp["country"] = $country;
								$gender = $row['gender'];
								$temp["category"] = $gender;
								$genderCount = $row['CountOfclient_gender'];
								$temp["count"] = $genderCount;
								$message='Successful';
								$temp["message"] = $message;
								if($genderCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
								
								
							}
					}

$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND type_of_registration='Health' AND merchant_country = :field_val";
			
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{    	

								$temp["success"] = true;  
								$temp["region"] = $region;
								$temp["country"] = $country;
								$type_of_registration = $row['type_of_registration'];
								$temp["category"] = $type_of_registration;
								$registrationClientCount = $row['CountOfclient_registration'];
								$temp["count"] = $registrationClientCount;
								$message='Successful';
								$temp["message"] = $message;
								
								if($registrationClientCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
								
							}
						}


$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND type_of_registration='Society' AND merchant_country = :field_val";
			
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{    						
								$temp["success"] = true;  
								$temp["region"] = $region;
								$temp["country"] = $country;
								$type_of_registration = $row['type_of_registration'];
								$temp["category"] = $type_of_registration;
								$registrationClientCount = $row['CountOfclient_registration'];
								$temp["count"] = $registrationClientCount;
								$message='Successful';
								$temp["message"] = $message;
								if($registrationClientCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
								
								

							}
						}

$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND type_of_registration='Company' AND merchant_country = :field_val";
			
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{  
								$temp["success"] = true;  
								$temp["region"] = $region;
								$temp["country"] = $country;
								$type_of_registration = $row['type_of_registration'];
								$temp["category"] = $type_of_registration;
								$registrationClientCount = $row['CountOfclient_registration'];
								$temp["count"] = $registrationClientCount;
								$message='Successful';
								$temp["message"] = $message;
								if($registrationClientCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
								
								

							}
						}


$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND type_of_registration='Private' AND merchant_country = :field_val";
			
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{  							
								$temp["success"] = true;  
								$temp["region"] = $region;
								$temp["country"] = $country;
								$type_of_registration = $row['type_of_registration'];
								$temp["category"] = $type_of_registration;
								$registrationClientCount = $row['CountOfclient_registration'];
								$temp["count"] = $registrationClientCount;
								$message='Successful';
								$temp["message"] = $message;
								
								if($registrationClientCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
								
							}
						}

			}//foreach

				
				if($data_found == 0){
					$message = "No Demographic Data For Region ".$region;
					$success = "false";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
				}else{
					echo json_encode($response);

				}
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
