<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}


if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$merchant_patient_report_type = $_SESSION["merchant_patient_report_type"];
	$submitbutton= isset($_POST['Submit']);
	//$logout
	if($submitbutton){
		$choice  = trim($_POST['choice']);
		$startdate  = trim($_POST['start_date']);
		$enddate  = trim($_POST['end_date']);
//error_log(strip_tags("Error logging in. missing fields lokhe ibambe u web  ".$startdate." and ".$enddate), 0);
		$phone_no_country = trim($_POST['nameofcountry']);
		if(strlen($choice) > 0)
		{
				$_SESSION["report_phone_no"] = $choice;
				$_SESSION["start_date"] = $startdate;
				$_SESSION["end_date"] = $enddate;
				
				if((strlen($startdate) > 0) && (strlen($enddate)>0))
				{
					if($startdate > $enddate)
					{
						$fgmembersite->HandleError("Start Report Date Cannot Be Greater Than The End Report Date.");
					}else{

						$_SESSION["report_criteria_selected"] = "DateRange";
						if(empty($_POST['nameofcountry'])){
							$fgmembersite->HandleError("Select Country To Run Report");
						}else{
							$_SESSION["report_country_phone_no"] = $phone_no_country;
							if($merchant_patient_report_type=="Detailed Medical Report")
							{
								$fgmembersite->RedirectToURL("get_patient_medical_details_client.php");
							}else if($merchant_patient_report_type=="Summary Medical Report"){
							
								$fgmembersite->RedirectToURL("get_patient_medical_details_summary.php");
						
							}else if($merchant_patient_report_type=="Billing Medical Report"){

								$fgmembersite->RedirectToURL("get_patient_medical_bills.php");
						
							}else if($merchant_patient_report_type=="Patient Medical History"){

								$fgmembersite->RedirectToURL("get_patient_medical_history.php");
										
							}
						}
					}
				}else{

					if((strlen($startdate) == 0) && (strlen($enddate)==0))
					{
						
						$_SESSION["report_criteria_selected"] = "PhoneNo";
						if(empty($_POST['nameofcountry'])){
							$fgmembersite->HandleError("Select Country To Run Report");
						}else{
							$_SESSION["report_country_phone_no"] = $phone_no_country;
							if($merchant_patient_report_type=="Detailed Medical Report")
							{
								$fgmembersite->RedirectToURL("get_patient_medical_details_client.php");

							}else if($merchant_patient_report_type=="Summary Medical Report"){
							
								$fgmembersite->RedirectToURL("get_patient_medical_details_summary.php");
						
							}else if($merchant_patient_report_type=="Billing Medical Report"){

								$fgmembersite->RedirectToURL("get_patient_medical_bills.php");
						
							}else if($merchant_patient_report_type=="Patient Medical History"){

								$fgmembersite->RedirectToURL("get_patient_medical_history.php");
						
							}
						}
					}else{

						if((strlen($startdate) > 0) && (strlen($enddate)<=0))
						{
							$fgmembersite->HandleError("End Report Date Cannot Be Empty. Please Enter The Date Or Remove The Start Date For Your Report");				}else{
							if((strlen($startdate) <= 0) && (strlen($enddate)>0))
							{
								$fgmembersite->HandleError("Start Report Date Cannot Be Empty. Please Enter The Date Or Remove The End Date For Your Report");					}					
						}
					}

				}
		}else{
			$fgmembersite->HandleError("Please Enter Phone Number");
		}
	}else{

		$main_menu = $_SESSION["main_menu"];
			
			if($main_menu=="main_merchant")
			{
				$fgmembersite->RedirectToURL("merchant_reports_patient_menu.php");
			}else{
				$fgmembersite->RedirectToURL("merchant_reports_patient_menu.php");
			}
	}

}


if(isset($_SESSION["user_id"])) {
	$current_time = time(); 
	if(isLoginSessionExpired()) {
		header("Location:logout.php?session_expired=1");
	}
	$_SESSION['loggedin_time'] = time();
}

if(isset($_GET["session_expired"])) {
	$message = "Login Session is Expired. Please Login Again";
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

			<form id='phone' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset >
				<legend>Phone Number And Date Range Report</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
					<label style="text-align: center;" for='choice' >Enter Phone Number*:</label>
    					<input type='text' name='choice' id='choice' value='<?php echo $fgmembersite->SafeDisplay('choice') ?>' maxlength="10" /><br/><br/>
    					<span id='phone_choice_errorloc' class='error'></span>
					<br/>
				</div>
				<div class="col-xs-12 text-center">
					<label  for='nameofcountry' >Select Country*:</label>
					
    					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$sql="Select country_name from tbl_country_codes"; 
					echo "<select id=nameofcountry name=nameofcountry>";
					echo "<option value=''>Select Country</option>"; // list box select command					
					 //echo '<option selected="selected" value="'.$row["public_desc"].'">'.$row["public_desc"].'</option>';
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["country_name"].'">'.$row["country_name"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
        			?><br/>

    					<span id='phone_nameofcountry_errorloc' class='error'></span>
				<br/>
				</div>

				<div class="col-xs-12 text-center">
					<label style="text-align: center;" >Enter Date Range(Optional):</label><br/>
					<div class="col-xs-4 text-center">
    					<label style="text-align: left;" for='date' >Start Date:</label>
    						<input type='date' name='start_date' id='start_date' value='<?php echo $fgmembersite->SafeDisplay('date') ?>' max='<?php echo date('Y-m-d');?>'/><br/>
    						<span id='start_date_errorloc' class='error'></span>
					</div>
					<div class="col-xs-4 text-center">
    						<label style="text-align: left;"  >To</label>
					</div>

					<div class="col-xs-4 text-center">
    						<label style="text-align: left;" for='date_to' >End Date:</label>
    						<input type='date' name='end_date' id='end_date' value='<?php echo $fgmembersite->SafeDisplay('date') ?>' max='<?php echo date('Y-m-d');?>'/><br/>
    						<span id='end_date_errorloc' class='error'></span>
					</div>

				</div>



				<div div class="col-xs-12 text-center">
					<br/>
    					<input type='submit' name='Submit' value='Submit' />
					<input type='submit' name='Logout' value='Cancel' /><br/><br/>
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
