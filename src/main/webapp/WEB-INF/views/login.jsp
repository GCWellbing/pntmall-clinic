<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script>
$(function() {
});

function proc() {
	if($("input[name=memId]").val() == 'ID') {
		alert("아이디를 입력하지 않았습니다.");
		$("input[name='memId']").focus();
	} else if($("input[name=passwd]").val() == 'PASSWORD') {
		alert("암호를 입력하지 않았습니다.");
		$("input[name='passwd']").focus();
	} else {
        disableScreen();
        ef.proc($("#form"), function(result) {
			if (result.succeed){
				if(result.message != "") alert(result.message);
				window.location.href = "/index";
			} else {
				alert(result.message);
				if(result.param.url != "") window.location.href = result.param.url;
			}

			enableScreen();
		});
	}
}

function checkSpacePress(){
	if(window.event.keyCode == 32) {
		$("#memId").val($("#memId").val().replace(" ",""));
		$("#passwd").val($("#passwd").val().replace(" ",""));
	}
}

</script>
</head>
<body>
<form id="form" name="form" method="POST" action="/loginProc">
<input type='hidden' name='clinicYn' value='Y'>
<div class="loginArea">
	<h1>
		<span><img src="/static/images/logo_login.png" alt="Dr.PNT"></span>
		<i>Administration</i>
	</h1>
	<fieldset>
		<input type="text" name="memId" id="memId" value="" class="txt" placeholder="ID" onKeyUp="JavaScript:checkSpacePress();">
		<input type="password" name="passwd" id="passwd" value="" class="txt" placeholder="PASSWORD" onKeyUp="JavaScript:checkSpacePress();">
		<button class="btnTypeB btn_login" onclick="proc();return false;">LOGIN</button>
	</fieldset>
	<p class="txtArea"><a href="/clinic/join/agree">회원가입</a></p>
	<p class="txtArea"><a href="/clinic/find/id">ID 찾기</a></p>
	<p class="txtArea"><a href="/clinic/find/pwd">비밀번호 찾기</a></p>
	<p class="txtArea">If you forgot ID and password, please contact <a href="mailto:pntmall@gccorp.com">pntmall@gccorp.com</a></p>
</div>
</form>

</body>
</html>
