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
	if($submitbutton)
	{
		$main_menu = $_SESSION["main_menu"];
			
			
		if($main_menu=="main_merchant")
			{
				$fgmembersite->RedirectToURL("main_merchant.php");
			}else{
				if($main_menu=="main_merchant_users_accounts")
				{
					$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");
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
	<script src='scripts/jquery.json-2.4.min.js'></script>  
<link rel="stylesheet" href="css/bootstrap-select.css"> 
<style>


.bootstrap-select {
  width: 220px \0;
  /*IE9 and below*/
  vertical-align: middle;
}

.bootstrap-select:not([class*="col-"]):not([class*="form-control"]):not(.input-group-btn) {
  width: 220px;
}


</style>
<script>
$(document).ready(function(){
$("select#phoneno").change(function(){
     var sel = document.getElementById('phoneno');
	
var opt = sel.options[sel.selectedIndex];
var phoneno1 = opt.text;

    var phoneno =  $("select#phoneno option:selected").attr('value'); 
   
//alert(nameofbank1);

    $("#doctorname").html( "" );
    if (phoneno.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "getdoctorname.php",
            data: "phoneno="+phoneno1,
            cache: false,
            success: function(response) {    
                var jsonData = JSON.parse(response);

												if (jsonData.response == true) 
												{

											
													for(var i=0; i < jsonData.ddd.length; i++)
													{
					
														var cnt = jsonData.ddd[i];
																																	$("#doctorname").val(cnt.full_name);
														

										
													}
				
												}	
            }
        });
    } 
});
});
</script>

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
				<legend>Activate Doctor Form</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='phoneno' >Select Doctor Phone Number*:</label>
					
    					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$merchant_country = $_SESSION["merchant_country"];
					$merchant_phone_number = $_SESSION["merchant_phone_number"];
					$merchant_code = $_SESSION["merchant_code"];
					
					$sql="Select phone_number from tbl_doctor_details WHERE merchant_phone_number = '$merchant_phone_number' AND merchant_code = '$merchant_code' AND doctor_active = 'N' ORDER BY 1"; 
					echo "<select  id=phoneno name=phoneno class='selectpicker' data-live-search='true' >";
					echo "<option value=''>Select Doctor Phone Number</option>"; // list box select command					
					 //echo '<option selected="selected" value="'.$row["public_desc"].'">'.$row["public_desc"].'</option>';
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["phone_number"].'">'.$row["phone_number"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
        					?><br/><br/>
    					<span id='process_system_transtype_errorloc' class='error'></span>
				</div>

				
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='doctorname' >Doctor Name*:</label>
						<fieldset disabled>
    							<input type='text' name='doctorname' id='doctorname'  maxlength="30" /><br/><br/>
    							<span id='create_bill_record_doctorname_errorloc' class='error'></span>

						</fieldset>
    					<span id='register_doctorname_errorloc' class='error'></span>
				</div>
			
			

				<div div class="col-xs-12 text-center">
					<br/>
    					<input  type='button' name='Create' value='Process' onclick='getCellData()' />
					<input type='submit' name='Submit' value='Main Menu' /><br/><br/>

				</div>


			</fieldset>
		</form>
	</div>
</div>

</div>
</div>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-select.js"></script>
<script>


function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
			                
			var phoneno = document.getElementById("phoneno").value;
  
			

			if(phoneno === ""){
                    
						alert("Please Select Doctor Phone Number. It Cannot Be Empty");
                    
						isEmpty = true;
                
			}

			return isEmpty;
            
		}
            
            
 


function getCellData(){


			var phoneno = document.getElementById("phoneno").value;
			var doctorname = document.getElementById("doctorname").value;
 

			if(!checkEmptyInput()){
					$.ajax({
            					type: "POST",
            					url: "activate_merchant_doctor_profile.php",
            				data: 'doctor_deactivated_phone_no='+phoneno+'&doctor_name='+doctorname,
           	 				cache: false,
						async: false,
            					success: function(data) {  
                						message = data;
								window.location.href="confirm_main_merchant_doctor_messages.php";
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
