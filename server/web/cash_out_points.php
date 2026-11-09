<?php
session_start();
$response = array();
$response_back = array();
$file_name='create_bill_record';
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
						$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");
					}
	}else{

		$fgmembersite->RedirectToURL("logout.php");
	}

}else{


$merchant_code = ($_SESSION['merchant_code']);
$username = ($_SESSION["merchant_phone_number"]);
$points_processed_by = ($_SESSION["user_name"]);
$amount_to_transfer = $_SESSION["no_of_points_cash_out"];
$amount_to_transfer_int = floatval($amount_to_transfer);

 	$media = 'Internet';
	$transaction_type='CashOut';
	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$payment_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$payment_number_generated = mt_rand(0, 999999);
	$payment_number=$payment_letter.$payment_number_generated;

	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT(); 			
 		$sql_merchant = "SELECT merchant_code, merchant_name,merchant_licence_number,merchant_country FROM tbl_merchant_details WHERE merchant_phone_number = :field_val AND merchant_code = :field_val1"; 
		$stmt_merchant = $db->sth->prepare($sql_merchant);
		$stmt_merchant->bindParam(':field_val', $username, PDO::PARAM_STR); 
		$stmt_merchant->bindParam(':field_val1', $merchant_code, PDO::PARAM_STR); 
		$stmt_merchant->execute();
		$total_merchant = $stmt_merchant->rowCount();
		if($total_merchant <= 0)
		{
				$success = "false";
				$message = "Phone Number Does Not Exist..Please Register Before You Can Cash Out Points!!";
				//array_push($response,array("success"=>$success,"message"=>$message));
				//echo json_encode($message);
				//return false;
		}else{	
				$result = $stmt_merchant->fetchAll(PDO::FETCH_ASSOC);						
				foreach($result as $row){
					$code = $row['merchant_code'];
					$name = $row['merchant_name'];
					$licence_no = $row['merchant_licence_number'];
					$merchant_country = $row['merchant_country'];
				}						

				$sql= "SELECT amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
				$stmt = $db->sth->prepare($sql);
				$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
				$stmt->bindParam(':field_val1', $merchant_country, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();
 				if($total <= 0)
					{
										
						$amount = 0; 
    						$success = "false";
						$message = "Your Account Balance Is Zero.";
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($message);
						//return false;
 					}else{
							$success = "true";
							$message = "Account Has A Credit Balance";
							$amount = $stmt->fetchColumn();
							try
							{
								$db->sth->beginTransaction();
								if($amount >= $amount_to_transfer_int)
								{
									$points_cashed_out_indicator = 'N';
									$bill_paid_indicator = 'N';
									$date_transfered = date('Y-m-d H:i:s');

									$balance_of_points = $amount - $amount_to_transfer_int;
									if($balance_of_points == 0)
										{
											$sql = "Update tbl_no_of_points Set amount ='$balance_of_points' Where phone_number='$username' AND country = '$merchant_country'";
    	                                                                        	$stmt_update = $db->sth->prepare($sql);
	                                                                            	$stmt_update->execute();
                                                                                	$total_update =  $stmt_update->rowCount();
 																																if ($total_update > 0)																			{				
								$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_merchant_amount_cashed_out (
														merchant_phone_number,
														merchant_code,
														amount_cashed_out,
														amount_cashed_out_date,
														amount_paid_out_date,
														amount_paid_out_indicator,
														media_used_to_cash_out,
														cashed_out_transaction_no,
														amount_cashed_out_indicator,
														amount_processed_by) 
					    								VALUES												(:merchant_phone_number,:merchant_code,:amount_cashed_out,:amount_cashed_out_date,:amount_paid_out_date,:amount_paid_out_indicator,:media_used_to_cash_out,:cashed_out_transaction_no,:amount_cashed_out_indicator,:amount_processed_by)");
					    						$stmt_insert_history->bindParam(':merchant_phone_number', $username);
					    						$stmt_insert_history->bindParam(':merchant_code', $merchant_code);
									$stmt_insert_history->bindParam(':amount_cashed_out', $amount_to_transfer_int);
											$stmt_insert_history->bindParam(':amount_cashed_out_date',$date_transfered);
											$stmt_insert_history->bindParam(':amount_paid_out_date', $date_transfered);
										$stmt_insert_history->bindParam(':amount_paid_out_indicator', $bill_paid_indicator);
											$stmt_insert_history->bindParam(':media_used_to_cash_out', $media);
										$stmt_insert_history->bindParam(':cashed_out_transaction_no', $payment_number);
								$stmt_insert_history->bindParam(':amount_cashed_out_indicator', $points_cashed_out_indicator);
					$stmt_insert_history->bindParam(':amount_processed_by', $points_processed_by);
											$stmt_insert_history->execute();
											$total_insert_history =  $stmt_insert_history->rowCount();																if($total_insert_history > 0){
														$success = "true";
														$balance_of_points_string = strval($balance_of_points);										$message = "Congratulations You Have Successfully Cashed Out " . $amount_to_transfer . " Your Balance Is Now " . $balance_of_points_string;
												
													}
																	  																}
									                
									      }else{
						
											$date_transfered = date('Y-m-d H:i:s');
											$points_cashed_out_indicator = 'N';
											$bill_paid_indicator = 'N';
											$balance_of_points = $amount - $amount_to_transfer_int;
											$sql = "Update tbl_no_of_points Set amount ='$balance_of_points' Where phone_number='$username' AND country = '$merchant_country'";
    	                                                                        	$stmt_update = $db->sth->prepare($sql);
	                                                                            	$stmt_update->execute();
                                                                                	$total_update =  $stmt_update->rowCount();
 																																if ($total_update > 0)																			{
										
									//Do an insert into tbl_points_transfers
									$stmt_insert_history = $db->sth->prepare("INSERT INTO tbl_merchant_amount_cashed_out (
														merchant_phone_number,
														merchant_code,
														amount_cashed_out,
														amount_cashed_out_date,
														amount_paid_out_date,
														amount_paid_out_indicator,
														media_used_to_cash_out,
														cashed_out_transaction_no,
														amount_cashed_out_indicator,
														amount_processed_by) 
					    			VALUES												(:merchant_phone_number,:merchant_code,:amount_cashed_out,:amount_cashed_out_date,:amount_paid_out_date,:amount_paid_out_indicator,:media_used_to_cash_out,:cashed_out_transaction_no,:amount_cashed_out_indicator,:amount_processed_by)");
					    						$stmt_insert_history->bindParam(':merchant_phone_number', $username);
					    						$stmt_insert_history->bindParam(':merchant_code', $merchant_code);
									$stmt_insert_history->bindParam(':amount_cashed_out', $amount_to_transfer_int);
											$stmt_insert_history->bindParam(':amount_cashed_out_date',$date_transfered);
											$stmt_insert_history->bindParam(':amount_paid_out_date', $date_transfered);
										$stmt_insert_history->bindParam(':amount_paid_out_indicator', $bill_paid_indicator);
											$stmt_insert_history->bindParam(':media_used_to_cash_out', $media);
										$stmt_insert_history->bindParam(':cashed_out_transaction_no', $payment_number);
									$stmt_insert_history->bindParam(':amount_cashed_out_indicator', $points_cashed_out_indicator);
									$stmt_insert_history->bindParam(':amount_processed_by', $points_processed_by);
											$stmt_insert_history->execute();
											$total_insert_history =  $stmt_insert_history->rowCount();																if($total_insert_history > 0){

														$success = "true";
														$balance_of_points_string = strval($balance_of_points);															$message = "Congratulations You Have Successfully Cashed Out " . $amount_to_transfer . " Your Balance Is Now " . $balance_of_points_string;
													
													}
																	  															}
										}
						}else{
						
						$success = "false";
						$amount_string = strval($amount);
						$message = "You Are Trying To Cash Out More Than What You Have In Your Account. Your Balance Is " .$amount_string;
						
						}
																																								
	$db->sth->commit();
	}catch(PDOException $e){
		if ($e->errorInfo[1] == 1062) 
		{
			$success = "false";
		 	$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 	$error_message=$e->getMessage();
		 	$error_type='duplicate';
		 	$db->sth->rollBack();
		}else{
			$success = "false";
		 	$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 	$error_message=$e->getMessage();
		 	$error_type='exception';
		 	$db->sth->rollBack();
		}	
	}				
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

			<form id='confirm' action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8'>
			<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>

			<fieldset disabled>

				<legend>Cash Out Amount Result</legend>
				<br/>
				<div class="col-xs-12 text-center">
    					<label><?php echo $message ?> </label><br/>
					<br/>
				</div>

			</fieldset>

			<fieldset >
				

				<input type='hidden' name='submitted' id='submitted' value='1'/>

				<div><span class='error'><?php echo $fgmembersite->GetErrorMessage(); ?></span></div>

				
				<div div class="col-xs-12 text-center">
					<br/>
    					<input type='submit' name='Submit' value='Main Menu' />
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