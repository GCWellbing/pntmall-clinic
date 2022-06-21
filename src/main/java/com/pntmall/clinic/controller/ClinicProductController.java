package com.pntmall.clinic.controller;

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

import com.pntmall.clinic.domain.ClinicProduct;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.ClinicProductService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.type.Param;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/product")
public class ClinicProductController {
	public static final Logger logger = LoggerFactory.getLogger(ClinicProductController.class);

    @Autowired
	private ClinicProductService clinicProductService;

	@GetMapping("/list")
	public ModelAndView list(HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();

		List<ClinicProduct> list = clinicProductService.getList(sess.getMemNo());

		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);

		return mav;
	}

	@GetMapping("/product")
	public ModelAndView product(@ModelAttribute ClinicProduct clinicProduct) {
    	List<ClinicProduct> list = clinicProductService.getProductList(clinicProduct);
    	Integer count = clinicProductService.getProductCount(clinicProduct);
		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), count, clinicProduct);

    	ModelAndView mav = new ModelAndView();
    	mav.addObject("list", list);
    	mav.addObject("count", count);
		mav.addObject("paging", paging);
		
		return mav;
	}

	@PostMapping("/create")
	@ResponseBody
	public ResultMessage create(HttpServletRequest request) {
		try {
			clinicProductService.create(new Param(request.getParameterMap()));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}
		
		return new ResultMessage(true, "등록되었습니다.");
	}

	@PostMapping("/remove")
	@ResponseBody
	public ResultMessage remove(@ModelAttribute ClinicProduct clinicProduct) {
		ClinicSession sess = ClinicSession.getInstance();

		try {
			clinicProduct.setMemNo(sess.getMemNo());
			clinicProductService.remove(clinicProduct);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}
		
		return new ResultMessage(true, "삭제되었습니다.");
	}
}
