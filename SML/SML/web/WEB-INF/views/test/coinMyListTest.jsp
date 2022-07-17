<%--
  Created by IntelliJ IDEA.
  User: LJY
  Date: 2022-07-17
  Time: 오후 8:58
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
<!-- html 영역 -->
<h1>
	<button type="button" id="testBtn" name="testBtn">Api Select Service Test</button>
</h1>

<form id="frm" name="frm" method="post">
	<input type="hidden" id="MARKET" name="MARKET" value="KRW-BTC" />
</form>

<script>

	$(document).ready(function () {

		$("#testBtn").click(function() {
			fnAjaxSubmit('<c:url value="/api/insertCoinMyList" />', '#frm', null, null);
		})
	});

</script>
</body>
</html>
