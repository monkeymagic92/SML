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

</head>
<body>

	<h1>ㅎㅇ</h1>
	<h1>${interceptorTest}</h1>
	<h1>${interceptorTest2}</h1>

	<c:forEach items="${list}" var="item">
		<div>${item.I_NUM}</div>
		<div>${item.TITLE}</div>
	</c:forEach>

	<form id="frm" name="frm" method="post">
		<input type="test" id="test" name="test" value="a" />
	</form>

	<button id="btn" name="btn" onclick="fnTest();">아작스</button>


<script type="text/javascript">

	$(document).ready(function () {


	});

	function fnTest() {
		$("#frm").ajaxSubmit({
			dataType: 'json'
			, url: "/quote/ajax"
			, success: function(data){

				alert(data.message);

			}
		});
	}

</script>
</body>
</html>