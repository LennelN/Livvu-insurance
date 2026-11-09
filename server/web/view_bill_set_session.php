<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();

$bill_number = trim($_POST["bill_number"]);
$merchant_code = trim($_POST["merchant_code"]);
$merchant_name = trim($_POST["merchant_name"]);
$no_of_points = trim($_POST["no_of_points"]);


$_SESSION["bill_number"] = $bill_number;
$_SESSION["merchant_code"] = $merchant_code;
$_SESSION["merchant_name"] = $merchant_name;
$_SESSION["no_of_points"] = $no_of_points;


?>