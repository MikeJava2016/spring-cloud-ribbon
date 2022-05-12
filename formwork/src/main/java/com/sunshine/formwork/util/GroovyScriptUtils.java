package com.sunshine.formwork.util;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * @Description 通过groovy动态反射加载与初始化类
 * @Author JL
 * @Date 2022/2/21
 * @Version V1.0
 */
public class GroovyScriptUtils {

    private GroovyScriptUtils(){
    }

    public static Class newGroovyInstance(String script) throws  IllegalArgumentException, SecurityException {
        // 每次执行都需要通过groovy动态反射加载类，高并发下有性能问题
        return new GroovyClassLoader().parseClass(script);
    }

    public static Object newObjectInstance(String script) throws CompilationFailedException, InstantiationException, IllegalArgumentException, SecurityException, IllegalAccessException {
        Class clazz = newGroovyInstance(script);
        return clazz.newInstance();
    }

}
