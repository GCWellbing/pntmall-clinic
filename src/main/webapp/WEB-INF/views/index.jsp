<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/include/head" />
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div id="wrapper">
<c:import url="/include/header" />
	<div id="container" class="main">
		<div id="location">
			<h1>&nbsp;</h1>
		</div>
		<c:if test="${ not empty noticeList }">
			<div class="noticeArea"><div class="wrap">
				<h2>공지사항</h2>
				<div class="bbsList swiper-container">
					<ul class="swiper-wrapper">
						<c:forEach items="${ noticeList }" var="row">
							<li class="swiper-slide"><a href="/notice/edit?noticeNo=${ row.noticeNo }">
								${ row.title }
								<span class="date">(<fmt:formatDate value="${row.cdate}" pattern="${ DateFormat }"/>)</span>
							</a></li>
						</c:forEach>
					</ul>
				</div>
				<a href="/notice/list" class="more">더보기 +</a>
			</div></div><!-- //noticeArea -->
		</c:if>

		<div class="mainWrap setList">
			<div class="sec">
				<c:set var="now" value="<%=new java.util.Date()%>" />
				<h2>
					<fmt:formatDate value="${now}" pattern="E" var="today" />
					<fmt:formatDate value="${now}" pattern="yyyy.MM.dd" /> ${today}요일
				</h2>

				<div class="listWrap">
					<table class="list">
						<colgroup>
							<col width="">
							<col width="">
							<col width="">
							<col width="">
							<col width="">
						</colgroup>
						<thead>
							<tr>
								<th>미확인 예약</th>
								<th>미확인 문의</th>
								<th>신규 회원</th>
								<th>신규 매출</th>
								<th>정산 금액</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>${ reserCount }</td>
								<td>${ qnaCount }</td>
								<td>${ memberCount }</td>
								<td><fmt:formatNumber value="${ orderSummary1 }" pattern="#,###" /></td>
								<td><fmt:formatNumber value="${ orderSummary2 }" pattern="#,###" /></td>
							</tr>
						</tbody>
					</table>
					<p class="text">* 정산 금액은 이용수수료율 15% 기준으로 산정된 금액이며, 실제 정산 금액과 상이할 수 있습니다.</p>
				</div>
			</div><!-- // -->

			<div class="sec">
				<h2>고객관리</h2>
				<div class="listWrap">
					<table class="list">
						<colgroup>
							<col width="">
							<col width="">
							<col width="">
						</colgroup>
						<thead>
							<tr>
								<th colspan="2">픽업</th>
								<th>닥터팩</th>
							</tr>
							<tr>
								<th>방문 예정</th>
								<th>픽업 완료</th>
								<th>상담 미완료</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><fmt:formatNumber value="${ pickupCount1 }" pattern="#,###" /></td>
								<td><fmt:formatNumber value="${ pickupCount2 }" pattern="#,###" /></td>
								<td><fmt:formatNumber value="${ doctorPackCount }" pattern="#,###" /></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div><!-- //고객관리 -->
		</div><!-- //mainWrap -->

		<div class="mainWrap setList">
			<div class="sec">
				<h2>미확인 예약 (총 ${ reserCount }건)</h2>
				<p class="more"><a href="/reservation/list">더보기</a></p>
				<div class="listWrap">
					<table class="list">
						<colgroup>
							<col width="110">
							<col width="">
							<col width="100">
							<col width="150">
						</colgroup>
						<thead>
							<tr>
								<th>예약목적</th>
								<th>내용</th>
								<th>예약자</th>
								<th>예약일시</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ reserList }" var="row">
								<tr>
									<td>${ row.cateName }</td>
									<td><a href="/reservation/edit?resNo=${ row.resNo }">${ row.content }</a></td>
									<td>${ row.cuserName }</td>
									<td>${ row.rdate } ${ row.rtime }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div><!-- //미확인예약 -->
			
			<c:if test="${ system ne 'real' }">
			<div class="sec">
				<h2>미답변 문의 (총 ${ qnaCount }건)</h2>
				<p class="more"><a href="">더보기</a></p>
				<div class="listWrap">
					<table class="list">
						<colgroup>
							<col width="120">
							<col width="">
							<col width="120">
						</colgroup>
						<thead>
							<tr>
								<th>유형</th>
								<th>제목</th>
								<th>등록일</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ qnaList }" var="row">
								<tr>
									<td>${ row.cateName }</td>
									<td><a href="/cs/qna/edit?qnaNo=${ row.qnaNo }">${ row.title }</a></td>
									<td><fmt:formatDate value="${row.qdate}" pattern="${ DateFormat }"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div> <!-- //미확인답변 -->
			</c:if>
		</div>

		<div class="mainWrap">
			<div class="sec">
				<h2>매출 <p class="text">${ saleRankText }</p></h2>
				<div class="listWrap">
					<div class="sel">
						<select name="salePeriod" style="width:80px" onchange="getSaleInfo()">
							<option value="1">7일</option>
							<option value="2">1개월</option>
							<option value="3">3개월</option>
							<option value="4">1년</option>
						</select>
					</div>
					<ul class="status" id="saleRank">
					</ul>
				</div>
			</div><!-- //매출/정산 -->

			<div class="sec">
				<h2>정산</h2>
				<p class="more"><a href="/clinic/calcu/edit">정산정보 변경</a></p>
				<div class="listWrap">
					<c:if test='${ !empty adjustInfo }'>
						<ul class="status">
							<li>정산상태
								<em class="colorA">
									<c:choose>
										<c:when test='${ adjustInfo.status eq 10 }'>
											정산예정
										</c:when>
										<c:when test='${ adjustInfo.status eq 20 }'>
											정산확인요청
										</c:when>
										<c:when test='${ adjustInfo.status eq 30 }'>
											세금계산서발행요청
										</c:when>
										<c:when test='${ adjustInfo.status eq 40 }'>
											세금계산서수정요청
										</c:when>
										<c:when test='${ adjustInfo.status eq 50 }'>
											세금계산서확인
										</c:when>
										<c:when test='${ adjustInfo.status eq 60 }'>
											지급준비중
										</c:when>
										<c:when test='${ adjustInfo.status eq 70 }'>
											지급완료
										</c:when>
										<c:when test='${ adjustInfo.status eq 80 }'>
											미정산
										</c:when>
									</c:choose>
								</em>
							</li>
							<li>정산상태 마감일 <em><c:out value='${ adjustInfo.deadline }' /></em></li>
							<li>지급일 <em><c:out value='${ adjustInfo.paymentDate }' /></em></li>
						</ul>
					</c:if>

					<h4>분기별 정산 내역</h4>
					<table class="list">
						<colgroup>
							<col width="80">
							<col width="">
							<col width="80">
							<col width="">
							<col width="100">
						</colgroup>
						<thead>
							<tr>
								<th>분기</th>
								<th>정산금액(원)</th>
								<th>주문건수</th>
								<th>정산상태</th>
								<th>지급일</th>
							</tr>
						</thead>
						<tbody>
							<c:if test='${ empty adjustList }'>
								<tr>
									<td>-</td>
									<td>-</td>
									<td>-</td>
									<td>-</td>
									<td>-</td>
								</tr>
							</c:if>
							<c:forEach items='${ adjustList }' var='row'>
								<tr>
									<td><a href="/adjust/detail?year=${ row.year }&quarter=${ row.quarter }"><c:out value='${ row.quarter }' />분기</a></td>
									<td><fmt:formatNumber value="${ row.adjustAmt }" pattern="#,###" /></td>
									<td><fmt:formatNumber value="${ row.orderCnt }" pattern="#,###" /></td>
									<td>
										<c:choose>
											<c:when test='${ row.status eq 10 }'>
												정산예정
											</c:when>
											<c:when test='${ row.status eq 20 }'>
												정산확인요청
											</c:when>
											<c:when test='${ row.status eq 30 }'>
												세금계산서발행요청
											</c:when>
											<c:when test='${ row.status eq 40 }'>
												세금계산서수정요청
											</c:when>
											<c:when test='${ row.status eq 50 }'>
												세금계산서확인
											</c:when>
											<c:when test='${ row.status eq 60 }'>
												지급준비중
											</c:when>
											<c:when test='${ row.status eq 70 }'>
												지급완료
											</c:when>
											<c:when test='${ row.status eq 80 }'>
												미정산
											</c:when>
										</c:choose>
									</td>
									<td><c:out value='${ row.paymentDate }' /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div><!-- //정산 -->
		</div><!-- //mainWrap -->


		<div class="mainWrap">
			<h2>Dr.PNT 관리
				<c:if test="${ memberRankRate < 80 }">
					<p class="text">회원수 보유 상위 ${memberRankRate eq 0 ? 1 : memberRankRate  }%입니다.</p>
				</c:if>
			</h2>
			<div class="secWrap setList">
				<div class="sec">
					<div class="listWrap">
						<h3>내 병의원 인기 제품 TOP5</h3>
						<div class="sel">
							<select name="salePeriod2" style="width:80px" onchange="getSaleInfo2()">
								<option value="1">7일</option>
								<option value="2">1개월</option>
								<option value="3">3개월</option>
								<option value="4">1년</option>
							</select>
						</div>
						<table class="list">
							<colgroup>
								<col width="60">
								<col width="">
								<col width="80">
							</colgroup>
							<thead>
								<tr>
									<th>순위</th>
									<th>제품명</th>
									<th>수량</th>
								</tr>
							</thead>
							<tbody id="saleRank2">
							</tbody>
						</table>
					</div>
				</div><!-- //내병의원 인기상품 -->

				<div class="sec">
					<div class="listWrap">
						<h3>마이클리닉 지정 회원</h3>
						<div class="sel">
							* 최근 1년 기준
						</div>
						<div class="chartArea">
							<canvas id="memberChart" style="width:100%;height:250px"></canvas>
						</div>
						<ul class="status">
							<li>마이클리닉 회원
								<em>총<strong class="num">${memberTotalCount}</strong>명</em>
							</li>
						</ul>
					</div>
				</div><!-- //마이클리닉지정회원 -->
			</div><!-- //secWrap -->
		</div><!-- //mainWrap(dr.pnt관리) -->

	</div><!-- //container -->
	<c:import url="/include/footer" />
</div><!-- //wrapper -->
<script>
var swiperNoti = new Swiper ('.noticeArea .bbsList', {
	slidesPerView: 1,
	direction: "vertical",
	loop: true,
	autoplay: {
		delay: 5000,
		speed: 1000
	}
});
$(window).on("load", function(){
	$(".setList").each(function(){
		var $target = $(this)
		var lineNum = 0;
		$target.find(".sec").each(function(){
			lineNum = parseInt($(this).index() / 2);
			$(this).addClass("line" + lineNum);
		});
	
		for(var idx = 0; idx < lineNum+1; idx++){
			var wHeight = 0;
			$target.find(".line" + idx + " .listWrap").each(function(){
				if(wHeight < $(this).height()){
					wHeight = $(this).height();
				}
			});
			$target.find(".line" + idx + " .listWrap").height(wHeight);
		}
	})
})
</script>
<script>
const ctx = document.getElementById('memberChart');

const footer = (tooltipItems) => {
	  let sum = 0;

	  tooltipItems.forEach(function(tooltipItem) {
	    sum += tooltipItem.parsed.y;
	  });
	  return '총 신규회원수: ' + sum;
	};

var xValues = [];
var yValuesM = [];
var yValuesF = [];
var yValuesN = [];

<c:forEach items="${ memberChartList }" var="row">
	xValues.push("${row.get('YM')}");
	yValuesM.push("${row.get('M_CNT')}");
	yValuesF.push("${row.get('W_CNT')}");
	yValuesN.push("${row.get('NOSEX_CNT')}");
</c:forEach>

new Chart("memberChart", {
	type: "bar",
	data: {
		labels: xValues,
		datasets: [{
			label: '여성 신규회원수',
			data: yValuesF,
			borderColor: "#f9b9b8",
			backgroundColor: "#f9b9b8",
			fill: false,
			yAxisID: 'y'
		},{
			label: '남성 신규회원수',
			data: yValuesM,
			borderColor: "#99cbf9",
			backgroundColor: "#99cbf9",
			fill: false,
			yAxisID: 'y'
		},{
			label: '미확인 신규회원수',
			data: yValuesN,
			borderColor: "#ddd",
			backgroundColor: "#ddd",
			fill: false,
			yAxisID: 'y'
		}]
	},
	options: {
	   interaction: {
		      intersect: false,
		      mode: 'index',
		    },
		scales: {
			x: {
		    	stacked: true,
		    },
		    y: {
		        stacked: true,
		        ticks: {
		            // forces step size to be 50 units
		            stepSize: 1
		          }
		    }
		},
		plugins: {
			legend: {
				position: 'bottom'
			},
		      tooltip: {
		          callbacks: {
		            footer: footer,
		          }
		        }

		}
	}
});

function getSaleInfo() {
// 	console.log("salePeriod", $("select[name=salePeriod]").val());
	$("#saleRank").load("/sale?period=" + $("select[name=salePeriod]").val());
}

function getSaleInfo2() {
// 	console.log("salePeriod", $("select[name=salePeriod]").val());
	$("#saleRank2").load("/sale2?period=" + $("select[name=salePeriod2]").val());
}


$(function() {
	getSaleInfo();
	getSaleInfo2();
});
</script>
</body>
</html>
