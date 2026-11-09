<?php
   
include 'functions.php';
$file_name='get_hospitals_points_report_continet';

$response = array();
//$_POST["continent"]= 'AMERICA';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['continent'])) {
   
	$continent = $_POST["continent"];

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$merchant_region="";
	$merchant_region_data_processed = "";
	$total_points_paid=0;
	$total_points_cashed_out=0;
	$balance=0;
	$sql = "SELECT name,region FROM `tbl_region_continent` WHERE continent = :field_val order by 2";
	//$sql = "SELECT name,region FROM `tbl_region_continent` Order by 2";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $continent, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
$num = 0;
	if($total > 0){
			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row){
				$country = $row['name'];
				$region = $row['region'];

				$total_points_paid=0;
				$total_points_cashed_out=0;
				$balance=0;
	
				$sql_hospitals= "SELECT tbl_merchant_details.merchant_phone_number,tbl_merchant_details.merchant_code,tbl_merchant_details.merchant_name,tbl_merchant_details.merchant_region FROM tbl_merchant_details WHERE merchant_country=:field_val AND type_of_registration = 'Health' ORDER BY 4";
					$stmt_hospitals = $db->sth->prepare($sql_hospitals);
					$stmt_hospitals->bindParam(':field_val', $country, PDO::PARAM_STR); 
					$stmt_hospitals->execute();
					$total_hospitals = $stmt_hospitals->rowCount();
					
					
					if($total_hospitals > 0 )
					{
						$points_cashed_out=0;
						$points_paid=0;
						$balance=0;
						$result = $stmt_hospitals->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
						{
									$merchant_phone_number = $row['merchant_phone_number'];
							   		$merchant_code = $row['merchant_code'];
									$merchant_name = $row['merchant_name'];
									$merchant_region = $row['merchant_region'];

									
									
									$sql= "SELECT SUM(tariff_total_amount)  FROM tbl_bill_tariff_merchant_records WHERE merchant_code =:field_val ";

									$stmt = $db->sth->prepare($sql);
									$stmt->bindParam(':field_val', $merchant_code, PDO::PARAM_STR); 
									$stmt->execute();
									$total = $stmt->rowCount();
									if($total > 0)
									{
									
										$points_cashed_out = $stmt->fetchColumn();
										if($points_cashed_out==NULL){
										
											$points_cashed_out = 0;
										} 
										$total_points_cashed_out = $total_points_cashed_out + $points_cashed_out;	
    									}

									$sql_payments = "SELECT SUM(amount_paid) FROM tbl_merchant_bill_payments WHERE merchant_code = :field_val";
									$stmt_payments = $db->sth->prepare($sql_payments);
									$stmt_payments->bindParam(':field_val', $merchant_code, PDO::PARAM_STR); 
									$stmt_payments->execute();
									$total_payments = $stmt_payments->rowCount();
									if($total_payments>0)
									{
										
										$points_paid  = $stmt_payments->fetchColumn();
										if($points_paid==NULL)
										{
										
											$points_paid = 0;
										}else{
											$num = $num + 1;
											$total_points_paid = $total_points_paid + $points_paid;
										}
									}

								$total_points_cashed_out=round($total_points_cashed_out,2);
								$total_points_paid=round($total_points_paid,2);
								$balance = $total_points_cashed_out - $total_points_paid;

									$balance=round($balance,2);
								if($merchant_region_data_processed=="")
								{
									$merchant_region_data_processed = $region;
								}else{
									if($merchant_region_data_processed != $region)
									{
										$merchant_region_data_processed = $region;

									}else{
										$region="";
									}
								}

									
						}//forach
						
					}
									$temp["success"] = true;  
									$temp["region"] = $region;
									$temp["country"] = $country;
									$temp["points_paid"] = $total_points_paid;
									$temp["points_cashed_out"] = $total_points_cashed_out;
									$temp["points_balance"] = $balance;
									$message='Successful';
									$temp["message"] = $message;
									array_push($response, $temp);
				}
				if($num > 0){
					echo json_encode($response);
				}else{
					$message = "No Hosiptal Data For Continent ".$continent;
					$success = "false";
					array_push($response,array("success"=>$success,"message"=>$message));
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
