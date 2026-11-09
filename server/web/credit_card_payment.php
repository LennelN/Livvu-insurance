<?php
    
session_start();

require_once("./include/membersite_config.php");
include("./include/functions.php");

if ($_SERVER["REQUEST_METHOD"] == "POST") {

	$submitbutton= isset($_POST['submit']);
	$submitmenu= isset($_POST['Main']);
	//$logout
	if($submitbutton)
	{
	error_log(strip_tags("Error logging in. missing fields lapaha "), 0);
		$fgmembersite->RedirectToURL("main.php");		
	}else{
		if($submitmenu){

			$main_menu = $_SESSION["main_menu"];
			//if($main_menu == "main_livvu"){
			$fgmembersite->RedirectToURL("main.php");
			//}else{
			//	$fgmembersite->RedirectToURL("main_admin_system_maintenance.php");
			//}

		}else{
			$fgmembersite->RedirectToURL("logout.php");
		}

	}

}

?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">
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
	<script src="js/PayLane.js"></script>
     
</head>
<body>

<div id="main-wrap">
	<div id="page-wrap">
		<div><?php include("./include/declaration.php");?></div>
			

<!-- Form Code Start -->

	   
<div class="row">
<div class="col-xs-12 text-center">	
			<form id="checkout-form" action='<?php echo $fgmembersite->GetSelfScript(); ?>' method='post' accept-charset='UTF-8' type="">
    
					<!-- merchant's input elements, as many as required -->
    
					<input type="text" name="first-name" value="">
    
					<input type="text" name="last-name" value="">
    
					<input type="text" name="email" value="">
    
					<input type="text" name="address" value="">

    
					<!-- card related input elements: -->
    
					<input type="text" value="" data-paylane="cc-number">
    
					<input type="text" value="" data-paylane="cc-expiry-month">
    
					<input type="text" value="" data-paylane="cc-expiry-year">
    
					<input type="text" value="" data-paylane="cc-cvv">
    
					<input type="text" value="" data-paylane="cc-name-on-card">

    
					<input type="submit" value="submit">

					<input type='submit' name='Main' value='Main Menu' />
					<input type='submit' name='Logout' value='Logout' /><br/><br/>
			</form>

</div>
</div>


<!--
Form Code End (see html-form-guide.com for more info.)
-->
<div>   <?php include("./include/footer.php");?>
</div> 



</div>
</div>	

<script>
    
alert("la");
	try
    
	{
      
		alert("la2");  
		var client = new PayLaneClient({
            
			publicApiKey : 'YOUR_PAYLANE_PUBLIC_KEY',
            
			paymentForm  : 'checkout-form',
        
			});
    
alert("la3");
	}
    
	catch (e)
    
	{
 
		alert("la4");       
		console.log(e); // exceptions are fatal
    
	}

</script>


</body>
</html>