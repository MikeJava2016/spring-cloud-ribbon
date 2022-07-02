package com.sunshine.mybatis.plus.intercept;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.sunshine.mybatis.plus.annotation.SensitiveBean;
import com.sunshine.mybatis.plus.annotation.DecryptField;
import com.sunshine.mybatis.plus.annotation.EncryptField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})

//https://www.codeleading.com/article/98311634475/
@Slf4j
public class SM4Interceptor implements Interceptor {

    private static final SymmetricCrypto sm4 = SmUtil.sm4("1234567890123456".getBytes());

    private static final String keyPrefix = "SM4.";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];

        // 获取该sql语句的类型，例如update，insert
        String methodName = invocation.getMethod().getName();

        // 获取该sql语句放入的参数
        Object parameter = invocation.getArgs()[1];

        // 如果是查询操作，并且返回值不是敏感实体类对象，并且传入参数不为空，就直接调用执行方法，返回这个方法的返回值
        // 方法中可以判断这个返回值是否是多条数据，如果有数据，就代表着是select 操作，没有就代表是update insert delete，
        // 因为mybatis的dao层不能为非select操作设置返回值
        if (statement.getResultMaps().size() > 0) {

            // 获取到这个返回值的类属性
            Class<?> type = statement.getResultMaps().get(0).getType();
            // 返回值没有带敏感实体类对象注解
            if (!type.isAnnotationPresent(SensitiveBean.class)) {
                // 直接执行语句并返回
                return invocation.proceed();
            }
        }
        // 如果该参数为空，就不进行判断敏感实体类，直接执行sql
        // 并且
        // 如果判断该参数带有敏感实体类的注解，才对这个实体类进行扫描查看是否有加密解密的注解
        if (parameter != null && sensitiveBean(parameter)) {

            // 如果有就扫描是否是更新操作和是否有加密注解
            // 如果是更新或者插入时，就对数据进行加密后保存在数据库
            if (StringUtils.equalsIgnoreCase("update", methodName) ||
                    StringUtils.equalsIgnoreCase("insert", methodName)) {
                // 对参数内含注解的字段进行加密
                encryptField(parameter);
            }
        }

        // 继续执行sql语句,调用当前拦截的执行方法
        Object returnValue = invocation.proceed();
        try {
            // 当返回值类型为数组集合时，就判断是否需要进行数据解密
            if (returnValue instanceof ArrayList<?>) {
                List<?> list = (List<?>) returnValue;
                // 判断结果集的数据是否为空
                if (list == null || list.size() < 1) {
                    return returnValue;
                }
                Object object = list.get(0);
                // 为空值就返回数据
                if (object == null) {
                    return returnValue;
                }
                // 判断第一个对象是否有解密注解
                Field[] fields = object.getClass().getDeclaredFields();
                // 定义一个临时变量
                if (fields != null && 0 < ( fields.length)) {
                    for (Object o : list) {
                        //调用解密,在这个方法中针对某个带有解密注解的字段进行解密
                        decryptField(o);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnValue;
        }
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * <p>声明这是一个泛型方法,让所有参数都能够调用这个方法扫描带有解密注解的字段，
     * 进行解密，然后显示在前端页面中</p>
     *
     * @param <T>
     */
    public <T> void decryptField(T t) {
        // 获取对象的域
        Field[] declaredFields = t.getClass().getDeclaredFields();
        if (declaredFields != null && declaredFields.length > 0) {
            // 遍历这些字段
            for (Field field : declaredFields) {
                // 如果这个字段存在解密注解就进行解密
                if (field.isAnnotationPresent(DecryptField.class) && field.getType().toString().endsWith("String")) {
                    field.setAccessible(true);
                    try {
                        // 获取这个字段的值
                        String fieldValue = (String) field.get(t);
                        // 判断这个字段的数值是否不为空
                        if (StringUtils.isNotEmpty(fieldValue)) {
                            // 首先调用一个方法判断这个数据是否是未经过加密的，如果可以解密就进行解密，解密失败就返回元数据
                            boolean canDecrypt;
                            canDecrypt = this.canDecrypt(fieldValue);
                            if (canDecrypt) {
                                // 进行解密
                                String encryptData = sm4.encryptHex(fieldValue);
                                if (encryptData.equals("解密失败")) {
                                    log.error("该字段不是被加密的字段,需要联系管理员进行修改数据");
                                }
                                // 将值反射到对象中
                                field.set(t, encryptData);
                            } else {
                                // 不能解密的情况下，就不对这个对象做任何操作，即默认显示元数据
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean canDecrypt(String fieldValue) {
        return fieldValue.startsWith(keyPrefix);
    }


    /**
     * 查看这个类是否带有敏感实体类注解，有就返回true，否则返回false
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T> boolean sensitiveBean(T t) {
        // 判断是否带有敏感实体类的注解
        if (t.getClass().isAnnotationPresent(SensitiveBean.class)) {
            log.info("带有敏感实体类的注解");
            return true;
        } else {
            return false;
        }
    }


    /**
     * 对对象t加密
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T encrypt(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(EncryptField.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(t);
                        if (StringUtils.isNotEmpty(fieldValue)) {
                            field.set(t,keyPrefix + sm4.encryptHex(fieldValue));
                        }
                        field.setAccessible(false);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    /**
     * 对象解密
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T decrypt(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(DecryptField.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String)field.get(t);
                        if(StringUtils.isNotEmpty(fieldValue)) {
                            field.set(t, sm4.decrypt(fieldValue.replaceAll(keyPrefix,"")));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return t;
    }


    /**
     * 对含注解字段加密
     * @param t
     * @param <T>
     */
    public static <T> void encryptField(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(EncryptField.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String)field.get(t);
                        if(StringUtils.isNotEmpty(fieldValue)) {
                            field.set(t, sm4.encrypt(fieldValue));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 隐藏号码中间4位
     * @param t
     * @param <T>
     */
    public static <T> void hidePhone(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(DecryptField.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String)field.get(t);
                        if(StringUtils.isNotEmpty(fieldValue)) {
                            // 暂时与解密注解共用一个注解，该注解隐藏手机号中间四位
                            field.set(t, StringUtils.overlay(fieldValue, "****", 3, 7));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
