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
		
		$fgmembersite->RedirectToURL("main.php");

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
            url: "getregionhospital.php",
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

    
<script>
$(document).ready(function(){
$("select#mregion").change(function(){
     var sel = document.getElementById('mregion');
	//alert("lapa");
var opt = sel.options[sel.selectedIndex];
var mregion1 = opt.text;

    var nameofcountry =  $("select#nameofcountry option:selected").attr('value'); 
   var nameofregion =  $("select#mregion option:selected").attr('value'); 
//alert(nameofcountry);
//alert(nameofregion);
//alert(mregion1);
//alert(nameofcountry1);
    $("#mhealth").html( "" );

    if (nameofcountry.length > 0 &&  nameofregion.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "gethospitalname.php",
            data: "nameofcountry="+nameofcountry+'&nameofregion='+mregion1,
            cache: false,
            beforeSend: function () { 
                $('#mhealth').html('<img src="loader.gif" alt="" width="244" height="24">');
            },
            success: function(html) {    
                $("#mhealth").html( html );
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
				<legend>Select Health Institution For Billing</legend>

			
				<input type='hidden' name='submitted' id='submitted' value='1'/>

<div class="col-xs-12 text-center" class='short_explanation'>* required fields</div>
<div id='fg_membersite'><input type='text'  class='spmhidip' name='<?php echo $fgmembersite->GetSpamTrapInputName(); ?>' /></div>

<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				<div class="col-xs-12 text-center">
					<label class="col-xs-3 text-center" style="text-align: right;" for='nameofcountry' >Select Country*:</label>
					
    					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$sql="Select country_name from tbl_country_codes"; 
					echo "<select class=col-xs-9 class=text-center id=nameofcountry name=nameofcountry>";
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
    					<label class="col-xs-3 text-center" style="text-align: right;" for='mhealth' >Select Institution*:</label>
					<select class="col-xs-9 text-center" id=mhealth name=mhealth>Enter Name Of Hospital</select>
    					<span id='register_mhealth_errorloc' class='error'></span>
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
	
	
	var nameofcountry = document.getElementById("nameofcountry").value;
  
	var selregion = document.getElementById('mregion');
	var optregion = selregion.options[selregion.selectedIndex];
	var mregion = optregion.text;

                  
	var sel = document.getElementById('mhealth');
	var opt = sel.options[sel.selectedIndex];
	var mhealth = opt.text;

                  
	
	if(!checkEmptyInput()){
		
		$.ajax({
            		type: "POST",
            		url: "register_billing_request.php",
            		data: 'nameofcountry='+nameofcountry+'&mregion='+mregion+'&mhealth='+mhealth,
           	 	cache: false,
			async: false,
            		success: function(data) {  
                			message = data;
					window.location.href="confirm_billing_request.php";
            			}
        		});
	}
	

}

function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
			var nameofcountry = document.getElementById("nameofcountry").value;
  
			//var mregion = document.getElementById("mregion").value;
			var selregion = document.getElementById('mregion');
			var optregion = selregion.options[selregion.selectedIndex];
			var mregion = optregion.text;

                  
			//var mhealth = document.getElementById("mhealth").value;
			var sel = document.getElementById('mhealth');
			var opt = sel.options[sel.selectedIndex];
			var mhealth = opt.text;

                  		if(nameofcountry === ""){
                    
						alert("Please Enter Country Of Institution");
                    
						isEmpty = true;
 
			}else if( mregion === "" ){
                    
						alert("Please Enter Region Of Institution");
                    
						isEmpty = true;
               
			}else if( mhealth === "" ){
                    
						alert("Please Enter Name Of Institution");
                    
						isEmpty = true;
               
			}		return isEmpty;
            
		}
            
            



</script>


</body>
</html>