<?php
chdir('/home/user/Documents/companynamegenerator/python/ai');
$command = escapeshellcmd('./sample.py');
$output = shell_exec($command);
echo nl2br(str_replace("\\r\\n",PHP_EOL,$output));
?>
