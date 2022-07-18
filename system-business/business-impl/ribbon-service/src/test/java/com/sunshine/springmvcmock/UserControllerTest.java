package com.sunshine.springmvcmock;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.sunshine.RibbonServiceApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 下午 03:48
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@EnableApolloConfig
@EnableFeignClients(basePackages = "com.sunshine")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages={"com.sunshine"},exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
@MapperScan("com.sunshine.mapper")
@SpringBootTest(classes= RibbonServiceApplication.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void selectOne() throws Exception {
        MvcResult authResult = null;
        authResult = mockMvc.perform(get("/api/workitem/equipmenttypes")//使用get方式来调用接口。
                        .contentType(MediaType.APPLICATION_XHTML_XML)//请求参数的类型
                        .param("sessionid", "ZlbpLxXw")//请求的参数（可多个）
                ).andExpect(status().isOk())
                .andReturn();
        //获取数据
        JSONObject jsonObject =new JSONObject(authResult.getResponse().getContentAsString());
        JSONArray jsonArrayData = (JSONArray)jsonObject.get("data");

        //获取第一个Array中的值,判断查询到的结果。
        JSONObject jsonObject_data = null;
        if(jsonArrayData.length()>0){
            jsonObject_data = (JSONObject) jsonArrayData.get(0);
        }
        //加断言，判断属性值的问题。
        Assert.assertNotNull(jsonObject.get("error_code"));
        Assert.assertEquals(jsonObject.get("error_code"),0);
        Assert.assertNotNull(jsonObject.get("error_msg"));
        Assert.assertEquals(jsonObject.get("error_msg"),"操作成功");
        Assert.assertNotNull(jsonObject.get("data"));
        Assert.assertNotNull(jsonObject_data);
        Assert.assertEquals(jsonObject_data.get("equipmentty"),1);
        Assert.assertEquals(jsonObject_data.get("equipmenttypename"),"xxxxx");

    }

}
