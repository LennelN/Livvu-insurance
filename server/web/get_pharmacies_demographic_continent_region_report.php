<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_pharmacies_demographic_continent_region_report';

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
   
	
	$continent = $_SESSION["selected_report_continent"];

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();
	$dataRow = "";
	$type_of_registration = 0;
	$registrationClientCount = 0;
	$gender = 0;
	$genderCount = 0;
	$dataRow = "";
	$data_found = 0;
	$total_count = 0;
	$data_found_region = 0;
	$region_processing = "";
	$sql = "SELECT name,region FROM `tbl_region_continent` WHERE continent = :field_val order by 2";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $continent, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0)
	{

			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row)
			{
				$country = $row['name'];
				$region = $row['region'];
				
		
				$sql = "SELECT merchant_description,count(merchant_description) AS CountOfSector FROM `tbl_merchant_details` where type_of_registration='Health' AND status_flag='Y' AND merchant_description = 'Pharmacy' AND merchant_country = :field_val GROUP BY 1 ";

					$stmt_male = $db->sth->prepare($sql);
					$stmt_male->bindParam(':field_val', $country, PDO::PARAM_STR);	
					$stmt_male->execute();
					$total_male = $stmt_male->rowCount();
					
					if($total_male > 0)
					{	
									
						$result = $stmt_male->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{  
							
								$merchant_description = $row['merchant_description'];
								$sectorCount = $row['CountOfSector'];
								if($sectorCount > 0){
									
									$data_found = $data_found + 1;
									$data_found_region = $data_found_region + 1;
								}
								if($data_found > 0)
								{
									if($region_processing == "")
									{
										$region_processing = $region;
									}else{
										if($region_processing != $region)
										{
											if($data_found_region > 0)
											{
												$dataRow = $dataRow."<tr><td></td><td>Total</td><td>$total_count</td></tr>";
$total_count=0;
											}
											$region_processing = $region;
										}else{
											$region="";
										}
									}
									$total_count = $total_count + $sectorCount ;
									$data_found = 0;
									$dataRow = $dataRow."<tr><td>$region</td><td>$country</td><td>$sectorCount</td></tr>";		
								}

							}
						
					}
					
			}//foreach
			if($data_found_region > 0)
											{
												$dataRow = $dataRow."<tr><td></td><td>Total</td><td>$total_count</td></tr>";
$total_count=0;
											}
			if($data_found_region == 0){
				$message = "No Pharmacies Registered For ".$continent;
				$_SESSION["message_create"] = $message;
				$fgmembersite->RedirectToURL("confirm_main_admin_reports_messages.php");
			}
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
				
				<legend><?php echo "Pharmacy Demographic Countinent Report For: ".$_SESSION["selected_report_continent"]; ?></legend>
				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
							<th class="text-center">Region</th>
            						<th class="text-center">Country</th>
							<th class="text-center">Count</th>
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



