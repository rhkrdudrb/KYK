<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
.box {
    width: 150px;
    height: 150px;
    border-radius: 70%;
    overflow: hidden;
}
.profile {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<b> 주문자 정보 </b> <br>
${mName } <br>
${mPhone } <br>

<br>

${orderList }


${message }


</body>

<script>

function order(orderInfo) {
	var form = document.createElement("form");
	form.action = "OrderComplete?orderInfo="+orderInfo;
	form.method = "post";
	document.body.appendChild(form);
	form.submit();
	alert("주문이 완료되었습니다.");

}
</script>
</html>