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

	<style>
		#market {cursor: pointer;}
		#market:hover {color: #9d9d9d; transition: 0.4s;}
	</style>
</head>
<body>
<script type="text/javascript">

	$(document).ready(function () {

		// (DAY) 같은경우 오늘날짜-1을 해야 오전9시전 하루 데이터를 볼수있음 (왜냐? 미래는 모르기에 오전9시 전까지 상승이 하루 상승량이라 보면됨)
		var d = new Date();
		var nowDate = d.getFullYear() + "/" + ((d.getMonth() + 1) > 9 ? (d.getMonth() + 1) : "0" + (d.getMonth() + 1)) + "/" + (d.getDate() > 9 ? (d.getDate() - 1) : "0" + (d.getDate() - 1));
		$("#searchTRADE_DATE").val(nowDate);

		// 달력 검색
		fnDatepicker($("#searchTRADE_DATE"), false);

		$("#testAjaxBtn").click(function() {
			fnAjaxSubmit('<c:url value="/quote/insertDayCoinList" />', "#frm");
		});

		$("#btnSearch").click(function() {
			fnSearch();
		});

		fnSearch();

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

	// 엔터키 클릭시 조회
	function enterKey() {
		if (window.event.keyCode == 13) {
			fnSearch();
		}
	}

	// 조회
	function fnSearch() {
		fnAjaxSubmit('<c:url value="/quote/selectQuoteDay" />', '#searchForm', makeSelectList);
	}

	// ajax 콜백함수로 조회리스트를 만든다
	function makeSelectList(data) {

		$("#selectList").html('');

		var items;
		items = data.list;

		$.each(items, function(index, rtnVal){

			// ********* 속성 부여 *********
			var tr = document.createElement('tr');

			var rnum = document.createElement('td');
			rnum.setAttribute("id", 'rnum');

			var market = document.createElement('td');
			market.setAttribute("id", "market");
			market.onclick = function() {
				fnMoveToUpbit(rtnVal.MARKET);
			};

			var korNm = document.createElement('td');
			korNm.setAttribute("id", 'korNm');

			var low_price = document.createElement('td');
			low_price.setAttribute("id", 'low_price');

			var opening_price = document.createElement('td');
			opening_price.setAttribute("id", 'opening_price');

			var high_price = document.createElement('td');
			high_price.setAttribute("id", 'high_price');

			var rise_price = document.createElement('td');
			rise_price.setAttribute("id", 'rise_price');
			if(rtnVal.RISE_PRICE >= 10) {
				rise_price.style.color = 'red';
			}

			var openPumpPopupTd = document.createElement('td');
			var openPumpPopup = document.createElement('button');
			openPumpPopup.setAttribute('class', 'btn btn-primary');
			openPumpPopup.setAttribute('id', 'openPumpPopup');
			openPumpPopup.onclick = function() {
				fnOpenPump(rtnVal.MARKET);
			};

			var searchIcon = document.createElement('i');
			searchIcon.setAttribute('class', 'fas fa-search');

			// ********* 해당 속성들에 DB값 넣기 *********
			rnum.append(rtnVal.RNUM);
			market.append(rtnVal.MARKET);
			korNm.append(rtnVal.KOR_NM);
			low_price.append(rtnVal.LOW_PRICE);
			opening_price.append(rtnVal.OPENING_PRICE);
			high_price.append(rtnVal.HIGH_PRICE);
			rise_price.append(rtnVal.RISE_PRICE);

			// ********* selectList에 값이 들어간 속성들을 실제나열 *********
			$("#selectList").append(tr);
			tr.append(rnum);
			tr.append(market);
			tr.append(korNm);
			tr.append(low_price);
			tr.append(opening_price);
			tr.append(high_price);
			tr.append(rise_price);
			openPumpPopup.append(searchIcon);
			openPumpPopupTd.append(openPumpPopup);
			tr.append(openPumpPopupTd);
		});
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
	<form id="searchForm" name="searchForm">
		<!-- 날짜 검색 -->
		<label for="searchTRADE_DATE">
			<span style="font-size: 1.2em; font-weight: bold; color: white; letter-spacing: 2px;">날짜</span>
		</label>
		<input type="text" class="ctl_input_1" id="searchTRADE_DATE" name="searchTRADE_DATE" onchange="fnSearch();"/>
		<!-- 코인명 검색 -->
		<label for="searchKOR_NM">
			<span style="font-size: 1.2em; font-weight: bold; color: white; letter-spacing: 2px;">코인명</span>
		</label>
		<input type="text" class="ctl_input_1" id="searchKOR_NM" name="searchKOR_NM" value="" onkeyup="enterKey()";/>

		<button type="button" class="btn btn-primary" id="btnSearch" name="btnSearch"><i class="fas fa-search"></i></button>
	</form>
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
		<th><h1>펌핑</h1></th>
	</tr>
	</thead>
	<tbody id="selectList">
<%--	<c:forEach var="list" items="${list}">--%>
<%--		<tr>--%>
<%--			<td>${list.RNUM}</td>--%>
<%--			<td id="market" onclick="fnMoveToUpbit('${list.MARKET}');">${list.MARKET}</td>--%>
<%--			<td id="kor_nm">${list.KOR_NM}</td>--%>
<%--			<td id="low_price">${list.LOW_PRICE}</td>--%>
<%--			<td id="opening_price">${list.OPENING_PRICE}</td>--%>
<%--			<td id="high_price">${list.HIGH_PRICE}</td>--%>
<%--			<c:choose>--%>
<%--				<c:when test="${list.RISE_PRICE >= 10}">--%>
<%--					<td id="rise_price" style="color: red;">${list.RISE_PRICE}%</td>--%>
<%--				</c:when>--%>
<%--				<c:otherwise>--%>
<%--					<td id="rise_price">${list.RISE_PRICE}%</td>--%>
<%--				</c:otherwise>--%>
<%--			</c:choose>--%>
<%--			<td id="change">${list.CHANGE}</td>--%>
<%--			<td><button id="openPumpModal" name="openPumpModal" type="button" onclick="fnOpenPump('${list.MARKET}');">pump</button></td>--%>
<%--		</tr>--%>
<%--	</c:forEach>--%>
	</tbody>
</table>

</body>
</html>