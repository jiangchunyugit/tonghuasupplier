

package cn.tonghua.service.config.swagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.parameters.SerializableParameter;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.Parameter;
import springfox.documentation.swagger2.mappers.VendorExtensionsMapper;

import java.util.Map;

import static com.google.common.base.Functions.*;
import static com.google.common.base.Strings.*;
import static cn.tonghua.service.config.swagger.MyEnumMapper.*;
import static cn.tonghua.service.config.swagger.MyProperties.*;

/**
 * 重写getExample方法
 * 复刻Swagger
 * @see springfox.documentation.swagger2.mappers.SerializableParameterFactories;
 */
public class MySerializableParameterFactories {
    /**
     * 合并接口为内部接口
     * @see springfox.documentation.swagger2.mappers.SerializableParameterFactory;
     */
    interface SerializableParameterFactory {
        SerializableParameter create(Parameter parameter);
    }


  public static final Map<String, SerializableParameterFactory> factory = ImmutableMap.<String,
      SerializableParameterFactory>builder()
      .put("header", parameter -> {
        HeaderParameter param = new HeaderParameter();
        param.setDefaultValue(parameter.getDefaultValue());
        return param;
      })
      .put("form", parameter -> {
        FormParameter param = new FormParameter(){
          @Override
          @JsonProperty("x-example")
          public Object getExample() {
            if(StringUtils.isBlank(this.example)){
              return null;
            }
            return super.getExample();
          }
        };
        param.setDefaultValue(parameter.getDefaultValue());
        return param;
      })
      .put("path", parameter -> {
        PathParameter param = new PathParameter(){
          @Override
          @JsonProperty("x-example")
          public Object getExample() {
            if(StringUtils.isBlank(this.example)){
              return null;
            }
            return super.getExample();
          }
        };
        param.setDefaultValue(parameter.getDefaultValue());
        return param;
      })
      .put("query", parameter -> {
        QueryParameter param = new QueryParameter(){
          @Override
          @JsonProperty("x-example")
          public Object getExample() {
            if(StringUtils.isBlank(this.example)){
              return null;
            }
            return super.getExample();
          }
        };
        param.setDefaultValue(parameter.getDefaultValue());
        return param;
      })
      .put("cookie", parameter -> {
        CookieParameter param = new CookieParameter(){
          @Override
          @JsonProperty("x-example")
          public Object getExample() {
            if(StringUtils.isBlank(this.example)){
              return null;
            }
            return super.getExample();
          }
        };
        param.setDefaultValue(parameter.getDefaultValue());
        return param;
      })
      .build();

  private static final VendorExtensionsMapper vendorMapper = new VendorExtensionsMapper();


  private MySerializableParameterFactories() {
    throw new UnsupportedOperationException();
  }

  static Optional<io.swagger.models.parameters.Parameter> create(Parameter source) {
    SerializableParameterFactory factory = forMap(MySerializableParameterFactories.factory,
            parameter -> null)
        .apply(nullToEmpty(source.getParamType()).toLowerCase());

    SerializableParameter toReturn = factory.create(source);
    if (toReturn == null) {
      return Optional.absent();
    }
    ModelReference paramModel = source.getModelRef();
    toReturn.setName(source.getName());
    toReturn.setDescription(source.getDescription());
    toReturn.setAccess(source.getParamAccess());
    toReturn.setPattern(source.getPattern());
    toReturn.setRequired(source.isRequired());
    toReturn.setAllowEmptyValue(source.isAllowEmptyValue());
    toReturn.getVendorExtensions()
        .putAll(vendorMapper.mapExtensions(source.getVendorExtentions()));
    Property property = property(paramModel.getType());
    MyEnumMapper.maybeAddAllowableValuesToParameter(toReturn, property, source.getAllowableValues());
    if (paramModel.isCollection()) {
      if (paramModel.getItemType().equals("byte")) {
        toReturn.setType("string");
        toReturn.setFormat("byte");
      } else {
        toReturn.setCollectionFormat(collectionFormat(source));
        toReturn.setType("array");
        ModelReference paramItemModelRef = paramModel.itemModel().get();
        Property itemProperty
            = MyEnumMapper.maybeAddAllowableValues(
                MyProperties.itemTypeProperty(paramItemModelRef),
                paramItemModelRef.getAllowableValues());
        toReturn.setItems(itemProperty);
        MyEnumMapper.maybeAddAllowableValuesToParameter(toReturn, itemProperty, paramItemModelRef.getAllowableValues());
      }
    } else if (paramModel.isMap()) {
      ModelReference paramItemModelRef = paramModel.itemModel().get();
      Property itemProperty = new MapProperty(MyProperties.itemTypeProperty(paramItemModelRef));
      toReturn.setItems(itemProperty);
    } else {
      //TODO: swagger-core remove this downcast when swagger-core fixes its problem
      ((AbstractSerializableParameter) toReturn).setDefaultValue(source.getDefaultValue());
      if (source.getScalarExample() != null) {
        ((AbstractSerializableParameter) toReturn).setExample(String.valueOf(source.getScalarExample()));
      }
      toReturn.setType(property.getType());
      toReturn.setFormat(property.getFormat());
    }
    return Optional.of((io.swagger.models.parameters.Parameter) toReturn);
  }

  private static String collectionFormat(Parameter source) {
    return isNullOrEmpty(source.getCollectionFormat()) ? "multi" : source.getCollectionFormat();
  }

}
