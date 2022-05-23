package com.sunshine.common.util.web;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 加载Properties流实现 springboot在静态方法中读取xxx.properties配置文件属性
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/21 10:27
 **/
public class PropertyUtils {

    private static final ConcurrentHashMap<String,Properties> configs = new ConcurrentHashMap<String, Properties>();

    /**
     * 读取配置文件下的key对应的值
     * @param fileName
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getPropertiesValue(String fileName,String key,String defaultValue){
        Properties config = getConfig(fileName);
        if (config == null){
            return defaultValue;
        }
        Object value = config.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    /**
     * 读取 classpath 下 指定的properties配置文件，加载到Properties并返回Properties
     *
     * @param fileName 配置文件名，如：mongo.properties
     * @return
     */
    public static synchronized Properties  getConfig(String fileName) {
        if (configs.containsKey(fileName)){
            return configs.get(fileName);
        }
        Properties props = null;
        try {
            props = new Properties();
            InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            props.load(bf);
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        configs.put(fileName,props);
        return props;
    }

    public static String getPropValue(Properties prop, String key) {
        if (key == null || "".equals(key.trim())) {
            return null;
        }
        String value = prop.getProperty(key);
        if (value == null) {
            return null;
        }
        value = value.trim();
        //判断是否是环境变量配置属性,例如 server.env=${serverEnv:local}
        if (value.startsWith("${") && value.endsWith("}") && value.contains(":")) {
            int indexOfColon = value.indexOf(":");
            String envName = value.substring(2, indexOfColon);
            //获取系统环境变量 envName 的内容，如果没有找到，则返回defaultValue
            String envValue = System.getenv(envName);
            if (envValue == null) {
                //配置的默认值
                return value.substring(indexOfColon + 1, value.length() - 1);
            }
            return envValue;
        }
        return value;
    }

    public static void main(String[] args) {
        Properties prop = PropertyUtils.getConfig("mongo-pool.properties");
        //
        System.out.println(prop.getProperty("spring.data.mongodb.database"));

        //建议采用下面这种获取方法，能够处理 环境变量配置属性 例如 server.env=${serverEnv:local}
        System.out.println( PropertyUtils.getPropValue( prop , "spring.data.mongodb.database" ) );
    }
}
