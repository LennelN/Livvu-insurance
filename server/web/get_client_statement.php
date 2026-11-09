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

	//$logout
	if($submitbutton){
		$fgmembersite->RedirectToURL("main.php");

	}else{
		
		$fgmembersite->RedirectToURL("logout.php");
	}

}else{
	$username = ($_SESSION["user_name"]);
	//$username='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";

$sql="SELECT transaction_type,date_transfered,phone_number,phone_number_recipient,no_of_points_transfered, 0 As Credit from tbl_points_transfers WHERE phone_number=:field_val UNION SELECT transaction_type,date_transfered,phone_number_recipient,phone_number,0, no_of_points_transfered from tbl_points_transfers WHERE phone_number_recipient=:field_val UNION SELECT transaction_type,date_of_purchase,phone_number,transaction_number,0,purchase_amount FROM tbl_purchase_points_details WHERE phone_number=:field_val UNION SELECT merchant_name,date_bill_created,bill_number,merchant_phone_number,bill_amount,0 As Credit FROM tbl_bill_client_records WHERE phone_number=:field_val UNION SELECT transaction_type,date_paid,bill_number,merchant_phone_number,amount_paid,0 As Credit FROM tbl_merchant_bill_payments WHERE phone_number=:field_val ORDER BY 2 DESC";

					$statement_count = 0;
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{	
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										$statement_count = $statement_count + 1;
												  
												$transaction_type = $row['transaction_type'];
												$date_paid = $row['date_transfered']; 
												$timestamp = strtotime($date_paid); 
												$new_date = date('d-m-Y', $timestamp);
												$phone_number = $row['phone_number'];
												$merchant_phone_number = $row['phone_number_recipient'];
												$no_of_points_paid = $row['no_of_points_transfered'];
												$Credit = $row['Credit'];
												if($statement_count > 5){break;}
										
		$dataRow = $dataRow."<tr><td>$new_date</td><td>$transaction_type</td><td>$merchant_phone_number</td><td>$no_of_points_paid</td><td>$Credit</td></tr>";		
												
								}
						
					}else{
								$success = "false";
								$message = "No Transactions To View At The Moment.";
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_main_messages.php");
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
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Type</th>
            						<th class="text-center">Phone</th>
            						<th class="text-center">Debit</th>
							<th class="text-center">Credit</th>
        					</tr>
    					</thead> 
					<tbody>
        					<?php echo $dataRow;?>
					</tbody>
				</table>

			</div>
			

				<div div class="col-xs-12 text-center">
					<br/>
					<input type='submit' name='Submit' value='Main Menu' />
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


