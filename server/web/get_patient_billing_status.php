<?php
    

session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_patient_billing_status';

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
			
			if($main_menu=="main_merchant")
			{
				$fgmembersite->RedirectToURL("merchant_reports_menu.php");

			}else if($main_menu=="main_merchant_users_accounts"){

				$fgmembersite->RedirectToURL("merchant_reports_menu.php");

			}else if($main_menu=="main_merchant_users_admin"){

				$fgmembersite->RedirectToURL("merchant_reports_admin_menu.php");
			}

	}else{
			
			$fgmembersite->RedirectToURL("logout.php");
	}

}else{
   
    
	date_default_timezone_set('Africa/Harare');
	$username = $_SESSION["report_phone_no"];
  	$report_country_phone_no = $_SESSION["report_country_phone_no"] ;
	$no_of_points_contributed_other_groups=0;  
	$no_of_points_contributed=0;
	$no_of_points_contributed_society=0;
	$no_of_points_contributed_other_groups_society=0;  
	require_once __DIR__ . '/db_connect.php';
	require_once __DIR__ . '/db_config.php';
	//$db = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$dataRow = "";
	$sql= "SELECT name,surname,dob,idno,gender,date_joined,member_active,country_name,group_category FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $report_country_phone_no, PDO::PARAM_STR); 
	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
	$total = $stmt->rowCount();

 	if($total > 0){
					foreach($result as $row){
									$name = $row['name'];
   
						$surname = $row['surname'];
  			      
						$fullname=$name." ".$surname; 
						$dob = $row['dob'];
						$idno = $row['idno'];
						$gender = $row['gender'];
						$date_of_joining = $row['date_joined'];
						$timestamp = strtotime($date_of_joining); 
						$new_date = date('d-m-Y', $timestamp);
						$member_active = $row['member_active'];
						$country_name = $row['country_name'];

						$group_category= $row['group_category'];		      
						}

					$sql_client_points= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
					$stmt_client_points = $db->prepare($sql_client_points);
					$stmt_client_points->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt_client_points->bindParam(':field_val1', $report_country_phone_no, PDO::PARAM_STR); 
					$stmt_client_points->execute();
					$total_client_points = $stmt_client_points->rowCount();

					if($total_client_points > 0)
					{//Dont Have Points	

						$no_of_points_client = $stmt_client_points->fetchColumn();
						$no_of_points = $no_of_points_client;
						$bonus_points = 0;

						$sql_client_up_to_date= "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val AND purchase_country = :field_val1"; 
						$stmt_client_up_to_date = $db->prepare($sql_client_up_to_date);
						$stmt_client_up_to_date->bindParam(':field_val', $username, PDO::PARAM_STR); 
						$stmt_client_up_to_date->bindParam(':field_val1', $report_country_phone_no, PDO::PARAM_STR); 
						$stmt_client_up_to_date->execute();
						$total_client_up_to_date = $stmt_client_up_to_date->rowCount();

						if($total_client_up_to_date > 0)
						{
							$client_due_date=$stmt_client_up_to_date->fetchColumn();
						}
						//array_push($response, $temp);
					}else{
						$no_of_points_client = 0;
						$no_of_points = $no_of_points_client;
						$bonus_points = 0;
						
					}
			
					$dataRow = $dataRow."<tr><td>$fullname</td><td>$dob</td><td>$idno</td><td>$gender</td><td>$new_date</td><td>$member_active</td><td>$country_name</td><td>$group_category</td><td>$no_of_points</td><td>$client_due_date</td></tr>";
	
		}else{

		  			$success = "false";
					$message = "Phone Number Does Not Exist! Please Register And Try Again.";
					$_SESSION["message_create"] = $message;
					$main_menu = $_SESSION["main_menu"];
					if($main_menu=="main_merchant_users_admin"){
						$fgmembersite->RedirectToURL("confirm_merchant_reports_admin_messages.php");
					}else{
						$fgmembersite->RedirectToURL("confirm_merchant_reports_messages.php");
					}
					

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
				<legend><?php echo "Medical Status Report For: ".$fullname; ?></legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Name</th>
            						<th class="text-center">Date Of Birth</th>
            						<th class="text-center">ID Number</th>
            						<th class="text-center">Gender</th>
            						<th class="text-center">Date Joined</th>
							<th class="text-center">Active</th>
							<th class="text-center">Country</th>
							<th class="text-center">Group</th>
							<th class="text-center">Balance</th>
							<th class="text-center">Due Date</th>
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


