var showInit, nextIdx, focusIdx = [], timing, nextItem, fixInit, headerHeight;
var mousewheelevt = (/Firefox/i.test(navigator.userAgent))? "DOMMouseScroll" : "mousewheel" //FF doesn't recognize mousewheel as of FF3.x
var startX, startY, moveX, moveY, endX, endY;
var hiddenScrollPoint = 0;

$(function(){

	$(".defaultSelect").each(function() {
		var euiSelect = new ef.ui.Select($(this), {
			html :
				"<div style='width:[width]'>" +
					"<p class='euiSelectMain'>" +
						"<span class='euiSelectTitle'></span>" +
					"</p>" +
					"<ul class='euiSelectList' style='display:none;'>" +
					"</ul>" +
				"</div>",
			visualClass : "eui_" + $(this).attr("class")
		});
		euiSelect.render();
	});

	/* placeholder */
	$('[placeholder]').focus(function() {
	    var input = $(this);
	    if (input.val() == input.attr('placeholder')) {
			input.val('');
			input.removeClass('placeholder');
		}
	})
	.blur(function() {
		var input = $(this);
		if (input.val() == '' || input.val() == input.attr('placeholder')) {
			input.addClass('placeholder');
			input.val(input.attr('placeholder'));
		}
	})
	.blur().parents('form').submit(function() {
		$(this).find('[placeholder]').each(function() {
			var input = $(this);
			if (input.val() == input.attr('placeholder')) {
				input.val('');
			}
		});
	});

	field.init();

	$(".fileBox input[type=file]").on("change", function(){
		$(this).parents(".fileBox").find("input[type=text]").val($(this).val())
	});

	$("[type=text], [type=password], [type=email], [type=number], [type=tel], textarea")
	.each(function(){
		if($(this).val()){
			$(this).addClass("isVal")
			$(this).parents(".txtArea").addClass("isVal")
		} else {
			$(this).removeClass("isVal")
		}
	})
	.on("focusout", function(){
		if($(this).val()){
			$(this).addClass("isVal")
		} else {
			$(this).removeClass("isVal")
		}
	})
	$(".txtArea textarea").each(function(){
		$(this).focusin(function() {
		    $(this).parents(".txtArea").addClass("focus")
		}).focusout(function() {
			$(this).parents(".txtArea").removeClass("focus")
		})
	});

	$(".pwdField input[type=password]").blur(function() {
		if ($(this).val()){
			$(this).addClass('isVal');
		}
	})

	showFocusContent();

	headerHeight = $("#header").height();
	if($(window).width() <= 1200){
		$("#wrapper").addClass("mobileType")
	} else {
		$("#wrapper").removeClass("mobileType")
	}

	$(document).off(mousewheelevt);
	$(document).on(mousewheelevt, function(e){
		var wheelDelta = e.originalEvent.wheelDelta;
		if(!wheelDelta){//firefox
			wheelDelta = -event.detail;
		}
		if(wheelDelta < 0){	//아래로
			scrollBottom()
		} else if(wheelDelta > 0) { //위로
			scrollTop()
		}
	});

	$(document).off("mousedown touchstart")
	$(document).on("mousedown touchstart", function(e) {
 		var ae, angle;
 		ae = e.originalEvent.targetTouches ? e.originalEvent.changedTouches[0] : e.originalEvent;
		startX = ae.pageX;
		startY = ae.pageY;
		$(document).off("mousemove touchmove")
		$(document).on("mousemove touchmove", function(e) {
			ae = e.originalEvent.targetTouches ? e.originalEvent.changedTouches[0] : e.originalEvent;
	 		endX = ae.pageX;
			endY = ae.pageY;
	 		moveX = endX - startX;
			moveY = endY - startY;
			angle = Math.atan2(moveY, moveX) * 180 / Math.PI;
			//e.preventDefault();
	 	});
		$(document).off("mouseup touchend");
		$(document).on("mouseup touchend", function(e) {
			ae = e.originalEvent.targetTouches ? e.originalEvent.changedTouches[0] : e.originalEvent;
			endX = ae.pageX;
			endY = ae.pageY;
			//if((Math.abs(angle) < 15) || (Math.abs(angle) > 168)) {
			if(Math.abs(endY - startY) > 5){
				if($("#wrapper").hasClass("mobileType")){
					if(startY > endY){ //아래로
						scrollBottom()
					} else if (startY < endY) {	//위로
						scrollTop()
					}
				} else {
					if(startY > endY){ //위로
						scrollTop()
					} else if (startY < endY) {	//아래로
						scrollBottom()
					}
				}
			}
		});
	});

	if($(".addCaution").length > 0){
		$(".addCaution").css("padding-left", $(".addCaution h2").width() + 20 + "px");
	}
});

function scrollBottom() {
	$("#wrapper").removeClass("headerFix")
	if($(window).scrollTop() > headerHeight) {
		$("#wrapper").addClass("fix")
	}
}
function scrollTop(){
	if($(window).scrollTop() > headerHeight) {
		$("#wrapper").addClass("headerFix")
	} else {
		$("#wrapper").removeClass("fix headerFix")
	}
}

function showFocusContent(reload){
	var sTop = $(window).scrollTop();
	timing = .8;

	showInit = parseInt(sTop + ($(window).height() * timing));

	$('.focus').each(function(i){
		if(!focusIdx[i]){
			focusIdx[i] = $(this).offset().top;
		}
		if(reload){
			focusIdx[i] = $(this).offset().top;
		}
		if($(this).hasClass("focusTop")){
			showInit = parseInt(sTop + ($(window).height() * .2));
		} else {
			showInit = parseInt(sTop + ($(window).height() * timing));
		}
		if(focusIdx[i] < showInit){
			if(!$(this).hasClass('show')){
				$(this).addClass('show');
				if($(this).find('.textMotion')){
					$(this).find('.textMotion >span').addClass('show');
				}
			}
			nextIdx = i+1;
			nextItem = focusIdx[i] + $(this).height()
			if(nextItem < sTop){
				if($(this).hasClass('show')){
					//$(this).removeClass('show');
					//if($(this).find('.textMotion')){
					//	$(this).find('.textMotion >span').removeClass('show');
					//}
				}
			}
		} else {
			if($(this).hasClass('show')){
				$(this).removeClass('show');
				if($(this).find('.textMotion')){
					$(this).find('.textMotion >span').removeClass('show');
				}
			}
		}
	});
}
function fixOrderAside(){
	if($(".orderSrmyArea").length > 0){
		if($(window).scrollTop() > 309){
			$(".orderSrmyArea").addClass("fix")
		} else {
			$(".orderSrmyArea").removeClass("fix")
		}
	}
}
var quickInit = 0
$(window).on("load", function(){
	showFocusContent();
	fixOrderAside();
	if($(window).scrollTop() > headerHeight) {
		$("#wrapper").addClass("fix")
	}
}).on("resize", function(){
	if($(window).width() <= 1200){
		$("#wrapper").addClass("mobileType")
	} else {
		$("#wrapper").removeClass("mobileType")
	}
}).on("scroll", function(){
	quickInit = $(window).height() - 200
	showFocusContent();
	fixOrderAside();
	if($(window).scrollTop() > headerHeight) {
		$("#wrapper").addClass("fix")
	} else {
		$("#wrapper").removeClass("headerFix fix")
	}
	if($(window).scrollTop() > 0){
		$(".btnTop").addClass("on");
		if($(window).scrollTop() + $(window).height() > ($(document).height() - $("#footer").height())){
			$(".btnTop").addClass("noFix");
			$("#quick").addClass("noFix").css("top", "-" + quickInit +"px");
		} else {
			$(".btnTop").removeClass("noFix");
			$("#quick").removeClass("noFix").removeAttr("style");
		}
	} else {
		$(".btnTop").removeClass("on");
	}
});

$(document).on("click", function(e) {
	$("#location>li>ul").each(function(){
		var $this = $(this)
		if($this.is(":visible")){
			if(!($(e.target).parents("#location").length)){
				$this.parents("li").removeClass("on")
				$this.slideUp(50);
			}
		}
	});
	if($(".searchLayer").is(":visible")){
		if(!($(e.target).parents(".searchBox").length)){
			$(".searchBox").removeClass("on")
			$(".searchBox").find(".searchLayer").fadeOut(100)
		}
	}
});


function showTab(obj, siblings){
	var target = $(obj).attr("href");
	$("." + siblings).hide();
	$(target).show();
	$(obj).parents("li").addClass("on").siblings().removeClass("on");
}

function showMore(obj, self){
	if(!$(self).hasClass("on")){
		$(self).addClass("on")
		$(obj).slideDown(100)
	} else {
		$(self).removeClass("on")
		$(obj).slideUp(50)
	}
}

function goTab(obj){
	$(obj).parents("li").addClass("on").siblings().removeClass("on")
	var scrollTop = $($(obj).attr("href")).offset().top;
	$("html, body").stop().animate({scrollTop:scrollTop}, 200, "easeInOutQuint");
}

function showFold(obj){
	$(obj).parents("li").siblings().add($(obj).parents("li").siblings().find(".icoFold")).removeClass("on");
	$(obj).parents("li").siblings().find(".content").slideUp(100);
	$(obj).parents("li").add($(obj)).toggleClass("on");
	$(obj).parents("li").find(".content").slideToggle(100);

}

function goTop(){
	$("html, body").stop().animate({scrollTop:0}, 200, "easeInOutQuint");
	if((!$("#wrapper").hasClass("mobileType")) && $("#wrapper").find(".aboutArea").length){
		goReset();
	}
}

//리스트 행간 높이 통일
function setListHeight(obj, num){
	var $target = $(obj);
	var lineNum = 0;
	if(!num){
		num = $target.find("li:not(.none)").size();
	}

	$target.find("li .wrap").removeAttr("style");

	$target.find("li:not(.none)").each(function(){
		lineNum = parseInt($(this).index() / num);
		$(this).addClass("line" + lineNum);
	});

	if($target.find("img").length > 0){
		$('#container').imagesLoaded()
		  .always( function( instance ) {
			  for(var idx = 0; idx < lineNum+1; idx++){
					var wHeight = 0;
					$target.find("li.line" + idx + " .wrap").each(function(){
						if(wHeight < $(this).height()){
							wHeight = $(this).height();
						}
					});
					$target.find("li.line" + idx + " .wrap").height(wHeight);
				}
		  });
	}


}
function resetListHeight(obj){
	var $target = $(obj);
	$target.find("li .wrap").removeAttr("style");
}

//오늘 하루 레이어팝업
function setCookie( name, value, expiredays ) {
	var todayDate = new Date();
	todayDate.setDate( todayDate.getDate() + expiredays );
	document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}
function getCookie(name){
	var nameOfCookie = name + "=";
	var x = 0;
	while (x <= document.cookie.length){
		var y = (x+nameOfCookie.length);
		if (document.cookie.substring(x, y) == nameOfCookie) {
			if ((endOfCookie=document.cookie.indexOf( ";", y )) == -1) endOfCookie = document.cookie.length;
			return unescape(document.cookie.substring(y, endOfCookie ));
		}
		x = document.cookie.indexOf(" ", x) + 1;
		if (x == 0) break;
	}
	return "";
}

field = {
	init: function init(){
		if($(".field").is(":visible")){
			$(".field input, .field select, .field textarea").each(function(){
				if($(this).val() != ''){
					$(this).addClass("focus");
				}
			}).on("focusin", function(){
				if($(this).val() == ''){
					$(this).addClass("focus");
				}
			}).on("focusout", function(){
				if($(this).val() == ''){
					$(this).removeClass("focus");
				}
				if($(this).hasClass("focusin")){
					$(this).addClass("focus");
				}
			});
		}
	}
}

function loadingAdd(obj) {
	if(obj == "full"){
		$("body").prepend("<div id=\"loadingArea\" class=\"fullLoad\" style=\"z-index:100010\"><img src=\"/images/common/loading.gif\" alt=\"로딩중...\"></div>");
	} else {
		if($(obj).css("position") != "relative" && $(obj).css("position") != "absolute"){
			$(obj).css("position", "relative");
		}
		$(obj).prepend("<div id=\"loadingArea\"><img src=\"/images/common/loading.gif\" alt=\"로딩중...\"></div>");
	}
}

function loadingRemove(obj) {
	if(obj != undefined) {
		$(obj).find("#loadingArea").remove();
	} else {
		$("#loadingArea").remove();
	}
}

//공통 레이어팝업
function showPopupLayer(popSrc){
	top._showPopupLayer(popSrc);
}

var POPUP_INIT_ID = 111;
var _popupLayerID = POPUP_INIT_ID;
function _showPopupLayer(popSrc) {
	hiddenScrollPoint = $(window).scrollTop();
	var $focus = $(document).find(":focus");
	$focus.addClass("focused");
	if($(".bgLayer").length < 1){
		$("body").append("<div class=\"bgLayer\"></div>");
	} else {

	}
	_popupLayerID += 2;
	var popSrcUrl = popSrc;
	if(popSrcUrl.indexOf("?") > 0){
		popSrcUrl += '&layerId='+ _popupLayerID
	}else {
		popSrcUrl += '?layerId='+ _popupLayerID
	}
	$("body").append(
		'<div class="popLayer" id="popLayer' + (_popupLayerID) + '" style="z-index:' + _popupLayerID + '">'
		+ ' <iframe src="' + popSrcUrl +'" width="100%" height="100%" frameborder="0" allowTransparency="true" scrolling="no" id="iframePopLayer' + (_popupLayerID) + '"></iframe>'
		+ ' <p class="close"><button onclick="hidePopupLayer()"></button></p>'
		+ '</div>'
	);
	$(".bgLayer").fadeIn(100);
	//$("#popLayer" + _popupLayerID).addClass("on");
	$("#iframePopLayer" + (_popupLayerID)).focus();
	//$("html, body").addClass("oh");
}

function hidePopupLayer(layerId, reset){
	top._hidePopupLayer(layerId, reset);
}

function _hidePopupLayer(layerId, reset) {
	if(layerId){
		$("#popLayer" + layerId).empty().remove();
		_popupLayerID = _popupLayerID - 2;
		if(reset){
			_popupLayerID = _popupLayerID + 2;
			POPUP_INIT_ID = _popupLayerID - 2;
		}
	} else {
		if(_popupLayerID != POPUP_INIT_ID + 2){
			$("#popLayer" + _popupLayerID).empty().remove();
			_popupLayerID = _popupLayerID - 2;
		} else {
			$("#popLayer" + _popupLayerID).empty().remove();
			_popupLayerID = _popupLayerID - 2;
			$(".bgLayer").fadeOut(50);
		}
	}

	$(document).find(".focused").focus();
	$(document).find(".focused").removeClass("focused");
	$("html, body").removeClass("oh");
	$("html, body").scrollTop(hiddenScrollPoint)
}

var popMargin = 20;
function setPopup(obj, reset){
	if($("#popWrap").hasClass("fullPage")){
		$("#popWrap").height($(window.parent).height() - 40)
	}
	parent.$("#popLayer" + obj).css("top", "50%");
	parent.$("#popLayer" + obj +", #iframePopLayer" + obj).height($("#popWrap").height());
	parent.$("#popLayer" + obj).css("margin-top", "-" + $("#popWrap").height()/2 + "px");
	var pWidth = $(document).width();
	parent.$("#popLayer" + obj).width(pWidth).css("margin-left", "-" + pWidth/2 + "px");

	var popHeight = $(window.parent).height()-(popMargin*2);
	var titleHeight = $("h1").height();
	var contPadding = parseInt($("#popCont").css("padding-top"))+parseInt($("#popCont").css("padding-bottom"));
	if($("#popWrap").height() >= popHeight){ //팝업높이가 부모창 높이보다 길때
		$("#popCont").css("height", (popHeight-titleHeight) + "px").css("overflow-y", "scroll").css("margin-right", "-12px").css("padding-right", "12px");
		parent.$("#popLayer" + obj +", #iframePopLayer" + obj).height($("#popWrap").height());
		parent.$("#popLayer" + obj).css("top", popMargin + "px").css("margin-top", "0");
	}

	parent.$("#popLayer" + obj).addClass($("#popWrap").attr("class")).addClass("on")
}


function showLayer(target){
	hiddenScrollPoint = $(window).scrollTop();
	if($(".bgLayer").length < 1){
		if($("#popWrap").length = 1){
			$("#popWrap").append("<div class=\"bgLayer\"></div>");
		}
		if($("#wrapper").length = 1){
			$("#wrapper").append("<div class=\"bgLayer\"></div>");
		}
	}
	$("."+target).css("margin-top", "-" + ($("."+target).height() / 2) + "px").css("margin-left", "-" + ($("."+target).width() / 2) + "px")
	$(".bgLayer").fadeIn(100);
	$("."+target).show().addClass("on");
	//$("html, body").addClass("oh");
}
function showBottomLayer(target){
	hiddenScrollPoint = $(window).scrollTop();
	if($(".bgLayer").length < 1){
		if($("#popWrap").length = 1){
			$("#popWrap").append("<div class=\"bgLayer\"></div>");
		}
		if($("#wrapper").length = 1){
			$("#wrapper").append("<div class=\"bgLayer\"></div>");
		}
	}
	$(".bgLayer").fadeIn(100);
	$("."+target).slideDown(100);
	//$("html, body").addClass("oh");
}
function hideLayer(target){
	$(".bgLayer").fadeOut(50);
	if(jQuery.type(target) == 'string'){
		$("."+target).removeClass("on").hide();
	} else {
		$("."+target).parents(".layer").removeClass("on").hide();
	}
	$("html, body").removeClass("oh");
	$("html, body").scrollTop(hiddenScrollPoint)
}

/*
function showPopup(target){
	$("."+target).fadeIn(100);
}
*/

function hidePopup(target){
	$("."+target).fadeOut(50);
}

function showSub(target){
	var $target = $(target).parents("li")
	$target.siblings().removeClass("on")
	$target.siblings().find("ul").slideUp(50);
	if(!$target.hasClass("on")){
		$target.addClass("on");
		$target.find("ul").css("margin-left", "-" + $target.find("ul").width()/2 +"px").slideDown(100);
	} else {
		$target.removeClass("on");
		$target.find("ul").slideUp(50);
	}
}

function setFileField(obj){
	var fileFullPath = $(obj).val()
	var filename = fileFullPath.replace(/^.*[\\\/]/, '');
	$(obj).parents(".fileField").find("input[type=text]").val(filename)
}

function showIframePopup(popSrcUrl, popName){
	$("body").append(
		' <iframe src="' + popSrcUrl + '" name="' + popName + '" width="100%" height="100%" frameborder="0" allowTransparency="true" scrolling="no" class="popIfrmLayer"></iframe>'
	);
}

function hideIframePopup(){
	$(".popIfrmLayer").remove()
}