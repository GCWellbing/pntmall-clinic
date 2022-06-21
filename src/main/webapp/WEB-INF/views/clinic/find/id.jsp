<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script>
$(function() {
	$("#emailSel").change(function(){
		$("#email2").val($("#emailSel").val());
		if($("#emailSel").val() == ""){
			$("#email2").attr("readonly", false);
		}else{
			$("#email2").attr("readonly", true);
		}
	});

});

function chgTab(obj){
	var target = $(obj).attr("href");
	if(target.indexOf("phone") > -1){
		$("#idSrhType").val("PHONE");
	}else{
		$("#idSrhType").val("EMAIL");
	}

	showTab(obj, 'findType')

	//$("#email").val($("#email1").val() +"@"+ $("#email2").val());
}

function goIdResult(){

	if($("#emailSel").val() != ""){
		$("#email2").val($("#emailSel").val());
	}
	$("#email").val($("#email1").val() +"@"+ $("#email2").val());

	if($("#idSrhType").val() == "PHONE"){
		if($("#name").val() == "" || $("#name").val() == "성명을 입력해주세요."){
			alert("이름을 입력하세요");
			return false;
		}
		if($("#mtel2").val() == "" || $("#mtel2").val() == "번호입력 (-없이 입력)"){
			alert("휴대폰 번호를 입력하세요");
			return false;
		}
	}else{
		if($("#name1").val() == "" || $("#name1").val() == "성명을 입력해주세요."){
			alert("이름을 입력하세요");
			return false;
		}else{
			$("#name").val($("#name1").val())
		}

		if($("#email").val() == ""){
			alert("이메일를 입력하세요");
			return false;
		}
	}

	editForm.submit();
}

</script>
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
<form name="editForm" id="editForm" method="post" action="idResult">
<input type="hidden" name="idSrhType" id="idSrhType" value="PHONE">
	<h1>${MENU_TITLE}</h1>
	<div id="popCont" class="findArea">
		<div class="tabInit typeB">
			<ul>
				<li class="on"><a href="#phoneType" onclick="chgTab(this); return false;"><span>휴대폰 번호로 찾기</span></a></li>
				<li><a href="#emailType" onclick="chgTab(this); return false;"><span>이메일로 찾기</span></a></li>
			</ul>
		</div>
		<p class="text">아이디를 잊으셨나요?<br>아래의 정보를 입력해 주세요.</p>
		<div class="findType" id="phoneType">
			<div class="fieldArea">
				<div class="field"><p class="tit">이름<em>*</em></p>
					<input type="text" name="name" id="name" placeholder="성명을 입력해주세요." title="성명">
				</div>
				<div class="field">
					<p class="tit">휴대폰<em>*</em></p>
					<div class="phone">
						<select  name="mtel1" id="mtel1" style="width:100px">
							<c:forEach items="${ telList }" var="row">
								<option value="${ row.name }">${ row.name }</option>
							</c:forEach>
						</select>
						<input type="number" name="mtel2" id="mtel2" placeholder="번호입력 (-없이 입력)" title="휴대폰번호">
					</div>
				</div>
			</div>
		</div><!-- //휴대폰 번호 -->
		<div class="findType" id="emailType" style="display:none">
			<div class="fieldArea">
				<div class="field">
					<p class="tit">이름<em>*</em></p>
					<input type="text" name="name1" id="name1" placeholder="성명을 입력해주세요." title="성명">
				</div>
				<div class="field">
					<p class="tit">이메일<em>*</em></p>
					<div class="email">
						<p class="emailTxt">
							<input type="hidden" name="email" id="email">
							<input type="text" name="email1" id="email1" placeholder="이메일 입력" title="이메일 앞자리">
							<span>@</span>
							<input type="text" name="email2" id="email2" title="이메일 뒷자리">
						</p>
						<select name="emailSel" id="emailSel" style="width:150px">
							<option value="">직접입력</option>
							<c:forEach items="${ emailList }" var="row">
								<option value="${ row.name }">${ row.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</div><!-- //이메일 -->
		<div class="btnArea">
			<span><a href="#" onclick="goIdResult();return false;" class="btnTypeA btnSizeD">아이디 찾기</a></span>
		</div>
	</div><!-- //popCont -->
</form>
</div><!-- //popWrap -->
<script>

</script>
</body>
</html>