package com.sunshine.manage.rest;

import com.sunshine.formwork.base.BaseRest;
import com.sunshine.formwork.bean.MonitorReq;
import com.sunshine.formwork.entity.Monitor;
import com.sunshine.formwork.service.MonitorService;
import com.sunshine.formwork.util.ApiResult;
import com.sunshine.formwork.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description 接口监控
 * @Author JL
 * @Date 2021/04/14
 * @Version V1.0
 */
@RestController
@RequestMapping("/monitor")
public class MonitorRest extends BaseRest {

    @Resource
    private MonitorService monitorService;

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult list(@RequestBody MonitorReq monitorReq){
        return new ApiResult(monitorService.list(monitorReq));
    }

    /**
     * 关闭本次告警，状态置为0正常
     * @param id
     * @return
     */
    @RequestMapping(value = "/close", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult close(@RequestParam String id){
        Assert.isTrue(StringUtils.isNotBlank(id), "未获取到对象ID");
        Monitor monitor = monitorService.findById(id);
        Assert.notNull(monitor, "未获取到对象");
        monitor.setStatus(Constants.YES);
        monitorService.update(monitor);
        return new ApiResult();
    }

}
