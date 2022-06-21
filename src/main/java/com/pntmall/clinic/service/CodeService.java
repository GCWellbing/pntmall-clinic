package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pntmall.clinic.domain.BankCard;
import com.pntmall.clinic.domain.Code;

@Service
public class CodeService {
    public static final Logger logger = LoggerFactory.getLogger(CodeService.class);

    @Autowired
    private SqlSessionTemplate sst;

    public List<Code> getList1() {
    	return sst.selectList("Code.list1");
    }

    public List<Code> getList2(String code1) {
    	return sst.selectList("Code.list2", code1);
    }

    public Code getInfo(Code code) {
    	return sst.selectOne("Code.info", code);
    }

    public List<BankCard> getBankCardList(BankCard bankCard) {
    	return sst.selectList("Code.bankCardList", bankCard);
    }
    
    public BankCard getBankCardInfo(BankCard bankCard) {
    	List<BankCard> list = getBankCardList(bankCard);
    	if(list.size() > 0) return list.get(0);
    	else return null;
    }
    
    public String getSapCd(BankCard bankCard) {
    	BankCard b = getBankCardInfo(bankCard);
    	if(b != null) return b.getSapCd();
    	else return null;
    }
}
