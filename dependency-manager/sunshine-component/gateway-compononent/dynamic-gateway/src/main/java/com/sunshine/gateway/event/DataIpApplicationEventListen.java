package com.sunshine.gateway.event;

import com.sunshine.formwork.service.SecureIpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 监听DataIpApplicationEvent事件，并触发IP鉴权数据重新加载（已过时）
 * @Author JL
 * @Date 2020/05/28
 * @Version V1.0
 */
@Slf4j
@Component
@Deprecated
public class DataIpApplicationEventListen{

    @Resource
    private SecureIpService secureIpService;

    /**
     * 监听事件刷新配置；
     * DataIpApplicationEvent发布后，即触发listenEvent事件方法；
     * （已过时，启用nacos配置监听事件，参见：NacosConfigRefreshEventListener）
     */
    @Deprecated
    @EventListener(classes = DataIpApplicationEvent.class)
    public void listenEvent() {
        // Todo 停止使用，请用InitSecureIpService类initLoadSecureIp()方法
        // initLoadSecureIp();
    }

    /**
     * 第一次初始化加载
     */
    @Deprecated
    //@PostConstruct
    public void initLoadSecureIp(){
        /*
        SecureIp secureIp = new SecureIp();
        //secureIp.setStatus(Constants.YES);
        List<SecureIp> list = secureIpService.findAll(secureIp);
        IpListCache.clear();
        int size = 0;
        if (!CollectionUtils.isEmpty(list)){
            size = list.size();
            list.forEach(s -> IpListCache.put(s.getIp(), s.getStatus().equals(Constants.YES)));
        }
        log.info("监听到IP配置发生变更，重新加载IP配置共{}条", size);
        */
    }

}
