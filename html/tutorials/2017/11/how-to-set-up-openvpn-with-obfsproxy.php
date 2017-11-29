<style>
<?php include '/var/www/html/resources/stylesheet.css'; ?>
</style>
<?php
$page_content = file_get_contents("/var/www/content/header.html") . file_get_contents("/var/www/content/tutorials/how-to-set-up-openvpn-with-obfsproxy.html");
echo $page_content;
?>
