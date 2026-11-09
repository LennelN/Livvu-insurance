<?php 
session_start();
$response = array();
$response_back = array();
$file_name='intdiv';
require_once("./include/membersite_config.php");
include("./include/functions.php");

$amount_per_point = 2;
$purchaseamount = 16;
$number_of_members = 5;

$a = $purchaseamount  / $number_of_members ;
error_log(strip_tags("a is ".$a), 0);

$b = intdiv($purchaseamount ,$number_of_members);
error_log(strip_tags("b is ".$b), 0);

$c = fmod($purchaseamount , $number_of_members);
error_log(strip_tags("c is ".$c), 0);

$d = $purchaseamount  % $number_of_members;
error_log(strip_tags("d is ".$d), 0);


?>