<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="https://kit.fontawesome.com/301043e4a8.js" crossorigin="anonymous"></script>
        <title>Goods Page</title>
        <link rel="icon" type="image/png" href="image/icia-logo.png">
        <link rel="stylesheet" href="css/style_goods.css">
    </head>
    <body onLoad ="init()" >
    	
        <!-- Header -->
	    <header id="header" >
	        <div class="search">
	            <span class="search__logo">
	                <a href="https://www.icia.co.kr/"><img id="logo2" src="image/icia-logo2.png"></a>
	            </span>            
	              
	            <span class="search__input">
	                <input type="text" name="word" id="word" >
	                <button type="button" onclick="searchGoods()" id="button"><i class="fas fa-search"></i></button> 
	                <input type="button" onclick="sendLogOut()" value="로그아웃">
	            </span>
	        </div>
	    </header>   
	    
        <!-- Section -->                
        <section id="section">
          
        </section>   
         ${gList}
         
        <!-- Footer -->
        <footer id="footer">
            <span class="footer__icon"><a href="https://www.icia.co.kr/"><img id="footer__icon" src="image/icia-logo.png" alt=""></a></span>
            <span class="footer__rights">Copyright <b>Sookyeong Lee.</b> All Rights Reserved.</span>
        </footer>
    </body>
    
    
    <script>
    function sendLogOut(){
    	   // Dynamic Element Creation
    	   var form = document.createElement("form");
    	   form.action = "LogOut";
    	   form.method = "post";
    	   
    	   document.body.appendChild(form);
    	   form.submit();
    	}
    	function init(){
    		var msg= "${message}";
    		if(msg !=""){
    			alert(msg);
    		}
    		
    	}
     	function searchGoods(){//get으로 바꾸기 
				var f=document.createElement("form");
	    		f.action = "Search";
	    		f.method = "post";
	    	
	    		f.appendChild(document.getElementsByName("word")[0]);
	    		document.body.appendChild(f);
	    		f.submit();
	}	
    	function goDetail(code){	
    		var splitCode = code.split(":");
    		
    		var form = document.createElement("form");
    		form.action="GoodsDetail";
    		form.method="post";
    		form.target = "_blank";//새로운창을 열어라 
    			      	
        	//서버전송 데이터 -> gocode, secode 2개
    		for(index=0; index<splitCode.length; index++){
    			var input =document.createElement("input"); //객체생성 
        		input.name = "code";
        		input.type = "hidden";
        		input.value = splitCode[index];
        		
    			form.appendChild(input);//form 안에 input을 넣을꺼야 
    		}   
    		document.body.appendChild(form);
    		
    		form.submit();
    	}
    	
    </script>
    </html>