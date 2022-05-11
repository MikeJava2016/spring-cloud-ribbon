package com.flying.fish.formwork.dao;

import com.flying.fish.formwork.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description 客户端Dao数据层操作接口
 * @Author jianglong
 * @Date 2020/05/15
 * @Version V1.0
 */
public interface ClientDao extends JpaRepository<Client, String> {

}
