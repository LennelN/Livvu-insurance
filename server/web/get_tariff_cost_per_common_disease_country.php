<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_cost_per_common_disease_country';

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

	
	//$username = ($_SESSION["merchant_phone_number"]);
	$country = $_SESSION["selected_report_country"];
	$startdate = $_SESSION["start_date"];
	$enddate = $_SESSION["end_date"];
	$tariff_code = $_SESSION["report_tariff_code"];

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$merchant_region="";
	$merchant_region_data_processed = "";
	$total_count = 0;
	$average_cost = 0;


	$len_phone_nos = 0;
	$in_clause_phone_nos="";
	$sql = "SELECT merchant_phone_number FROM tbl_merchant_details WHERE merchant_country = :field_val ORDER BY 1";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
									
	if($total > 0 )
	{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row)
		{	
				
			$merchant_phone_no = $row['merchant_phone_number'];

			$in_clause_phone_nos = $in_clause_phone_nos."'".$merchant_phone_no."',";
		}
	}

	$len_phone_nos = strlen($in_clause_phone_nos);
	$len_phone_nos = $len_phone_nos - 1;
	$merchant_phone_numbers = substr($in_clause_phone_nos, 0, $len_phone_nos);


	$len = 0;
	$in_clause="";

	$sql = "SELECT admission_tariff_code FROM tbl_country_admissions_tariff_codes WHERE country_name = :field_val ORDER BY 1";
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();
									
	if($total > 0 )
	{
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    		foreach($result as $row)
		{	
				
			$admission_tariff_code = $row['admission_tariff_code'];

			$in_clause = $in_clause."'".$admission_tariff_code."',";
		}
	}
	$len = strlen($in_clause);
	$len = $len - 1;
	$tariff_code_in = substr($in_clause, 0, $len);

	$sql = "SELECT tariff_code,count(tariff_code) AS tariffCount,tariff_description,SUM(`tariff_total_amount`)  AS totalAmount FROM `tbl_bill_tariff_merchant_records` where tariff_code = '$tariff_code' AND (transaction_date BETWEEN '$startdate' AND '$enddate') AND merchant_phone_number IN($merchant_phone_numbers)  GROUP BY tariff_code";
					$stmt = $db->sth->prepare($sql);
					$stmt->execute();
					$total = $stmt->rowCount();
					$num = 0;
					
					if($total > 0 )
					{
						$total_points_cashed_out=0;
						$total_points_paid=0;
						$balance=0;
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
						{
							$tariff_code = $row['tariff_code'];
	
							$total_count = $row['tariffCount'];
							$tariff_description = $row['tariff_description'];
							$totalAmount = $row['totalAmount'];
							$num = $num + 1;
							$average_cost = round($totalAmount / $total_count);
							$dataRow = $dataRow."<tr><td>$tariff_code</td><td>$tariff_description</td><td>$totalAmount</td><td>$average_cost</td><td>$total_count</td></tr>";		
	
						}
					}else{						
								$success = "false";
								$message = "No Costs For Tariff Code ".$tariff_code." During This Period From ".$startdate." To ".$enddate;
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
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
							<th class="text-center">Code</th>
							<th class="text-center">Description</th>
							<th class="text-center">Total Amount</th>
							<th class="text-center">Average Cost</th>
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



