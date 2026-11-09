<?php
    
	session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_reset_passwords_phone_numbers';

$response = array();

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}


//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
   
if ($_SERVER["REQUEST_METHOD"] == "POST") {
	

	$submitbutton= isset($_POST['Submit']);
	$send= isset($_POST['Send']);
	//$logout
	if($submitbutton){
		
		$main_menu = $_SESSION["main_menu"];
			
			
			if($main_menu=="main_livvu")
			{
				$fgmembersite->RedirectToURL("main_livvu_system_maintenance.php");
			}else{
				$fgmembersite->RedirectToURL("main_admin_system_maintenance.php");
			}

	}else{
		
			$fgmembersite->RedirectToURL("logout.php");
	}

}else{

   	$dataRow ="";
	$username = $_SESSION["user_name"];
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();
	$qry = "SELECT phone_number, request_date, request_processed, request_completed,country FROM tbl_reset_passwords_requests WHERE request_completed = 'N' ORDER BY 2 ASC";
$num=0;
	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$total = $stmt->rowCount();
	if($total > 0)
	{	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		    		

		foreach($result as $row)
		{
			$temp["success"] = true; 
			$phone_number = $row['phone_number'];
   
        
			//$temp["phone_number"] = $row['phone_number'];
 
			$request_date = $row['request_date'];
 
			//$temp["request_date"] = $row['request_date'];
 
			//$temp["request_processed"] = $row['request_processed']; 
			$request_processed = $row['request_processed']; 
			//$temp["request_completed"] = $row['request_completed'];

			$request_completed = $row['request_completed'];
			$login_country = $row['country'];

			$num=$num+1;
						//array_push($response, $temp);
			$dataRow = $dataRow."<tr><td>$num</td><td>$phone_number</td><td>$request_date</td><td>$login_country</td><td>$request_processed</td><td>$request_completed</td><td><input type='radio' name='View' value='View' onclick='viewRowData($num)'>View Details</td><td><input type='radio' name='Send' value='Send' onclick='getRowData($num)'>Reset</td></tr>";		
		}
	}else{
						
		$success = "false";
		$message = "There Are No Phone Numbers For Password Reset.";
		$_SESSION["message_create"] = $message;
		$fgmembersite->RedirectToURL("confirm_main_maintenance_messages.php");
								
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
			
			

			<form id='result' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>


			<fieldset >
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
							<th class="text-center">No</th>
							<th class="text-center">Phone</th>
            						<th class="text-center">Date</th>
						<th class="text-center">Country</th>
            						<th class="text-center">Processed</th>
            						<th class="text-center">Completed</th>
        					</tr>
    					</thead> 
					<tbody>
        					<?php echo $dataRow;?>
					</tbody>
				</table>

			</div>
				

				<div div class="col-xs-12 text-center">
					<br/>
					<input type='submit' name='Submit' value='Maintenance Menu' />
					<input type='submit' name='Logout' value='Logout' /><br/><br/>
				</div>


			</fieldset>
		</form>
	</div>
</div>


</div>
</div>

<script>
function getRowData(id_value)
{

var sel1 = document.getElementById('table1')
var phone_no = sel1.rows[id_value].cells[1].innerHTML;
var user_country = sel1.rows[id_value].cells[3].innerHTML;

if (phone_no.length > 0 ) {

     $.ajax({
            	type: "POST",
           	url: "reset_profiles.php",
            	data: 'user_login_id_to_update='+phone_no+'&user_country='+user_country,
           	cache: false,
		async: false,
            	success: function(data) {  
                		message = data;
				window.location.href="confirm_main_maintenance_messages.php";
            		}
        });
    } 

}

function viewRowData(id_value)
{

var sel1 = document.getElementById('table1')
var phone_no = sel1.rows[id_value].cells[1].innerHTML;
var user_country = sel1.rows[id_value].cells[3].innerHTML;

if (phone_no.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "view_reset_password_set_session.php",
            data: "phone_number="+phone_no+'&user_country='+user_country,
            cache: false,
async: false,
            	success: function(data) {  
                		message = data;
				window.location.href="get_reset_passwords_details.php";
            		}
        });

//window.location.href="get_reset_passwords_details.php";
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

