<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	request.setAttribute("Depth_1", new Integer(3));
	request.setAttribute("Depth_2", new Integer(4));
	request.setAttribute("Depth_3", new Integer(1));
	request.setAttribute("MENU_TITLE", new String("아이디 찾기"));
	String layerId = request.getParameter("layerId");
%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<c:import url="/include/head" />
<link rel="stylesheet" type="text/css" href="/static/css/member.css?time=<%=System.currentTimeMillis()%>">
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
	<h1>${MENU_TITLE}</h1>
	<div id="popCont">
	<c:if test='${ fn:length(list) > 0 }'>
		<div class="findResult">
			<p class="head">입력하신 정보와 일치하는 <br>아이디입니다.</p>
			<c:forEach items="${ list }" var="row">
				<p class="success">${row.memId}</p>
			</c:forEach>
			<div class="btnArea ac">
				<a href="/login" class="btnTypeA btnSizeD">로그인</a>
			</div>
			<p class="go"><a href="/clinic/find/pwd">비밀번호 찾기</a></p>
		</div>
	</c:if>
	<c:if test='${ fn:length(list) == 0 }'>
		<div class="findFail">
			<p class="head">회원정보를 찾을 수 없습니다.</p>
			<div class="btnArea ac">
				<a href="/clinic/join/agree" class="btnTypeA btnSizeD">회원가입</a>
			</div>
		</div>
	</c:if>
	</div><!-- //popCont -->
</div><!-- //popWrap -->
<script>

</script>
</body>
</html>