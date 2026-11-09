<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_pharmacies_demographic_country_region_report';

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

//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
   	
	$country = $_SESSION["selected_report_country"];
	//$username = '0772455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

$dataRow = "";
$merchant_region="";
$merchant_region_data_processed = "";
$total_count = 0;
	$sql_hospitals= "SELECT DISTINCT(tbl_merchant_details.merchant_region) FROM tbl_merchant_details WHERE merchant_country=:field_val AND type_of_registration = 'Health' AND merchant_description = 'Pharmacy' ORDER BY 1";
					$stmt_hospitals = $db->sth->prepare($sql_hospitals);
					$stmt_hospitals->bindParam(':field_val', $country, PDO::PARAM_STR); 
					$stmt_hospitals->execute();
					$total_hospitals = $stmt_hospitals->rowCount();
					$num = 0;
					
					if($total_hospitals > 0 )
					{
						$total_points_cashed_out=0;
						$total_points_paid=0;
						$balance=0;
						$result = $stmt_hospitals->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){
									$merchant_region = $row['merchant_region'];
	
									
									$sql= "SELECT merchant_description,COUNT(merchant_description) As DescriptionCount FROM tbl_merchant_details WHERE tbl_merchant_details.merchant_region = :field_val AND status_flag='Y' AND tbl_merchant_details.merchant_country = :field_val1 AND merchant_description = 'Pharmacy' AND type_of_registration = 'Health' GROUP BY 1 ORDER BY 1";

									$stmt = $db->sth->prepare($sql);
									$stmt->bindParam(':field_val', $merchant_region, PDO::PARAM_STR); 
									$stmt->bindParam(':field_val1', $country, PDO::PARAM_STR);
									$stmt->execute();
									$total = $stmt->rowCount();
									if($total > 0)
									{
										
										$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
										foreach($result as $row)
										{
											$merchant_description = $row['merchant_description'];

											$merchant_count = $row['DescriptionCount'];
	
											$total_count = $total_count + $merchant_count;
											if($merchant_region_data_processed=="")
											{
												$merchant_region_data_processed = $merchant_region;
												$num = $num + 1;
											}else{
												if($merchant_region_data_processed != $merchant_region)
												{
													$num = $num + 1;
													$merchant_region_data_processed = $merchant_region;
												}else{
													$num = 0;
													$merchant_region="";
												}
											}
					 					
											$dataRow = $dataRow."<tr><td>$merchant_region</td><td>$merchant_count</td></tr>";		
	
										}

									} 					
							}
											$dataRow = $dataRow."<tr><td>Total</td><td>$total_count</td></tr>";
					}else{
						
								$success = "false";
								$message = "No Pharmacies Registered For ".$country;
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_main_admin_reports_messages.php");
								
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
				
				<legend><?php echo "Pharmacy Demographic Country Count Report For: ".$_SESSION["selected_report_country"]; ?></legend>
				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
							<th class="text-center">Region</th>
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



