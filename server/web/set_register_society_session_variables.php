<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();

	$sgname = $_POST['sgname'];
	$_SESSION["sgname_society"]=$sgname;

        $sgno = $_POST['sgno'];
	$_SESSION["sgno_society"]=$sgno;

        $sgaddress = $_POST['sgaddress'];
    	$_SESSION["sgaddress_society"]=$sgaddress;
    
	$sgdescription = $_POST['sgdescription'];
	$_SESSION["sgdescription_society"]=$sgdescription;


	$nameofcountry = $_POST['nameofcountry'];
	$_SESSION["nameofcountry_society"]=$nameofcountry;
	
	$sgregion = $_POST['sgregion'];
	$_SESSION["sgregion_society"]=$sgregion;      

	$phone_number = $_POST['username'];
	$_SESSION["phone_number_society"]=$phone_number;


        $password = $_POST['password'];
	$_SESSION["password_society"]=$password;

	$cpassword = $_POST['cpassword'];
	$_SESSION["cpassword_society"]=$cpassword;



?>