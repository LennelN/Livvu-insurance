<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();

$cashed_out_transaction_no = trim($_POST["cashed_out_transaction_no"]);
$cashed_out_transaction_amount = trim($_POST["cashed_out_transaction_amount"]);

$_SESSION["cashed_out_transaction_no"] = $cashed_out_transaction_no;
$_SESSION["cashed_out_transaction_amount"] = $cashed_out_transaction_amount;


?>