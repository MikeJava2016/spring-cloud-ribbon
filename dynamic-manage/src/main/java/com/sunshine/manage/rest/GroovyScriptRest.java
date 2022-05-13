package com.sunshine.manage.rest;

import com.sunshine.common.enums.StatusUpdateEnum;
import com.sunshine.formwork.base.BaseRest;
import com.sunshine.formwork.entity.GroovyScript;
import com.sunshine.formwork.service.CustomConfigService;
import com.sunshine.formwork.service.CustomNacosConfigService;
import com.sunshine.formwork.service.GroovyScriptService;
import com.sunshine.formwork.util.ApiResult;
import com.sunshine.formwork.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description 规则引擎动态脚本管理控制器
 * @Author JL
 * @Date 2022/2/22
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/groovyScript")
public class GroovyScriptRest extends BaseRest {

    @Resource
    private GroovyScriptService groovyScriptService;

    @Resource
    private CustomConfigService customNacosConfigService;

    /**
     * 添加动态脚本
     * @param groovyScript
     * @return
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public ApiResult add(@RequestBody GroovyScript groovyScript) throws Exception {
        Assert.notNull(groovyScript, "未获取到对象");
        groovyScript.setOrderNum(1);
        groovyScript.setStatus(Constants.NO);
        groovyScript.setCreateTime(new Date());
        this.validate(groovyScript);

        //查询最大orderNum
        int orderNum = groovyScriptService.findMaxOrderNum(groovyScript.getRouteId(), groovyScript.getEvent());
        groovyScript.setOrderNum(orderNum + 1);
        groovyScriptService.save(groovyScript);
        // 编译一下
        groovyScriptService.instance(groovyScript,true);
        //将ID推送到nacos注册发现与配置中心
        customNacosConfigService.publishGroovyScriptConfig(groovyScript.getId(), StatusUpdateEnum.INSERT);
        return new ApiResult();
    }

    /**
     * 获取已注册网关路由列表
     * @param routeId
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult list(@RequestParam String routeId) {
        Assert.isTrue(StringUtils.isNotBlank(routeId), "未获取到对象网关路由ID");
        GroovyScript groovyScript = new GroovyScript();
        groovyScript.setRouteId(routeId);
        return new ApiResult(groovyScriptService.list(groovyScript));
    }

    /**
     * 删除动态脚本
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult delete(@RequestParam Long id) {
        GroovyScript groovyScript = getGroovyScript(id);
        groovyScriptService.delete(groovyScript);
        if (Constants.YES.equals(groovyScript.getStatus())) {
            customNacosConfigService.publishGroovyScriptConfig(id,StatusUpdateEnum.DELETE);
        }
        return new ApiResult();
    }

    /**
     * 更新动态脚本
     * @param groovyScript
     * @return
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody GroovyScript groovyScript) throws Exception {
        Assert.notNull(groovyScript, "未获取到对象");
        Long id = groovyScript.getId();
        Assert.notNull(id, "未获取到对象ID");
        Assert.isTrue(id>0, "ID值错误");
        groovyScript.setUpdateTime(new Date());
        this.validate(groovyScript);
        // 编译一下
        groovyScriptService.instance(groovyScript,true);
        groovyScriptService.update(groovyScript);
        if (Constants.YES.equals(groovyScript.getStatus())) {
            customNacosConfigService.publishGroovyScriptConfig(id,StatusUpdateEnum.UPDATE);
        }
        return new ApiResult();
    }

    /**
     * 设置状态为启用
     * @param id
     * @return
     */
    @RequestMapping(value = "/start", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult start(@RequestParam Long id) {
        GroovyScript groovyScript = getGroovyScript(id);
        if (Constants.NO.equals(groovyScript.getStatus())) {
            groovyScript.setStatus(Constants.YES);
            groovyScript.setUpdateTime(new Date());
            groovyScriptService.update(groovyScript);
            customNacosConfigService.publishGroovyScriptConfig(id,StatusUpdateEnum.START);
        }
        return new ApiResult();
    }

    /**
     * 设置状态为禁用
     * @param id
     * @return
     */
    @RequestMapping(value = "/stop", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult stop(@RequestParam Long id) {
        GroovyScript groovyScript = getGroovyScript(id);
        if (Constants.YES.equals(groovyScript.getStatus())) {
            groovyScript.setStatus(Constants.NO);
            groovyScript.setUpdateTime(new Date());
            groovyScriptService.update(groovyScript);
            customNacosConfigService.publishGroovyScriptConfig(id,StatusUpdateEnum.STOP);
        }
        return new ApiResult();
    }

    /**
     * 上移（重新排序）
     * @param id
     * @return
     */
    @RequestMapping(value = "/up", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult up(@RequestParam Long id) {
        GroovyScript groovyScript = getGroovyScript(id);
        if (groovyScriptService.upOrderNum(groovyScript)){
            customNacosConfigService.publishGroovyScriptConfig(id,StatusUpdateEnum.UPDATE);
        }
        return new ApiResult();
    }

    /**
     * 下移（重新排序）
     * @param id
     * @return
     */
    @RequestMapping(value = "/down", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult down(@RequestParam Long id) {
        GroovyScript groovyScript = getGroovyScript(id);
        if (groovyScriptService.downOrderNum(groovyScript)){
            customNacosConfigService.publishGroovyScriptConfig(id,StatusUpdateEnum.UPDATE);
        }
        return new ApiResult();
    }

    /**
     * 获取指定ID对应的动态脚本数据库记录
     * @param id
     * @return
     */
    private GroovyScript getGroovyScript(Long id){
        Assert.notNull(id, "未获取到对象ID");
        Assert.isTrue(id>0, "ID值错误");
        GroovyScript groovyScript = groovyScriptService.findById(id);
        Assert.notNull(groovyScript, "未获取到对象");
        return groovyScript;
    }

}
