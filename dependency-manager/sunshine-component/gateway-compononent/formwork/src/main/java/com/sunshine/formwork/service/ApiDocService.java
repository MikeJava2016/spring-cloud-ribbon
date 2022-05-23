package com.sunshine.formwork.service;

import com.sunshine.formwork.base.BaseService;
import com.sunshine.formwork.dao.ApiDocDao;
import com.sunshine.formwork.entity.ApiDoc;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * @Description 网关路由API接口文档业务实现类
 * @Author JL
 * @Date 2020/11/25
 * @Version V1.0
 */
@Service
public class ApiDocService extends BaseService<ApiDoc, String, ApiDocDao> {
    private EntityManager entityManager;
}
