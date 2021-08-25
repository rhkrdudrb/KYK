<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>title</title>
</head>

<body>
<div onclick="Test()">테스트</div>
	<input type = "text" name="id" >
	<input type = "text" name="pw" >
	<input type = "button" value = "확인" onclick="login()">
	
	<textarea id="freeContext" name="freeContext"></textarea>
	<input type = "button" value = "에디터 테스트" onclick="upload()">
	 
</body>
<script type="text/javascript" src="./ck/ckeditor/ckeditor.js" ></script>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	$(function() {
	CKEDITOR.replace('freeContext',
					{//해당 이름으로 된 textarea에 에디터를 적용
					width : '50%',
					height : '400px',
				   filebrowserImageUploadUrl : '/imageUpload/' //여기 경로로 파일을 전달하여 업로드 시킨다.
				});
				CKEDITOR.on('dialogDefinition', function(ev) {
					var dialogName = ev.data.name;
					var dialogDefinition = ev.data.definition;

					switch (dialogName) {
					case 'image': //Image Properties dialog
						//dialogDefinition.removeContents('info');
						dialogDefinition.removeContents('Link');
						dialogDefinition.removeContents('advanced');
						break;
					}
				});

			});
</script>
<script>
function upload() {
    var form = document.createElement("form");
    
    var content = document.createElement("input");
    content.value = CKEDITOR.instances.freeContext.getData(); // 불러오기
    content.type = "hidden";
    content.name = "freeContext";
	form.appendChild(content);

    form.action = "upload";
    form.method = "post";
    document.body.appendChild(form);
    form.submit();
 }
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
    form.action = "test";
    form.method = "post";
    document.body.appendChild(form);
    form.submit();
 }
 

// 1번
	function a(callback) {
	setTimeout(function() {
	console.log('a');
	callback();//매개변수  b
	}, 2000)
	}
	// 2번
	function b() {
	console.log('b');
	}

a();
b();
</script>
</html>