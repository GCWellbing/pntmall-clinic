package com.pntmall.clinic.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pntmall.clinic.domain.Reservation;
import com.pntmall.clinic.domain.ReservationProduct;
import com.pntmall.clinic.domain.ReservationSearch;
import com.pntmall.common.type.Param;

@Service
public class ReservationService {
    public static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private SqlSessionTemplate sst;

    @Autowired
    CartService cartService;

    public Integer getMemberCount(ReservationSearch reservationSearch) {
    	return sst.selectOne("Reservation.memberCount",reservationSearch);
    }

    public Integer getMemberTotalCount(ReservationSearch reservationSearch) {
    	return sst.selectOne("Reservation.memberTotalCount",reservationSearch);
    }

    public Integer getMemberRankRate(ReservationSearch reservationSearch) {
    	return sst.selectOne("Reservation.memberRankRate",reservationSearch);
    }

    public List<Param> getMemberChart(ReservationSearch reservationSearch) {
    	return sst.selectList("Reservation.memberChart",reservationSearch);
    }

    public List<Reservation> getList(ReservationSearch reservationSearch) {
    	return sst.selectList("Reservation.list",reservationSearch);
    }

    public Integer getCount(ReservationSearch reservationSearch) {
    	return sst.selectOne("Reservation.count",reservationSearch);
    }

    public Reservation getInfo(ReservationSearch reservationSearch) {
    	return sst.selectOne("Reservation.info", reservationSearch);
    }


    public List<ReservationProduct> getProductList(ReservationSearch reservationSearch) {
    	return sst.selectList("Reservation.productList", reservationSearch);
    }

    @Transactional
    public void modify(Reservation reservation) {
    	sst.update("Reservation.update", reservation);
    }

    @Transactional
    public void modifyMemo(Reservation reservation) {
    	sst.update("Reservation.updateMemo", reservation);
    }

    @Transactional
    public void complete(Reservation reservation, Param param, HttpServletRequest request) {
    	sst.update("Reservation.updateComplete", reservation);

    	// 전시 제품
    	sst.delete("Reservation.deleteProduct", reservation);
    	String[] pno = param.getValues("pno");
    	for(int i = 0; i < pno.length; i++) {
    		ReservationProduct reservationProduct = new ReservationProduct();
    		reservationProduct.setResNo(reservation.getResNo());
    		reservationProduct.setPno(Integer.parseInt(pno[i]));
    		sst.insert("Reservation.insertProduct", reservationProduct);
    	}

    	//카트담기
    	cartService.createDrPack(request);
    }

    public Integer getDoctorPackCount(Integer clinicMemNo) {
    	return sst.selectOne("Reservation.getDoctorPackCount",clinicMemNo);
    }

}
