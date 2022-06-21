package com.pntmall.clinic.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.pntmall.clinic.domain.Clinic;
import com.pntmall.clinic.domain.ClinicSearch;
import com.pntmall.clinic.domain.Code;
import com.pntmall.clinic.domain.Member;
import com.pntmall.clinic.domain.Reservation;
import com.pntmall.clinic.domain.ReservationSearch;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.ClinicService;
import com.pntmall.clinic.service.CodeService;
import com.pntmall.clinic.service.ReservationService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.directSend.DirectsendKakaoAllimApi;
import com.pntmall.common.directSend.SendKatalk;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	public static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

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
		ClinicSession sess = ClinicSession.getInstance();
		reservationSearch.setClinicMemNo(sess.getMemNo());

		logger.debug("list"+reservationSearch+"---"+request.getRequestURI());

		List<Code> cateList = codeService.getList2("016");
		List<Code> statusList = codeService.getList2("025");

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

		ModelAndView mav = new ModelAndView();
		mav.addObject("reservation", reservation);


		return mav;
	}

	@PostMapping("/modify")
	@ResponseBody
	public ResultMessage modify(@ModelAttribute Reservation reservation, HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();

		try {

			reservation.setUuser(sess.getMemNo());
			reservation.setClinicMemNo(sess.getMemNo());
			reservationService.modify(reservation);

			//SMS발송
			//회원정보
			Member infoMem = clinicService.infoMem(reservation.getCuser());
			ClinicSearch clinicSearch = new ClinicSearch();
			clinicSearch.setMemNo(sess.getMemNo());
			Clinic infoClinic = clinicService.getInfo(clinicSearch);

			if("025004".equals(reservation.getStatus())) { //예약 확정
				//일반 회원 SMS발송
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(184);
				sendKatalk.setName(infoMem.getName());
				sendKatalk.setMobile(infoMem.getMtel1()+infoMem.getMtel2());
				sendKatalk.setNote1(Utils.unSafeHTML(sess.getClinic().getClinicName()));
				sendKatalk.setNote2(infoClinic.getAddr1()+" "+infoClinic.getAddr2());
				sendKatalk.setNote3(reservation.getRdate()+" "+reservation.getRtime());
				sendKatalk.setNote4(reservation.getCateName());
				sendKatalk.setNote5(directSendFrontReturnUrl);

				try {
					directsendKakaoAllimApi.send(sendKatalk);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				//병의원 회원 SMS발송
				sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(193);
				sendKatalk.setName(Utils.unSafeHTML(sess.getClinic().getClinicName()));
				sendKatalk.setMobile(sess.getClinic().getAlarmTel1()+sess.getClinic().getAlarmTel2());
				sendKatalk.setNote1(infoMem.getName());
				sendKatalk.setNote2(infoMem.getMtel1()+infoMem.getMtel2());
				sendKatalk.setNote3(reservation.getRdate()+" "+reservation.getRtime());
				sendKatalk.setNote4(reservation.getCateName());
				sendKatalk.setNote5(directSendClinicReturnUrl);
				//야간 발송 금지 시 예약 시간 등록
				sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());

				try {
					directsendKakaoAllimApi.send(sendKatalk);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				//닥터팩 상담 30분전 안내
				if("016005".equals(reservation.getCate())) {
					//발송시간 구하기
					String sendTime = "";
					SimpleDateFormat simplReser = new SimpleDateFormat("yyyy.MM.dd HH:mm");
					SimpleDateFormat simplSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					Date reserTime = simplReser.parse(reservation.getRdate()+" "+reservation.getRtime().substring(0, 5));
					Calendar cal = Calendar.getInstance();
					cal.setTime(reserTime);
					cal.add(Calendar.MINUTE, -30);
					sendTime = simplSend.format(cal.getTime());


					//일반 회원 SMS발송
					sendKatalk = new SendKatalk();
					sendKatalk.setUserTemplateNo(232);
					sendKatalk.setName(infoMem.getName());
					sendKatalk.setMobile(infoMem.getMtel1()+infoMem.getMtel2());
					sendKatalk.setNote1(reservation.getRdate()+" "+reservation.getRtime());
					sendKatalk.setNote2(directSendFrontReturnUrl+"/healthCheck/resultStep2?healthSeq="+reservation.getHealthSeq());
					sendKatalk.setNote3(directSendFrontReturnUrl);

					//예약발송
					sendKatalk.setReserveTime(sendTime);
					try {
						directsendKakaoAllimApi.send(sendKatalk);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

					//병의원 회원 SMS발송
					sendKatalk = new SendKatalk();
					sendKatalk.setUserTemplateNo(235);
					sendKatalk.setName(Utils.unSafeHTML(sess.getClinic().getClinicName()));
					sendKatalk.setMobile(sess.getClinic().getAlarmTel1()+sess.getClinic().getAlarmTel2());
					sendKatalk.setNote1(infoMem.getName());
					sendKatalk.setNote2(reservation.getRdate()+" "+reservation.getRtime());
					sendKatalk.setNote3(directSendClinicReturnUrl);
					sendKatalk.setNote4(directSendClinicReturnUrl);
					//예약발송
					sendKatalk.setReserveTime(sendTime);

					try {
						directsendKakaoAllimApi.send(sendKatalk);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

				}

			}else if("025003".equals(reservation.getStatus())) { //상담취소
				//일반 회원 SMS발송
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(187);
				sendKatalk.setName(infoMem.getName());
				sendKatalk.setMobile(infoMem.getMtel1()+infoMem.getMtel2());
				sendKatalk.setNote1(Utils.unSafeHTML(sess.getClinic().getClinicName()));
				sendKatalk.setNote2(reservation.getRdate()+" "+reservation.getRtime());
				sendKatalk.setNote3(Utils.unSafeHTML(reservation.getReason()));
				sendKatalk.setNote4(directSendFrontReturnUrl);

				try {
					directsendKakaoAllimApi.send(sendKatalk);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				//병의원 회원 SMS발송
				sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(196);
				sendKatalk.setName(Utils.unSafeHTML(sess.getClinic().getClinicName()));
				sendKatalk.setMobile(sess.getClinic().getAlarmTel1()+sess.getClinic().getAlarmTel2());
				sendKatalk.setNote1(infoMem.getName());
				sendKatalk.setNote2(infoMem.getMtel1()+infoMem.getMtel2());
				sendKatalk.setNote3(reservation.getRdate()+" "+reservation.getRtime());
				sendKatalk.setNote4(Utils.unSafeHTML(reservation.getReason()));
				sendKatalk.setNote5(directSendClinicReturnUrl);
				//야간 발송 금지 시 예약 시간 등록
				sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());

				try {
					directsendKakaoAllimApi.send(sendKatalk);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

			}

		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "수정되었습니다.");
	}


}
