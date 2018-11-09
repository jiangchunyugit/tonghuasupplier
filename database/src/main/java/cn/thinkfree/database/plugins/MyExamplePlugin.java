package cn.thinkfree.database.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class MyExamplePlugin extends PluginAdapter {


    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        topLevelClass.setSuperClass("cn.thinkfree.core.model.AbstractDataAuth");
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }
}
