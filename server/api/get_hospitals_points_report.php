<?php
   
include 'functions.php';
$file_name='get_hospitals_points_report';

$response = array();
//$_POST["country"]= 'ZIMBABWE';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['country'])) {
   
	$country = $_POST["country"];
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$merchant_region="";
	$merchant_region_data_processed = "";
$total_points_cashed_out=floatval(0);
$total_points_paid=floatval(0);
$balance = 0;

$amount_points_cashed_out=floatval(0);
$amount_points_paid=floatval(0);
$amount = 0;


	$sql_hospitals= "SELECT tbl_merchant_details.merchant_phone_number,tbl_merchant_details.merchant_code,tbl_merchant_details.merchant_name,tbl_merchant_details.merchant_region,tbl_merchant_details.terms_status_flag,tbl_merchant_details.merchant_address,tbl_merchant_details.merchant_date_joined FROM tbl_merchant_details WHERE merchant_country=:field_val AND type_of_registration = 'Health' ORDER BY 4";
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
    							foreach($result as $row)
							{
										$merchant_phone_number = $row['merchant_phone_number'];
							   			$merchant_code = $row['merchant_code'];
										$merchant_name = $row['merchant_name'];
										$merchant_region = $row['merchant_region'];

										$merchant_sector = $row['terms_status_flag'];

										$merchant_address = $row['merchant_address'];

										$merchant_date_joined = $row['merchant_date_joined'];

										$num = $num + 1;
									
										$sql= "SELECT SUM(tariff_total_amount)  FROM tbl_bill_tariff_merchant_records WHERE merchant_code =:field_val ";

									$stmt = $db->sth->prepare($sql);
									$stmt->bindParam(':field_val', $merchant_code, PDO::PARAM_STR); 
									$stmt->execute();
									$total = $stmt->rowCount();
									if($total > 0)
									{
									
										$total_points_cashed_out = $stmt->fetchColumn();
										if($total_points_cashed_out==NULL){
										
										$total_points_cashed_out = 0;
										} 
    									}


										$sql_payments = "SELECT SUM(amount_paid) FROM tbl_merchant_bill_payments WHERE merchant_code = :field_val";
										$stmt_payments = $db->sth->prepare($sql_payments);
										$stmt_payments->bindParam(':field_val', $merchant_code, PDO::PARAM_STR); 
										$stmt_payments->execute();
										$total_payments = $stmt_payments->rowCount();
										if($total_payments>0)
										{
										
											$total_points_paid  = $stmt_payments->fetchColumn();
											if($total_points_paid==NULL)
											{
										
												$total_points_paid = 0;
											}
										}



									$total_points_cashed_out =floatval($total_points_cashed_out);
									$amount_points_cashed_out =round($total_points_cashed_out,2);
									$total_points_paid = floatval($total_points_paid);
									$amount_points_paid = round($total_points_paid,2);
					$balance =  $total_points_cashed_out - $total_points_paid;								
					$balance = floatval($balance);
$amount = round($balance,2);
					

									if($merchant_region_data_processed=="")
									{
										$merchant_region_data_processed = $merchant_region;
									}else{
										if($merchant_region_data_processed != $merchant_region)
										{
											$merchant_region_data_processed = $merchant_region;
										}else{
											$merchant_region="";
										}
									}
					 
									$temp["success"] = true;  
									$temp["phone"] = $merchant_phone_number;
									$temp["region"] = $merchant_region;
									$temp["sector"] = $merchant_sector;
									$temp["name"] = $merchant_name;
									$temp["code"] = $merchant_code;
									$temp["points_paid"] = $amount_points_paid;
									$temp["points_cashed_out"] = $amount_points_cashed_out;
									$temp["points_balance"] = $amount;
									$temp["merchant_address"] = $merchant_address;

									$temp["merchant_date_joined"] = $merchant_date_joined;

									$message='Successful';
									$temp["message"] = $message;
									array_push($response, $temp);
									
	
							}//foraeach
							echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "No Hospital Data For ".$country;
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
