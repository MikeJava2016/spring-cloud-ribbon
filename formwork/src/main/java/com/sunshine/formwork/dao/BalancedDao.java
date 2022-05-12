package com.sunshine.formwork.dao;

import com.sunshine.formwork.entity.Balanced;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description 负载管理数据层操作接口
 * @Author jianglong
 * @Date 2020/06/28
 * @Version V1.0
 */
public interface BalancedDao extends JpaRepository<Balanced, Long>, JpaSpecificationExecutor<Balanced> {

}
