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
	<link href="/res/css/pump/pumpModal.css" rel="stylesheet">

	<style>
		#market {cursor: pointer;}
		#market:hover {color: #9d9d9d; transition: 0.4s;}
	</style>
</head>
<body>
<script type="text/javascript">

	$(document).ready(function () {

		// 달력 검색
		fnDatepicker($("#searchTRADE_DATE"), true);

		$("#testAjaxBtn").click(function() {
			fnAjaxSubmit('<c:url value="/quote/insertDayCoinList" />', "#frm");
		});

	});


	// 클릭한 market 명으로 upbit 사이트 이동 (쿼리스트링=market)
	function fnMoveToUpbit(market) {
		var link = 'https://upbit.com/exchange?code=CRIX.UPBIT.'+market;
		window.open(link);
	}

	// PUMP팝업창 오픈 ( 해당 코인 이름으로 PUMP테이블 조회 )
	function fnOpenPump(market) {
		var type = "DAY";
		var s_url = '<c:url value="/pump/pumpPopup" />'+ "?MARKET="+market+"&TYPE="+type;
		fnOpenPopup('pumpPopup', s_url, 800, 500);
	}

</script>

<%-- ----------------- Test ----------------- --%>
<h3 id="quoteIndexTitle">
	<span style="color: #CCCCCC; font-weight: bold;">08:50am Day List</span>
</h3>

<button type="button" id="testAjaxBtn" name="testAjaxBtn">click To Test Ajax</button>
<form id="frm" name="frm" method="post">
	<input type="hidden" id="ina" name="ina" value="123" />
</form>
<%-- ----------------- Test ----------------- --%>

<!-- 검색관련 -->
<div class="searchBox">
	<!-- 날짜 검색 -->
	<label for="searchTRADE_DATE">
		<span style="font-size: 1.2em; font-weight: bold; color: white; letter-spacing: 2px;">날짜</span>
	</label>
	<input type="text" class="ctl_input_1" id="searchTRADE_DATE" name="searchTRADE_DATE" />
	<!-- 코인명 검색 -->
	<label for="searchKOR_NM">
		<span style="font-size: 1.2em; font-weight: bold; color: white; letter-spacing: 2px;">코인명</span>
	</label>
	<input type="text" class="ctl_input_1" id="searchKOR_NM" name="searchKOR_NM" />
</div>


<table class="quoteIndexContainer">
	<colgroup>
		<col width="6.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="12.5%">
		<col width="8.5%">
		<col width="16.5%">
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
		<th><h1>펌핑</h1></th>
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
			<td><button id="openPumpModal" name="openPumpModal" type="button" onclick="fnOpenPump('${list.MARKET}');">pump</button></td>
		</tr>
	</c:forEach>
	</tbody>
</table>

</body>
</html>