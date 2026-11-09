<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='bil_get_merchant_details';

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
	
		$fgmembersite->RedirectToURL("merchant_reports_menu.php");

	}else{
		if($send)
		{
			error_log(strip_tags("send was pressed"), 0);
		}else{
			
			$fgmembersite->RedirectToURL("logout.php");
		}
	}

}else{


	$bill_number = $_SESSION["bill_number_to_report"];
	$phone_number = $_SESSION["phone_number_to_report"];



$dataRow = "";
$dataRow2 = "";
$total_debit = 0;
$total_credit = 0;
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

		$qry = "Select name, surname from tbl_clients where phone_number = '$phone_number'";
		$stmt = $db->sth->prepare($qry);
        	$stmt->execute();
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
		$total =  $stmt->rowCount();
		if($total>0)
			{
    				foreach($result as $row)
					{
						$name = $row['name'];
   
						$surname = $row['surname'];
  			      
					}	
			}

			$client_name = $name." ".$surname;
				
$sql="Select transaction_date,tariff_code,tariff_description,tariff_total_amount,0 As no_of_points_paid,phone_number_billed FROM tbl_bill_tariff_merchant_records WHERE bill_number = :field_val ORDER BY 1";

				
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
					
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true;  
												$date_of_bill = $row['transaction_date'];
												$timestamp = strtotime($date_of_bill); 
												$new_date = date('Y-m-d', $timestamp);
												$temp["transaction_date"] = $new_date;
												$temp["tariff_code"] = $row['tariff_code'];
												$temp["tariff_description"] = $row['tariff_description'];
												$debit = $row['tariff_total_amount'];
												$temp["no_of_points"] = $debit;
												$credit = $row['no_of_points_paid'];
												$temp["no_of_points_paid"] = $credit;
												$total_debit = $total_debit + $debit;
												$total_credit = $total_credit + $credit;
												$temp["client_name"] = $client_name;
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								}

						$sql = "SELECT date_paid,transaction_type,payment_number,0 As no_of_points,amount_paid,phone_number from tbl_merchant_bill_payments where bill_number = 	:field_val1 ORDER BY 1";
						$stmt = $db->sth->prepare($sql);
					
						$stmt->bindParam(':field_val1', $bill_number, PDO::PARAM_STR); 
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true;  
												$date_of_payment = $row['date_paid'];
												$timestamp = strtotime($date_of_payment); 
												$new_date_payment = date('Y-m-d', $timestamp);
												$temp["transaction_date"] = $new_date_payment;
												$temp["tariff_code"] = $row['transaction_type'];
												$temp["tariff_description"] = $row['payment_number'];
												$debit = $row['no_of_points'];
												$temp["no_of_points"] = $debit;
												$credit = $row['amount_paid'];
												$temp["no_of_points_paid"] = $credit;
												$total_debit = $total_debit + $debit;
												$total_credit = $total_credit + $credit;
												$temp["client_name"] = $client_name;
												$message='Successful';
												$temp["message"] = $message;
												array_push($response, $temp);
												
												
								}
						}
								//error_log(strip_tags($response), 0);
								foreach($response as $row)
								{	
									$report_date = $row['transaction_date'];
									$dt   = new DateTime();
									$date_formated_out = $dt->createFromFormat('Y-m-d', $report_date);
									$transaction_date_result = $date_formated_out->format('d-m-Y');
									$tariff_code_result = $row['tariff_code'];
									$tariff_description_result=$row['tariff_description'];
									$debit_result = $row['no_of_points'];
									$credit_result = $row['no_of_points_paid'];
									
								$dataRow = $dataRow."<tr><td>$transaction_date_result</td><td>$tariff_code_result</td><td>$tariff_description_result</td><td>$debit_result</td><td>$credit_result</td></tr>";	
								}
								$total = "Total";
								$code = "";
								$desc = "";
								$dataRow = $dataRow."<tr><td>$total</td><td>$code</td><td>$desc</td><td>$total_debit</td><td>$total_credit</td></tr>";	
															
								$todayDate =  date('Y-m-d');
								$dateofbilling = strtotime($date_of_bill);
								$todaysdate = strtotime($todayDate);
								$noofdays = $todaysdate-$dateofbilling;
								$noofdays = round($noofdays/(60*60*24));
								$owingbalance = $total_debit - $total_credit;

								if($noofdays < 30)
								{
									$dataRow2 = $dataRow2."<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>$owingbalance</td></tr>";	
								}else if($noofdays >= 30 && $noofdays < 60){
									$dataRow2 = $dataRow2."<tr><td>0</td><td>0</td><td>0</td><td>$owingbalance</td><td>0</td></tr>";
								}else if($noofdays >= 60 && $noofdays < 90){
									$dataRow2 = $dataRow2."<tr><td>0</td><td>0</td><td>$owingbalance</td><td>0</td><td>0</td></tr>";
								}else if($noofdays >= 90 && $noofdays < 120){
									$dataRow2 = $dataRow2."<tr><td>0</td><td>$owingbalance</td><td>0</td><td>0</td><td>0</td></tr>";
								}else{
									$dataRow2 = $dataRow2."<tr><td>$owingbalance</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>";
								}
						
					}else{
						
								$success = "false";
								$message = "You Have No Outstanding Bills To View.";
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_merchant_reports_messages.php");
					}
    
 }	
/*if(isset($_SESSION["session_id"])) {
	
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
}*/
  	
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
<script src="js/jquery.min.js"></script>
  	<script src="js/bootstrap.min.js"></script>
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
				<legend><?php echo "View Outstanding Bills"; ?></legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Code</th>
            						<th class="text-center">Name</th>
            						<th class="text-center">Debit</th>
            						<th class="text-center">Credit</th>
        					</tr>
    					</thead> 
					<tbody>
        					<?php echo $dataRow;?>
					</tbody>

					

				</table>

			</div>
			<div id="table2" class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    				
					<thead>
        					<tr>
            						<th class="text-center">120days</th>
            						<th class="text-center">90days</th>
            						<th class="text-center">60days</th>
            						<th class="text-center">30days</th>
            						<th class="text-center">Owing</th>
        					</tr>
    					</thead> 
					<tbody>
        					<?php echo $dataRow2;?>
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
<div>   <?php include("./include/footer.php");?>
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


</body>
</html>
