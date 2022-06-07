<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Exception" %>

<%
	String message = "";
	if (request.getAttribute("error") != null) {
		Exception ex = (Exception) request.getAttribute("error");
		message = ex.getMessage();
	}


	if (request.getAttribute("exceptionMsgType") == null) {
//if (request.getHeader("dataType").equals("json")) {
		out.println("{\"message\":\""+message+"\"}");
//} else {
//	out.println(message);
//}
	} else {
		out.println(message);
	}
%>
