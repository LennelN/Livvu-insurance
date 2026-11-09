<?php

include("./include/functions.php");
require_once("./include/membersite_config.php");
require_once("./include/config.php");

$country_name = trim($_POST["country_name"]);
$sql= "SELECT name_of_mobile_operator FROM tbl_mobile_operators WHERE country_of_operation = '".$country_name ."' ORDER BY 1";

echo "<select id=mobile name=mobile value=''>Enter Mobile Operator</option>"; // list box select command
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						//echo $row[name_of_mobile_operator];
						echo "<option value=$row[name_of_mobile_operator]>$row[name_of_mobile_operator]</option>"; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
?>