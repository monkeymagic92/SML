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
	<link href="/res/css/pump/pumpPopup.css" rel="stylesheet">
	<%@ include file="../../common/commonUtils.jsp" %>
</head>

<body>
<script>

	$(document).ready(function() {

	});

	// 트랜잭션 Test ===================================================
	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 해당부분은 따로 sample이나 study이런 패키지에 jsp로 넣어서 참고용으로 두기
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
<div class="pumpContainer">
	<div class="pumpHeader">
		<span>${title.KOR_NM}(${title.MARKET})</span><br>
		<span>상승 : <span style="color: red;">${title.PUMP_CNT}회</span></span><br>
		<span>마지막 상승시간 : ${title.TRADE_DATE}</span>
	</div>
	<br>

	<table class="pumpIndexContainer">
		<colgroup>
			<col width="5.5%">
			<col width="11.5%">
			<col width="12.5%">
			<col width="13.5%">
			<col width="13.5%">
			<col width="18.5%">
		</colgroup>
		<thead>
		<tr>
			<th><h1>No</h1></th>
			<th><h1>마켓</h1></th>
			<th><h1>한글이름</h1></th>
			<th><h1>상승기준</h1></th>
			<th><h1>상승률</h1></th>
			<th><h1>상승날짜</h1></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="list" items="${list}">
			<tr>
				<td id="rnum">${list.RNUM}</td>
				<td id="market">${list.MARKET}</td>
				<td id="kor_nm">${list.KOR_NM}</td>
				<td id="pump_set_rise_price">${list.PUMP_SET_RISE_PRICE}</td>
				<c:choose>
					<c:when test="${list.RISE_PRICE >= 10}">
						<td id="rise_price" style="color: red;">${list.RISE_PRICE}%</td>
					</c:when>
					<c:otherwise>
						<td id="rise_price">${list.RISE_PRICE}%</td>
					</c:otherwise>
				</c:choose>
				<td id="upd_dt">${list.TRADE_DATE}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
</body>
</html>
