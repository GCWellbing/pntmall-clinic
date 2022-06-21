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
		$("#allAgree").change(function(){
			if($("#allAgree").is(":checked")){
	            $("#necessary1").prop("checked", true);
	            $("#necessary2").prop("checked", true);
	            $("#necessary3").prop("checked", true);
	            $("#smsYn").prop("checked", true);
	            $("#emailYn").prop("checked", true);
	        }else{
	            $("#necessary1").prop("checked", false);
	            $("#necessary2").prop("checked", false);
	            $("#necessary3").prop("checked", false);
	            $("#smsYn").prop("checked", false);
	            $("#emailYn").prop("checked", false);
	        }
		});
		/*
		v = new ef.utils.Validator($("#editForm"));
		v.add("memId", {
			empty : "ID를 입력하세요."
		});
		v.add("passwd", {
			empty : "패스워드를 입력하세요."
		});
 */

		$("input").change(function(){
			if(!$("#necessary1").is(":checked")){
				$('#goJoinBtn').attr('disabled', true);
				return false;
			}
			if(!$("#necessary2").is(":checked")){
				$('#goJoinBtn').attr('disabled', true);
				return false;
			}
			if(!$("#necessary3").is(":checked")){
				$('#goJoinBtn').attr('disabled', true);
				return false;
			}

			$('#goJoinBtn').attr('disabled', false);

		});


	});

	function goJoin() {
		if(!$("#necessary1").is(":checked")){
			alert("PNTmall 이용약관에 동의해 주세요.");
			return false;
		}
		if(!$("#necessary2").is(":checked")){
			alert("민감정보 항목 수집에 동의해 주세요.");
			return false;
		}
		if(!$("#necessary3").is(":checked")){
			alert("개인정보 취급방침에 동의해 주세요.");
			return false;
		}
		editForm.submit();
	}

	function necessaryOpen(id){
		showPopupLayer("/clinic/join/"+id+"?gubun=2");
	}

	function download(filename, textInput) {
		location.href = "/upload/downloadFile?attach="+textInput+"&attachOrgName="+filename;
	}

</script>
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
	<h1 class="line">${MENU_TITLE}<br><span class="fn">(<b>약관동의</b> > 회원조회 > 정보입력)</span></h1>	
	<div id="popCont">
	<form name="editForm" id="editForm" method="post" action="businessNo">
	<input type="hidden" name="layerId" id="layerId" value="${param.layerId}" />

		<div class="joinArea joinAgree">
			<p class="text">
				<span class="colorE">아래 서류를 사전에 준비해주세요.</span><br>
				정산을 위해 필요한 서류이며, 해당 목적 이외에 사용되지 않습니다.<br>
				자세한 사항은 (이용 안내 - 병의원회원 가입 안내)를 참고하시길 바랍니다.<br>
				<ul class="caution2">
					<li>병의원 사업자등록증 / 스캔 파일 업로드</li>
					<li>건강기능식품 사업자등록증 / 스캔 파일 업로드</li>
					<li>통장 사본 / 스캔 파일 등록</li>
					<li>계약서 [<input type='button' id="attachBtn" class='btnSizeA btnTypeD valid' onclick='download("${agree.attachOrgName}","${agree.attach}"); return false;' value='다운로드'>] / 날인 후 스캔 파일 업로드</li>
				</ul>
			</p>
			<p class="info">PNTmall 서비스 이용을 위해 약관 동의를 선택해주세요.</p>
			<p class="allChk">
				<input type="checkbox" id="allAgree"><label for="allAgree">모든 약관 동의</label>
			</p>
			<p class="text">모든 약관 동의는 필수 약관 및 광고성 수신 동의가 포함되어 있으며,
선택 항목에 대한 동의를 거부하는 경우에도 회원가입은 정상적으로 진행됩니다.</p>
			<div class="agreeArea">
				<h2>PNTmall 회원약관</h2>
				<ul class="agreeList">
					<li>
						<label><input type="checkbox" name="necessary1" id="necessary1"><span>[필수] PNTmall 이용약관</span></label>
						<p><a href="#" onclick="necessaryOpen('stipulation'); return false" class="">보기</a></p>
					</li>
					<li>
						<label><input type="checkbox" name="necessary2" id="necessary2"><span>[필수] 민감정보 항목 수집 동의</span></label>
						<p><a href="#" onclick="necessaryOpen('sensitive'); return false" class="">보기</a></p>
					</li>
					<li>
						<label><input type="checkbox" name="necessary3" id="necessary3"><span>[필수] 개인정보 처리방침 동의</span></label>
						<p><a href="#" onclick="necessaryOpen('privacy'); return false" class="">보기</a></p>
					</li>
				</ul>
				<h2>광고성 정보 수신동의<p class="colorE">* 쇼핑 혜택, 이벤트 소식을 받아보세요.</p></h2>
				<ul class="agreeList">
					<li>
						<label><input type="checkbox" name="smsYn" id="smsYn" value="Y"><span>[선택] PNTmall 문자 수신 동의</span></label>
					</li>
					<li>
						<label><input type="checkbox" name="emailYn" id="emailYn" value="Y"><span>[선택] PNTmall 메일 수신 동의</span></label>
					</li>
				</ul>
			</div>
		</div><!-- //joinArea -->
		<div class="btnArea">
			<span><button id="goJoinBtn" class="btnTypeA btnSizeD" disabled="false" onclick="goJoin();">다음</button></span>
		</div>
	</form>
	</div><!-- //popCont -->
</div><!-- //popWrap -->
</body>
</html>