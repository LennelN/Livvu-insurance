<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='bil_get_merchant_details';

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
   
if ($_SERVER["REQUEST_METHOD"] == "POST") {
	$submitbutton= isset($_POST['Submit']);
	
	//$logout
	if($submitbutton){
		
		$fgmembersite->RedirectToURL("merchant_reports_menu.php");

	}else{
	
			$fgmembersite->RedirectToURL("logout.php");
	}

}else{
	$username = ($_SESSION["merchant_phone_number"]);
	$merchant_code = ($_SESSION["merchant_code"]);
	//$username='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";

	$sql= "SELECT date_paid,phone_number,bill_number,amount_paid FROM tbl_merchant_bill_payments WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1 ORDER BY 1 DESC"; 
			
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    							
								$date_of_payment = $row['date_paid']; 
								$timestamp = strtotime($date_of_payment); 
								$new_date = date('d-m-Y', $timestamp);
								$dt   = new DateTime();
								$phone_number = $row['phone_number'];
								$bill_number = $row['bill_number'];
								$amount_paid = $row['amount_paid'];															
$dataRow = $dataRow."<tr><td>$new_date</td><td>$phone_number</td><td>$bill_number</td><td>$amount_paid</td></tr>";			
							}
					}else{
						
							$success = "false";
							$message = "You Have No Payments To View.";
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
				
				<legend>Payment Details Report</legend>
				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Phone</th>
            						<th class="text-center">Bill No</th>
            						<th class="text-center">Amount</th>
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


