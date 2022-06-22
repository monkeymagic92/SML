<%--
  Created by IntelliJ IDEA.
  User: LJY
  Date: 2022-06-19
  Time: 오후 8:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Title</title>
	<link href="/res/css/pump/pumpModal.css" rel="stylesheet">
	<%@ include file="../../common/commonUtils.jsp" %>
</head>

<body>
<script>

	$(document).ready(function() {

	});

	// 트랜잭션 Test ===================================================
	// ajax 호출
	function selectTest() {
		fnAjaxSubmit('/pump/selectTest', '#testFrm', selectCall);
	}

	// ajax 호출후 callback함수
	function selectCall(data) {

		// 1. respone 값 뿌리기 첫번째 방법 ==============
		for(var i=0; i<data.list.length; i++) {
			console.log(data.list[i].MARKET);
			console.log(data.list[i].RISE_PRICE);
			console.log('^^;');
		}
		// ==========================================

		// 2. response 값 뿌리기 두번째 방법 ==============
		// var items;
		// items = data.list;
		// $.each(items, function(index, rtnVal) {
		// 	console.log(rtnVal.MARKET);
		// 	console.log(rtnVal.KOR_NM);
		// 	console.log('------');
		// });
		// ==========================================
	}

	function saveTest() {
		fnAjaxSubmit('/pump/saveTest', '#testFrm', saveCall);
	}

	function saveCall(data) {
		alert(data.MSG);
	}
	// 트랜잭션 Test ===================================================

</script>
	<!-- 테스트 성공 값 정상적으로 날라옴 -->
	<c:forEach var="list" items="${list}">
		<div>${list.MARKET}</div>
		<div>${list.UPD_DT}</div>
	</c:forEach>

	<!-- 트랜잭션 및 ajax 테스트  -->
	<button type="button" id="saveBtn" name="saveBtn" onclick="saveTest();">saveTest</button>
	<button type="button" id="testBtn" name="testBtn" onclick="selectTest();">selectTest</button>
	<form id="testFrm" name="testFrm" method="post">
		<input type="hidden" id="MARKET" name="MARKET" value="AAA"/>
		<input type="hidden" id="RISE_PRICE" name="RISE_PRICE" value="123"/>
	</form>
	<!-- 트랜잭션 및 ajax 테스트  -->
</body>
</html>
