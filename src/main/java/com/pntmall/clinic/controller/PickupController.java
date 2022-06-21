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

import com.pntmall.clinic.domain.Order;
import com.pntmall.clinic.domain.OrderItem;
import com.pntmall.clinic.domain.OrderMemo;
import com.pntmall.clinic.domain.OrderRefund;
import com.pntmall.clinic.domain.OrderSearch;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.PickupService;
import com.pntmall.common.ResultMessage;
import com.pntmall.common.type.Param;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/order")
public class PickupController {
	public static final Logger logger = LoggerFactory.getLogger(PickupController.class);

    @Autowired
	private PickupService pickupService;

    @GetMapping("/pickup/list")
	public ModelAndView pickupList(@ModelAttribute OrderSearch orderSearch) {
		ClinicSession sess = ClinicSession.getInstance();
		orderSearch.setOrderGubun(3);
		orderSearch.setPickupClinic(sess.getMemNo());
		
		List<Order> list = pickupService.getList(orderSearch);
		Integer count = pickupService.getCount(orderSearch);
		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), count, orderSearch);

		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);
		mav.addObject("count", count);
		mav.addObject("paging", paging);

		return mav;
	}

	@GetMapping("/pickup/edit")
	public ModelAndView pickupEdit(@ModelAttribute Order order) {
		ClinicSession sess = ClinicSession.getInstance();
		order.setPickupClinic(sess.getMemNo());
		
		order = pickupService.getInfo(order);
		List<OrderItem> itemList = pickupService.getItemList(order.getOrderid());
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("order", order);
		mav.addObject("itemList", itemList);

		return mav;
	}

	@PostMapping("/pickup/addMemo")
	@ResponseBody
	public ResultMessage addMemo(@ModelAttribute OrderMemo orderMemo, HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();

		try {
			orderMemo.setCuser(sess.getMemNo());
			pickupService.createMemo(orderMemo);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}
		
		return new ResultMessage(true, "등록되었습니다.");
	}
	
	@PostMapping("/pickup/modifyStatus")
	@ResponseBody
	public ResultMessage modifyStatus(@ModelAttribute Order order, HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();

		try {
			order.setCuser(sess.getMemNo());
			order.setPickupClinic(sess.getMemNo());
			pickupService.modifyStatus(order);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, "오류가 발생했습니다.");
		}
		
		return new ResultMessage(true, "주문상태가 변경되었습니다.");
	}

	@PostMapping("/pickup/cancel")
	@ResponseBody
	public ResultMessage cancel(HttpServletRequest request) {
		ClinicSession sess = ClinicSession.getInstance();

		Param param = new Param(request.getParameterMap());
		String orderid = param.get("orderid");
		
		Order order = pickupService.getInfo(orderid, sess.getMemNo());
		if(order == null || "110,120".indexOf(order.getStatus()) == -1) {
			return new ResultMessage(false, "취소할 수 없는 상태입니다.");
		}
		
		try {
			OrderRefund orderRefund = new OrderRefund();
			orderRefund.setBank(param.get("refundBank"));
			orderRefund.setAccount(param.get("refundAccount"));
			orderRefund.setDepositor(param.get("refundDepositor"));
			orderRefund.setCuser(sess.getMemNo());
			pickupService.cancel(order, orderRefund);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultMessage(false, e.getMessage());
		}
		
		return new ResultMessage(true, "취소되었습니다.");
	}

}
