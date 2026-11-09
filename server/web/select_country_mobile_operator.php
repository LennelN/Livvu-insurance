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
		$country  = trim($_POST['country']);
		$mobile  = trim($_POST['mobile']);
		if(strlen($country) > 0 && strlen($mobile) > 0)
		{
			$_SESSION["selected_country"] = $country;
			$_SESSION["selected_mobile"] = $mobile;
			$fgmembersite->RedirectToURL("get_mobile_network_purchases.php");
			
			
		}else{
			$fgmembersite->HandleError("Enter Valid Country And Mobile Operator");
		}
	}else{

		$main_menu = $_SESSION["main_menu"];
			if($main_menu=="main_admin"){
				$fgmembersite->RedirectToURL("main_admin.php");
			}else{
				$fgmembersite->RedirectToURL("main_admin_accounts.php");
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
<script src="js/jquery.min.js"></script>
  	<script src="js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
$("select#country").change(function(){
     var sel = document.getElementById('country');
	
var opt = sel.options[sel.selectedIndex];
var nameofcountry1 = opt.text;

    var nameofcountry =  $("select#country option:selected").attr('value'); 
  

    $("#mobile").html( "" );
    if (nameofcountry.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "getmobile.php",
            data: "country_name="+nameofcountry1,
            cache: false,
            beforeSend: function () { 
                $('#mobile').html('<img src="loader.gif" alt="" width="24" height="24">');
            },
            success: function(html) {    
                $("#mobile").html( html );
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

			<form id='phone' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset >
				<legend>Please Select Country And Mobile Operator</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">

					<div class="col-xs-6 text-center" >
						<label style="text-align: center;" for='role' >Select Country*:</label>
						<span id='process_country_label_role_errorloc' class='error'></span>
					</div>

					<div class="col-xs-6 text-left" style="text-align: left;">
    						<?php include "./include/config.php"; // Database connection using PDO
					
					 		$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
							$sql="Select country_name from tbl_country_codes"; 
							echo "<select id=country name=country>";
							echo "<option value=''>Select Country</option>"; // list box select command					
							foreach ($dbh->query($sql) as $row){//Array or records stored in $row
								$country_name =$row["country_name"]; 
								echo '<option value="'.$country_name.'">'.$country_name.'</option>';
							/* Option values are added by looping through the array */ 
							}
							echo "</select>";
							
						?>
						<span id='select_country_mobile_operator_errorloc' class='error'></span>
					</div>
				</div>


				<div class="col-xs-12 text-center">

					<div class="col-xs-6 text-center" >
						<label style="text-align: center;" for='role' >Select Mobile Operator*:</label>
						<span id='process_mobile_label_role_errorloc' class='error'></span>
					</div>
					<div class="col-xs-6 text-left" style="text-align: left;">
    						<select  id=mobile name=mobile>Enter Name Of Mobile Operator</select>
					</div>
					<span id='select_mobile_operator_errorloc' class='error'></span>
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
