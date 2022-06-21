<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="Depth_1" value="2" scope="request"/>
<c:set var="Depth_2" value="1" scope="request"/>
<c:set var="Depth_3" value="1" scope="request"/>
<c:set var="MENU_TITLE" value="회원가입 완료" scope="request"/>

<!DOCTYPE HTML>
<html lang="ko">
<head>
<c:import url="/include/head" />
<link rel="stylesheet" href="/static/css/member.css?time=<%=System.currentTimeMillis()%>" type="text/css">
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
	<div id="popCont" class="joinComplete">
		<div class="head">
			<strong>PNTmall 병의원 <br>온라인 회원가입이 신청되었습니다.
			</strong>
			관리자가 승인 후 등록하신 휴대폰번호로<br>
			가입 완료 안내 톡알림/메세지를 보내드립니다.
		</div>

		<div class="btnArea">
			<span><a href="/" class="btnTypeC btnSizeD">확인</a></span>
		</div>
	</div><!-- //popCont -->

</div><!-- //popWrap -->
</body>
</html>