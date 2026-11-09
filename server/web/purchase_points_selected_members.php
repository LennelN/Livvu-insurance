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
			$fgmembersite->RedirectToURL("main_society.php");
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
<meta http-equiv="refresh" content="9000">
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
			<form id='create_society_members' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
			<fieldset >
				<legend>Enter Phone Numbers To Pay Subscriptions</legend>

			
				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div class="col-xs-12 text-center" class='short_explanation'>* required fields</div>
				<div id='fg_membersite'><input type='text'  class='spmhidip' name='<?php echo $fgmembersite->GetSpamTrapInputName(); ?>' /></div>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				<div class="col-xs-12 text-center">
    					<label class="col-xs-3 text-center" style="text-align: right;" for='phone' >Enter Phone Number*:</label>
    					<input class="col-xs-9 text-center" type='text' name='phone' id='phone' value='<?php echo $fgmembersite->SafeDisplay('phone') ?>' maxlength="50" /><br/><br/>
    					<span id='create_society_members_phone_errorloc' class='error'></span>
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

    					<span id='create_society_members_nameofcountry_errorloc' class='error'></span>
				
				</div>

				<div class="col-xs-12 text-center">

						<input  type='button' name='Add' value='Add Phone Number' onclick='getRowData()'/>
				</div>




			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
							<th class="text-center">Phone Number</th>
							<th class="text-center">Country</th>
        					</tr>
    					</thead> 
					<tbody>					
						
					</tbody>
				</table>

			</div>
				


<div class="col-xs-12 text-center">
	
    	<input  type='button' name='Create' value='Purchase Points' onclick='getCellData()' /><br/>
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
  

function getRowData()
{

var errors=[];

			if(!checkEmptyInput()){
	
   
							var newRow = table.insertRow(table.length);  
							var cell1 = newRow.insertCell(0); 
							var cell2 = newRow.insertCell(1); 
							var cell3 = newRow.insertCell(2);
							var phone = document.getElementById("phone").value;
							var nameofcountry = document.getElementById("nameofcountry").value;
							var buttoncode = document.createElement('input');
							buttoncode.setAttribute('type','button');
							buttoncode.setAttribute('name','Remove');
							buttoncode.setAttribute('value','Remove');
							buttoncode.setAttribute('onclick','removeSelectedRow()');
							var element1 = document.createElement("input");
			
							element1.type = "checkbox";
			
							element1.name="chkbox[]";

							
							
							cell1.innerHTML = phone;
                							cell2.innerHTML = nameofcountry;
							cell3.appendChild(buttoncode);
							
							     
				}

}

	

	function checkEmptyInput()
            
		{
                
			
			var isEmpty = false;                    
			var phone = document.getElementById("phone").value;
 			var nameofcountry = document.getElementById("nameofcountry").value;
			if(phone === ""){
                    
						alert("Phone Number Cannot Be Empty");
                    
						isEmpty = true;
                
			}else if(nameofcountry === ""){

				alert("Please Select Country");
                    
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
 
var phone = document.getElementById("phone").value;
  
                 	
if(!checkEmptyInput()){

var row_phone;
var row_country;

for(var i = 1; i <= table.rows.length-1; i++)
                
	{
                  
		row_phone = table.rows[i].cells[0].innerHTML;
		row_country = table.rows[i].cells[1].innerHTML;
		
		TableData[i]={"phone_no" : row_phone,"phone_country" : row_country}
; 


	}



if(table.rows.length > 1)
{

		TableData.shift();
		TableData = $.toJSON(TableData);
	
			

								$.ajax({
            								type: "POST",
            								url: "enter_part_society_company_points_purchased.php",
            								data: 'phone_number='+phone+'&pTableData='+TableData,
           	 							cache: false,
									async: false,
            								success: function(data) {  
                								message = data;
										window.location.href="confirm_purchase_society_members.php";
            								}
        							});



}else{
	alert("Please Add Telephone Numbers Before You Can Purchase Points");
}
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