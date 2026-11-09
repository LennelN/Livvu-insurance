<?php

include("./include/functions.php");
require_once("./include/membersite_config.php");
require_once("./include/config.php");

$nameofcountry = trim($_POST["nameofcountry"]);

$sql= "SELECT name_of_region FROM tbl_country_regions where name_of_country  = '".$nameofcountry ."' ORDER BY 1";

echo "<select id=mregion name=mregion value=''>Select Country Region</option>"; // list box select command
					foreach ($dbh->query($sql) as $row){//Array or records stored in $row
						//echo $row[name_of_mobile_operator];
						echo "<option value=$row[name_of_region]>$row[name_of_region]</option>"; 
						/* Option values are added by looping through the array */ 
						}
					echo "</select>";// Closing of list box
?>