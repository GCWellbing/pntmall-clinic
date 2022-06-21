<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="Depth_1" value="2" scope="request"/>
<c:set var="Depth_2" value="1" scope="request"/>
<c:set var="Depth_3" value="1" scope="request"/>
<c:set var="MENU_TITLE" value="PNTmall 병의원 회원가입" scope="request"/>

<!DOCTYPE HTML>
<html lang="ko">
<head>
<c:import url="/include/head" />
<link rel="stylesheet" type="text/css" href="/static/css/member.css?time=<%=System.currentTimeMillis()%>">
<script>
	$(function() {

	});

	function goJoin() {
		if($("#businessNo").val() == ""){
			alert("사업자 등록 번호를 입력해주세요.");
			return false;
		}
		if($("#businessOwner").val() == ""){
			alert("대표자명을 입력해주세요.");
			return false;
		}

		//사업자번호 검증
		if(isNaN($("#businessNo").val())){
			alert("사업자번호는 숫자만 입력 가능합니다.");
			return false;
		}

		if($("#businessNo").val().length != 10){
			alert("사업자번호를 10자 입력하세요.");
			return false;
		}

		$.getJSON("existsBusinessNo?businessNo=" + $("#businessNo").val(), function(json) {
			if(json.param.isExists) {
				alert("이미 가입된 회원입니다.");
	            return false;
			}
			else {
				editForm.submit();
			}
		});
	}


	function ckBisNo(bisNo)
	{
		if(isNaN(bisNo)){
			alert("숫자만 입력 가능합니다.");
			return false;
		}

		if(bisNo.length != 10){
			alert("10자로 입력하세요.");
			return false;
		}

		return true;

	}

</script>
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
	<h1 class="line">${MENU_TITLE}<br><span class="fn">(약관동의 > <b>회원조회</b> > 정보입력)</span></h1>
	<div id="popCont">
	<form name="editForm" id="editForm" method="post" action="form">
		<input type="hidden" name="smsYn" value="${param.smsYn}" />
		<input type="hidden" name="emailYn" value="${param.emailYn}" />

		<div class="joinArea joinAgree">
			<p class="info">온라인 회원가입을 위해 운영 중인 병의원의 사업자 등록번호와 대표자명을 입력해주세요.</p>
			<div class="fieldArea">
				<div class="field"><p class="tit">사업자 등록번호</p>
					<input type="text" name="businessNo" id="businessNo">
				</div>
				<div class="field"><p class="tit">대표자 명</p>
					<input type="text" name="businessOwner" id="businessOwner">
				</div>
			</div>
		</div>
		<div class="btnArea">
			<span><button id="goJoinBtn" class="btnTypeA btnSizeD" onclick="goJoin();return false;" >다음</button></span>
		</div>
	</form>
	</div><!-- //popCont -->
</div><!-- //popWrap -->
</body>
</html>