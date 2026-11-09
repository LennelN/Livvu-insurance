<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");


if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Logout']);
	//$logout
	if($submitbutton)
	{
		$fgmembersite->RedirectToURL("login.php");
	}

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
			
			<fieldset >
				<legend>Submit Details For Password Reset Form</legend>

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='user_login_id' >Enter Username*:</label>
    					<input class="col-xs-9 text-center" type='text' name='user_login_id' id='user_login_id' value='<?php echo $fgmembersite->SafeDisplay('user_login_id') ?>' maxlength="30" /><br/>
    					<span id='process_system_user_login_id_errorloc' class='error'></span>
				</div>
			
<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='nameofcountry' >Select Country*:</label>
					
    					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$sql="Select country_name from tbl_country_codes"; 
					echo "<select class=col-xs-9 id=nameofcountry name=nameofcountry>";
					echo "<option value=''>Select Country</option>"; // list box select command					
					 //echo '<option selected="selected" value="'.$row["public_desc"].'">'.$row["public_desc"].'</option>';
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["country_name"].'">'.$row["country_name"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
        			?><br/>

    					<span id='process_system_nameofcountry_errorloc' class='error'></span>
				
				</div>
				<div  class="col-xs-12 text-center">
					<br/>
    					<input  type='button' name='Create' value='Submit' onclick='getCellData()' />
					<input type='submit' name='Logout' value='Cancel' /><br/><br/>

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
			                
			var user_login_id = document.getElementById("user_login_id").value;
  var nameofcountry = document.getElementById("nameofcountry").value;
		

			if(user_login_id === ""){
                    
						alert("UserName Number Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(nameofcountry === ""){

				alert("Please Select Country");
                    
						isEmpty = true;

			}

			return isEmpty;
            
		}
            
            
 


function getCellData(){

			var user_login_id = document.getElementById("user_login_id").value;

 			var nameofcountry = document.getElementById("nameofcountry").value;

			if(!checkEmptyInput()){
					$.ajax({
            					type: "POST",
            					url: "enter_password_reset_phone_number.php",
            					data: 'user_login_id_to_update='+user_login_id+'&user_login_country='+nameofcountry,
           	 				cache: false,
						async: false,
            					success: function(data) {  
                						message = data;
								window.location.href="confirm_login_messages.php";
            								}
        				});

					}
		}


</script>

</body>
</html>
