package com.pntmall.clinic.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import com.pntmall.clinic.domain.Clinic;
import com.pntmall.clinic.domain.ClinicAdjust;
import com.pntmall.clinic.domain.ClinicAdjustSearch;
import com.pntmall.clinic.domain.ClinicSearch;
import com.pntmall.clinic.domain.MemberLog;
import com.pntmall.clinic.domain.Notice;
import com.pntmall.clinic.domain.NoticeSearch;
import com.pntmall.clinic.domain.Qna;
import com.pntmall.clinic.domain.QnaSearch;
import com.pntmall.clinic.domain.Reservation;
import com.pntmall.clinic.domain.ReservationSearch;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.ClinicAdjustService;
import com.pntmall.clinic.service.ClinicNoticeService;
import com.pntmall.clinic.service.ClinicService;
import com.pntmall.clinic.service.OrderService;
import com.pntmall.clinic.service.PickupService;
import com.pntmall.clinic.service.QnaService;
import com.pntmall.clinic.service.ReservationService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.type.Param;
import com.pntmall.common.utils.HttpRequestHelper;
import com.pntmall.common.utils.SecurityUtils;
import com.pntmall.common.utils.Utils;

@Controller
public class IndexController {
    public static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
	private ClinicService clinicService;

    @Autowired
	private ClinicNoticeService noticeService;

    @Autowired
	private QnaService qnaService;

    @Autowired
	private ReservationService reservationService;

    @Autowired
	private PickupService pickupService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ClinicAdjustService clinicAdjustService;

    @Autowired
    private OrderService orderService;

//    @RequestMapping("/{variable:(?!static).*}/**")
    @RequestMapping("/**/{variable:[a-zA-Z0-9\\-_]*}")
    public String resolve(HttpServletRequest request) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String uri = urlPathHelper.getRequestUri(request);
		uri = uri.substring(1);
		logger.debug("--- **/* :" + uri);
		return uri;
    }

    @RequestMapping("/index")
    public ModelAndView index() {
		ClinicSession sess = ClinicSession.getInstance();
    	ModelAndView mav = new ModelAndView();

		//????????????
    	NoticeSearch noticeSearch = new NoticeSearch();
    	noticeSearch.setFixYn("Y");
		List<Notice> noticeList = noticeService.getList(noticeSearch);

		//????????????, ??????????????? ????????????(??????), ??????????????? ????????????, ???????????????%
		ReservationSearch reservationSearch = new ReservationSearch();
		reservationSearch.setClinicMemNo(sess.getMemNo());
		Integer memberCount = reservationService.getMemberCount(reservationSearch);
		List<Param> memberChartList = reservationService.getMemberChart(reservationSearch);
		Integer memberTotalCount = reservationService.getMemberTotalCount(reservationSearch);
		Integer memberRankRate = reservationService.getMemberRankRate(reservationSearch);

		//???????????????
		reservationSearch.setClinicMemNo(sess.getMemNo());
		reservationSearch.setStatus("025001");
		reservationSearch.setPageSize(5);
		List<Reservation> reserList = reservationService.getList(reservationSearch);
		Integer reserCount = reservationService.getCount(reservationSearch);

		//???????????????
		QnaSearch qnaSearch = new QnaSearch();
		qnaSearch.setClinicMemNo(sess.getMemNo());
		qnaSearch.setStatus("Q");
		qnaSearch.setPageSize(5);
		List<Qna> qnaList = qnaService.getList(qnaSearch);
		Integer qnaCount = qnaService.getCount(qnaSearch);

		// ????????????
		Param orderSummary = pickupService.getTodaySummary(sess.getMemNo());

		// ??????
		Integer thisYear = Integer.parseInt(Utils.getTimeStampString("yyyy"));
		ClinicAdjustSearch clinicAdjustSearch = new ClinicAdjustSearch();
		clinicAdjustSearch.setYear1(thisYear);
		clinicAdjustSearch.setQuarter1(1);
		clinicAdjustSearch.setYear2(thisYear);
		clinicAdjustSearch.setQuarter2(4);
		clinicAdjustSearch.setMemNo(sess.getMemNo());

		List<ClinicAdjust> adjustList = clinicAdjustService.getList(clinicAdjustSearch);
		ClinicAdjust adjustInfo = new ClinicAdjust();
		if(adjustList.size() > 0) {
			adjustInfo = adjustList.get(adjustList.size() - 1);
		}

		// ??????
		Param saleRank = orderService.getSaleRank(sess.getMemNo());
		if(saleRank != null && saleRank.getInt("cnt") > 0) {
			int percent = 100 * saleRank.getInt("rank") / saleRank.getInt("cnt");
			if(percent <= 80) {
				String s = "?????? ?????? " + percent + "% ?????????.";
				mav.addObject("saleRankText", s);
			}
		}
		
    	mav.addObject("system", System.getProperty("spring.profiles.active"));
		mav.addObject("noticeList", noticeList);
		mav.addObject("memberCount", memberCount);
		mav.addObject("memberChartList", memberChartList);
		mav.addObject("memberTotalCount", memberTotalCount);
		mav.addObject("memberCount", memberCount);
		mav.addObject("memberRankRate", memberRankRate);
		mav.addObject("reserList", reserList);
		mav.addObject("reserCount", reserCount);
		mav.addObject("qnaList", qnaList);
		mav.addObject("qnaCount", qnaCount);
		mav.addObject("orderSummary1", orderSummary == null ? 0 : orderSummary.getInt("sale_price"));
		mav.addObject("orderSummary2", orderSummary == null ? 0 : (orderSummary.getInt("sale_price") - orderSummary.getInt("supply_price") -(0.15 * orderSummary.getInt("sale_price"))));
		mav.addObject("pickupCount1", pickupService.getCountTodayPickup420(sess.getMemNo()));
		mav.addObject("pickupCount2", pickupService.getCountTodayPickup430(sess.getMemNo()));
		mav.addObject("doctorPackCount", reservationService.getDoctorPackCount(sess.getMemNo()));
		mav.addObject("adjustList", adjustList);
		mav.addObject("adjustInfo", adjustInfo);

    	return mav;
    }

	@RequestMapping("/ajaxMsg")
    @ResponseBody
    public ResultMessage ajaxMsg(String type) {
    	if("logout".equals(type)) {
    		return new ResultMessage(false, "?????? ???????????????????????????.\n?????? ??????????????????.");
    	} else if("noauth".equals(type)) {
        	return new ResultMessage(false, "????????? ???????????????.");
    	} else {
    		return new ResultMessage(false, "?????? ?????? ???????????????.\n?????? ??????????????????.");
    	}
    }


    @PostMapping("/loginProc")
    @ResponseBody
    public ResultMessage loginProc(@ModelAttribute ClinicSearch clinicSearch, HttpServletRequest request) throws Exception {
//		String clinicId = clinicSearch.getClinicId();
    	Param param = new Param();
    	param.set("url", "");

		String passwd = clinicSearch.getPasswd();

		Clinic info = clinicService.getInfo(clinicSearch);

		if(info == null || "".equals(info.getMemId()) || "006004".equals(info.getApprove()) ) {
			return new ResultMessage(false, "????????? ????????? ???????????? ????????????.",param);
		} else if("006001".equals(info.getApprove())) {
			return new ResultMessage(false, "?????? ??????????????????.",param);
		} else if("Y".equals(info.getOldMember()) ) {
			param.set("url", "/clinic/find/pwdCheck?memId="+clinicSearch.getMemId());
			return new ResultMessage(false, "??????????????? ??????????????? ?????? ???????????????.",param);
		} else if(info.getLoginFailCnt() >= 5) {
			param.set("url", "/clinic/find/pwd");
			return new ResultMessage(false, "??????????????? 5????????? ?????? ?????? ???????????????. ??????????????? ???????????????.",param);
		} else {
			String enc = securityUtils.encodeSHA512(passwd);
			logger.debug(String.format("-------- %s", enc));

			if(!enc.equals(info.getPasswd())) {
				MemberLog memberLog = new MemberLog();
				memberLog.setMemNo(info.getMemNo());
				memberLog.setIp(HttpRequestHelper.getRequestIp());
				memberLog.setSuccessYn("N");
				clinicService.createAccessLog(memberLog);

				info.setLoginFailCnt(info.getLoginFailCnt() + 1);
				clinicService.modifyFailCnt(info);

				return new ResultMessage(false, "????????? ????????? ???????????? ????????????.",param);
			} else {
				String msg = "";
				ClinicSession clinicSession = ClinicSession.getInstance();
				clinicSession.login(info);

				if(info.getBusinessNo2() == null || "".equals(info.getBusinessNo2()) ) { //????????? ????????? ????????????
					msg = "2022????????? ??????????????????????????? ?????????????????? ????????? ???????????????.\r\n"
						+ "??????????????? ????????? ????????? ?????? ????????? ?????? ??? ??????????????? ??????????????? ???????????? ????????????.";
				} else if("006003".equals(info.getApprove()) ) { //?????? ?????? ??????
					msg = "??????????????? ?????????????????????. ?????? ???????????? ????????????.\r\n"
						+ "?????? : "+info.getReason();
				} else if("006006".equals(info.getApprove()) ) { //???????????? ?????? ??????
					msg = "???????????? ??????????????? ?????????????????????. ?????? ???????????? ????????????.\r\n"
						+ "?????? : "+info.getReason();
				}

				MemberLog memberLog = new MemberLog();
				memberLog.setMemNo(info.getMemNo());
				memberLog.setIp(HttpRequestHelper.getRequestIp());
				memberLog.setSuccessYn("Y");
				Device device = DeviceUtils.getCurrentDevice(request);
				if(device.isMobile()) memberLog.setDevice("M");
				else memberLog.setDevice("P");

				clinicService.createAccessLog(memberLog);

				info.setLoginFailCnt(0);
				clinicService.modifyFailCnt(info);

				return new ResultMessage(true, msg, param);
			}
		}
    }

    @RequestMapping("/logout")
    public String logout() {
    	ClinicSession clinicSession = ClinicSession.getInstance();
    	clinicSession.logout();

    	return "redirect:/login";
    }

    @GetMapping("/sale")
    public ModelAndView sale(HttpServletRequest request) {
    	ModelAndView mav = new ModelAndView();
    	ClinicSession sess = ClinicSession.getInstance();

    	Param param = new Param(request.getParameterMap());
    	param.set("clinicMemNo", sess.getMemNo());
    	
    	Param p = orderService.getSaleRank2(param);
    	
    	mav.addObject("v1", Utils.formatMoney(p.get("amt", "0")));
    	mav.addObject("v2", p.getInt("mem_cnt") == 0 ? 0 : Utils.formatMoney(p.getInt("amt") / p.getInt("mem_cnt")));
    	
    	return mav;
    }

    @GetMapping("/sale2")
    public ModelAndView sale2(HttpServletRequest request) {
    	ModelAndView mav = new ModelAndView();
    	ClinicSession sess = ClinicSession.getInstance();

    	Param param = new Param(request.getParameterMap());
    	param.set("clinicMemNo", sess.getMemNo());
    	
    	List<Param> list = orderService.getSaleRank3(param);
    	
    	mav.addObject("list", list);
    	
    	return mav;
    }
    
}
