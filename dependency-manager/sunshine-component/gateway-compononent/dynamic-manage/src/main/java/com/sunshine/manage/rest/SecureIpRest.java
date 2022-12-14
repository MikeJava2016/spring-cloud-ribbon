package com.sunshine.manage.rest;

import com.sunshine.common.enums.StatusUpdateEnum;
import com.sunshine.formwork.base.BaseRest;
import com.sunshine.formwork.bean.SecureIpReq;
import com.sunshine.formwork.entity.SecureIp;
import com.sunshine.formwork.service.CustomConfigService;
import com.sunshine.formwork.service.SecureIpService;
import com.sunshine.utils.ApiResult;
import com.sunshine.utils.RouteConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description Ip管理控制器
 * @Author JL
 * @Date 2020/05/28
 * @Version V1.0
 */
@RestController
@RequestMapping("/ip")
public class SecureIpRest extends BaseRest {

    @Resource
    private SecureIpService secureIpService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CustomConfigService customNacosConfigService;

    /**
     * 添加IP
     * @param secureIp
     * @return
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public ApiResult add(@RequestBody SecureIp secureIp) {
        Assert.notNull(secureIp, "未获取到对象");
        Assert.isTrue(StringUtils.isNotBlank(secureIp.getIp()), "IP值不能为空");
        secureIp.setCreateTime(new Date());
        this.validate(secureIp);
        //验证注册服务是否重复
        SecureIp qSecureIp = new SecureIp();
        qSecureIp.setIp(secureIp.getIp());
        long count = secureIpService.count(qSecureIp);
        Assert.isTrue(count <= 0, "该IP已添加，请不要重复添加");
        secureIpService.save(secureIp);
        //this.setIpCacheVersion();
        customNacosConfigService.publishIpConfig(secureIp.getIp(), StatusUpdateEnum.INSERT);
        return new ApiResult();
    }

    /**
     * 删除IP
     * @param ip
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult delete(@RequestParam String ip) {
        Assert.isTrue(StringUtils.isNotBlank(ip), "IP值不能为空");
        secureIpService.deleteById(ip);
        //this.setIpCacheVersion();
        customNacosConfigService.publishIpConfig(ip,StatusUpdateEnum.DELETE);
        return new ApiResult();
    }

    /**
     * 更新IP
     * @param secureIp
     * @return
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody SecureIp secureIp) {
        Assert.notNull(secureIp, "未获取到对象");
        Assert.isTrue(StringUtils.isNotBlank(secureIp.getIp()), "IP值不能为空");
        secureIp.setUpdateTime(new Date());
        this.validate(secureIp);
        secureIpService.update(secureIp);
        //this.setIpCacheVersion();
        customNacosConfigService.publishIpConfig(secureIp.getIp(),StatusUpdateEnum.UPDATE);
        return new ApiResult();
    }

    /**
     * 查询IP
     * @param ip
     * @return
     */
    @RequestMapping(value = "/findById", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult findById(@RequestParam String ip) {
        Assert.isTrue(StringUtils.isNotBlank(ip), "IP值不能为空");
        return new ApiResult(secureIpService.findById(ip));
    }

    /**
     * 分页查询
     * @param secureIpReq
     * @return
     */
    @RequestMapping(value = "/pageList", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult pageList(@RequestBody SecureIpReq secureIpReq){
        Assert.notNull(secureIpReq, "未获取到对象");
        int currentPage = getCurrentPage(secureIpReq.getCurrentPage());
        int pageSize = getPageSize(secureIpReq.getPageSize());
        SecureIp secureIp = new SecureIp();
        if (StringUtils.isNotBlank(secureIpReq.getIp())){
            secureIp.setIp(secureIpReq.getIp());
        }
        if (StringUtils.isNotBlank(secureIpReq.getStatus())){
            secureIp.setStatus(secureIpReq.getStatus());
        }
        return new ApiResult(secureIpService.pageList(secureIp,currentPage, pageSize));
    }

    /**
     * 对IP数据进行变更后，设置redis中缓存的版本号
     */
    @Deprecated
    private void setIpCacheVersion(){
        redisTemplate.opsForHash().put(RouteConstants.SYNC_VERSION_KEY, RouteConstants.IP, String.valueOf(System.currentTimeMillis()));
    }

}
