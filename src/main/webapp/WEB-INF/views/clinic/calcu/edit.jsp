<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="1" scope="request"/>
<c:set var="Depth_2" value="2" scope="request"/>
<c:set var="name" value="정산정보" scope="request"/>
<c:set var="locUl" value="<ul><li>병의원정보관리</li><li class='on'>정산정보</li></ul>" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script type="text/javascript" src="/static/se2/js/HuskyEZCreator.js" charset="utf-8"></script>
<script>
	var v;

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
		        businessName: { required: true,
		            notDefaultText: true
		        },
		        businessOwner: { required: true,
		            notDefaultText: true
		        },
		        businessNo: { required: true,
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
				businessName : {
					required : "병의원명을 입력하세요.",
					notDefaultText : "병의원명을 입력하세요."
				},
				businessOwner : {
					required : "병의원 대표자명을 입력하세요.",
					notDefaultText : "건강기능식품 대표자명을 입력하세요."
				},
				businessNo : {
					required : "병의원 사업자번호를 입력하세요.",
					notDefaultText : "건강기능식품 사업자번호를 입력하세요."
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
					required : "건강기능식품 사업자번호를 입력하세요.",
					notDefaultText : "건강기능식품 사업자번호를 입력하세요."
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
				alert(validator.errorList[0].message);
				validator.errorList[0].element.focus();
			}
		});

	});

	function goSubmit() {
		if (!$("#editForm").valid()){
			return false;
		}

		//건기식 대표자 : 예금주명 일치 여부 확인
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

		disableScreen();
		ef.proc($("#editForm"), function(result) {
			alert(result.message);
			enableScreen();
			if(result.succeed) location.reload();
		});
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
		var field = $(obj).parent().parent().closest('div').attr('id')
		$("#"+field+"File").show();
		$("#"+field+"FileTxt").show();
		$(obj).parent().remove();
	}

	function download(filename, textInput) {
		location.href = "/upload/downloadFile?attach="+textInput+"&attachOrgName="+filename;
	}

</script>
</head>
<body>
<div id="wrapper">
<c:import url="/include/header" />
	<div id="container">
		<c:import url="/include/location" />
		<form name="editForm" id="editForm" method="POST" action="modify">
			<input type="hidden" name="memNo" value="${ clinic.memNo}" />
			<input type="hidden" name="approve" value="${ clinic.approve}" />
		<h2>회원상태</h2>
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="15%">
					<col width="">
				</colgroup>
				<tr>
					<th>구분</th>
					<td>
						${ clinic.approveName }(${ clinic.reason })
					</td>
				</tr>
			</table>
		</div>

		<h2>정산 정보(필수)</h2>
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="15%">
					<col width="">
				</colgroup>
				<tr>
					<th>병의원 사업자 정보</th>
					<td>
						대표자명 : <input type="text" name="businessOwner" id="businessOwner" value="${ clinic.businessOwner }" style="width:200px"><br>
						병의원명 : <input type="text" name="businessName" id="businessName" value="${ clinic.businessName }" style="width:200px"><br>
						사업자번호 : <input type="text" name="businessNo" id="businessNo" value="${ clinic.businessNo }" style="width:200px"><br>
						사업자 등록증 :
						<div id="docuImg">
							<c:set var="docuImgChk" value="N"/>
							<c:forEach items="${ clinicImgList }" var="row">
								<c:if test="${ row.gubun eq 'docu' }">
									<c:set var="docuImgChk" value="Y"/>
								</c:if>
							</c:forEach>

							<input type="file" name="docuImgFile" id="docuImgFile" onchange="uploadImageAll('docuImgFile')" style="display:${docuImgChk eq 'Y' ? 'none':''};">
							<p class="txt" id="docuImgFileTxt" style="display:${docuImgChk eq 'Y' ? 'none':''};">* 최대 5Mbyte / 확장자 : jpg, jpeg, gif, png, bmp, pdf</p>

							<c:forEach items="${ clinicImgList }" var="row">
								<c:if test="${ row.gubun eq 'docu' }">
									<div>
										<input type='hidden' name='attach' value='${row.attach}'>
										<input type='hidden' name='attachOrgName' value='${row.attachOrgName}'>
										<input type='hidden' name='gubun' value='${row.gubun}'>
										<input type='button' class='btnSizeC' onclick='download("${row.attachOrgName}","${row.attach}"); return false;' value='${row.attachOrgName}'>
										<input type='button' class='btnSizeC' onclick='removeClinicImg(this); return false;' value='삭제'>
									</div>
								</c:if>
							</c:forEach>
						</div>
						<br>
						업태 : <input type="text" name="businessType" id=businessType value="${ clinic.businessType }" style="width:200px"><br>
						업종 : <input type="text" name="businessItem" id="businessItem" value="${ clinic.businessItem }" style="width:200px"><br>
					</td>
				</tr>
				<tr>
					<th>건강기능식품 사업자 정보</th>
					<td>
						대표자명 : <input type="text" name="businessOwner2" id="businessOwner2" value="${ clinic.businessOwner2 }" style="width:200px"><br>
						업체명 : <input type="text" name="businessName2" id="businessName2" value="${ clinic.businessName2 }" style="width:200px"><br>
						사업자번호 : <input type="text" name="businessNo2" id="businessNo2" value="${ clinic.businessNo2 }" style="width:200px"><br>
						사업자 등록증 :
						<div id="healthImg">
							<c:set var="healthImgChk" value="N"/>
							<c:forEach items="${ clinicImgList }" var="row">
								<c:if test="${ row.gubun eq 'health' }">
									<c:set var="healthImgChk" value="Y"/>
								</c:if>
							</c:forEach>

							<input type="file" name="healthImgFile" id="healthImgFile" onchange="uploadImageAll('healthImgFile')" style="display:${healthImgChk eq 'Y' ? 'none':''};">
							<p class="txt" id="healthImgFileTxt" style="display:${healthImgChk eq 'Y' ? 'none':''};">* 최대 5Mbyte / 확장자 : jpg, jpeg, gif, png, bmp, pdf</p>

							<c:forEach items="${ clinicImgList }" var="row">
								<c:if test="${ row.gubun eq 'health' }">
									<div>
										<input type='hidden' name='attach' value='${row.attach}'>
										<input type='hidden' name='attachOrgName' value='${row.attachOrgName}'>
										<input type='hidden' name='gubun' value='${row.gubun}'>
										<input type='button' class='btnSizeC' onclick='download("${row.attachOrgName}","${row.attach}"); return false;' value='${row.attachOrgName}'>
										<input type='button' class='btnSizeC' onclick='removeClinicImg(this); return false;' value='삭제'>
									</div>
								</c:if>
							</c:forEach>
						</div>
						<br>
						업태 : <input type="text" name="businessType2" id="businessType2" value="${ clinic.businessType }" style="width:200px"><br>
						업종 : <input type="text" name="businessItem2" id="businessItem2" value="${ clinic.businessItem }" style="width:200px"><br>
					</td>
				</tr>
				<tr>
					<th>정산대금 계좌정보</th>
					<td>
						<ul>
						    <li>건강기능식품 사업자 정보의 대표자명과 예금주명은 일치해야 합니다. 만약 일치하지 않을 경우, 아래 체크박스에 체크해주시기 바랍니다.</li>
					    </ul>
						계좌번호 : <select name="bank" id="bank" style="width:100px">
									<c:forEach items="${ bankList }" var="row">
								    	<option value="${row.code1}${row.code2}" ${row.code1+=row.code2 eq clinic.bank ? 'selected':''} >${row.name}</option>
								    </c:forEach>
								</select>
							    <input type="text" name="account" id="account" value="${ clinic.account }" style="width:200px"><br>
						예금주명 : <input type="text" name="depositor" id="depositor" value="${ clinic.depositor }" style="width:200px">
						        <input type="checkbox" name="depositorNot" id="depositorNot" value="Y" ${ clinic.depositorNot eq 'Y'?'checked':'' }>대표자 예금주 불일치 확인<br>
						통장 사본 :
						<div id="bankImg">
							<c:set var="bankImgChk" value="N"/>
							<c:forEach items="${ clinicImgList }" var="row">
								<c:if test="${ row.gubun eq 'bank' }">
									<c:set var="bankImgChk" value="Y"/>
								</c:if>
							</c:forEach>

							<input type="file" name="bankImgFile" id="bankImgFile" onchange="uploadImageAll('bankImgFile')" style="display:${bankImgChk eq 'Y' ? 'none':''};">
							<p class="txt" id="bankImgFileTxt" style="display:${bankImgChk eq 'Y' ? 'none':''};">* 최대 5Mbyte / 확장자 : jpg, jpeg, gif, png, bmp, pdf</p>

							<c:forEach items="${ clinicImgList }" var="row">
								<c:if test="${ row.gubun eq 'bank' }">
									<div>
										<input type='hidden' name='attach' value='${row.attach}'>
										<input type='hidden' name='attachOrgName' value='${row.attachOrgName}'>
										<input type='hidden' name='gubun' value='${row.gubun}'>
										<input type='button' class='btnSizeC' onclick='download("${row.attachOrgName}","${row.attach}"); return false;' value='${row.attachOrgName}'>
										<input type='button' class='btnSizeC' onclick='removeClinicImg(this); return false;' value='삭제'>
									</div>
								</c:if>
							</c:forEach>
						</div>
						<br>
					</td>
				</tr>
				<tr>
					<th>계약서
						[<input type='button' id="attachBtn" class='btnSizeA btnTypeD valid' onclick='download("${agree.attachOrgName}","${agree.attach}"); return false;' value='다운로드'>]
					</th>
					<td>
						<ul>
							<li>날인된 계약서를 스캔하여 업로드 해주세요.</li>
					    </ul>
						<div id="agreeImg">
							<c:set var="agreeImgChk" value="N"/>
							<c:forEach items="${ clinicImgList }" var="row">
								<c:if test="${ row.gubun eq 'bank' }">
									<c:set var="agreeImgChk" value="Y"/>
								</c:if>
							</c:forEach>

							<input type="file" name="agreeImgFile" id="agreeImgFile" onchange="uploadImageAll('agreeImgFile')" style="display:${agreeImgChk eq 'Y' ? 'none':''};">
							<p class="txt" id="agreeImgFileTxt" style="display:${agreeImgChk eq 'Y' ? 'none':''};">* 최대 5Mbyte / 확장자 : jpg, jpeg, gif, png, bmp, pdf</p>

							<c:forEach items="${ clinicImgList }" var="row">
								<c:if test="${ row.gubun eq 'agree' }">
									<div>
										<input type='hidden' name='attach' value='${row.attach}'>
										<input type='hidden' name='attachOrgName' value='${row.attachOrgName}'>
										<input type='hidden' name='gubun' value='${row.gubun}'>
										<input type='button' class='btnSizeC' onclick='download("${row.attachOrgName}","${row.attach}"); return false;' value='${row.attachOrgName}'>
										<input type='button' class='btnSizeC' onclick='removeClinicImg(this); return false;' value='삭제'>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</td>
				</tr>
			</table>
		</div>

		<div class="btnArea">
			<a href="javascript:goSubmit()" class="btnTypeC btnSizeA"><span>수정</span></a>
		</div>
		</form>

	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->

<div style="display:none" id="addimgTmp">
	<div>
		<img src="" name="clinicImgTag" style="display:none">
		<input type="hidden" name="clinicImg" value="">
		<input type="hidden" name="gubun" value="##GUBUN##">
		<input type='button' class='btnSizeC' onclick='removeClinicImg(this); return false;' value='삭제'>
	</div>
</div>

</body>
</html>
