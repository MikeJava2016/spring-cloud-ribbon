package com.sunshine.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.sunshine.common.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
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
//@Configuration
public class SunshineSentinelAutoConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(SunshineSentinelAutoConfiguration.class);

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    public SunshineSentinelAutoConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                             ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }


    // ??????????????????????????????
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }


    //??????????????????????????????
    @PostConstruct
    public void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = (serverWebExchange, throwable) -> {

            String msg  ;
            if (throwable instanceof FlowException) {
                msg = "??????????????????????????????";

            } else if (throwable instanceof DegradeException) {
                msg = "????????????";

            } else if (throwable instanceof ParamFlowException) {
                msg = "??????????????????";

            } else if (throwable instanceof SystemBlockException) {
                msg = "???????????????????????????";

            } else if (throwable instanceof AuthorityException) {
                msg = "?????????????????????";

            } else {
                msg = "??????????????????";
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
