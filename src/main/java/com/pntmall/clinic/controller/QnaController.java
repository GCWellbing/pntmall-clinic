package com.pntmall.clinic.controller;

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
import com.pntmall.clinic.domain.Qna;
import com.pntmall.clinic.domain.QnaImg;
import com.pntmall.clinic.domain.QnaSearch;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.ClinicService;
import com.pntmall.clinic.service.CodeService;
import com.pntmall.clinic.service.QnaService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.directSend.DirectsendKakaoAllimApi;
import com.pntmall.common.directSend.SendKatalk;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/cs/qna")
public class QnaController {
	public static final Logger logger = LoggerFactory.getLogger(QnaController.class);

    @Value("${directSend.front.returnUrl}")
	String directSendFrontReturnUrl;

    @Autowired
	private QnaService qnaService;

    @Autowired
	private CodeService codeService;

    @Autowired
	private ClinicService clinicService;

    @Autowired
	DirectsendKakaoAllimApi directsendKakaoAllimApi;

	@GetMapping("/list")
	public ModelAndView list(@ModelAttribute QnaSearch qnaSearch) {
		logger.debug("list", qnaSearch);
		ClinicSession sess = ClinicSession.getInstance();
		qnaSearch.setClinicMemNo(sess.getMemNo());
		List<Code> cateList1 = codeService.getList2("014");
		List<Code> cateList2 = codeService.getList2("015");

		List<Qna> list = qnaService.getList(qnaSearch);
		Integer count = qnaService.getCount(qnaSearch);
		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), count, qnaSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("cateList1", cateList1);
		mav.addObject("cateList2", cateList2);
		mav.addObject("list", list);
		mav.addObject("count", count);
		mav.addObject("paging", paging);
		mav.addObject("qnaSearch", qnaSearch);

		return mav;
	}

	@GetMapping("/edit")
	public ModelAndView edit(@ModelAttribute QnaSearch qnaSearch) {
		String mode = "modify";
		List<Code> cateList = codeService.getList2("003");

		ClinicSession sess = ClinicSession.getInstance();
		qnaSearch.setClinicMemNo(sess.getMemNo());

		Qna qna = qnaService.getInfo(qnaSearch);
		List<QnaImg> qnaImgList = qnaService.getQnaImgList(qnaSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("mode", mode);
		mav.addObject("qna", qna);
		mav.addObject("qnaImgList", qnaImgList);
		mav.addObject("cateList", cateList);

		return mav;
	}

	@PostMapping("/modify")
	@ResponseBody
	public ResultMessage modify(@ModelAttribute Qna qna, HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();

		try {
			qna.setStatus(qna.getAnswer().length() > 0 ? "A":"Q");

			qna.setCuser(sess.getMemNo());
			qna.setUuser(sess.getMemNo());
			qna.setUuser(sess.getMemNo());
			qnaService.modify(qna);

			if("A".equals(qna.getStatus()) ){
				//회원정보
				Member infoMem = clinicService.infoMem(qna.getQuser());
				//SMS발송
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(175);
				sendKatalk.setName(infoMem.getName());
				sendKatalk.setMobile(infoMem.getMtel1()+infoMem.getMtel2());
				sendKatalk.setNote1(directSendFrontReturnUrl);
				//예약발송
				//sendKatalk.setReserveTime("2021-10-07 11:40:00");
				//야간 발송 금지 시 예약 시간 등록
				//sendKatalk.setReserveTime(directsendKakaoAllimApi.nightSendChk());
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
