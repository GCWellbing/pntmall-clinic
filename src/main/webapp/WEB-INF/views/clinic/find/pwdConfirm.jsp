<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	counting();
});

// 각종 인증 카운팅
var timer;
var comparisonTime;
var countType = "${ param.sendType}";
var currentTime = new Date();
currentTime = currentTime.getTime();

function counting(){

	comparisonTime = new Date();
	if (countType == 'SMS') {
		var targetTime = currentTime + (1000 * 60 * 3);		// SMS의 경우 3분
	} else {
		var targetTime = currentTime + (1000 * 60 * 10);	// 이메일의 경우 10분
	}
	var interval = targetTime - comparisonTime.getTime();

	if(interval > 0){
		var msecPerSecond = 1000;
		var msecPerMinute = 1000 * 60;

		var minutes = Math.floor(interval / msecPerMinute );
		interval = interval - (minutes * msecPerMinute );
		var seconds = Math.floor(interval / msecPerSecond );

		if(minutes < 10){
			minutes = "0" + minutes;
		}
		if(seconds < 10){
			seconds = "0" + seconds;
		}

		if (countType == 'SMS') {
			$("#Minute").text(minutes.toString());
			$("#Second").text(seconds.toString());
		} else {
			$("#Minute").text(minutes.toString());
			$("#Second").text(seconds.toString());
		}

		timer = setTimeout(counting, 1000);
	}else{
		alert("인증번호 유효시간이 경과되어 인증에 실패하였습니다.");
		$('#goPwdResetBtn').attr('disabled', true);
	}
}

function goPwdConfirm(){
	$("#editForm").attr("action", "pwdConfirm");
	editForm.submit();
}

function goPwdReset(){
	if( $("#certiNo").val() == "" || $("#certiNo").val() == "인증번호 입력"){
		alert("인증 번호를 입력해 주세요");
		return false;
	}

	$("#editForm").attr("action", "pwdConfirmProc");
	ef.proc($("#editForm"), function(result) {
		if(result.succeed) {
			$("#editForm").attr("action", "pwdReset");
			editForm.submit();
		}else{
			alert(result.message);
		}
	});
}
</script>
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
<form name="editForm" id="editForm" method="post" action="pwdResetProc" onSubmit="goPwdReset();return false;">
<input type="hidden" name="memId" value="${param.memId }">
<input type="hidden" name="sendType" value="${param.sendType }">
	<h1>${MENU_TITLE}${ system ne 'real' ? confirmNum : '' }</h1>
	<div id="popCont" class="findArea">
		<c:if test="${ param.sendType eq 'SMS' }">
			<p class="text">수신된 SMS의 인증번호를 <br>입력해 주세요.</p>
		</c:if>
		<c:if test="${ param.sendType eq 'MAIL' }">
			<p class="text">수신된 이메일의 인증번호를 <br>입력해 주세요.</p>
		</c:if>
		<div class="fieldArea">
			<div class="field">
				<input type="text" name="certiNo" id="certiNo"  placeholder="인증번호 입력" title="인증번호">
				<span class="timer"><span id="Minute">00</span>:<span id="Second">00</span></span>
			</div>
		</div>
		<p class="goCheck">
			<span>인증번호가 도착하지 않았나요?</span>
			<a href="#" onclick="goPwdConfirm();return false;">인증번호 다시받기</a>
		</p>
		<div class="btnArea">
			<span><a href="/clinic/find/pwdCheck?memId=${param.memId }" class="btnTypeC btnSizeD">이전</a></span>
			<span><button type="button" id="goPwdResetBtn" class="btn btnTypeA btnSizeD" onclick="goPwdReset();return false;">다음</button></span>
		</div>
	</div><!-- //popCont -->
</form>
</div><!-- //popWrap -->
</body>
</html>