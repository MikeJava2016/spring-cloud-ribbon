package com.sunshine.security.mapper;

import com.sunshine.security.GpVipSecurity1011Application;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpVipSecurity1011Application.class)
class RoleMapperTest {
    @Resource
    RoleMapper roleMapper;
    @Test
    public  void testMasterSlave(){

        roleMapper.selectById(1);
    }

    @Test
    public void findAll(){

    }
}