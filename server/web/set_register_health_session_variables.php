<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();

	$hname = $_POST['hname'];
	$_SESSION["hname_health"]=$hname;

        $licence_no = $_POST['licence_no'];
	$_SESSION["licence_no_health"]=$licence_no;

        $maddress = $_POST['maddress'];
    	$_SESSION["maddress_health"]=$maddress;
    
	$mdescription = $_POST['mdescription'];
	$_SESSION["mdescription_health"]=$mdescription;

	$msector = $_POST['msector'];
	$_SESSION["msector_health"]=$msector;


	$nameofcountry = $_POST['nameofcountry'];
	$_SESSION["nameofcountry_health"]=$nameofcountry;
	
	$mregion = $_POST['mregion'];
	$_SESSION["mregion_health"]=$mregion;      

	$phone_number = $_POST['username'];
	$_SESSION["phone_number_health"]=$phone_number;


        $password = $_POST['password'];
	$_SESSION["password_health"]=$password;

	$cpassword = $_POST['cpassword'];
	$_SESSION["cpassword_health"]=$cpassword;


?>