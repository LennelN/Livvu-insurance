<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();

$purchase_transaction_no = trim($_POST["purchase_transaction_no"]);
$mobile_phone_no= trim($_POST["mobile_phone_no"]);
$_SESSION["purchase_transaction_no"] = $purchase_transaction_no;
$_SESSION["mobile_phone_no"] = $mobile_phone_no;

?>