<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	$(function(){
		$("#header .nav li").each(function(){
			if($("ul", this).length != 0){
				$(this).addClass("tree");

				$(">p", this).click(function(){
					$(this).parent().find(">ul").toggle();
					setHeight();
				});
			}
			$(this).find("ul li:last-child").addClass("last");
		});

		$("#header .nav li ul li").each(function() {
			if ($(this).hasClass("on")) {
				$(this).parent().show();
				return;
			}
		});

		$("#snb .nav>li").each(function(){
			if($(this).find('ul').length > 0){
				$(this).children('p').addClass('arrow');
			}
			if($(this).hasClass('on')){
				if($(this).find('ul').length > 0){
					$(this).find('ul').show();
					$(this).children('p').find('a').addClass('open');
				}
			}
		});
		$("#snb .nav>li p.arrow a").click(function(){
			$(this).parent().parent().find('ul').toggle();
			$(this).toggleClass('open');
		});
	});

</script>
<div id="header">
	<div class="logo"><div class="tableSet"><a href="/"><img src="/static/images/logo.png" alt="Dr PNT"></a></div></div>
	<div class="user">
		<c:if test="${ not empty cs.clinic.recommendSeq }">
			<p class="bestIcon"><img src="/static/images/icon_best.png" alt="추천병의원"></p>
		</c:if>
		<div class="tableSet">
			<strong class="id">${ cs.clinic.clinicName }</strong>
			<ul class="user_util">
				<li><a href="/clinic/edit">병의원 정보 수정</a></li>
				<li><a href="/logout">LOGOUT</a></li>
			</ul>
		</div>
	</div>
</div><!-- //header -->
<div id="snb">
	<p class="title">${ rootInfo.name }</p>
	<ul class="nav">
		<li <c:if test="${Depth_1 eq 1}" >class="on"</c:if>>
			<p class="arrow"><a href="#" class="open">병의원정보 관리</a></p>
			<ul>
				<li <c:if test="${Depth_1 eq 1 && Depth_2 eq 1}" >class="on"</c:if>>
					<p><a href="/clinic/edit">병의원정보</a></p>
				</li>
				<li <c:if test="${Depth_1 eq 1 && Depth_2 eq 2}" >class="on"</c:if>>
					<p><a href="/clinic/calcu/edit">정산정보</a></p>
				</li>
			</ul>
		</li>
		<c:if test="${ not(cs.clinic.approve eq '006001' or cs.clinic.approve eq '006003') }">
			<li <c:if test="${Depth_1 eq 2}" >class="on"</c:if>>
				<p class="arrow"><a href="#" class="open">주문 관리</a></p>
				<ul>
					<li <c:if test="${Depth_1 eq 2 && Depth_2 eq 3}" >class="on"</c:if>>
						<p><a href="/order/order/list">주문 목록</a></p>
					</li>
					<li <c:if test="${Depth_1 eq 2 && Depth_2 eq 1}" >class="on"</c:if>>
						<p><a href="/doctorPack/list">닥터팩 신청 관리</a></p>
					</li>
					<li <c:if test="${Depth_1 eq 2 && Depth_2 eq 2}" >class="on"</c:if>>
						<p><a href="/order/pickup/list">병의원 픽업 관리</a></p>
					</li>
				</ul>
			</li>
			<c:if test="${ profile ne 'real' }">
				<li <c:if test="${Depth_1 eq 3}" >class="on"</c:if>>
					<p><a href="/cs/qna/list">1:1문의관리</a></p>
				</li>
			</c:if>
			<li <c:if test="${Depth_1 eq 4}" >class="on"</c:if>>
				<p><a href="/reservation/list">병의원 예약관리</a></p>
			</li>
			<li <c:if test="${Depth_1 eq 5}" >class="on"</c:if>>
				<p><a href="/product/list">제품관리</a></p>
			</li>
			<li <c:if test="${Depth_1 eq 7}" >class="on"</c:if>>
				<p class="arrow"><a href="#" class="open">정산관리</a></p>
				<ul>
					<li <c:if test="${Depth_1 eq 7 && Depth_2 eq 1}" >class="on"</c:if>>
						<p><a href="/adjust/confirm">정산금액확인</a></p>
					</li>
					<li <c:if test="${Depth_1 eq 7 && Depth_2 eq 2}" >class="on"</c:if>>
						<p><a href="/adjust/list">정산처리 내역조회</a></p>
					</li>
				</ul>
			</li>
			<li <c:if test="${Depth_1 eq 6}" >class="on"</c:if>>
				<p class="arrow"><a href="#" class="open">Dr. 자유게시판</a></p>
				<ul>
					<c:if test="${ cs.clinic.approve ne '006007' }">
						<li <c:if test="${Depth_1 eq 6 && Depth_2 eq 1}" >class="on"</c:if>>
							<p><a href="/bbs/list">자유게시판</a></p>
						</li>
					</c:if>
					<li <c:if test="${Depth_1 eq 6 && Depth_2 eq 2}" >class="on"</c:if>>
						<p><a href="/job/list">메디잡 채용관</a></p>
					</li>
				</ul>
			</li>
		</c:if>
	</ul>
</div>