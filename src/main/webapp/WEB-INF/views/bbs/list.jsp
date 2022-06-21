<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="utils" scope="request" class="com.pntmall.common.utils.Utils"/>
<c:set var="Depth_1" value="6" scope="request"/>
<c:set var="Depth_2" value="1" scope="request"/>
<c:set var="name" value="자유게시판" scope="request"/>
<c:set var="locUl" value="<ul><li>Dr. 자유게시판</li><li class='on'>자유게시판</li></ul>" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script>
	$(function() {
		$("#fromCdate").datepicker();
		$("#toCdate").datepicker();

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
					<th>제목</th>
					<td >
						<input type="text" name="title" value="${ param.title }" style="width:100%" >
					</td>
					<th>등록일</th>
					<td>
						<div class="dateBox">
							<input type="text" name="fromCdate" id="fromCdate" readonly value="${ param.fromCdate }">
						</div>
						<div class="dateBox">
							<input type="text" name="toCdate" id="toCdate" readonly value="${ param.toCdate }">
						</div>
					</td>
				</tr>
				<tr>
					<th>분류</th>
					<td>
						<select name="cate" style="width:200px">
							<option value="" ${ empty param.cate ? 'selected' : '' }>전체</option>
							<c:forEach items="${ cateList }" var="row">
								<option value="${ row.code1 }${ row.code2 }" ${ param.cate eq row.code1+=row.code2 ? 'selected' : '' }>${ row.name }</option>
							</c:forEach>
						</select>
					</td>
					<th>등록자</th>
					<td>
						<input type="text" name="userName" id="userName" value="${ param.userName }">
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
					<strong class="totalTit">Total</strong> <span class="totalCount"><fmt:formatNumber value="${ fn:length(list) }" pattern="#,###" /></span>
				</div>
			</div>
			<p class="fr">
				<c:import url="/include/pageSize?pageSize=${ adminSearch.pageSize }" />
			</p>
		</div>
		</form>
		<table class="list">
			<colgroup>
				<col width="60px">
				<col width="">
				<col width="">
				<col width="">
				<col width="">
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>분류</th>
					<th>제목</th>
					<th>등록자</th>
					<th>등록일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="row">
				<tr style="background:${row.fixYn eq 'Y' ? '#eeeeee':''}">
					<td><c:out value="${ row.bbsNo }" /></td>
					<td><c:out value="${ row.cateName }" /></td>
					<td><a href="edit?bbsNo=${ row.bbsNo }"><c:out value="${ row.title }" /></a></td>
					<td>${ utils.idMasking(row.uuserId) }(${ utils.nameMasking(row.uuserName) })
					<td><fmt:formatDate value="${row.cdate}" pattern="${ DateTimeFormat }"/></td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="btnArea">
			<a href="edit" class="btnTypeC btnSizeA"><span>등록</span></a>
		</div>
		<div class="paging btnSide"><!-- btnArea와 동시에 노출될때만 btnSide 추가 -->
			${ paging }
		</div>

	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->

</body>
</html>
