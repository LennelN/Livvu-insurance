<?php
   
include 'functions.php';
$file_name='get_no_of_admissions_continent_per_month';

$response = array();
//$_POST["continent"]='AFRICA';
//$_POST["start_date"]='01/01/2017';
//$_POST["end_date"]='16/05/2021';
//error_log(strip_tags("Error logging in. missing fields"), 0);
if (isset($_POST['continent']) && isset($_POST['start_date']) && isset($_POST['end_date'])) {
   
	$continent = $_POST["continent"];
	$startdate = $_POST["start_date"];
	$enddate = $_POST["end_date"];

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$merchant_region="";
	$merchant_region_data_processed = "";
	$total_count = 0;
	$total_country_count = 0;
	$num = 0;
	$len = 0;
	$in_clause="";

	$data_found = 0;
	$region_processing = "";

	$dt   = new DateTime();
	$date_paid_out = $dt->createFromFormat('d/m/Y', $startdate);
	$newStartDate = $date_paid_out->format('Y-m-d');
		

	$dt2= new DateTime();
	$date_paid_out2 = $dt2->createFromFormat('d/m/Y', $enddate);
	$newEndDate = $date_paid_out2->format('Y-m-d');

	$sql = "SELECT name,region FROM `tbl_region_continent` WHERE continent = :field_val order by 2";
	//$sql = "SELECT name,region FROM `tbl_region_continent` Order by 2";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $continent, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0){

			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row)
			{
				$country = $row['name'];
				$region = $row['region'];

				$in_clause="";

				$sql = "SELECT merchant_phone_number FROM tbl_merchant_details WHERE merchant_country = :field_val ORDER BY 1";
				$stmt = $db->sth->prepare($sql);
				$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();
									
				if($total > 0 )
				{
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result as $row)
					{	
				
						$merchant_phone_no = $row['merchant_phone_number'];

						$in_clause = $in_clause."'".$merchant_phone_no."',";
					}
				}

				$len = strlen($in_clause);
				$len = $len - 1;
				$merchant_phone_numbers = substr($in_clause, 0, $len);

				$len_tariff_codes = 0;
				$in_clause_tariff_codes ="";

				$sql = "SELECT admission_tariff_code FROM tbl_country_admissions_tariff_codes WHERE country_name = :field_val ORDER BY 1";
				$stmt = $db->sth->prepare($sql);
				$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();
									
				if($total > 0 )
				{
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result as $row)
					{	
				
						$admission_tariff_code = $row['admission_tariff_code'];

						$in_clause_tariff_codes = $in_clause_tariff_codes."'".$admission_tariff_code."',";
					}
				}
				$len_tariff_codes = strlen($in_clause_tariff_codes);
				$len_tariff_codes = $len_tariff_codes - 1;
				$tariff_code_in = substr($in_clause_tariff_codes, 0, $len_tariff_codes);
error_log(strip_tags("Error logging in. missing fields tariff_code_in ".$tariff_code_in." and phone numbers ".$merchant_phone_numbers), 0);
			if($tariff_code_in != null && $merchant_phone_numbers != null)
			{
				$sql = "SELECT tariff_code,count(tariff_code) AS tariffCount,tariff_description  FROM `tbl_bill_tariff_merchant_records` where tariff_code IN($tariff_code_in) AND (transaction_date BETWEEN '$newStartDate' AND '$newEndDate') AND merchant_phone_number IN($merchant_phone_numbers) GROUP BY tariff_code";
					$stmt = $db->sth->prepare($sql); 
				//$stmt->bindParam(':field_val', $admission_tariff_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0 )
					{
						
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
						{
							$tariff_code = $row['tariff_code'];
	
							$total_count = $row['tariffCount'];
							$tariff_description = $row['tariff_description'];
							$total_country_count = $total_country_count + $total_count;
							$num = $num + 1;
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


							$temp["success"] = true;  
							$temp["region"] = $region;
							$temp["country"] = $country;
							$temp["tariff_code"] = $tariff_code;
							$temp["tariff_description"] = $tariff_description;
							$temp["count"] = $total_count;
							$message='Successful';
							$temp["message"] = $message;
							array_push($response, $temp);
							
						}
					}
				}
			}
	}

			if($num == 0)
			{				
				$success = "false";
				$message = "No Admissions During This Period From ".$startdate." To ".$enddate." For ".$continent;
				array_push($response,array("success"=>$success,"message"=>$message));
				echo json_encode($response);				
			}else{

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

