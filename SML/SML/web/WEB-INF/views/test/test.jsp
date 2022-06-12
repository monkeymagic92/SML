<%--
  Created by IntelliJ IDEA.
  User: LJY
  Date: 2022-06-11
  Time: 오후 7:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Title</title>
	<%@ include file="../../common/commonUtils.jsp" %>
</head>
<body>

<script>

	$(document).ready(function () {
		//..
	});

	// 서버단에서 날라온값을 자바스크립트에 담는법 ( 해당함수는 사용안하는중 )
	function coinList() {
		var coinInfo = new Array();

		<c:forEach var="list" items="${list}">
		coinInfo.push({market: "${list.MARKET}"
			, high_price: "${list.HIGH_PRICE}"
			, opening_price: "${list.OPENING_PRICE}"
			, low_price: "${list.LOW_PRICE}"
			, rise_price: "${list.RISE_PRICE}"});
		</c:forEach>

		for(var i=0; i<coinInfo.length; i++) {
			console.log('market : ' + coinInfo[i].market);
			console.log('high_price : ' + coinInfo[i].rise_price);
		}
	}

</script>
</body>
</html>
