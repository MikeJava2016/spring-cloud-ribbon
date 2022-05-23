package com.sunshine.formwork.util;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilationFailedException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 通过groovy动态反射加载与初始化类
 * @Author JL
 * @Date 2022/2/21
 * @Version V1.0
 */
public class GroovyScriptUtils {

    private static final ConcurrentHashMap<Long,Class> map = new ConcurrentHashMap();


    private GroovyScriptUtils(){
    }

    public static Class newGroovyInstance(String script,Long id,Boolean forceResh) throws  IllegalArgumentException, SecurityException {
        if (forceResh) {
            Class clazz = new GroovyClassLoader().parseClass(script);
            map.put(id,clazz);
            return clazz;
        }
        if (map.containsKey(id)) {
            return map.get(id);
        }
        Class clazz = new GroovyClassLoader().parseClass(script);
        map.put(id,clazz);
        return clazz;
        // 每次执行都需要通过groovy动态反射加载类，高并发下有性能问题
    }

    public static Object newObjectInstance(String script,Long id,Boolean forceResh) throws CompilationFailedException, InstantiationException, IllegalArgumentException, SecurityException, IllegalAccessException {
        Class clazz = newGroovyInstance(script,id,forceResh);
        return clazz.newInstance();
    }

}
