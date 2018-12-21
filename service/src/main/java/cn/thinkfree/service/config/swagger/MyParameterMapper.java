package cn.thinkfree.service.config.swagger;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.Property;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.swagger2.mappers.ParameterMapper;
import springfox.documentation.swagger2.mappers.VendorExtensionsMapper;

import java.util.Collection;
import java.util.Map;

import static springfox.documentation.schema.Types.isBaseType;
import static cn.thinkfree.service.config.swagger.MyEnumMapper.*;
import static cn.thinkfree.service.config.swagger.MyProperties.*;

/**
 * 复刻参数解析
 * @see springfox.documentation.swagger2.mappers.ParameterMapper;
 */
public class MyParameterMapper extends ParameterMapper {
    private static final VendorExtensionsMapper vendorMapper = new VendorExtensionsMapper();

    public Parameter mapParameter(springfox.documentation.service.Parameter source) {

        Parameter bodyParameter = bodyParameter(source);
        return MySerializableParameterFactories.create(source).or(bodyParameter);
    }

    private Parameter bodyParameter(springfox.documentation.service.Parameter source) {
        BodyParameter parameter = new BodyParameter()
                .description(source.getDescription())
                .name(source.getName())
                .schema(fromModelRef(source.getModelRef()));
        parameter.setIn(source.getParamType());
        parameter.setAccess(source.getParamAccess());
        parameter.setPattern(source.getPattern());
        parameter.setRequired(source.isRequired());
        parameter.getVendorExtensions().putAll(vendorMapper.mapExtensions(source.getVendorExtentions()));
        for (Map.Entry<String, Collection<Example>> each : source.getExamples().asMap().entrySet()) {
            Optional<Example> example = FluentIterable.from(each.getValue()).first();
            if (example.isPresent() && example.get().getValue() != null) {
                parameter.addExample(each.getKey(), String.valueOf(example.get().getValue()));
            }
        }

        //TODO: swagger-core Body parameter does not have an enum property
        return parameter;
    }

    Model fromModelRef(ModelReference modelRef) {
        if (modelRef.isCollection()) {
            if (modelRef.getItemType().equals("byte")) {
                ModelImpl baseModel = new ModelImpl();
                baseModel.setType("string");
                baseModel.setFormat("byte");
                return maybeAddAllowableValuesToParameter(baseModel, modelRef.getAllowableValues());
            } else if (modelRef.getItemType().equals("file")) {
                ArrayModel files = new ArrayModel();
                files.items(new FileProperty());
                return files;
            }
            ModelReference itemModel = modelRef.itemModel().get();
            return new ArrayModel()
                    .items(maybeAddAllowableValues(itemTypeProperty(itemModel), itemModel.getAllowableValues()));
        }
        if (modelRef.isMap()) {
            ModelImpl baseModel = new ModelImpl();
            ModelReference itemModel = modelRef.itemModel().get();
            baseModel.additionalProperties(
                    maybeAddAllowableValues(
                            itemTypeProperty(itemModel),
                            itemModel.getAllowableValues()));
            return baseModel;
        }
        if (isBaseType(modelRef.getType())) {
            Property property = property(modelRef.getType());
            ModelImpl baseModel = new ModelImpl();
            baseModel.setType(property.getType());
            baseModel.setFormat(property.getFormat());
            return maybeAddAllowableValuesToParameter(baseModel, modelRef.getAllowableValues());

        }
        return new RefModel(modelRef.getType());
    }

}
