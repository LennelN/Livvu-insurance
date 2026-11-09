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
				$fgmembersite->RedirectToURL("main_merchant_users_admin.php");
			}
	}else{
		
		$fgmembersite->RedirectToURL("logout.php");
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

			<form id='process' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset >
				<legend>Record Patient Medical Details Form</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
			
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='phone_number' >Enter Phone Number*:</label>
    					<input class="col-xs-9 text-center" type='text' name='phone_number' id='phone_number' value='<?php echo $fgmembersite->SafeDisplay('phone_number') ?>' maxlength="30" /><br/>
    					<span id='process_phone_number_errorloc' class='error'></span>
				</div>


				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='temp' >Enter Temperature Recorded*:</label>
    					<input class="col-xs-9 text-center" type='text' name='temp' id='temp' value='<?php echo $fgmembersite->SafeDisplay('temp') ?>' maxlength="30" /><br/>
    					<span id='process_temp_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='bplevel' >Enter Blood Pressure Level*:</label>
    					<input class="col-xs-9 text-center" type='text' name='bplevel' id='bplevel' value='<?php echo $fgmembersite->SafeDisplay('bplevel') ?>' maxlength="30" /><br/>
    					<span id='process_bplevel_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='pulse' >Enter Body Pulse*:</label>
    					<input class="col-xs-9 text-center" type='text' name='pulse' id='pulse' value='<?php echo $fgmembersite->SafeDisplay('pulse') ?>' maxlength="30" /><br/>
    					<span id='process_pulse_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='allergies' >Enter Allergies Known*:</label>
    					<input class="col-xs-9 text-center" type='text' name='allergies' id='allergies' value='<?php echo $fgmembersite->SafeDisplay('allergies') ?>' maxlength="30" /><br/>
    					<span id='process_allergies_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='bloodtype' >Enter Blood Type*:</label>
    					<input class="col-xs-9 text-center" type='text' name='bloodtype' id='bloodtype' value='<?php echo $fgmembersite->SafeDisplay('bloodtype') ?>' maxlength="30" /><br/>
    					<span id='process_bloodtype_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12  text-center">
    
					<label class="col-xs-3 text-center" style="text-align: right;" for='txt_msg' >Other Details*:</label>
					
					<textarea class="col-xs-9 text-center" id="txt_msg" name="txt_msg" rows="4" placeholder="Enter Other Details"></textarea>
                        
				<span id='process_txt_msg_errorloc' class='error'></span>
                    									
					         
			</div>

         


				<div class="col-xs-12 text-center">
					<br/>

					<input  type='button' name='Create' value='Record Details' onclick='getCellData()' /><br/>
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
			                
			var temp = document.getElementById("temp").value;
  
                 	var bplevel = document.getElementById("bplevel").value;
  
			var pulse = document.getElementById("pulse").value;
  
			var phone_number = document.getElementById("phone_number").value;
			
			if(phone_number === ""){
                    
						alert("Phone Number Cannot Be Empty");
                    
						isEmpty = true;

                	}else if(temp === ""){
                    
						alert("Temperature Recorder Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(bplevel === ""){
                    
						alert("Blood Pressure Level Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(pulse === ""){
                    
						alert("Body Pulse Cannot Be Empty");
                    
						isEmpty = true;

                	} 

			return isEmpty;
            
		}
            
            
 


function getCellData(){


			var temp = document.getElementById("temp").value;
  
                 	var bplevel = document.getElementById("bplevel").value;
  
			var pulse = document.getElementById("pulse").value;
			var allergies = document.getElementById("allergies").value;
			var bloodtype = document.getElementById("bloodtype").value;
			var other_details = document.getElementById("txt_msg").value;
			var phone_number = document.getElementById("phone_number").value;
			if(!checkEmptyInput()){
					$.ajax({
            					type: "POST",
            					url: "create_patient_medical_details.php",
            				data: 'body_temperature='+temp+'&bp_level='+bplevel+'&body_pulse='+pulse+'&known_allergies='+allergies+'&blood_type='+bloodtype+'&other_details='+other_details+'&phone_number='+phone_number,
           	 				cache: false,
						async: false,
            					success: function(data) {  
                						message = data;
								window.location.href="confirm_create_bill.php";
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
