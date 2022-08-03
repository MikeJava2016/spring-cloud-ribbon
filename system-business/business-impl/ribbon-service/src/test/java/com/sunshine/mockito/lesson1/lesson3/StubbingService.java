/*
package com.sunshine.mockito.lesson1.lesson3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 上午 10:30
 **//*

public class StubbingService {

    private final static Logger logger = LoggerFactory.getLogger(StubbingService.class);

    public int getI(int i) {
        logger.info(" StubbingService getI  i = {} 。", i);
        return i * 10;
    }

    //模拟远程调用
    public String getS(String cityId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://xxxxx.com.cn/city/id=" + cityId;
        logger.info(" StubbingService getS  cityId = {} 。", cityId);
        return restTemplate.getForObject(url, String.class);
    }
}
*/
