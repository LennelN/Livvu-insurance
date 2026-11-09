
<?php

session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
$response = array();
$file_name='process_points_purchased';

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

	$phone_number = trim($_SESSION["user_name"]);
	$transtype = trim($_SESSION["transaction_type"]);
	$transnum = trim($_SESSION["transaction_number"]);
	//$currency = trim($_POST["purchase_currency"]);
	$amount = $_SESSION["purchase_amount"];
	$purchase_by = $_SESSION["user_name"];
	$countryName = $_SESSION["country_name"];
	error_log(strip_tags("now entering ".$countryName), 0); 
	if($transtype=='Airtime'){
		$purchase_status = 'N';
	}else{
		$purchase_status = 'Y';
	}


	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    	// set the PDO error mode to exception
    	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$bill_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
	$bill_number_generated = mt_rand(0, 999999);
	$bill_number=$bill_letter.$bill_number_generated;
	$date_bill_created = date('Y-m-d H:i:s');
	$total_bill=0;
	$total_bill_amount=0;
	$transaction_id_insert=0;
	$contributionYearAmount=100;
	//$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->beginTransaction();

	//$transnum = $formvars['transnum'];

			$sql_year_contributions= "SELECT amount  FROM tbl_year_country_contributions WHERE country = :field_val"; 
			$stmt_year_contributions = $db->prepare($sql_year_contributions);
			$stmt_year_contributions->bindParam(':field_val', $countryName, PDO::PARAM_STR);  
			$stmt_year_contributions->execute();
			$total_year_contributions = $stmt_year_contributions->rowCount();

			if($total_year_contributions > 0) {

				$contributionYearAmount=$stmt_year_contributions->fetchColumn();
			}else{
				$contributionYearAmount=0;
			}



			$sql= "SELECT phone_number,group_category,country_name FROM tbl_clients WHERE phone_number = :field_val AND country_name = :field_val1"; 
			$stmt = $db->prepare($sql);
			$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR);  
			$stmt->bindParam(':field_val1', $countryName, PDO::PARAM_STR);  
			$stmt->execute();
			$total = $stmt->rowCount();

			if($total > 0) {

					$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    					foreach($result as $row)
						{    							
							$client_phone_number_returned=$row['phone_number'];
							$client_group_category_returned=$row['group_category'];
							$client_country_name_returned=$row['country_name'];
						}
			}

 
	
		$ddate = Date("Y-m-d h:i:s");
		$date_joined = date('Y-m-d H:i:s');
		$media='mobile';
		$type='Individual';
		//$currency = $formvars['currency'];
		$date_deactivated = date('Y-m-d H:i:s');
		$qry = "Select amount,type_of_currency from tbl_currency_rates where country_currency = :field_val";
		$stmt = $db->prepare($qry);
		$stmt->bindParam(':field_val', $client_country_name_returned, PDO::PARAM_STR); 
    		$stmt->execute();
		$total =  $stmt->rowCount();

 		if($total <= 0){
			$success = "false";
			$message = "Type Of Currency Does Not Exist...Please Enter Type Of Currency!";
 		}else{

			$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    			foreach($result as $row)
			{    							
				$amount_per_point=$row['amount'];
				$currency=$row['type_of_currency'];
			}
	    		//$amount_per_point = $stmt->fetchColumn();

			$no_of_points_purchased = round(($amount / $amount_per_point),0);
//error_log(strip_tags("now entering noof poiunts  ".$no_of_points_purchased), 0); 
			try{
									
				$dt   = new DateTime();
				$newDate = date('Y-m-d H:i:s');

				$sql_client_up_to_date= "SELECT MAX(due_date) FROM tbl_purchase_points_details WHERE phone_number = :field_val AND purchase_country = :field_val2"; 
				$stmt_client_up_to_date = $db->prepare($sql_client_up_to_date);
				$stmt_client_up_to_date->bindParam(':field_val', $phone_number, PDO::PARAM_STR);
				$stmt_client_up_to_date->bindParam(':field_val2', $client_country_name_returned, PDO::PARAM_STR);  
				$stmt_client_up_to_date->execute();
				$total_client_up_to_date = $stmt_client_up_to_date->rowCount();

				if($total_client_up_to_date > 0)
				{
					$client_due_date=$stmt_client_up_to_date->fetchColumn();
					$dt   = new DateTime();
					//$date_due_date = $dt->createFromFormat('d/m/Y', $client_due_date);
					//$newDueDate = $date_due_date->format('Y-m-d');
					for($i=0 ; $i<$no_of_points_purchased;$i++){
						
						if($i==0){
							
   						 	$dueDate = date('Y-m-d',strtotime($client_due_date.'+30days'));
						}else{
							//error_log(strip_tags("now entering 1i is not zero ".$i), 0); 
							$dueDate = date('Y-m-d',strtotime($dueDate.'+30days'));
						}
					}
					//$dueDate = date('Y-m-d',strtotime($client_due_date.'+30days'));

				}else{

					for($i=0 ; $i<$no_of_points_purchased;$i++){
						
						if($i==0){
							
   						 	$dueDate = date('Y-m-d',strtotime($newDate.'+30days'));
						}else{
							
							$dueDate = date('Y-m-d',strtotime($dueDate.'+30days'));
						}
					}
				}

				$stmt = $db->prepare("INSERT INTO tbl_purchase_points_details (transaction_id,
							phone_number,
							transaction_type,
                							type_of_currency,
                							purchase_amount,
							no_of_points_purchased,
							date_of_purchase,
                							time_of_purchase,
							transaction_number,
							media_used,
							due_date,
							processed_date,
							processed_by,
							purchase_status,
							purchase_done_by,
							purchase_country) 
    				VALUES (:transaction_id,:phone_number,:transaction_type,:type_of_currency,:purchase_amount,:no_of_points_purchased,:date_of_purchase,:time_of_purchase,:transaction_number,:media_used,:due_date,:processed_date,:processed_by,:purchase_status,:purchase_done_by,:purchase_country)");
							$stmt->bindParam(':transaction_id', $transaction_id_insert);
    							$stmt->bindParam(':phone_number', $phone_number);
							$stmt->bindParam(':transaction_type', $transtype);
							$stmt->bindParam(':type_of_currency', $currency);
							$stmt->bindParam(':purchase_amount', $amount);
							$stmt->bindParam(':no_of_points_purchased', $no_of_points_purchased);
    							$stmt->bindParam(':date_of_purchase', $newDate);
							$stmt->bindParam(':time_of_purchase', $ddate);
							$stmt->bindParam(':transaction_number', $transnum);
							$stmt->bindParam(':media_used', $media);
							$stmt->bindParam(':due_date', $dueDate);
							$stmt->bindParam(':processed_date', $newDate);
							$stmt->bindParam(':processed_by', $phone_number);
							$stmt->bindParam(':purchase_status', $purchase_status);
							$stmt->bindParam(':purchase_done_by', $purchase_by);
							$stmt->bindParam(':purchase_country', $client_country_name_returned);
									
							$stmt->execute();
	
							$total =  $stmt->rowCount();
	
							if($total > 0)
							{
								//$phone_number = ($formvars['phone']);
								$sql= "SELECT no_of_points,amount FROM tbl_no_of_points WHERE phone_number = :field_val AND country = :field_val1"; 
								$stmt_beneficiary = $db->prepare($sql);
								$stmt_beneficiary->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
								$stmt_beneficiary->bindParam(':field_val1', $client_country_name_returned, PDO::PARAM_STR); 
								$stmt_beneficiary->execute();
								$total_beneficiary = $stmt_beneficiary->rowCount();
								if($total_beneficiary <= 0)
								{				
									$stmt_insert = $db->prepare("INSERT INTO tbl_no_of_points (
												phone_number,
												no_of_points,
												amount,country) 
											VALUES(:phone_number,:no_of_points,:amount,:country)");
												$stmt_insert->bindParam(':phone_number', $phone_number);
												$stmt_insert->bindParam(':no_of_points', $no_of_points_purchased);
												$stmt_insert->bindParam(':amount', $amount);
												$stmt_insert->bindParam(':country', $client_country_name_returned);
												$stmt_insert->execute();
												$total_insert =  $stmt_insert->rowCount();
								}else{

									$result_beneficiary = $stmt_beneficiary->fetchAll(PDO::FETCH_ASSOC); 
    									foreach($result_beneficiary as $row)
										{    							
											$no_of_points_client_beneficiary=$row['no_of_points'];
											$amount_client_beneficiary=$row['amount'];
										}
										//$no_of_points_client_beneficiary = $stmt_beneficiary->fetchColumn();
										$no_of_points_client_beneficiary = $no_of_points_client_beneficiary + $no_of_points_purchased;
										$amount_client_beneficiary = $amount_client_beneficiary + $amount;
		                                                					$sql = "Update tbl_no_of_points Set no_of_points ='$no_of_points_client_beneficiary', amount = '$amount_client_beneficiary' Where phone_number='$phone_number' AND country ='$client_country_name_returned'";
		    	                                          				$stmt_update = $db->prepare($sql);
			                                              				$stmt_update->execute();
		    	                                                               			$total_update =  $stmt_update->rowCount();		   												}

								$sql_year= "SELECT no_of_points,amount,MAX(end_date) FROM tbl_year_contributions WHERE phone_number = :field_val AND country = :field_val1"; 
								$stmt_year_contributions = $db->prepare($sql_year);
								$stmt_year_contributions->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
								$stmt_year_contributions->bindParam(':field_val1', $client_country_name_returned, PDO::PARAM_STR); 
								$stmt_year_contributions->execute();
								$total_year_contributions = $stmt_year_contributions->rowCount();
								if($total_year_contributions <= 0)
								{
									$todayDate = date('Y-m-d');
									$endDate = date('Y-m-d',strtotime($todayDate.'+365days'));
									$stmt_insert_year = $db->prepare("INSERT INTO tbl_year_contributions (
													phone_number,
													no_of_points,
													amount,available_balance,country,start_date,end_date) 
												VALUES									(:phone_number,:no_of_points,:amount,:available_balance,:country,:start_date,:end_date)");
												$stmt_insert_year->bindParam(':phone_number', $phone_number);
												$stmt_insert_year->bindParam(':no_of_points', $no_of_points_purchased);
												$stmt_insert_year->bindParam(':amount', $contributionYearAmount);
												$stmt_insert_year->bindParam(':available_balance', $contributionYearAmount);
												$stmt_insert_year->bindParam(':country', $client_country_name_returned);
												$stmt_insert_year->bindParam(':start_date', $todayDate);
												$stmt_insert_year->bindParam(':end_date', $endDate);
												$stmt_insert_year->execute();
												$total_insert_year =  $stmt_insert_year->rowCount();

								}else{
									$todayDate = date('Y-m-d');
									$result_year_contributions = $stmt_year_contributions->fetchAll(PDO::FETCH_ASSOC); 
    									foreach($result_year_contributions as $row)
										{    							
											$points_stmt_year_contributions=$row['no_of_points'];
											$amount_stmt_year_contributions=$row['amount'];
											$end_stmt_year_contributions=$row['MAX(end_date)'];
										}

										if($todayDate > $end_stmt_year_contributions)
										{
											$endDate = date('Y-m-d',strtotime($todayDate.'+365days'));
											$stmt_insert_year = $db->prepare("INSERT INTO tbl_year_contributions (
													phone_number,
													no_of_points,
													amount,available_balance,country,start_date,end_date) 
											VALUES																			(:phone_number,:no_of_points,:amount,:available_balance,:country,:start_date,:end_date)");
												$stmt_insert_year->bindParam(':phone_number', $phone_number);
												$stmt_insert_year->bindParam(':no_of_points', $no_of_points_purchased);
												$stmt_insert_year->bindParam(':amount', $contributionYearAmount);
												$stmt_insert_year->bindParam(':available_balance', $contributionYearAmount);
												$stmt_insert_year->bindParam(':country', $client_country_name_returned);
												$stmt_insert_year->bindParam(':start_date', $todayDate);
												$stmt_insert_year->bindParam(':end_date', $endDate);
												$stmt_insert_year->execute();
												$total_insert_year =  $stmt_insert_year->rowCount();
										}
				    						
									}

							$success = "true";
							$message = "Congratulations You Have Successfully Paid Your Subscription ";
											
						}
		
						$db->commit();
									
					}catch(PDOException $e){
						if ($e->errorInfo[1] == 1062) 
						{
							
							$error_message=$e->getMessage();
							$success = "false";
							//$message = "An error occurred. Trying To Insert A Duplicate Value";
							//$message = $e->getMessage();
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							$error_message=$e->getMessage();
							$error_type='duplicate';
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later Duplicate");
							//array_push($response,array("success"=>$success,"message"=>$message));
							//echo json_encode($response);
							$db->rollBack();
						
						}else{	
	
							$success = "false";
							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
							//$this->HandleError("An Error Occurred Processing Transaction. Please Try Again Later ");
							$message=$e->getMessage();
							error_log(strip_tags($message), 0);
							$error_type='exception';
							//array_push($response,array("success"=>$success,"message"=>$message));
							//echo json_encode($response);
							//echo json_encode($response);
							$db->rollBack();
							
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
