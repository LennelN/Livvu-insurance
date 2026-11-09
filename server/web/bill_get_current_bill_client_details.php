<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='bil_get_current_bill_client_details';

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
		
		$fgmembersite->RedirectToURL("main.php");

	}else{
		if($send)
		{
			
			$fgmembersite->RedirectToURL("confirm_payment.php");
		}else{
			
			$fgmembersite->RedirectToURL("logout.php");
		}
	}

}else{

	$response = array();
	$username = ($_SESSION["user_name"]);
//error_log(strip_tags("Error logging in. missing fields"), 0);
	$total_no_of_points_paid = 0;

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$dataRow = "";
$num=0;
		$sql = "SELECT DISTINCT(bill_number),merchant_phone_number,merchant_code FROM `tbl_bill_items_tariff_records` WHERE phone_number_billed = :field_val ORDER BY 1 ASC";
					
		$stmt = $db->sth->prepare($sql);
		$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					
		$stmt->execute();
		$total = $stmt->rowCount();
					
		if($total > 0)
		{	

			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row){   
				$bill_number = $row['bill_number'];
				$merchant_phone_number = $row['merchant_phone_number'];
				$merchant_code = $row['merchant_code'];

			}

			$sql= "SELECT SUM(no_of_points_paid) As no_of_points_paid FROM `tbl_merchant_bill_payments` WHERE bill_number = :field_val";
			$stmt = $db->sth->prepare($sql);
			$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
			$stmt->execute();
			$total = $stmt->rowCount();
					
			if($total > 0)
			{					
				$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    				foreach($result as $row){   
					$total_no_of_points_paid = $row['no_of_points_paid'];
				}
			}


			$sql= "SELECT SUM(no_of_points) As total_no_of_points,SUM(tariff_total_amount) As total_bill_amount FROM tbl_bill_items_tariff_records WHERE bill_number = :field_val"; 
			$stmt = $db->sth->prepare($sql);
			$stmt->bindParam(':field_val', $bill_number, PDO::PARAM_STR); 
			$stmt->execute();
			$total = $stmt->rowCount();
					
			if($total > 0)
			{					
				$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    				foreach($result as $row){   
					$total_no_of_points = $row['total_no_of_points'];
					$total_bill_amount = $row['total_bill_amount'];
				}
			}
			
			$sql= "SELECT merchant_name FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
			$stmt = $db->sth->prepare($sql);
			$stmt->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
			$stmt->execute();
			$total = $stmt->rowCount();
					
			if($total > 0)
			{		
				$merchant_name = $stmt->fetchColumn();
			}


				
												
    										//$success = true;
												$temp["success"] = true; 
												$new_date = date('d-m-Y');
												$dt   = new DateTime();
												$date_bill_created = $new_date;
												$merchant_phone_number = $merchant_phone_number;
												$merchant_code = $merchant_code;
												$merchant_name = $merchant_name;
																	$bill_number = $bill_number;
												$total_bill_amount = $total_bill_amount;
												$total_no_of_points = $total_no_of_points - $total_no_of_points_paid;
												$message='Successful';
															
								
												$num=$num + 1;

					$dataRow = $dataRow."<tr><td>$num</td><td>$date_bill_created</td><td>$merchant_name</td><td>$merchant_code</td><td>$bill_number</td><td>$total_no_of_points</td><td><input type='radio' name='Pay' value='Pay' onclick='getRowData($num)'>Pay</td><td><input type='radio' name='View' value='View' onclick='viewRowData($num)'>Details</td></tr>";		
	}else{
						
		$success = "false";
		$message = "You Have No Current Bill To Pay.";
		$_SESSION["message_create"] = $message;
		$fgmembersite->RedirectToURL("confirm_main_messages.php");
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
            						<th class="text-center">Name</th>
            						<th class="text-center">Code</th>
            						<th class="text-center">Bill No</th>
            						<th class="text-center">Points</th>
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
var bill_no = sel1.rows[id_value].cells[4].innerHTML;
var merchant_code = sel1.rows[id_value].cells[3].innerHTML;
var merchant_name = sel1.rows[id_value].cells[2].innerHTML;
var no_of_points = sel1.rows[id_value].cells[5].innerHTML;

var b2 = bill_no

if (merchant_code.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "pay_bill_set_session.php",
            data: 'bill_number='+bill_no+'&merchant_name='+merchant_name+'&merchant_code='+merchant_code+'&no_of_points='+no_of_points,
            cache: false,
        });
    } 

window.location.href="confirm_current_bill_payment.php";

}

function viewRowData(id_value)
{

var sel1 = document.getElementById('table1')
var bill_no = sel1.rows[id_value].cells[4].innerHTML;
var merchant_code = sel1.rows[id_value].cells[3].innerHTML;
var merchant_name = sel1.rows[id_value].cells[2].innerHTML;
var no_of_points = sel1.rows[id_value].cells[5].innerHTML;


if (merchant_code.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "view_bill_set_session.php",
            data: 'bill_number='+bill_no+'&merchant_name='+merchant_name+'&merchant_code='+merchant_code+'&no_of_points='+no_of_points,
            cache: false,
        });
    } 

window.location.href="bill_get_current_client_statement.php";


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