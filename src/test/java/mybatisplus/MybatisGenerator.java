package mybatisplus;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisGenerator {


    public static void main(String[] args) {
        List<String> tables = List.of("news", "news_comment", "file");
        String localProperty = System.getProperty("user.dir");
        String mapLocation = localProperty + "\\src\\main\\resources\\mappers";
        String finalMapLocation = mapLocation;

        String outputLocation = localProperty + "\\src\\main\\java";


        Map<String, String> customFileMap = new HashMap<>();
        customFileMap.put("FinanceEditBo.java", "/vm/java/editBo.java.vm");
        customFileMap.put("FinanceAddBo.java", "/vm/java/addBo.java.vm");
        customFileMap.put("FinanceQueryBo.java", "/vm/java/queryBo.java.vm");
        customFileMap.put("FinanceVo.java", "/vm/java/vo.java.vm");

        FastAutoGenerator.create("jdbc:mysql://47.103.156.64:3307/instabook?serverTimezone=GMT%2b8", "root", "Njwx671011!")//数据库配置
                .globalConfig(builder -> {
                    builder.author("Kuiyan Zhao")
                            .fileOverride() // 覆盖已生成文件
                            .dateType(DateType.ONLY_DATE)// 日期类型
                            .outputDir(outputLocation); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.instabook") // 设置父包名
//                            .moduleName("xxx")// 设置父包模块名
                            .controller("controller")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .entity("model.dos")
                            .other("model")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, finalMapLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("sys_") // 设置过滤表前缀
                            //entity配置
                            .entityBuilder()
                            .enableLombok()
                            //.superClass(com.cloud.model.common.BaseDataEntity.class)//继承父类
//                            .logicDeleteColumnName("del_flag")//逻辑删除字段
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("create_by_name", FieldFill.INSERT))
                            .addTableFills(new Column("create_by_id", FieldFill.INSERT))
                            .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                            .addTableFills(new Column("update_by_name", FieldFill.INSERT_UPDATE))
                            .addTableFills(new Column("update_by_id", FieldFill.INSERT_UPDATE))
                            .enableTableFieldAnnotation()
                            //controller配置
                            .controllerBuilder()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            //service配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            //dao配置
                            .mapperBuilder()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapping")
                            .enableMapperAnnotation();
                })
                //模板配置
//                .templateConfig((scanner, builder) -> builder
//                                .disable()//禁用所有模板
//                                //.entityKt("/templates/entity.java")// 设置实体模板路径(kotlin)，/templates/entity.java
////						.disable(TemplateType.ENTITY)//禁用模板 TemplateType.ENTITY
//                                .entity("/java/domain.java.vm") //设置实体模板路径(JAVA)，/templates/entity.java
//                                .service("/java/service.java.vm")//设置 service 模板路径，/templates/service.java
//                                .serviceImpl("/java/serviceImpl.java.vm")//设置 serviceImpl 模板路径，/templates/serviceImpl.java
//                                .mapper("/java/mapper.java.vm")//设置 mapper 模板路径，/templates/mapper.java
//                                .mapperXml("/java/mapper.xml.vm")//设置 mapperXml 模板路径，/templates/mapper.xml
//                                .controller("/java/controller.java")//设置 controller 模板路径，/templates/controller.java
//                                .build()//加入构建队列
//                )
//                .injectionConfig(builder -> builder
//                        .beforeOutputFile((tableInfo, objectMap) -> {
//                            System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
//                        }) //输出文件之前消费者
//                        .customFile(customFileMap) //自定义配置模板文件
//                        .build()//加入构建队列
//                )
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
