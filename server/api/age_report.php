<?php
   
include 'functions.php';
$file_name='bill_get_demographic_report';
error_log(strip_tags("ola"), 0);
$response = array();

	require_once __DIR__ . '/db_connect.php';
	// connecting to db
    	$db = new DB_CONNECT();


			$sql="SELECT phone_number,dob,DateDiff(CurDate(),tbl_clients.dob) As Age FROM tbl_clients WHERE member_active = 'Y'";	
					$stmt = $db->sth->prepare($sql);	
					$stmt->execute();
					$total = $stmt->rowCount();
					
					if($total > 0)
					{					
						$result = $stmt->fetchAll(PDO::FETCH_ASSOC); 
    						foreach($result as $row)
							{    							
								$temp["success"] = true;  
								$gender = $row['phone_number'];
								$age = $row['Age'];
								echo $age;
echo 't';
								$dob = $row['dob'];
								$temp["category"] = $gender;
								$temp["count"] = $yearInAge;
								$temp["dob"] = $dob;
								$message='Successful';
								$temp["message"] = $message;
								array_push($response, $temp);
							}
					}
								//error_log(strip_tags($response), 0);
						echo json_encode($response);
							
?>
