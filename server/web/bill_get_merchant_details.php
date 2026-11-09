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
	$view= isset($_POST['View']);
	//$logout
	if($submitbutton){
	
		$fgmembersite->RedirectToURL("merchant_reports_menu.php");

	}else{
		if($send)
		{
			$fgmembersite->RedirectToURL("contact_sms_bill_reminder.php");

		}else if($view){

			$fgmembersite->RedirectToURL("bill_get_client_statement_merchant.php");

		}else{
			
			$fgmembersite->RedirectToURL("logout.php");
		}
	}

}else{
	$username = ($_SESSION["merchant_phone_number"]);
	//$username='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$todayDate =  date('Y-m-d');
	$todaysdate = strtotime($todayDate);

	$sql= "SELECT date_bill_created,phone_number_billed,bill_number,total_bill_amount FROM tbl_bill_merchant_records WHERE merchant_phone_number = :field_val AND bill_paid_indicator = 'N' ORDER BY 1 ASC"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												//$temp["success"] = true; 
							
												$date_of_bill = $row['date_bill_created'];																	$timestamp = strtotime($date_of_bill); 
												$new_date = date('d-m-Y', $timestamp);
												$dt   = new DateTime();
												$date_bill_created = $new_date;
												$phone_number_billed = $row['phone_number_billed'];
												$bill_number = $row['bill_number'];
												$total_bill_amount = $row['total_bill_amount'];
												$noofdays = $todaysdate - $timestamp;
												$noofdays = round($noofdays/(60*60*24));
												
												
		$dataRow = $dataRow."<tr><td>$date_bill_created</td><td>$phone_number_billed</td><td>$bill_number</td><td>$total_bill_amount</td><td>$noofdays</td><td><input type='submit' onclick='getCellData()' name='View' value='View' /></td><td><input type='submit' onclick='getCellSMSData()' name='Send' value='SMS' /></td></tr>";										
								}
								//echo json_encode($response);
					}else{
								$dataRow ="";
								$success = "false";
								$message = "You Have No Outstanding Bills To View.";
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
				<legend><?php echo "View Outstanding Bills"; ?></legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Phone</th>
            						<th class="text-center">Bill No</th>
            						<th class="text-center">Amount</th>
            						<th class="text-center">Days</th>
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


function getCellData(){

var table = document.getElementById("table1");
 
var row_phone_no="";
var row_bill_no="";
var row_points_no = "";
for(var i = 1; i <= table.rows.length-1; i++)
                
	{
  

		table.rows[i].onclick = function()
                    
		{
  
                	rIndex = this.rowIndex;

		
			row_phone_no = table.rows[rIndex].cells[1].innerHTML;
			row_bill_no = table.rows[rIndex].cells[2].innerHTML;
			row_points_no = table.rows[rIndex].cells[3].innerHTML;
			
		$.ajax({
            		type: "POST",
            		url: "create_bill_merchant_session_variables.php",
            		data: 'phone_no='+row_phone_no+'&bill_number='+row_bill_no,
           	 	cache: false,
			async: false,
        		});
		}
	}



		

}
function getCellSMSData(){

var table = document.getElementById("table1");
 
var row_phone_no="";
var row_bill_no="";
var row_points_no = "";

for(var i = 1; i <= table.rows.length-1; i++)
                
	{
  

		table.rows[i].onclick = function()
                    
		{
  
                	rIndex = this.rowIndex;

		
			row_phone_no = table.rows[rIndex].cells[1].innerHTML;
			row_bill_no = table.rows[rIndex].cells[2].innerHTML;
			row_points_no = table.rows[rIndex].cells[3].innerHTML;
			
		$.ajax({
            		type: "POST",
            		url: "create_bill_merchant_session_sms_variables.php",
            		data: 'phone_no='+row_phone_no+'&bill_number='+row_bill_no+'&no_of_points='+row_points_no,
           	 	cache: false,
			async: false,
        		});
		}
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


