<?php
include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();

 $_SESSION["sms_bill_number_to_report"] ="";
 $_SESSION["sms_phone_number_to_report"] ="";
 $_SESSION["sms_points_to_report"] ="";

$bill_number_to_report = trim($_POST["bill_number"]);
$phone_number_to_report = trim($_POST["phone_no"]);
$sms_points_to_report = trim($_POST["no_of_points"]);

	$_SESSION["bill_number_to_report"] =$bill_number_to_report;
 	$_SESSION["phone_number_to_report"] =$phone_number_to_report;
	$_SESSION["sms_points_to_report"] =$sms_points_to_report;
?>