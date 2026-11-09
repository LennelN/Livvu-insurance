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
				$fgmembersite->RedirectToURL("main_merchant_users_admin.php");
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">
<head>
<meta http-equiv="refresh" content="900">
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
	<script src='scripts/jquery.json-2.4.min.js'></script>  
<link rel="stylesheet" href="css/bootstrap-select.css"> 
<style>


.bootstrap-select {
  width: 120px \0;
  /*IE9 and below*/
  vertical-align: middle;
}

.bootstrap-select:not([class*="col-"]):not([class*="form-control"]):not(.input-group-btn) {
  width: 220px;
}


</style>
<script>

function setTwoNumberDecimal(el){
	alert("la");
	alert(el.value);
	el.value = parseFloat(el.value).toFixed(2);
}
</script>
<script>
$(document).ready(function(){
$("select#nameofcountry").change(function(){
	
     	var sel = document.getElementById('nameofcountry');
	
	var opt = sel.options[sel.selectedIndex];
	var nameofcountry1 = opt.text;

	var phone = document.getElementById("phone").value;
	

    	var nameofcountry =  $("select#nameofcountry option:selected").attr('value'); 
	//var phonenoentered =  $("select#phone option:selected").attr('value'); 
	  var billno;
	//alert(nameofbank1);

  	  if (nameofcountry.length > 0 && phone > 0) {
		
     		$.ajax({
            			type: "POST",
            			url: "get_patient_billing_details.php",
            			data: "phone="+phone+'&nameofcountry='+nameofcountry1,
           	 		cache: false,
			async: false,
            			success: function(response) 
			{

				var jsonData = JSON.parse(response);

				if (jsonData.response == true) 
				{
					for(var i=0; i < jsonData.ddd.length; i++)
					{
						var cnt = jsonData.ddd[i];
						$("#patientname").val(cnt.full_name);
						$("#patientbalance").val(cnt.total_balance);
						$("#billnumber").val(cnt.bill_no);
					}
				
				}else{
					alert("No Bill Has Been Created. Please Ask Client To Request For A Bill From Livvu");
				}	
			}
        		});
    	} 
});
});
</script>


<script>
$(document).ready(function(){
$("select#fname").change(function(){
     var sel = document.getElementById('fname');
	
var opt = sel.options[sel.selectedIndex];
var fname1 = opt.text;

    var fname =  $("select#fname option:selected").attr('value'); 
   
//alert(nameofbank1);

    $("#doctorname").html( "" );
    if (fname.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "get_merchant_doctor_details.php",
            data: "phoneno="+fname1,
            cache: false,
            success: function(response) {    
                var jsonData = JSON.parse(response);

	if (jsonData.response == true) 
	{
		for(var i=0; i < jsonData.ddd.length; i++)
		{
			var cnt = jsonData.ddd[i];																	$("#doctorname").val(cnt.full_name);
		}
				
	}	
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
			<form id='create_bill_record' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset >
				<legend>Capture And Create Bill For Patient</legend>

			
				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div class="col-xs-12 text-center" class='short_explanation'>* required fields</div><br/>
				<div id='fg_membersite'><input type='text'  class='spmhidip' name='<?php echo $fgmembersite->GetSpamTrapInputName(); ?>' /></div>
<br/>
				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				<div class="col-xs-12 text-center">
					<div class="col-xs-6 text-left">
    					<label style="text-align: left;" for='fname' >Select Doctor Phone Number*:</label>
					
    					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					//$merchant_country = $_SESSION["merchant_country"];
					$merchant_phone_number = $_SESSION["merchant_phone_number"];
					$merchant_code = $_SESSION["merchant_code"];
					
					$sql="Select phone_number from tbl_doctor_details WHERE merchant_phone_number = '$merchant_phone_number' AND merchant_code = '$merchant_code' AND doctor_active = 'Y' ORDER BY 1"; 
					echo "<select  id=fname name=fname class='selectpicker' data-live-search='true' >";
					echo "<option value=''>Select Doctor Phone Number</option>"; // list box select command					
					 //echo '<option selected="selected" value="'.$row["public_desc"].'">'.$row["public_desc"].'</option>';
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["phone_number"].'">'.$row["phone_number"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
	        					?><br/><br/>
					</div>

					<div class="col-xs-6 text-left">

						<fieldset disabled>
							<label style="text-align: left;" for='doctorname' >Doctors Name*:</label>
    							<input type='text' name='doctorname' id='doctorname'  maxlength="30" /><br/><br/>
    							<span id='create_bill_record_doctorname_errorloc' class='error'></span>

						</fieldset>

					</div>

    					<span id='create_bill_record_fname_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
					<div class="col-xs-6 text-left">
    						<label style="text-align: left;" for='phone' >Patient Phone No*:</label>
    						<input type='text' name='phone' id='phone' value='<?php echo $fgmembersite->SafeDisplay('phone') ?>' maxlength="10" /><br/><br/>
					</div>

					<div class="col-xs-6 text-left">
    						<label for='nameofcountry'  >Patient Country*:</label>
    						<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$sql="Select country_name from tbl_country_codes"; 
					echo "<select class=text-center id=nameofcountry name=nameofcountry>";
					echo "<option value=''>Select Country</option>"; // list box select command					
					 //echo '<option selected="selected" value="'.$row["public_desc"].'">'.$row["public_desc"].'</option>';
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["country_name"].'">'.$row["country_name"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
        			?><br/>
<br/>
					</div>

				</div>
				<div class="col-xs-12 text-center">
					<div class="col-xs-4 text-left">
						<fieldset disabled>
							<label style="text-align: left;" for='patientname' >Patient Name*:</label>
    							<input  type='text' name='patientname' id='patientname' maxlength="50" /><br/><br/>
    							<span id='create_bill_record_patientname_errorloc' class='error'></span>

						</fieldset>


					</div>
				
					<div class="col-xs-4 text-left">
						<fieldset disabled>
							<label style="text-align: left;" for='patientbalance' >Balance*:</label>
    							<input type='text' name='patientbalance' id='patientbalance'  onchange="setTwoNumberDecimal(this)" maxlength="20" /><br/><br/>
    							<span id='create_bill_record_patientbalance_errorloc' class='error'></span>

						</fieldset>


					</div>
					
					<div class="col-xs-4 text-left">
						<fieldset disabled>
							<label style="text-align: left;" for='billnumber' >Billing Number*:</label>
    							<input type='text' name='billnumber' id='billnumber'  maxlength="15" /><br/><br/>
    							<span id='create_bill_record_billnumber_errorloc' class='error'></span>

						</fieldset>


					</div>


    					<span id='create_bill_record_phone_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">

					<div class="col-xs-6 text-center">
					
    					<label style="text-align: left;" for='quantity' >Enter Quantity</label>
    					<input style="text-align: left;" type='quantity' name='quantity' id='quantity' onkeydown="validateNumber(event)" value='<?php echo $fgmembersite->SafeDisplay('quantity') ?>' />
    						<span id='create_bill_record_quantity_errorloc' class='error'></span>
					</div>

					
				<div class="col-xs-6 text-center">
					<label style="text-align: left;" for='tarifff' >Select Tariff</label>
					
    					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$merchant_country = $_SESSION["country"];
					
					$sql="Select tariff_code from tbl_ancillary_tariffs WHERE tariff_country = '$merchant_country' ORDER BY 1"; 
					echo "<select  id=tariff name=tariff class='selectpicker' data-live-search='true' >";
					echo "<option value=''>Select Tariff</option>"; // list box select command					
					 //echo '<option selected="selected" value="'.$row["public_desc"].'">'.$row["public_desc"].'</option>';
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						echo '<option value="'.$row["tariff_code"].'">'.$row["tariff_code"].'</option>'; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
        					?><br/>

    					<span id='create_bill_record_tariff_errorloc' class='error'></span>
				
				</div>

<br/>
			</div>



			<div class="col-xs-12 text-center">
					<div class="col-xs-12 text-center">
						<input  type='button' name='Add' value='Add Tariff Data' onclick='getRowData()'/><br/><br/>
					</div>
			</div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
						<th class="text-center">Date</th>
            						<th class="text-center">No</th>
            						<th class="text-center">Tariff Code</th>
            						<th class="text-center">Tariff Description</th>
            						<th class="text-center">Tariff Price</th>
            						<th class="text-center">Tariff Total</th>
        					</tr>
    					</thead>
					
					<tbody>					
					</tbody>
				</table>

			</div>
				

			<div class="col-xs-12 text-center">
<br/>
				<fieldset disabled>
					<label class="col-xs-3 text-center" style="text-align: left;" for='tbil' >Total Bill Amount*:</label>
    					<input class="col-xs-9 text-center" type='text' name='tbill' id='tbill' value=0.00 maxlength="50" /><br/><br/>
    					<span id='create_bill_record_tbill_errorloc' class='error'></span>

				</fieldset>


			</div>

<div class="col-xs-12 text-center">
	
    	<input  type='button' name='Create' value='Create Bill' onclick='getCellData()' />
	<input type='submit' name='Submit' value='Main Menu' />
	<input type='submit' name='Logout' value='Logout' /><br/><br/>

	
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


<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-select.js"></script>


<script>
var rIndex;
                
var table = document.getElementById("table1");
  


function getRowData()
{

var errors=[];

if(!checkEmptyInput()){
   
			
							var price=0;
							var desc;
							var tot_price=0;
							var balance=0;
							var tot_bill=0;
							var tot_bill_2=0;
							
							var fname = document.getElementById("fname").value;
                    
							var phone = document.getElementById("phone").value;
    							var quantity =  document.getElementById("quantity").value;
							var total_bill =  document.getElementById("tbill").value;  
          							var patientbalance = document.getElementById("patientbalance").value;
              							var sel = document.getElementById('tariff');
							var opt = sel.options[sel.selectedIndex];
							var tariff1 = opt.text;
    							var tariffcode =  $("select#tariff option:selected").attr('value'); 
							var buttoncode = document.createElement('input');
							buttoncode.setAttribute('type','button');
							buttoncode.setAttribute('name','Remove');
							buttoncode.setAttribute('value','Remove');
							buttoncode.setAttribute('onclick','removeSelectedRow()');
							var element1 = document.createElement("input");
			
							element1.type = "checkbox";
			
							element1.name="chkbox[]";

							
							if (tariffcode.length > 0 ) {
								
     								$.ajax({
            								type: "POST",
            								url: "tt.php",
            								data: "tariffcode="+tariffcode,
           	 							cache: false,
									async: false,
            								success: function(response) {
										var dt = JSON.stringify(response).replace('[', '').replace(']', '');
										
										if (dt) {
											
  											var json = JSON.parse(dt);
											
											var json2 = JSON.parse(json);
											
											price=json2.price
											
											desc=json2.description;
  											
											tot_price = price * quantity;
											
											tot_price = +(tot_price.toFixed(2));
											
											tot_bill = tot_price + parseFloat(total_bill,2);
											
											
											tot_bill= tot_bill.toFixed(2);
											
											tot_price = tot_price.toFixed(2);
											price=parseFloat(price,2);
											price=price.toFixed(2);
											
										}		

            								}
        							});
    							} 

							balance = patientbalance - tot_price;
							if(balance > 0){
							var newRow = table.insertRow(table.length);  
							var cell1 = newRow.insertCell(0); 
							var cell2 = newRow.insertCell(1);
							var cell3 = newRow.insertCell(2);
							var cell4 = newRow.insertCell(3);
							var cell5 = newRow.insertCell(4);
	                   					var cell6 = newRow.insertCell(5);
							var cell7=newRow.insertCell(6);
							balance=balance.toFixed(2);
							$("#patientbalance").val(balance);							
							$("#tbill").val(tot_bill);
							
							//cell1.innerHTML = date_billed;
                							cell1.innerHTML = '<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?>';
							cell2.innerHTML = quantity;
                
							cell3.innerHTML = tariffcode;
 
							cell4.innerHTML = desc;
							cell5.innerHTML = price;               
							cell6.innerHTML = tot_price;
							
							cell7.appendChild(buttoncode);
							}else{
								alert("Your Bill Cannot Be More Than Your Balance");
							}
							
							     
}

}

	

	function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
			var fname = document.getElementById("fname").value;
                  //alert(fname);
			var phone = document.getElementById("phone").value;
			var patientbalance = document.getElementById("patientbalance").value;
  
			var quantity =  document.getElementById("quantity").value;  
		              var sel = document.getElementById('tariff');
			var opt = sel.options[sel.selectedIndex];
			var tariff1 = opt.text;
    			var tariffcode =  $("select#tariff option:selected").attr('value'); 
			var today = new Date();


			if(fname === ""){
                    
						alert("Doctor's Name Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(phone === ""){
                    
						alert("Phone To Be Billed Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(quantity === ""){
                    
						alert("Quantity Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(tariffcode === ""){
                    
						alert("Tariffcode Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(patientbalance === ""){
                    
						alert("Patient Balance Cannot Be Empty");
                    
						isEmpty = true;
 
			}

			return isEmpty;
            
		}
            
            
 
function checkEmptyPhoneInput()
            
		{
                
			
			var isEmpty = false;                                  
			var phone = document.getElementById("phone").value;
  
                 	
			
			if(phone === ""){
                    
						alert("Phone To Be Billed Cannot Be Empty");
                    
						isEmpty = true;
                
			}

			return isEmpty;
            
		}
            
            



	function removeSelectedRow()
            
		{
  
			rIndex="";
 
			for(var i = 1; i < table.rows.length; i++)
                
			{
     
			        
				table.rows[i].onclick = function()
                    
					{
                      
						// get the seected row index
                
						rIndex = this.rowIndex;

						var total_tariff = table.rows[rIndex].cells[5].innerHTML;
						var total_bill =  document.getElementById("tbill").value; 
						var patient_balance =  document.getElementById("patientbalance").value; 
						var new_total = 0;
						var new_balance = 0;
						new_total = parseFloat((total_bill - total_tariff),2);
						new_total = new_total.toFixed(2);
						
						patient_balance = parseFloat(patient_balance,2);
						new_balance = patient_balance + parseFloat(total_tariff,2);
						new_balance =new_balance.toFixed(2);
						$("#tbill").val(new_total);
						$("#patientbalance").val(new_balance);
						table.deleteRow(rIndex);                        
					           
					};
                
			}
  
			
		}
    

function getCellData(){

var TableData = new Array();
var table = document.getElementById("table1");
 
var fname = document.getElementById("fname").value;
                  
var phone = document.getElementById("phone").value;
//alert(phone);
var billnumber = document.getElementById("billnumber").value;
//alert(billnumber);
var sel = document.getElementById('nameofcountry');
			var opt = sel.options[sel.selectedIndex];
			var patientcountry = opt.text;
//  alert(patientcountry);
                 	
//if(!checkEmptyInput()){

var row_dte;
var row_qty;
var row_cde;
var row_desc;
var row_price;
var row_total;
var total_bill;

for(var i = 1; i <= table.rows.length-1; i++)
                
	{
                  
		row_dte = table.rows[i].cells[0].innerHTML;
		row_qty = table.rows[i].cells[1].innerHTML;
		row_cde = table.rows[i].cells[2].innerHTML;
		row_desc = table.rows[i].cells[3].innerHTML;
		row_price = table.rows[i].cells[4].innerHTML;
		row_total = table.rows[i].cells[5].innerHTML;
		
TableData[i]={"bill_date" : row_dte, "sNo" :row_qty, "code" : row_cde, "description" : row_desc,"price" : row_price, "total_price" : row_total}
; 


	}


if(billnumber == 0){
	alert("Bill Number Cannot Be Equal ToZero. The Client Must Do A Billing Request From Their Phone Before Billing Can Continue");
}else{
	if(table.rows.length > 1)
	{
		
		TableData.shift();
		
		TableData = $.toJSON(TableData);

								$.ajax({
            								type: "POST",
            								url: "create.php",
            								data: 'phone_no_of_doctor='+fname+'&phone_number='+phone+'&patient_country='+patientcountry+'&bill_number='+billnumber+'&pTableData='+TableData,
           	 							cache: false,
									async: false,
            								success: function(data) {  
                								message = data;
										window.location.href="confirm_create_bill.php";
            								}
        							});



	}else{
		alert("Please Add Tariff Data Before You Can Create Bill");
	}

}
}





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

</body>
</html>