/*
package com.sunshine.security;

*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/22 18:34
 **//*


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.sun.javafx.PlatformUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

*/
/**
 * @author River
 *//*

public class CodeGenerator {
    private org.apache.velocity.context.Context context;
    */
/**
     * 代码生成位置
     *//*

    public static final String PARENT_NAME = "com.sunshine.security";

    */
/**
     * modular 名字
     *//*

    public static final String MODULAR_NAME = "";

    */
/**
     * 基本路径
     *//*

    public static final String SRC_MAIN_JAVA = "src/main/java/";

    public static final String projectPath = "D:/GP_code/git_hub/spring-cloud-ribbon/ribbon-security";

    */
/**
     * 作者
     *//*

    public static final String AUTHOR = "huzhanglin";

    */
/**
     * 是否是 rest 接口
     *//*

    private static final boolean REST_CONTROLLER_STYLE = true;

    public static final String JDBC_MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/ribbon-security?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";

    public static final String JDBC_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    public static final String JDBC_USERNAME = "root";

    public static final String JDBC_PASSWORD = "123456";

    public static void main(String[] args) {
//        String moduleName = scanner("模块名");
//        String tableName = scanner("表名(多个表明用,号隔开)");
//        String tablePrefix = scanner("表前缀(无前缀输入#)").replaceAll("#", "");
        autoGenerator("security","user,role,user_role", "#");
    }

    public static void autoGenerator(String moduleName, String tableName, String tablePrefix) {
        new FastAutoGenerator()
                .setGlobalConfig(getGlobalConfig())
                .setDataSource(getDataSourceConfig())
                .setPackageInfo(getPackageConfig(moduleName))
                .setStrategy(getStrategyConfig(tableName, tablePrefix))
                .setCfg(getInjectionConfig(moduleName))
                .setTemplate(getTemplateConfig())
                .execute();
    }

    private static String getDateTime() {
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    private static InjectionConfig getInjectionConfig(final String moduleName) {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String,Object> map = new HashMap<>(10);
                map.put("dateTime", getDateTime());
                setMap(map);
//                final String projectPath = System.getProperty("user.dir");
                final String projectPath2 = projectPath+"/src/main/resources";
                List<FileOutConfig> fileOutConfigList = new ArrayList<FileOutConfig>();
                // 自定义配置会被优先输出
                fileOutConfigList.add(new FileOutConfig("/generator/mapper.xml.vm") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名，如果entity设置了前后缀，此次注意xml的名称也会跟着发生变化
                        return projectPath + "/mapper/" +
                                moduleName + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                    }
                });
                setFileOutConfigList(fileOutConfigList);
            }
        };
    }


    private static StrategyConfig getStrategyConfig(String tableName, String tablePrefix) {
        return new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableName.split(","))
                .setRestControllerStyle(REST_CONTROLLER_STYLE)
                .setEntityBuilderModel(true)
                .setControllerMappingHyphenStyle(true)
                .entityTableFieldAnnotationEnable(true)
                .setEntityLombokModel(true)
                .setTablePrefix(tablePrefix + "_");
    }

    private static PackageConfig getPackageConfig(String moduleName) {
        return new PackageConfig()
                .setModuleName(moduleName)
                .setParent(PARENT_NAME)
                .setService("service")
                .setServiceImpl("serviceimpl")
                .setController("controller")
                .setEntity("entity");
    }

    private static DataSourceConfig getDataSourceConfig() {
        return new DataSourceConfig()
                .setUrl(JDBC_MYSQL_URL)
                .setDriverName(JDBC_DRIVER_NAME)
                .setUsername(JDBC_USERNAME)
                .setPassword(JDBC_PASSWORD);
    }

    private static GlobalConfig getGlobalConfig() {

        String filePath = projectPath + "/" + MODULAR_NAME + SRC_MAIN_JAVA;
        if (PlatformUtil.isWindows()) {
            filePath = filePath.replaceAll("/+|//+", "//");
        } else {
            filePath = filePath.replaceAll("/+|//+", "/");
        }
        return new GlobalConfig.Builder()
                .outputDir(filePath)
                .dateType(DateType.ONLY_DATE)
//                .(IdType.UUID)
                .author(AUTHOR)
//                .setBaseColumnList(true)
                .enableSwagger( )
//                .setEnableCache(false)
                .(true)
                .setMapperName("%sMapper")
                //.setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")
                .setEntityName("%sModel")
                .setOpen(false);
    }

    private static TemplateConfig getTemplateConfig() {
        return new TemplateConfig()
                .setController("/generator/controller.java.vm")
                .setService("/generator/service.java.vm")
                .setServiceImpl("/generator/serviceImpl.java.vm")
                .setEntity("/generator/entity.java.vm")
                .setMapper("/generator/mapper.java.vm")
                .setXml(null);
    }

    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        sb.append("please input " + tip + " : ");
        System.out.println(sb.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("please input the correct " + tip + ". ");
    }
}*/
