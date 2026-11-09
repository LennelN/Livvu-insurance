<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();


$tariff_code_session = trim($_POST["tariff_code"]);
$tariff_description_session = trim($_POST["tariff_description"]);
$tariff_price_session = trim($_POST["tariff_price"]);
$tariff_category_session = trim($_POST["tariff_category"]);
$tariff_type_session = trim($_POST["tariff_type"]);


$_SESSION["tariff_code"] = $tariff_code_session;
$_SESSION["tariff_description"] = $tariff_description_session;
$_SESSION["tariff_price"] = $tariff_price_session;
$_SESSION["tariff_category"] = $tariff_category_session;
$_SESSION["tariff_type"] = $tariff_type_session;


?>