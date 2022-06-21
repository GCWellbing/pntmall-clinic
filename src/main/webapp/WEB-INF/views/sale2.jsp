<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="num" value="1" />
<c:forEach items="${ list }" var="row">
	<tr>
		<td>${ num }</td>
		<td>${ row.get('pname') }</td>
		<td><fmt:formatNumber value="${ row.get('qty') }" pattern="#,###" /></td>
	</tr>
	<c:set var="num" value="${ num + 1 }" />
</c:forEach>
