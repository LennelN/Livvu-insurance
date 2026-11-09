<?php
    	include("./include/config.php");
	require_once("./include/membersite_config.php");
	session_start();

	$fname = $_POST['fname'];
	$_SESSION["fname_doctor"] = $fname;

        $surname = $_POST['surname'];
	$_SESSION["surname_doctor"] = $surname;

        $idno = $_POST['idno'];
	$_SESSION["idno_doctor"]=$idno;

        $dob = $_POST['dob'];
	$_SESSION["dob_doctor"]=$dob;

        $gender = $_POST['gender'];
	$_SESSION["gender_doctor"]=$gender;


	$phone_number = $_POST['username'];
	$_SESSION["phone_number_doctor"]=$phone_number;

$profession = $_POST['profession'];
	$_SESSION["profession_doctor"]=$profession;


        $licenceno = $_POST['licenceno'];
	$_SESSION["licence_no_doctor"]=$licenceno;



?>