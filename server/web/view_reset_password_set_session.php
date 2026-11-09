<?php
    include("./include/config.php");
//var_dump($_POST);
require_once("./include/membersite_config.php");
session_start();
error_log(strip_tags("now entering reset set session  "), 0); 
$phone_number = trim($_POST["phone_number"]);
$user_country = trim($_POST["user_country"]);
error_log(strip_tags("now entering reset set session  ".$user_country." andphone no is ".$phone_number), 0); 
$_SESSION["reset_password_phone_number"] = $phone_number;
$_SESSION["reset_password_user_country"] = $user_country;


?>