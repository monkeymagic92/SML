<%--
    날짜 : 2022-02-06 / 일요일 / 오전 10:32
    JSP : index
    유저 : LJY
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>

	<%-- js / css공통함수cdn jsp파일 --%>
	<link href="/res/css/quote/quoteIndex.css" rel="stylesheet">

	<!-- ----------------- style Start ----------------- -->
	<style>
		#market {cursor: pointer;}
	</style>
	<!-- ----------------- style End ----------------- -->
</head>
<body>
<script type="text/javascript">

	// ----------------- Document Start -----------------
	$(document).ready(function () {

		$("#testAjaxBtn").click(function() {
			fnAjaxSubmit('<c:url value="/quote/insertCoinList" />', "#frm", call);
		});

	});
	// ----------------- Document End -----------------

	// ----------------- Function Start -----------------

	// 클릭한 market 명으로 upbit 사이트 이동 (쿼리스트링=market)
	function fnMoveToUpbit(market) {
		var link = 'https://upbit.com/exchange?code=CRIX.UPBIT.'+market;
		window.open(link);
	}

	// Test Ajax 클릭시 콜백함수
	function call(data) {
		console.log(data.AAA);
	}

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


	// ----------------- Function End -----------------

</script>

<%-- ----------------- Test ----------------- --%>
<h1 id="quoteIndexTitle">
	<span>10:00AM Quote Table</span>
</h1>
<h2 id="quoteIndexTitle2">업비트 바로가기
	<a href="https://upbit.com/exchange?code=CRIX.UPBIT.KRW-BTC" target="_blank">Upbit</a>
	<button type="button" id="testAjaxBtn" name="testAjaxBtn">click To Test Ajax</button>
</h2>

<form id="frm" name="frm" method="post">
	<input type="hidden" id="ina" name="ina" value="123" />
</form>
<%-- ----------------- Test ----------------- --%>


<table class="quoteIndexContainer">
	<colgroup>
		<col width="6.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
	</colgroup>
	<thead>
	<tr>
		<th><h1>No</h1></th>
		<th><h1>마켓</h1></th>
		<th><h1>한글이름</h1></th>
		<th><h1>저가</h1></th>
		<th><h1>시가</h1></th>
		<th><h1>고가</h1></th>
		<th><h1>상승률</h1></th>
		<th><h1>보합</h1></th>
	</tr>
	</thead>
	<tbody>
	<c:forEach var="list" items="${list}">
		<tr>
			<td>${list.RNUM}</td>
			<td id="market" onclick="fnMoveToUpbit('${list.MARKET}');">${list.MARKET}</td>
			<td id="kor_nm">${list.KOR_NM}</td>
			<td id="low_price">${list.LOW_PRICE}</td>
			<td id="opening_price">${list.OPENING_PRICE}</td>
			<td id="high_price">${list.HIGH_PRICE}</td>
			<c:choose>
				<c:when test="${list.RISE_PRICE >= 10}">
					<td id="rise_price" style="color: red;">${list.RISE_PRICE}%</td>
				</c:when>
				<c:otherwise>
					<td id="rise_price">${list.RISE_PRICE}%</td>
				</c:otherwise>
			</c:choose>
			<td id="change">${list.CHANGE}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</body>
</html>