package com.sunshine.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.sunshine.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/7 10:24
 * https://www.cnblogs.com/fanshuyao/p/14607028.html
 **/
@Configuration
public class SunshineSentinelAutoConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(SunshineSentinelAutoConfiguration.class);

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    public SunshineSentinelAutoConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                             ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }


    // 配置限流的异常处理器
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }


    //　自定义限流异常页面
    @PostConstruct
    public void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = (serverWebExchange, throwable) -> {

            String msg  ;
            if (throwable instanceof FlowException) {
                msg = "访问频繁，请稍候再试";

            } else if (throwable instanceof DegradeException) {
                msg = "系统降级";

            } else if (throwable instanceof ParamFlowException) {
                msg = "热点参数限流";

            } else if (throwable instanceof SystemBlockException) {
                msg = "系统规则限流或降级";

            } else if (throwable instanceof AuthorityException) {
                msg = "授权规则不通过";

            } else {
                msg = "未知限流降级";
            }
            Result result = new Result<>().code(-1).message(msg);
            logger.info(throwable.getMessage());
            return ServerResponse.status(HttpStatus.OK).
                    contentType(MediaType.APPLICATION_JSON).
                    body(BodyInserters.fromObject(result));
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

}
