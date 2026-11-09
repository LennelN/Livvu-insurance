<?php
    	include("./include/config.php");
	require_once("./include/membersite_config.php");
	session_start();

	$fname = $_POST['fname'];
	$_SESSION["fname_individual"] = $fname;

        $surname = $_POST['surname'];
	$_SESSION["surname_individual"] = $surname;

        $idno = $_POST['idno'];
	$_SESSION["idno_individual"]=$idno;

        $dob = $_POST['dob'];
	$_SESSION["dob_individual"]=$dob;

        $gender = $_POST['gender'];
	$_SESSION["gender_individual"]=$gender;


	$nameofcountry = $_POST['nameofcountry'];
	$_SESSION["nameofcountry_individual"]=$nameofcountry;


	$phone_number = $_POST['username'];
	$_SESSION["phone_number_individual"]=$phone_number;


        $password = $_POST['password'];
	$_SESSION["password_individual"]=$password;

	$cpassword = $_POST['cpassword'];
	$_SESSION["cpassword_individual"]=$cpassword;



?>