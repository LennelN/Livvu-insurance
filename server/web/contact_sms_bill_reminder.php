<?php 
    //set validation error flag as false
    $error = false;
    //check if form is submitted
    

session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");
if ($_SERVER["REQUEST_METHOD"] == "POST") {
if (isset($_POST['submit']))
    
	{
        
		$phone_number = trim($_POST['txt_phone_number']);
        
		$carrier = trim($_POST['txt_carrier']);
         
		$message = trim($_POST['txt_msg']);

        

		if(empty($phone_number))
        
		{
            
			$error = true;
            
			$phone_number_error = "Please Enter Phone Number";
        
		}
         
		if(empty($carrier))
        
		{
            
			$error = true;
            
			$carrier_error = "Please Enter Your Carrier";
        
		}
        
		if(empty($message))
        
		{
            
			$error = true;
            
			$message_error = "Please Enter Your Message";
        
		}
        
		if (!$error)
        
		{
       

			//$message = wordwrap( $_REQUEST['smsMessage'], 70 );
  
			//$to = $phone_number . '@' . $carrier;
  
			//$result = @mail( $to, '', $message );
  

			

			$mail = new PHPMailer();
 


			$mail->IsSMTP();                

			$mail->SMTPDebug  = 2;          

			$mail->SMTPAuth = true;         
			$mail->SMTPSecure = "tls";      
			$mail->Host = "smtp.gmail.com";

			$mail->Port = 587;

			$mail->Encoding = '7bit';       
			$mail->Username   = getenv("SMTP_USER"); 

			$mail->Password   = getenv("SMTP_PASSWORD");

			$mail->Subject = "";
			$mail->Body = $message;
			$mail->AddAddress( "##########@vtext.com" ); 
			//$mail->it
var_dump( $mail->send() );
			
     			$alertmsg  = '<div class="alert alert-success text-center">Message sent successfully.  We will get back to you shortly!</div>';
  
			   
		}
    
}else{


			$main_menu = $_SESSION["main_menu"];

			if($main_menu=="main_merchant"){

				$fgmembersite->RedirectToURL("main_merchant.php");

			}else if($main_menu=="main_merchant_users_accounts"){
				
				$fgmembersite->RedirectToURL("main_merchant_users_accounts.php");

			}else if($main_menu=="main_admin"){
					
				$fgmembersite->RedirectToURL("main_admin.php");

			}else if($main_menu=="main_livvu"){

				$fgmembersite->RedirectToURL("main_livvu.php");

			}else if($main_menu == "main_admin_accounts"){

				$fgmembersite->RedirectToURL("main_admin_accounts.php");

			}else if($main_menu == "main"){

				$fgmembersite->RedirectToURL("main.php");

			}else if($main_menu == "main_society"){

				$fgmembersite->RedirectToURL("main_society.php");

			}else{
				$fgmembersite->RedirectToURL("login.php");
			
			}


}


}
?>


<!DOCTYPE html>

	<html>
	
<head>
   
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Livvu Investments (Pvt) Ltd</title>
  
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
		<div><?php include("./include/declaration.php");?></div>

	<div class="row">
		<div class="col-xs-12 text-center">        
				<form role="form" class="form-horizontal" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>" method="post" name="contactform">
            
					<fieldset disabled>
				<legend>UserName:<?php date_default_timezone_set('Africa/Harare'); echo " ".$_SESSION["user_name"]; ?> - Date:<?php date_default_timezone_set('Africa/Harare'); echo " ".date('d-m-Y'); ?> <div id="clock"></div> </legend>

			</fieldset>
					<fieldset>
<div class="col-xs-12 xs-12-class-width text-center">
 
					<legend>Contact Livvu</legend>

                
</div>
					<div class="col-xs-12 xs-12-class-width text-center">
    
						<div class="col-xs-3 xs-3-class-width text-center">                
						<p>
                        
							<label for="txt_phone_number" class="control-label">Phone Number</label>
                    
						</p>
                    
						</div>
						<div class="col-xs-9 xs-9-class-width text-center">
						<p>
                        
							<input class="form-control" name="txt_phone_number" placeholder="Phone Number" type="text" 
								value="<?php 
										if($error){ echo $phone_number;}else{ echo $_SESSION["phone_number_to_report"];} ?>" />
                        			
							<span class="text-danger"><?php if (isset($phone_number_error)) echo $phone_number_error; ?></span>
                    
						</p>
   
						</div>             
					</div>

                
					<div class="col-xs-12  text-center xs-12-class-width">
    
						<div class="col-xs-3 xs-3-class-width text-center">                
						<p>
                        
							<label for="txt_email" class="control-label">Carrier</label>
                    
						</p>
                    
						</div>
						<div class="col-xs-9 xs-9-class-width text-center">
						<p>
                        
							<input class="form-control" name="txt_carrier" placeholder="Your Carrier" type="text" value="<?php if($error) echo $carrier; ?>" />
                        		<span class="text-danger"><?php if (isset($carrier_error)) echo $carrier_error; ?></span> 
                    						</p>
                
						</div>
					</div>

                		
							        
							<div class="col-xs-12  text-center xs-12-class-width">
    
								<div class="col-xs-3 xs-3-class-width text-center">                 
								<p>
                        
									<label for="txt_msg" class="control-label">Message</label>
                    
								</p>
                    
								</div>
								<div class="col-xs-9 xs-9-class-width text-center">
								<p>
                        
									<textarea class="form-control" name="txt_msg" rows="4" placeholder="Your Message"><?php if($error){ echo $message;}else{echo "Outstanding Bill No. ".$_SESSION["bill_number_to_report"]." Balance ".$_SESSION["sms_points_to_report"]." Points. Please Make Payment ";} ?></textarea>
                        <span class="text-danger"><?php if (isset($message_error)) echo $message_error; ?></span>
                    								</p>
   
								</div>             
							</div>

                
							<div class="col-xs-12 text-center xs-12-class-width">
                    
								                  
								
<input name="submit" type="submit" class="btn btn-primary" style= "text-align: center;" value="Send Message" />
                    								
								<button type="reset" name="reset" class="btn btn-primary">Reset&nbsp;<i class="icon-white icon-
arrow-right"></i></button>            
             
								
<input name="main" type="submit" class="btn btn-primary" style= "text-align: center;" value="Main Menu" />
                    								
							
							</div>
    
							        
			</fieldset>
            
		</form>
            
		<?php if (isset($alertmsg)) { echo $alertmsg; } ?>
        
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


		

</body>
</html>