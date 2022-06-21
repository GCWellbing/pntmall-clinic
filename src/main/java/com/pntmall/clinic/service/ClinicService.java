package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pntmall.clinic.domain.Admin;
import com.pntmall.clinic.domain.AgreeDocu;
import com.pntmall.clinic.domain.Clinic;
import com.pntmall.clinic.domain.ClinicImg;
import com.pntmall.clinic.domain.ClinicJoin;
import com.pntmall.clinic.domain.ClinicSap;
import com.pntmall.clinic.domain.ClinicSearch;
import com.pntmall.clinic.domain.Member;
import com.pntmall.clinic.domain.MemberLog;
import com.pntmall.clinic.domain.Stipulation;
import com.pntmall.common.type.Param;

@Service
public class ClinicService {
    public static final Logger logger = LoggerFactory.getLogger(ClinicService.class);

    @Autowired
    private SqlSessionTemplate sst;


    public List<Clinic> getList(ClinicSearch clinicSearch) {
    	return sst.selectList("Clinic.list",clinicSearch);
    }

    public boolean isExistsBusinessNo(ClinicSearch clinicSearch) {
        return getInfo(clinicSearch) != null;
    }

    public ClinicSap infoClinicSap(ClinicSearch clinicSearch) {
    	return sst.selectOne("Clinic.infoClinicSap", clinicSearch);
    }

    public boolean isExists(ClinicSearch clinicSearch) {
        return sst.selectOne("Clinic.isExists", clinicSearch) != null;
    }

    @Transactional
    public void create(Clinic clinic, Param param) {
    	sst.insert("Clinic.insertMember", clinic);
    	sst.update("Clinic.updateMemberMyClinic", clinic);
    	sst.insert("Clinic.insertClinic", clinic);

    	Integer memNo = clinic.getMemNo();

    	//병의원 상태
    	ClinicJoin clinicJoin = new ClinicJoin();
    	clinicJoin.setMemNo(memNo);
    	clinicJoin.setApprove("006001");
    	clinicJoin.setReason("신규회원가입");
    	clinicJoin.setCuser(memNo);
    	sst.insert("Clinic.insertJoin", clinicJoin);

    	// 이미지
    	String[] gubun = param.getValues("gubun");
    	String[] attach = param.getValues("attach");
    	String[] attachOrgName = param.getValues("attachOrgName");

    	for(int i = 0; i < gubun.length; i++) {
    		ClinicImg clinicImg = new ClinicImg();
    		clinicImg.setMemNo(memNo);
    		clinicImg.setGubun(gubun[i]);
    		clinicImg.setAttach(attach[i]);
    		clinicImg.setAttachOrgName(attachOrgName[i]);
    		clinicImg.setCuser(clinic.getCuser());
    		sst.insert("Clinic.insertClinicImg", clinicImg);
    	}

    }

    public Clinic getInfo(ClinicSearch clinicSearch) {
    	return sst.selectOne("Clinic.info", clinicSearch);
    }

    public Clinic getInfo(Integer memNo) {
    	ClinicSearch clinicSearch = new ClinicSearch();
    	clinicSearch.setMemNo(memNo);

    	return getInfo(clinicSearch);
    }

    public List<ClinicImg> getClinicImgList(ClinicSearch clinicSearch) {
    	return sst.selectList("Clinic.getClinicImgList",clinicSearch);
    }

    public List<ClinicJoin> getClinicJoinList(ClinicSearch clinicSearch) {
    	return sst.selectList("Clinic.getClinicJoinList",clinicSearch);
    }


    public void createAccessLog(MemberLog memberLog) {
    	sst.insert("Clinic.insertLog", memberLog);
    }

    public void modifyFailCnt(Clinic clinic) {
    	sst.update("Clinic.updateFailCnt", clinic);
    }

    @Transactional
    public void modify(Clinic clinic, Param param) {
    	sst.update("Clinic.update", clinic);
    	sst.update("Clinic.updateClinic", clinic);

    	Integer memNo = clinic.getMemNo();

    	//병의원 상태 : 가입반려, 비활성 상태에서만 신규 회원 가입으로 변경
    	if("006003".equals(clinic.getApprove()) || "006007".equals(clinic.getApprove()) ){
	    	ClinicJoin clinicJoin = new ClinicJoin();
	    	clinicJoin.setMemNo(memNo);
	    	clinicJoin.setApprove("006001");
	    	clinicJoin.setReason("신규회원가입");
	    	clinicJoin.setCuser(memNo);
	    	sst.insert("Clinic.insertJoin", clinicJoin);
    	}

    	// 이미지
    	sst.delete("Clinic.deleteClinicImgInfo", memNo);
    	String[] gubun = param.getValues("gubun");
    	String[] attach = param.getValues("attach");
    	String[] attachOrgName = param.getValues("attachOrgName");

    	for(int i = 0; i < gubun.length; i++) {
    		ClinicImg clinicImg = new ClinicImg();
    		clinicImg.setMemNo(memNo);
    		clinicImg.setGubun(gubun[i]);
    		clinicImg.setAttach(attach[i]);
    		clinicImg.setAttachOrgName(attachOrgName[i]);
    		clinicImg.setCuser(clinic.getCuser());
    		sst.insert("Clinic.insertClinicImg", clinicImg);
    	}

    }

    @Transactional
    public void calcuModify(Clinic clinic, Param param) {
    	sst.update("Clinic.updateCalcu", clinic);

    	//병의원 상태 승인,업데이트 요청,업데이트 반려,  상태에서만 업데이트 요청으로 상태가 변경된다.
    	if("006002".equals(clinic.getApprove()) || "006005".equals(clinic.getApprove()) || "006006".equals(clinic.getApprove()) ){
	    	ClinicJoin clinicJoin = new ClinicJoin();
	    	clinicJoin.setMemNo(clinic.getMemNo());
	    	clinicJoin.setApprove("006005");
	    	clinicJoin.setReason("정산정보변경");
	    	clinicJoin.setCuser(clinic.getMemNo());
	    	sst.insert("Clinic.insertJoin", clinicJoin);
    	}else if("006003".equals(clinic.getApprove()) || "006007".equals(clinic.getApprove()) ){
	    	ClinicJoin clinicJoin = new ClinicJoin();
	    	clinicJoin.setMemNo(clinic.getMemNo());
	    	clinicJoin.setApprove("006001");
	    	clinicJoin.setReason("신규회원가입");
	    	clinicJoin.setCuser(clinic.getMemNo());
	    	sst.insert("Clinic.insertJoin", clinicJoin);
    	}

    	Integer memNo = clinic.getMemNo();

    	// 이미지
    	sst.delete("Clinic.deleteClinicImgDocu", memNo);
    	String[] gubun = param.getValues("gubun");
    	String[] attach = param.getValues("attach");
    	String[] attachOrgName = param.getValues("attachOrgName");

    	for(int i = 0; i < gubun.length; i++) {
    		ClinicImg clinicImg = new ClinicImg();
    		clinicImg.setMemNo(memNo);
    		clinicImg.setGubun(gubun[i]);
    		clinicImg.setAttach(attach[i]);
    		clinicImg.setAttachOrgName(attachOrgName[i]);
    		clinicImg.setCuser(clinic.getCuser());
    		sst.insert("Clinic.insertClinicImg", clinicImg);
    	}

    }

    public Stipulation getStipulation(Integer gubun) {
    	return sst.selectOne("Clinic.getStipulation", gubun);
    }

    public Stipulation getSensitive(Integer gubun) {
    	return sst.selectOne("Clinic.getSensitive", gubun);
    }

    public Stipulation getPrivacy(Integer gubun) {
    	return sst.selectOne("Clinic.getPrivacy", gubun);
    }

    @Transactional
    public void setPasswd(Clinic clinic) {
    	sst.update("Clinic.setPasswd", clinic);
    	clinic.setLoginFailCnt(0);
    	sst.update("Clinic.updateFailCnt", clinic);
    }


    public AgreeDocu getAgreeDocuInfo() {
    	return sst.selectOne("Clinic.AgreeDocuInfo");
    }

    public Member infoMem(Integer memNo) {
    	return sst.selectOne("Clinic.infoMem", memNo);
    }

    public Admin getAdminInfo(String adminId) {
    	return sst.selectOne("Clinic.getAdminInfo", adminId);
    }
}
