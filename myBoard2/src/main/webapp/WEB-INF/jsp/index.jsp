<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="my.impl.MyDTO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>title</title>
</head>

<body>
<div onclick="Test()">
테스트
</div>
<input type = "text" name="id" >
<input type = "text" name="pw" >
<input type = "button" value = "확인" onclick="login()">
</body>
<script>
function login(){
	
	var form = document.createElement("form");
	form.action = "login";
    form.method = "post";
    //id넘겨주기
 	var id = document.createElement("input");
	id.value = document.getElementsByName("id")[0].value;
	id.type = "hidden";
	id.name = "id";
	form.appendChild(id);
    //pw넘겨주기
    var pw = document.createElement("input");
    pw.value = document.getElementsByName("pw")[0].value;
    pw.type = "hidden";
    pw.name = "pw";
    form.appendChild(pw);
    document.body.appendChild(form);
    form.submit();
}
function Test() {
    var form = document.createElement("form");
    form.action = "test.do";
    form.method = "post";
    document.body.appendChild(form);
    form.submit();
 }
</script>
</html>