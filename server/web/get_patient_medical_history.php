<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_patient_medical_history';

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
	$sql= "SELECT name,surname FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $report_country_phone_no, PDO::PARAM_STR); 
	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
	$total = $stmt->rowCount();

 	if($total > 0){
			

	
	if($report_type=='PhoneNo'){	

	$sql= "SELECT `date_record_created`, `merchant_phone_number`, `merchant_name`, `body_temperature`, `blood_pressure`, `body_pulse`, `body_allergies`, `blood_type`, `other_details` FROM `tbl_patient_medical_history` WHERE phone_number = :field_val AND country = :field_val1 ORDER BY 1 DESC";

	
}else{

	$sql= "SELECT `date_record_created`, `merchant_phone_number`, `merchant_name`, `body_temperature`, `blood_pressure`, `body_pulse`, `body_allergies`, `blood_type`, `other_details` FROM `tbl_patient_medical_history` WHERE phone_number = :field_val AND country = :field_val1 AND date_record_created BETWEEN '$start_date' AND '$end_date' ORDER BY 1 DESC";
		
	
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
								
								$transaction_date = $row['date_record_created'];
								$timestamp = strtotime($transaction_date); 
								$new_date = date('d-m-Y', $timestamp);
								$dt   = new DateTime(); 
								$merchant_phone_number = $row['merchant_phone_number'];
								$merchant_name = $row['merchant_name'];
								$body_temperature = $row['body_temperature'];	
								$blood_pressure = $row['blood_pressure'];	
								$body_pulse = $row['body_pulse'];	
								$body_allergies = $row['body_allergies'];	
								$blood_type = $row['blood_type'];
								$other_details = $row['other_details'];	

			$dataRow = $dataRow."<tr><td>$new_date</td><td>$merchant_phone_number</td><td>$merchant_name</td><td>$body_temperature</td><td>$blood_pressure</td><td>$body_pulse</td><td>$body_allergies</td><td>$blood_type</td><td>$other_details</td></tr>";
											
							}
					}else{
						
							$success = "false";
							$message = "There Is No Medical History To View For Patient.";
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
				<legend><?php echo "Summary Medical History Report For: ".$_SESSION["report_phone_no"]; ?></legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Merchant Phone No</th>
            						<th class="text-center">Merchant Name</th>
            						<th class="text-center">Temperature</th>
            						<th class="text-center">BP</th>
							<th class="text-center">Pulse</th>
							<th class="text-center">Allergies</th>
							<th class="text-center">Blood Type</th>
							<th class="text-center">Other Details</th>
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


