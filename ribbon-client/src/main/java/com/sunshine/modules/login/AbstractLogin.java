package com.sunshine.modules.login;

import com.sunshine.common.exception.BizException;
import com.sunshine.common.util.JwtUtils;
import com.sunshine.common.util.Result;
import com.sunshine.common.ao.LoginUserDto;
import com.sunshine.configuration.ApplicationContextUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 12:36
 **/
public abstract class AbstractLogin implements Login {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static ConcurrentHashMap<Integer, AbstractLogin> loginMap = new ConcurrentHashMap<>();

    private static final StringRedisTemplate redisTemplate = (StringRedisTemplate)ApplicationContextUtils.getBean(StringRedisTemplate.class);

    public AbstractLogin() {
        loginMap.put(getLoginType(), this);
    }

    public static Result validToken(String token) {
        String uid = redisTemplate.opsForValue().get(token);
        if (uid == null){
            throw new BizException("token失效了");
        }
        try {
            Claims claims = JwtUtils.parseToken(token);

        } catch (SignatureException e) {
            redisTemplate.delete(token);
            throw new BizException("签名校验失败");
        }
       // redisTemplate.expire(token,30 , TimeUnit.MINUTES);
        return Result.success();
    }

    public static Result<String> refreshToken(String token) {
        redisTemplate.expire(token,30 , TimeUnit.MINUTES);
        return Result.success();
    }

    @Override
    public Result doLogin(LoginUserDto loginUserDto) throws BizException {
        logger.info("begin AbstractLogin.doLogin:" + loginUserDto);
        validate(loginUserDto); //第一步完成验证
        Map<String, Object> map = this.doProcessor(loginUserDto);
        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("uid", map.get("id"));
        String token = JwtUtils.getToken(payLoad);

        redisTemplate.opsForValue().set(token,payLoad.get("uid").toString(),30, TimeUnit.MINUTES);
        return Result.success(token);
    }

    /**
     * 在子类中去声明自己的登录类型
     *
     * @return
     */
    public abstract int getLoginType();

    /**
     * 通过子类去完成验证
     *
     * @param authLoginDto
     */
    public abstract void validate(LoginUserDto authLoginDto);

    /**
     * 登录校验
     *
     * @param authLoginDto
     */
    public abstract Map doProcessor(LoginUserDto authLoginDto);


}
