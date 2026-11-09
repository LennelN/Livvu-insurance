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
		
		$main_menu = $_SESSION["main_menu"];

			if($main_menu=="main_livvu")
			{
				$fgmembersite->RedirectToURL("main_livvu.php");
			}else if ($main_menu == "main_admin_accounts"){
				$fgmembersite->RedirectToURL("main_admin_accounts.php");
			}else{
				$fgmembersite->RedirectToURL("main_admin.php");
			}

	}else{
		if($send)
		{
			
			$fgmembersite->RedirectToURL("confirm_payment.php");
		}else{
			
			$fgmembersite->RedirectToURL("logout.php");
		}
	}

}else{

//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
   
	$username = ($_SESSION["user_name"]);
	//$username = '0772455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

$dataRow = "";
					$sql= "SELECT date_bill_created,merchant_phone_number,merchant_code,merchant_name,bill_number,total_bill_amount,bill_amount ,phone_number,country FROM tbl_bill_client_records WHERE bill_paid_indicator = 'N' ORDER BY 1 DESC"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->execute();
					$total = $stmt->rowCount();
					
					$num = 0;
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$temp["success"] = true; 
												$date_of_bill = $row['date_bill_created']; 
												$timestamp = strtotime($date_of_bill); 
												$new_date = date('d-m-Y', $timestamp);
												$dt   = new DateTime();
												$date_bill_created = $new_date;
												$merchant_phone_number = $row['merchant_phone_number'];
												$merchant_code = $row['merchant_code'];
												$merchant_name = $row['merchant_name'];
																														$bill_number = $row['bill_number'];
												$total_bill_amount = $row['total_bill_amount'];
												$total_no_of_points = $row['bill_amount'];
												$phone_number = $row['phone_number'];
												$country = $row['country'];
												$message='Successful';
												$num=$num + 1;

					$dataRow = $dataRow."<tr><td>$num</td><td>$date_bill_created</td><td>$phone_number</td><td>$country</td><td>$merchant_name</td><td>$merchant_code</td><td>$bill_number</td><td>$total_bill_amount </td><td><input type='radio' name='Send' value='Send' onclick='getRowData($num)'>Pay</td><td><input type='radio' name='View' value='View' onclick='viewRowData($num)'>Details</td></tr>";		
	
								}
					}else{
						
								$success = "false";
								$message = "There Are No Outstanding Bills To Pay At The Moment.";
								
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
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
						<th class="text-center">No</th>
            						<th class="text-center">Date</th>
						<th class="text-center">Phone</th>
						<th class="text-center">Country</th>
            						<th class="text-center">Name</th>
            						<th class="text-center">Code</th>
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
					<input type='submit' name='Submit' value='Main Menu' />
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
function getRowData(id_value)
{

var sel1 = document.getElementById('table1')
var bill_no = sel1.rows[id_value].cells[6].innerHTML;
var merchant_code = sel1.rows[id_value].cells[5].innerHTML;
var merchant_name = sel1.rows[id_value].cells[4].innerHTML;
var no_of_points = sel1.rows[id_value].cells[7].innerHTML;

var b2 = bill_no

if (merchant_code.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "pay_bill_set_session.php",
            data: 'bill_number='+bill_no+'&merchant_name='+merchant_name+'&merchant_code='+merchant_code+'&no_of_points='+no_of_points,
            cache: false,
	async: false,
            	success: function(data) {  
                		message = data;
		window.location.href="confirm_payment.php";
            	}
        });
    } 

//window.location.href="confirm_payment.php";

}

function viewRowData(id_value)
{

var sel1 = document.getElementById('table1')
var bill_no = sel1.rows[id_value].cells[6].innerHTML;
var merchant_code = sel1.rows[id_value].cells[5].innerHTML;
var merchant_name = sel1.rows[id_value].cells[4].innerHTML;
var no_of_points = sel1.rows[id_value].cells[7].innerHTML;

if (merchant_code.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "view_bill_set_session.php",
            data: 'bill_number='+bill_no+'&merchant_name='+merchant_name+'&merchant_code='+merchant_code+'&no_of_points='+no_of_points,
            cache: false,
	async: false,
            	success: function(data) {  
                		message = data;
		window.location.href="bill_get_client_tariff_details.php";
            	}

        });
    } 

//window.location.href="bill_get_client_tariff_details.php";


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

</body>
</html>



