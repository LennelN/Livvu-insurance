<?php
    
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$response = array();
$file_name='transfer_points';

if(!$fgmembersite->CheckLogin())
{
    header("Location:logoutSession.php?session_logged_out=1");
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
	
	$submitbutton= isset($_POST['Submit']);
	//$logout
		if($submitbutton){
			$fgmembersite->RedirectToURL("merchant_reports_menu.php");
		}else{

			$fgmembersite->RedirectToURL("logout.php");
		}

	
}else{

	$username = ($_SESSION["merchant_phone_number"]);
	$merchant_code = ($_SESSION["merchant_code"]);
	$cashed_out_transaction_no = ($_SESSION["cashed_out_transaction_no"]);
	$selected_cash_out_date = ($_SESSION["selected_cashed_out_date"]);
 	$media = "Internet";
 
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();

	try
	{
			$db->sth->beginTransaction();
			$todayDate = date('Y-m-d');
			if($selected_cash_out_date > $todayDate)
			{
						
				$success = "false";
				$message = "Paid Out Date Cannot Be Greater Than Today...!!!";
				//array_push($response,array("success"=>$success,"message"=>$message));
				//echo json_encode($response);
			}else{


		
					$sql= "UPDATE tbl_merchant_amount_cashed_out SET amount_paid_out_date = '$selected_cash_out_date',amount_paid_out_indicator = 'Y' WHERE merchant_phone_number = :field_val AND cashed_out_transaction_no = :field_val1 AND merchant_code = :field_val2 AND amount_cashed_out_indicator = 'Y'"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $cashed_out_transaction_no, PDO::PARAM_STR);
					$stmt->bindParam(':field_val2', $merchant_code, PDO::PARAM_STR);
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$success = "true";
						$message = "You Have Successfully Confirmed Your Payment. ";
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($response);
					}else{
						
						$success = "false";
						$message = "Your Amount Has Not Been Cashed Out..!!!";
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($response);
					}
					$db->sth->commit();
			}
			}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
					{																						$success = "false";													
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
						$error_message=$e->getMessage();
						$error_type='duplicate';
						handler($error_message,$file_name,$success,$error_type);
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($response);
						$db->sth->rollBack();
					}else{		
						$success = "false";
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";												$error_message=$e->getMessage();
						$error_type='exception';
						handler($error_message,$file_name,$success,$error_type);
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($response);
						$db->sth->rollBack();
					}
		}//


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

					<label><?php echo $message ?></label><br/>
				</div>


				<div div class="col-xs-12 text-center">
					<br/>
					<input type='submit' name='Submit' value='Reports Menu' />
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

