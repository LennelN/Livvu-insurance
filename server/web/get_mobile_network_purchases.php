<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_ancillary_tariffs';

$response = array();
   
if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
	
	$submitbutton= isset($_POST['Submit']);
	$process= isset($_POST['Process']);
	//$logout
	if($submitbutton){

		$main_menu = $_SESSION["main_menu"];
			if($main_menu=="main_admin"){
				$fgmembersite->RedirectToURL("main_admin.php");
			}else{
				$fgmembersite->RedirectToURL("main_admin_accounts.php");
			}

	}else{
		if($process)
		{
			$fgmembersite->RedirectToURL("process_mobile_network_purchase.php");
		}else{
				$fgmembersite->RedirectToURL("logout.php");
		}
	}

}else {
  
	$mobile_operator = $_SESSION["selected_mobile"];
	$country_operator = $_SESSION["selected_country"];
	require_once __DIR__ . '/db_connect.php';
	$todayDate =  date('Y-m-d');
	$todaysdate = strtotime($todayDate);

	// connecting to db
    	$db = new DB_CONNECT();
	$dataRow = "";

					$sql= "SELECT date_of_purchase,phone_number,purchase_amount,purchase_done_by,transaction_number FROM tbl_purchase_points_details WHERE transaction_type ='Airtime' AND purchase_done_by = :field_val AND purchase_status = 'N' ORDER BY 1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $mobile_operator, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					$num =0;
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												$date_of_purchase = $row['date_of_purchase']; 
												$timestamp = strtotime($date_of_purchase); 
												$new_date = date('d-m-Y', $timestamp);
												$phone_number = $row['phone_number'];
												$purchase_amount = $row['purchase_amount'];
												$purchase_done_by = $row['purchase_done_by'];
												$noofdays = $todaysdate - $timestamp;
												$noofdays = round($noofdays/(60*60*24));
												$temp["no_of_days"] = $noofdays;
												$transaction_number = $row['transaction_number'];
							$num = $num + 1;

			$dataRow = $dataRow."<tr><td>$new_date</td><td>$phone_number</td><td>$purchase_amount</td><td>$transaction_number</td><td>$no_of_days</td><td><input type='submit' name='Process' value='Process' onclick='getRowData($num)' /></tr>";
												
												
								}
							
					}else{
						
								$success = "false";
								$message = "You Have No Outstanding Mobile Network Purchases...!!!";
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_main_admin_messages.php");
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
				
			<legend><?php echo "View Outstanding Mobile Network Purchases"; ?></legend>
				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Phone</th>
            						<th class="text-center">Amount</th>
            						<th class="text-center">Transaction No</th>
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
function getRowData(id_value)
{

var sel1 = document.getElementById('table1')
var mobile_phone_no = sel1.rows[id_value].cells[1].innerHTML;
var purchase_transaction_no = sel1.rows[id_value].cells[3].innerHTML;



     $.ajax({
            type: "POST",
            url: "set_mobile_purchase_transaction_no.php",
            data: 'purchase_transaction_no='+purchase_transaction_no+'&mobile_phone_no='+mobile_phone_no,
            cache: false,
        });
    

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