package cn.thinkfree.core.model;



import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 基础Model
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
