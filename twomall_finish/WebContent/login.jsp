<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <meta name="description" content="Login Page">
    <meta name="author" content="Sookyeong">
    <link rel="icon" type="image/png" href="icia-logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
    <!-- Header -->
    <header id="header">
        <h1><a href="https://www.icia.co.kr/"><img src="image/icia-logo.png" alt="어서와~~~"></a></h1>
    </header>
    <!-- Secion -->
    <section id="section">
        <form action="LogIn" method="post" onSubmit="return addParam(this)">
            <div class="id">
                <h3 class="join__title"><label for="id">아이디</label></h3>
                <span class="input__space"><input type="text" name="accessInfo" id="id" minlength="3" maxlength="10" title="ID" required></span>
            </div>
            <div class="pw">
                <h3 class="join__title"><label for="password">패스워드</label></h3>
                <span class="input__space"><input type="password" name="accessInfo" id="password" minlength="4" maxlength="10" title="ë¹ë°ë²í¸ ìë ¥" required></span>
            </div>
			<div id="message">${message }</div>
            <div class="submit">
                <button type="submit" id="submit">로그인</button>
                <a href="find.jsp">아이디찾기</a>
            	<a href="find2.jsp">비밀번호 찾기</a>
            </div>
        </form>
        <span onClick="movePage(true)">[메인으로]</span>
    </section>
    <!-- Footer -->
    <footer id="footer">
        <span class="footer__icon"><a href="https://www.icia.co.kr/"><img src="image/icia-logo.png" alt="" id="footer__icon"></a></span>
        <span class="footer__rights">Copyright <b>ICIA.</b> All Rights Reserved.</span>
    </footer>
</body>
<script>

function addParam(form){
	form.action += "?action=${param.action}&gInfo=${paramValues.gInfo[0]}&gInfo=${paramValues.gInfo[1]}&gInfo=${paramValues.gInfo[2]}&gInfo=${paramValues.gInfo[3]}" 
	if(gCode != ""){
		
	}
	return true;
}


function movePage(selection){
	var form = document.createElement("form");
	
	form.action = "${prev}";
	form.method = "post";
	
	// Current Page정보
	var page = document.createElement("input");
	page.type = "hidden";
	page.name = "page";
	page.value = "LogInForm";
	form.appendChild(page);
	
	document.body.appendChild(form);
	form.submit();
}
</script>
</html>