package com.pntmall.clinic.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pntmall.clinic.domain.Job;
import com.pntmall.common.type.Param;

@Service
public class JobService {
    public static final Logger logger = LoggerFactory.getLogger(JobService.class);

    public Param getList(Job job) {
    	Param param = new Param();
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	
    	try {
    		String url = "https://www.medijob.cc/gcwb_resume?page=" + job.getPage() + "&rows=" + job.getPageSize();
    		logger.debug("url --------- " + url);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(url);
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("TotalCnt");
//			logger.debug("TotalCnt : " + nodeList.item(0).getNodeName() + ", " + nodeList.item(0).getFirstChild().getNodeValue());
			logger.debug("TotalCnt : " + nodeList.item(0).getNodeName() + ", " + nodeList.item(0).getTextContent());
			
			Integer count = Integer.parseInt(nodeList.item(0).getTextContent());
			nodeList = root.getElementsByTagName("Items");
			List<Job> list = new ArrayList<Job>();
			
			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				NodeList items = node.getChildNodes();
				String area1 = "";
				String area2 = "";
				String area3 = "";
				Job _job = new Job();
				
				for(int j = 0; j < items.getLength(); j++) {
					Node item = items.item(j);
					if("Gl_URL".equals(item.getNodeName())) {
						_job.setUrl(item.getTextContent());
					} else if("Gl_No".equals(item.getNodeName())) {
						_job.setNo(item.getTextContent());
					} else if("C_Name".equals(item.getNodeName())) {
						_job.setName(item.getTextContent());
					} else if("C_Gender".equals(item.getNodeName())) {
						_job.setGender(item.getTextContent());
					} else if("C_Birth_Day".equals(item.getNodeName())) {
						int m = Integer.parseInt(item.getTextContent().substring(0, 4));
						_job.setAge(year - m);
					} else if("Gl_Title".equals(item.getNodeName())) {
						_job.setTitle(item.getTextContent());
					} else if("Gl_Area_Code".equals(item.getNodeName())) {
						area1 = item.getTextContent();
					} else if("Gl_Area_Detail_code".equals(item.getNodeName())) {
						area1 += " " + item.getTextContent();
					} else if("Gl_Area_Code2".equals(item.getNodeName())) {
						area2 = item.getTextContent();
					} else if("Gl_Area_Detail_code2".equals(item.getNodeName())) {
						area2 += " " + item.getTextContent();
					} else if("Gl_Area_Code3".equals(item.getNodeName())) {
						area3 = item.getTextContent();
					} else if("Gl_Area_Detail_code3".equals(item.getNodeName())) {
						area3 += " " + item.getTextContent();
					} else if("Gl_Regist_Date".equals(item.getNodeName())) {
						String s = item.getTextContent();
						_job.setRegistDate(s.substring(0, 4) + "." + s.substring(4, 6) + "." + s.substring(6, 8));
					} else if("Gl_Career_Day".equals(item.getNodeName())) {
						int m = Integer.parseInt(item.getTextContent());
						_job.setCareer(m == 0 ? "-" : (m > 12 ? (m / 12) + "년 " + (m % 12) + " 개월" : (m % 14) + " 개월"));
					}
					
					_job.setArea(area1 + ("".equals(area2.trim()) ? "" : ", " + area2) + ("".equals(area3.trim()) ? "" : ", " + area3));
					
				}

				logger.debug(i + " : " +_job.toString());
				list.add(_job);
			}
			
			param.set("count", count);
			param.set("list", list);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
    	
    	return param;
    }

}
