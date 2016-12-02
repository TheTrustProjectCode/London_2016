<?php

/*
* Validate is a "content" is available to be "trustable"
* Content must be prepared to be tested with the presence of metatags
* Being valid allows to publishers to include a "possible" trustable reference.
*
*/

include ("phpQuery.php");
    $output = array("status" =>FALSE,"msg"=>"Content unreachable", "msghtml"=>"<div class='trustproject_output'>Content unreachable</div>");

if (isset($_REQUEST['link'])){


    $url = $_REQUEST['link'];
    $ch=curl_init();  // cURL started here
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_USERAGENT,$_SERVER['HTTP_USER_AGENT']);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
    curl_setopt ($ch, CURLOPT_HEADER, 0);
    curl_setopt ($ch, CURLOPT_NOBODY, 0);
    curl_setopt($ch, CURLOPT_TIMEOUT, 30);
    $file = curl_exec($ch);
    curl_close($ch);


//    $results=json_decode($kv_json,true);
    $html = phpQuery::newDocumentHTML($file, $charset = 'utf-8');
      $metaItems = [];
        foreach (pq('meta') as $keys) {
            $names = pq($keys)->attr('property');
            if (!empty($names) && trim($names)) {
//                array_push($metaItems, $names);
                $metaItems[$names]=$names;
            }
        }

//        print_r($metaItems);
    if(isset($metaItems["ot:sourcename"]) && $metaItems["ot:sourcename"] !=="" && isset($metaItems["ot:sources"]) && $metaItems["ot:sources"] !== "" ){
            $output = array("status" =>TRUE,"msg"=>"Trustable content", "msghtml"=>"<div class='trustproject_output'><img src='https://res.cloudinary.com/marvel-content/image/fetch/s--kzHzrxon--/t_app_icon_120/https://marvelapp-live.storage.googleapis.com/serve/3af6efee9a6441f88ff5292370296f12.png' width='30px' height='30px' /></div>");

        }
    else {
          $output = array("status" =>FALSE,"msg"=>"No data", "msghtml"=>"<div class='trustproject_output'>No data</div>");
    }

}
else {

    $output = array("status" =>FALSE,"msg"=>"Content unreachable", "msghtml"=>"<div class='trustproject_output'>Content unreachable</div>");
}

// json output
header('Content-Type: application/json');
echo json_encode($output);