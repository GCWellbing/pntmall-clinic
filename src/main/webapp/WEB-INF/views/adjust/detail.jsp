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
		<table class="list">
			<colgroup>
				<col width="150px">
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
				<tr>
					<th colspan="12" class="al">
						지급일 : <c:out value='${ clinicAdjust.paymentDate }' />
						|
						정산상태 :  
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
					</th>
				</tr>
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
