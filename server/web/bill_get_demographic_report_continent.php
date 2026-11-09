<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='bill_get_demographic_report_continent';

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
	
		$fgmembersite->RedirectToURL("main_admin_reports_menu.php");

	}else{
		
			$fgmembersite->RedirectToURL("logout.php");
	}

}else{

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$dataRow = "";
	$type_of_registration = 0;
	$registrationClientCount = 0;
	$gender = 0;
	$genderCount = 0;
	$continent = $_SESSION["selected_report_continent"];

	$data_found = 0;
	$region_processing = "";
	$sql = "SELECT name,region FROM `tbl_region_continent` WHERE continent = :field_val order by 2";
	//$sql = "SELECT name,region FROM `tbl_region_continent` Order by 2";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $continent, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0){

			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row){
				$country = $row['name'];
				$region = $row['region'];

				if($region_processing == "")
				{
					$region_processing = $region;
				}else{
					if($region_processing != $region)
					{
						$region_processing = $region;
					}else{
						$region="";
					}
				}
				
				//$data_found = 0;
				$sql = "SELECT gender,Count(tbl_clients.gender) AS CountOfclient_gender FROM tbl_clients WHERE member_active = 'Y' AND gender = 'Male' AND country_name = :field_val";

					$stmt_male = $db->sth->prepare($sql);
					$stmt_male->bindParam(':field_val', $country, PDO::PARAM_STR);	
					$stmt_male->execute();
					$total_male = $stmt_male->rowCount();
					
					if($total_male > 0)
					{	
									
						$result = $stmt_male->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    
												
								$gender = $row['gender'];
								$genderCount = $row['CountOfclient_gender'];
								if($genderCount > 0){
									$data_found = $data_found + 1;
									$dataRow = $dataRow."<tr><td>$region</td><td>$country</td><td>$gender</td><td>$genderCount</td></tr>";
								}

							}
					}



$sql_female = "SELECT gender,Count(tbl_clients.gender) AS CountOfclient_gender FROM tbl_clients WHERE member_active = 'Y' AND gender = 'Female' AND country_name = :field_val";

					$stmt_female = $db->sth->prepare($sql_female);
					$stmt_female->bindParam(':field_val', $country, PDO::PARAM_STR);	
					$stmt_female->execute();
					$total_female = $stmt_female->rowCount();
					
					if($total_female > 0)
					{					
						$result = $stmt_female->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    					
								$gender = $row['gender'];
								$genderCount = $row['CountOfclient_gender'];
								if($genderCount > 0){
									$data_found = $data_found + 1;
									$dataRow = $dataRow."<tr><td>$region</td><td>$country</td><td>$gender</td><td>$genderCount</td></tr>";
								}
							}
					}


$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND type_of_registration='Health' AND merchant_country = :field_val";
			
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{    				
								$type_of_registration = $row['type_of_registration'];
								$registrationClientCount = $row['CountOfclient_registration'];
							if($registrationClientCount > 0){
								$data_found = $data_found + 1;
								$dataRow = $dataRow."<tr><td>$region</td><td>$country</td><td>$type_of_registration</td><td>$registrationClientCount</td></tr>";
							}

							}
						}


$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND type_of_registration='Society' AND merchant_country = :field_val";
			
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{    						
								$type_of_registration = $row['type_of_registration'];
								$registrationClientCount = $row['CountOfclient_registration'];

							if($registrationClientCount > 0){
								$data_found = $data_found + 1;
								$dataRow = $dataRow."<tr><td>$region</td><td>$country</td><td>$type_of_registration</td><td>$registrationClientCount</td></tr>";
							}

							}
						}


$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND type_of_registration='Company' AND merchant_country = :field_val";
			
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{  
								$type_of_registration = $row['type_of_registration'];
								$registrationClientCount = $row['CountOfclient_registration'];

							if($registrationClientCount > 0){
								$data_found = $data_found + 1;
								$dataRow = $dataRow."<tr><td>$region</td><td>$country</td><td>$type_of_registration</td><td>$registrationClientCount</td></tr>";
							}

							}
						}


$sql = "SELECT type_of_registration,Count(tbl_merchant_details.type_of_registration) AS CountOfclient_registration FROM tbl_merchant_details WHERE status_flag = 'Y' AND type_of_registration='Private' AND merchant_country = :field_val";
			
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $country, PDO::PARAM_STR);
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row)
							{  							
								$type_of_registration = $row['type_of_registration'];
								$registrationClientCount = $row['CountOfclient_registration'];

								if($registrationClientCount > 0){
									$data_found = $data_found + 1;
									$dataRow = $dataRow."<tr><td>$region</td><td>$country</td><td>$type_of_registration</td><td>$registrationClientCount</td></tr>";
								}

							}
						}

			}//foreach
				
				if($data_found == 0){
					$message = "No Demographic Data For Region ".$region;
					$_SESSION["message_create"] = $message;
					$fgmembersite->RedirectToURL("confirm_main_admin_reports_messages.php");
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
				<legend><?php echo "Demographic Country Report For: ".$_SESSION["selected_report_continent"]; ?></legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table class="col-xs-12 text-center">
    					<thead>
        					<tr>
							<th class="text-center">Region</th>
            						<th class="text-center">Country</th>
            						<th class="text-center">Category</th>
            						<th class="text-center">Number</th>
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



