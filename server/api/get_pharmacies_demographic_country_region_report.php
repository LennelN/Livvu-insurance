<?php
   
include 'functions.php';
$file_name='get_pharmacies_demographic_country_region_report';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['country'])) {
   
	$country = $_POST["country"];
	
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$merchant_region="";
	$merchant_region_data_processed = "";
	$total_count = 0;
	$data_found = 0;

	$sql_hospitals= "SELECT DISTINCT(tbl_merchant_details.merchant_region) FROM tbl_merchant_details WHERE merchant_country=:field_val AND type_of_registration = 'Health' AND merchant_description = 'Pharmacy' ORDER BY 1";
					$stmt_hospitals = $db->sth->prepare($sql_hospitals);
					$stmt_hospitals->bindParam(':field_val', $country, PDO::PARAM_STR); 
					$stmt_hospitals->execute();
					$total_hospitals = $stmt_hospitals->rowCount();
					$num = 0;
					
					if($total_hospitals > 0 )
					{
						$total_points_cashed_out=0;
						$total_points_paid=0;
						$balance=0;
						$result = $stmt_hospitals->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){
									$merchant_region = $row['merchant_region'];
	
									
									$sql= "SELECT merchant_description,COUNT(merchant_description) As DescriptionCount FROM tbl_merchant_details WHERE tbl_merchant_details.merchant_region = :field_val AND status_flag='Y' AND tbl_merchant_details.merchant_country = :field_val1 AND merchant_description = 'Pharmacy' AND type_of_registration = 'Health' GROUP BY 1 ORDER BY 1";

									$stmt = $db->sth->prepare($sql);
									$stmt->bindParam(':field_val', $merchant_region, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $country, PDO::PARAM_STR);
									$stmt->execute();
									$total = $stmt->rowCount();
									if($total > 0)
									{
										
										$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
										foreach($result as $row)
										{
											$merchant_description = $row['merchant_description'];

											$merchant_count = $row['DescriptionCount'];
	
											$total_count = $total_count + $merchant_count;

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
	
					 						$data_found = $data_found + 1;
											$temp["success"] = true;  
											$temp["region"] = $merchant_region;
											$temp["count"] = $merchant_count;
											$message='Successful';
											$temp["message"] = $message;
											array_push($response, $temp);
											//$dataRow = $dataRow."<tr><td>$merchant_region</td><td>$merchant_count</td></tr>";		
	
										}

									} 					
							}

											$temp["success"] = true;  
											$message_total='Total';
											$temp["region"] = $message_total;
											$temp["count"] = $total_count;
											$message='Successful';
											$temp["message"] = $message;
											array_push($response, $temp);
											//$dataRow = $dataRow."<tr><td>Total</td><td>$total_count</td></tr>";
					}else{
						
								$success = "false";
								$message = "No Pharmacies Registered For ".$country;
								array_push($response,array("success"=>$success,"message"=>$message));
								echo json_encode($response);
								
					}

					if($data_found > 0)
					{
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
