package com.sunshine.mybatis.plus.com.sunshine.mybatis.utils;

import com.sunshine.utils.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.CompositeTransactionAttributeSource;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableTransactionManagement
@Aspect
@Configuration
@Slf4j
public class GlobalTransactionConfig {
    //写事务的超时时间为10秒
    private static final int TX_METHOD_TIMEOUT = 10;

    //restful包下所有service包或者service的子包的任意类的任意方法
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.sunshine.security.service.*.*(..))";

    @Autowired
    private TransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {

        /**
         * 这里配置只读事务
         */
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        readOnlyTx.setDescriptor("【sunshine 事务 只读】");
        /**
         * 必须带事务
         * 当前存在事务就使用当前事务，当前不存在事务,就开启一个新的事务
         */
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
        rollbackRules.add( new RollbackRuleAttribute(BaseException.class));
        rollbackRules.add( new RollbackRuleAttribute(Exception.class));
        //检查型异常也回滚
        requiredTx.setRollbackRules(rollbackRules);
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setTimeout(TX_METHOD_TIMEOUT);
//        requiredTx.setIsolationLevel();
        requiredTx.setDescriptor("【sunshine 事务 写】");
        /***
         * 无事务地执行，挂起任何存在的事务
         */
        RuleBasedTransactionAttribute noTx = new RuleBasedTransactionAttribute();
        noTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        noTx.setDescriptor("【sunshine 事务 no事务】");

        Map<String, TransactionAttribute> txMap = new HashMap<>();
        //只读事务
        txMap.put("get*", readOnlyTx);
        txMap.put("query*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("list*", readOnlyTx);
        txMap.put("count*", readOnlyTx);
        txMap.put("exist*", readOnlyTx);
        txMap.put("search*", readOnlyTx);
        txMap.put("fetch*", readOnlyTx);
        //无事务
        txMap.put("noTx*", noTx);

        //写事务
        txMap.put("add*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("modify*", requiredTx);
        txMap.put("trans*", requiredTx);
        txMap.put("pay*", requiredTx);
        txMap.put("summary*", requiredTx);
        txMap.put("process*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("check*", requiredTx);
        txMap.put("cancel*", requiredTx);
        txMap.put("audit*", requiredTx);
        txMap.put("change*", requiredTx);
        txMap.put("create*", requiredTx);
        txMap.put("start*", requiredTx);
        txMap.put("*WithSqlTrans", requiredTx);
        txMap.put("*WithNewSqlTrans", requiredTx);

        NameMatchTransactionAttributeSource nameMatchTransactionAttributeSource = new NameMatchTransactionAttributeSource();
        nameMatchTransactionAttributeSource.setNameMap(txMap);
        //支持transactional注解
        AnnotationTransactionAttributeSource annotationTransactionAttributeSource = new AnnotationTransactionAttributeSource();
        // 成员变量中有一个数组 实现的是自己
        CompositeTransactionAttributeSource source = new CompositeTransactionAttributeSource(nameMatchTransactionAttributeSource,annotationTransactionAttributeSource);

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor(transactionManager, source);
        log.info("开启事务拦截。。。编程式事务");
        return transactionInterceptor;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

}
