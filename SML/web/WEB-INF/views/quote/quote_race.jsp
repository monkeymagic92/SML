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

</head>
<body>
<%-- test용 --%>
<h1 id="quoteIndexTitle">
	<span>10:00AM Quote Table</span>
</h1>
<h2 id="quoteIndexTitle2">업비트 바로가기
	<a href="https://upbit.com/exchange?code=CRIX.UPBIT.KRW-BTC" target="_blank">Upbit</a>
</h2>

<button type="button" id="a" name="a">click Test Ajax</button>

<form id="frm" name="frm" method="post">
	<input type="hidden" id="ina" name="ina" value="123" />
</form>

<table class="quoteIndexContainer">
	<colgroup>
		<col width="12.5%">
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
			<td>${list.MARKET}</td>
			<td>${list.KOR_NM}</td>
			<td>${list.LOW_PRICE}</td>
			<td>${list.OPENING_PRICE}</td>
			<td>${list.HIGH_PRICE}</td>
			<td>${list.RISE_PRICE}</td>
			<td>${list.CHANGE}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<script type="text/javascript">

	$(document).ready(function () {

		$("#a").click(function() {

			fnAjaxSubmit('<c:url value="/quote/insertCoinList" />', "#frm", call);
		});
	});

	function call(data) {
		console.log(data.AAA);
	}

</script>
</body>
</html>