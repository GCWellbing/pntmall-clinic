<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="Depth_1" value="6" scope="request"/>
<c:set var="Depth_2" value="2" scope="request"/>
<c:set var="name" value="메디잡 채용관" scope="request"/>
<c:set var="locUl" value="<ul><li>Dr. 자유게시판</li><li class='on'>메디잡 채용관</li></ul>" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script>
	$(function() {
	});
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
					<strong class="totalTit">Total</strong> <span class="totalCount"><fmt:formatNumber value="${ count }" pattern="#,###" /></span>
				</div>
			</div>
			<p class="fr">
				<c:import url="/include/pageSize?pageSize=${ adminSearch.pageSize }" />
			</p>
		</div>
		</form>

		<table class="list">
			<colgroup>
				<col width="100px">
				<col width="100px">
				<col width="100px">
				<col width="100px">
				<col width="">
				<col width="">
				<col width="100px">
			</colgroup>
			<thead>
				<tr>
					<th>이름</th>
					<th>성별</th>
					<th>나이</th>
					<th>총경력</th>
					<th>이력서 제목</th>
					<th>희망근무지</th>
					<th>등록일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="row">
					<tr>
						<td><c:out value="${ row.name }" /></td>
						<td><c:out value="${ row.gender }" /></td>
						<td><c:out value="${ row.age }" /></td>
						<td><c:out value="${ row.career }" /></td>
						<td><a href="${ row.url }" target="_blank"><c:out value="${ row.title }" /></a></td>
						<td><c:out value="${ row.area }" /></td>
						<td><c:out value="${ row.registDate}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="btnArea">
<!-- 			<a href="edit" class="btnTypeC btnSizeA"><span>등록</span></a> -->
		</div>
		<div class="paging"><!-- btnArea와 동시에 노출될때만 btnSide 추가 -->
			${ paging }
		</div>

	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->

</body>
</html>
