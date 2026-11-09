<?php
session_start();
require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

if(!$fgmembersite->CheckLogin())
{
//error_log(strip_tags("Error logging in. missing fields"), 0);
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
				}else{
					$fgmembersite->RedirectToURL("main.php");
				}
			}


	}else{
		$fgmembersite->RedirectToURL("logout.php");
	}

	if(!isLoginSessionExpired()) 
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
					$fgmembersite->RedirectToURL("main.php");
				}
			}
		}else{
			header("Location:logout.php?session_expired=1");
		}

  		
	
   }



if(isset($_SESSION["session_id"])) {
	//error_log(strip_tags("Error logging in. missing fields1"), 0);
	$current_time = time(); 
	if(isLoginSessionExpired()) {
//error_log(strip_tags("Error logging in. missing fields2"), 0);
		$message = "Login Session is Expired. Please Login Again";
		header("Location:logout.php?session_expired=1");
	}
	$_SESSION['loggedin_time'] = time();
}else{
//error_log(strip_tags("Error logging in. missing fields3"), 0);
header("Location:logout.php?session_new_created=1");
}

if(isset($_GET["session_expired"])) {
error_log(strip_tags("Error logging in. missing fields4"), 0);
	$message = "Login Session is Expired. Please Login Again";
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="refresh" content="990">
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
<?php
include "./include/config.php";

$message="";

	$main_menu = $_SESSION["main_menu"];
error_log(strip_tags("Error logging in. missing fields main m ".$main_menu), 0);
	if($main_menu=="main")
	{
   		if($fgmembersite->CheckBalance())
   		{
	//error_log(strip_tags("Error logging in. missing fields6b"), 0);
			if(!isLoginSessionExpired()) {
				$message=$_SESSION["message"];

			}else{
				header("Location:logout.php?session_expired=1");
			}
	//}
  		 }else{
			//error_log(strip_tags("Error logging in. missing fields7"), 0);
			$fgmembersite->RedirectToURL("700.html");
		}
	}else{

		if($fgmembersite->CheckBalanceMerchant())
   		{
	error_log(strip_tags("Error logging in. missing fields6b"), 0);
			if(!isLoginSessionExpired()) {
				$message=$_SESSION["message"];

			}else{
				header("Location:logout.php?session_expired=1");
			}
	//}
  		 }else{
			error_log(strip_tags("Error logging in. missing fields7"), 0);
			$fgmembersite->RedirectToURL("700.html");
		}

	}


if(isset($_GET["session_expired"])) {
	$message = "Login Session is Expired. Please Login Again";
}


?>



<div id="main-wrap">
	<div id="page-wrap">
	<div>
		<?php include("./include/declaration.php");?>
	</div>	
<div class="row">
		<div class="col-xs-12 text-center">    

			<form id='login' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset >
				<legend>Account Balance:</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				<div class="col-xs-12 text-center">   				
    					<legend><span><?php echo $message ?></span></legend>
					<br/>
				</div>


				<div div class="col-xs-12 text-center">
					<br/>
    					<input type='submit' name='Submit' value='Main Menu' />
					<input type='submit' name='Logout' value='Logout' /><br/><br/>
				</div>


			</fieldset>
		</form>
	</div>
</div>

<div>   <?php include("./include/footer.php");?>
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

</body>
</html>
