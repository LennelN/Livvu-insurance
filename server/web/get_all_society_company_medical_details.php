<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_all_society_company_member_details';

$response = array();
   
if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
	

	$submitbutton= isset($_POST['Submit']);
	$send= isset($_POST['Send']);
	//$logout
	if($submitbutton){
		
		$fgmembersite->RedirectToURL("main_society_reports_menu.php");

	}else{
		if($send)
		{
			
		}else{
			
			$fgmembersite->RedirectToURL("logout.php");
		}
	}

}else{
	
	$username = ($_SESSION["user_name"]);
	//$username='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$no_of_bill_records = 0;
	$dataRow = "";


			$sql= "SELECT member_phone_number from tbl_society_company_members WHERE merchant_phone_number = :field_val"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){  
							$member_phone_number = trim($row['member_phone_number']);

							$sql_details= "SELECT date_bill_created,bill_number,merchant_name,total_bill_amount,bill_amount,bill_paid_indicator FROM tbl_bill_client_records WHERE phone_number = :field_val ORDER BY 6 ASC "; 
							$stmt_details = $db->sth->prepare($sql_details);
							$stmt_details->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR);
							$stmt_details->execute();
							$total_details = $stmt_details->rowCount();

							if($total_details > 0)
							{
  							
								$result_details = $stmt_details->fetchAll(PDO::FETCH_ASSOC); 
    								foreach($result_details as $row){  
    									
									$date_bill_created = $row['date_bill_created']; 
									$timestamp = strtotime($date_bill_created); 
									$new_date = date('d-m-Y', $timestamp);
									
									$bill_number=$row['bill_number'];
									$merchant_name=$row['merchant_name'];
									$total_bill_amount=$row['total_bill_amount'];
									$total_no_of_points=$row['bill_amount'];
									$bill_paid_indicator=$row['bill_paid_indicator'];
									//	$message='Successful';
									//$temp["message"] = $message;
									$no_of_bill_records = $no_of_bill_records + 1;

									//array_push($response, $temp);
	$dataRow = $dataRow."<tr><td>$new_date</td><td>$member_phone_number</td><td>$bill_number</td><td>$merchant_name</td><td>$total_bill_amount</td><td>$bill_paid_indicator</td></tr>";		
								}
							}
						}
								
					}else{
							$success = "false";
							$message = "There Are No Members In The System...Please Register Members !!";
							$_SESSION["message_create"] = $message;
							$fgmembersite->RedirectToURL("confirm_society_reports_messages.php");
						
					}


					if($no_of_bill_records <= 0 )
							{
								$success = "false";
								$message = "There Are No Bill Records To Display !!";
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_society_reports_messages.php");
								
							}
						

}
if(isset($_SESSION["session_id"])) {
	
	$current_time = time(); 
	if(isLoginSessionExpired()) {
		$message = "Login Session is Expired. Please Login Again";
		header("Location:logout.php?session_expired=1");
	}
	$_SESSION['loggedin_time'] = time();
}else{

header("Location:logout.php?session_new_created=1");
}

if(isset($_GET["session_expired"])) {
	$message = "Login Session is Expired. Please Login Again";
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
				<legend>Member Medical Reports</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Phone</th>
            						<th class="text-center">Bill No</th>
            						<th class="text-center">Name</th>
						<th class="text-center">Amount</th>
							<th class="text-center">Paid</th>
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


