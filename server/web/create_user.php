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
				<legend>User Registration Form</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='fullName' >Enter Full Name*:</label>
    					<input class="col-xs-8 text-center" type='text' name='fullName' id='fullName' value='<?php echo $fgmembersite->SafeDisplay('fullName') ?>' maxlength="30" /><br/>
    					<span id='create_user_fullname_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='address' >Enter Physical Address*:</label>
    					<input class="col-xs-8 text-center" type='text' name='address' id='address' value='<?php echo $fgmembersite->SafeDisplay('address') ?>' maxlength="30" /><br/>
    					<span id='create_user_address_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='emailAddress' >Enter Email Address*:</label>
    					<input class="col-xs-8 text-center" type='text' name='emailAddress' id='emailAddress' value='<?php echo $fgmembersite->SafeDisplay('emailAddress') ?>' maxlength="30" /><br/>
    					<span id='create_user_emailaddress_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='idno' >Enter ID Number*:</label>
    					<input class="col-xs-8 text-center" type='text' name='idno' id='idno' value='<?php echo $fgmembersite->SafeDisplay('idno') ?>' maxlength="11" /><br/>
    					<span id='create_user_idno_errorloc' class='error'></span>
				</div>
				
				<div class="col-xs-12 text-center">
					<div class="col-xs-4 text-center" >
						<label style="text-align: center;" for='role' >Select User Role*:</label>
						<span id='create_user_label_role_errorloc' class='error'></span>
					</div>
					<div style="text-align: left;">
    					
						<?php include "./include/config.php"; // Database connection using PDO
					
					 	$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
						$sql="Select type_of_role from tbl_user_roles"; 
						echo "<select id=role name=role>";
						echo "<option value=''>User Role</option>"; // list box select command					
						foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["type_of_role"].'">'.$row["type_of_role"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
						echo "</select>";
						?>
    						<span id='create_user_role_errorloc' class='error'></span>
					</div>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='phone' >Enter Phone Number*:</label>
    					<input class="col-xs-8 text-center" type='text' name='phone' id='phone' value='<?php echo $fgmembersite->SafeDisplay('phone') ?>' maxlength="11" /><br/><br/>
    					<span id='create_user_phone_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='password' >Password*:</label>
    					<input class="col-xs-8 text-center" type='text' name='password' id='password' value='1234' maxlength="10" /><br/>
    					<span id='create_user_password_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='cpassword' >Confirm Password*:</label>
    					<input class="col-xs-8 text-center" type='text' name='cpassword' id='cpassword' value='1234' maxlength="10" /><br/>
    					<span id='create_user_cpassword_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='userBranch' >Enter User Branch*:</label>
    					<input class="col-xs-8 text-center" type='text' name='userBranch' id='userBranch' value='<?php echo $fgmembersite->SafeDisplay('userBranch') ?>' maxlength="20" /><br/>
    					<span id='create_user_branch_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-4 text-center" style="text-align: center;" for='country' >Enter User Country*:</label>
    					<input class="col-xs-8 text-center" type='text' name='country' id='country' value='<?php echo $fgmembersite->SafeDisplay('country') ?>' maxlength="20" /><br/>
    					<span id='create_user_country_errorloc' class='error'></span>
				</div>





				<div div class="col-xs-12 text-center">
					<br/>
    					<input  type='button' name='Create' value='Create User' onclick='getCellData()' /><br/>
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
			var phone = document.getElementById("phone").value;
  
                 	var fullName = document.getElementById("fullName").value;
 
			var address = document.getElementById("address").value;
  
			var email = document.getElementById("emailAddress").value;

			var idno = document.getElementById("idno").value;
			var role = document.getElementById("role").value;
			var passwd= document.getElementById("password").value;
			var cpasswd= document.getElementById("cpassword").value;
			var branch= document.getElementById("userBranch").value;
			var country= document.getElementById("country").value;
		
			
			if(fullName === ""){
                    
						alert("Name Of User Cannot Be Empty");
                    
						isEmpty = true;
    
			}
else if(address === ""){
                    
						alert("User Physical Address Cannot Be Empty");
                    
						isEmpty = true;
                            
			}else if(email === ""){
                    
						alert("Email Addresss Cannot Be Empty");
                    
						isEmpty = true;
  
			}else if(idno === ""){
                    
						alert("ID Number Cannot Be Empty");
                    
						isEmpty = true;
                 
			}else if(role === ""){
                    
						alert("User Role Cannot Be Empty");
                    
						isEmpty = true;
 
			}
else if(phone === ""){
                    
						alert("Phone Number Cannot Be Empty");
                    
						isEmpty = true;
   
			}else if( passwd==="" ){
                    
						alert("Password Cannot Be Greater Than Today");
                    
						isEmpty = true;
               
			}else if(branch === ""){
                    
						alert("User Branch Cannot Be Empty");
                    
						isEmpty = true;
                 
			}else if(country === ""){
                    
						alert("Country Of User Cannot Be Empty");
                    
						isEmpty = true;
 
			
			}

			return isEmpty;
            
		}
            
            
 


function getCellData(){
			
			var phone = document.getElementById("phone").value;
  
                 	var fullName = document.getElementById("fullName").value;
  
			var address = document.getElementById("address").value;
  
			var email = document.getElementById("emailAddress").value;

			var idno = document.getElementById("idno").value;
			var role = document.getElementById("role").value;
			var passwd= document.getElementById("password").value;
			var cpasswd= document.getElementById("cpassword").value;
			var branch= document.getElementById("userBranch").value;
			var country= document.getElementById("country").value;
			if(!checkEmptyInput()){
					$.ajax({
            					type: "POST",
            					url: "register_merchant_user_profile.php",
            				data: 'name='+fullName+'&email='+email+'&user_role='+role+'&phone_no='+phone+'&idno='+idno+'&password='+passwd+'&branch='+branch+'&country='+country+'&address='+address,
           	 				cache: false,
						async: false,
            					success: function(data) {  
                						message = data;
								window.location.href="confirm_user_registration.php";
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
