package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pntmall.clinic.domain.MemGrade;

@Service
public class MemGradeService {
    public static final Logger logger = LoggerFactory.getLogger(MemGradeService.class);

    @Autowired
    private SqlSessionTemplate sst;

    public List<MemGrade> getList() {
    	return sst.selectList("MemGrade.list");
    }

    public MemGrade getInfo(Integer gradeNo) {
    	return sst.selectOne("MemGrade.info", gradeNo);
    }
}
