<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order Document</title>
</head>
<body>${ordersInfo }
</body>

<script>
	var checkCount = 0;
	function checkState(obj) {
		checkCount += (obj.checked ? 1 : -1);

	}
	function order(gInfo) {
		var check = document.getElementsByName("check");
		var count = 0;
		var orderInfo = "";
		var goods = gInfo.split(":");
		for (i = 0; i < check.length; i++) {
			if (check[i].checked) {
				count++;
				orderInfo += (goods[i] + (checkCount == count ? "" : ":"));
			}
		}
		if (count == 0) {
			alert("하나 이상의 상품을 체크해주세요.");
		} else {
			var form = document.createElement("form");
			form.action = "OrderList?orderInfo=" + orderInfo;
			form.method = "post";
			document.body.appendChild(form);
			form.submit();
		}
	}
</script>

</html>