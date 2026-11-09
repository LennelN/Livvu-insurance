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

}else{
	$username = ($_SESSION["merchant_phone_number"]);
	//$username='0779455025';
	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();

	$dataRow = "";
	$todayDate =  date('Y-m-d');
	$todaysdate = strtotime($todayDate);

	$sql = "SELECT `transaction_date`, `merchant_doctor`, `bill_number`, `phone_number_billed`, `tariff_description`, `tariff_amount`, `tariff_total_amount`, `tariff_code`, `tariff_code_frequency` FROM `tbl_bill_items_tariff_records` WHERE merchant_phone_number = :field_val ORDER BY 1 ASC";
	
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
    										//$success = true;
												//$temp["success"] = true; 
							
												$transaction_date = $row['transaction_date'];																	$timestamp = strtotime($transaction_date); 
												$new_date = date('d-m-Y', $timestamp);
												$dt   = new DateTime();
												$date_bill_created = $new_date;
												$merchant_doctor = $row['merchant_doctor'];
												$bill_number = $row['bill_number'];
												$phone_number_billed = $row['phone_number_billed'];
												
												$tariff_description = $row['tariff_description'];
												$tariff_amount = $row['tariff_amount'];
												$tariff_total_amount = $row['tariff_total_amount'];
												$tariff_code = $row['tariff_code'];
												$tariff_code_frequency = $row['tariff_code_frequency'];
												
		$dataRow = $dataRow."<tr><td>$date_bill_created</td><td>$tariff_code_frequency</td><td>$tariff_code</td><td>$tariff_description</td><td>$tariff_amount</td><td>$tariff_total_amount</td><td><input type='button' onclick='removeSelectedRow()' name='Remove' value='Remove' /></tr>";										
								}
								//echo json_encode($response);
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
  	<script src="js/bootstrap.min.js"></script> 
	<script src='scripts/jquery.json-2.4.min.js'></script>   
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

				<div class="col-xs-12 text-center" class='short_explanation'>* required fields</div>
				<div id='fg_membersite'><input type='text'  class='spmhidip' name='<?php echo $fgmembersite->GetSpamTrapInputName(); ?>' /></div>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: left;" for='fname' >Enter Doctor's Name*:</label>
    					<input class="col-xs-9 text-center" type='text' name='fname' id='fname' value='<?php echo $fgmembersite->SafeDisplay('fname') ?>' maxlength="50" /><br/><br/>
    					<span id='create_bill_record_fname_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: left;" for='phone' >Enter Phone Number*:</label>
    					<input class="col-xs-6 text-center" type='text' name='phone' id='phone' value='<?php echo $fgmembersite->SafeDisplay('phone') ?>' maxlength="50" />
					<input  class="col-xs-3 text-center" type='button' name='Get' value='Get Current Bill Items' onclick='getBillItemsData()'/><br/><br/>
    					<span id='create_bill_record_phone_errorloc' class='error'></span>
				</div>
				<div class="col-xs-12 text-center">

					<div class="col-xs-3 text-center">
    						<label style="text-align: left;" for='date' >Date*:</label>
    						<input type='date' name='date' id='date' value='<?php echo $fgmembersite->SafeDisplay('date') ?>' max='<?php echo date('Y-m-d');?>'/><br/>
    						<span id='create_bill_record_date_errorloc' class='error'></span>
					</div>


					<div class="col-xs-3 text-center">
					
    					<label style="text-align: left;" for='quantity' >Quantity</label>
    					<input type='quantity' name='quantity' id='quantity' onkeydown="validateNumber(event)" value='<?php echo $fgmembersite->SafeDisplay('quantity') ?>' />
    						<span id='create_bill_record_quantity_errorloc' class='error'></span>
					</div>

					
				<div class="col-xs-3 text-center">
					
    					<?php 

					include "./include/config.php"; // Database connection using PDO
					
					 $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					$merchant_country = $_SESSION["merchant_country"];
					
					$sql="Select tariff_code from tbl_ancillary_tariffs WHERE tariff_country = '$merchant_country' ORDER BY 1"; 
					echo "<select  id=tariff name=tariff>";
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

					<div class="col-xs-3 text-center">  
						<input  type='button' name='Add' value='Add Data' onclick='getRowData()'/>
					</div>

			</div>




			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
							<th class="text-center">Date</th>
            						<th class="text-center">No</th>
            						<th class="text-center">Code</th>
            						<th class="text-center">Description</th>
            						<th class="text-center">Price</th>
            						<th class="text-center">Total</th>
        					</tr>
    					</thead> 
					<tbody>					
					
					</tbody>
				</table>

			</div>
				


<div class="col-xs-12 text-center">
	
    	<input  type='button' name='Create' value='Create Bill' onclick='getCellData()' />
	<input  type='button' name='Save' value='Save Bill Items' onclick='getCellDataToSave()' /><br/>
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

<script>
var rIndex;
                
var table = document.getElementById("table1");
  

function getBillItemsData()
{

var errors=[];

if(!checkEmptyPhoneInput()){
   
			
							var price=0;
							var desc;
							var transactiondate;
							var tariffcode;
							var tariffcodefrequency;
							var billnumber;
							var tariffamount = 0;
							var totalamount = 0;
							var tot_price = 0;
							var newRow = table.insertRow(table.length);  
							var cell1 = newRow.insertCell(0); 
							var cell2 = newRow.insertCell(1);
							var cell3 = newRow.insertCell(2);
							var cell4 = newRow.insertCell(3);
							var cell5 = newRow.insertCell(4);
                   					var cell6 = newRow.insertCell(5);
							var cell7=newRow.insertCell(6);                   
							var phone = document.getElementById("phone").value;
							alert(phone);
							var buttoncode = document.createElement('input');
							buttoncode.setAttribute('type','button');
							buttoncode.setAttribute('name','Remove');
							buttoncode.setAttribute('value','Remove');
							buttoncode.setAttribute('onclick','removeSelectedRow()');
							var element1 = document.createElement("input");
			
							element1.type = "checkbox";
			
							element1.name="chkbox[]";

							
							if (phone.length > 0 ) {
								alert("here 1");
     								$.ajax({
            								type: "POST",
            								url: "get_saved_bill_items.php",
            								data: "phone="+phone,
           	 							cache: false,
									async: false,
            								success: function(response) {
										var dt = JSON.stringify(response).replace('[', '').replace(']', '');
										alert("here 22222");
										if (dt) {
											alert("here 333333");

  												var json = JSON.parse(dt);
											alert("here 4444");
												var json2 = JSON.parse(json);
											
											alert("here 5555555");
												transactiondate =json2.transaction_date;
												alert(transactiondate);
											merchant_doctor = json2.merchant_doctor;
											billnumber = json2.bill_number;
											tariffamount=json2.tariff_amount;
											desc=json2.tariff_description;
  											totalamount=json2.tariff_total_amount;
											tariffcode=json2.$tariff_code;
											tariffcodefrequency=json2.tariff_code_frequency;
										
											
										}		

            								}
        							});
    							} 
							
							cell1.innerHTML = transactiondate;
                
							cell2.innerHTML = tariffcodefrequency;
                
							cell3.innerHTML = tariffcode;
 
							cell4.innerHTML = desc;
							cell5.innerHTML = tariffamount;               
							cell6.innerHTML = totalamount;
							
							cell7.appendChild(buttoncode);
											
		
							     
}

}


function getRowData()
{

var errors=[];

if(!checkEmptyInput()){
   
			
							var price=0;
							var desc;
							var tot_price = 0;
							var newRow = table.insertRow(table.length);  
							var cell1 = newRow.insertCell(0); 
							var cell2 = newRow.insertCell(1);
							var cell3 = newRow.insertCell(2);
							var cell4 = newRow.insertCell(3);
							var cell5 = newRow.insertCell(4);
                   					var cell6 = newRow.insertCell(5);
							var cell7=newRow.insertCell(6);
							var fname = document.getElementById("fname").value;
                    
							var phone = document.getElementById("phone").value;
							var date_billed = document.getElementById("date").value; 
    							var quantity =  document.getElementById("quantity").value; 
          
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
											
										}		

            								}
        							});
    							} 
							
							cell1.innerHTML = date_billed;
                
							cell2.innerHTML = quantity;
                
							cell3.innerHTML = tariffcode;
 
							cell4.innerHTML = desc;
							cell5.innerHTML = price;               
							cell6.innerHTML = tot_price;
							
							cell7.appendChild(buttoncode);
		
							     
}

}

	

	function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
			var fname = document.getElementById("fname").value;
                  
			var phone = document.getElementById("phone").value;
  
                 	
			
			var quantity =  document.getElementById("quantity").value;  
			
          
              var sel = document.getElementById('tariff');
			var opt = sel.options[sel.selectedIndex];
			var tariff1 = opt.text;
    			var tariffcode =  $("select#tariff option:selected").attr('value'); 
			var today = new Date();

			var date_billed = document.getElementById("date").value;
			var myDate = new Date(date_billed);

			if(fname === ""){
                    
						alert("Doctor's Name Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(phone === ""){
                    
						alert("Phone To Be Billed Cannot Be Empty");
                    
						isEmpty = true;
                
			}
else if(quantity === ""){
                    
						alert("Quantity Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(tariffcode === ""){
                    
						alert("Tariffcode Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(date_billed === ""){
                    
						alert("Date Of Billing Cannot Be Empty");
                    
						isEmpty = true;
 
			}else if( myDate > today ){
                    
						alert("Date Of Billing Cannot Be Greater Than Today");
                    
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

						table.deleteRow(rIndex);                        
					           
					};
                
			}
  
			
		}
    

function getCellData(){

var TableData = new Array();
var table = document.getElementById("table1");
 
var fname = document.getElementById("fname").value;
                  
var phone = document.getElementById("phone").value;
  
                 	
if(!checkEmptyInput()){

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



if(table.rows.length > 1)
{
		
		TableData.shift();
		
		TableData = $.toJSON(TableData);
			
			

								$.ajax({
            								type: "POST",
            								url: "create.php",
            								data: 'name_of_doctor='+fname+'&phone_number='+phone+'&pTableData='+TableData,
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


function getCellDataToSave(){

var TableData = new Array();
var table = document.getElementById("table1");
 
var fname = document.getElementById("fname").value;
                  
var phone = document.getElementById("phone").value;
  
                 	
if(!checkEmptyInput()){

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



if(table.rows.length > 1)
{

		
		TableData.shift();
		
		TableData = $.toJSON(TableData);
			
			

								$.ajax({
            								type: "POST",
            								url: "save_bill_items.php",
            								data: 'name_of_doctor='+fname+'&phone_number='+phone+'&pTableData='+TableData,
           	 							cache: false,
									async: false,
            								success: function(data) {  
                								message = data;
										window.location.href="confirm_create_bill.php";
            								}
        							});



}else{
	alert("Please Add Tariff Data Before You Can Save Bill Items");
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