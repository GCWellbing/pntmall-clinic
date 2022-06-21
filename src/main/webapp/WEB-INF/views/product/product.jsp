<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script>
	$(function() {
		window.resizeTo(1000, 800);
	});

	function setProduct() {
		if($("input[name=pno]:checked").length == 0) {
			alert("제품을 선택하세요.");
			return;
		}

		disableScreen();
		ef.proc($("#listForm"), function(result) {
			alert(result.message);
			if(result.succeed) {
				opener.document.location.reload();
				self.close();
			}

			enableScreen();
		});

	}

	function checkAll(obj) {
		$("input[name=pno]").each(function() {
			$(this).prop("checked", $(obj).prop("checked"));
		});
	}
</script>
</head>
<body>
<div id="popWrapper">
	<h1>제품 검색</h1>
	<div id="popContainer">
		<form name="searchForm" id="searchForm">
		<div class="white_box">
			<table class="board">
				<colgroup>
					<col width="160">
					<col width="">
				</colgroup>
				<tr>
					<th>제품명</th>
					<td>
						<input type="text" name="pname" style="width:100%" value="${ param.pname }">
					</td>
				</tr>
				<tr>
					<th>제품 코드</th>
					<td>
						<input type="number" name="pno" style="width:100%" value="${ param.pno }">
					</td>
				</tr>
				<tr>
					<th>상태</th>
					<td>
						<label><input type="radio" name="status" value="" ${ empty param.status || param.status eq '' ? 'checked' : '' }><span>전체</span></label>
						<label><input type="radio" name="status" value="S" ${ param.status eq 'S' ? 'checked' : '' }><span>공개</span></label>
						<label><input type="radio" name="status" value="H" ${ param.status eq 'H' ? 'checked' : '' }><span>비공개</span></label>
						<label><input type="radio" name="status" value="E" ${ param.status eq 'E' ? 'checked' : '' }><span>단종</span></label>
					</td>
				</tr>
			</table>
		</div>
		<div class="btnArea ac">
			<input type="submit" class="btnTypeA btnSizeA" value="검색">
			<input type="button" class="btnTypeB btnSizeA" id="resetBtn" value="초기화">
		</div>
		<div class="listHead">
			<div class="fl">
				<div class="totalArea">
					<strong class="totalTit">Total</strong> <span class="totalCount"><fmt:formatNumber value="${ count }" pattern="#,###" /></span>
				</div>
			</div>
			<p class="fr">
				<select name="pageSize" onchange="$('#searchForm').submit()" style="width:100px;">
					<option value="10" ${ param.pageSize eq 10 ? 'selected' : '' }>10 Views</option>
					<option value="20" ${ param.pageSize eq 20 ? 'selected' : '' }>20 Views</option>
					<option value="50" ${ param.pageSize eq 50 ? 'selected' : '' }>50 Views</option>
					<option value="100" ${ param.pageSize eq 100 ? 'selected' : '' }>100 Views</option>
				</select>
			</p>
		</div>
		</form>

		<form name="listForm" id="listForm" action="create" method="post">
		<table class="list">
			<colgroup>
				<col width="100px">
				<col width="150px">
				<col width="">
				<col width="80px">
				<col width="80px">
			</colgroup>
			<thead>
				<tr>
					<th>제품코드</th>
					<th>SAP코드</th>
					<th>제품명</th>
					<th>판매가격</th>
					<th><input type="checkbox" onclick="checkAll(this)"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="row">
				<tr>
					<td><c:out value="${ row.pno }" /></td>
					<td><c:out value="${ row.matnr }" /></td>
					<td><c:out value="${ row.pname }" /></td>
					<td><fmt:formatNumber value="${ row.salePrice }" pattern="#,###" /></td>
					<td>
						<input type="checkbox" name="pno" value="${ row.pno }" style="appearance:auto">
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		</form>
		<div class="btnArea">
			<a href="javascript:setProduct();" class="btnTypeC btnSizeA"><span>확인</span></a>
		</div>
		<div class="paging btnSide"><!-- btnArea와 동시에 노출될때만 btnSide 추가 -->
			${ paging }
		</div>
	</div>
</div><!-- //wrapper -->
</body>
</html>
