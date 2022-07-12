package com.sunshine.mybatis.plus;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.extension.plugins.IllegalSQLInterceptor;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.sunshine.mybatis.plus.intercept.SM4Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class MybatisConfiguration {

    @Bean
    public Interceptor sqlExplainInterceptor(){
        log.info("Mybatis sqlExplainInterceptor starting...");
        return new SqlExplainInterceptor();
    }

    /**
     * 分页插件
     * @return
     */
    @Bean
    public Interceptor paginationInterceptor(){
        log.info("Mybatis PaginationInterceptor starting...");
        return new PaginationInterceptor();
    }

    @Bean
    public Interceptor interceptor2(){
        log.info("Mybatis OptimisticLockerInterceptor starting...");
        return new OptimisticLockerInterceptor();
    }

    @Bean
    public Interceptor interceptor4(){
        log.info("Mybatis IllegalSQLInterceptor starting...");
        return new IllegalSQLInterceptor();
    }

    @Bean
    public Interceptor SM4Interceptor(){
        log.info("Mybatis SM4Interceptor starting...");

        return new SM4Interceptor();
    }

    @Bean
    public MybatisBatchExecutor mybatisBatchExecutor(){
        log.info("Mybatis MybatisBatchExecutor starting...");
        return new MybatisBatchExecutor();
    }


    /**
     * 注册DruidServlet
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean druidServletRegistrationBean() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        // 白名单：
        //servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not
        // permitted to view this page.
        // 登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "root");
        servletRegistrationBean.addInitParameter("loginPassword", "root");
        // 是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    /**
     * 注册DruidFilter拦截
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean druidFilterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<String, String>();
        // 设置忽略请求
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
