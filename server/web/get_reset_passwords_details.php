<?php
    
	session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$file_name='get_reset_passwords_details';

$response = array();

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
error_log(strip_tags("now entering get details now   "), 0); 
   	$dataRow ="";
	$dataHeadRow ="";
	$username = $_SESSION["user_name"];
	$phone_number = $_SESSION["reset_password_phone_number"];
	$user_country = $_SESSION["reset_password_user_country"];

	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();

			
	$sql= "SELECT name,surname,dob,idno,gender,date_joined,member_active,country_name,group_category FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
	$stmt = $db->sth->prepare($sql);
	$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
	$total = $stmt->rowCount();

 	if($total <= 0)
	{
				
		$sql= "SELECT merchant_name,merchant_licence_number,merchant_code,merchant_description,merchant_date_joined,status_flag,merchant_country,type_of_registration FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_country = :field_val1"; 
				$stmt = $db->sth->prepare($sql);
				$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
				$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();			
				
				if($total <= 0)
				{

					//Check in tbl_users for livvu company employees

					$sql= "SELECT name, email, idno, branch, date_registered, user_active, country,user_role FROM tbl_users WHERE user_login_id = :field_val AND country = :field_val1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();

					if($total <=0)
					{//tbl_users else

						$sql= "SELECT name, email, idno, branch, date_registered, user_active,country,user_role FROM tbl_merchant_users WHERE user_login_id = :field_val AND country = :field_val1"; 
						$stmt = $db->sth->prepare($sql);
						$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
						$stmt->bindParam(':field_val1', $user_country, PDO::PARAM_STR); 
						$stmt->execute();
						$total = $stmt->rowCount();

						if($total <=0)
						{
 
    							$success = "false";
							$message = "Phone Number ".$phone_number." Does Not Exist In The System.!!";
									
						}else{
							$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
							foreach($result as $row)
							{
							
								$name = $row['name'];
								$email = $row['email'];
								$idno = $row['idno'];
								$branch = $row['branch'];
								$date_of_joining = $row['date_registered'];
								$timestamp = strtotime($date_of_joining); 
								$new_date = date('d-m-Y', $timestamp);
								$member_active = $row['user_active'];
								$country_name = $row['country'];

								$user_role = $row['user_role'];
								
								$dataHeadRow = $dataHeadRow."<tr><th style=text-align:center;>Name</th><th style=text-align:center;>Email</th><th style=text-align:center;>ID Number</th><th style=text-align:center;>Branch</th><th style=text-align:center;>Joining Date</th><th style=text-align:center;>Active</th><th style=text-align:center;>Country</th><th style=text-align:center;>User Role</th></tr>";
								$dataRow = $dataRow."<tr><td>$name</td><td>$email</td><td>$idno</td><td>$branch</td><td>$new_date</td><td>$member_active</td><td>$country_name</td><td>$user_role</td></tr>";		

							}
								
						}
					}else{
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
						foreach($result as $row)
						{
							$name = $row['name'];
							$email = $row['email'];
							$idno = $row['idno'];
							$branch = $row['branch'];
							$date_of_joining = $row['date_registered'];
							$timestamp = strtotime($date_of_joining); 
							$new_date = date('d-m-Y', $timestamp);
							$member_active = $row['user_active'];
							$country_name = $row['country'];

							$user_role = $row['user_role'];

							$dataHeadRow = $dataHeadRow."<tr><th  style=text-align:center;>Name</th><th style=text-align:center;>Email</th><th style=text-align:center;>ID Number</th><th style=text-align:center;>Branch</th><th style=text-align:center;>Joining Date</th><th style=text-align:center;>Active</th><th style=text-align:center;>Country</th><th style=text-align:center;>User Role</th></tr>";
								$dataRow = $dataRow."<tr><td>$name</td><td>$email</td><td>$idno</td><td>$branch</td><td>$new_date</td><td>$member_active</td><td>$country_name</td><td>$user_role</td></tr>";	
					
						}
					}
				}else{//merchantdetails
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
					foreach($result as $row)
					{
							
						$name = $row['merchant_name'];
						$licence_no = $row['merchant_licence_number'];
						$merchant_code = $row['merchant_code'];
						$description = $row['merchant_description'];
						$date_of_joining = $row['merchant_date_joined'];
						$timestamp = strtotime($date_of_joining); 
						$new_date = date('d-m-Y', $timestamp);
						$member_active = $row['status_flag'];
						$country_name = $row['merchant_country'];

						$group_category= $row['type_of_registration'];
						
						$dataHeadRow = $dataHeadRow."<tr><th  style=text-align:center;>Merchant Name</th><th style=text-align:center;>Licence No</th><th style=text-align:center;>Merchant Code</th><th style=text-align:center;>Description</th><th style=text-align:center;>Joining Date</th><th style=text-align:center;>Active</th><th style=text-align:center;>Country</th><th style=text-align:center;>Category</th></tr>";
								$dataRow = $dataRow."<tr><td>$name</td><td>$licence_no</td><td>$merchant_code</td><td>$description</td><td>$new_date</td><td>$member_active</td><td>$country_name</td><td>$group_category</td></tr>";	
							
					}


				}
		}else{//clients 

			foreach($result as $row)
			{
				$name = $row['name'];
   
				$surname = $row['surname'];
  			      
				$fullname=$name." ".$surname; 
				$dob = $row['dob'];
				$idno = $row['idno'];
				$gender = $row['gender'];
				$date_of_joining = $row['date_joined'];
				$timestamp = strtotime($date_of_joining); 
				$new_date = date('d-m-Y', $timestamp);
				$member_active = $row['member_active'];
				$country_name = $row['country_name'];

				$group_category= $row['group_category'];
	
				$dataHeadRow = $dataHeadRow."<tr><th  style=text-align:center;>Name</th><th style=text-align:center;>Date Of Birth</th><th style=text-align:center;>ID Number</th><th style=text-align:center;>Gender</th><th style=text-align:center;>Joining Date</th><th style=text-align:center;>Active</th><th style=text-align:center;>Country</th><th style=text-align:center;>Group</th></tr>";
				$dataRow = $dataRow."<tr><td>$fullname</td><td>$dob</td><td>$idno</td><td>$gender</td><td>$new_date</td><td>$member_active</td><td>$country_name</td><td>$group_category</td></tr>";	
					
			}
				
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
        					<?php echo $dataHeadRow;?>
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

