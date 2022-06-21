package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pntmall.clinic.domain.Notice;
import com.pntmall.clinic.domain.NoticeImg;
import com.pntmall.clinic.domain.NoticeSearch;

@Service
public class ClinicNoticeService {
    public static final Logger logger = LoggerFactory.getLogger(ClinicNoticeService.class);

    @Autowired
    private SqlSessionTemplate sst;

    public List<Notice> getList(NoticeSearch noticeSearch) {
    	return sst.selectList("ClinicNotice.list",noticeSearch);
    }

    public Integer getCount(NoticeSearch noticeSearch) {
    	return sst.selectOne("ClinicNotice.count",noticeSearch);
    }

    public Notice getInfo(Integer noticeNo) {
    	return sst.selectOne("ClinicNotice.info", noticeNo);
    }

    public List<NoticeImg> getNoticeImgList(Integer noticeNo) {
    	return sst.selectList("ClinicNotice.getClinicNoticeImgList",noticeNo);
    }

}
