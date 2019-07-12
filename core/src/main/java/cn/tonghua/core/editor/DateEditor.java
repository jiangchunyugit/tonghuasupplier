package cn.tonghua.core.editor;


import cn.tonghua.core.base.MyLogger;
import cn.tonghua.core.utils.LogUtil;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 时间处理
 * TODO(需要丰富内容)
 *
 */
public class DateEditor extends PropertyEditorSupport   {
    MyLogger logger = LogUtil.getLogger(getClass());

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            text = text.replace("-","/");
            try {
                switch (text.length()){
                    case 10: setValue(new SimpleDateFormat("yyyy/MM/dd").parse(text));  break;
                    case 19: setValue(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(text)); break;
                    default: setValue(text); break;
                }
            } catch (ParseException e) {
              logger.error("日期转换错误:{}",e.getMessage());
            }
        }
    }

    @Override
    public String getAsText() {

        return getValue().toString();
    }



}
