package com.pntmall.clinic.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pntmall.clinic.domain.Code;
import com.pntmall.clinic.domain.MemGrade;
import com.pntmall.clinic.domain.Order;
import com.pntmall.clinic.domain.OrderAddr;
import com.pntmall.clinic.domain.OrderItem;
import com.pntmall.clinic.domain.OrderMemo;
import com.pntmall.clinic.domain.OrderRefund;
import com.pntmall.clinic.domain.OrderSearch;
import com.pntmall.clinic.domain.OrderShip;
import com.pntmall.clinic.domain.OrderStatusCode;
import com.pntmall.clinic.domain.PaymentLog;
import com.pntmall.clinic.etc.ClinicPaging;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.clinic.service.CodeService;
import com.pntmall.clinic.service.MemGradeService;
import com.pntmall.clinic.service.OrderService;
import com.pntmall.common.payment.TossService;
import com.pntmall.common.utils.HttpRequestHelper;
import com.pntmall.common.utils.Utils;

@Controller
@RequestMapping("/order/order")
public class OrderController {
	public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
	private OrderService orderService;

    @Autowired
	private MemGradeService memGradeService;

    @Autowired
	private CodeService codeService;

    @GetMapping("/list")
	public ModelAndView list(@ModelAttribute OrderSearch orderSearch) {
		Utils.savePath();
		
		ClinicSession cs = ClinicSession.getInstance();
		orderSearch.setClinicMemNo(cs.getMemNo());
		if(StringUtils.isEmpty(orderSearch.getSdate())) orderSearch.setSdate(Utils.getTimeStampString("yyyy.MM.dd"));
		if(StringUtils.isEmpty(orderSearch.getEdate())) orderSearch.setEdate(Utils.getTimeStampString("yyyy.MM.dd"));
		
		List<OrderStatusCode> statusList = orderService.getOrderStatusCodeList();
		List<Code> payTypeList = codeService.getList2("013");
		List<MemGrade> gradeList = memGradeService.getList();
		List<Order> list = orderService.getList(orderSearch);
		Integer count = orderService.getCount(orderSearch);
		ClinicPaging paging = new ClinicPaging(Utils.getUrl(), count, orderSearch);
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("statusList", statusList);
		mav.addObject("payTypeList", payTypeList);
		mav.addObject("gradeList", gradeList);
		mav.addObject("list", list);
		mav.addObject("count", count);
		mav.addObject("paging", paging);
		return mav;
	}
	
	@GetMapping("/edit")
	public ModelAndView edit(String orderid) {
		Order order = orderService.getOrderInfo(orderid);
		OrderAddr orderAddr = orderService.getOrderAddrInfo(orderid);
		
		List<OrderShip> shipList = orderService.getOrderShipList(orderid);
		for(OrderShip orderShip : shipList) {
			List<OrderItem> itemList = orderService.getOrderItemList(orderShip);
			orderShip.setItems(itemList);
		}
		
		OrderMemo orderMemo = orderService.getOrderMemoInfo(orderid);
		List<OrderItem> returnItemList = orderService.getReturnItemList(orderid);
		List<Code> mtel1List = codeService.getList2("011");
		List<Code> tel1List = codeService.getList2("012");
		List<Code> msgList = codeService.getList2("010");
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("retrivePath", Utils.retrivePath());
		mav.addObject("order", order);
		mav.addObject("orderMemo", orderMemo);
		mav.addObject("orderAddr", orderAddr);
		mav.addObject("shipList", shipList);
		mav.addObject("returnItemList", returnItemList);
		mav.addObject("mtel1List", mtel1List);
		mav.addObject("tel1List", tel1List);
		mav.addObject("msgList", msgList);
    	
    	if("013003".equals(order.getPayType())) {
			PaymentLog paymentLog = orderService.getVirtualAccountInfo(orderid);
			mav.addObject("paymentLog", paymentLog);
		}

		return mav;
	}

	@GetMapping("/redit")
	public ModelAndView redit(String orderid) {
		Order order = orderService.getOrderInfo(orderid);
		Order orgOrder = orderService.getOrderInfo(order.getOrgOrderid());
		OrderAddr orderAddr = orderService.getOrderAddrInfo(orderid);
		OrderRefund orderRefund = orderService.getRefundInfo(orderid);
		
		List<OrderShip> shipList = orderService.getOrderShipList(orderid);
		for(OrderShip orderShip : shipList) {
			List<OrderItem> itemList = orderService.getOrderItemList(orderShip);
			orderShip.setItems(itemList);
		}
		
		OrderMemo orderMemo = orderService.getOrderMemoInfo(orderid);
		List<OrderItem> orgItemList = orderService.getOrgItemList(order.getOrgOrderid());
		List<Code> mtel1List = codeService.getList2("011");
		List<Code> tel1List = codeService.getList2("012");
		List<Code> msgList = codeService.getList2("010");
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("retrivePath", Utils.retrivePath());
		mav.addObject("order", order);
		mav.addObject("orgOrder", orgOrder);
		mav.addObject("orderMemo", orderMemo);
		mav.addObject("orderAddr", orderAddr);
		mav.addObject("orderRefund", orderRefund);
		mav.addObject("shipList", shipList);
		mav.addObject("orgItemList", orgItemList);
		mav.addObject("mtel1List", mtel1List);
		mav.addObject("tel1List", tel1List);
		mav.addObject("msgList", msgList);
    	mav.addObject("bankOptions", TossService.getBankOptions(orderRefund == null ? "" : orderRefund.getBank()));
    	
    	if("013003".equals(order.getPayType())) {
			PaymentLog paymentLog = orderService.getVirtualAccountInfo(orderid);
			mav.addObject("paymentLog", paymentLog);
		}

    	if(order.getGubun() == 2) {
    		List<Code> reasonList = codeService.getList2("021");
    		mav.addObject("reasonList", reasonList);
    	} else {
    		List<Code> reasonList = codeService.getList2("021");
    		mav.addObject("reasonList", reasonList);
    	}
		return mav;
	}
	
	
    @GetMapping("/statusLog")
    public ModelAndView statusLog(String orderid) {
    	List<OrderStatusCode> list = orderService.getOrderStatusLogList(orderid);

    	ModelAndView mav = new ModelAndView();
    	mav.addObject("list", list);

    	return mav;
    }

	@GetMapping("/excel2")
	@ResponseBody
	public void excel2(@ModelAttribute OrderSearch orderSearch) {
		ClinicSession cs = ClinicSession.getInstance();
		orderSearch.setClinicMemNo(cs.getMemNo());
		orderSearch.setPageSize(Integer.MAX_VALUE);
		List<Order> list = orderService.getList(orderSearch);

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		workbook.setCompressTempFiles(true);
		SXSSFSheet sheet = workbook.createSheet();
		sheet.setRandomAccessWindowSize(100);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);

		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(HorizontalAlignment.CENTER);

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(HorizontalAlignment.RIGHT);

		CellStyle styleLeft= workbook.createCellStyle();
		styleLeft.setAlignment(HorizontalAlignment.LEFT);

		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle bodyStyleDate = workbook.createCellStyle();
		bodyStyleDate.setAlignment(HorizontalAlignment.CENTER);
		bodyStyleDate.setDataFormat(
			    createHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));

		Row headerRow = sheet.createRow(0);
		String[] headerTexts = { 
				"????????????", "?????????", "????????????", "????????????", "????????????", 
				"?????????", "??????ID", "?????????", "???????????????", "??????", 
				"???????????????", "????????????", "????????????", "?????????", "??????", 
				"?????????ID", "?????? ?????????", "?????? ?????? ??????", "????????????", "SAP??????", 
				"??????/??????", "?????????", "????????? ??????", "??????", "?????????", 
				"????????????", "?????? SAP??????", "SAP?????? * ??????", "??????????????????", "??????????????????", 
				"?????????", "???????????????", "??????????????? * ??????", "?????????/????????????", "??????/????????????", 
				"???????????????", "???????????????", "?????????", "???????????????", "?????? ??? ??????", 
				"???????????????", "?????? ?????? ?????? ?????????", "??? ???????????? ?????????", "???????????????", "????????????", 
				"????????????", "??????????????????", "??????????????????", "???????????????", "?????????????????????", 
				"???????????????", "?????????????????????", "???????????????", "????????????", "?????????", 
				"????????????", "???????????????", "????????????", "????????????", "????????????", 
				"?????????", "????????????", "?????? ?????? ??????", "?????? ?????? ??????", "????????????", 
				"???????????????", "?????? ??????", "?????? ?????? ??????", "?????? ?????? ??????", "?????? ?????? ??????"
			};
		
		int[] headerWidth = {
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000, 
				4000, 4000, 4000, 4000, 4000 
			};

		for (int i = 0; i < headerTexts.length; i++) {
			String text = headerTexts[i];

			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(text);

			sheet.setColumnWidth(i, headerWidth[i]);
		}

		Cell cell;
		int rowNo = 0;
		for(Order order : list) {
			Row row = sheet.createRow(++rowNo);

			OrderMemo orderMemo = orderService.getOrderMemoInfo(order.getOrderid());
			OrderAddr orderAddr = orderService.getOrderAddrInfo(order.getOrderid());
			
			cell = row.createCell(0);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getOrderid());	// ????????????

			cell = row.createCell(1);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(Utils.getTimeStampString(order.getOdate(), "yyyy.MM.dd HH:mm"));	// ?????????

			cell = row.createCell(2);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getGubunName());	// ????????????
			
			cell = row.createCell(3);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getOrderGubunName());	// ????????????
			
			cell = row.createCell(4);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getStatusName());	// ????????????

			cell = row.createCell(5);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getMemName());	// ?????????

			cell = row.createCell(6);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getMemId());	// ??????ID

			cell = row.createCell(7);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getEmail());	// ?????????

			cell = row.createCell(8);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getMtel1() + order.getMtel2());	// ???????????????

			cell = row.createCell(9);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getGradeName());	// ??????

			cell = row.createCell(10);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getClinicId());	// ???????????????
			
			cell = row.createCell(11);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getClinicName().replaceAll("&amp;", "&"));	// ????????????
			
			cell = row.createCell(12);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getDevice());	// ????????????

			cell = row.createCell(13);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getFirstOrderYn());	// ?????????

			cell = row.createCell(14);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(orderMemo != null ? orderMemo.getMemo() : "");	// ??????

			cell = row.createCell(15);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(orderMemo != null ? orderMemo.getAdminId() : "");	// ?????????ID

			cell = row.createCell(16);
			cell.setCellStyle(styleLeft);
			cell.setCellValue(order.getPname() + (order.getItemCount() > 1 ? " ??? " + (order.getItemCount() - 1) : ""));		// ?????? ?????????
			
			cell = row.createCell(17);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getItemCount());	// ?????? ?????? ??????
			
			cell = row.createCell(35);
			cell.setCellStyle(styleRight);
			cell.setCellValue(order.getAmt());	// ???????????????
			
			cell = row.createCell(36);
			cell.setCellStyle(styleRight);
			cell.setCellValue(order.getGradeDiscount());	// ???????????????
			
			cell = row.createCell(37);
			cell.setCellStyle(styleRight);
			cell.setCellValue(order.getShipAmt());	// ?????????
			
			cell = row.createCell(38);
			cell.setCellStyle(styleRight);
			cell.setCellValue(order.getShipDiscount());	// ???????????????
			
			cell = row.createCell(39);
			cell.setCellStyle(styleRight);
			cell.setCellValue(order.getCouponDiscount());	// ?????? ??? ??????
			
			cell = row.createCell(40);
			cell.setCellStyle(styleRight);
			cell.setCellValue(order.getPoint());	// ???????????????
			
			cell = row.createCell(42);
			cell.setCellStyle(styleRight);
			cell.setCellValue(order.getSumPoint() == null ? 0 : order.getSumPoint());	// ??? ???????????? ?????????
			
			cell = row.createCell(43);
			cell.setCellStyle(styleRight);
			cell.setCellValue(order.getPayAmt());	// ???????????????

			cell = row.createCell(44);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getPayTypeName());	// ????????????

			cell = row.createCell(45);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getOname());	// ????????????

			cell = row.createCell(46);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getOmtel1() + order.getOmtel2());	// ??????????????????

			cell = row.createCell(47);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(order.getOemail());	// ??????????????????

			cell = row.createCell(48);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(orderAddr.getSname());	// ???????????????
			
			cell = row.createCell(49);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(orderAddr.getSzip());	// ?????????????????????
			
			cell = row.createCell(50);
			cell.setCellStyle(styleLeft);
			cell.setCellValue(orderAddr.getSaddr1());	// ???????????????
			
			cell = row.createCell(51);
			cell.setCellStyle(styleLeft);
			cell.setCellValue(orderAddr.getSaddr2());	// ?????????????????????
			
			cell = row.createCell(52);
			cell.setCellStyle(styleCenter);	
			cell.setCellValue(orderAddr.getSmtel1() + orderAddr.getSmtel2());	// ???????????????
				
			cell = row.createCell(53);
			cell.setCellStyle(styleCenter);
			cell.setCellValue(orderAddr.getStel1() + orderAddr.getStel2());	// ????????????
			
			cell = row.createCell(54);
			cell.setCellStyle(styleLeft);
			cell.setCellValue(order.getPname() + (order.getItemCount() > 1 ? " ??? " + (order.getItemCount() - 1) : ""));	// ????????? 

			cell = row.createCell(56);
			cell.setCellStyle(styleLeft);
			cell.setCellValue(orderAddr.getMsg());	// ??????????????? 

			if(order.getGubun() == 3) {	// ?????? 
				OrderRefund orderRefund = orderService.getRefundInfo(order.getOrderid());

				cell = row.createCell(64);
				cell.setCellStyle(styleRight);
				cell.setCellValue(orderRefund.getAmt() == null ? 0 : orderRefund.getAmt());	// ????????????

				cell = row.createCell(65);
				cell.setCellStyle(styleRight);
				cell.setCellValue(orderRefund.getPoint() == null ? 0 : orderRefund.getPoint());	// ???????????????

				cell = row.createCell(65);
				cell.setCellStyle(styleCenter);
				cell.setCellValue(orderRefund.getAccount() == null ? "" : orderRefund.getAccount());	// ?????? ??????
			}
			
			List<OrderShip> shipList = orderService.getOrderShipList(order.getOrderid());
			for(int i = 0; i < shipList.size(); i++) {
				if(i > 0) row = sheet.createRow(++rowNo);
				
				OrderShip orderShip = shipList.get(i);
				
				cell = row.createCell(55);
				cell.setCellStyle(styleRight);
				cell.setCellValue(1);	// ????????????
				
				cell = row.createCell(57);
				cell.setCellStyle(styleRight);
				cell.setCellValue(1);	// ????????????
				
				cell = row.createCell(58);
				cell.setCellStyle(styleRight);
				cell.setCellValue(1);	// ????????????
				
				cell = row.createCell(59);
				cell.setCellStyle(styleRight);
				cell.setCellValue(3000);	// ????????????
				
				cell = row.createCell(60);
				cell.setCellStyle(styleCenter);
				cell.setCellValue(orderShip.getShipper());	// ?????????
				
				cell = row.createCell(61);
				cell.setCellStyle(styleCenter);
				cell.setCellValue(orderShip.getInvoice());	// ????????????
				
				cell = row.createCell(62);
				cell.setCellStyle(styleCenter);
				cell.setCellValue(Utils.formatDate(orderShip.getDate150(), "yyyy.MM.dd"));	// ?????? ?????? ??????

				cell = row.createCell(63);
				cell.setCellStyle(styleCenter);
				cell.setCellValue(Utils.formatDate(orderShip.getDate150(), "yyyy.MM.dd"));	// ?????? ?????? ??????
			
				List<OrderItem> itemList = orderService.getOrderItemList(orderShip);
				for(int j = 0; j < itemList.size(); j++) {
					if(j > 0) row = sheet.createRow(++rowNo);
					
					OrderItem orderItem = itemList.get(j);
					
					cell = row.createCell(18);
					cell.setCellStyle(styleCenter);
					cell.setCellValue(orderItem.getPno());	// ????????????

					cell = row.createCell(19);
					cell.setCellStyle(styleCenter);
					cell.setCellValue(orderItem.getMatnr());	// SAP??????

					cell = row.createCell(20);
					cell.setCellStyle(styleCenter);
					cell.setCellValue("Y".equals(orderItem.getColdYn()) ? "??????" : "??????");	// ??????/??????

					cell = row.createCell(21);
					cell.setCellStyle(styleLeft);
					cell.setCellValue(orderItem.getPname());	// ?????????

					cell = row.createCell(22);
					cell.setCellStyle(styleLeft);
					cell.setCellValue(orderItem.getGift());	// ????????? ??????

					cell = row.createCell(23);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getQty());	// ??????

					cell = row.createCell(24);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getSalePrice());	// ?????????

					cell = row.createCell(25);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getSalePrice() / orderItem.getQty());	// ????????????

					cell = row.createCell(26);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getNetpr());	// ?????? SAP??????

					cell = row.createCell(27);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getNetpr() * orderItem.getQty());	// SAP?????? * ??????

					cell = row.createCell(28);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getDiscount());	// ??????????????????

					cell = row.createCell(29);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getSalePrice() - orderItem.getMemPrice());	// ??????????????????

					cell = row.createCell(30);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getApplyPrice());	// ?????????

					cell = row.createCell(31);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getSupplyPrice());	// ???????????????

					cell = row.createCell(32);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getSupplyPrice() * orderItem.getQty());	// ??????????????? * ??????

					cell = row.createCell(33);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getReturnQty() + orderItem.getExchangeQty());	// ?????????/????????????

					if(order.getGubun() == 2 || order.getGubun() == 3) {
						cell = row.createCell(34);
						cell.setCellStyle(styleRight);
						cell.setCellValue(orderItem.getQty());	// ??????/????????????
					}
					
					cell = row.createCell(41);
					cell.setCellStyle(styleRight);
					cell.setCellValue(orderItem.getPoint());	// ?????? ?????? ?????? ?????????
					
					if("190,191".indexOf(order.getStatus()) != -1) {
						cell = row.createCell(67);
						cell.setCellStyle(styleRight);
						cell.setCellValue(orderItem.getQty());	// ?????? ?????? ??????
					}

					if(order.getGubun() == 2) {
						cell = row.createCell(68);
						cell.setCellStyle(styleRight);
						cell.setCellValue(orderItem.getQty());	// ?????? ?????? ??????
					}

					if(order.getGubun() == 3) {
						cell = row.createCell(69);
						cell.setCellStyle(styleRight);
						cell.setCellValue(orderItem.getQty());	// ?????? ?????? ??????
					}
					
					cell = row.createCell(10);
					cell.setCellStyle(styleCenter);
					cell.setCellValue(order.getClinicId());	// ???????????????
					
					cell = row.createCell(11);
					cell.setCellStyle(styleCenter);
					cell.setCellValue(order.getClinicName().replaceAll("&amp;", "&"));	// ????????????
				}
			}
		}

		HttpServletResponse response = HttpRequestHelper.getCurrentResponse();
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment;filename=order_" + Utils.getTimeStampString("yyyyMMdd") + ".xlsx");

		try {
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
