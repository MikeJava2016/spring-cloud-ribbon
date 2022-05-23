package com.sunshine.formwork.dao;

import com.sunshine.formwork.entity.SecureIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description IP管理Dao数据层操作接口
 * @Author jianglong
 * @Date 2020/05/28
 * @Version V1.0
 */
public interface SecureIpDao extends JpaRepository<SecureIp, String>, JpaSpecificationExecutor<SecureIp> {
}
