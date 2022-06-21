<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script>
function chkValidPasswd(passwd) {
    if(passwd.length < 10 ) {
    	alert("비밀번호는 최소 10자 이상입니다.");
        return false;
    }

    if(passwd.length > 16 ) {
    	alert("비밀번호는 17자 이상 입력할 수 없습니다.");
        return false;
    }

    //생성 항목 포함 갯수
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
        alert("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 최소한 2가지 항목이 포함되어야합니다.");
        return false;
    }
    return true;
}

function goSubmit() {
	if(!chkValidPasswd($("input[name=passwd]").val())) {
		$("input[name=passwd]").focus();
		return;
	}

	if($("input[name=passwd]").val() != '' && $("input[name=passwd]").val() != $("input[name=passwd2]").val()) {
    	alert("비밀번호 확인이 일치하지 않습니다.");
        $("input[name=passwd2]").focus();
		return;
	}

	ef.proc($("#editForm"), function(result) {
		alert(result.message);
		if(result.succeed) {
			self.close();
		}
	});

}
</script>
</head>
<body>
<div id="popWrapper">
	<h1>비밀번호 변경</h1>
	<div id="popContainer">
		<form name="editForm" id="editForm" method="post" action="changPassWdProc">
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="160">
					<col width="">
				</colgroup>
				<tr>
					<th>기존 패스워드</th>
					<td>
						<input type="password" name="oldPasswd" id="oldPasswd">
					</td>
				</tr>
				<tr>
					<th>신규 패스워드</th>
					<td>
						<input type="password" name="passwd" id="passwd">
					</td>
				</tr>
				<tr>
					<th>신규 패스워드 확인</th>
					<td>
						<input type="password" name="passwd2" id="passwd2">
					</td>
				</tr>
			</table>
		</div>
		<div class="btnArea">
			<input type="button" id="regist" onclick="goSubmit();return false;" class="btnSizeA btnTypeD" value="수정">
		</div>
		</form>

	</div>
</div><!-- //wrapper -->
</body>
</html>
