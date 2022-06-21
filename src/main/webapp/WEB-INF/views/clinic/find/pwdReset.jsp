<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setAttribute("Depth_1", new Integer(3));
	request.setAttribute("Depth_2", new Integer(4));
	request.setAttribute("Depth_3", new Integer(2));
	request.setAttribute("MENU_TITLE", new String("비밀번호 찾기"));
	String layerId = request.getParameter("layerId");
%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<c:import url="/include/head" />
<link rel="stylesheet" type="text/css" href="/static/css/member.css?time=<%=System.currentTimeMillis()%>">
<script type="text/javascript">
function chkValidPasswd(passwd) {
	$("#passMsg").removeClass("pass fail");
	if(passwd.length < 10 || passwd.length > 16 ) {
    	$("#passMsg").addClass("fail");
        $("#passMsg").html("비밀번호는 최소 10자 , 최대 16자 입니다.");
        return false;
    }

    //생성 항목 포함 갯수
    //2개 항목이상 10자리 이상
    //3개 항목이상 8자리 이상
    var validCount	=	0;

    //대문자 포함 여부
    if(passwd.search(/[a-z]+/) > -1){
        validCount	+=	1;
    }
    //소문자 포함 여부
    if(passwd.search(/[A-Z]+/) > -1){
        validCount	+=	1;
    }
    //숫자 포함 여부
    if(passwd.search(/[0-9]+/) > -1){
        validCount	+=	1;
    }
    //특수문자 포함 여부
    if(passwd.search(/[~!@\#$%<>^&*\()\-=+_\']/) > -1 ){
        validCount	+=	1;
    }

    if(validCount <= 1){
    	$("#passMsg").addClass("fail");
        //alert("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 최소한 2가지 항목이 포함되어야합니다.");
        $("#passMsg").html("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 최소한 2가지 항목이 포함되어야합니다.");
        return false;
    }
    /*
    else if ( validCount == 2){
        if( passwd.length < 10){
        	$("#passMsg").addClass("fail");
            //alert("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 2가지 항목이 포함된경우 최소 10자이상 등록해주세요.");
            $("#passMsg").html("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 2가지 항목이 포함된경우 최소 10자이상 등록해주세요.");
            return false;
        }
    } else if ( validCount > 3){
        if( passwd.length < 8){
        	$("#passMsg").addClass("fail");
            //alert("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 3가지 항목이상 포함된경우 최소 8자이상 등록해주세요.");
            $("#passMsg").html("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 3가지 항목이상 포함된경우 최소 8자이상 등록해주세요.");
            return false;
        }
    }
    */

    /*
    var SamePass_0 = 0; //동일문자 카운트
    var SamePass_1 = 0; //연속성(+) 카운드
    var SamePass_2 = 0; //연속성(-) 카운드

    var chr_pass_0;
    var chr_pass_1;

    for(var i=0; i < passwd.length; i++) {
        chr_pass_0 = passwd.charAt(i);
        chr_pass_1 = passwd.charAt(i+1);

        //동일문자 카운트
        if(chr_pass_0 == chr_pass_1) {
            SamePass_0 = SamePass_0 + 1
        }

        //연속성(+) 카운드
        if(chr_pass_0.charCodeAt(0) - chr_pass_1.charCodeAt(0) == 1) {
            SamePass_1 = SamePass_1 + 1
        }

        //연속성(-) 카운드
        if(chr_pass_0.charCodeAt(0) - chr_pass_1.charCodeAt(0) == -1) {
            SamePass_2 = SamePass_2 + 1
        }
    } // for

    if(SamePass_0 > 1) {
    	$("#passMsg").addClass("fail");
        //alert("동일문자를 3번 이상 사용할 수 없습니다.");
        $("#passMsg").html("동일문자를 3번 이상 사용할 수 없습니다.");
        return false;
    }

    if(SamePass_1 > 1 || SamePass_2 > 1 )  {
    	$("#passMsg").addClass("fail");
        //alert("연속된 문자열(123 또는 321, abc, cba 등)을\n 3자 이상 사용 할 수 없습니다.");
        $("#passMsg").html("연속된 문자열(123 또는 321, abc, cba 등)을\n 3자 이상 사용 할 수 없습니다.");
        return false;
    }
    */

	$("#passMsg").addClass("pass");
    $("#passMsg").html("사용 가능한 비밀 번호입니다..");
    return true;
}

function goSubmit() {
	if(!chkValidPasswd($("input[name=passwd]").val())) {
		$("input[name=passwd]").focus();
		return;
	}

	if($("input[name=passwd]").val() != '' && $("input[name=passwd]").val() != $("input[name=passwd2]").val()) {
    	$("#passMsg").addClass("fail");
        $("#passMsg").html("비밀번호 확인이 일치하지 않습니다.");
        $("input[name=passwd2]").focus();
		return;
	}

	ef.proc($("#editForm"), function(result) {
		alert(result.message);
		if(result.succeed) {
			location.href = "/";
		}
	});

}
</script>
</head>
<body>
<div id="popWrapper" class="typeB memberWrap">
<form name="editForm" id="editForm" method="post" action="pwdResetProc">
<input type="hidden" name="memId" value="${param.memId }">
	<h1>${MENU_TITLE}</h1>
	<div id="popCont" class="findArea">
		<p class="text">비밀번호 재설정 해주세요.</p>
		<div class="fieldArea">
			<div class="field">
				<p class="tit">비밀번호 재설정</p>
					<p class="pwdField"><input type="password" name="passwd" id="passwd" title="비밀번호"><span>비밀번호 입력</span></p>
					<p class="pwdField"><input type="password" name="passwd2" id="passwd2" title="비밀번호 확인"><span>비밀번호 입력</span></p>
				<p class="message" id="passMsg"></p><!-- 성공시 addClass:pass, 실패시 addClass:fail -->
				<p class="text">비밀번호는 영문 대문자, 소문자, 숫자, 특수문자 중 최소 2가지 이상의 <br>문자조합 10-16자로 입력해주세요.</p>
			</div>
		</div>
		<div class="btnArea">
			<span><a href="#" onclick="goSubmit();return false;" class="btnTypeA btnSizeD">확인</a></span>
		</div>
	</div><!-- //popCont -->
</form>
</div><!-- //popWrap -->
<script>

</script>
</body>
</html>