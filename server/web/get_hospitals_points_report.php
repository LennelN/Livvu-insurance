<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_hospitals_report';

$response = array();

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
   
if ($_SERVER["REQUEST_METHOD"] == "POST") {
	

	$submitbutton= isset($_POST['Submit']);
	
	//$logout
	if($submitbutton){
	
		$fgmembersite->RedirectToURL("main_admin_reports_menu.php");

	}else{
		
			$fgmembersite->RedirectToURL("logout.php");
	}

}else{

//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
   
	$country = $_SESSION["selected_report_country"];
	//$username = '0772455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

$dataRow = "";
$merchant_region="";
$merchant_region_data_processed = "";
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
    						foreach($result as $row){
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
										if($total_points_paid==NULL){
										
											$total_points_paid = 0;
										}
									}
$total_points_cashed_out =floatval($total_points_cashed_out);
					$balance =  $total_points_cashed_out - $total_points_paid;
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
					 
					$dataRow = $dataRow."<tr><td>$num</td><td>$merchant_region</td><td>$merchant_name</td><td>$merchant_sector</td><td>$merchant_code</td><td>$total_points_cashed_out</td><td>$total_points_paid</td><td>$balance</td></tr>";		
	
						}
					}else{
						
								$success = "false";
								$message = "No Hospitals Registered For ".$country;
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_main_admin_reports_messages.php");
								
					}
    
 }	
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Livvu Investments (Pvt) Ltd">
  <meta name="keywords" content="Livvu Investments (Pvt) Ltd,Livvu,livvu">
  <meta name="author" content="Moyet Tech Systems">
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="css/bootstrap-responsive.css">
	<link rel="STYLESHEET" type="text/css" href="css/fg_membersite.css" />
  	<link rel="stylesheet" type="text/css" href="css/main.css">

</head>
<body>

<div id="main-wrap">
	<div id="page-wrap">
	<div>
		<?php include("./include/declaration.php");?>
	</div>	
<div class="row">
		<div class="col-xs-12 text-center">   
			
			

			<form id='result' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset disabled>
				<legend>Hospital Report For:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["selected_report_country"]; ?> </legend>

			</fieldset>

			<fieldset >
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
							<th class="text-center">No</th>
							<th class="text-center">Region</th>
            						<th class="text-center">Name</th>
							<th class="text-center">Sector</th>
            						<th class="text-center">Code</th>
            						<th class="text-center">Amount</th>
							<th class="text-center">Amount Paid</th>
							<th class="text-center">Balance</th>
        					</tr>
    					</thead> 
					<tbody>
        					<?php echo $dataRow;?>
					</tbody>
				</table>

			</div>
				

				<div div class="col-xs-12 text-center">
					<br/>
					<input type='submit' name='Submit' value='Reports Menu' />
					<input type='submit' name='Logout' value='Logout' /><br/><br/>
				</div>


			</fieldset>
		</form>
	</div>
</div>


</div>
</div>
<script>
function displayCurrentTime() 
{
  

	currentTime = new Date();
	hours = currentTime.getHours();
  
	minutes = currentTime.getMinutes();
  
	seconds = currentTime.getSeconds();
  
	amOrPm = hours < 12 ? "AM" : "PM";

  
	hours = hours === 0 ? 12 : hours > 12 ? hours - 12 : hours;
  
	hours = addZero(hours);
  
	minutes = addZero(minutes);
  
	seconds = addZero(seconds);

 
	timeString = `${hours} : ${minutes} : ${seconds} ${amOrPm}`;

  
	document.getElementById("clock").innerText = timeString;
  
	timer = setTimeout(displayCurrentTime, 1000);


}



function addZero(component) 
{
  
	return component < 10 ? "0" + component : component;

}

displayCurrentTime();



</script>


<div>   <?php include("./include/footer.php");?>
</div>
</body>
</html>



