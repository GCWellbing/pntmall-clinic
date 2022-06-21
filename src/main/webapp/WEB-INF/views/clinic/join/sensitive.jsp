<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="Depth_1" value="3" scope="request"/>
<c:set var="Depth_2" value="2" scope="request"/>
<c:set var="Depth_3" value="0" scope="request"/>
<c:set var="MENU_TITLE" value="민감정보 항목 수집" scope="request"/>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<c:import url="/include/head" />
<link rel="stylesheet" href="/static/css/popup.css?time=<%=System.currentTimeMillis()%>" type="text/css">
</head>
<body>
<div id="popWrap" class="typeB">
	<h1>${MENU_TITLE}</h1>
	<div id="popCont" class="includeFixedBtn">
		${stipulation.content }
		<div class="btnArea fixedBtn">
			<span><a href="#" onclick="hidePopupLayer()" class="btnTypeA sizeB">확인</a></span>
		</div>
	</div><!-- //popCont -->
</div><!-- //popWrap -->
<script>
//팝업높이조절
setPopup(${ param.layerId });
</script>
</body>
</html>