<?php
   session_start();
$response = array();
$response_back = array();
$file_name='reset_merchant_account_profile';
require_once("./include/membersite_config.php");
include("./include/functions.php");


	$nameofcountry = $_POST['nameofcountry'];
	$_SESSION["nameofcountry_health"]=$nameofcountry;
	
	$mregion = $_POST['mregion'];
	$_SESSION["mregion_health"]=$mregion;      

	$mhealth = $_POST['mhealth'];
	$_SESSION["health_institution"]=$mhealth;

	
?>