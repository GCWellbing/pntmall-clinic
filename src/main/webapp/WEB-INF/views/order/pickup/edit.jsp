<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="2" scope="request"/>
<c:set var="Depth_2" value="2" scope="request"/>
<c:set var="name" value="병의원 픽업 관리" scope="request"/>
<c:set var="locUl" value="<ul><li>주문 관리</li><li class='on'>병의원 픽업 관리</li></ul>" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script type="text/javascript" src="/static/se2/js/HuskyEZCreator.js" charset="utf-8"></script>
<script>

	$(function() {
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
					<th>주문상태</th>
					<td>
						<c:out value="${ order.boName }" />
					</td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th>메모</th>
					<td colspan="3">
						<form name="memoForm" id="memoForm" method="POST" action="addMemo">
							<input type="hidden" name="orderid" value="${ order.orderid }">
							<textarea name="memo" style="width:90%;height:100px"><c:out value="${ order.memo }" /></textarea>
							<a href="javascript:goMemo()" class="btnSizeA btnTypeC"><span>등록</span></a>
						</form>
					</td>
				</tr>
			</table>
		</div>

		<span class="fr">
			<c:choose>
				<c:when test='${ fn:indexOf("110,120", order.status) >= 0 }'>
					<a href="#" onclick="goCancel(); return false;" class="btnSizeA btnTypeD">전체주문 취소</a>
				</c:when>
				<c:when test='${ fn:indexOf("410", order.status) >= 0 }'>
					<a href="#" onclick="goStatus('420'); return false;" class="btnSizeA btnTypeD">픽업 가능</a>
				</c:when>
				<c:when test='${ fn:indexOf("420", order.status) >= 0 }'>
					<a href="#" onclick="goStatus('430'); return false;" class="btnSizeA btnTypeD">픽업 완료</a>
					<a href="#" onclick="goStatus('440'); return false;" class="btnSizeA btnTypeD">픽업 미완료</a>
				</c:when>
			</c:choose>
		</span>
		<h2>제품정보</h2>
		<div class="">
			<table class="list">
				<colgroup>
					<col width="150px">
					<col width="100px">
					<col width="">
					<col width="100px">
					<col width="100px">
					
				</colgroup>
				<thead>
					<tr>
						<th>주문번호</th>
						<th>제품코드</th>
						<th>제품명</th>
						<th>수량</th>
						<th>판매가</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items='${ itemList }' var='item'>
						<tr>
							<td><c:out value="${ item.orderid }" />_<c:out value="${ item.shipNo }" /></td>
							<td><c:out value="${ item.pno }" /></td>
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
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->
<form name="statusForm" id="statusForm" action="modifyStatus" method="post">
	<input type="hidden" name="orderid" value="${ order.orderid }">
	<input type="hidden" name="status">
</form>
<form name="cancelForm" id="cancelForm" method="POST" action="cancel">
	<input type="hidden" name="orderid" value="${ param.orderid }">
</form>
</body>
<script>
	function goMemo() {
		disableScreen();
		ef.proc($("#memoForm"), function(result) {
			alert(result.message);
			enableScreen();
		});
	}
	
	function goStatus(status) {
		if(confirm("주문상태를 변경하시겠습니까?")) {
			$("#statusForm input[name=status]").val(status);

			disableScreen();
			ef.proc($("#statusForm"), function(result) {
				alert(result.message);
				document.location.reload();;
			});
		}
	}

	function goCancel() {
		<c:choose>
			<c:when test='${ order.status eq "120" && order.payType eq "013003"}'>
				showPopup('/popup/refundAccount?orderid=${ param.orderid }', '500', '600');
			</c:when>
			<c:otherwise>
				if(confirm("취소처리를 하시겠습니까?")) {
					disableScreen();
			        ef.proc($("#cancelForm"), function(result) {
						alert(result.message);
						document.location.reload();
					});
				}
			</c:otherwise>
		</c:choose>
	}
</script>
</html>
