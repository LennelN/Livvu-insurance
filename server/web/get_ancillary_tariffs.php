<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_ancillary_tariffs';

$response = array();

//error_log(strip_tags("Error logging in. missing fields"), 0);
//if (isset($_POST['username'])) {
  
if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}
 
if ($_SERVER["REQUEST_METHOD"] == "POST") {
	
	$submitbutton= isset($_POST['Submit']);
	$delete= isset($_POST['Delete']);
	$update= isset($_POST['Update']);
	//$logout
	if($submitbutton){

		if($_SESSION["main_menu"] == "main_livvu"){
			$fgmembersite->RedirectToURL("main_livvu_system_maintenance.php");
		}else{
			$fgmembersite->RedirectToURL("main_admin_system_maintenance.php");
		}

	}else{
		if($update)
		{
			$fgmembersite->RedirectToURL("mantain_update_ancillary_tariff.php");
		}else{
			if($delete){
			$fgmembersite->RedirectToURL("mantain_delete_ancillary_tariff.php");
			}else{
				$fgmembersite->RedirectToURL("logout.php");
			}
		}
	}

}else {
	//error_log(strip_tags("coming in "), 0);
	$country = $_SESSION["selected_country"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();
	$dataRow = "";
	$qry = "Select tariff_code,tariff_description,tariff_price,tariff_category,tariff_type from tbl_ancillary_tariffs WHERE tariff_country = '$country' ORDER BY 1";
	$stmt = $db->sth->prepare($qry);
        $stmt->execute();
	$total = $stmt->rowCount();
					$num=0;
					if($total > 0){
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row){

								$tariff_code = $row['tariff_code'];
								$tariff_description = $row['tariff_description'];
								$tariff_price = $row['tariff_price'];
			
								$tariff_category = $row['tariff_category'];
								$tariff_type = $row['tariff_type'];
								$num = $num + 1;

			$dataRow = $dataRow."<tr><td>$tariff_code</td><td>$tariff_description</td><td>$tariff_price</td><td>$tariff_category</td><td>$tariff_type</td><td><input type='submit' name='Update' value='Update Tariff' onclick='getRowData($num)' /></td><td><input type='submit' name='Delete' value='Delete Tariff' onclick='getRowData($num)'/></td></tr>";
							}
		
    
					}else{
								$success = "false";
								$message = "There Are No Ancillary Tariff Details For Country ".$country." Please Add Tariffs ";
								$_SESSION["message_create"] = $message;
								$fgmembersite->RedirectToURL("confirm_main_maintenance_messages.php");
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
				
			<legend><?php echo "Ancillary Tariffs"; ?></legend>
				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

			<div class="col-xs-12 text-center">
				<table id="table1" class="col-xs-12 text-center">
    					<thead>
        					<tr>
            						<th class="text-center">Code</th>
            						<th class="text-center">Description</th>
            						<th class="text-center">Price</th>
            						<th class="text-center">Category</th>
							<th class="text-center">Type</th>
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
var tariff_code = sel1.rows[id_value].cells[0].innerHTML;
var tariff_description = sel1.rows[id_value].cells[1].innerHTML;
var tariff_price = sel1.rows[id_value].cells[2].innerHTML;
var tariff_category = sel1.rows[id_value].cells[3].innerHTML;
var tariff_type = sel1.rows[id_value].cells[4].innerHTML;

if (tariff_code.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "set_tariff_codes.php",
            data: 'tariff_code='+tariff_code+'&tariff_description='+tariff_description+'&tariff_price='+tariff_price+'&tariff_category='+tariff_category+'&tariff_type='+tariff_type,
            cache: false,
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



