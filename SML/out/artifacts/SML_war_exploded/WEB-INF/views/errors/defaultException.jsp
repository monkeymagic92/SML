<%@ page import="com.sml.utils.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String message = "서버에서 오류가 발생하였습니다.";
	if (StringUtil.nullValue(request.getHeader("dataType")).equals("json")) {
		out.println("{\"message\":\""+message+"\"}");
	} else {
		out.println(message);
	}
%>


