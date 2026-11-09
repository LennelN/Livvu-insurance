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
		error_log(strip_tags("submit was pressed"), 0);
		$fgmembersite->RedirectToURL("main_society_reports_menu.php");

	}else{
		if($send)
		{
			error_log(strip_tags("send was pressed"), 0);
		}else{
			error_log(strip_tags("logout pressed"), 0);
			$fgmembersite->RedirectToURL("logout.php");
		}
	}

}else{
	
	$username = ($_SESSION["society_phone_no"]);
	//$username='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";

$item_count = 0;

$sql_details= "SELECT tbl_purchase_points_details.date_of_purchase,transaction_type,purchase_amount,no_of_points_purchased,transaction_number FROM tbl_purchase_points_details WHERE tbl_purchase_points_details.phone_number = :field_val ORDER BY 1 DESC "; 
							$stmt_details = $db->sth->prepare($sql_details);
							$stmt_details->bindParam(':field_val', $username, PDO::PARAM_STR);
							$stmt_details->execute();
							$total_details = $stmt_details->rowCount();
							if($total_details > 0)
							{
								$result_details = $stmt_details->fetchAll(PDO::FETCH_ASSOC); 
    								foreach($result_details as $row){  
    										//$success = true;
									if($item_count==5){break;}
										$date_of_purchase = $row['date_of_purchase']; 
										$timestamp = strtotime($date_of_purchase); 
										$new_date = date('d-m-Y', $timestamp);
									
									$transaction_type = $row['transaction_type'];
									$purchase_amount = $row['purchase_amount'];
									$no_of_points_purchased = $row['no_of_points_purchased'];
									$transaction_number = $row['transaction_number'];

									$item_count = $item_count + 1;

		$dataRow = $dataRow."<tr><td>$new_date</td><td>$transaction_type</td><td>$transaction_number</td><td>$purchase_amount</td></tr>";	
								}					
						 				
					}else{
						
								$success = "false";
								$message = "There Are No Purchase Details To Display...!!!";
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
				<legend>Member Purchases Report For: <?php echo $_SESSION["society_phone_no"]; ?></legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Date</th>
            						<th class="text-center">Type</th>
            						<th class="text-center">Transaction No</th>
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


