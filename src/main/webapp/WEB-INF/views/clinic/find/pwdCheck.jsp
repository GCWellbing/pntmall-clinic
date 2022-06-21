<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="utils" scope="request" class="com.pntmall.common.utils.Utils"/>
<c:set var="Depth_1" value="3" scope="request"/>
<c:set var="Depth_2" value="4" scope="request"/>
<c:set var="Depth_3" value="2" scope="request"/>
<c:set var="MENU_TITLE" value="비밀번호 찾기" scope="request"/>
<c:set var="layerId" value="${param.layerId }" scope="request"/>

<!DOCTYPE HTML>
<html lang="ko">
<head>
<c:import url="/include/head" />
<link rel="stylesheet" type="text/css" href="/static/css/member.css?time=<%=System.currentTimeMillis()%>">
<script>
$(function() {
	$("input[name='sendType']:radio").change(function () {
        var sendType = this.value;

        if(sendType == "SMS"){
        	$("#msg").html("가입시 등록한 SMS로 인증번호가 발송됩니다.");
        }else{
        	$("#msg").html("가입시 등록한 이메일로 인증번호가 발송됩니다.");
        }
	});

});

function goPwdConfirm(){
	var sendType = $('input[name="sendType"]:checked').val();
	if(typeof sendType == "undefined" || sendType == "" || sendType == null){
		alert("본인 인증 방법을 선택하여 주세요");
		return false;
	}

	if("${ info.oldMember}" == "Y"){
		if(!$("#necessary2").is(":checked")){
			alert("민감정보 항목 수집에 동의해 주세요.");
			return false;
		}
	}

	editForm.submit();
}

function necessaryOpen(id){
	var layerId = $("#layerId").val();
	parent.memFrameId = "iframePopLayer"+layerId;
	showPopupLayer("/clinic/join/"+id+"?gubun=1");
}

</script>
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
<form name="editForm" id="editForm" method="post" action="pwdConfirm">
<input type="hidden" name="memId" value="${param.memId }">
	<h1>${MENU_TITLE}</h1>
	<div id="popCont" class="findArea">
		<p class="text">본인인증 방법을 선택해 주세요.</p>
		<div class="typeCheck">
			<p>
				<label><input type="radio" name="sendType" value="SMS"><span>SMS 인증</span></label>
				<strong>${info.mtel1}${utils.tel2Masking(info.mtel2)}</strong>
			</p>
			<p>
				<label><input type="radio" name="sendType" value="MAIL"><span>이메일 인증</span></label>
				<strong>${utils.emailMasking1(info.email)}</strong>
			</p>
		</div>
		<p id="msg" class="colorF"></p>
		<c:if test="${ info.oldMember eq 'Y' }">
			<div class="agree">
				<label><input type="checkbox" name="necessary2" id="necessary2"><span>[필수] 민감정보 항목 수집 동의</span></label>
				<span ><a href="#" onclick="necessaryOpen('sensitive'); return false" style="float: right;">보기</a></span>
			</div>
		</c:if>
		<div class="btnArea">
			<span><a href="#" onclick="goPwdConfirm();return false;" class="btnTypeA btnSizeD">다음</a></span>
		</div>
	</div><!-- //popCont -->
</form>
</div><!-- //popWrap -->
<script>

</script>
</body>
</html>