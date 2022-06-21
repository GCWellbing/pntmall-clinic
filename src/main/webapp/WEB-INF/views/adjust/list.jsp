<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="7" scope="request"/>
<c:set var="Depth_2" value="2" scope="request"/>
<c:set var="name" value="정산금액 내역조회" scope="request"/>
<c:set var="locUl" value="<ul><li>정산관리</li><li class='on'>정산금액 내역조회</li></ul>" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script>
	$(function() {
		$("#fromDate").datepicker();
		$("#toDate").datepicker();

		$("#resetBtn").click(function() {
			document.location.href = "list";
		});
	});
</script>
</head>
<body>
<div id="wrapper">
<c:import url="/include/header" />
	<div id="container">
		<div id="location">
			<h1>정산처리 내역 조회</h1>
			<ul>
				<li>정산관리</li>
				<li class="on">정산처리 내역 조회</li>
			</ul>
		</div>
		<form name="searchForm" id="searchForm">
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="10%">
					<col width="">
					<col width="10%">
					<col width="">
				</colgroup>
				<tr>
					<th>정산기간</th>
					<td colspan="3">
						<select name="year1" style="width:100px">
							<c:forEach var='i' begin='2021' end='${ thisYear }'>
								<option value='${ thisYear - i + 2021 }' ${ thisYear - i + 2021 eq clinicAdjustSearch.year1 ? 'selected' : '' }>${ thisYear - i + 2021 }</option>
							</c:forEach>
						</select>
						<select name="quarter1" style="width:100px">
							<c:forEach var='i' begin='1' end='4'>
								<option value='${ i }' ${ i eq clinicAdjustSearch.quarter1 ? 'selected' : '' }>${ i }분기</option>
							</c:forEach>
						</select>
						~
						<select name="year2" style="width:100px">
							<c:forEach var='i' begin='2021' end='${ thisYear }'>
								<option value='${ thisYear - i + 2021 }' ${ thisYear - i + 2021 eq clinicAdjustSearch.year2 ? 'selected' : '' }>${ thisYear - i + 2021 }</option>
							</c:forEach>
						</select>
						<select name="quarter2" style="width:100px">
							<c:forEach var='i' begin='1' end='4'>
								<option value='${ i }' ${ i eq clinicAdjustSearch.quarter2 ? 'selected' : '' }>${ i }분기</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
			<div class="btnArea ac">
				<input type="submit" class="btnTypeA btnSizeA" value="검색">
			</div>
		</div>
		</form>
		<table class="list">
			<colgroup>
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
			</colgroup>
			<thead>
				<tr>
					<th>정산기간</th>
					<th>정산금액(원)</th>
					<th>주문건수</th>
					<th>정산상태</th>
					<th>지급일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="row">
					<tr>
						<td><a href="detail?year=${ row.year }&quarter=${ row.quarter }"><c:out value="${ row.year }" />년 <c:out value="${ row.quarter }" />분기</a></td>
						<td><fmt:formatNumber value="${ row.adjustAmt }" pattern="#,###" /></td>
						<td><fmt:formatNumber value="${ row.orderCnt }" pattern="#,###" /></td>
						<td>
							<c:choose>
								<c:when test='${ row.status eq 10 }'>
									정산예정
								</c:when>
								<c:when test='${ row.status eq 20 }'>
									정산확인요청
								</c:when>
								<c:when test='${ row.status eq 30 }'>
									세금계산서발행요청
								</c:when>
								<c:when test='${ row.status eq 40 }'>
									세금계산서수정요청
								</c:when>
								<c:when test='${ row.status eq 50 }'>
									세금계산서확인
								</c:when>
								<c:when test='${ row.status eq 60 }'>
									지급준비중
								</c:when>
								<c:when test='${ row.status eq 70 }'>
									지급완료
								</c:when>
								<c:when test='${ row.status eq 80 }'>
									미정산
								</c:when>
							</c:choose>
						</td>
						<td><c:out value="${ row.paymentDate }" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->

</body>
<script>
	function goConfirm() {
		$("#searchForm").attr("action", "confirmProc");
		$("#searchForm").attr("method", "post");
		
		disableScreen();
        ef.proc($("#searchForm"), function(result) {
			alert(result.message);
			document.location.reload();
			enableScreen();
		});
	}
</script>
</html>
