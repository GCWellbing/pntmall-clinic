package com.pntmall.clinic.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pntmall.clinic.domain.Product;
import com.pntmall.clinic.domain.ProductSearch;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.service.ProductService;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/popup")
public class CommonPopupController {
	public static final Logger logger = LoggerFactory.getLogger(CommonPopupController.class);

    @Autowired
	private ProductService productService;

    @GetMapping("/productDoctorPack")
    public ModelAndView productDoctorPack(@ModelAttribute ProductSearch productSearch) {
    	productSearch.setPtype(1);
    	return productList(productSearch);
    }

    private ModelAndView productList(ProductSearch productSearch) {
    	List<Product> list = productService.getList(productSearch);
    	Integer count = productService.getCount(productSearch);
		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), count, productSearch);

    	ModelAndView mav = new ModelAndView();
    	mav.addObject("list", list);
    	mav.addObject("count", count);
		mav.addObject("paging", paging);

    	return mav;
    }


}
