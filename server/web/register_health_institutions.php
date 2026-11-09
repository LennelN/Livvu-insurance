<?PHP

session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");


if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['Submit']);
	//$logout
	if($submitbutton)
	{
			//if($fgmembersite->RegisterHealthInstitution())
   			//{
        			$fgmembersite->RedirectToURL("terms_conditions_health.php");
			//}
	}else{
		
		$fgmembersite->RedirectToURL("login.php");

	}
}


?>
<!DOCTYPE html>
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

    $("#mregion").html( "" );

    if (nameofcountry.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "getregion.php",
            data: "nameofcountry="+nameofcountry1,
            cache: false,
            beforeSend: function () { 
                $('#mregion').html('<img src="loader.gif" alt="" width="24" height="24">');
            },
            success: function(html) {    
                $("#mregion").html( html );
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
				<legend>Health Institutions Registration Form</legend>

			
				<input type='hidden' name='submitted' id='submitted' value='1'/>

<div class="col-xs-12 text-center" class='short_explanation'>* required fields</div>
<div id='fg_membersite'><input type='text'  class='spmhidip' name='<?php echo $fgmembersite->GetSpamTrapInputName(); ?>' /></div>

<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='hname' >Name*:</label>
    					<input class="col-xs-9 text-center" type='text' name='hname' id='hname' value='<?php echo $fgmembersite->SafeDisplay('hname') ?>' maxlength="50" /><br/>
    					<span id='register_hname_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='licence_no' >Merchant Licence No*:</label>
    					<input class="col-xs-9 text-center" type='text' name='licence_no' id='licence_no' value='<?php echo $fgmembersite->SafeDisplay('licence_no') ?>' maxlength="50" /><br/>
    					<span id='register_licence_no_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='maddress' >Merchant Address*:</label>
    					<input class="col-xs-9 text-center" type='text' name='maddress' id='maddress' value='<?php echo $fgmembersite->SafeDisplay('maddress') ?>' maxlength="50" /><br/>
    					<span id='register_maddress_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='mdescription' >Merchant Description*:</label>
    					

					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$sql="SELECT type_of_institution FROM tbl_health_descriptions"; 
					echo "<select class=col-xs-9 id=mdescription name=mdescription>";
					echo "<option value=''>Select Merchant Description</option>"; // list box select command					

					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["type_of_institution"].'">'.$row["type_of_institution"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
						echo "</select>";// Closing of list box
        				?>

					<br/>
    					<span id='register_mdescription_errorloc' class='error'></span>
				</div>
					
				
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='msector' >Merchant Sector*:</label>
    					

					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$sql="SELECT name_of_sector FROM tbl_merchant_sector"; 
					echo "<select class=col-xs-9 id=msector name=msector>";
					echo "<option value=''>Select Merchant Sector</option>"; // list box select command					

					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["name_of_sector"].'">'.$row["name_of_sector"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
						echo "</select>";// Closing of list box
        				?>

					<br/>
    					<span id='register_msector_errorloc' class='error'></span>
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
    					<label class="col-xs-3 text-center" style="text-align: right;" for='mregion' >Country Region*:</label>
					<select class="col-xs-9 text-center" id=mregion name=mregion>Enter Name Of Region</select>
    					<span id='register_mregion_errorloc' class='error'></span>
				</div>
	
				
			
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='username' >Phone Number*:</label>
    					<input class="col-xs-9 text-center" type='text' name='username' id='username' value='<?php echo $fgmembersite->SafeDisplay('username') ?>'  /><br/>
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
	
	var hname = document.getElementById("hname").value;
                  
	var licence_no = document.getElementById("licence_no").value;
 
	var maddress = document.getElementById("maddress").value;
                  
	var mdescription = document.getElementById("mdescription").value;
  
	var msector = document.getElementById("msector").value;
            
	var nameofcountry = document.getElementById("nameofcountry").value;
  
	var mregion = document.getElementById("mregion").value;
                  
	var username = document.getElementById("username").value;
                  
	var password = document.getElementById("password").value;
  
 	var cpassword = document.getElementById("cpassword").value;
  

	if(!checkEmptyInput()){

		$.ajax({
            		type: "POST",
            		url: "set_register_health_session_variables.php",
            		data: 'hname='+hname+'&licence_no='+licence_no+'&maddress='+maddress+'&mdescription='+mdescription+'&msector='+msector+'&nameofcountry='+nameofcountry+'&mregion='+mregion+'&username='+username+'&password='+password+'&cpassword='+cpassword,
           	 	cache: false,
			async: false,
            		success: function(data) {  
                			message = data;
					window.location.href="terms_conditions_health.php";
            			}
        		});
	}
	

}

function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
			var hname = document.getElementById("hname").value;
                  
			var licence_no = document.getElementById("licence_no").value;
 
			var maddress = document.getElementById("maddress").value;
                  
			var mdescription = document.getElementById("mdescription").value;
 
			var msector = document.getElementById("msector").value;            
			var nameofcountry = document.getElementById("nameofcountry").value;
  
			var mregion = document.getElementById("mregion").value;
                  
			var username = document.getElementById("username").value;
                  
			var password = document.getElementById("password").value;
  
 			var cpassword = document.getElementById("cpassword").value;
  
 

			if(hname === ""){
                    
						alert("Please Enter Name Of Institution");
                    
						isEmpty = true;
                
			}
else if(licence_no === ""){
                    
						alert("Please Enter The Licence No");
                    
						isEmpty = true;
                
			}
else if(maddress === ""){
                    
						alert("Please Enter Address Of Institution");
                    
						isEmpty = true;
                
			}else if(mdescription === ""){
                    
						alert("Please Enter Description Of Institution");
                    
						isEmpty = true;
                
			}else if(msector === ""){
                    
						alert("Please Enter Sector Of Institution");
                    
						isEmpty = true;
                
			}else if(nameofcountry === ""){
                    
						alert("Please Enter Country Of Institution");
                    
						isEmpty = true;
 
			}else if( mregion === "" ){
                    
						alert("Please Enter Region Of Institution");
                    
						isEmpty = true;
               
			}else if( username === "" ){
                    
						alert("Please provide Phone Number Of Institution");
                    
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