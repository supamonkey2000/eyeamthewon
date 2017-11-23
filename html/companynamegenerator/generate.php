<?php
$command = escapeshellcmd('/home/user/Documents/companynamegenerator/get.sh');
$output = shell_exec($command);
echo $output;
?>
