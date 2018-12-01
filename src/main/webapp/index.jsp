<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 标签库及根路径 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
<c:if test="${message!=null && message!=''&& message!='转换成功'}">
	${message}
</c:if>
<form action="${ctx}/UserController?action=upload" method="post" enctype="multipart/form-data">
	选择图片：<input type="file" name="file"/>
	<input type="text" name="text"/>
	<input type="radio" name="quality" value="1" checked="checked">标清
	<input type="radio" name="quality" value="2">高清
	<input type="radio" name="quality" value="3">超清
	<input type="submit" value="提交">
</form>
</body>
</html>