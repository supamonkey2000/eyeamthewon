<?php
require_once "/var/www/html/resources/mobile-detect/Mobile_Detect.php";
?>

<style>
<?php
$detect = new Mobile_Detect;
if ( $detect->isMobile() ) {
	include 'resources/stylesheet-mobile.css';
} else {
	include 'resources/stylesheet.css';
}
?>
</style>
<?php
$page_content = file_get_contents("/var/www/content/header.html") . file_get_contents("/var/www/content/content-404.html") . file_get_contents("/var/www/content/footer.html");
echo $page_content;
?>
