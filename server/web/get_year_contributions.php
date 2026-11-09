<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_currency_rate';

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
   
if ($_SERVER["REQUEST_METHOD"] == "POST") {
	
	$submitbutton= isset($_POST['Submit']);
	
	//$logout
	if($submitbutton){

		if($_SESSION["main_menu"] == "main_livvu"){
			$fgmembersite->RedirectToURL("main_livvu_system_maintenance.php");
		}else{
			$fgmembersite->RedirectToURL("main_admin_system_maintenance.php");
		}

	}else{
			
		$fgmembersite->RedirectToURL("logout.php");
	}

}else {

	$country = $_SESSION["selected_country_contribution"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();

	$qry = "Select country,amount from tbl_year_country_contributions WHERE country = '$country' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
        	$stmt->execute();
	$total = $stmt->rowCount();
					
					if($total > 0){
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){
							$temp["success"] = true;  
								$country_contribution = $row['country'];
								$amount = $row['amount'];
			
							}
		
					}else{
						$success = "false";
						$message = "No Year Contributing Amount For  ".$country." Has Been Created In The System ";
						$_SESSION["message_create"] = $message;
						$fgmembersite->RedirectToURL("confirm_main_maintenance_messages.php");
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

			<form id='process' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset >
				<legend>Update Currency Rate Form</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
			

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='country' >Country*:</label>
    					<input class="col-xs-9 text-center" type='text' name='country' id='country' value='<?php echo $country; ?>' maxlength="30" /><br/>
    					<span id='process_country_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='camount' >Amount*:</label>
    					<input class="col-xs-9 text-center" type='text' name='camount' id='camount' value='<?php echo $amount; ?>' maxlength="30" /><br/>
    					<span id='process_camount_errorloc' class='error'></span>
				</div>


				<div div class="col-xs-12 text-center">
					<br/>
    					<input  type='button' name='Create' value='Update' onclick='getCellData()' /><br/>
					<input type='submit' name='Submit' value='Maintenance Menu' />
					<input type='submit' name='Logout' value='Logout' /><br/><br/>

				</div>


			</fieldset>
		</form>
	</div>
</div>

</div>
</div>
<script>


function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
                 			var camount = document.getElementById("camount").value;
			var country = document.getElementById("country").value;
  

			if(camount === ""){
                    
						alert("Amount Cannot Be Empty");
                    
						isEmpty = true;
                
			}
			else if(country === ""){
                    
						alert("Country Cannot Be Empty");
                    
						isEmpty = true;

                	}


			return isEmpty;
            
		}
            
            
 


function getCellData(){

                 			var camount = document.getElementById("camount").value;
			var country = document.getElementById("country").value;

			if(!checkEmptyInput()){

					$.ajax({
            					type: "POST",
            					url: "update_year_contributing_rate.php",
            				data: 'contributing_amount='+camount+'&contributing_country='+country,
           	 				cache: false,
						async: false,
            					success: function(data) {  
                						message = data;
								window.location.href="confirm_main_maintenance_messages.php";
            								}
        				});

					}
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
