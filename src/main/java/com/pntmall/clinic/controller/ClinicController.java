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


		//???????????? ?????? ?????? ??????
		//SAP ?????? ??????
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

			//SMS??????
			//?????? ?????? SMS??????
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
			return new ResultMessage(false, "????????? ??????????????????."+e);
		}

		return new ResultMessage(true, "?????????????????????");
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

		//????????????
		List<Code> approveList = codeService.getList2("006");
		//??????
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

			//SMS?????? - ??????????????? ?????? ?????? ????????????
		    if(!clinic.getSmsYn().equals(clinic.getSmsYnOld()) || !clinic.getEmailYn().equals(clinic.getEmailYnOld()) ) {
				try {
					SendKatalk sendKatalk = new SendKatalk();
					sendKatalk.setUserTemplateNo(265);
					sendKatalk.setName(clinic.getName());
					sendKatalk.setMobile(clinic.getMtel1()+clinic.getMtel2());
					sendKatalk.setNote1("Y".equals(clinic.getSmsYn())?"??????":"?????????");
					sendKatalk.setNote2("Y".equals(clinic.getEmailYn())?"??????":"?????????");
					sendKatalk.setNote3(directSendClinicReturnUrl);

					directsendKakaoAllimApi.send(sendKatalk);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
		    }

		    //????????? SMS ?????? - ????????? ID pntmall ??????
			try {
			    Admin admin = clinicService.getAdminInfo("pntmall");
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(253);
				sendKatalk.setName(admin.getName());
				sendKatalk.setMobile(admin.getMtel());
				sendKatalk.setNote1(Utils.unSafeHTML(clinic.getClinicName()));
				sendKatalk.setNote2(sess.getClinic().getMemId());
				//?????? ?????? ?????? ??? ?????? ?????? ??????
				sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());

				directsendKakaoAllimApi.send(sendKatalk);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "????????? ??????????????????.");
		}

		return new ResultMessage(true, "?????????????????????.");
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


		//????????????
		List<Code> approveList = codeService.getList2("006");
		//??????
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

		    //????????? SMS ?????? - ????????? ID pntmall ??????
			try {
			    Admin admin = clinicService.getAdminInfo("pntmall");
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(256);
				sendKatalk.setName(admin.getName());
				sendKatalk.setMobile(admin.getMtel());
				sendKatalk.setNote1(Utils.unSafeHTML(sess.getClinic().getClinicName()));
				sendKatalk.setNote2(sess.getClinic().getMemId());
				//?????? ?????? ?????? ??? ?????? ?????? ??????
				sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());

				directsendKakaoAllimApi.send(sendKatalk);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "????????? ??????????????????.");
		}

		return new ResultMessage(true, "?????? ?????? ???????????? ????????? ?????????????????????.");
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
				return new ResultMessage(false, "????????? ??????????????? ????????????.");
			}
			if(enc.equals(info.getPasswd())){
				return new ResultMessage(false, "????????? ??????????????? ???????????????. ?????? ??????????????? ???????????? ?????????.");
			}

			info.setPasswd(enc);
			clinicService.setPasswd(info);

			return new ResultMessage(true, "??????????????? ?????????????????????.");
		} catch (Exception e) {
			return new ResultMessage(false, "????????? ??????????????????..");
		}


	}


	@PostMapping("/find/pwdProc")
	@ResponseBody
	public ResultMessage pwdProc(@ModelAttribute ClinicSearch clinicSearch) {
		Clinic info = clinicService.getInfo(clinicSearch);

		if(info == null || "".equals(info.getMemId()) || !"S".equals(info.getStatus())) {
			return new ResultMessage(false, "????????? ID??? ???????????? ?????????, ?????????????????? ????????????.");
		}

		return new ResultMessage(true, "?????? ???????????????.");
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

		//????????????
		String confirmNum   = StringUtils.EMPTY;
		Random random = new Random();
		String a = "";
		for(int i = 0;i < 6;i++){ // 6?????? ?????? ??????
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
			//SMS??????
			SendKatalk sendKatalk = new SendKatalk();
			sendKatalk.setUserTemplateNo(130);
			sendKatalk.setName(info.getName());
			sendKatalk.setMobile(info.getMtel1()+info.getMtel2());
			sendKatalk.setNote1(confirmNum);
			//????????????
			//sendKatalk.setReserveTime("2021-10-07 11:40:00");
			//?????? ?????? ?????? ??? ?????? ?????? ??????
			//sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());
			try {
				directsendKakaoAllimApi.send(sendKatalk);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			//cookie ??????
			Utils.setCookie(response, "CONFIRM_NUM_PASS", confirmNumEnc, request.getServerName(), 60);

		}else if("MAIL".equals(clinicSearch.getSendType())){
			Param param = new Param();
			param.set("confirmNum",confirmNum);
			param.set("name",info.getName());
			param.set("directSendFrontReturnUrl",directSendFrontReturnUrl);

			SendMail sendMail = new SendMail();
			sendMail.setTempUrl(directSendFrontReturnUrl+"/mailTemp/pwdConfirmNum");
			sendMail.setSubject("PNTmall ?????????????????? ??????????????? ????????? ????????????.");
			sendMail.setName(info.getName());
			sendMail.setEmail(info.getEmail());
			sendMail.setParam(param);
			try {
				directsendMailApi.send(sendMail);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//cookie ??????
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
			//cookie ????????????
			String confirmNumCookie = Utils.getCookie(request, "CONFIRM_NUM_PASS");

			if(confirmNumCookie.equals(certiNoEnc)) {
				return new ResultMessage(true, "?????? ???????????????.");
			}else {
				return new ResultMessage(false, "??????????????? ????????????.");
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResultMessage(false, "????????? ??????????????????..");
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
				return new ResultMessage(false, "????????? ??????????????? ???????????????. ?????? ??????????????? ???????????? ?????????.");
			}

			info.setPasswd(enc);
			clinicService.setPasswd(info);

			return new ResultMessage(true, "??????????????? ?????????????????????.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResultMessage(false, "????????? ??????????????????..");
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
