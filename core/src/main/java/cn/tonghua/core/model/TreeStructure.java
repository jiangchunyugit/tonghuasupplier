package cn.tonghua.core.model;

import java.util.List;

public interface TreeStructure<T> {

     Integer getPk();

     Integer getParentKey();

     void setChild(List<T> child);

}
