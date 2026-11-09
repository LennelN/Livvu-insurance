<?php
   
include 'functions.php';
$file_name='bill_get_hospital_demographic_report_continent';

$response = array();
//$_POST["continent"]= 'South America';
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
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $continent, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0){

			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row){
				$country = $row['name'];
				$region = $row['region'];

				//error_log(strip_tags("Error logging in. missing fields"), 0);

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
				$sql = "SELECT terms_status_flag,count(terms_status_flag) AS CountOfSector FROM `tbl_merchant_details` where type_of_registration='Health' AND status_flag='Y' AND merchant_country = :field_val GROUP BY 1 ";

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
								$terms_status_flag = $row['terms_status_flag'];
								$temp["sector"] = $terms_status_flag;
								$sectorCount = $row['CountOfSector'];
								$temp["count"] = $sectorCount;
								$message='Successful';
								$temp["message"] = $message;
								if($sectorCount > 0){
									$data_found = $data_found + 1;
									array_push($response, $temp);
								}
								
								

							}
					}

				}

				if($data_found == 0){
					$message = "No Hospital Demographic Data For Region ".$region;
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
