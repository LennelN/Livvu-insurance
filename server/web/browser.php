<?php
error_reporting(E_ALL); 
ini_set('log_errors','1'); 
ini_set('display_errors','0'); 

ini_set('error_log', 'C:/wamp64/www/livvu/logs/myerrors.log');


echo $_SERVER['HTTP_USER_AGENT'];
$browser = get_browser();
print_r($browser);
echo $browser;
error_log(strip_tags("in browser"), 0);
error_log(strip_tags( $browser), 0);
?>