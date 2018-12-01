<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%-- 标签库及根路径 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>结果</title>
</head>
<body style="white-space:nowrap;">
	<div>
		${message},<a href="${ctx}/UserController?action=download&realFilename=${realFilename}">下载</a>
	</div>
	<img alt="加载失败！" src="${ctx}/WebContent/resultImg/${realFilename}">
</body>
</html>