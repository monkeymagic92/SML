<%--
    날짜 : 2022-02-05 / 토요일 / 오후 6:57
    JSP : test
    유저 : LJY
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<title>Title</title>

	<%-- js / css공통함수cdn jsp파일 --%>
	<%@ include file="../common/commonUtils.jsp" %>

	<%-- properties 파일안에 값을 jsp에서 사용하기 --%>
	<spring:eval expression="@systemProp['test.test']" var="testtest"/>

</head>
<body>

<c:forEach items="${list}" var="item">
	<div>${item.i_num}</div>
	<div>${item.title}</div>
</c:forEach>



<script type="text/javascript">

	$(document).ready(function () {




		$("#ajax").click(function() {

			$("#frm").ajaxSubmit({
				type: 'post'
				, url: '/user/ajax'
				, dataType: 'json'
				, beforeSend: function() {
					$("body").append('<div id="windowByMask"><img src="${img}/loading1.gif" /></div>');

					wrapWindowByMask();
				}
				, success: function(data) {

					// for(var i=0; i<data.length; i++) {
					// 	$("#I_USER2").val(data[i].i_user);
					// 	$("#USER_ID").val(data[i].user_id);
					// 	$("#USER_PW").val(data[i].user_pw);
					// }

					data.forEach(function(item){
						$("#I_USER2").val(item.i_user);
						$("#USER_ID").val(item.user_id);
						$("#USER_PW").val(item.user_pw);
					});

					setTimeout(function() {
						wrapWindowByUnMask();
					}, 1000);
				}
			});
		});

		$("#tr").click(function(){
			location.href='/user/insertTr';
		});

	});

</script>
</body>
</html>