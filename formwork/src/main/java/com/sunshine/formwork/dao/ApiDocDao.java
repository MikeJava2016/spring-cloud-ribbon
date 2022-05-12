package com.sunshine.formwork.dao;

import com.sunshine.formwork.entity.ApiDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description 网关路由API接口文档数据层操作接口
 * @Author JL
 * @Date 2020/11/25
 * @Version V1.0
 */
public interface ApiDocDao extends JpaRepository<ApiDoc, String>, JpaSpecificationExecutor<ApiDoc> {

}
