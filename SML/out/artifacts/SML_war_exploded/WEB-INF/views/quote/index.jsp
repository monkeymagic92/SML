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
	<title>Title</title>

	<%-- js / css공통함수cdn jsp파일 --%>
	<link href="/res/css/quote/quoteIndex.css" rel="stylesheet">

</head>
<body>

<h1 id="quoteIndexTitle">
	<span>통계그래프</span>
</h1>
<h2 id="quoteIndexTitle2">Created with love by
	<a href="https://upbit.com/exchange?code=CRIX.UPBIT.KRW-BTC" target="_blank">Upbit</a>
</h2>

<table class="quoteIndexContainer">
	<thead>
	<tr>
		<th><h1>Sites</h1></th>
		<th><h1>Views</h1></th>
		<th><h1>Clicks</h1></th>
		<th><h1>Average</h1></th>
		<th><h1>Average</h1></th>
		<th><h1>Average</h1></th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td>Google</td>
		<td>9518</td>
		<td>6369</td>
		<td>01:32:50</td>
		<td>01:32:50</td>
		<td>01:32:50</td>
	</tr>
	<tr>
		<td>Twitter</td>
		<td>7326</td>
		<td>10437</td>
		<td>00:51:22</td>
		<td>01:32:50</td>
		<td>01:32:50</td>
	</tr>
	<tr>
		<td>Amazon</td>
		<td>4162</td>
		<td>5327</td>
		<td>00:24:34</td>
		<td>01:32:50</td>
		<td>01:32:50</td>
	</tr>
	<tr>
		<td>LinkedIn</td>
		<td>3654</td>
		<td>2961</td>
		<td>00:12:10</td>
		<td>01:32:50</td>
		<td>01:32:50</td>
	</tr>
	<tr>
		<td>LinkedIn</td>
		<td>01:32:50</td>
		<td>3654</td>
		<td>2961</td>
		<td>00:12:10</td>
		<td>01:32:50</td>
	</tr>
	<tr>
		<td>LinkedIn</td>
		<td>3654</td>
		<td>01:32:50</td>
		<td>2961</td>
		<td>00:12:10</td>
		<td>01:32:50</td>
	</tr>
	</tbody>
</table>

<script type="text/javascript">

	$(document).ready(function () {
		$("#aa").attr("href", "https://upbit.com/exchange?code=CRIX.UPBIT.KRW-${NM}").css('color', 'red');
	});
</script>
</body>
</html>