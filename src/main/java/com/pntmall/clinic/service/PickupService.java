package com.pntmall.clinic.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.pntmall.clinic.domain.Clinic;
import com.pntmall.clinic.domain.Order;
import com.pntmall.clinic.domain.OrderItem;
import com.pntmall.clinic.domain.OrderMemo;
import com.pntmall.clinic.domain.OrderRefund;
import com.pntmall.clinic.domain.OrderSearch;
import com.pntmall.clinic.domain.PaymentLog;
import com.pntmall.clinic.domain.Point;
import com.pntmall.common.directSend.DirectsendKakaoAllimApi;
import com.pntmall.common.directSend.SendKatalk;
import com.pntmall.common.payment.KakaopayService;
import com.pntmall.common.payment.NpayService;
import com.pntmall.common.payment.TossService;
import com.pntmall.common.type.Param;
import com.pntmall.common.utils.Utils;

@Service
public class PickupService {
    public static final Logger logger = LoggerFactory.getLogger(PickupService.class);

    @Value("${directSend.front.returnUrl}")
	String directSendFrontReturnUrl;

    @Value("${directSend.clinic.returnUrl}")
	String directSendClinicReturnUrl;

    @Autowired
    private SqlSessionTemplate sst;

    @Autowired
    DirectsendKakaoAllimApi directsendKakaoAllimApi;

    @Autowired
    ClinicService clinicService;

    @Autowired
    OrderService orderService;

    @Autowired
    private TossService tossService;

    @Autowired
    private KakaopayService kakaopayService;

    @Autowired
    private NpayService npayService;

    public List<Order> getList(OrderSearch orderSearch) {
    	return sst.selectList("Pickup.list", orderSearch);
    }

    public Integer getCount(OrderSearch orderSearch) {
    	return sst.selectOne("Pickup.count", orderSearch);
    }

    public Order getInfo(Order order) {
    	return sst.selectOne("Pickup.info", order);
    }

    public Order getInfo(String orderid, int clinicMemNo) {
    	Order order = new Order();
    	order.setOrderid(orderid);
    	order.setPickupClinic(clinicMemNo);
    	return getInfo(order);
    }

    public List<OrderItem> getItemList(String orderid) {
    	return sst.selectList("Pickup.itemList", orderid);
    }

    public void createMemo(OrderMemo orderMemo) {
    	sst.insert("Pickup.insertMemo", orderMemo);
    }

	@Transactional
    public void modifyStatus(Order order) {
    	sst.update("Pickup.updateStatus", order);
    	sst.insert("Pickup.insertStatusLog", order);

    	// 알림톡
    	try {
			Order _order = this.getInfo(order);
			Clinic _clinic = clinicService.getInfo(_order.getPickupClinic());
			List<OrderItem> itemList = this.getItemList(_order.getOrderid());

			if("420".equals(order.getStatus())) {	// 픽업가능
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 5);

				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(205);
				sendKatalk.setName(_order.getOname());
				sendKatalk.setMobile(_order.getOmtel1() + _order.getOmtel2());
				sendKatalk.setNote1(Utils.unSafeHTML(_clinic.getClinicName()));
				sendKatalk.setNote2(_clinic.getAddr1() + " " + _clinic.getAddr2());
				sendKatalk.setNote3(itemList.get(0).getPname() + (itemList.size() > 2 ? "외 " + (itemList.size() - 1) : ""));
				sendKatalk.setNote4(Utils.formatDate(cal.getTime(), "yyyy.MM.dd"));
				sendKatalk.setNote5(directSendFrontReturnUrl);
				directsendKakaoAllimApi.send(sendKatalk);
	    	} else if("430".equals(order.getStatus())) {	// 픽업완료
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(214);
				sendKatalk.setName(_order.getOname());
				sendKatalk.setMobile(_order.getOmtel1() + _order.getOmtel2());
				sendKatalk.setNote1(Utils.unSafeHTML(_clinic.getClinicName()));
				sendKatalk.setNote2(_order.getOrderid());
				sendKatalk.setNote3(directSendFrontReturnUrl);
				directsendKakaoAllimApi.send(sendKatalk);

				sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(211);
				sendKatalk.setName(Utils.unSafeHTML(_clinic.getClinicName()));
				sendKatalk.setMobile(_clinic.getAlarmTel1() + _clinic.getAlarmTel2());
				sendKatalk.setNote1(_order.getOname());
				sendKatalk.setNote2(_order.getOrderid());
				sendKatalk.setNote3(directSendClinicReturnUrl);
				directsendKakaoAllimApi.send(sendKatalk);
			} else if("440".equals(order.getStatus())) {	// 픽업미완료
				SendKatalk sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(226);
				sendKatalk.setName(_order.getOname());
				sendKatalk.setMobile(_order.getOmtel1() + _order.getOmtel2());
				sendKatalk.setNote1(Utils.unSafeHTML(_clinic.getClinicName()));
				sendKatalk.setNote2(_order.getOrderid());
				sendKatalk.setNote3(directSendFrontReturnUrl);
				directsendKakaoAllimApi.send(sendKatalk);

				sendKatalk = new SendKatalk();
				sendKatalk.setUserTemplateNo(223);
				sendKatalk.setName(Utils.unSafeHTML(_clinic.getClinicName()));
				sendKatalk.setMobile(_clinic.getAlarmTel1() + _clinic.getAlarmTel2());
				sendKatalk.setNote1(_order.getOname());
				sendKatalk.setNote2(_order.getOrderid());
				sendKatalk.setNote3(directSendClinicReturnUrl);
				directsendKakaoAllimApi.send(sendKatalk);
			}
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    	}
	}

	public Param getTodaySummary(Integer clinicMemNo) {
		return sst.selectOne("Pickup.todaySummary", clinicMemNo);
	}

	public Integer getCountTodayPickup420(Integer clinicMemNo) {
		return sst.selectOne("Pickup.countTodayPickup420", clinicMemNo);
	}

	public Integer getCountTodayPickup430(Integer clinicMemNo) {
		return sst.selectOne("Pickup.countTodayPickup430", clinicMemNo);
	}

    @SuppressWarnings("unchecked")
	@Transactional
    public void cancel(Order order, OrderRefund orderRefund) throws Exception {
    	String orgStatus = order.getStatus();

    	// 환불계좌 등록
    	if(!"".equals(orderRefund.getBank())) {
    		sst.update("Pickup.insertRefund", orderRefund);
    	}
    	
    	order.setStatus("190");
    	sst.update("Pickup.updateStatus", order);
    	sst.insert("Pickup.insertStatusLog", order);
    	
    	// 배송비 쿠폰 복원
    	if(StringUtils.isNotEmpty(order.getShipMcouponid())) {
    		sst.update("Coupon.refund", order.getShipMcouponid());
    	}
    	
    	// 상품쿠폰 복원
    	List<OrderItem> itemList = orderService.getOrderItemList(order);
    	for(OrderItem orderItem : itemList) {
    		if(StringUtils.isNotEmpty(orderItem.getMcouponid())) {
        		sst.update("Coupon.refund", orderItem.getMcouponid());
    		}
    	}
    	
    	// 포인트 환불
    	if(order.getPoint() > 0) {
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.YEAR, 1);
    		
    		Point point = sst.selectOne("Point.currentInfo", order.getMemNo());
    		Point p = new Point();
    		p.setMemNo(order.getMemNo());
    		p.setCurPoint(point.getCurPoint() + order.getPoint());
    		p.setPrevPoint(point.getCurPoint());
    		p.setPoint(order.getPoint());
    		p.setReason("019004");
    		p.setEdate(Utils.getTimeStampString(cal.getTime(), "yyyy.MM.dd"));
    		p.setUsePoint(0);
    		p.setBalance(order.getPoint());
    		p.setCuser(order.getCuser());
    		p.setOrderid(order.getOrderid());
    		sst.insert("Point.insert", p);
    	}
    	
    	// 결제취소
    	if("120".equals(orgStatus) && order.getPayAmt() > 0) {
    		String log = "";
    		PaymentLog paymentLog = orderService.getPaymentLog(order.getOrderid());
    		
    		if(paymentLog == null) {
    			throw new Exception("결제정보가 없습니다.");
    		}
    		
    		Gson gson = new Gson();
    		Param p = new Param(gson.fromJson(paymentLog.getLog(), Map.class));

    		if("013004".equals(order.getPayType())) {	// npay
				try {
					JSONObject logJson = (JSONObject) (new JSONParser()).parse(paymentLog.getLog());
					JSONObject body = (JSONObject) logJson.get("body");
					
					p = new Param();
					p.set("paymentId", body.get("paymentId"));
					p.set("cancelAmount", order.getPayAmt());
					p.set("cancelReason", "병의원관리자취소");
					p.set("cancelRequester", "2");	// 1:구매자 2:가맹점관리자
					p.set("taxScopeAmount", order.getPayAmt());
					p.set("taxExScopeAmount", 0);
					
					JSONObject json = npayService.cancel(p);
		    		int responseCode = (Integer) json.get("response_code");
		    		String code = (String) json.get("code");
				    if(responseCode == 200 && "Success".equals(code)) {
				    	log = json.toJSONString();
				    } else {
	                 	logger.error(order.getOrderid() + " : " + json.toJSONString());
						throw new RuntimeException("결제 취소중 오류가 발생했습니다.");
				    }
				} catch(Exception e) {
					logger.error(e.getMessage(), e);
					throw new RuntimeException("결제 취소중 오류가 발생했습니다.");
				}
    		} else if("013005".equals(order.getPayType())) {	// kakaopay
				p.set("cancel_amount", order.getPayAmt());
				p.set("cancel_tax_free_amount", 0);
				
				JSONObject json = kakaopayService.cancel(p);
	    		int responseCode = (Integer) json.get("response_code");
			    if(responseCode == 200) {
			    	log = json.toJSONString();
			    } else {
                 	logger.error(order.getOrderid() + " : " + json.toJSONString());
					throw new RuntimeException("결제 취소중 오류가 발생했습니다.");
			    }
    		} else {	// toss
    			// 환불계좌정보
    			Param bank = null;
    			if("013003".equals(order.getPayType()) && "120".equals(orgStatus)) {
	    			bank = new Param();
	    			bank.set("bank", orderRefund.getBank());
	    			bank.set("accountNumber", orderRefund.getAccount());
	    			bank.set("holderName", orderRefund.getDepositor());
    			}
    			
    			Param result = tossService.cancel(order.getOrderGubun(), p.get("paymentKey"), "관리자 취소", order.getPayAmt(), bank);
    			
    			if("".equals(result.get("code"))) {
    				log = result.get("message");
    			} else {
    				throw new Exception("Toss 결제 취소 오류가 발생했습니다.");
    			}
    		}
    		
    		PaymentLog plog = new PaymentLog();
    		plog.setOrderid(order.getOrderid());
    		plog.setGubun(2);
    		plog.setPayType(order.getPayType());
    		plog.setPayAmt(order.getPayAmt());
    		plog.setLog(log);
    		sst.insert("Order.insertPaymentLog", plog);
    		
    	}
    	
   		try {
			SendKatalk sendKatalk = new SendKatalk();
			sendKatalk.setUserTemplateNo(118);
			sendKatalk.setName(order.getOname());
			sendKatalk.setMobile(order.getOmtel1() + order.getOmtel2());
			sendKatalk.setNote1(order.getOrderid());
			sendKatalk.setNote2(Utils.formatMoney(order.getPayAmt()));
			sendKatalk.setNote3(directSendFrontReturnUrl);
			directsendKakaoAllimApi.send(sendKatalk);
   		} catch(Throwable e) {
   			logger.error(e.getMessage(), e);
   		}
    	
    }

}
