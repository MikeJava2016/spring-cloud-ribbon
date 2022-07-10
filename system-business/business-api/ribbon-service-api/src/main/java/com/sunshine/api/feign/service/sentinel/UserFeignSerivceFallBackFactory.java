package com.sunshine.api.feign.service.sentinel;

import com.sunshine.api.feign.service.UserFeignSerivce;
import com.sunshine.common.base.Result;
import com.sunshine.entity.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignSerivceFallBackFactory implements FallbackFactory<UserFeignSentinelFallBackSerivce> {

    public UserFeignSerivceFallBackFactory(){
        logger.info("init UserFeignSerivceFallBackFactory ");
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserFeignSentinelFallBackSerivce create(Throwable throwable) {
        logger.error(throwable.getLocalizedMessage());
        throwable.printStackTrace();
        return new UserFeignSentinelFallBackSerivce(throwable);
    }

}


class UserFeignSentinelFallBackSerivce implements UserFeignSerivce{

    private final Logger logger = LoggerFactory.getLogger(UserFeignSentinelFallBackSerivce.class);

    private Throwable throwable;

    public UserFeignSentinelFallBackSerivce(Throwable throwable) {
        logger.info(throwable.getLocalizedMessage());
        this.throwable = throwable;
    }

    @Override
    public Result<User> getOne(Long id) {
        return Result.fail(throwable.getMessage());
    }

    @Override
    public Result<User> post(User user) {
        return Result.fail(throwable.getMessage());
    }

    @Override
    public Result<User> put(User user) {
        return Result.fail(throwable.getMessage());
    }

    @Override
    public Result<User> delete(Long id) {
        return Result.fail(throwable.getMessage());
    }

    @Override
    public Result<User> getInfoByUserName(String username) {
        return Result.fail(throwable.getMessage());
    }


}