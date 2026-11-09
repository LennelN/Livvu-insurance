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
		//error_log(strip_tags("submit was pressed"), 0);
		$fgmembersite->RedirectToURL("main_society_reports_menu.php");

	}else{
		if($send)
		{
			error_log(strip_tags("send was pressed"), 0);
		}else{
			//error_log(strip_tags("logout pressed"), 0);
			$fgmembersite->RedirectToURL("logout.php");
		}
	}

}else{
	$username = ($_SESSION["user_name"]);
	$merchant_code = ($_SESSION["merchant_code"]);
	//$username='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";


			$sql= "SELECT member_phone_number,member_phone_country from tbl_society_company_members WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){  
							$member_phone_number = trim($row['member_phone_number']);
							$member_phone_country = trim($row['member_phone_country']);
							$sql_details= "SELECT tbl_clients.name,tbl_clients.surname,tbl_clients.date_joined,MAX(tbl_purchase_points_details.due_date) As due_date FROM tbl_clients INNER JOIN tbl_purchase_points_details ON(tbl_clients.phone_number = tbl_purchase_points_details.phone_number AND tbl_clients.phone_number = :field_val AND tbl_clients.country_name = :field_val1 AND tbl_clients.member_active = 'Y') ORDER BY 1 DESC "; 
							$stmt_details = $db->sth->prepare($sql_details);
							$stmt_details->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR);
							$stmt_details->bindParam(':field_val1', $member_phone_country, PDO::PARAM_STR);
							$stmt_details->execute();
							$total_details = $stmt_details->rowCount();
							if($total_details > 0)
							{
  							
								$result_details = $stmt_details->fetchAll(PDO::FETCH_ASSOC); 
    								foreach($result_details as $row){  
    										
										$client_name = $row['name'];
										$client_surname = $row['surname']; 
										$name = $client_name." ".$client_surname;
										$date_joined = $row['date_joined']; 
										$timestamp = strtotime($date_joined); 
										$new_date = date('d-m-Y', $timestamp);
										$due_date = $row['due_date'];
										if($due_date == NULL)
										{
											$due_date=0;
									
										}									 
									$sql_points = "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1 "; 
										$stmt_points = $db->sth->prepare($sql_points);
										$stmt_points->bindParam(':field_val', $member_phone_number, PDO::PARAM_STR); 
										$stmt_points->bindParam(':field_val1', $member_phone_country, PDO::PARAM_STR); 
										$stmt_points->execute();
										$total_points = $stmt_points->rowCount();
										if($total_points > 0)
										{
											$no_of_points = $stmt_points->fetchColumn();
										}else{
											$no_of_points = 0;
										}
									
$dataRow = $dataRow."<tr><td>$member_phone_number</td><td>$name</td><td>$due_date</td><td>$no_of_points</td><td><input type='submit' name='Send' value='Send' /></td></tr>";		
								}
						

							}
												
						  }					
						//echo json_encode($response);
					}else{
						
								$success = "false";
								$message = "There Are No Members To Display. Please Add Members To Your Account...!!!";
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_society_reports_messages.php");
					}

}  

if(isset($_SESSION["session_id"])) {
	error_log(strip_tags("id still valid"), 0);
	$current_time = time(); 
	if(isLoginSessionExpired()) {
		$message = "Login Session is Expired. Please Login Again";
		header("Location:logout.php?session_expired=1");
	}
	$_SESSION['loggedin_time'] = time();
}else{

error_log(strip_tags("id destroyed"), 0);
error_log(strip_tags($_SESSION["session_id"]), 0);

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
				<legend>Member Balances Report</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Phone</th>
            						<th class="text-center">Name</th>
            						<th class="text-center">Due Date</th>
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


