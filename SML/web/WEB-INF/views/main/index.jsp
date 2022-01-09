<%--
  Created by IntelliJ IDEA.
  User: LJY
  Date: 2021-10-03
  Time: 오후 12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Title</title>
    <%-- js / css공통함수cdn jsp파일 --%>
    <%@ include file="../../common/commonUtils.jsp" %>

    <spring:eval expression="@systemProp['test.test']" var="testtest"/>

</head>
<body>
<%
    String a = "";
%>

    <div>------------ DB -----------</div>
    <c:forEach items="${list}" var="item">
        <div>${item.i_num}</div>
        <div>${item.title}</div>
    </c:forEach>
    <div>------------ DB -----------</div>
    <h1>${name}</h1>
    <h2>${aop}</h2>
    <h2>----------</h2>
    <h2>${testtest}</h2>
    <h2>----------</h2>


    <form id="frm" name="frm">
        <input type="text" id="i_num" name="i_num">
        <input type="text" id="title" name="title">
    </form>

    <button type="button" id="btn" name="btn" onclick="onBtn()">클릭</button>
    <button type="button" id="testBoot" name="testBoot" onclick="moveTestBoot()">부스스트랩 이동</button>
    <button type="button" id="btn" name="btn" onclick="moveToLogin()">로그인</button>

    <br><br><br><br><br>
    <h1>---------------------------------------------------------------------------------</h1>
    <br><br><br><br><br>

    <form id="testFrm" name="testFrm">
        <input type="input" name="I_NUM" value="" />
        <input type="input" name="TITLE" value="" />
    </form>
    <button type="button" id="testBtn" name="testBtn">testBtn</button>


<script type="text/javascript">


    $(document).ready(function () {

        $("#testBtn").click(function() {
            fn_test();
        });
    });

    // testBtn 클릭시 ajax실행
    function fn_test() {

        // ajax submit
        $("#testFrm").ajaxSubmit({
            type: 'post'
            , url: '/main/ajax2'
            , dataType: 'json'

            , success: function(data){
            }

            ,error:function(data) {
                alert(JSON.stringify(data.responseText));
            }
        });
    }




    function onBtn() {
        fnSearch("/main/ajax", "#frm");

        // $("#frm").ajaxSubmit({
        //     type:'post'
        //     , url: '/main/ajax'
        //     ,success: function(data) {
        //         alert(data.title);
        //     }
        // })
    }

    function moveTestBoot() {
        location.href="/main/test?id=123";
    }

    function moveToLogin() {
        location.href="/main/login";
    }

</script>
</body>
</html>
