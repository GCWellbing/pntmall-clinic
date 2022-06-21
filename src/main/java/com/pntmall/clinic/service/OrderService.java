package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pntmall.clinic.domain.Order;
import com.pntmall.clinic.domain.OrderAddr;
import com.pntmall.clinic.domain.OrderGift;
import com.pntmall.clinic.domain.OrderItem;
import com.pntmall.clinic.domain.OrderMemo;
import com.pntmall.clinic.domain.OrderRefund;
import com.pntmall.clinic.domain.OrderSearch;
import com.pntmall.clinic.domain.OrderShip;
import com.pntmall.clinic.domain.OrderStatusCode;
import com.pntmall.clinic.domain.PaymentLog;
import com.pntmall.common.directSend.DirectsendKakaoAllimApi;
import com.pntmall.common.type.Param;

@Service
public class OrderService {
    public static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private SqlSessionTemplate sst;
    
    @Autowired
    DirectsendKakaoAllimApi directsendKakaoAllimApi;

    public List<Order> getList(OrderSearch orderSearch) {
    	return sst.selectList("Order.list", orderSearch);
    }
    
    public Integer getCount(OrderSearch orderSearch) {
    	return sst.selectOne("Order.count", orderSearch);
    }
    
    public List<OrderStatusCode> getOrderStatusCodeList() {
    	return sst.selectList("Order.statusList");
    }
    
    public Order getOrderInfo(String orderid) {
    	return sst.selectOne("Order.orderInfo", orderid);
    }

    public OrderAddr getOrderAddrInfo(String orderid) {
    	return sst.selectOne("Order.addrInfo", orderid);
    }

    public OrderMemo getOrderMemoInfo(String orderid) {
    	return sst.selectOne("Order.memoInfo", orderid);
    }

    public List<OrderShip> getOrderShipList(String orderid) {
    	return sst.selectList("Order.shipList", orderid);
    }
    
    public List<OrderItem> getOrderItemList(Order order) {
    	OrderShip orderShip = new OrderShip();
    	orderShip.setOrderid(order.getOrderid());
    	return getOrderItemList(orderShip);
    }
    
    public List<OrderItem> getOrderItemList(OrderShip orderShip) {
    	return sst.selectList("Order.itemList", orderShip);
    }
    
    public List<OrderStatusCode> getOrderStatusLogList(String orderid) {
    	return sst.selectList("Order.statusLogList", orderid);
    }
    
    public PaymentLog getPaymentLog(String orderid) {
    	PaymentLog paymentLog = new PaymentLog();
    	paymentLog.setOrderid(orderid);
    	paymentLog.setGubun(1);
    	
    	return  sst.selectOne("Order.paymentLog", paymentLog);
    }
    
    public PaymentLog getVirtualAccountInfo(String orderid) {
    	PaymentLog paymentLog = getPaymentLog(orderid);

    	try {
    		if(paymentLog != null) {
	        	ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(paymentLog.getLog());
				
				paymentLog.setBank(node.get("virtualAccount").get("bank").asText());
				paymentLog.setAccountNumber(node.get("virtualAccount").get("accountNumber").asText());
    		}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    	
    	return paymentLog;
    }
    
    public List<OrderItem> getReturnItemList(String orderid) {
    	return sst.selectList("Order.returnItemList", orderid);
    }

    public List<OrderItem> getOrgItemList(String orgOrderid) {
    	return sst.selectList("Order.orgItemList", orgOrderid);
    }
    
    public OrderRefund getRefundInfo(String orderid) {
    	return sst.selectOne("Order.refundInfo", orderid);
    }
    

	public OrderItem getItemInfo(Integer itemNo) {
		return sst.selectOne("Order.itemInfo", itemNo);
	}

	public List<OrderGift> getGiftList(Integer itemNo) {
		return sst.selectList("Order.giftList", itemNo);
	}
	
	public List<Order> getListForXml() {
		return sst.selectList("Order.listForXml");
	}
	
    public PaymentLog getPaymentLogInfoByNo(int plogNo) {
    	return sst.selectOne("Order.paymemtLogInfoByNo", plogNo);
    }

	public Param getTotalOrderInfo(Integer memNo) {
		return sst.selectOne("Order.totOrderInfo", memNo);
	}

	public Param getSaleRank(Integer memNo) {
		return sst.selectOne("Order.saleRank", memNo);
	}

	public Param getSaleRank2(Param param) {
		return sst.selectOne("Order.saleRank2", param);
	}
	
	public List<Param> getSaleRank3(Param param) {
		return sst.selectList("Order.saleRank3", param);
	}
}
