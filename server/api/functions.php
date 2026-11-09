<?php
error_reporting(E_ALL); 
ini_set('log_errors','1'); 
ini_set('display_errors','0'); 

ini_set('error_log', 'C:/wamp64/www/phila/logs/phila-errors.log');

define('E_FATAL',  E_ERROR | E_USER_ERROR | E_PARSE | E_CORE_ERROR | 
        E_COMPILE_ERROR | E_RECOVERABLE_ERROR);

define('ENV', 'production');

//Custom error handling vars
define('DISPLAY_ERRORS', FALSE);
define('ERROR_REPORTING', E_ALL | E_STRICT);
define('LOG_ERRORS', TRUE);

register_shutdown_function('shut');

set_error_handler('handler');

//Function to catch no user error handler function errors...
function shut(){

    $error = error_get_last();

    if($error && ($error['type'] & E_FATAL)){
        handler($error['type'], $error['message'], $error['file'], $error['line']);
    }

}

function handler( $errstr, $errfile, $result,$errtype ) {

    $message = '<b>'.$errstr.': </b>'.$errfile.' in <b>'.$result.'</b> on line <b>'.$errtype.'</b><br/>';

    //if(($errno & E_FATAL) && ENV === 'production'){

      //  header('Location: 500.html');
      //  header('Status: 500 Internal Server Error');

   // }

    //if(!($errno & ERROR_REPORTING))
   //     return;

    //if(DISPLAY_ERRORS)
    //    printf('%s', $message);

    //Logging error on php file error log...
    if(LOG_ERRORS)
        error_log(strip_tags($message), 0);

}

ob_start();

ob_end_flush();


?>