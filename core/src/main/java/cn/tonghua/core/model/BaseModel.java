package cn.tonghua.core.model;



import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 基础Model
 * 对象序列化接口，对象写入到硬盘，用套接字在网络上传送对象，通过RMI传输对象。
 */
public abstract class BaseModel implements Serializable {

    public String toString() {
        StringBuffer strToReturn = new StringBuffer();

        Arrays.stream(PropertyUtils.getPropertyDescriptors(this))
                .filter((p)->  PropertyUtils.isReadable(this, p.getName())
                                && PropertyUtils.isWriteable(this, p.getName()))
                .forEach((p)->{
                    strToReturn.append(p.getName() + ": ");
                    try {
                        Object value = PropertyUtils.getProperty(this, p.getName());
                        strToReturn.append(value);
                    } catch (Exception e) {
                        strToReturn.append("");
                    }
                    strToReturn.append(",");
//                    strToReturn.append(System.getProperty("line.separator"));
                });
        strToReturn.insert(0,"{").insert(strToReturn.length(),"}");
        return strToReturn.toString();
    }
}
