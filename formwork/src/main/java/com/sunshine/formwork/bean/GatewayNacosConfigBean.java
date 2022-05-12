package com.sunshine.formwork.bean;

import com.sunshine.common.enums.StatusUpdateEnum;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description nacos中的配置属性
 * @Author JL
 * @Date 2021/09/17
 * @Version V1.0
 */
@Data
@ToString
public class GatewayNacosConfigBean implements java.io.Serializable {

    /**
     * 负载均衡ID
     */
    private String balancedId;
    /**
     * 网关路由ID
     */
    private String routeId;
    /**
     * 客户端注册网关路由的关联表ID
     */
    private Long regServerId;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端IP
     */
//    private String clientIp;
    /**
     * 黑|白名单IP
     */
    private String ip;
    /**
     * groovyScript规则引擎动态脚本ID
     */
    private Long groovyScriptId;
    /**
     * 创建时间戳
     */
    private Long createTime;



    private StatusUpdateEnum statusUpdateEnum;

    private String type;

}
