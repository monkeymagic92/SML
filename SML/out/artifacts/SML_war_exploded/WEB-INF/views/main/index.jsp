<%--
  Created by IntelliJ IDEA.
  User: LJY
  Date: 2021-10-03
  Time: 오후 12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>

    <title>Title</title>
    <%-- ajax공통함수cdn jsp파일 --%>
    <%@ include file="../common/ajaxCommon.jsp" %>

</head>
<body>

    <c:forEach items="${list}" var="item">
        <div>${item.i_num}</div>
        <div>${item.title}</div>
    </c:forEach>
    <h1>${name}</h1>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.3.0/jquery.form.min.js" integrity="sha384-qlmct0AOBiA2VPZkMY3+2WqkHtIQ9lSdAsAn5RUJD/3vA5MKDgSGcdmIv4ycVxyn" crossorigin="anonymous"></script>
<script>

    commonTest(); // 경로 : ajaxCommon.jsp -> ajaxCommonFile.js 에서 넘어온 것

</script>
</body>
</html>
