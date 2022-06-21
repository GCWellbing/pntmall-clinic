package com.pntmall.clinic.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pntmall.clinic.domain.Code;
import com.pntmall.clinic.domain.Notice;
import com.pntmall.clinic.domain.NoticeImg;
import com.pntmall.clinic.domain.NoticeSearch;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.service.ClinicNoticeService;
import com.pntmall.clinic.service.CodeService;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/notice")
public class ClinicNoticeController {
	public static final Logger logger = LoggerFactory.getLogger(ClinicNoticeController.class);

    @Autowired
	private ClinicNoticeService noticeService;

    @Autowired
	private CodeService codeService;

	@GetMapping("/list")
	public ModelAndView list(@ModelAttribute NoticeSearch noticeSearch) {
		logger.debug("list noticeSearch:"+noticeSearch.getFromCdate()+"::"+noticeSearch.getToCdate());
		List<Code> cateList = codeService.getList2("004");

		List<Notice> list = noticeService.getList(noticeSearch);
		Integer count = noticeService.getCount(noticeSearch);
		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), count, noticeSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("cateList", cateList);
		mav.addObject("list", list);
		mav.addObject("count", count);
		mav.addObject("paging", paging);
		mav.addObject("noticeSearch", noticeSearch);

		return mav;
	}

	@GetMapping("/edit")
	public ModelAndView edit(Integer noticeNo) {
		String mode = "create";
		List<Code> cateList = codeService.getList2("004");
		Notice notice = new Notice();
		List<NoticeImg> noticeImgList = new ArrayList<NoticeImg> ();
		if(noticeNo != null && noticeNo > 0) {
			mode = "modify";
			notice = noticeService.getInfo(noticeNo);
			noticeImgList = noticeService.getNoticeImgList(noticeNo);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("mode", mode);
		mav.addObject("notice", notice);
		mav.addObject("noticeImgList", noticeImgList);
		mav.addObject("cateList", cateList);

		return mav;
	}




}
