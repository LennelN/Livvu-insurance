<?php
   
include 'functions.php';
$file_name='spinner_get_active_system_users';
$response = array();
//$_POST['username']='9772455025';
if (isset($_POST['username'])) {

	$username = $_POST["username"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();


	$qry = "SELECT user_login_id FROM tbl_users WHERE user_active = 'Y' AND user_login_id != :field_val AND confirmcode = 'Y' AND user_created_by = :field_val1";
	$stmt = $db->sth->prepare($qry);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->bindParam(':field_val1', $username, PDO::PARAM_STR); 
        $stmt->execute();
	$total = $stmt->rowCount();
					
					if($total > 0)
					{
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row){
							$temp["success"] = true;  
							$temp["user_login_id"] = $row['user_login_id'];
							array_push($response, $temp);
						}
					}else{
						$success = "false";
    						$message = "There Are No Active User ID To Display.";
						array_push($response,array("success"=>$success,"message"=>$message));
					}
		
    echo json_encode($response);


}else{
// required field is missing
    	$success = "false";
    	$message = "Required field(s) Are Missing";
	array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);

}
	
?>
