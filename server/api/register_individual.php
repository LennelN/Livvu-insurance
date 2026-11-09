<?php
    

// array for JSON response
$response = array();
$group_total = 200;
include 'functions.php';
$file_name='register_individual';
//error_log(strip_tags("comingin"), 0);
// check for required fields
if (isset($_POST['name']) && isset($_POST['surname']) && isset($_POST['dob']) && isset($_POST['gender']) && isset($_POST['username']) && isset($_POST['idno']) && isset($_POST['password']) && isset($_POST['country_name'])){
//error_log(strip_tags("comingin 2"), 0);
	$name = $_POST["name"];
    
	$surname = $_POST["surname"];
    
	$dob = $_POST["dob"];
    
	$gender = $_POST["gender"];
    
	$username = $_POST["username"];
 
   	$idno = $_POST["idno"];
 
	$password = $_POST["password"];

   
	$country = $_POST["country_name"];

	require_once __DIR__ . '/db_config.php';
	$db = new PDO(DB_SERVER, DB_USER,DB_PASSWORD);
    // set the PDO error mode to exception
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    	//$db = new DB_CONNECT();
	$db->beginTransaction();
	$field_val = ($username);

	$sql= "SELECT phone_number FROM tbl_clients WHERE phone_number = :field_val"; 
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
	$stmt->execute();
	$total = $stmt->rowCount();

	 // If the SQL query is succesfully performed ($result not false)
  	if($total > 0) {
		
		$success = "false";
		$message = "Phone Number ". $username ." Is Already Registered In The System.";
		array_push($response,array("success"=>$success,"message"=>$message));
		echo json_encode($response);
	}else{

				//Check whether the id number is unique
				$field_val = ($idno);

				$sql_id= "SELECT phone_number FROM tbl_clients WHERE idno = :field_val"; 
				$stmt_id = $db->prepare($sql_id);
				$stmt_id->bindParam(':field_val', $idno, PDO::PARAM_STR); 
				$stmt_id->execute();
				$total_id = $stmt_id->rowCount();
			
				if($total_id > 0)
				{
					$success = "false";
					$message = "ID Number ". $idno ." Is Already Registered In The System. Please Check Again ";
					array_push($response,array("success"=>$success,"message"=>$message));
					echo json_encode($response);
				}else{

				//Check whether the phone has already been registered as a merchant
				
				$sql= "SELECT merchant_phone_number FROM tbl_merchant_details WHERE merchant_phone_number = :field_val"; 
				$stmt = $db->prepare($sql);
				$stmt->bindParam(':field_val', $username, PDO::PARAM_STR); 
				$stmt->execute();
				$total = $stmt->rowCount();

	 // If the SQL query is succesfully performed ($result not false)
  			if($total > 0) {
		
						$success = "false";
						$message = "Phone Number ". $username. " " . " Is Registered As A Merchant In The System.";
						array_push($response,array("success"=>$success,"message"=>$message));
						echo json_encode($response);
				}else{
					try 
							{
											do
							{
							
								$string_char="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
								$group_letter=$string_char[mt_rand(0,strlen($string_char)-1)];
								$sql= "SELECT group_code,group_count,group_no FROM tbl_group_category WHERE group_code = :field_val AND group_code_active = 'Y'"; 
								$stmt_group_code = $db->prepare($sql);
								$stmt_group_code->bindParam(':field_val', $group_letter, PDO::PARAM_STR); 
								$stmt_group_code->execute();
								$total_group_code = $stmt_group_code->rowCount();
								$result = $stmt_group_code->fetchAll(PDO::FETCH_ASSOC); 
								
								if($total_group_code > 0)
									{
    										foreach($result as $row){
											$group_category = $row['group_code'];  
        										$group_count = $row['group_count'];
											$group_no = $row['group_no'];
										}	

										$group_count = $group_count + 1;

										if($group_count == $group_total)
										{
											//$group_no = $group_no + 1;
											$sql = "UPDATE tbl_group_category SET group_code_active = 'N', group_count = :field_val1 WHERE group_code = :field_val";
											// Prepare statement
    											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $group_category, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $group_total, PDO::PARAM_STR); 
											// execute the query
    											$stmt->execute();
											$total = $stmt->rowCount();
											if($total>0)
												{
													$result=true;
												}	
											//$sql = "UPDATE tbl_group_category SET group_code_active ='Y' WHERE group_no = :field_val";
											// Prepare statement
    											//$stmt = $db->prepare($sql);
											//$stmt->bindParam(':field_val', $group_no, PDO::PARAM_STR); 
											// execute the query
    															//$stmt->execute();
										}else{				
											$sql = "UPDATE tbl_group_category SET group_count = :field_val1 WHERE group_code = :field_val";
											// Prepare statement
    											$stmt = $db->prepare($sql);
											$stmt->bindParam(':field_val', $group_category, PDO::PARAM_STR); 
											$stmt->bindParam(':field_val1', $group_count, PDO::PARAM_STR); 
											// execute the query
    											$stmt->execute();
											$total = $stmt->rowCount();
											if($total>0)
												{
													$result=true;
												}	
										}
									}else{
							$qry = "SELECT count(*) FROM tbl_group_category WHERE group_code = :field_val";
										$stmt = $db->prepare($qry);
										$stmt->bindParam(':field_val', $group_letter, PDO::PARAM_STR);
        									$stmt->execute();
										$count_returned = $stmt->fetchColumn();
										if($count_returned == 1)
											{
												$result=false;
			
											}else{
											
										$qry = "SELECT MAX(group_no) FROM tbl_group_category";
										$stmt = $db->prepare($qry);
        									$stmt->execute();
										$group_no = $stmt->fetchColumn();
												
												$group_category=$group_letter;
												$group_count = 1;
												$group_code_active ='Y';
												$group_no = $group_no + 1;
												$stmt_insert = $db->prepare("INSERT INTO tbl_group_category (
														group_code,
														group_count,
														group_code_active,
														group_no) 
												VALUES(:group_code,:group_count,:group_code_active,:group_no)");
												$stmt_insert->bindParam(':group_code', $group_category);
												$stmt_insert->bindParam(':group_count', $group_count);
												$stmt_insert->bindParam(':group_code_active', $group_code_active);
												$stmt_insert->bindParam(':group_no', $group_no);
												$stmt_insert->execute();																		$total_insert =  $stmt_insert->rowCount();
												if($total_insert > 0)
												{
													$result=true;	
												}
											}
									}
											
							} while ($result == false);

													$date_joined = date('Y-m-d H:i:s');
													$member_active = 'Y';
													$date_deactivated = date('Y-m-d H:i:s');
		
													$qry = "Select country_code from tbl_country_codes where country_name = :field_val";
													$stmt = $db->prepare($qry);
													$stmt->bindParam(':field_val', $country, PDO::PARAM_STR); 
    											$stmt->execute();
													$total =  $stmt->rowCount();

 													if($total <= 0){
															$country_code = 'Unknown';
 													}else{
	    												$country_code = $stmt->fetchColumn();
													}
		
													$type_of_membership = 'Individual';
													$terms_status_flag = 'Individual';		


													$hash = sodium_crypto_pwhash_str(
    												$password,
    												SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE,
    												SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE
														);
														$dt   = new DateTime();
														$date = $dt->createFromFormat('d/m/Y', $dob);
														$newDate = $date->format('Y-m-d');

														$day=substr($dob,0,2);
														$month=substr($dob,2,2);
														$year=substr($dob,4,4);
														//$new_date= $year + "/" + $month + "/" + $day;
//$username='0772455025';
															$dd = $year."/".$month."/".$day;
							$stmt = $db->prepare("INSERT INTO tbl_clients (
													phone_number,
													name,
                											surname,
                											dob,
													idno,
													gender,
                											password,
																									date_joined,
																									date_deactivated,
																									member_active,
																									terms_status_flag,
																									type_of_membership,
																									country_name,
																									country_code,
																									group_category) 
    									VALUES (:phone_number,:name,:surname,:dob,:idno,:gender,:password,:date_joined,:date_deactivated,:member_active,:terms_status_flag,:type_of_membership,:country_name,:country_code,:group_category)");
    													$stmt->bindParam(':phone_number', $username);
															$stmt->bindParam(':name', $name);
															$stmt->bindParam(':surname', $surname);
															$stmt->bindParam(':dob', $newDate);
															$stmt->bindParam(':idno', $idno);
    													$stmt->bindParam(':gender', $gender);
															$stmt->bindParam(':password', $hash);
															$stmt->bindParam(':date_joined', $date_joined);
															$stmt->bindParam(':date_deactivated', $date_deactivated);
															$stmt->bindParam(':member_active', $member_active);
															$stmt->bindParam(':terms_status_flag', $terms_status_flag);
															$stmt->bindParam(':type_of_membership', $type_of_membership);
															$stmt->bindParam(':country_name', $country);
															$stmt->bindParam(':country_code', $country_code);
															$stmt->bindParam(':group_category', $group_category);
		
															$stmt->execute();
	
															$total =  $stmt->rowCount();
	
															if($total > 0)
																{
																		$success = "true";
																		$message = "Congratulations You Have Successfully Registered. Go Ahead And Log Into The System.";
																		array_push($response,array("success"=>$success,"message"=>$message));
																		echo json_encode($response);
																}
		
									$db->commit();
							}catch(PDOException $e){
					
							if ($e->errorInfo[1] == 1062) 
								{
									$success = "false";
		 							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 							$error_message=$e->getMessage();
		 							$error_type='duplicate';
		 							handler($error_message,$file_name,$success,$error_type);
		 							$db->rollBack();
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
								
								}else{
									$success = "false";
		 							$message = "An Error Occurred Processing Transaction. Please Try Again Later";
		 							$error_message=$e->getMessage();
		 							$error_type='exception';
		 							handler($error_message,$file_name,$success,$error_type);
		 							$db->rollBack();
									array_push($response,array("success"=>$success,"message"=>$message));
									echo json_encode($response);
									//$db->rollBack();
								}
						}
				}
	}
}	
} else {
    // required field is missing

    $success = "false";
    $message = "Required field(s) Are Missing...Please Complete All Fields";
		array_push($response,array("success"=>$success,"message"=>$message));
    // echoing JSON response
    echo json_encode($response);
}


?>
