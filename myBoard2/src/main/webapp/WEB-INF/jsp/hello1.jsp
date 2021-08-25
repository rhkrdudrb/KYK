<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
%>
<title>title</title>
</head>

<body>
<!-- <body> -->
${content}
<table width=50% border="0" cellpadding="0" cellspacing="0">
	<tr align="center" valign="middle">
		<td colspan="4">MVC 게시판</td>
		<td align=right>
			<font size=2>로그인 : ${id}님 </font>
		</td>
	</tr>
	<div onclick="Test()">
테스트
</div>
<div onclick="sessionDel()">
세션지우기
</div>
</body>
<script>
function Test() {
    var form = document.createElement("form");
    form.action = "test";
    form.method = "post";
    document.body.appendChild(form);
    form.submit();
 }
function sessionDel() {
    var form = document.createElement("form");
    form.action = "sessionDel";
    form.method = "post";
    document.body.appendChild(form);
    form.submit();
 }
</script>
</html>