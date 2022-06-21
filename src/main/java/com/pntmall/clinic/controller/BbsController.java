package com.pntmall.clinic.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pntmall.clinic.domain.Bbs;
import com.pntmall.clinic.domain.BbsComment;
import com.pntmall.clinic.domain.BbsImg;
import com.pntmall.clinic.domain.BbsSearch;
import com.pntmall.clinic.domain.Code;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.BbsService;
import com.pntmall.clinic.service.CodeService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.type.Param;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/bbs")
public class BbsController {
	public static final Logger logger = LoggerFactory.getLogger(BbsController.class);

    @Autowired
	private BbsService bbsService;

    @Autowired
	private CodeService codeService;

	@GetMapping("/list")
	public ModelAndView list(@ModelAttribute BbsSearch bbsSearch) {
		logger.debug("list bbsSearch:"+bbsSearch.getFromCdate()+"::"+bbsSearch.getToCdate());
		List<Code> cateList = codeService.getList2("017");

		ClinicSession sess = ClinicSession.getInstance();
		bbsSearch.setCuser(sess.getMemNo());

		List<Bbs> list = bbsService.getList(bbsSearch);
		Integer count = bbsService.getCount(bbsSearch);
		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), count, bbsSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("cateList", cateList);
		mav.addObject("list", list);
		mav.addObject("count", count);
		mav.addObject("paging", paging);
		mav.addObject("bbsSearch", bbsSearch);

		return mav;
	}

	@GetMapping("/edit")
	public ModelAndView edit(Integer bbsNo) {
		String mode = "create";
		List<Code> cateList = codeService.getList2("017");
		Bbs bbs = new Bbs();
		List<BbsImg> bbsImgList = new ArrayList<BbsImg> ();
		List<BbsComment> bbsCommentList = new ArrayList<BbsComment>();
		if(bbsNo != null && bbsNo > 0) {
			mode = "modify";
			bbs = bbsService.getInfo(bbsNo);
			bbsImgList = bbsService.getBbsImgList(bbsNo);
			bbsCommentList = bbsService.getBbsCommentList(bbsNo);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("mode", mode);
		mav.addObject("bbs", bbs);
		mav.addObject("cateList", cateList);
		mav.addObject("bbsImgList", bbsImgList);
		mav.addObject("bbsCommentList", bbsCommentList);

		return mav;
	}

	@PostMapping("/create")
	@ResponseBody
	public ResultMessage create(@ModelAttribute Bbs bbs, HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();
		Param param = new Param(request.getParameterMap());

		try {
			bbs.setFixYn("Y".equals(bbs.getFixYn())?"Y":"N");
			bbs.setCuser(sess.getMemNo());
			bbsService.create(bbs, param);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "등록되었습니다.");
	}

	@PostMapping("/modify")
	@ResponseBody
	public ResultMessage modify(@ModelAttribute Bbs bbs, HttpServletRequest request) {

		ClinicSession sess = ClinicSession.getInstance();
		Param param = new Param(request.getParameterMap());

		try {
			bbs.setFixYn("Y".equals(bbs.getFixYn())?"Y":"N");
			bbs.setCuser(sess.getMemNo());
			bbsService.modify(bbs, param);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "수정되었습니다.");
	}

	@PostMapping("/delete")
	@ResponseBody
	public ResultMessage delete(@ModelAttribute Bbs bbs) {
		ClinicSession sess = ClinicSession.getInstance();
		try {
			bbs.setCuser(sess.getMemNo());
			bbsService.delete(bbs);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "처리되었습니다.");
	}


	@PostMapping("/insertComment")
	@ResponseBody
	public ResultMessage insertComment(@ModelAttribute BbsComment bbsComment) {
		ClinicSession sess = ClinicSession.getInstance();
		try {
			bbsComment.setCuser(sess.getMemNo());
			bbsComment.setStatus("S");
			bbsService.insertComment(bbsComment);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "등록되었습니다.");
	}

	@PostMapping("/updateComment")
	@ResponseBody
	public ResultMessage updateComment(@ModelAttribute BbsComment bbsComment) {
		ClinicSession sess = ClinicSession.getInstance();
		try {
			bbsComment.setCuser(sess.getMemNo());
			bbsService.updateComment(bbsComment);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "처리되었습니다.");
	}

	@PostMapping("/deleteComment")
	@ResponseBody
	public ResultMessage deleteComment(@ModelAttribute BbsComment bbsComment) {
		ClinicSession sess = ClinicSession.getInstance();
		try {
			bbsComment.setCuser(sess.getMemNo());
			bbsService.deleteComment(bbsComment);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "처리되었습니다.");
	}


}
