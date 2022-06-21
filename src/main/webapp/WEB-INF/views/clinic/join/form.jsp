<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="MENU_TITLE" value="PNTmall 병의원 회원가입" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<link rel="stylesheet" href="/static/css/member.css?time=<%=System.currentTimeMillis()%>" type="text/css">
<script type="text/javascript" src="/static/se2/js/HuskyEZCreator.js" charset="utf-8"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
    function sample4_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                	 extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zip').value = data.zonecode;
                document.getElementById("addr1").value = roadAddr;

                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                if(roadAddr !== ''){
                    document.getElementById("addr2").value = extraRoadAddr;
                } else {
                    document.getElementById("addr2").value = '';
                }

            }
        }).open();
    }
</script>
<script>
var isSubmit = false;

$(function() {

	$.validator.addMethod("notDefaultText", function (value, element) {
		   if (value == $(element).attr('placeholder')) {
		      return false;
		   } else {
		       return true;
		   }
		});

	$("#editForm").validate({
		ignore: [],
		rules: {
			name: { required: true,
                notDefaultText: true
            },
			memId: { required: true,
                notDefaultText: true
            },
            passwd: { required: true,
                notDefaultText: true
	        },
	        clinicName: { required: true,
	            notDefaultText: true
	        },
	        zip: { required: true,
	            notDefaultText: true
	        },
	        addr1: { required: true,
	            notDefaultText: true
	        },
	        addr2: { required: true,
	            notDefaultText: true
	        },
            tel1: { required: true,
                notDefaultText: true
            },
            tel2: { required: true,
                notDefaultText: true,
                number: true
            },
            mtel1: { required: true,
                notDefaultText: true
            },
            mtel2: { required: true,
                notDefaultText: true,
                number: true
            },
            email1: { required: true,
                notDefaultText: true
            },
            email2: { required: true,
                notDefaultText: true
            },
            subject: { required: true,
	            notDefaultText: true
	        },
	        businessName: { required: true,
	            notDefaultText: true
	        },
	        businessItem: { required: true,
	            notDefaultText: true
	        },
	        businessType: { required: true,
	            notDefaultText: true
	        },
	        businessOwner2: { required: true,
	            notDefaultText: true
	        },
	        businessName2: { required: true,
	            notDefaultText: true
	        },
	        businessNo2: { required: true,
	            notDefaultText: true
	        },
	        businessItem2: { required: true,
	            notDefaultText: true
	        },
	        businessType2: { required: true,
	            notDefaultText: true
	        },
	        bank: { required: true,
	            notDefaultText: true
	        },
	        account: { required: true,
	            notDefaultText: true
	        },
	        depositor: { required: true,
	            notDefaultText: true
	        }

		},
		messages :{
			name : {
				required : "이름(원장명)을 입력하세요.",
				notDefaultText : "이름(원장명)을 입력하세요."
			},
			memId : {
				required : "ID를 입력하세요.",
				notDefaultText : "ID를 입력하세요."
			},
			passwd : {
				required : "비밀번호를 입력하세요.",
				notDefaultText : "비밀번호를 입력하세요."
			},
			clinicName : {
				required : "병의원명을 입력하세요.",
				notDefaultText : "병의원명을 입력하세요."
			},
			zip : {
				required : "우편번호를 입력하세요.",
				notDefaultText : "우편번호를 선택하세요."
			},
			addr2 : {
				required : "주소를 입력하세요.",
				notDefaultText : "주소를 선택하세요."
			},
			tel1 : {
				required : "전화번호를 선택하세요.",
				notDefaultText : "휴대폰번호를 선택하세요."
			},
			tel2 : {
				required : "전화번호를 입력하세요.",
				notDefaultText : "휴대폰번호를 입력하세요.",
				number: "휴대폰번호를 입력하세요.number"
			},
			mtel1 : {
				required : "휴대폰번호를 선택하세요.",
				notDefaultText : "휴대폰번호를 선택하세요."
			},
			mtel2 : {
				required : "휴대폰번호를 입력하세요.",
				notDefaultText : "휴대폰번호를 입력하세요.",
				number: "휴대폰번호를 입력하세요.number"
			},
			email1 : {
				required : "이메일를 입력하세요.",
				notDefaultText : "이메일를 입력하세요."
			},
			email2 : {
				required : "이메일를 입력하세요.",
				notDefaultText : "이메일를 입력하세요."
			},
			subject : {
				required : "진료과목을 입력하세요.",
				notDefaultText : "진료과목을 입력하세요."
			},
			businessName : {
				required : "병의원명을 입력하세요.",
				notDefaultText : "병의원명을 입력하세요."
			},
			businessItem : {
				required : "업종을 입력하세요.",
				notDefaultText : "업종을 입력하세요."
			},
			businessType : {
				required : "업태를 입력하세요.",
				notDefaultText : "업태를 입력하세요."
			},
			businessOwner2 : {
				required : "건강기능식품 대표자명을 입력하세요.",
				notDefaultText : "건강기능식품 대표자명을 입력하세요."
			},
			businessName2 : {
				required : "건강기능식품 업체명을 입력하세요.",
				notDefaultText : "건강기능식품 업체명을 입력하세요."
			},
			businessNo2 : {
				required : "건강기능식품 사업자(주민)번호를 입력하세요.",
				notDefaultText : "건강기능식품 사업자(주민)번호를 입력하세요."
			},
			businessItem2 : {
				required : "건강기능식품 업종을 입력하세요.",
				notDefaultText : "건강기능식품 업종을 입력하세요."
			},
			businessType2 : {
				required : "건강기능식품 업태를 입력하세요.",
				notDefaultText : "건강기능식품 업태를 입력하세요."
			},
			account : {
				required : "계좌번호를 입력하세요.",
				notDefaultText : "계좌번호를 입력하세요."
			},
			depositor: {
				required : "예금주명을 입력하세요.",
				notDefaultText : "예금주명을 입력하세요."
			}

		},
		errorPlacement: function(error, element) {
			// error.insertAfter(element);
			// error.css({"margin":"0 0 0 0px", "color":"red"});
		},
		invalidHandler: function(form, validator) {
			if(isSubmit){
				alert(validator.errorList[0].message);
				validator.errorList[0].element.focus();
			}
		}
	});

	$("#emailSel").change(function(){
		if($("#emailSel").val() == ""){
			$("#email2").attr("readonly", false);
		}else{
			$("#email2").val("");
			$("#email2").attr("readonly", true);
		}
	});

	$("#memId").change(function(){
		chkValidId();

   	});

	$("#passwd").change(function(){
		chkValidPasswd($("#passwd").val());
	});

	$("#passwd2").change(function(){
		chkValidPasswd($("#passwd").val());
	});


	$("input").change(function(){
		chkRadio();

		isSubmit = false;
		if (!$("#editForm").valid()){
			$('#goJoinBtn').attr('disabled', true);
			return false;
		}

		if($("#checkDuplicate").val() == "N"){
			$('#goJoinBtn').attr('disabled', true);
			return false;
		}

		if(!chkValidPasswd($("input[name=passwd]").val())) {
			$('#goJoinBtn').attr('disabled', true);
			return;
		}

		if($("input[name=passwd]").val() != '' && $("input[name=passwd]").val() != $("input[name=passwd2]").val()) {
			$('#goJoinBtn').attr('disabled', true);
			return;
		}

		$('#goJoinBtn').attr('disabled', false);

	});

});

function chkRadio(){
	var reservationYn = $("input[name=reservationYn]:checked").val();
	var katalkYn = $("input[name=katalkYn]:checked").val();
	var divisionYn = $("input[name=divisionYn]:checked").val();
	var pickupYn = $("input[name=pickupYn]:checked").val();

	if(katalkYn=='Y'){
		$("#katalkId").show();
	}else{
		$("#katalkId").hide();
	}

}

function chkValidId() {
	$("input[name=checkDuplicate]").val("N");
	var memId = $('#memId').val();
	var userIdCheck = RegExp(/^[A-Za-z0-9+]{4,12}$/);

	if($("input[name=memId]").val() == '') {
		return false;
	}

	$("#memIdMsg").removeClass("pass fail");
    if(memId.length < 4) {
    	$("#memIdMsg").addClass("fail");
        $("#memIdMsg").html("아이디는 최소 4자 이상입니다.");
        return false;
    }
    if(memId.length >12) {
    	$("#memIdMsg").addClass("fail");
        $("#memIdMsg").html("아이디는 12자 이상 입력할 수 없습니다.");
        return false;
    }

    if(!userIdCheck.test(memId)){
    	$("#memIdMsg").addClass("fail");
		$("#memIdMsg").html("아이디는 영문 대소문자, 숫자만 입력 가능합니다.");
		return false;
	}

    var result;
	$.ajax ({
	    url: "exists?memId=" + $("input[name=memId]").val(),
	    dataType: "json",
	    async: false,
	    success: function(json) {
			if(json.param.isExists) {
				$("#memIdMsg").addClass("fail");
				$("#memIdMsg").html("동일한 ID가 있습니다.");
				result = false;
			}
			else {
				$("#memIdMsg").addClass("pass");
				$("#memIdMsg").html("사용가능한 아이디입니다.");
				$("input[name=checkDuplicate]").val("Y");
				result = true;
			}
	    }
	});
	return result;
}


function chkValidPasswd(passwd) {
	$("#passMsg").removeClass("pass fail");
	$("#passMsg2").removeClass("pass fail");
    if(passwd.length < 10 ) {
    	$("#passMsg").addClass("fail");
        $("#passMsg").html("비밀번호는 최소 10자 이상입니다.");
        return false;
    }

    if(passwd.length > 16 ) {
    	$("#passMsg").addClass("fail");
        $("#passMsg").html("비밀번호는 17자 이상 입력할 수 없습니다.");
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
        //alert("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 최소한 2가지 항목이 포함되어야합니다.");
        $("#passMsg").html("비밀번호는 대문자, 소문자, 숫자, 특수문자들중 최소한 2가지 항목이 포함되어야합니다.");
        return false;
    }
	$("#passMsg").addClass("pass");
    $("#passMsg").html("사용 가능한 비밀 번호입니다.");

	if($("input[name=passwd]").val() != '' && $("input[name=passwd]").val() != $("input[name=passwd2]").val()) {
    	$("#passMsg2").addClass("fail");
        $("#passMsg2").html("비밀번호 확인이 일치하지 않습니다.");
		return false;
	}else{
        $("#passMsg2").addClass("pass");
        $("#passMsg2").html("비밀번호가 일치합니다.");
	}

    return true;
}


function goSubmit() {
	isSubmit = true;


	memId = $("#memId").val();
	if($("#emailSel").val() != ""){
		$("#email2").val($("#emailSel").val());
	}

	if(!chkValidEmail()){
		return false;
	}

	$("#email").val($("#email1").val() +"@"+ $("#email2").val());

	if (!$("#editForm").valid()){
		return false;
	}

	if(!chkValidId()){
		$("#memId").focus();
		return false;
	}

	if($("#checkDuplicate").val() == "N"){
		alert("아이디가 검증되지 않았습니다.");
		$("#memId").focus();
		return false;
	}

	if(!chkValidPasswd($("input[name=passwd]").val())) {
		$("input[name=passwd]").focus();
		return;
	}

	if($("input[name=passwd]").val() != '' && $("input[name=passwd]").val() != $("input[name=passwd2]").val()) {
		alert("비밀번호 확인이 일치하지 않습니다.");
		$("input[name=passwd2]").focus();
		return;
	}
/*
	$("input[name=gubun]").each(function(idx){
        // 해당 체크박스의 Value 가져오기
        var value = $(this).val();
        var eqValue = $("input[name=chk]:eq(" + idx + ")").val() ;
      });
*/

	//건기식 대표자 예금주명 일치 여부 확인
	if($("#depositor").val() == $("#businessOwner2").val() ){
		if($("#depositorNot").is(":checked") ){
			alert("건강기능식품 대표자와 예금주명이 같으므로 대표자 예금주 불일치 확인을 선택해제하여 주세요!" );
			$("#depositorNot").focus();
			return false;
		}
	}else{
		if(!$("#depositorNot").is(":checked") ){
			alert("건강기능식품 대표자와 예금주명이 다르면 대표자 예금주 불일치 확인을  선택하여 주세요!" );
			$("#depositorNot").focus();
			return false;
		}
	}

	var chkDocu = false;
	var chkHealth = false;
	var chkBank = false;
	var chkAgree = false;
	$("input[name=gubun]").each(function(idx){
        var gubunValue = $("input[name=gubun]:eq(" + idx + ")").val() ;
        if(gubunValue == "docu") chkDocu = true;
        if(gubunValue == "health") chkHealth = true;
        if(gubunValue == "bank") chkBank = true;
        if(gubunValue == "agree") chkAgree = true;
    });
	if(!chkDocu){
		alert("병의원 사업자등록증을 등록해 주세요");
		return false;
	}
	if(!chkHealth){
		alert("건강기능식품 사업자등록증을 등록해 주세요");
		return false;
	}
	if(!chkBank){
		alert("통장사본을 등록해 주세요");
		return false;
	}
	if(!chkAgree){
		alert("계약서를 등록해 주세요");
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

	//사업자번호 검증
	if(isNaN($("#businessNo2").val())){
		alert("건강기능식품 사업자번호는 숫자만 입력 가능합니다.");
		return false;
	}

	if($("#businessNo2").val().length != 10){
		alert("건강기능식품 사업자번호를 10자 입력하세요.");
		return false;
	}

	//카카오 ID check
	if($("input[name=katalkYn]:checked").val()=='Y'){
		if( $("#katalkId").val() == "" || $("#katalkId").val() == "채널 URL 입력" ){
			alert("카카오 상담 가능이면 채널 URL을 입력하셔야 합니다." );
			$("#katalkId").focus();
			return false;
		}
	}

	//알림설정 check
	var reservationYn = $("input[name=reservationYn]:checked").val();
	var katalkYn = $("input[name=katalkYn]:checked").val();
	var divisionYn = $("input[name=divisionYn]:checked").val();
	var pickupYn = $("input[name=pickupYn]:checked").val();

	if( $("#alarmTel2").val() == ""){
		alert("알림설정 연락처를 입력하셔야 합니다." );
		$("#alarmTel2").focus();
		return false;
	}


	ef.proc($("#editForm"), function(result) {
		if(result.succeed) {
			//alert("PNTmall 병의원 온라인 회원가입이 신청되었습니다.\n관리자가 승인 후 등록하신 휴대폰번호로\n가입 완료 안내 톡알림/메세지를 보내드립니다.");
			location.href = "complete";
		}else{
			alert(result.message);
		}

	});

}

function uploadImage(field) {
	var fileName = $("input[name=" + field + "]").val()
	var orgFilename = fileName.split('/').pop().split('\\').pop();

	if($("input[name=" + field + "]").val() != '') {
        disableScreen();

        var action = "/upload/upload?field=" + field + "&path=clinic&fileType=image";
        ef.multipart($("#editForm"), action, field, function(result) {

        	if(result.succeed) {
        		var fieldName = field.replace(/File/gi, '');
        		var gubun = fieldName.replace(/Img/gi, '');

        		var html = "";
				html += "<div>";
				html += "	<img src='"+result.param.uploadUrl+"'>";
				html += "	<input type='hidden' name='attach' value='"+result.param.uploadUrl+"'>";
				html += "	<input type='hidden' name='attachOrgName' value='"+orgFilename+"'>";
				html += "	<input type='hidden' name='gubun' value='"+gubun+"'>";
				html += "	<input type='button' class='btnSizeC' onclick='removeClinicImg(this); return false;' value='삭제'>";
				html += "</div>";
				$("#"+fieldName).append(html);
        	} else {
        		alert(result.message);
        	}

        	$("input[name=" + field + "]").val("");
        	enableScreen();
        });
	}
}

function uploadImageAll(field) {
	var fileName = $("input[name=" + field + "]").val()
	var orgFilename = fileName.split('/').pop().split('\\').pop();

	if($("input[name=" + field + "]").val() != '') {
        disableScreen();

        var action = "/upload/upload?field=" + field + "&path=clinic&fileType=all";
        ef.multipart($("#editForm"), action, field, function(result) {

        	if(result.succeed) {
        		var fieldName = field.replace(/File/gi, '');
        		var gubun = fieldName.replace(/Img/gi, '');

        		var html = "";
				html += "<div>";
				html += "	<input type='hidden' name='attach' value='"+result.param.uploadUrl+"'>";
				html += "	<input type='hidden' name='attachOrgName' value='"+orgFilename+"'>";
				html += "	<input type='button' class='btnSizeC' onclick='download(\""+orgFilename+"\",\""+result.param.uploadUrl+"\"); return false;' value='"+orgFilename+"'>";
				html += "	<input type='hidden' name='gubun' value='"+gubun+"'>";
				html += "	<input type='button' class='btnSizeC' onclick='removeClinicImg(this); return false;' value='삭제'>";
				html += "</div>";
				$("#"+fieldName).append(html);
				$("#"+field).hide();
				$("#"+field+"Txt").hide();
        	} else {
        		alert(result.message);
        	}

        	$("input[name=" + field + "]").val("");
        	enableScreen();
        });
	}
}

function removeClinicImg(obj) {
	var field = $(obj).parent().parent().closest('span').attr('id')
	$("#"+field+"File").show();
	$("#"+field+"FileTxt").show();
	$(obj).parent().remove();
}

function download(filename, textInput) {
	location.href = "/upload/downloadFile?attach="+textInput+"&attachOrgName="+filename;
}


function ckBisNo(bisNo)
{
	${ system ne 'real' ? 'return true;' : '' }

	// 넘어온 값의 정수만 추츨하여 문자열의 배열로 만들고 10자리 숫자인지 확인합니다.
	if ((bisNo = (bisNo+'').match(/\d{1}/g)).length != 10) { return false; }
	// 합 / 체크키
	var sum = 0, key = [1, 3, 7, 1, 3, 7, 1, 3, 5];
	// 0 ~ 8 까지 9개의 숫자를 체크키와 곱하여 합에더합니다.
	for (var i = 0 ; i < 9 ; i++) { sum += (key[i] * Number(bisNo[i])); }
	// 각 8번배열의 값을 곱한 후 10으로 나누고 내림하여 기존 합에 더합니다.
	// 다시 10의 나머지를 구한후 그 값을 10에서 빼면 이것이 검증번호 이며 기존 검증번호와 비교하면됩니다.
	return (10 - ((sum + Math.floor(key[8] * Number(bisNo[8]) / 10)) % 10)) == Number(bisNo[9]);
}

function validateEmail(sEmail) {
	var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	if (filter.test(sEmail)) {
		return true;
	} else {
		return false;
	}
}

function chkValidEmail(){

	var sEmail = $('#email1').val()+"@"+$('#email2').val();
	console.log(sEmail);
	if (validateEmail(sEmail)) {
	    return true;
	} else {
		alert("이메일 형식을 확인해주세요.");
	    return false;
	}
}


</script>
</head>
<body>
<div id="popWrapper" class="memberWrap">
	<div id="popCont">
		<h1 class="line">${MENU_TITLE}<br><span class="fn">(약관동의 > 회원조회 > <b>정보입력</b>)</span></h1>
		<form name="editForm" id="editForm" method="POST" action="Proc">
			<input type="hidden" name="smsYn" value="${param.smsYn}" />
			<input type="hidden" name="emailYn" value="${param.emailYn}" />

		<div class="joinForm">
			<h2>병의원 회원 정보(필수)</h2>
			<div class="white_box">
				<table class="board">
					<colgroup>
						<col width="110px">
						<col width="">
					</colgroup>
					<tr>
						<th>대표자명<sup>*</sup></th>
						<td>
							<input type="text" name="name" id="name" value="${ param.businessOwner }" style="width:200px">
						</td>
					</tr>
					<tr>
						<th>휴대폰 번호<sup>*</sup></th>
						<td>
							<c:set var="mtel" value="${fn:split(clinicSap.mtel,'-')}" />
							<select  name="mtel1" id="mtel1" style="width:110px">
								<c:forEach items="${ mtelList }" var="row">
									<option value="${ row.name }" ${ row.name eq mtel[0] ? 'selected':''  }>${ row.name }</option>
								</c:forEach>
							</select>
							<input type="text" name="mtel2" id="mtel2" value="${ mtel[1] }${ mtel[2] }" style="width:200px" maxlength="8" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');">
						</td>
					</tr>
					<tr>
						<th>이메일 주소<sup>*</sup></th>
						<td>
							<div class="email">
								<p class="emailTxt">
									<c:set var="mail" value="${fn:split(clinicSap.email,'@')}" />
									<input type="hidden" name="email" id="email">
									<input type="text" name="email1" id="email1" value="${ mail[0] }" placeholder="이메일 입력" title="이메일 앞자리">
									<span>@</span>
									<input type="text" name="email2" id="email2" value="${ mail[1] }" title="이메일 뒷자리">
								</p>
								<select name="emailSel" id="emailSel" style="width:150px">
									<option value="">직접입력</option>
									<c:forEach items="${ emailList }" var="row">
										<option value="${ row.name }">${ row.name }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<th>병의원회원ID<sup>*</sup></th>
						<td>
							<input type="text" name="memId" id="memId" value="${clinicSap.clinicSellCd}" style="width:200px">
							<c:if test="${ not empty clinicSap.clinicSellCd }">
									<br>* 오프라인 병의원코드를 온라인 아이디로도 사용하시겠습니까? <br>변경하시려면 재입력을 해주세요.
							</c:if>
							<p id="memIdMsg" class="message">
								* 4~12자 영문 대소문자, 숫자만 가능합니다.
							</p>
						</td>
					</tr>
					<tr>
						<th>패스워드<sup>*</sup></th>
						<td>
							<input type="password" name="passwd" id="passwd" value="" style="width:200px">
							<p class="message" id="passMsg"> 비밀번호는 영문 대문자, 소문자, 숫자, 특수문자 중 최소 2가지 이상의 문자조합 10-16자로 입력해주세요.</p>
						</td>
					</tr>
					<tr>
						<th>패스워드 확인<sup>*</sup></th>
						<td>
							<input type="password" name="passwd2" id="passwd2" value="" style="width:200px">
							<p class="message" id="passMsg2"></p>
						</td>
					</tr>
					<tr>
						<th>병의원명<sup>*</sup></th>
						<td>
							<input type="text" name="clinicName" id="clinicName" value="${clinicSap.businessName}" style="width:200px">
						</td>
					</tr>
					<tr>
						<th>병의원 주소<sup>*</sup></th>
						<td>
							<input type="text" name="zip" id="zip" value="${clinicSap.zip}" style="width:110px">
							<input type="button" onclick="sample4_execDaumPostcode()" value="우편번호 찾기" class="btnSizeA btnTypeD">
							<p style="padding-top:5px"><input type="text" name="addr1" id="addr1" value="${clinicSap.addr1}"></p>
							<p style="padding-top:5px"><input type="text" name="addr2" id="addr2" value="${clinicSap.addr2}"></p>
						</td>
					</tr>
					<tr>
						<th>병의원 전화번호<sup>*</sup><br>(대표전화번호)</th>
						<td>
							<c:set var="tel" value="${fn:split(clinicSap.tel,'-')}" />
							<select  name="tel1" id="tel1" style="width:110px">
								<c:forEach items="${ telList }" var="row">
									<option value="${ row.name }" ${ row.name eq tel[0] ? 'selected':''  }>${ row.name }</option>
								</c:forEach>
							</select>
							<input type="text" name="tel2" id="tel2" value="${ tel[1] }${ tel[2] }" style="width:200px" maxlength="8" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');">
						</td>
					</tr>
					<tr>
						<th>진료과목<sup>*</sup></th>
						<td>
							<input type="text" name="subject" id="subject" value="${clinicSap.subject}" style="width:200px">
							<p class="text">진료과목을 추가할 때 , 로 구분해주세요.</p>
						</td>
					</tr>
					<tr>
						<th>운영시간<sup>*</sup></th>
						<td>
							<p class="text">휴진이거나 점심시간이 없을 경우 체크 박스를 선택해 주세요.</p>
							<p class="text">월요일 <select name="monSh" id="monSh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '9' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="monSm" id="monSm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select> ~
								 <select name="monEh" id="monEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '18' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="monEm" id="monEm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select>
								 <label><input type="checkbox" name="monClose" value="Y" ><span>휴진</span></label>
							</p>
							<p class="text">화요일 <select name="tueSh" id="tueSh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '9' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="tueSm" id="tueSm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select> ~
								 <select name="tueEh" id="tueEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '18' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="tueEm" id="tueEm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select>
								 <label><input type="checkbox" name="tueClose" value="Y" ><span>휴진</span></label>
							</p>
							<p class="text">수요일 <select name="wedSh" id="wedSh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '9' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="wedSm" id="wedSm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select> ~
								 <select name="wedEh" id="wedEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '18' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="wedEm" id="wedEm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select>
								 <label><input type="checkbox" name="wedClose" value="Y" ><span>휴진</span></label>
							</p>
							<p class="text">목요일 <select name="thuSh" id="thuSh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '9' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="thuSm" id="thuSm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select> ~
								 <select name="thuEh" id="thuEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '18' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="thuEm" id="thuEm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select>
								 <label><input type="checkbox" name="thuClose" value="Y" ><span>휴진</span></label>
							</p>
							<p class="text">금요일 <select name="friSh" id="friSh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '9' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="friSm" id="friSm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select> ~
								 <select name="friEh" id="friEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '18' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="friEm" id="friEm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select>
								 <label><input type="checkbox" name="friClose" value="Y" ><span>휴진</span></label>
							</p>
							<p class="text">토요일 <select name="satSh" id="satSh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '9' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="satSm" id="satSm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select> ~
								 <select name="satEh" id="satEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '18' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="satEm" id="satEm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select>
								 <label><input type="checkbox" name="satClose" value="Y" ><span>휴진</span></label>
							</p>
							<p class="text">일요일 <select name="sunSh" id="sunSh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}">${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="sunSm" id="sunSm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select> ~
								 <select name="sunEh" id="sunEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}">${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="sunEm" id="sunEm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select>
								 <label><input type="checkbox" name="sunClose" value="Y" checked><span>휴진</span></label>
							</p>
							<p class="text">공휴일 <select name="holidaySh" id="holidaySh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}">${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="holidaySm" id="holidaySm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select> ~
								 <select name="holidayEh" id="holidayEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}">${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="holidayEm" id="holidayEm" style="width:60px">
						    		<option value="00" "selected">00</option>
						    		<option value="30" >30</option>
								 </select>
								 <label><input type="checkbox" name="holidayClose" value="Y" checked><span>휴진</span></label>
							</p>
							<p class="text">점심시간 <select name="lunchSh" id="lunchSh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '13' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="lunchSm" id="lunchSm" style="width:60px">
						    		<option value="00"  "selected">00</option>
						    		<option value="30"  >30</option>
								 </select> ~
								 <select name="lunchEh" id="lunchEh" style="width:60px">
									<c:forEach var="hour" begin="0" end="24">
								    	<option value="${hour}"  ${hour eq '14' ? 'selected':''}>${hour}</option>
								    </c:forEach>
								 </select>
								 <select name="lunchEm" id="lunchEm" style="width:60px">
						    		<option value="00"  selected>00</option>
						    		<option value="30"  >30</option>
								 </select>
								 <label><input type="checkbox" name="lunchYn" value="Y" ><span>없음</span></label>
							</p>
							<p class="text">안내사항 <label><input type="checkbox" name="noticeYn" value="Y" ><span>등록</span></label>
							        <textarea name="notice" id="notice" style="height:100px"></textarea>
							</p>
						</td>
					</tr>
				</table>
			</div>

			<h2>정산 정보(필수)</h2>
			<ul class="caution2">
				<li>정산을 위해 정확한 정보를 입력해 주세요.</li>
				<li>PNTmall은 병의원 전용 건강기능식품 브랜드로 병의원 사업자와 건강기능식품 사업자를 모두 보유한 회원만 병의원 회원으로 가입할 수 있습니다.</li>
				<li>대표자명과 예금주명은 일치해야 됩니다.</li>
				<li>업태, 업종이 2개 이상인 경우 쉼표(,)로 구분하여 작성해 주세요.</li>
				<li>첨부서류는 최대 5Mbyte까지 업로드 가능합니다.<br>
					(가능한 확장자: jpg, jpeg, gif, png, bmp, pdf)
				</li>
			</ul>


			<div class="white_box">
				<table class="board">
					<colgroup>
						<col width="110px">
						<col width="">
					</colgroup>
					<tr>
						<th>병의원 <br>사업자 정보<sup>*</sup></th>
						<td>
							<p class="text">대표자명 <input type="text" name="businessOwner" id="businessOwner" value="${param.businessOwner}" style="width:200px" readonly></p>
							<p class="text">병의원명 <input type="text" name="businessName" id="businessName" value="${clinicSap.businessName}" style="width:200px"></p>
							<p class="text">사업자번호 <input type="text" name="businessNo" id="businessNo" value="${param.businessNo}" style="width:200px" readonly></p>
							<p class="text">사업자등록증
								<span id="docuImg">
									<input type="file" name="docuImgFile" id="docuImgFile" onchange="uploadImageAll('docuImgFile')"><br>
									<span class="txt" id="docuImgFileTxt">* 최대 5Mbyte / 확장자 jpg, jpeg, gif, png, bmp, pdf</span>
								</span>
							</p>
							<p class="text">업태 <input type="text" name="businessType" id=businessType value="" style="width:200px"></p>
							<p class="text">업종 <input type="text" name="businessItem" id="businessItem" value="" style="width:200px"></p>
						</td>
					</tr>
					<tr>
						<th>건강기능식품 <br>사업자 정보<sup>*</sup></th>
						<td>
							<p class="text">대표자명 <input type="text" name="businessOwner2" id="businessOwner2" value="" style="width:200px"></p>
							<p class="text">업체명 <input type="text" name="businessName2" id="businessName2" value="" style="width:200px"></p>
							<p class="text">사업자번호 <input type="text" name="businessNo2" id="businessNo2" value="" style="width:200px"></p>
							<p class="text">사업자등록증
								<span id="healthImg">
									<input type="file" name="healthImgFile" id="healthImgFile" onchange="uploadImageAll('healthImgFile')"><br>
									<span class="txt" id="healthImgFileTxt">* 최대 5Mbyte / 확장자 jpg, jpeg, gif, png, bmp, pdf</span>
								</span>
							</p>
							<p class="text">업태 <input type="text" name="businessType2" id="businessType2" value="" style="width:200px"></p>
							<p class="text">업종 <input type="text" name="businessItem2" id="businessItem2" value="" style="width:200px"></p>
						</td>
					</tr>
					<tr>
						<th>정산대금 <br>계좌정보<sup>*</sup></th>
						<td>
							<p class="text">건강기능식품 사업자 정보의 대표자명과 예금주명은 일치해야 합니다. 만약 일치하지 않을 경우, 아래 체크박스에 체크해주시기 바랍니다.</p>
							<p class="text">계좌번호
								<select name="bank" id="bank" style="width:110px">
									<c:forEach items="${ bankList }" var="row">
								    	<option value="${row.code1}${row.code2}" >${row.name}</option>
								    </c:forEach>
								</select>
							    <input type="text" name="account" id="account" value="" style="width:200px">
							</p>
							<p class="text">예금주명 <input type="text" name="depositor" id="depositor" value="" style="width:200px">
								<label><input type="checkbox" name="depositorNot" id="depositorNot" value="Y" ><span>대표자 예금주 불일치 확인</span></label>
							</p>
							<p class="text">통장 사본
								<span id="bankImg">
									<input type="file" name="bankImgFile" id="bankImgFile" onchange="uploadImageAll('bankImgFile')"><br>
									<span class="txt" id="bankImgFileTxt" >* 최대 5Mbyte / 확장자 jpg, jpeg, gif, png, bmp, pdf</span>
								</span>
							</p>
						</td>
					</tr>
					<tr>
						<th>계약서<sup>*</sup><br>
							[<input type='button' id="attachBtn" class='btnSizeA btnTypeD valid' onclick='download("${agree.attachOrgName}","${agree.attach}"); return false;' value='다운로드'>]
						</th>
						<td>
							<p class="text">날인된 계약서를 스캔하여 업로드 해주세요.</p>
							<span id="agreeImg">
								<input type="file" name="agreeImgFile" id="agreeImgFile" onchange="uploadImageAll('agreeImgFile')"><br>
								<span class="txt" id="agreeImgFileTxt">* 최대 5Mbyte / 확장자 jpg, jpeg, gif, png, bmp, pdf</span>
							</span>
						</td>
					</tr>
				</table>
			</div>

			<h2>추가정보(선택)</h2>
			<div class="white_box">
				<table class="board">
					<colgroup>
						<col width="110px">
						<col width="">
					</colgroup>
					<tr>
						<th>예약 가능 여부</th>
						<td>
							<label><input type="radio" name="reservationYn" value="Y" ><span>가능</span></label>
							<label><input type="radio" name="reservationYn" value="N" ><span>불가능</span></label>
							<ul class="caution2">
								<li>Dr.PNT제품 상담 외에도 진료, 기타 상담 등의 진행을 위해 PNTmall에서 미리 진행된 예약을 기반으로 예약한 고객 안내 및 응대, 예약 관리(예약 확인 및 확정/취소)가 가능한 병의원</li>
							    <li>업무 범위: 예약건 확인 및 접수, 취소 시 사유 입력(clinic.pntmall.com에서 관리), 확정된 예약건에 따라 고객 방문 시 응대 및 상담, 노쇼 및 일정 변경 등 관리 </li>
						    </ul>
						</td>
					</tr>
					<tr>
						<th>카톡 상담 여부</th>
						<td>
							<label><input type="radio" name="katalkYn" value="Y" ><span>가능</span></label>
							<label><input type="radio" name="katalkYn" value="N" ><span>불가능</span></label><br>
							<input type="text" name="katalkId" id="katalkId" value="" style="width:300px" placeholder="채널 URL 입력" style="display:none;">
							<ul class="caution2">
								<li>카톡 상담이 가능한 경우, 채널 채팅 URL을 입력해주세요. (ex. http://pf.kakao.com/_jbLbu/chat)</li>
								<li>채팅 URL 확인 방법: 카카오톡 채널 관리자 센터 - 채널 홍보 메뉴 - 채팅 URL 복사</li>
								<li>병의원 카카오톡 채널을 통해 운영 시간 내 실시간으로 상담이 가능한 병의원</li>
							    <li>업무 범위: 고객 응대 및 상담(Dr.PNT제품 구매 문의, 예약 일정 확인/변경 문의, 위치 문의 등 각종 문의 사항)</li>
						    </ul>
						</td>
					</tr>
					<tr>
						<th>소분 가능 여부</th>
						<td>
							<label><input type="radio" name="divisionYn" value="Y" ><span>가능</span></label>
							<label><input type="radio" name="divisionYn" value="N" ><span>불가능</span></label>
							<ul class="caution2">
								<li class="colorE">소분가능병의원은 개인맞춤형 건강기능식품 추천을 위해 화상상담 및 해당 서비스 일체를 진행하는 병의원을 의미합니다. 소분가능병의원 지정을 원하실 경우 반드시 당사와 우선 협의 바랍니다.</li>
								<li>Dr.PNT의 닥터팩 소분 서비스(규제샌드박스 특례 실증 사업)를 위한 개인 맞춤형 건강기능식품 추천, 상담, 판매 등 업무 수행이 가능한 병의원</li>
							    <li>업무 범위: 화상 또는 대면 상담을 통한 개인맞춤형 건강기능식품 소분 추천, 상담, 상담 예약 관리 및 닥터팩 소분 구성 승인을 포함한 안내 및 관리(clinic.pntmall.com에서 관리)</li>
						    </ul>
						</td>
					</tr>
					<tr>
						<th>픽업 가능 여부</th>
						<td>
							<label><input type="radio" name="pickupYn" value="Y" ><span>가능</span></label>
							<label><input type="radio" name="pickupYn" value="N" ><span>불가능</span></label>
							<ul class="caution2">
								<li>Dr.PNT 제품을 고객이 병의원에 방문해 직접 수령해가는 픽업 서비스를 위해 고객 방문 시 안내, 제품 수령 절차 수행, 제품 재고 확인 등 업무 수행이 가능한 병의원(고객은 PNTmall에서 결제까지 완료한 상태이며, 병의원에서는 내원한 신규 고객을 병의원 신규 환자로 전환 가능)</li>
							    <li>업무 범위: 고객이 병의원 방문 시 응대 및 안내, 픽업 일정 확인 및 픽업 제품 전달, 재고 유무 관리(clinic.pntmall.com에서 관리), 재고 부족 등 이슈 발생 시 고객 컨택 후 PNTmall 관리자에 전달 등 전반적인 고객 안내 및 관리</li>
						    </ul>
						</td>
					</tr>
					<tr id="alarmTr">
						<th>알림 설정<sup>*</sup></th>
						<td>
							연락처
							<select  name="alarmTel1" id="alarmTel1" style="width:110px">
								<c:forEach items="${ mtelList }" var="row">
									<option value="${ row.name }">${ row.name }</option>
								</c:forEach>
							</select>
							<input type="text" name="alarmTel2" id="alarmTel2" value="" style="width:200px" maxlength="8" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');"><br>
						</td>
					</tr>
					<tr>
						<th>SNS<br>(https://포함)</th>
						<td>
							<p class="text">블로그 <input type="text" name="blog" id="blog" value="" style="width:200px"></p>
							<p class="text">유투브 <input type="text" name="youtube" id="youtube" value="" style="width:200px"></p>
							<p class="text">페이스북 <input type="text" name="facebook" id="facebook" value="" style="width:200px"></p>
							<p class="text">인스타그램 <input type="text" name="instagram" id="instagram" value="" style="width:200px"></p>
							<p class="text">트위터 <input type="text" name="twitter" id="twitter" value="" style="width:200px"></p>
						</td>
					</tr>
				</table>
			</div>
		</div>

		<div class="btnArea">
			<span><a href="#" class="btnTypeA btnSizeD" onclick="goSubmit();return false;" >등록</a></span>
		</div>
		</form>

	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->

</body>
</html>
