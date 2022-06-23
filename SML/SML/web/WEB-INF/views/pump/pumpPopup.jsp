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
		<h3>${title.KOR_NM}(${title.MARKET})</h3>
		<h3>상승 : ${title.PUMP_CNT}회</h3>
		<h3>마지막 상승시간 : ${title.UPD_DT}</h3>
	</div>

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
				<td id="rise_price">${list.RISE_PRICE}</td>
				<td id="upd_dt">${list.UPD_DT}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
</body>
</html>
