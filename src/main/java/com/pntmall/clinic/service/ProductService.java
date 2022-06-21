package com.pntmall.clinic.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pntmall.clinic.domain.Product;
import com.pntmall.clinic.domain.ProductSearch;

@Service
public class ProductService {
    public static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private SqlSessionTemplate sst;

    public List<Product> getList(ProductSearch productSearch) {
    	return sst.selectList("Product.list", productSearch);
    }

    public Integer getCount(ProductSearch productSearch) {
    	return sst.selectOne("Product.count", productSearch);
    }

}
