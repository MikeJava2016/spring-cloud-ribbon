package com.sunshine.formwork.service;

import com.sunshine.formwork.base.BaseService;
import com.sunshine.formwork.dao.BalancedDao;
import com.sunshine.formwork.entity.Balanced;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description 负载管理业务实现类
 * @Author jianglong
 * @Date 2020/06/28
 * @Version V1.0
 */
@Service
public class BalancedService extends BaseService<Balanced, String, BalancedDao> {

    @Resource
    private LoadServerService loadServerService;

    /**
     * 删除负载以及注册到负载的路由服务
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Throwable.class})
    public void deleteAndServer(String id){
        loadServerService.deleteAllByBalancedId(id);
        this.deleteById(id);
    }
}
