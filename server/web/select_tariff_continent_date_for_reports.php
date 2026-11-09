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

	$submitbutton= isset($_POST['Submit']);

	//$logout
	if($submitbutton){
		$continent  = trim($_POST['continent']);
		if(strlen($continent) > 0 )
		{
				$_SESSION["selected_report_continent"] = $continent;
				$startdate  = trim($_POST['start_date']);
				$enddate  = trim($_POST['end_date']);
				$choice  = trim($_POST['choice']);
				if((strlen($startdate) > 0) && (strlen($enddate)>0) && (strlen($choice) > 0))
				{
					$_SESSION["report_tariff_code"] = $choice;
					$_SESSION["start_date"] = $startdate;
					$_SESSION["end_date"] = $enddate;
					if($startdate > $enddate)
					{
						$fgmembersite->HandleError("Start Report Date Cannot Be Greater Than The End Report Date.");
					}else{
					
							$fgmembersite->RedirectToURL("get_tariff_cost_per_common_disease_continent.php");
					}
				}else{
					$fgmembersite->HandleError("Enter Valid Date");
				}
		}else{
			$fgmembersite->HandleError("Enter Valid Continent");
		}
	}else{

		$fgmembersite->RedirectToURL("main_admin_reports_menu.php");
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

			<form id='phone' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset >
				<legend>Please Select Continent</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
    					<?php include "./include/config.php"; // Database connection using PDO
					
					 	$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
						$sql="Select DISTINCT(continent) from tbl_region_continent"; 
						echo "<select id=continent name=continent>";
						echo "<option value=''>Select Continent</option>"; // list box select command					
						foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["continent"].'">'.$row["continent"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
						echo "</select>";
						?>
				</div>

				<div class="col-xs-12 text-center">
<br/>
<br/>
					<label style="text-align: center;" for='choice' >Enter Tariff Code*:</label>
    					<input type='text' name='choice' id='choice' value='<?php echo $fgmembersite->SafeDisplay('choice') ?>' maxlength="10" /><br/><br/>
    					<span id='tariff_code_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<br/>
					<br/>
					<div class="col-xs-4 text-center">
    					<label style="text-align: left;" for='date' >Start Date*:</label>
    						<input type='date' name='start_date' id='start_date' value='<?php echo $fgmembersite->SafeDisplay('date') ?>' max='<?php echo date('Y-m-d');?>'/><br/>
    						<span id='start_date_errorloc' class='error'></span>
					</div>
					<div class="col-xs-4 text-center">
    						<label style="text-align: left;"  >To</label>
					</div>

					<div class="col-xs-4 text-center">
    						<label style="text-align: left;" for='date_to' >End Date*:</label>
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
