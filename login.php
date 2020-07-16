<?php

  header('content-type: text/html; charset=utf-8'); 
  // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
  $connect=mysqli_connect("localhost", "root", "wltn2002569") or 
	die( "SQL server에 연결할 수 없습니다.");
 
  mysqli_query($connect, "SET NAMES UTF8");
  // 데이터베이스 선택
  mysqli_select_db($connect,"signdb");
 
  // 세션 시작
  session_start();
 
  $id = $_POST[u_id];
  $password1 = $_POST[u_pw];
   $sql = "SELECT IF(strcmp(password1,'$password1'),0,1) pw1
	 FROM signtb  WHERE id = '$id'";
 
  $result = mysqli_query($connect, $sql);
 
  // result of sql query
  if($result)
  {
    $row = mysqli_fetch_array($result);
    if(is_null($row[pw1]))
    {
      echo "ID를 찾을 수 없습니다.";
    }
    else
    {
      echo "$row[pw1]";
    }
  }
  else
  {
   echo mysqli_errno($connect);
  }
?>
