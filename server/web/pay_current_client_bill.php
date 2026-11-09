<?php
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$payment_details = array();
$contributing_max_number=4;
$contributing_max_reached = false;
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
			$fgmembersite->RedirectToURL("main.php");
	}else{

		$fgmembersite->RedirectToURL("logout.php");
	}

}else{

	$bill_number = ($_SESSION['bill_number']);
	$merchant_code = ($_SESSION['merchant_code']);
	$merchant_name = ($_SESSION['merchant_name']);
	$phone_no = ($_SESSION["user_name"]);
	$total_no_of_points =  $_SESSION['no_of_points'];
	$date_paid = date('Y-m-d H:i:s');
	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$payment_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$payment_number_generated = mt_rand(0, 999999);
	$payment_number=$payment_letter.$payment_number_generated;
	$transaction_type='Payment';
	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($phone_no);
	$field_val1 = ($bill_number);
if($total_no_of_points > 0){
		$sql = "SELECT DISTINCT(bill_number),merchant_phone_number,merchant_code FROM `tbl_bill_items_tariff_records` WHERE phone_number_billed = :field_val ORDER BY 1 ASC";
					
		$stmt = $db->prepare($sql);
		$stmt->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 	
		$stmt->execute();
		$total = $stmt->rowCount();
					
		if($total > 0)
		{	
			
			try
				{
					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
					foreach($result as $row){   
						$bill_number = $row['bill_number'];
						$merchant_phone_number = $row['merchant_phone_number'];
						$merchant_code = $row['merchant_code'];

					}
				
					//Get the number of points
					$sql_client_points= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
					$stmt_client_points = $db->prepare($sql_client_points);
					$stmt_client_points->bindParam(':field_val', $phone_no, PDO::PARAM_STR); 
					$stmt_client_points->execute();
					$total_client_points = $stmt_client_points->rowCount();

					if($total_client_points > 0)
					{//Dont Have Points	

						$no_of_points_client = $stmt_client_points->fetchColumn();
														
						if($no_of_points_client >= $total_no_of_points)
						{//close 1
							$no_of_points_client_balance = $no_of_points_client - $total_no_of_points;
							
							if($no_of_points_client_balance == 0 )
							{
								$sql = "DELETE from tbl_no_of_points Where phone_number='$phone_no'";
    	                                			$stmt_delete = $db->prepare($sql);
	                                    			$stmt_delete->execute();    	                                                               
                                      				$total_delete =  $stmt_delete->rowCount();
                                                
							}
							
							
							if($no_of_points_client_balance > 0)
							{
								$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_balance' Where phone_number='$phone_no'";
    	                                          		$stmt_update = $db->prepare($sql);
	                                              		$stmt_update->execute();
                                                		$total_update =  $stmt_update->rowCount();
							}

							$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
							$stmt_beneficiary = $db->prepare($sql);
							$stmt_beneficiary->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
							$stmt_beneficiary->execute();
							$total_beneficiary = $stmt_beneficiary->rowCount();
							if($total_beneficiary <= 0)
							{				
								$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
												phone_number,
												no_of_points) 
										VALUES(:phone_number,:no_of_points)");
										$stmt_insert->bindParam(':phone_number', $merchant_phone_number);
										$stmt_insert->bindParam(':no_of_points', $total_no_of_points);
										$stmt_insert->execute();																		$total_insert =  $stmt_insert->rowCount();
							}else{
									$no_of_points_client_beneficiary = $stmt_beneficiary->fetchColumn();
									$no_of_points_client_beneficiary = 													$no_of_points_client_beneficiary + $total_no_of_points;        
				$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_beneficiary' Where phone_number='$merchant_phone_number'";
    	                                          				$stmt_update = $db->prepare($sql);
	                                              				$stmt_update->execute();
                                                				$total_update =  $stmt_update->rowCount();
							}

										//Insert Into tbl_merchant_payment
										$stmt_insert = $db->prepare("INSERT INTO tbl_merchant_bill_payments (
										merchant_phone_number,
										merchant_code,
										phone_number,
										bill_number,
										no_of_points_paid,
										date_paid,
										payment_number,
										transaction_type) 
					VALUES(:merchant_phone_number,:merchant_code,:phone_number,:bill_number,:no_of_points_paid,:date_paid,:payment_number,:transaction_type)");
										$stmt_insert->bindParam(':merchant_phone_number', $merchant_phone_number);
										$stmt_insert->bindParam(':merchant_code', $merchant_code);
										$stmt_insert->bindParam(':phone_number', $phone_no);
										$stmt_insert->bindParam(':bill_number', $bill_number);
										$stmt_insert->bindParam(':no_of_points_paid', $total_no_of_points);
										$stmt_insert->bindParam(':date_paid', $date_paid);
										$stmt_insert->bindParam(':payment_number', $payment_number);
										$stmt_insert->bindParam(':transaction_type', $transaction_type);
										$stmt_insert->execute();
										$total_insert =  $stmt_insert->rowCount();
										if($total_insert > 0)
										{
											$success = "true";
								$message = "You Have Successfully Paid Bill Number ". $bill_number . " ". "For Merchant ". $merchant_name. " ". "Your Balance is ". $no_of_points_client_balance." Points. ";
								//array_push($response,array("success"=>$success,"message"=>$message));
								//echo json_encode($response);
																															}							
										

						}else{
								//error_log(strip_tags("in else points are less than total"), 0);
								//error_log(strip_tags($phone_no), 0);
								$no_of_points_client_balance = 0;
								$sql = "DELETE from tbl_no_of_points Where phone_number='$phone_no'";
    	                                          		$stmt_delete = $db->prepare($sql);
	                                              		$stmt_delete->execute();
    	                                                               
                                                		$total_delete =  $stmt_delete->rowCount();
                                                		if($total_delete > 0)//clse delete 
                                                		{
																		
									$total_no_of_points_balance = $total_no_of_points - $no_of_points_client;
										
									$no_of_points_contributed = 0;
									$no_of_points_contributed_other_groups = 0;
				    																										$sql= "SELECT no_of_points FROM tbl_no_of_points WHERE phone_number = :field_val"; 
									$stmt_beneficiary = $db->prepare($sql);
									$stmt_beneficiary->bindParam(':field_val', $merchant_phone_number, PDO::PARAM_STR); 
									$stmt_beneficiary->execute();
									$total_beneficiary = $stmt_beneficiary->rowCount();
									if($total_beneficiary <= 0)
									{				
											//do an insert
										$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
													phone_number,
													no_of_points) 
											VALUES(:phone_number,:no_of_points)");
											$stmt_insert->bindParam(':phone_number', $merchant_phone_number);
											$stmt_insert->bindParam(':no_of_points', $no_of_points_client);
											$stmt_insert->execute();
											$total_insert =  $stmt_insert->rowCount();
									}else{
											$no_of_points_client_beneficiary = $stmt_beneficiary->fetchColumn();
											$no_of_points_client_beneficiary = $no_of_points_client_beneficiary + $no_of_points_client;
		                                                			$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_beneficiary' Where phone_number='$merchant_phone_number'";
		    	                                          				$stmt_update = $db->prepare($sql);
			                                              				$stmt_update->execute();
		    	                                                               		$total_update =  $stmt_update->rowCount();		   											}
				    				}//update 2 after
		    			$stmt_insert = $db->prepare("INSERT INTO tbl_merchant_bill_payments (
											merchant_phone_number,
											merchant_code,
											phone_number,
											bill_number,
											no_of_points_paid,
											date_paid,
											payment_number,
											transaction_type) 
									VALUES					(:merchant_phone_number,:merchant_code,:phone_number,:bill_number,:no_of_points_paid,:date_paid,:payment_number,:transaction_type)");
						$stmt_insert->bindParam(':merchant_phone_number', $merchant_phone_number);
								$stmt_insert->bindParam(':merchant_code', $merchant_code);
								$stmt_insert->bindParam(':phone_number', $phone_no);
								$stmt_insert->bindParam(':bill_number', $bill_number);
								$stmt_insert->bindParam(':no_of_points_paid', $no_of_points_client);
								$stmt_insert->bindParam(':date_paid', $date_paid);
								$stmt_insert->bindParam(':payment_number', $payment_number);
								$stmt_insert->bindParam(':transaction_type', $transaction_type);
								$stmt_insert->execute();
								$total_insert =  $stmt_insert->rowCount();
								if($total_insert > 0)
								{
									$success = "true";
									$message = "You Have Successfully Paid Bill Number ". $bill_number . " ". " For Merchant ". $merchant_name. " ". "Your Balance is ". $no_of_points_client_balance." Points. ";
									//array_push($response,array("success"=>$success,"message"=>$message));
									//echo json_encode($response);
								}																																								}//update 1 after

			}else{
						$success = "false";
						$message = "You Do Not Have Points To Pay The Bill. Please Purchase Points For You To Be Able To Pay Your Bill";
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($response);
			}//dont have points
$db->commit();
			}catch(PDOException $e){
					if ($e->errorInfo[1] == 1062) 
					{																						$success = "false";													
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";
						$error_message=$e->getMessage();
						$error_type='duplicate';
						//handler($error_message,$file_name,$success,$error_type);
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($message);
						$db->rollBack();
					}else{		
						$success = "false";
						$message = "An Error Occurred Processing Transaction. Please Try Again Later";												$error_message=$e->getMessage();
						$error_type='exception';
						//handler($error_message,$file_name,$success,$error_type);
						//array_push($response,array("success"=>$success,"message"=>$message));
						//echo json_encode($message);
						$db->rollBack();
					}
		}//try

	}
}else{
$success = "false";
$message = "Your Bill Is Currently Paid Up. Thank You...!!!!";

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

				<legend>Payment Result</legend>
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