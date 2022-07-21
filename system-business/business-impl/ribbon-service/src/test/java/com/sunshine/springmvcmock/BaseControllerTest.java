package com.sunshine.springmvcmock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 下午 07:17
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BaseControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build(); //初始化MockMvc对象
    }


    public String doPost(String url, String jsonObject) throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(url).content(jsonObject)
                .contentType(MediaType.APPLICATION_JSON));
        return doPrint(result);
    }

    public String doGet(String url, Map<String, String> param) throws Exception {
        MockHttpServletRequestBuilder mockBuilder = MockMvcRequestBuilders.get(url)    //请求url,请求的方式是get
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);  //请求的数据格式
        if (param != null && param.size() > 0) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                mockBuilder.param(entry.getKey(), entry.getValue());
            }
        }
        ResultActions result = mockMvc.perform(mockBuilder);
        return doPrint(result);
    }

    //处理返回结果
    public String doPrint(ResultActions result) throws Exception {
        String responseDate = result.andExpect(status().isOk())     //返回的状态是200
                .andDo(print())                //打印出请求和响应的内容
                .andReturn().getResponse().getContentAsString();   //将响应的数据转换为字符串
        System.out.println("responseDate=" + responseDate);
        return responseDate;
    }
}
