<?php
    
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	//$logout
	if($submitbutton)
	{
			//if($fgmembersite->RegisterSocietyGroupSession())
   			//{
        			$fgmembersite->RedirectToURL("terms_conditions_society.php");
			//}
	}else{
		
		$fgmembersite->RedirectToURL("login.php");

	}
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
<script src="js/jquery.min.js"></script>
  	<script src="js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
$("select#nameofcountry").change(function(){
     var sel = document.getElementById('nameofcountry');
	
var opt = sel.options[sel.selectedIndex];
var nameofcountry1 = opt.text;

    var nameofcountry =  $("select#nameofcountry option:selected").attr('value'); 
   
//alert(nameofbank1);

    $("#sgregion").html( "" );
    if (nameofcountry.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "getregion.php",
            data: "nameofcountry="+nameofcountry1,
            cache: false,
            beforeSend: function () { 
                $('#sgregion').html('<img src="loader.gif" alt="" width="24" height="24">');
            },
            success: function(html) {    
                $("#sgregion").html( html );
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
		<div><?php include("./include/declaration.php");?></div>

<!-- Form Code Start -->

	   
<div class="row">
<div class="col-xs-12 text-center">	
			<form id='register' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset >
				<legend>Society/Group Registration Form</legend>

			
				<input type='hidden' name='submitted' id='submitted' value='1'/>

<div class="col-xs-12 text-center" class='short_explanation'>* required fields</div>
<div id='fg_membersite'><input type='text'  class='spmhidip' name='<?php echo $fgmembersite->GetSpamTrapInputName(); ?>' /></div>

<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='sgname' >Society/Group Name*:</label>
    					<input class="col-xs-9 text-center" type='text' name='sgname' id='sgname' value='<?php echo $fgmembersite->SafeDisplay('sgname') ?>' maxlength="50" /><br/>
    					<span id='register_sgname_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='sgno' >Identification No*:</label>
    					<input class="col-xs-9 text-center" type='text' name='sgno' id='sgno' value='<?php echo $fgmembersite->SafeDisplay('sgno') ?>' maxlength="50" /><br/>
    					<span id='register_sgno_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='sgaddress' >Society/Group Address*:</label>
    					<input class="col-xs-9 text-center" type='text' name='sgaddress' id='sgaddress' value='<?php echo $fgmembersite->SafeDisplay('sgaddress') ?>' maxlength="50" /><br/>
    					<span id='register_sgaddress_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='sgdescription' >Society/Group Description*:</label>
    					<input class="col-xs-9 text-center" type='text' name='sgdescription' id='sgdescription' value='<?php echo $fgmembersite->SafeDisplay('sgdescription') ?>' maxlength="50" /><br/>
    					<span id='register_sgdescription_errorloc' class='error'></span>
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

    					<span id='register_nameofcountry_errorloc' class='error'></span>
				
				</div>

				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='sgregion' >Society/Group Region*:</label>
					<select class="col-xs-9 text-center" id=sgregion name=sgregion>Enter Name Of Region</select>
    					<span id='register_sgregion_errorloc' class='error'></span>
				</div>
	
				
			
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='username' >Phone Number*:</label>
    					<input class="col-xs-9 text-center" type='text' name='username' id='username' value='<?php echo $fgmembersite->SafeDisplay('username') ?>' /><br/>
    					<span id='register_username_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='password' >Password(4 Digits)*:</label>
    					<input class="col-xs-9 text-center" type='text' onkeydown="validateNumber(event)" name='password' id='password' maxlength="4" /><br/>
    					<div id='register_password_errorloc' class='error'></div>
				</div>

				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='cpassword' >Confirm Password*:</label>
    					<input class="col-xs-9 text-center" type='text' onkeydown="validateNumber(event)" name='cpassword' id='cpassword' maxlength="4" /><br/>
    					<span id='register_cpassword_errorloc' class='error'></span>
				</div>




<div class="col-xs-12 text-center">
	<div class="col-xs-3 text-center">
	</div>
	<div class="col-xs-9 text-center">
<br/>
    	<input  type='button' name='Submit' value='Submit' onclick='getCellData()'/>
<input  type='submit' name='Cancel' value='Cancel'/>
<br/><br/>
	</div>
</div>

<hr>



</fieldset>
</form>
</div>
</div>


<!--
Form Code End (see html-form-guide.com for more info.)
-->
<div>   <?php include("./include/footer.php");?>
</div> 



</div>
</div>
<script>

function validateNumber(evt) {
    var e = evt || window.event;
    var key = e.keyCode || e.which;

    if (!e.shiftKey && !e.altKey && !e.ctrlKey &&
    // numbers   
    key >= 48 && key <= 57 ||
    // Numeric keypad
    key >= 96 && key <= 105 ||
    // Backspace and Tab and Enter
    key == 8 || key == 9 || key == 13 ||
    // Home and End
    key == 35 || key == 36 ||
    // left and right arrows
    key == 37 || key == 39 ||
    // Del and Ins
    key == 46 || key == 45 ||
     // comma, period and minus, . on keypad
    key == 190 ) {
       // input is VALID
    }
    else {
        // input is INVALID
        e.returnValue = false;
        if (e.preventDefault) e.preventDefault();
    }
}

function getCellData(){
	

	alert("collectio");

	var sgname = document.getElementById("sgname").value;
                  
	var sgno = document.getElementById("sgno").value;
 
	var sgaddress = document.getElementById("sgaddress").value;
                  
	var sgdescription = document.getElementById("sgdescription").value;
             
	var nameofcountry = document.getElementById("nameofcountry").value;
  
	var sgregion = document.getElementById("sgregion").value;
                  
	var username = document.getElementById("username").value;
                  
	var password = document.getElementById("password").value;
  
 	var cpassword = document.getElementById("cpassword").value;
  
alert(username);
	if(!checkEmptyInput()){

		$.ajax({
            		type: "POST",
            		url: "set_register_society_session_variables.php",
            		data: 'sgname='+sgname+'&sgno='+sgno+'&sgaddress='+sgaddress+'&sgdescription='+sgdescription+'&nameofcountry='+nameofcountry+'&sgregion='+sgregion+'&username='+username+'&password='+password+'&cpassword='+cpassword,
           	 	cache: false,
			async: false,
            		success: function(data) {  
                			message = data;
					window.location.href="terms_conditions_society.php";
            			}
        		});
	}
	

}

function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
			var sgname = document.getElementById("sgname").value;
                  
			var sgno = document.getElementById("sgno").value;
 
			var sgaddress = document.getElementById("sgaddress").value;
                  
			var sgdescription = document.getElementById("sgdescription").value;
             
			var nameofcountry = document.getElementById("nameofcountry").value;
  
			var sgregion = document.getElementById("sgregion").value;
                  
			var username = document.getElementById("username").value;
                  
			var password = document.getElementById("password").value;
  
 			var cpassword = document.getElementById("cpassword").value;
  


			if(sgname === ""){
                    
						alert("Please Provide  Name Of Society/Group");
                    
						isEmpty = true;
                
			}
else if(sgno === ""){
                    
						alert("Please Provide Identification Number Of Society/Group");
                    
						isEmpty = true;
                
			}
else if(sgaddress === ""){
                    
						alert("Please Provide Address Of Society/Group");
                    
						isEmpty = true;
                
			}else if(sgdescription === ""){
                    
						alert("Please Provide Description Of Society/Group");
                    
						isEmpty = true;
                
			}else if(nameofcountry === ""){
                    
						alert("Please Provide Country Name Of Society/Group");
                    
						isEmpty = true;
 
			}else if( sgregion === "" ){
                    
						alert("Please Provide Region Of Company");
                    
						isEmpty = true;
               
			}else if( username === "" ){
                    
						alert("Please provide Phone Number Of Society/Group");
                    
						isEmpty = true;
               
			}else if( password === "" ){
                    
						alert("Please Enter A Password");
                    
						isEmpty = true;
               
			}else if( cpassword === "" ){
                    
						alert("Please Enter A Confirmation Password");                   
						isEmpty = true;
               
			}
			return isEmpty;
            
		}
            
            


</script>
	
</body>
</html>