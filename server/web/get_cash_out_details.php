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
	$sendSMS= isset($_POST['SendSMS']);
	$confirm= isset($_POST['Confirm']);
	//$logout
	if($submitbutton){

		$fgmembersite->RedirectToURL("merchant_reports_menu.php");

	}else{
		if($sendSMS)
		{
			$fgmembersite->RedirectToURL("contact_sms_cash_out_reminder.php");

		}else{
			if($confirm){

				$fgmembersite->RedirectToURL("select_cashed_out_date.php");

			}else{

				$fgmembersite->RedirectToURL("logout.php");
			}
		}
	}

}else{
	$username = ($_SESSION["merchant_phone_number"]);
	$merchant_code = ($_SESSION["merchant_code"]);
	//$username='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
$todayDate =  date('Y-m-d');
$todaysdate = strtotime($todayDate);
	$dataRow = "";

$sql= "SELECT amount_cashed_out_date,merchant_phone_number,amount_cashed_out,cashed_out_transaction_no,(CURRENT_DATE-amount_cashed_out_date)As NoOfDays,amount_cashed_out_indicator FROM tbl_merchant_amount_cashed_out WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1 AND amount_paid_out_indicator = 'N' ORDER BY 1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					$num=0;
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    																			
												$date_cashed_out = $row['amount_cashed_out_date']; 
												$timestamp = strtotime($date_cashed_out); 
												$new_date = date('d-m-Y', $timestamp);
												$merchant_phone_number = $row['merchant_phone_number'];
												$amount_cashed_out = $row['amount_cashed_out'];
												$cashed_out_transaction_no = $row['cashed_out_transaction_no'];
												$noofdays = $todaysdate - $timestamp;
												$noofdays = round($noofdays/(60*60*24));
												$no_of_days = $row['NoOfDays'];
												$amount_cashed_out_indicator = $row['amount_cashed_out_indicator'];
												$num=$num + 1;
											
$dataRow = $dataRow."<tr><td>$new_date</td><td>$cashed_out_transaction_no</td><td>$amount_cashed_out</td><td>$noofdays</td><td>$amount_cashed_out_indicator</td><td><input type='submit' name='Confirm' value='Confirm Payment Received' onclick='getRowData($num)' /></td><td><input type='submit' name='SendSMS' value='Send SMS Remider' onclick='getRowSMSData($num)'/></td></tr>";
												
												
								}
						
					}else{
						
								$success = "false";
								$message = "You Have No Outstanding Amount Cashed Out...!!!";
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_merchant_reports_messages.php");
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
				
			<legend><?php echo "View Outstanding Cashed Out Points"; ?></legend>
				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Transaction No</th>
            						<th class="text-center">Amount</th>
            						<th class="text-center">Days</th>
							<th class="text-center">Cashed</th>
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
function getRowData(id_value)
{

var sel1 = document.getElementById('table1')
var transaction_no = sel1.rows[id_value].cells[1].innerHTML;

if (transaction_no.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "set_cashed_out_transaction_no.php",
            data: "cashed_out_transaction_no="+transaction_no,
            cache: false,
        });
    } 

}

function getRowSMSData(id_value)
{

var sel1 = document.getElementById('table1')
var transaction_no = sel1.rows[id_value].cells[1].innerHTML;
var no_of_points = sel1.rows[id_value].cells[2].innerHTML;

if (transaction_no.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "set_cashed_out_sms_transaction_no.php",
            data: "cashed_out_transaction_no="+transaction_no+'&no_of_points='+no_of_points,
            cache: false,
        });
    } 

}




</script>


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


