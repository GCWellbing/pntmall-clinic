package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pntmall.clinic.domain.ClinicProduct;
import com.pntmall.clinic.etc.ClinicSession;
import com.pntmall.common.type.Param;

@Service
public class ClinicProductService {
    public static final Logger logger = LoggerFactory.getLogger(ClinicProductService.class);

    @Autowired
    private SqlSessionTemplate sst;

    public List<ClinicProduct> getList(int memNo) {
    	return sst.selectList("ClinicProduct.list", memNo);
    }
    
    @Transactional
    public void create(Param param) {
		ClinicSession sess = ClinicSession.getInstance();
		String[] pnos = param.getValues("pno");
		
		for(String pno : pnos) {
			ClinicProduct clinicProduct = new ClinicProduct();
			clinicProduct.setMemNo(sess.getMemNo());
			clinicProduct.setPno(Integer.parseInt(pno));
			
			sst.insert("ClinicProduct.insert", clinicProduct);
		}
    }
    
    public void remove(ClinicProduct clinicProduct) {
    	sst.update("ClinicProduct.delete", clinicProduct);
    }
    
    public List<ClinicProduct> getProductList(ClinicProduct clinicProduct) {
    	return sst.selectList("ClinicProduct.productList", clinicProduct);
    }

    public Integer getProductCount(ClinicProduct clinicProduct) {
    	return sst.selectOne("ClinicProduct.productCount", clinicProduct);
    }
}
