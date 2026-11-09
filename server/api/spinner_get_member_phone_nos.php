<?php
   
include 'functions.php';
$file_name='spinner_get_member_phone_nos';
$response = array();
//$_POST['username']='0779241029';
//$_POST['merchantCode']='267265';
if (isset($_POST['username']) && isset($_POST['merchantCode'])) {
	$username = $_POST["username"];
	$merchantCode = $_POST["merchantCode"];

	require_once __DIR__ . '/db_connect.php';


	// connecting to db
    	$db = new DB_CONNECT();

	$sql= "SELECT tbl_society_company_members.member_phone_number,tbl_clients.name,tbl_clients.surname FROM tbl_society_company_members,tbl_clients WHERE tbl_society_company_members.member_phone_number = tbl_clients.phone_number AND merchant_phone_number = :field_val AND merchant_code = :field_val1"; 
					$stmt = $db->sth->prepare($sql);
					$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
					$stmt->bindParam(':field_val1', $merchantCode, PDO::PARAM_STR); 
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0){

							$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    							foreach($result as $row){
								$temp["success"] = true;  
        
								$member_phone_number = $row['member_phone_number'];
  
								$name = $row['name'];
  
								$surname = $row['surname'];
  
								$client_name = $member_phone_number." ".$name." ".$surname;
								$temp["phone_number"] = $member_phone_number;

								$temp["client_name"] = $client_name;
  
								array_push($response, $temp);
							}
		
					}else{

							$success = "false";
    							$message = "There Are No Phone Numbers To Display.";
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
