<?php
   
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");

$file_name='bill_get_current_client_statement_merchant';

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
	
		$fgmembersite->RedirectToURL("merchant_reports_menu.php");

	}else{
		
			$fgmembersite->RedirectToURL("logout.php");
	}

}else{



	//$bill_number = ($_SESSION['bill_number']);
	$phone_number = ($_SESSION['bill_statement_phone_no']);
	$report_country_phone_no = $_SESSION["report_country_phone_no"] ;
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
    	$db = new DB_CONNECT();


$dataRow = "";
$dataRow2 = "";
$dataRow3 = "";
$dataRow4 = "";
$num=0;
$sql="Select transaction_date,tariff_code,tariff_description,tariff_total_amount,0 As no_of_points_paid,phone_number_billed,bill_number FROM tbl_bill_tariff_merchant_records WHERE phone_number_billed  = :field_val AND  phone_number_billed_country  = :field_val1 ORDER BY 1";
//$sql = "SELECT transaction_date, tariff_code, tariff_description, no_of_points, 0 As no_of_points_paid, phone_number_billed,bill_number FROM tbl_bill_items_tariff_records WHERE //phone_number_billed = :field_val ORDER BY 1 ASC";

				
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $phone_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $report_country_phone_no, PDO::PARAM_STR); 
					
					$stmt->execute();
					$total = $stmt->rowCount();
					
					$total_no_of_points = 0;
					$total_no_of_points_paid = 0;
					if($total > 0)
					{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){    							
 
												
												$date_of_bill = $row['transaction_date'];
												$timestamp = strtotime($date_of_bill); 
												$new_date = date('d-m-Y', $timestamp);
												$transaction_date = $new_date;
												$tariff_code = $row['tariff_code'];
												$tariff_description = $row['tariff_description'];
												$no_of_points = $row['tariff_total_amount'];
												$no_of_points_paid = $row['no_of_points_paid'];
												$bill_number = $row['bill_number'];
												$total_no_of_points = $total_no_of_points + $no_of_points;
												$message='Successful';

												$num=$num + 1;
$dataRow = $dataRow."<tr><td>$num</td><td>$transaction_date</td><td>$tariff_code</td><td>$tariff_description</td><td>$no_of_points</td><td>$no_of_points_paid</td></tr>";		
												
												
								}
$dataRow2 = "";
	$sql = "SELECT date_paid,'',transaction_type,0,amount_paid,phone_number from tbl_merchant_bill_payments where bill_number = :field_val1 AND country = :field_val2 ORDER BY 1";
						$stmt = $db->sth->prepare($sql);
					
						$stmt->bindParam(':field_val1', $bill_number, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val2', $report_country_phone_no, PDO::PARAM_STR); 
						$stmt->execute();
						$total = $stmt->rowCount();
					
						if($total > 0)
						{					
								$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row){    							
    										//$success = true;
												//$temp["success"] = true;  
												$date_of_payment = $row['date_paid'];
												$timestamp = strtotime($date_of_payment); 
												$new_date_payment = date('d-m-Y', $timestamp);
												$transaction_payment_date = $new_date_payment;
												$tariff_code = $row['phone_number'];
												$tariff_description = $row['transaction_type'];
												$no_of_points = 0;
												$no_of_points_paid = $row['amount_paid'];
												$total_no_of_points_paid = $total_no_of_points_paid + $no_of_points_paid;
												//$message='Successful';
												//$temp["message"] = $message;
												//array_push($response, $temp);

											$num=$num + 1;	
$dataRow2 = $dataRow2."<tr><td>$num</td><td>$transaction_payment_date</td><td>$tariff_code</td><td>$tariff_description</td><td>$no_of_points</td><td>$no_of_points_paid</td></tr>";			
												
								}
						}		
						
$dataRow3 = "";
						$num = '';
						
						$tariff_code = '';
						$tariff_description = '';

						$dataRow3 = $dataRow3."<tr><td>$num</td><td>Total</td><td>$tariff_code</td><td>$tariff_description</td><td>$total_no_of_points</td><td>$total_no_of_points_paid</td></tr>";

					$balance = $total_no_of_points - $total_no_of_points_paid;
					//$todays_date = new DateTime('Y-m-d');
					$todays_date_string = date('Y-m-d H:i:s');
					$todays_date = new DateTime($todays_date_string);
					

					$bill_transaction_date = $transaction_date;
					

					$timestamp = strtotime($bill_transaction_date); 
					$new_date = date('Y-m-d', $timestamp);

					//$new_date = date("Y-m-d", $bill_transaction_date);
					
					$new_bill_date = new DateTime($new_date);
					
					$no_of_days = $todays_date->diff($new_bill_date)->format("%a");
					
				
					$balance_owing = $total_no_of_points - $total_no_of_points_paid;
				
$no120days = 0;
					$no90days = 0;
					$no60days = 0;
					$no30days = 0;
					$Owing = 0;

					if ($no_of_days >= 120)
						{
							$no120days = $balance_owing;
						}else{
							if(($no_of_days >= 90) && ($no_of_days < 120))
							{
								$no90days = $balance_owing;
							}else{
								if(($no_of_days >= 60) && ($no_of_days < 90))
								{
									$no60days = $balance_owing;
								}else{
									if(($no_of_days >= 30) && ($no_of_days < 60))
									{
										$no30days = $balance_owing;
									}else{

										$Owing = $balance_owing;
									}

								}


							}

						}
							

				

					$dataRow4 = "";
					/*$no120days = 0;
					$no90days = 0;
					$no60days = 0;
					$no30days = 0;
					$Owing = 0;*/

			$dataRow4 = $dataRow4."<tr><td>$no120days</td><td>$no90days</td><td>$no60days</td><td>$no30days</td><td>$Owing</td></tr>";

					}else{
						
								$success = "false";
								$message = "You Have No Current Outstanding Bills To View For ".$phone_number;
								//array_push($response,array("success"=>$success,"message"=>$message));
								//echo json_encode($response);
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
            						<th class="text-center">Date</th>
            						<th class="text-center">Code</th>
            						<th class="text-center">Description</th>
							<th class="text-center">Debit</th>
							<th class="text-center">Credit</th>
        					</tr>
    					</thead> 
					<tbody>
        					<?php echo $dataRow;?>
						<?php echo $dataRow2;?>
						<?php echo $dataRow3;?>
					</tbody>
				</table>

				<table id="table2" class="col-xs-12 text-center" >
    					<thead>
        					<tr>
							<th class="text-center">120days+</th>
            						<th class="text-center">90days</th>
            						<th class="text-center">60days</th>
            						<th class="text-center">30days</th>
							<th class="text-center">Owing</th>
        					</tr>
    					</thead> 
					<tbody>
        					<?php echo $dataRow4;?>
					</tbody>
				</table>

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

<div>   <?php include("./include/footer.php");?>
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


<script>
function getRowData(id_value)
{

var sel1 = document.getElementById('table1')
var bill_no = sel1.rows[id_value].cells[4].innerHTML;
var merchant_code = sel1.rows[id_value].cells[3].innerHTML;
var merchant_name = sel1.rows[id_value].cells[2].innerHTML;
var no_of_points = sel1.rows[id_value].cells[5].innerHTML;

var b2 = bill_no
alert(b2);

alert(bill_no);
alert(merchant_code);
alert(merchant_name);

if (merchant_code.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "pay_bill_set_session.php",
            data: 'bill_number='+bill_no+'&merchant_name='+merchant_name+'&merchant_code='+merchant_code+'&no_of_points='+no_of_points,
            cache: false,
        });
    } 

window.location.href="confirm_payment.php";

}

function viewRowData(id_value)
{

var sel1 = document.getElementById('table1')
var bill_no = sel1.rows[id_value].cells[4].innerHTML;
var merchant_code = sel1.rows[id_value].cells[3].innerHTML;

if (merchant_code.length > 0 ) {

     $.ajax({
            type: "POST",
            url: "view_bill_set_session.php",
            data: "bill_number="+bill_no,
            cache: false,
        });
    } 

window.location.href="bill_get_client_tariff_details.php";


}


</script>


</body>
</html>