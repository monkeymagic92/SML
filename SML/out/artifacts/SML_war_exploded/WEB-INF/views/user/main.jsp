<%--
    날짜 : 2022-01-10 / 월요일 / 오후 8:44
    JSP : main
    유저 : LJY
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<title>Title</title>

	<%-- js / css공통함수cdn jsp파일 --%>
	<%@ include file="../../common/commonUtils.jsp" %>

</head>
<body>


<input type="text" name="I_USER2" id="I_USER2" value="" />
<input type="text" name="USER_ID" id="USER_ID" value="" />
<input type="text" name="USER_PW" id="USER_PW" value="" />

<form id="frm" name="frm">
	<input type="text" id="I_USER" name="I_USER" value="">
</form>
<button type="button" name="ajax" id="ajax">TestBtn</button>
<br><br>
<button type="button" name="tr" id="tr">트랜잭션 테스트</button>

<script type="text/javascript">

	$(document).ready(function () {

		$("#ajax").click(function() {

			$("#frm").ajaxSubmit({
				type: 'post'
				, url: '/user/ajax'
				, dataType: 'json'
				,success: function(data) {

					// for(var i=0; i<data.length; i++) {
					// 	$("#I_USER2").val(data[i].i_user);
					// 	$("#USER_ID").val(data[i].user_id);
					// 	$("#USER_PW").val(data[i].user_pw);
					// }
					data.forEach(function(item){
						$("#I_USER2").val(item.i_user);
						$("#USER_ID").val(item.user_id);
						$("#USER_PW").val(item.user_pw);
					})
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