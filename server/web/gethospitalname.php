<?php

include("./include/functions.php");
require_once("./include/membersite_config.php");
require_once("./include/config.php");

$nameofregion = trim($_POST["nameofregion"]);
$nameofcountry = trim($_POST["nameofcountry"]);

error_log(strip_tags("hgetting hospital name  is ".$nameofregion." and country name is ".$nameofcountry), 0);

$sql= "SELECT merchant_name FROM tbl_merchant_details where merchant_country  = '".$nameofcountry."' AND merchant_region =  '".$nameofregion."' AND type_of_registration = 'Health' AND status_flag = 'Y' ORDER BY 1";

echo "<select id=mhealth name=mhealth value=''>Select Hospital</option>"; // list box select command
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						//echo $row[name_of_mobile_operator];
						echo "<option value=$row[merchant_name]>$row[merchant_name]</option>"; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
?>