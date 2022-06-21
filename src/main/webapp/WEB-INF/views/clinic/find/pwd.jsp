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
function goPwdCheck(){
	ef.proc($("#editForm"), function(result) {

		if(result.succeed) {
			location.href = "/clinic/find/pwdCheck?memId="+$("#memId").val();
		}else{
			alert(result.message);
		}
	});

}
</script>
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
	<h1>${MENU_TITLE}</h1>
	<div id="popCont" class="findArea">
	<form name="editForm" id="editForm" method="post" action="pwdProc" onSubmit="goPwdCheck();return false;">

		<p class="text">비밀번호를 잃어버리셨나요?<br>아래의 정보를 입력해 주세요.</p>
		<div class="findType">
			<div class="fieldArea">
				<div class="field">
					<p class="tit">아이디<em>*</em></p>
					<input type="text" name="memId" id="memId" placeholder="아이디를 입력해주세요." title="아이디">
				</div>
			</div>
		</div>
		<p class="goFindId">
			<span>아이디를 모르시나요?</span>
			<a href="/clinic/find/id">아이디 찾기</a>
		</p>
		<div class="btnArea">
			<span><a href="#" onclick="goPwdCheck();return false;" class="btnTypeA btnSizeD">다음</a></span>
		</div>
	</form>
	</div><!-- //popCont -->
</div><!-- //popWrap -->
<script>

</script>
</body>
</html>