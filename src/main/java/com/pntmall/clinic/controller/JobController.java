package com.pntmall.clinic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pntmall.clinic.domain.Job;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.service.JobService;
import com.pntmall.common.type.Param;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/job")
public class JobController {
	public static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
	private JobService jobService;

	@GetMapping("/list")
	public ModelAndView list(@ModelAttribute Job job) {
		if(job.getPage() == null) job.setPage(1);
		Param p = jobService.getList(job);
		
		ModelAndView mav = new ModelAndView();

		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), p.getInt("count"), job);

		mav.addObject("list", p.getObject("list"));
		mav.addObject("count", p.getInt("count"));
		mav.addObject("paging", paging);
		
		return mav;
	}

}
