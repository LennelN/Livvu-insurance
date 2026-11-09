<?php
   
include 'functions.php';
$file_name='bill_get_group_count_report';

$response = array();
//$_POST["country"]='Eritrea';

if (isset($_POST['country'])) {

   	$country = $_POST["country"];   	
	
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$merchant_region="";
	$merchant_region_data_processed = "";
	$data_found = 0;
	$sql_hospitals= "SELECT DISTINCT(tbl_merchant_details.merchant_region) FROM tbl_merchant_details WHERE merchant_country=:field_val AND type_of_registration = 'Health' ORDER BY 1";
					$stmt_hospitals = $db->sth->prepare($sql_hospitals);
					$stmt_hospitals->bindParam(':field_val', $country, PDO::PARAM_STR); 
					$stmt_hospitals->execute();
					$total_hospitals = $stmt_hospitals->rowCount();
					$num = 0;
					
					if($total_hospitals > 0 )
					{
						
						$result = $stmt_hospitals->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
						{
								$merchant_region = $row['merchant_region'];

										
									
								$sql= "SELECT terms_status_flag,COUNT(terms_status_flag) As SectorCount FROM tbl_merchant_details WHERE tbl_merchant_details.merchant_region = :field_val AND status_flag='Y' AND tbl_merchant_details.merchant_country = :field_val1 AND type_of_registration = 'Health' GROUP BY 1 ORDER BY 1";

								$stmt = $db->sth->prepare($sql);
								$stmt->bindParam(':field_val', $merchant_region, PDO::PARAM_STR); 
								$stmt->bindParam(':field_val1', $country, PDO::PARAM_STR);
								$stmt->execute();
								$total = $stmt->rowCount();
								if($total > 0)
								{
									
										$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
										foreach($result as $row){
										
											
											if($merchant_region_data_processed=="")
											{
												$merchant_region_data_processed = $merchant_region;
												$num = $num + 1;
											}else{
												if($merchant_region_data_processed != $merchant_region)
												{
													$num = $num + 1;
													$merchant_region_data_processed = $merchant_region;
												}else{
													$num = 0;
													$merchant_region="";
												}
											}
					 
											$temp["success"] = true;  
											$temp["region"] = $merchant_region;						
											$terms_status_flag = $row['terms_status_flag'];
											$terms_status_flagCount = $row['SectorCount'];
											$temp["terms_status_flag"] = $terms_status_flag;
											$temp["count"] = $terms_status_flagCount;
											$message='Successful';
											$temp["message"] = $message;
											if($terms_status_flagCount > 0){
												$data_found = $data_found + 1;
												array_push($response, $temp);
											}	
	
										}
									} 
					
						}
						echo json_encode($response);
					}else{
						
							$message = "No Hospitals Registered For ".$country;
							$success = "false";
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

