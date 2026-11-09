<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

$tariff_code_session = $_SESSION["tariff_code"];
$tariff_description_session = $_SESSION["tariff_description"];
$tariff_price_session = $_SESSION["tariff_price"];
$tariff_category_session = $_SESSION["tariff_category"];
$tariff_type_session = $_SESSION["tariff_type"];


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
					$fgmembersite->RedirectToURL("main_livvu_system_maintenance.php");
				}else{
					$fgmembersite->RedirectToURL("main_admin_system_maintenance.php");
				}
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
				<legend>Update Ancillary Tariff Form</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='tcode' >Tariff Code*:</label>
    					<input class="col-xs-9 text-center" type='text' name='tcode' id='tcode' value='<?php echo $tariff_code_session; ?>' maxlength="30" /><br/>
    					<span id='process_tcode_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='tdescription' >Tariff Description*:</label>
    					<input class="col-xs-9 text-center" type='text' name='tdescription' id='tdescription' value='<?php echo $tariff_description_session; ?>' maxlength="30" /><br/>
    					<span id='process_tdescription_errorloc' class='error'></span>
				</div>
				

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='tprice' >Tariff Price*:</label>
    					<input class="col-xs-9 text-center" type='text' name='tprice' id='tprice' value='<?php echo $tariff_price_session; ?>' maxlength="30" /><br/>
    					<span id='process_tprice_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='tcategory' >Tariff Category*:</label>
    					<input class="col-xs-9 text-center" type='text' name='tcategory' id='tcategory' value='<?php echo $tariff_category_session; ?>' maxlength="30" /><br/>
    					<span id='process_tcategory_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='ttype' >Tariff Type*:</label>
    					<input class="col-xs-9 text-center" type='text' name='ttype' id='ttype' value='<?php echo $tariff_type_session; ?>' maxlength="30" /><br/>
    					<span id='process_ttype_errorloc' class='error'></span>
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
			var tcode = document.getElementById("tcode").value;
  
                 	var tdescription = document.getElementById("tdescription").value;
  
			var tcategory = document.getElementById("tcategory").value;
  
			var tprice = document.getElementById("tprice").value;

			var ttype = document.getElementById("ttype").value;

			if(tcode === ""){
                    
						alert("Tariff Code Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(tdescription === ""){
                    
						alert("Tariff Description Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(tcategory === ""){
                    
						alert("Tariff Category Cannot Be Empty");
                    
						isEmpty = true;

                	}
else if(tprice === ""){
                    
						alert("Tariff Price Cannot Be Empty");
                    
						isEmpty = true;
			}
else if(ttype === ""){
                    
						alert("Tariff Type Cannot Be Empty");
                    
						isEmpty = true; 
			}
			return isEmpty;
            
		}
            
            
 


function getCellData(){

			var tcode = document.getElementById("tcode").value;
  
                 	var tdescription = document.getElementById("tdescription").value;
 
			var tcategory = document.getElementById("tcategory").value;
			var tprice = document.getElementById("tprice").value;
			var ttype = document.getElementById("ttype").value;

			if(!checkEmptyInput()){

					$.ajax({
            					type: "POST",
            					url: "update_ancillary_tariff.php",
            				data: 'tariff_code='+tcode+'&tariff_description='+tdescription+'&tariff_category='+tcategory+'&tariff_price='+tprice+'&tariff_type='+ttype,
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
