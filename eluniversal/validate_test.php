<?php
/*
url to: http://api.eluniversal.com.mx/trust/index.php
calling validate url = http://api.eluniversal.com.mx/trust/validate.php?link=http://api.eluniversal.com.mx/trust/note.php
*/
 $url = 'http://api.eluniversal.com.mx/trust/validate.php?link=http://api.eluniversal.com.mx/trust/note.php';
    $ch=curl_init();  // cURL started here
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_USERAGENT,$_SERVER['HTTP_USER_AGENT']);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
    curl_setopt ($ch, CURLOPT_HEADER, 0);
    curl_setopt ($ch, CURLOPT_NOBODY, 0);
    curl_setopt($ch, CURLOPT_TIMEOUT, 30);
    $json = curl_exec($ch);
    curl_close($ch);

$data = json_decode($json, true);

// bandage of trustproject should be printed
print($data['msghtml']);