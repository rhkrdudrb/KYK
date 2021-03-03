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
<body>
    <!-- Header -->
    <header id="header">
        <h1><a href="https://www.icia.co.kr/"><img src="image/icia-logo.png" alt="어서와~~~"></a></h1>
    </header>
    <!-- Secion -->
    <section id="section">
        <form action="FindForm" method="post">
            <div class="id">
                <h3 class="join__title"><label for="id">이름을 입력해주세요</label></h3>
                <span class="input__space"><input type="text" name="accessInfo" id="id"  title="ID" ></span>
            </div>
            <div class="pw">
                <h3 class="join__title"><label for="password">폰번호를적어주세요</label></h3>
                <span class="input__space"><input type="text" name="accessInfo" id="password" ></span>
            </div>
            	<!--아이디는 :${message}입니다-->
			<div id="message"></div>
            <div class="submit">
                <input  type="submit" value="아이디찾기"  >  
                <input  type="button"  onClick="init()" value="찾은 아이디 보기를 원한다면  여기를 클릭!"  >
            </div>
        </form>
        <div class="submit">
		
		 </div>
		<a href="main.jsp">메인으로</a>
    </section>
    <!-- Footer -->
    <footer id="footer">
        <span class="footer__icon"><a href="https://www.icia.co.kr/"><img src="image/icia-logo.png" alt="" id="footer__icon"></a></span>
        <span class="footer__rights">Copyright <b>ICIA.</b> All Rights Reserved.</span>
    </footer>
</body>
<script>
	

	function init(){
		var message = "${message}";
		alert(message);
	}
	
</script>
</html>