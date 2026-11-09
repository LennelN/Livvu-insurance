
require_once("./include/class.phpmailer.php");
$mail = new PHPMailer();
 


$mail->IsSMTP();                

$mail->SMTPDebug  = 2;          

$mail->SMTPAuth = true;         
$mail->SMTPSecure = "tls";      
$mail->Host = "smtp.gmail.com";

$mail->Port = 587;

$mail->Encoding = '7bit';       
$mail->Username   = getenv("SMTP_USER"); 

$mail->Password   = getenv("SMTP_PASSWORD");

$mail->Subject = "Testing";

$mail->Body = "Testing";

$mail->AddAddress( "##########@vtext.com" ); 
it
var_dump( $mail->send() ); 
