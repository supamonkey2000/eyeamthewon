<style>
<?php include '/var/www/html/resources/stylesheet.css'; ?>
</style>
<?php
$page_content = file_get_contents("/var/www/content/header.html") . file_get_contents("/var/www/content/content-reviews.html");
echo $page_content;
?>
