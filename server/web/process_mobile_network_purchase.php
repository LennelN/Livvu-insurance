<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");

$purchase_transaction_no = $_SESSION["purchase_transaction_no"];
$header_message = "Purchase Transaction No: ".$purchase_transaction_no;

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
			if($main_menu=="main_admin"){
				$fgmembersite->RedirectToURL("main_admin.php");
			}else{
				$fgmembersite->RedirectToURL("main_admin_accounts.php");
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
				<legend>Process Cashed Out Points Form</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
					<label style="text-align: center;"><?php echo $header_message; ?></label><br/><br/>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='transno' >Enter Transaction Number*:</label>
    					<input class="col-xs-9 text-center" type='text' name='transno' id='transno' value='<?php echo $fgmembersite->SafeDisplay('transno') ?>' maxlength="30" /><br/>
    					<span id='process_cashed_transno_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='transtype' >Enter Transaction Type*:</label>
    					<input class="col-xs-9 text-center" type='text' name='transtype' id='transtype' value='<?php echo $fgmembersite->SafeDisplay('transtype') ?>' maxlength="30" /><br/>
    					<span id='process_cashed_transtype_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='transamount' >Enter Transaction Amount*:</label>
    					<input class="col-xs-9 text-center" type='text' name='transamount' id='transamount' value='<?php echo $fgmembersite->SafeDisplay('transamount') ?>' maxlength="30" /><br/>
    					<span id='process_cashed_transamount_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='transdate' >Enter Transaction Date*:</label>
    					<input class="col-xs-9 text-center" type='date' name='transdate' id='transdate' value='<?php echo $fgmembersite->SafeDisplay('transdate') ?>' max='<?php echo date('Y-m-d');?>' /><br/>
    					<span id='process_cashed_transdate_errorloc' class='error'></span>
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
			                
			var transno = document.getElementById("transno").value;
  
                 	var transtype = document.getElementById("transtype").value;
  
			var transamount = document.getElementById("transamount").value;
  
			var transdate = document.getElementById("transdate").value;


			if(transno === ""){
                    
						alert("Transaction Number Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(transtype === ""){
                    
						alert("Transaction Type Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(transamount === ""){
                    
						alert("Transaction Amount Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(transdate === ""){
                    
						alert("Transaction Date Cannot Be Empty");
                    
						isEmpty = true;
  
			}

			return isEmpty;
            
		}
            
            
 


function getCellData(){


			var transno = document.getElementById("transno").value;
  
                 	var transtype = document.getElementById("transtype").value;
  
			var transamount = document.getElementById("transamount").value;
			var transdate = document.getElementById("transdate").value;

			if(!checkEmptyInput()){
					$.ajax({
            					type: "POST",
            					url: "update_mobile_purchases.php",
            				data: 'transaction_no='+transno+'&transaction_type='+transtype+'&transaction_amount='+transamount+'&transaction_date='+transdate,
           	 				cache: false,
						async: false,
            					success: function(data) {  
                						message = data;
								window.location.href="confirm_main_admin_messages.php";
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
