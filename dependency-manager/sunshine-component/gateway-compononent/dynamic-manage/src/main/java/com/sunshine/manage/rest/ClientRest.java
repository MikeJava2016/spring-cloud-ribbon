package com.sunshine.manage.rest;

import com.sunshine.common.enums.StatusUpdateEnum;
import com.sunshine.formwork.base.BaseRest;
import com.sunshine.formwork.bean.ClientReq;
import com.sunshine.formwork.entity.Client;
import com.sunshine.formwork.service.ClientService;
import com.sunshine.formwork.service.CustomConfigService;
import com.sunshine.utils.ApiResult;
import com.sunshine.utils.Constants;
import com.sunshine.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
 * @Description 客户端管理控制器类
 * @Author jianglong
 * @Date 2020/05/16
 * @Version V1.0
 */
@RestController
@RequestMapping("/client")
public class ClientRest extends BaseRest {

    @Resource
    private ClientService clientService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CustomConfigService customNacosConfigService;

    /**
     * 添加客户端
     * @param client
     * @return
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public ApiResult add(@RequestBody Client client) {
        Assert.notNull(client, "未获取到对象");
        client.setId(UUIDUtils.getUUIDString());
        client.setCreateTime(new Date());
        this.validate(client);
        //验证名称是否重复
        Client qClinet = new Client();
        qClinet.setName(client.getName());
        long count = clientService.count(qClinet);
        Assert.isTrue(count <= 0, "客户端名称已存在，不能重复");
        //保存
        clientService.save(client);
        customNacosConfigService.publishClientConfig(client.getId(), StatusUpdateEnum.INSERT);
        return new ApiResult();
    }

    /**
     * 删除客户端
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult delete(@RequestParam String id) {
        Assert.isTrue(StringUtils.isNotBlank(id), "未获取到对象ID");
        Client dbClient = clientService.findById(id);
        Assert.notNull(dbClient, "未获取到对象");
        clientService.delete(dbClient);
        customNacosConfigService.publishClientConfig(id,StatusUpdateEnum.DELETE);
        return new ApiResult();
    }

    /**
     * 更新客户端
     * @param client
     * @return
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Client client) {
        Assert.notNull(client, "未获取到对象");
        Assert.isTrue(StringUtils.isNotBlank(client.getId()), "未获取到对象ID");
        client.setUpdateTime(new Date());
        this.validate(client);
        clientService.update(client);
        customNacosConfigService.publishClientConfig(client.getId(),StatusUpdateEnum.UPDATE);
        return new ApiResult();
    }

    /**
     * 查询客户端
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult findById(@RequestParam String id) {
        Assert.isTrue(StringUtils.isNotBlank(id), "未获取到对象ID");
        return new ApiResult(clientService.findById(id));
    }

    /**
     * 分页查询客户端
     * @param clientReq
     * @return
     */
    @RequestMapping(value = "/pageList", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult pageList(@RequestBody ClientReq clientReq) {
        Client client = new Client();
        Integer reqCurrentPage = null;
        Integer reqPageSize = null;
        if (clientReq != null) {
            reqCurrentPage = clientReq.getCurrentPage();
            reqPageSize = clientReq.getPageSize();
            BeanUtils.copyProperties(clientReq, client);
            if (StringUtils.isBlank(client.getName())) {
                client.setName(null);
            }
            if (StringUtils.isBlank(client.getIp())) {
                client.setIp(null);
            }
            if (StringUtils.isBlank(client.getGroupCode())) {
                client.setGroupCode(null);
            }
            if (StringUtils.isBlank(client.getStatus())) {
                client.setStatus(null);
            }
        }
        int currentPage = getCurrentPage(reqCurrentPage);
        int pageSize = getPageSize(reqPageSize);
        return new ApiResult(clientService.pageList(client, currentPage, pageSize));
    }

    /**
     * 设置客户端状态为启用
     * @param id
     * @return
     */
    @RequestMapping(value = "/start", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult start(@RequestParam String id) {
        Assert.isTrue(StringUtils.isNotBlank(id), "未获取到对象ID");
        Client dbClient = clientService.findById(id);
        dbClient.setStatus(Constants.YES);
        clientService.update(dbClient);
        customNacosConfigService.publishClientConfig(id,StatusUpdateEnum.START);
        return new ApiResult();
    }

    /**
     * 设置客户端状态为禁用
     * @param id
     * @return
     */
    @RequestMapping(value = "/stop", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult stop(@RequestParam String id) {
        Assert.isTrue(StringUtils.isNotBlank(id), "未获取到对象ID");
        Client dbClient = clientService.findById(id);
        dbClient.setStatus(Constants.NO);
        clientService.update(dbClient);
        customNacosConfigService.publishClientConfig(id,StatusUpdateEnum.STOP);
        return new ApiResult();
    }

}
