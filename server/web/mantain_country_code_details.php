<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}


if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	//$logout
	if($submitbutton){
			$main_menu = $_SESSION["main_menu"];
			
			if($main_menu=="main_merchant")
			{
				$fgmembersite->RedirectToURL("main_merchant.php");
			}else{
				if($main_menu=="main_merchant_users_accounts")
				{
					$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");
				}else if($main_menu=="main_livvu"){
					$fgmembersite->RedirectToURL("main_livvu.php");
				}else{
					$fgmembersite->RedirectToURL("main_admin.php");
				}
			}
	}else{
		$fgmembersite->RedirectToURL("logout.php");
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
				<legend>Country Details Form</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='countryName' >Enter Country Name*:</label>
    					<input class="col-xs-9 text-center" type='text' name='countryName' id='countryName' value='<?php echo $fgmembersite->SafeDisplay('countryName') ?>'  /><br/>
    					<span id='countryName_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='countryCode' >Enter Country Code*:</label>
    					<input class="col-xs-9 text-center" type='text' name='countryCode' id='countryCode' value='<?php echo $fgmembersite->SafeDisplay('countryCode') ?>' maxlength="30" /><br/>
    					<span id='countryCode_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='countryDigits' >Enter Number Of Phone Digits*:</label>
    					<input class="col-xs-9 text-center" type='text' name='countryDigits' id='countryDigits' value='<?php echo $fgmembersite->SafeDisplay('countryDigits') ?>' maxlength="30" /><br/>
    					<span id='countryDigits_errorloc' class='error'></span>
				</div>

<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='countryRegion' >Enter Country Region*:</label>
    					<input class="col-xs-9 text-center" type='text' name='countryRegion' id='countryRegion' value='<?php echo $fgmembersite->SafeDisplay('countryRegion') ?>'  /><br/>
    					<span id='countryRegion_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='countryContinent' >Enter Country Continent*:</label>
    					<input class="col-xs-9 text-center" type='text' name='countryContinent' id='countryContinent' value='<?php echo $fgmembersite->SafeDisplay('countryContinent') ?>' maxlength="30" /><br/>
    					<span id='countryContinent_errorloc' class='error'></span>
				</div>
		

			
			

				<div div class="col-xs-12 text-center">
					<br/>
    					<input  type='button' name='Create' value='Process' onclick='getCellData()' /><br/>
					<input type='submit' name='Submit' value='Main Menu' />
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
			                
			var countryName = document.getElementById("countryName").value;
  
                 	var countryCode = document.getElementById("countryCode").value;
  
			var countryDigits = document.getElementById("countryDigits").value;

			var countryRegion = document.getElementById("countryRegion").value;
  
                 	var countryContinent = document.getElementById("countryContinent").value;
  


			if(countryName === ""){
                    
						alert("Country Name Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(countryCode === ""){
                    
						alert("Country Code Cannot Be Empty");
                    
						isEmpty = true;
                             
			}else if(countryDigits === ""){
                    
						alert("No Of Phone Digits Cannot Be Empty");
                    
						isEmpty = true;
  
			}else if(countryRegion === ""){
                    
						alert("Country Region Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(countryContinent === ""){
                    
						alert("Country Continent Cannot Be Empty");
                    
						isEmpty = true;
 
			}

			return isEmpty;
            
		}
            
            
 


function getCellData(){


			var countryName = document.getElementById("countryName").value;
  
                 	var countryCode = document.getElementById("countryCode").value;
  
			var countryDigits = document.getElementById("countryDigits").value;

			var countryRegion = document.getElementById("countryRegion").value;
  
                 	var countryContinent = document.getElementById("countryContinent").value;
  


			if(!checkEmptyInput()){
					$.ajax({
            					type: "POST",
            					url: "insert_country_details.php",
            				data: 'country_name='+countryName+'&country_code='+countryCode+'&no_phone_digits='+countryDigits+'&country_region='+countryRegion+'&country_continent='+countryContinent,
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
