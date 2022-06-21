package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pntmall.clinic.domain.Member;
import com.pntmall.clinic.domain.Qna;
import com.pntmall.clinic.domain.QnaImg;
import com.pntmall.clinic.domain.QnaSearch;

@Service
public class QnaService {
    public static final Logger logger = LoggerFactory.getLogger(QnaService.class);

    @Autowired
    private SqlSessionTemplate sst;

    public List<Qna> getList(QnaSearch qnaSearch) {
    	return sst.selectList("Qna.list",qnaSearch);
    }

    public Integer getCount(QnaSearch qnaSearch) {
    	return sst.selectOne("Qna.count",qnaSearch);
    }

    public Qna getInfo(QnaSearch qnaSearch) {
    	return sst.selectOne("Qna.info", qnaSearch);
    }

    public List<QnaImg> getQnaImgList(QnaSearch qnaSearch) {
    	return sst.selectList("Qna.getQnaImgList",qnaSearch);
    }

    public void modify(Qna qna) {
    	sst.update("Qna.update", qna);
    }

}
