package com.pntmall.clinic.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pntmall.clinic.domain.Code;
import com.pntmall.clinic.domain.Member;
import com.pntmall.clinic.domain.Reservation;
import com.pntmall.clinic.domain.ReservationProduct;
import com.pntmall.clinic.domain.ReservationSearch;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.ClinicService;
import com.pntmall.clinic.service.CodeService;
import com.pntmall.clinic.service.ReservationService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.directSend.DirectsendKakaoAllimApi;
import com.pntmall.common.directSend.SendKatalk;
import com.pntmall.common.type.Param;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/doctorPack")
public class DoctorPackController {
	public static final Logger logger = LoggerFactory.getLogger(DoctorPackController.class);

    @Value("${directSend.front.returnUrl}")
	String directSendFrontReturnUrl;

    @Value("${directSend.clinic.returnUrl}")
	String directSendClinicReturnUrl;

    @Autowired
	private ReservationService reservationService;

    @Autowired
	private CodeService codeService;

    @Autowired
	private ClinicService clinicService;

    @Autowired
	DirectsendKakaoAllimApi directsendKakaoAllimApi;

	@GetMapping("/list")
	public ModelAndView list(@ModelAttribute ReservationSearch reservationSearch, HttpServletRequest request) {
		Utils.savePath("doctorPack");

		ClinicSession sess = ClinicSession.getInstance();
		reservationSearch.setClinicMemNo(sess.getMemNo());

		logger.debug("list"+reservationSearch+"---"+request.getRequestURI());

		List<Code> cateList = codeService.getList2("016");
		List<Code> statusList = codeService.getList2("025");

		reservationSearch.setDoctorPack("Y");
		List<Reservation> list = reservationService.getList(reservationSearch);
		Integer count = reservationService.getCount(reservationSearch);
		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), count, reservationSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("cateList", cateList);
		mav.addObject("statusList", statusList);
		mav.addObject("list", list);
		mav.addObject("count", count);
		mav.addObject("paging", paging);

		return mav;
	}

	@GetMapping("/edit")
	public ModelAndView edit(@ModelAttribute ReservationSearch reservationSearch, HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();
		reservationSearch.setClinicMemNo(sess.getMemNo());

		Reservation reservation = reservationService.getInfo(reservationSearch);
		List<ReservationProduct> reservationProductList = reservationService.getProductList(reservationSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("reservation", reservation);
		mav.addObject("reservationProductList", reservationProductList);
		mav.addObject("retrivePath", Utils.retrivePath("doctorPack"));
		mav.addObject("directSendFrontReturnUrl", directSendFrontReturnUrl);

		return mav;
	}

	@PostMapping("/modify")
	@ResponseBody
	public ResultMessage modify(@ModelAttribute Reservation reservation, HttpServletRequest request) {

		ClinicSession sess = ClinicSession.getInstance();

		try {

			reservation.setClinicMemNo(sess.getMemNo());
			reservation.setUuser(sess.getMemNo());
			reservationService.modify(reservation);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "수정되었습니다.");
	}


	@PostMapping("/memo")
	@ResponseBody
	public ResultMessage memo(@ModelAttribute Reservation reservation, HttpServletRequest request) {

		ClinicSession sess = ClinicSession.getInstance();

		try {
			reservation.setClinicMemNo(sess.getMemNo());
			reservation.setUuser(sess.getMemNo());
			reservationService.modifyMemo(reservation);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "수정되었습니다.");
	}

	@PostMapping("/complete")
	@ResponseBody
	public ResultMessage complete(@ModelAttribute Reservation reservation, HttpServletRequest request) {

		ClinicSession sess = ClinicSession.getInstance();
		Param param = new Param(request.getParameterMap());
		try {
			reservation.setClinicMemNo(sess.getMemNo());
			reservation.setUuser(sess.getMemNo());
			reservationService.complete(reservation, param, request);

			try {
				//SMS발송
				//회원정보
				Member infoMem = clinicService.infoMem(reservation.getCuser());
	
	 	    	SimpleDateFormat simpl = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	 	    	long millis = System.currentTimeMillis();
	 	    	String s = simpl.format(millis);
	
				//일반 회원 SMS발송
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(247);
				sendKatalk.setName(infoMem.getName());
				sendKatalk.setMobile(infoMem.getMtel1()+infoMem.getMtel2());
				sendKatalk.setNote1(s);
				sendKatalk.setNote2(directSendFrontReturnUrl+"/order/cart?gubun=4");
				sendKatalk.setNote3(directSendFrontReturnUrl);

				directsendKakaoAllimApi.send(sendKatalk);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "처리되었습니다.");
	}

}
