package com.pntmall.clinic.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pntmall.clinic.domain.Admin;
import com.pntmall.clinic.domain.AgreeDocu;
import com.pntmall.clinic.domain.Clinic;
import com.pntmall.clinic.domain.ClinicImg;
import com.pntmall.clinic.domain.ClinicJoin;
import com.pntmall.clinic.domain.ClinicSap;
import com.pntmall.clinic.domain.ClinicSearch;
import com.pntmall.clinic.domain.Code;
import com.pntmall.clinic.domain.Stipulation;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.ClinicService;
import com.pntmall.clinic.service.CodeService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.directSend.DirectsendKakaoAllimApi;
import com.pntmall.common.directSend.DirectsendMailApi;
import com.pntmall.common.directSend.SendKatalk;
import com.pntmall.common.directSend.SendMail;
import com.pntmall.common.type.Param;
import com.pntmall.common.utils.SecurityUtils;
import com.pntmall.common.utils.Utils;
@Controller
@RequestMapping("/clinic")
public class ClinicController {
	public static final Logger logger = LoggerFactory.getLogger(ClinicController.class);

    @Value("${directSend.clinic.returnUrl}")
	String directSendClinicReturnUrl;

    @Value("${directSend.front.returnUrl}")
	String directSendFrontReturnUrl;

    @Autowired
	private ClinicService clinicService;

    @Autowired
	private CodeService codeService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
	DirectsendKakaoAllimApi directsendKakaoAllimApi;

    @Autowired
	DirectsendMailApi directsendMailApi;


	@GetMapping("/join/agree")
	public ModelAndView agree(Integer privacyNo) {
		String mode = "create";
		AgreeDocu agreeDocu = clinicService.getAgreeDocuInfo();

		ModelAndView mav = new ModelAndView();
		mav.addObject("agree", agreeDocu);

		return mav;
	}


	@GetMapping("/join/existsBusinessNo")
	@ResponseBody
	public ResultMessage existsBusinessNo(@ModelAttribute ClinicSearch clinicSearch) {
		logger.debug("exists::"+clinicSearch);

		Param param = new Param();
		param.set("isExists", clinicService.isExistsBusinessNo(clinicSearch));
		return new ResultMessage(true, "", param);
	}


	@GetMapping("/join/exists")
	@ResponseBody
	public ResultMessage exists(@ModelAttribute ClinicSearch clinicSearch) {
		logger.debug("exists::"+clinicSearch);
		Param param = new Param();
		param.set("isExists", clinicService.isExists(clinicSearch));
		return new ResultMessage(true, "", param);
	}

	@PostMapping("/join/form")
	public ModelAndView form(@ModelAttribute ClinicSearch clinicSearch) {
		logger.debug("form:111:"+clinicSearch);
		AgreeDocu agreeDocu = clinicService.getAgreeDocuInfo();


		//오프라인 회원 정보 체크
		//SAP 정보 조회
		ClinicSap clinicSap = clinicService.infoClinicSap(clinicSearch);

		List<Code> mtelList = codeService.getList2("011");
		List<Code> telList = codeService.getList2("012");
		List<Code> emailList = codeService.getList2("009");
		List<Code> bankList = codeService.getList2("007");

		ModelAndView mav = new ModelAndView();
		mav.addObject("clinicSap", clinicSap);
		mav.addObject("mtelList", mtelList);
		mav.addObject("telList", telList);
		mav.addObject("emailList", emailList);
		mav.addObject("bankList", bankList);
		mav.addObject("agree", agreeDocu);

		return mav;
	}

	@PostMapping("/join/Proc")
	@ResponseBody
	public ResultMessage joinProc(@ModelAttribute Clinic clinic, HttpServletRequest request) {
		logger.debug("joinProc::"+clinic);

		try {
			Param param = new Param(request.getParameterMap());
			clinic.setPasswd(securityUtils.encodeSHA512(clinic.getPasswd()));
			clinicService.create(clinic, param);

			//SMS발송
			//일반 회원 SMS발송
			SendKatalk sendKatalk = new SendKatalk();
			sendKatalk.setUserTemplateNo(145);
			sendKatalk.setName(Utils.unSafeHTML(clinic.getClinicName()));
			sendKatalk.setMobile(clinic.getMtel1()+clinic.getMtel2());
			sendKatalk.setNote1(clinic.getMemId());
			sendKatalk.setNote2(directSendClinicReturnUrl);

			try {
				directsendKakaoAllimApi.send(sendKatalk);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다."+e);
		}

		return new ResultMessage(true, "등록되었습니다");
	}

	@GetMapping("/edit")
	public ModelAndView edit() {
		ClinicSession sess = ClinicSession.getInstance();
		ClinicSearch clinicSearch = new ClinicSearch();

		Integer memNo = sess.getMemNo();
		clinicSearch.setMemNo(memNo);

		String mode = "modify";
		Clinic clinic = clinicService.getInfo(clinicSearch);
		List<ClinicJoin> clinicJoinList = clinicService.getClinicJoinList(clinicSearch);
		List<ClinicImg> clinicImgList = clinicService.getClinicImgList(clinicSearch);

		//가입상태
		List<Code> approveList = codeService.getList2("006");
		//은행
		List<Code> bankList = codeService.getList2("007");
		List<Code> mtelList = codeService.getList2("011");
		List<Code> telList = codeService.getList2("012");
		List<Code> emailList = codeService.getList2("009");

		ModelAndView mav = new ModelAndView();
		mav.addObject("mode", mode);
		mav.addObject("approveList", approveList);
		mav.addObject("mtelList", mtelList);
		mav.addObject("telList", telList);
		mav.addObject("emailList", emailList);
		mav.addObject("bankList", bankList);
		mav.addObject("clinic", clinic);
		mav.addObject("clinicJoinList", clinicJoinList);
		mav.addObject("clinicImgList", clinicImgList);

		return mav;
	}

	@PostMapping("/modify")
	@ResponseBody
	public ResultMessage modify(@ModelAttribute Clinic clinic, HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();
		Param param = new Param(request.getParameterMap());

		try {
			clinic.setCuser(sess.getClinic().getMemNo());
			clinicService.modify(clinic, param);

			//SMS발송 - 수신동의가 기존 값과 틀릴경우
		    if(!clinic.getSmsYn().equals(clinic.getSmsYnOld()) || !clinic.getEmailYn().equals(clinic.getEmailYnOld()) ) {
				try {
					SendKatalk sendKatalk = new SendKatalk();
					sendKatalk.setUserTemplateNo(265);
					sendKatalk.setName(clinic.getName());
					sendKatalk.setMobile(clinic.getMtel1()+clinic.getMtel2());
					sendKatalk.setNote1("Y".equals(clinic.getSmsYn())?"동의":"미동의");
					sendKatalk.setNote2("Y".equals(clinic.getEmailYn())?"동의":"미동의");
					sendKatalk.setNote3(directSendClinicReturnUrl);

					directsendKakaoAllimApi.send(sendKatalk);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
		    }

		    //관리자 SMS 발송 - 관리자 ID pntmall 고정
			try {
			    Admin admin = clinicService.getAdminInfo("pntmall");
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(253);
				sendKatalk.setName(admin.getName());
				sendKatalk.setMobile(admin.getMtel());
				sendKatalk.setNote1(Utils.unSafeHTML(clinic.getClinicName()));
				sendKatalk.setNote2(sess.getClinic().getMemId());
				//야간 발송 금지 시 예약 시간 등록
				sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());

				directsendKakaoAllimApi.send(sendKatalk);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "수정되었습니다.");
	}

	@GetMapping("/calcu/edit")
	public ModelAndView calcuEdit() {
		ClinicSession sess = ClinicSession.getInstance();
		ClinicSearch clinicSearch = new ClinicSearch();

		Integer memNo = sess.getMemNo();
		clinicSearch.setMemNo(memNo);

		String mode = "modify";
		Clinic clinic = clinicService.getInfo(clinicSearch);
		List<ClinicJoin> clinicJoinList = clinicService.getClinicJoinList(clinicSearch);
		List<ClinicImg> clinicImgList = clinicService.getClinicImgList(clinicSearch);

		AgreeDocu agreeDocu = clinicService.getAgreeDocuInfo();


		//가입상태
		List<Code> approveList = codeService.getList2("006");
		//은행
		List<Code> bankList = codeService.getList2("007");

		ModelAndView mav = new ModelAndView();
		mav.addObject("mode", mode);
		mav.addObject("approveList", approveList);
		mav.addObject("bankList", bankList);
		mav.addObject("clinic", clinic);
		mav.addObject("clinicJoinList", clinicJoinList);
		mav.addObject("clinicImgList", clinicImgList);
		mav.addObject("agree", agreeDocu);

		return mav;
	}

	@PostMapping("/calcu/modify")
	@ResponseBody
	public ResultMessage calcuModify(@ModelAttribute Clinic clinic, HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();
		Param param = new Param(request.getParameterMap());

		try {
			clinic.setCuser(sess.getClinic().getMemNo());
			clinicService.calcuModify(clinic, param);

		    //관리자 SMS 발송 - 관리자 ID pntmall 고정
			try {
			    Admin admin = clinicService.getAdminInfo("pntmall");
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(256);
				sendKatalk.setName(admin.getName());
				sendKatalk.setMobile(admin.getMtel());
				sendKatalk.setNote1(Utils.unSafeHTML(sess.getClinic().getClinicName()));
				sendKatalk.setNote2(sess.getClinic().getMemId());
				//야간 발송 금지 시 예약 시간 등록
				sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());

				directsendKakaoAllimApi.send(sendKatalk);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "정산 정보 업데이트 요청이 완료되었습니다.");
	}

	@GetMapping("/join/stipulation")
	public ModelAndView stipulation(Integer gubun) {
		Stipulation stipulation = clinicService.getStipulation(gubun);

		ModelAndView mav = new ModelAndView();
		mav.addObject("stipulation", stipulation);

		return mav;
	}

	@GetMapping("/join/sensitive")
	public ModelAndView sensitive(Integer gubun) {
		Stipulation stipulation = clinicService.getSensitive(gubun);

		ModelAndView mav = new ModelAndView();
		mav.addObject("stipulation", stipulation);

		return mav;
	}

	@GetMapping("/join/privacy")
	public ModelAndView privacy(Integer gubun) {
		Stipulation stipulation = clinicService.getPrivacy(gubun);

		ModelAndView mav = new ModelAndView();
		mav.addObject("stipulation", stipulation);

		return mav;
	}

	@PostMapping("/changPassWdProc")
	@ResponseBody
	public ResultMessage changPassWdProc(@ModelAttribute ClinicSearch clinicSearch, HttpServletRequest request, HttpServletResponse response) {
		try {
			ClinicSession sess = ClinicSession.getInstance();
			Integer memNo = sess.getMemNo();

			String passwd = clinicSearch.getPasswd();
			String oldPasswd = clinicSearch.getOldPasswd();


			clinicSearch.setMemNo(memNo);
			Clinic info = clinicService.getInfo(clinicSearch);
			String enc = securityUtils.encodeSHA512(passwd);
			String encOld = securityUtils.encodeSHA512(oldPasswd);
			logger.debug("exists::"+clinicSearch);
			if(!encOld.equals(info.getPasswd())){
				return new ResultMessage(false, "기존의 패스워드가 틀립니다.");
			}
			if(enc.equals(info.getPasswd())){
				return new ResultMessage(false, "기존의 패스워드와 동일합니다. 다른 패스워드를 입력하여 주세요.");
			}

			info.setPasswd(enc);
			clinicService.setPasswd(info);

			return new ResultMessage(true, "패스워드가 변경되었습니다.");
		} catch (Exception e) {
			return new ResultMessage(false, "오류가 발생했습니다..");
		}


	}


	@PostMapping("/find/pwdProc")
	@ResponseBody
	public ResultMessage pwdProc(@ModelAttribute ClinicSearch clinicSearch) {
		Clinic info = clinicService.getInfo(clinicSearch);

		if(info == null || "".equals(info.getMemId()) || !"S".equals(info.getStatus())) {
			return new ResultMessage(false, "로그인 ID가 정확하지 않거나, 병의원회원이 아닙니다.");
		}

		return new ResultMessage(true, "조회 되었습니다.");
	}

	@GetMapping("/find/pwdCheck")
	public ModelAndView pwdCheck(@ModelAttribute ClinicSearch clinicSearch) {
		Clinic info = clinicService.getInfo(clinicSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("info", info);

		return mav;
	}

	@PostMapping("/find/pwdConfirm")
	public ModelAndView pwdConfirm(@ModelAttribute ClinicSearch clinicSearch, HttpServletRequest request, HttpServletResponse response) {
		Clinic info = clinicService.getInfo(clinicSearch);

		boolean isOK = false;

		//난수발생
		String confirmNum   = StringUtils.EMPTY;
		Random random = new Random();
		String a = "";
		for(int i = 0;i < 6;i++){ // 6자리 난수 생성
			a = Integer.toString(random.nextInt(9));
			confirmNum = confirmNum + a;
		}
		String confirmNumEnc = "";
		try {
			confirmNumEnc = securityUtils.encodeSHA512(confirmNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}

		if("SMS".equals(clinicSearch.getSendType())){
			//SMS발송
			SendKatalk sendKatalk = new SendKatalk();
			sendKatalk.setUserTemplateNo(130);
			sendKatalk.setName(info.getName());
			sendKatalk.setMobile(info.getMtel1()+info.getMtel2());
			sendKatalk.setNote1(confirmNum);
			//예약발송
			//sendKatalk.setReserveTime("2021-10-07 11:40:00");
			//야간 발송 금지 시 예약 시간 등록
			//sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());
			try {
				directsendKakaoAllimApi.send(sendKatalk);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			//cookie 저장
			Utils.setCookie(response, "CONFIRM_NUM_PASS", confirmNumEnc, request.getServerName(), 60);

		}else if("MAIL".equals(clinicSearch.getSendType())){
			Param param = new Param();
			param.set("confirmNum",confirmNum);
			param.set("name",info.getName());
			param.set("directSendFrontReturnUrl",directSendFrontReturnUrl);

			SendMail sendMail = new SendMail();
			sendMail.setTempUrl(directSendFrontReturnUrl+"/mailTemp/pwdConfirmNum");
			sendMail.setSubject("PNTmall 비밀번호찾기 인증번호를 안내해 드립니다.");
			sendMail.setName(info.getName());
			sendMail.setEmail(info.getEmail());
			sendMail.setParam(param);
			try {
				directsendMailApi.send(sendMail);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//cookie 저장
			Utils.setCookie(response, "CONFIRM_NUM_PASS", confirmNumEnc, request.getServerName(), 60 * 11);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("system", System.getProperty("spring.profiles.active"));
		mav.addObject("confirmNum", confirmNum);

		return mav;
	}


	@PostMapping("/find/pwdConfirmProc")
	@ResponseBody
	public ResultMessage pwdConfirmProc(@RequestParam String certiNo, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("certiNo::"+ certiNo);
		try {
			String certiNoEnc = securityUtils.encodeSHA512(certiNo);
			//cookie 가져오기
			String confirmNumCookie = Utils.getCookie(request, "CONFIRM_NUM_PASS");

			if(confirmNumCookie.equals(certiNoEnc)) {
				return new ResultMessage(true, "조회 되었습니다.");
			}else {
				return new ResultMessage(false, "인증번호가 틀립니다.");
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResultMessage(false, "오류가 발생했습니다..");
		}


	}

	@PostMapping("/find/pwdResetProc")
	@ResponseBody
	public ResultMessage pwdResetProc(@ModelAttribute ClinicSearch clinicSearch, HttpServletRequest request, HttpServletResponse response) {
		try {
			Clinic info = clinicService.getInfo(clinicSearch);
			String passwd = clinicSearch.getPasswd();
			String enc = securityUtils.encodeSHA512(passwd);
			if(enc.equals(info.getPasswd())){
				return new ResultMessage(false, "기존의 패스워드와 동일합니다. 다른 패스워드를 입력하여 주세요.");
			}

			info.setPasswd(enc);
			clinicService.setPasswd(info);

			return new ResultMessage(true, "패스워드가 변경되었습니다.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResultMessage(false, "오류가 발생했습니다..");
		}


	}

	@GetMapping("/find/id")
	public ModelAndView id(@ModelAttribute ClinicSearch clinicSearch, HttpServletRequest request) {

		List<Code> telList = codeService.getList2("011");
		List<Code> emailList = codeService.getList2("009");

		ModelAndView mav = new ModelAndView();
		mav.addObject("telList", telList);
		mav.addObject("emailList", emailList);

		return mav;
	}

	@PostMapping("/find/idResult")
	public ModelAndView idResult(@ModelAttribute ClinicSearch clinicSearch, HttpServletRequest request) {
		logger.debug("idResult::"+ clinicSearch);
		List<Clinic> list = clinicService.getList(clinicSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);

		return mav;
	}
}
