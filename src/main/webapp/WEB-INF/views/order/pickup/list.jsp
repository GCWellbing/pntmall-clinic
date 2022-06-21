<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="2" scope="request"/>
<c:set var="Depth_2" value="2" scope="request"/>
<c:set var="name" value="병의원 픽업 관리" scope="request"/>
<c:set var="locUl" value="<ul><li>주문 관리</li><li class='on'>병의원 픽업 관리</li></ul>" scope="request"/>
<jsp:useBean id="utils" scope="request" class="com.pntmall.common.utils.Utils"/>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script>
	$(function() {
		$("#sdate").datepicker();
		$("#edate").datepicker();

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
		<c:import url="/include/location" />
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
					<th>주문일</th>
					<td>
						<div class="dateBox">
							<input type="text" name="sdate" id="sdate" readonly value="${ param.sdate }">
						</div>
						~
						<div class="dateBox">
							<input type="text" name="edate" id="edate" readonly value="${ param.edate }">
						</div>
					</td>
					<th>상태</th>
					<td>
						<select name="status" style="width:200px">
							<option value=""></option>
							<option value="110" ${ param.status eq '110' ? 'selected' : '' }>입금대기</option>
							<option value="120" ${ param.status eq '120' ? 'selected' : '' }>결제완료</option>
							<option value="130" ${ param.status eq '130' ? 'selected' : '' }>WMS전송전</option>
							<option value="140" ${ param.status eq '140' ? 'selected' : '' }>WMS전송</option>
							<option value="410" ${ param.status eq '410' ? 'selected' : '' }>픽업준비중</option>
							<option value="420" ${ param.status eq '420' ? 'selected' : '' }>픽업가능</option>
							<option value="430" ${ param.status eq '430' ? 'selected' : '' }>픽업완료</option>
							<option value="440" ${ param.status eq '440' ? 'selected' : '' }>픽업미완료</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>주문자명</th>
					<td>
						<input type="text" name="oname" value="${ param.oname }" style="width:100%" >
					</td>
					<th>전화번호</th>
					<td>
						<input type="text" name="omtel" value="${ param.omtel }" style="width:100%" >
					</td>
				</tr>
			</table>
			<div class="btnArea ac">
				<input type="submit" class="btnTypeA btnSizeA" value="검색">
				<input type="button" class="btnTypeB btnSizeA" id="resetBtn" value="초기화">
			</div>
		</div>
		<div class="listHead">
			<div class="fl">
				<div class="totalArea">
					<strong class="totalTit">Total</strong> <span class="totalCount"><fmt:formatNumber value="${ count }" pattern="#,###" /></span>
				</div>
			</div>
			<p class="fr">
				<c:import url="/include/pageSize?pageSize=${ param.pageSize }" />
			</p>
		</div>
		</form>

		<table class="list">
			<colgroup>
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
					<th>주문일시</th>
					<th>제품명</th>
					<th>주문자명</th>
					<th>전화번호</th>
					<th>상태</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="row">
					<tr>
						<td><a href="edit?orderid=${ row.orderid }"><c:out value="${ row.orderid }" /></a></td>
						<td><fmt:formatDate value="${ row.odate }" pattern="${ DateTimeFormat }"/></td>
						<td><c:out value="${  row.pname }" /><c:if test='${ row.itemCnt > 1 }'> 외 ${ row.itemCnt - 1 }</c:if></td>
						<td><c:out value="${ utils.nameMasking(row.oname) }" /></td>
						<td><c:out value="${ utils.callNumberMasking(row.omtel1.concat(row.omtel2)) }" /></td>
						<td><c:out value="${ row.boName }" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="paging "><!-- btnArea와 동시에 노출될때만 btnSide 추가 -->
			${ paging }
		</div>

	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->

</body>
</html>
