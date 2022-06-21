package com.pntmall.clinic.controller;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;

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

import com.pntmall.clinic.domain.ClinicAdjust;
import com.pntmall.clinic.domain.ClinicAdjustDetail;
import com.pntmall.clinic.domain.ClinicAdjustSearch;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.ClinicAdjustService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/adjust")
public class ClinicAdjustController {
	public static final Logger logger = LoggerFactory.getLogger(ClinicAdjustController.class);
	
	@Autowired
	private ClinicAdjustService clinicAdjustService;
	
	@GetMapping("/confirm")
	public ModelAndView confirm(@ModelAttribute ClinicAdjustSearch clinicAdjustSearch) {
		ClinicSession sess = ClinicSession.getInstance();
		
		Integer thisYear = Integer.parseInt(Utils.getTimeStampString("yyyy"));
		
		if(clinicAdjustSearch.getYear() == null) clinicAdjustSearch.setYear(thisYear);
		if(clinicAdjustSearch.getQuarter() == null) clinicAdjustSearch.setQuarter(LocalDate.now().get(IsoFields.QUARTER_OF_YEAR));
		clinicAdjustSearch.setMemNo(sess.getMemNo());
		
		
		ClinicAdjust clinicAdjust = clinicAdjustService.getInfo(clinicAdjustSearch);
		List<ClinicAdjustDetail> list = clinicAdjustService.getDetailList(clinicAdjust);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("clinicAdjust", clinicAdjust);
		mav.addObject("list", list);
		mav.addObject("thisYear", thisYear);
		
		return mav;
	}

	@GetMapping("/list")
	public ModelAndView list(@ModelAttribute ClinicAdjustSearch clinicAdjustSearch) {
		Utils.savePath();
		
		ClinicSession sess = ClinicSession.getInstance();
		Integer thisYear = Integer.parseInt(Utils.getTimeStampString("yyyy"));
		
		if(clinicAdjustSearch.getYear1() == null) clinicAdjustSearch.setYear1(thisYear);
		if(clinicAdjustSearch.getQuarter1() == null) clinicAdjustSearch.setQuarter1(LocalDate.now().get(IsoFields.QUARTER_OF_YEAR));
		if(clinicAdjustSearch.getYear2() == null) clinicAdjustSearch.setYear2(thisYear);
		if(clinicAdjustSearch.getQuarter2() == null) clinicAdjustSearch.setQuarter2(LocalDate.now().get(IsoFields.QUARTER_OF_YEAR));
		clinicAdjustSearch.setMemNo(sess.getMemNo());
		
		List<ClinicAdjust> list = clinicAdjustService.getList(clinicAdjustSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);
		mav.addObject("thisYear", thisYear);
		
		return mav;
	}

	@PostMapping("/confirmProc")
	@ResponseBody
	public ResultMessage confirmProc(@ModelAttribute ClinicAdjustSearch clinicAdjustSearch) {
		ClinicSession sess = ClinicSession.getInstance();
		
		try {
			clinicAdjustSearch.setMemNo(sess.getMemNo());
			ClinicAdjust clinicAdjust = clinicAdjustService.getInfo(clinicAdjustSearch);
			if(clinicAdjust.getStatus() != 20) {
				return new ResultMessage(false, "정산금액확인이 불가능한 상태입니다.");
			}
			clinicAdjust.setCuser(sess.getMemNo());
			clinicAdjust.setStatus(30);
			
			clinicAdjustService.modifyStatus(clinicAdjust);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}

		return new ResultMessage(true, "적용되었습니다.");
	}
	
	@GetMapping("/detail")
	public ModelAndView detail(@ModelAttribute ClinicAdjustSearch clinicAdjustSearch) {
		ModelAndView mav = new ModelAndView();
		ClinicSession sess = ClinicSession.getInstance();
		clinicAdjustSearch.setMemNo(sess.getMemNo());

		ClinicAdjust clinicAdjust = clinicAdjustService.getInfo(clinicAdjustSearch);
		List<ClinicAdjustDetail> list = clinicAdjustService.getDetailList(clinicAdjust);
		
		mav.addObject("clinicAdjust", clinicAdjust);
		mav.addObject("list", list);
		
		return mav;
	}
}
