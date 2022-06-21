<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="5" scope="request"/>
<c:set var="Depth_2" value="1" scope="request"/>
<c:set var="name" value="제품관리" scope="request"/>
<c:set var="locUl" value="<ul><li class='on'>제품관리</li></ul>" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script>
	$(function() {
	});

	function removeProduct(pno) {
		if(confirm("삭제하시겠습니까?")) {
			$("input[name=pno]").val(pno);

			disableScreen();
	        ef.proc($("#removeForm"), function(result) {
				if(result.succeed) {
					document.location.reload();
				}
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
		<form name="searchForm" id="searchForm">
		<div class="listHead">
			<div class="fl">
				<div class="totalArea">
					* 병의원에서 현재 보유하고 있는 제품을 추가해주세요. 병의원 픽업 주문 시 해당 제품이 있는 경우, 픽업 가능한 병의원으로 노출됩니다.
				</div>
			</div>
			<p class="fr">
				<a href="#" onclick="showPopup('product', '500', '600'); return false;" class="btnTypeC btnSizeA"><span>제품 추가</span></a>
			</p>
		</div>
		</form>

		<table class="list">
			<colgroup>
				<col width="100px">
				<col width="">
				<col width="100px">
				<col width="100px">
			</colgroup>
			<thead>
				<tr>
					<th>제품코드</th>
					<th>제품명</th>
					<th>판매가</th>
					<th>삭제</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="row">
					<tr>
						<td><c:out value="${ row.pno }" /></td>
						<td><c:out value="${ row.pname }" /></td>
						<td><fmt:formatNumber value="${ row.salePrice }" pattern="#,###" /></td>
						<td>
							<a href="javascript:removeProduct(${ row.pno });" class="btnTypeB btnSizeA"><span>삭제</span></a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->
<form name="removeForm" id="removeForm" action="remove" method="post">
	<input type="hidden" name="pno">
</form>
</body>
</html>
