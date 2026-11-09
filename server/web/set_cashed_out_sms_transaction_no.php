<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();
$_SESSION["cashed_out_transaction_no"] = "";
$_SESSION["sms_points_to_remind"] ="";

$cashed_out_transaction_no = trim($_POST["cashed_out_transaction_no"]);
$sms_points_to_remind = trim($_POST["no_of_points"]);

$_SESSION["cashed_out_transaction_no"] = $cashed_out_transaction_no;
$_SESSION["sms_points_to_remind"] =$sms_points_to_remind;

?>