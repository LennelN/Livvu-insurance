<?PHP
session_start();
require_once("./include/membersite_config.php");
$message="";

if(isset($_GET["session_expired"])) {
	$message = "Login Session is Expired. Please Login Again";
}

if(isset($_GET["session_new_created"])) {
	$message = "Login Session Created On A New Tab. Please Use Only One Session At A Time";
}


	$session_id = $_SESSION["session_id"];

$fgmembersite->LogOut();
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">
<head>
      <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>
      <title>Logout</title>
      <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="css/bootstrap-responsive.css">
	<link rel="STYLESHEET" type="text/css" href="css/fg_membersite.css" />
  	<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>

<div id="main-wrap">
	<div id="page-wrap">

<div class="row">
		<div class="col-xs-12 text-center">    

<h2>You have logged out</h2>
<p>
<br>
Goodbye <?= $fgmembersite->UserFullName(); ?>!
<br>
<br>
<?php if($message!="") { ?>
<div class="message"><?php echo $message; ?></div>
<?php } ?>
<label><a style="font-size: 18px;font-style: bold;color: black;" href='login.php'>Login Again</a></label>
</p>
</div>
</div>

<?php
// remove all session variables
session_unset(); 

// destroy the session 
session_destroy(); 
?>

</div>
</div>

</body>
</html>