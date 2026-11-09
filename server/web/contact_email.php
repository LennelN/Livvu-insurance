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
        
		$name = trim($_POST['txt_name']);
        
		$fromemail = trim($_POST['txt_email']);
        
		$subject = trim($_POST['txt_subject']);
        
		$message = trim($_POST['txt_msg']);

        
		//name can contain only alpha characters and space
        
		if (!preg_match("/^[a-zA-Z ]+$/",$name))
        
		{
            
			$error = true;
            
			$name_error = "Please Enter Valid Name";
        
		}
        
		if(!filter_var($fromemail,FILTER_VALIDATE_EMAIL))
        
		{
            
			$error = true;
            
			$fromemail_error = "Please Enter Valid Email ID";
        
		}
        
		if(empty($subject))
        
		{
            
			$error = true;
            
			$subject_error = "Please Enter Your Subject";
        
		}
        
		if(empty($message))
        
		{
            
			$error = true;
            
			$message_error = "Please Enter Your Message";
        
		}
        
		if (!$error)
        
		{
            
			//send mail
            
			$toemail = "everlastingwalls1@gmail.com";            
			$subject = "Enquiry from Visitor " . $name;
            
			$body = "Here goes your Message Details: \n\n Name: $name \n From: $fromemail \n Message: \n $message";
            
			$headers = "From: $fromemail\n";
            
			$headers .= "Reply-To: $fromemail";

            
			if (mail ($toemail, $subject, $body, $headers))
                
				$alertmsg  = '<div class="alert alert-success text-center">Message sent successfully.  We will get back to you shortly!</div>';
            
			else
                
				$alertmsg = '<div class="alert alert-danger text-center">There is error in sending mail.  Please try again later.</div>';
        
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
                        
							<label for="txt_name" class="control-label">Name</label>
                    
						</p>
 
						</div>
						<div class="col-xs-9 xs-9-class-width text-center">                   
						<p>
                        
							<input class="form-control" name="txt_name" placeholder="Your Full Name" type="text" value="<?php if($error) echo $name; ?>" />
                        			<span class="text-danger"><?php if (isset($name_error)) echo $name_error; ?></span>
                    
						</p>
   
						</div>             
					</div>

                
					<div class="col-xs-12  text-center xs-12-class-width">
   
						<div class="col-xs-3 xs-3-class-width text-center">                 
						<p>
                        
							<label for="txt_email" class="control-label">Email ID</label>
                    
						</p>
                    
						</div>
						<div class="col-xs-9 xs-9-class-width text-center">
						<p>
                        
							<input class="form-control" name="txt_email" placeholder="Your Email ID" type="text" value="<?php if($error) echo $fromemail; ?>" />
                        		<span class="text-danger"><?php if (isset($fromemail_error)) echo $fromemail_error; ?></span> 
                    						</p>
  
						</div>              
					</div>

                		
							<div class="col-xs-12  text-center xs-12-class-width">
    
								<div class="col-xs-3 xs-3-class-width text-center">                
								<p>
                        
									<label for="txt_subject" class="control-label">Subject</label>
                    
								</p>
                    
								</div>
								<div class="col-xs-9 xs-9-class-width text-center">
								<p>
                        
									<input class="form-control" name="txt_subject" placeholder="Your Subject" type="text" value="<?php if($error) echo $subject; ?>" />
                        <span class="text-danger"><?php if (isset($subject_error)) echo $subject_error; ?></span>
          
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
                        
									<textarea class="form-control" name="txt_msg" rows="4" placeholder="Your Message"><?php if($error) echo $message; ?></textarea>
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