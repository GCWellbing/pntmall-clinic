<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="2" scope="request"/>
<c:set var="Depth_2" value="3" scope="request"/>
<c:set var="name" value="주문 목록" scope="request"/>
<c:set var="locUl" value="<ul><li>주문 관리</li><li class='on'>주문 목록</li></ul>" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
</head>
<body>
<div id="wrapper">
<c:import url="/include/header" />
	<div id="container">
		<c:import url="/include/location" />
		<h2>주문자 정보</h2>
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="150px">
					<col width="">
					<col width="150px">
					<col width="">
				</colgroup>
				<tr>
					<th>주문번호</th>
					<td><c:out value="${ order.orderid }" /></td>
					<th>주문일</th>
					<td><fmt:formatDate value="${ order.odate }" pattern="${ DateTimeFormat }" /></td>
				</tr>
				<tr>
					<th>아이디</th>
					<td><c:out value="${ order.memId }" /></td>
					<th>주문자명</th>
					<td><c:out value="${ order.oname }" /></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><c:out value="${ order.oemail }" /></td>
					<th>휴대전화</th>
					<td><c:out value="${ order.omtel1 }${ order.omtel2 }" /></td>
				</tr>
				<tr>
					<th>등급</th>
					<td><c:out value="${ order.gradeName }" /></td>
					<th>병의원코드</th>
					<td><c:out value="${ order.clinicId }" /></td>
				</tr>
				<tr>
					<th>디바이스</th>
					<td><c:out value="${ order.device }" /></td>
					<th>첫주문여부</th>
					<td><c:out value="${ order.firstOrderYn }" /></td>
				</tr>
				<tr>
					<th>주문상태</th>
					<td>
						<c:out value="${ order.statusName }" />
						<a href="#" onclick="showPopup('statusLog?orderid=${ param.orderid }', '500', '600'); return false" class="btnSizeA btnTypeB">주문상태 로그</a>
					</td>
					<th>상담자 ID</th>
					<td><c:out value='${ orderMemo.adminId }' /></td>
				</tr>
				<tr>
					<th>메모</th>
					<td colspan="3">
						<form name="memoForm" id="memoForm" method="POST" action="addMemo">
							<input type="hidden" name="orderid" value="${ order.orderid }">
							<textarea name="memo" style="width:90%;height:100px"><c:out value="${ orderMemo.memo }" /></textarea>
						</form>
					</td>
				</tr>
			</table>
		</div>

		<form name="cancelForm" id="cancelForm" method="POST" action="cancel">
			<input type="hidden" name="orderid" value="${ param.orderid }">
		</form>
		<form name="returnForm" id="returnForm" method="POST" action="createReturn">
			<input type="hidden" name="orderid" value="${ param.orderid }">
		<span class="fr">
		</span>
		<c:forEach items='${ shipList }' var='row'>
			<h2>제품정보(${ row.shipGubun eq 1 ? '일반배송' : '냉장배송' })</h2>
			<div class="">
				<table class="list">
					<colgroup>
						<col width="150px">
						<col width="100px">
						<col width="130px">
						<col width="">
						<col width="100px">
						<col width="100px">
						<col width="100px">
						<col width="100px">
					</colgroup>
					<thead>
						<tr>
							<th>주문번호</th>
							<th>제품코드</th>
							<th>SAP코드</th>
							<th>제품명</th>
							<th>수량</th>
							<th>판매가</th>
							<th>쿠폰</th>
							<th>매출가</th>
						</tr>
					</thead>
					<tbody>
						<c:set var='idx' value='0' />
						<c:forEach items='${ row.items }' var='item'>
							<tr>
								<td><c:out value="${ item.orderid }" />_<c:out value="${ item.shipNo }" /></td>
								<td><c:out value="${ item.pno }" /></td>
								<td><c:out value="${ item.matnr }" /></td>
								<td class="al">
									<c:out value="${ item.pname }" />
									<c:if test='${ !empty item.gift }'>
										<c:forEach items="${fn:split(item.gift, '|') }" var="gift">
											<p>[증정품] ${ gift }</p>
										</c:forEach>
									</c:if>
								</td>
								<td><c:out value="${ item.qty }" /></td>
								<td><fmt:formatNumber value="${ item.salePrice }" pattern="#,###" /></td>
								<td><fmt:formatNumber value="${ item.discount }" pattern="#,###" /></td>
								<td><fmt:formatNumber value="${ item.applyPrice }" pattern="#,###" /></td>
							</tr>
							<c:set var='idx' value='${ idx + 1 }' />
						</c:forEach>
						<tr>
							<th colspan="3">배송정보</th>
							<td colspan="5" class="al">
								<c:if test='${ !empty row.invoice }'>
									<c:out value="${ row.shipperName }" /> <c:out value="${ row.invoice }" />
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</c:forEach>
		</form>

		<h2>환불정보</h2>
		<div class="white_box">
			<form name="refundForm" id="refundForm" method="POST" action="addRefund">
				<input type="hidden" name="orderid" value="${ param.orderid }">
			<table class="board">
				<colgroup>
					<col width="150px">
					<col width="">
					<col width="150px">
					<col width="">
				</colgroup>
				<tr>
					<th>환불금액</th>
					<td><input type="number" name="amt" value="${ orderRefund.amt }">원</td>
					<th>환불포인트</th>
					<td><input type="number" name="point" value="${ orderRefund.point }">P</td>
				</tr>
				<tr>
					<th>환불계좌번호</th>
					<td>
						<select name="bank" style="width:150px">
							${ bankOptions }
						</select>
						<input type="number" name="account" style="width:200px" value="${ orderRefund.account }">
					</td>
					<th>예금주</th>
					<td><input type="text" name="depositor" value="${ orderRefund.depositor }"></td>
				</tr>
				<tr>
					<th>반품/교환 사유</th>
					<td colspan="3">
						<select name="reason" style="width:300px">
							<option></option>
							<c:forEach items='${ reasonList }' var='row'>
								<option value="${ row.code1 }${ row.code2 }" ${ orderRefund.reason eq row.code1.concat(row.code2) ? 'selected' : '' }>${ row.name }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>반품/교환 사유2</th>
					<td colspan="3">
						<textarea name="reason2" style="width:80%;height=100px">${ orderRefund.reason2 }</textarea>
					</td>
				</tr>
				<tr>
					<th>반품/교환 거절사유</th>
					<td colspan="3">
						<textarea name="rejectReason" style="width:80%;height=100px">${ orderRefund.rejectReason }</textarea>
					</td>
				</tr>
			</table>
			</form>
		</div>

		<h2>원주문 결제정보</h2>
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="150px">
					<col width="">
					<col width="150px">
					<col width="">
				</colgroup>
				<tr>
					<th>제품총가격</th>
					<td><fmt:formatNumber value="${ orgOrder.amt }" pattern="#,###" />원</td>
					<th>등급할인</th>
					<td>- <fmt:formatNumber value="${ orgOrder.gradeDiscount }" pattern="#,###" />원</td>
				</tr>
				<tr>
					<th>배송비</th>
					<td><fmt:formatNumber value="${ orgOrder.shipAmt }" pattern="#,###" />원</td>
					<th>배송비 할인</th>
					<td>- <fmt:formatNumber value="${ orgOrder.shipDiscount }" pattern="#,###" />원</td>
				</tr>
				<tr>
					<th>쿠폰할인</th>
					<td>- <fmt:formatNumber value="${ orgOrder.couponDiscount }" pattern="#,###" />원</td>
					<th>포인트 사용</th>
					<td>- <fmt:formatNumber value="${ orgOrder.point }" pattern="#,###" />원</td>
				</tr>
				<tr>
					<th>총결제금액</th>
					<td><fmt:formatNumber value="${ orgOrder.payAmt }" pattern="#,###" />원</td>
					<th>결제수단</th>
					<td><c:out value="${ orgOrder.payTypeName }" /></td>
				</tr>
			</table>
		</div>

		<h2>수거지정보</h2>
		<div class="white_box">
			<form name="addrForm" id="addrForm" method="POST" action="addAddr">
				<input type="hidden" name="orderid" value="${ param.orderid }">
			<table class="board">
				<colgroup>
					<col width="150px">
					<col width="">
				</colgroup>
				<tr>
					<th>수취인<sup>*</sup></th>
					<td><input type="text" name="sname" value="${ orderAddr.sname }"></td>
				</tr>
				<tr>
					<th>휴대폰번호<sup>*</sup></th>
					<td>
						<select name="smtel1" style="width:100px">
							<c:forEach items='${ mtel1List }' var='row'>
								<option value="${ row.name }" ${ row.name eq orderAddr.smtel1 ? 'selected' : '' }><c:out value='${ row.name }' /></option>
							</c:forEach>
						</select>
						<input type="number" name="smtel2" value="${ orderAddr.smtel2 }">
					</td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td>
						<select name="stel1" style="width:100px">
							<c:forEach items='${ tel1List }' var='row'>
								<option value="${ row.name }" ${ row.name eq orderAddr.stel1 ? 'selected' : '' }><c:out value='${ row.name }' /></option>
							</c:forEach>
						</select>
						<input type="number" name="stel2" value="${ orderAddr.stel2 }">
					</td>
				</tr>
				<tr>
					<th>우편번호<sup>*</sup></th>
					<td>
						<input type="text" name="szip" value="${ orderAddr.szip}" readonly>
					</td>
				</tr>
				<tr>
					<th>주소<sup>*</sup></th>
					<td>
						<input type="text" name="saddr1" value="${ orderAddr.saddr1 }" style="width:90%" readonly>
						<input type="text" name="saddr2" value="${ orderAddr.saddr2 }" style="width:90%">
					</td>
				</tr>
				<tr>
					<th>요청사항</th>
					<td>
						<select name="msg2" style="width:90%" onchange="setMsg(this.value)">
							<option value="">직접입력</option>
							<c:forEach items='${ msgList }' var='row'>
								<option value="${ row.name }" ${ row.name eq orderAddr.msg ? 'selected' : '' }><c:out value='${ row.name }' /></option>
							</c:forEach>
						</select>
						<input type="text" name="msg" value="${ orderAddr.msg }" style="width:90%">
					</td>
				</tr>
			</table>
			</form>
		</div>

		<c:if test='${ fn:length(orgItemList) > 0 }'>
			<h2>원주문 목록</h2>
			<div class="">
				<table class="list">
					<colgroup>
						<col width="150px">
						<col width="100px">
						<col width="100px">
						<col width="100px">
						<col width="">
						<col width="100px">
						<col width="100px">
						<col width="100px">
					</colgroup>
					<thead>
						<tr>
							<th>주문번호</th>
							<th>일시</th>
							<th>제품코드</th>
							<th>SAP코드</th>
							<th>제품명</th>
							<th>수량</th>
							<th>매출가</th>
							<th>상태</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items='${ orgItemList }' var='row'>
							<tr>
								<td><a href="edit?orderid=<c:out value="${ row.orderid }" />"><c:out value="${ row.orderid }" />_<c:out value="${ row.shipNo }" /></a></td>
								<td><fmt:formatDate value="${ row.cdate }" pattern="${ DateTimeFormat }" /></td>
								<td><c:out value="${ row.pno }" /></td>
								<td><c:out value="${ row.matnr }" /></td>
								<td><c:out value="${ row.pname }" /></td>
								<td><c:out value="${ row.qty }" /></td>
								<td><fmt:formatNumber value="${ row.applyPrice }" pattern="#,###" /></td>
								<td><c:out value="${ row.statusName }" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>

		<div class="btnArea">
			<span class="fl">
				<a href="${ retrivePath }" class="btnTypeC btnSizeA"><span>목록</span></a>
			</span>
		</div>
	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->
<script type="text/javascript">
	$(function() {
	});

	function setMsg(v) {
		$("input[name=msg]").val(v);
	}

</script>

</body>
</html>
