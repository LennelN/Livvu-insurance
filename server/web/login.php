<?PHP
session_start();
require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";	

if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	$submitregister= isset($_POST['Register']);
	//$logout
	if($submitbutton)
	{

   		if($fgmembersite->Login())
   		{
			if(!isLoginSessionExpired()) 
			{
        				if($_SESSION["type_of_client"] == "1")
				{
					$_SESSION["main_menu"] = "main";
					if($_SESSION["message_create"]=="Reset Password")
					{
        						$fgmembersite->RedirectToURL("reset_password_form.php");
					}else{
						$fgmembersite->RedirectToURL("get_individual_client_otp.php");
					}
				}else{

					if($_SESSION["type_of_client"] == "3")
					{
					
						if($_SESSION["type_of_registration"] == "Complete Registration") 
						{
							$fgmembersite->RedirectToURL("complete_system_user_registration.php");
						}else{

							if($_SESSION["message_create"]=="Reset Password")
							{
        							$fgmembersite->RedirectToURL("reset_password_form.php");
							}else{
								if($_SESSION["user_role"] == "Accounts"){
									$_SESSION["main_menu"] = "main_admin_accounts";
									$fgmembersite->RedirectToURL("main_admin_accounts.php");
								}else if($_SESSION["user_role"] == "Admin"){
									$_SESSION["main_menu"] = "main_admin";
									$fgmembersite->RedirectToURL("main_admin.php");
								}else{
								
									$_SESSION["main_menu"] = "main_livvu";
									$fgmembersite->RedirectToURL("main_livvu.php");
								}
							}
						}
					}else{
						if($_SESSION["type_of_client"] == "2")
						{
							if($_SESSION["message_create"]=="Reset Password")
							{
        							$fgmembersite->RedirectToURL("reset_password_form.php");
							}else{
								if($_SESSION["type_of_registration"] == "Health") 
								{
									$_SESSION["main_menu"] = "main_merchant";
									$fgmembersite->RedirectToURL("main_merchant.php");
								}else{
									$_SESSION["main_menu"] = "main_society";
									$fgmembersite->RedirectToURL("main_society.php");
								}
							}
						}else{
							if($_SESSION["type_of_registration"] == "Complete Registration") 
							{
								$fgmembersite->RedirectToURL("complete_user_registration.php");
							}else{
								if($_SESSION["message_create"]=="Reset Password")
								{
        								$fgmembersite->RedirectToURL("reset_password_form.php");
								}else{
									if($_SESSION["user_role"] == "Accounts")
									{
										$_SESSION["main_menu"] = "main_merchant_users_accounts";
										$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");
									}else{
										$_SESSION["main_menu"] = "main_merchant_users_admin";
										$fgmembersite->RedirectToURL("main_merchant_users_admin.php");
									}
								}

							}
						}
					}
				}
			}else{
				$fgmembersite->RedirectToURL("logout.php");
			}
   		}else{
			$fgmembersite->RedirectToURL("confirm_login_messages.php");
		}

	}else{
		$fgmembersite->RedirectToURL("type_of_registration.php");
	
	}

}

if(isset($_GET["session_expired"])) 
{
	$message = "Login Session is Expired. Please Login Again";
}

if(isset($_GET["session_logged_out"])) 
{
	$message = "Session Was Logged Out By Another Attempt To LogIn. Please Login Again";
	// header("Location:login.php?session_logged_out=1");
}

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">
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
		<div><?php include("./include/declaration.php");?></div>

<!-- Form Code Start -->
	<div class="row">
		<div class="col-xs-12 text-center">    

			<form id='login' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>


			<fieldset >
				<legend>Login</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div class='short_explanation'>* required fields</div>
<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				
				<div div class="col-xs-12 text-center">
    					<label for='username' >Username*:</label>
    					<input type='text' name='username' id='username' value='<?php echo $fgmembersite->SafeDisplay('username') ?>' maxlength="20" /><br/>
    					<span id='login_username_errorloc' class='error'></span>
				</div>
<div div class="col-xs-12 text-center">
    <label for='password' >Password*:</label>
    <input type='password' name='password' id='password' maxlength="4" /><br/>
    <span id='login_password_errorloc' class='error'></span>
</div>
<div class="col-xs-12 text-center">
					<label  for='nameofcountry' >Country*:</label>
					
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

    					<span id='login_nameofcountry_errorloc' class='error'></span>
				
				</div>

<div div class="col-xs-12 text-center">
    	<input type='submit' name='Submit' value='Submit' />
	<input type='submit' name='Register' value='Register' />
<br/>
<br/>
<div div class="col-xs-12 text-center">
<div><label><a style="font-size: 18px;font-style: bold;color: black;" href='process_submit_phone_reset_password.php'>Forgot Password</a></label></div>
</div>
</div>

<hr>



</fieldset>
</form>
</div>
</div>
<!-- client-side Form Validations:
Uses the excellent form validation script from JavaScript-coder.com-->

<script type='text/javascript'>
// <![CDATA[

    var frmvalidator  = new Validator("login");
    frmvalidator.EnableOnPageErrorDisplay();
    frmvalidator.EnableMsgsTogether();

    frmvalidator.addValidation("username","req","Please provide your username");
    
    frmvalidator.addValidation("password","req","Please provide the password");

frmvalidator.addValidation("nameofcountry","req","Please select country");


// ]]>
</script>

<!--
Form Code End (see html-form-guide.com for more info.)
-->
<div>   <?php include("./include/footer.php");?>
</div> 



</div>
</div>	
</body>
</html>