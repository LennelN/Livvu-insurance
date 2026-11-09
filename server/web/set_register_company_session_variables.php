<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();

	$cname = $_POST['cname'];
	$_SESSION["cname_company"]=$cname;

        $company_reg_no = $_POST['company_reg_no'];
	$_SESSION["company_reg_no_company"]=$company_reg_no;

        $caddress = $_POST['caddress'];
    	$_SESSION["caddress_company"]=$caddress;
    
	$cdescription = $_POST['cdescription'];
	$_SESSION["cdescription_company"]=$cdescription;


	$nameofcountry = $_POST['nameofcountry'];
	$_SESSION["nameofcountry_company"]=$nameofcountry;
	
	$cregion = $_POST['cregion'];
	$_SESSION["cregion_company"]=$cregion;      

	$phone_number = $_POST['username'];
	$_SESSION["phone_number_company"]=$phone_number;


        $password = $_POST['password'];
	$_SESSION["password_company"]=$password;

	$cpassword = $_POST['cpassword'];
	$_SESSION["cpassword_company"]=$cpassword;



?>