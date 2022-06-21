<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="7" scope="request"/>
<c:set var="Depth_2" value="1" scope="request"/>
<c:set var="name" value="정산금액확인" scope="request"/>
<c:set var="locUl" value="<ul><li>정산관리</li><li class='on'>정산금액확인</li></ul>" scope="request"/>
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
			<h1>정산금액 확인</h1>
			<ul>
				<li>정산관리</li>
				<li class="on">정산금액확인</li>
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
						<select name="year" style="width:100px">
							<c:forEach var='i' begin='2021' end='${ thisYear }'>
								<option value='${ thisYear - i + 2021 }' ${ thisYear - i + 2021 eq clinicAdjustSearch.year ? 'selected' : '' }>${ thisYear - i + 2021 }</option>
							</c:forEach>
						</select>
						<select name="quarter" style="width:100px">
							<c:forEach var='i' begin='1' end='4'>
								<option value='${ i }' ${ i eq clinicAdjustSearch.quarter ? 'selected' : '' }>${ i }분기</option>
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
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="130px">
					<col width="">
					<col width="130px">
					<col width="">
					<col width="130px">
					<col width="">
					<col width="130px">
					<col width="">
				</colgroup>
				<tr>
					<th>총매출가(원)</th>
					<td><fmt:formatNumber value="${ clinicAdjust.totSaleAmt }" pattern="#,###" /></td>
					<th>주문건수</th>
					<td><fmt:formatNumber value="${ clinicAdjust.orderCnt }" pattern="#,###" /></td>
					<th>정산금액(원)</th>
					<td><fmt:formatNumber value="${ clinicAdjust.adjustAmt }" pattern="#,###" /></td>
					<th>정산상태</th>
					<td>
						<c:choose>
							<c:when test='${ clinicAdjust.status eq 10 }'>
								정산예정
							</c:when>
							<c:when test='${ clinicAdjust.status eq 20 }'>
								정산확인요청
							</c:when>
							<c:when test='${ clinicAdjust.status eq 30 }'>
								세금계산서발행요청
							</c:when>
							<c:when test='${ clinicAdjust.status eq 40 }'>
								세금계산서수정요청
							</c:when>
							<c:when test='${ clinicAdjust.status eq 50 }'>
								세금계산서확인
							</c:when>
							<c:when test='${ clinicAdjust.status eq 60 }'>
								지급준비중
							</c:when>
							<c:when test='${ clinicAdjust.status eq 70 }'>
								지급완료
							</c:when>
							<c:when test='${ clinicAdjust.status eq 80 }'>
								미정산
							</c:when>
						</c:choose>
						<c:if test='${ clinicAdjust.status eq 20 }'>
							<input type="button" class="btnTypeC btnSizeA" onclick="goConfirm(); return false;" value="정산 금액 확인">
						</c:if> 
					</td>
				</tr>
			</table>
		</div>
		<c:if test='${ !empty clinicAdjust }'>
			<div class="white_box">
				<table class="board">
					<colgroup>
						<col width="">
					</colgroup>
					<tr>
						<td class="ac">
							정산금액 확인 후 세금계산서 발행이 필요합니다.<br>
							공급자: 건강기능식품 사업자 정보 / 공급받는자: 녹십자웰빙<br>
							공급가액: <fmt:formatNumber value="${ clinicAdjust.adjustAmt / 1.1 }" pattern="#,###" />
							세액: <fmt:formatNumber value="${ clinicAdjust.adjustAmt / 11 }" pattern="#,###" />
							합계금액: <fmt:formatNumber value="${ clinicAdjust.adjustAmt }" pattern="#,###" /><br>
							녹십자웰빙 사업자등록증 및 세금계산서 발행 방법은 공지사항을 참고해주세요. 
						</td>
					</tr>
				</table>
			</div>
		</c:if>
		
		<table class="list">
			<colgroup>
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
			</colgroup>
			<thead>
				<tr>
					<th>주문번호</th>
					<th>구매확정일</th>
					<th>주문유형</th>
					<th>주문상태</th>
					<th>상품명</th>
					<th>총출하가</th>
					<th>총결제금액</th>
					<th>수량</th>
					<th>총품목금액</th>
					<th>포인트</th>
					<th>배송비</th>
					<th>할인금액</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="row">
					<tr>
						<td><c:out value="${ row.orderid }" /></td>
						<td>
							<c:choose>
								<c:when test='${ row.gubun eq 3 }'>
									<fmt:formatDate value="${ row.returnDate }" pattern="${ DateFormat }" />
								</c:when>
								<c:otherwise>
									<fmt:formatDate value="${ row.confirmDate }" pattern="${ DateFormat }" />
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:if test='${ row.orderGubun eq 1 }'>일반</c:if>
							<c:if test='${ row.orderGubun eq 3 }'>병의원픽업</c:if>
							<c:if test='${ row.orderGubun eq 4 }'>닥터팩</c:if>
						</td>
						<td><c:out value="${ row.boName }" /></td>
						<td><c:out value="${ row.pname }" /><c:if test='${ row.itemCnt > 1 }'>외 ${ row.itemCnt - 1 }종</c:if></td>
						<td class="ar"><fmt:formatNumber value="${ row.status eq '380' ? row.supplyAmt * -1 : row.supplyAmt }" pattern="#,###" /></td>
						<td class="ar"><fmt:formatNumber value="${ row.payAmt }" pattern="#,###" /></td>
						<td class="ar"><fmt:formatNumber value="${ row.itemQty }" pattern="#,###" /></td>
						<td class="ar"><fmt:formatNumber value="${ row.status eq '380' ? row.saleAmt * -1 : row.saleAmt }" pattern="#,###" /></td>
						<td class="ar"><fmt:formatNumber value="${ row.point }" pattern="#,###" /></td>
						<td class="ar"><fmt:formatNumber value="${ row.shipAmt }" pattern="#,###" /></td>
						<td class="ar"><fmt:formatNumber value="${ row.totDiscount }" pattern="#,###" /></td>
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
