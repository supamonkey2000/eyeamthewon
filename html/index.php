<style>
<?php include 'resources/stylesheet.css'; ?>
</style>
<?php
$page_content = file_get_contents("/var/www/content/header.html") . file_get_contents("/var/www/content/content-main.html") . file_get_contents("/var/www/content/footer.html");
echo $page_content;
?>
