<?php
    

session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
//error_log(strip_tags("Error logging in. missing fields b4 "), 0);
	$submitbutton= isset($_POST['Submit']);
	//$logout
	if($submitbutton)
	{

		//error_log(strip_tags("Error logging in. missing fields isode"), 0);	

			//if($fgmembersite->RegisterIndividual())
   			//{
        			$fgmembersite->RedirectToURL("terms_conditions_doctors.php");
			//}
	}else{
		
		

			$main_menu = $_SESSION["main_menu"];

			if($main_menu=="main_merchant")
			{
				$fgmembersite->RedirectToURL("main_merchant.php");
			}else{
				$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");
			}

	

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
				<legend>Medical Doctors Registration Form</legend>

			
				<input type='hidden' name='submitted' id='submitted' value='1'/>

<div class="col-xs-12 text-center" class='short_explanation'>* required fields</div>
<div id='fg_membersite'><input type='text'  class='spmhidip' name='<?php echo $fgmembersite->GetSpamTrapInputName(); ?>' /></div>

<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='fname' >Name*:</label>
    					<input class="col-xs-9 text-center" type='text' name='fname' id='fname' value='<?php echo $fgmembersite->SafeDisplay('fname') ?>' maxlength="50" /><br/>
    					<span id='register_fname_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='surname' >Surname*:</label>
    					<input class="col-xs-9 text-center" type='text' name='surname' id='surname' value='<?php echo $fgmembersite->SafeDisplay('surname') ?>' maxlength="50" /><br/>
    					<span id='register_surname_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='idno' >ID Number*:</label>
    					<input class="col-xs-9 text-center" type='text' name='idno' id='idno' value='<?php echo $fgmembersite->SafeDisplay('idno') ?>' maxlength="50" /><br/>
    					<span id='register_idno_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='dob' >Date Of Birth*:</label>
					<input class="col-xs-9 text-center" type='date' name='dob' id='dob' value='<?php echo $fgmembersite->SafeDisplay('dob') ?>' max='<?php echo date('Y-m-d');?>'/><br/>
    					<span id='register_dob_errorloc' class='error'></span>
				</div>

				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='gender' >Select Gender*:</label>
					<div style="text-align: left;">
    					<input type='radio' name='gender' id='genderMale' value='Male' checked/><label for='genderMale'>Male</label>
					<input type='radio' name='gender' id='genderFemale' value='Female'/><label for='genderFemale'>Female</label>
					
    					<span id='register_gender_errorloc' class='error'></span>
					</div>
				</div>


				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='username' >Phone Number*:</label>
    					<input class="col-xs-9 text-center" type='text' name='username' id='username' value='<?php echo $fgmembersite->SafeDisplay('username') ?>' /><br/>
    					<span id='register_username_errorloc' class='error'></span>
				</div>
			
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='profession' >Profession*:</label>
    					<input class="col-xs-9 text-center" type='text' name='profession' id='profession' value='<?php echo $fgmembersite->SafeDisplay('profession') ?>' /><br/>
    					<span id='register_profession_errorloc' class='error'></span>
				</div>


				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='licenceno' >Registration Number*:</label>
    					<input class="col-xs-9 text-center" type='text' name='licenceno' id='licenceno' value='<?php echo $fgmembersite->SafeDisplay('licenceno') ?>' /><br/>
    					<span id='register_licenceno_errorloc' class='error'></span>
				</div>

<div class="col-xs-12 text-center">
	<div class="col-xs-3 text-center">
	</div>

	<div class="col-xs-9 text-center">
<br/>
    	<input  type='button' name='Submit' value='Submit' onclick='getCellData()'/>
	<input  type='submit' name='Cancel' value='Main Menu'/>
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
	
			var fname = document.getElementById("fname").value;
    
			var surname = document.getElementById("surname").value;
                                
			var idno = document.getElementById("idno").value;
 
			var dob = document.getElementById("dob").value;
                  
			var gender = $("input:radio[name=gender]:checked").val();                       
			var username = document.getElementById("username").value;
                  
			var licenceno = document.getElementById("licenceno").value;
			var profession = document.getElementById("profession").value;
  
 			
			

	if(!checkEmptyInput()){

		$.ajax({
            		type: "POST",
            		url: "set_register_doctors_session_variables.php",
            		data: 'fname='+fname+'&surname='+surname+'&idno='+idno+'&dob='+dob+'&gender='+gender+'&username='+username+'&licenceno='+licenceno+'&profession='+profession,
           	 	cache: false,
			async: false,
            		success: function(data) {  
                			message = data;
					window.location.href="terms_conditions_doctors.php";
            			}
        		});
	}
	

}

function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
			var fname = document.getElementById("fname").value;
    
			var surname = document.getElementById("surname").value;
                                
			var idno = document.getElementById("idno").value;
 
			var dob = document.getElementById("dob").value;
                  
			var gender = $("input:radio[name=gender]:checked").val();                          
			var username = document.getElementById("username").value;
                  
			var licenceno = document.getElementById("licenceno").value;
			var profession = document.getElementById("profession").value;  
 			
      


			if(fname === ""){
                    
						alert("Please Enter First Name");
                    
						isEmpty = true;
                
			}
else if(surname === ""){
                    
						alert("Please Provide Your Surname");
                    
						isEmpty = true;
                
			}
else if(idno === ""){
                    
						alert("Please Provide Your Id Number");
                    
						isEmpty = true;
                
			}else if(dob === ""){
                    
						alert("Please Provide Your Date Of Birth");
                    
						isEmpty = true;
                
			}else if( gender === "" ){
                    
						alert("Please Select Your Gender");
                    
						isEmpty = true;
               
			}else if( username === "" ){
                    
						alert("Please Enter Phone Number");
                    
						isEmpty = true;
               
			}else if( profession === "" ){
                    
						alert("Please Enter Profession Of Doctor");
                    
						isEmpty = true;
               
			}else if( licenceno === "" ){
                    
						alert("Please Enter Registration Number");
                    
						isEmpty = true;
               
			}
			return isEmpty;
            
		}
            
            



</script>
</body>
</html>