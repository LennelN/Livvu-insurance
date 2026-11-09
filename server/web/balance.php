<?PHP
require_once("./include/membersite_config.php");
include("./include/functions.php");
$message="";

if(isset($_POST['submitted']))
{
   if($fgmembersite->Login())
   {
	
	if(isset($_SESSION["user_id"])) {
		if(!isLoginSessionExpired()) {
        		$fgmembersite->RedirectToURL("main.php");
		}else{
			header("Location:logout.php?session_expired=1");
		}
	}
   }
}

if(isset($_GET["session_expired"])) {
	$message = "Login Session is Expired. Please Login Again";
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
</head>
<body>

<div id="main-wrap">
	<div id="page-wrap">
		<div><?php include("./include/declaration.php");?></div>
			
		<div>
<?php include("./include/menu.php");?>
</div>

<div>   <?php include("./include/footer.php");?>
</div>
</div>
</div>	
</body>
</html>