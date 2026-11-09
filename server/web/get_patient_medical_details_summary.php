<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_patient_medical_details_summary';

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
	$send= isset($_POST['Send']);
	//$logout
	if($submitbutton){
	
		$main_menu = $_SESSION["main_menu"];
			
			if($main_menu=="main_merchant")
			{
				$fgmembersite->RedirectToURL("merchant_reports_menu.php");

			}else if($main_menu=="main_merchant_users_accounts"){

				$fgmembersite->RedirectToURL("merchant_reports_menu.php");

			}else if($main_menu=="main_merchant_users_admin"){

				$fgmembersite->RedirectToURL("merchant_reports_admin_menu.php");
			}


	}else{
			
			$fgmembersite->RedirectToURL("logout.php");
	}

}else {
   
	$username = $_SESSION["report_phone_no"];
	$report_type = $_SESSION["report_criteria_selected"];
	$start_date = $_SESSION["start_date"];
	$end_date = $_SESSION["end_date"];
	$report_country_phone_no = $_SESSION["report_country_phone_no"] ;
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$dataRow="";
	$sql= "SELECT name,surname FROM tbl_clients WHERE phone_number = :field_val and country_name = :field_val1"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $report_country_phone_no, PDO::PARAM_STR); 
	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
	$total = $stmt->rowCount();

 	if($total > 0){
				foreach($result as $row){
						$name = $row['name'];
   
						$surname = $row['surname'];
  			      
						$fullname=$name." ".$surname; 	
					}
	
	if($report_type=='PhoneNo'){	
	$sql= "Select tbl_bill_tariff_merchant_records.tariff_code,tbl_bill_tariff_merchant_records.transaction_date,tbl_bill_tariff_merchant_records.tariff_description,tbl_bill_tariff_merchant_records.merchant_doctor,tbl_bill_tariff_merchant_records.merchant_phone_number,SUM(tbl_bill_tariff_merchant_records.tariff_code_frequency) AS sum_tariff_frequency,tariff_amount,SUM(tariff_total_amount) AS sum_total_tariff_amount,SUM(no_of_points) As sum_no_of_points from tbl_bill_tariff_merchant_records WHERE tbl_bill_tariff_merchant_records.phone_number_billed =:field_val AND tbl_bill_tariff_merchant_records.phone_number_billed_country =:field_val1 GROUP BY 1"; 
}else{

	$sql= "Select tbl_bill_tariff_merchant_records.tariff_code,tbl_bill_tariff_merchant_records.transaction_date,tbl_bill_tariff_merchant_records.tariff_description,tbl_bill_tariff_merchant_records.merchant_doctor,tbl_bill_tariff_merchant_records.merchant_phone_number,SUM(tbl_bill_tariff_merchant_records.tariff_code_frequency) AS sum_tariff_frequency,tariff_amount,SUM(tariff_total_amount) AS sum_total_tariff_amount,SUM(no_of_points) As sum_no_of_points from tbl_bill_tariff_merchant_records WHERE tbl_bill_tariff_merchant_records.phone_number_billed =:field_val AND tbl_bill_tariff_merchant_records.phone_number_billed_country =:field_val1 AND transaction_date BETWEEN '$start_date' AND '$end_date' GROUP BY 1"; 


}
			
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $report_country_phone_no, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    							
								
								$tariff_code = $row['tariff_code'];
								$transaction_date = $row['transaction_date'];
								$tariff_description = $row['tariff_description'];
								$merchant_doctor = $row['merchant_doctor'];
								$merchant_phone_number = $row['merchant_phone_number'];	
								$sum_tariff_frequency = $row['sum_tariff_frequency'];	
								$tariff_amount = $row['tariff_amount'];	
								$sum_total_tariff_amount = $row['sum_total_tariff_amount'];	
								$sum_no_of_points = $row['sum_no_of_points'];	

$dataRow = $dataRow."<tr><td>$tariff_code</td><td>$tariff_description</td><td>$sum_tariff_frequency</td><td>$tariff_amount</td><td>$sum_total_tariff_amount</td><td>$merchant_doctor</td></tr>";
							}

					}else{
						
							$success = "false";
							$message = "You Have No Medical Records To View.";
							$_SESSION["message_create"] = $message;
							$main_menu = $_SESSION["main_menu"];
							if($main_menu=="main_merchant_users_admin"){
								$fgmembersite->RedirectToURL("confirm_merchant_reports_admin_messages.php");
							}else{
								$fgmembersite->RedirectToURL("confirm_merchant_reports_messages.php");
							}
					}
    
	}else{

					$success = "false";
					$message = "Phone Number Does Not Exist! Please Register And Try Again.";
					$_SESSION["message_create"] = $message;
					$main_menu = $_SESSION["main_menu"];
					if($main_menu=="main_merchant_users_admin"){
						$fgmembersite->RedirectToURL("confirm_merchant_reports_admin_messages.php");
					}else{
						$fgmembersite->RedirectToURL("confirm_merchant_reports_messages.php");
					}

	}     
}	
?>

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="refresh" content="900">
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

			<fieldset >
				<legend><?php echo "Summary Medical Bill Report For: ".$_SESSION["report_phone_no"]."(".$fullname.")"; ?></legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Code</th>
            						<th class="text-center">Description</th>
            						<th class="text-center">No</th>
            						<th class="text-center">Tariff Amount</th>
							<th class="text-center">Total Amount</th>
							<th class="text-center">Doctor</th>
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


