package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pntmall.clinic.domain.ClinicAdjust;
import com.pntmall.clinic.domain.ClinicAdjustDetail;
import com.pntmall.clinic.domain.ClinicAdjustSearch;

@Service
public class ClinicAdjustService {
    public static final Logger logger = LoggerFactory.getLogger(ClinicAdjustService.class);

    @Autowired
    private SqlSessionTemplate sst;

    public List<ClinicAdjust> getList(ClinicAdjustSearch clinicAdjustSearch) {
    	return sst.selectList("ClinicAdjust.list", clinicAdjustSearch);
    }

    public Integer getCount(ClinicAdjustSearch clinicAdjustSearch) {
    	return sst.selectOne("ClinicAdjust.count", clinicAdjustSearch);
    }

    public ClinicAdjust getInfo(ClinicAdjustSearch clinicAdjustSearch) {
    	return sst.selectOne("ClinicAdjust.info", clinicAdjustSearch);
    }

    @Transactional
    public void modifyStatus(ClinicAdjust clinicAdjust) {
		sst.update("ClinicAdjust.updateStatus", clinicAdjust);
		sst.update("ClinicAdjust.insertStatusLog", clinicAdjust);
    }
    
    public List<ClinicAdjustDetail> getDetailList(ClinicAdjust clinicAdjust) {
    	return sst.selectList("ClinicAdjust.detailList", clinicAdjust);
    }
}
