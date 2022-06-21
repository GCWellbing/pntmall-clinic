<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="3" scope="request"/>
<c:set var="Depth_2" value="1" scope="request"/>
<c:set var="name" value="1:1문의" scope="request"/>
<c:set var="locUl" value="<ul><li class='on'>1:1문의</li></ul>" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script type="text/javascript" src="/static/se2/js/HuskyEZCreator.js" charset="utf-8"></script>
<script>
$(function(){
	<c:if test="${empty qna.quserName}" >
	alert("탈퇴한 회원입니다.");
	$("#uptBtn").hide();
	</c:if>
	<c:if test="${qna.status eq 'D'}" >
		alert("삭제건입니다.");
		$("#uptBtn").hide();
	</c:if>
});

	var v;

	$(function() {
		v = new ef.utils.Validator($("#editForm"));
		v.add("answer", {
			empty : "답변을 입력하세요.",
			max : "4000"
		});

	});

	function goSubmit() {

		if(v.validate()) {
			disableScreen();
			ef.proc($("#editForm"), function(result) {
				alert(result.message);
				if(result.succeed) location.href = "list";
				enableScreen();
			});
		}
	}

</script>
</head>
<body>
<div id="wrapper">
<c:import url="/include/header" />
	<div id="container">
		<c:import url="/include/location" />
		<form name="editForm" id="editForm" method="POST" action="${ mode }">
			<input type="hidden" name="qnaNo" value="${ qna.qnaNo }" />
			<input type="hidden" name="oldStatus" value="${ qna.status}" />
			<input type="hidden" name="quser" value="${ qna.quser }" />

		<h2>정보 등록</h2>
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="15%">
					<col width="">
					<col width="15%">
					<col width="">
				</colgroup>
				<tr>
					<th>문의유형</th>
					<td>${ qna.cateName}</td>
					<th>답변상태</th>
					<td>${ qna.statusName}</td>
				</tr>
				<tr style="display:${empty qna.pname?'none':'' };">
					<th>문의 제품</th>
					<td colspan=3>${ qna.pname }
					</td>
				</tr>
				<tr style="display:${empty qna.clinicName?'none':'' };">
					<th>병의원</th>
					<td colspan=3>${ qna.clinicName }
					</td>
				</tr>
				<tr>
					<th>제목</th>
					<td colspan=3>${ qna.title }
					</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td>
						<c:if test="${empty qna.quserName}" >
					 		-
				 		</c:if>
						<c:if test="${not empty qna.quser}" >
					 		<a href="/member/general/edit?memNo=${ qna.quser }">${ qna.quserName }</a>
				 		</c:if>
					</td>
					<th>작성일</th>
					<td><fmt:formatDate value="${qna.qdate}" pattern="${ DateTimeFormat }"/></td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan=3>
						<textarea name="" id="" style="width:100%;height:100px" readonly>${ qna.question }</textarea>
					</td>
				</tr>
				<tr style="display:${fn:length(clinicImgList) == 0?'none':'' };">
					<th>첨부파일</th>
					<td colspan=3>
						<c:forEach items="${ clinicImgList }" var="row">
							<div>
								<img src='${row.attach}'>
							</div>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>답변<sup>*</sup></th>
					<td colspan=3>
						<textarea name="answer" id="answer" style="width:100%;height:100px">${ qna.answer }</textarea>
					</td>
				</tr>
			</table>
		</div>

		<div class="white_box">
			<table class="board">
				<tr>
					<th>등록일/등록자</th>
					<td>
						<fmt:formatDate value="${ qna.cdate }" pattern="${ DateTimeFormat }" /> / ${qna.cuserName}
					</td>
					<th>수정일/수정자</th>
					<td>
						<fmt:formatDate value="${qna.udate}" pattern="${ DateTimeFormat }"/> / ${qna.uuserName}
					</td>
				</tr>
			</table>
		</div>

		<div class="btnArea">
			<a href="javascript:goSubmit()" id="uptBtn" class="btnTypeC btnSizeA"><span>수정</span></a>
		</div>
		</form>
	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->

</body>
</html>
