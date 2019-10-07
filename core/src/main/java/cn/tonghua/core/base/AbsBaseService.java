package cn.tonghua.core.base;


import cn.tonghua.core.bundle.MyRespBundle;
import cn.tonghua.core.constants.ResultMessage;
import cn.tonghua.core.editor.DateEditor;
import cn.tonghua.core.editor.DoubleEditor;
import cn.tonghua.core.editor.IntegerEditor;
import cn.tonghua.core.editor.LongEditor;
import cn.tonghua.core.logger.AbsLogPrinter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.Instant;
import java.util.Date;

/**
 *  接口数据组装
 *  提供日志方法
 */
public abstract class AbsBaseService extends AbsLogPrinter {


    protected static final Integer success = ResultMessage.SUCCESS.code;
    protected static final Integer fail = ResultMessage.FAIL.code;
    protected static final Integer error = ResultMessage.ERROR.code;
    protected static final ResultMessage SUCCESS = ResultMessage.SUCCESS;
    protected static final ResultMessage FAIL = ResultMessage.FAIL;
    protected static final ResultMessage ERROR = ResultMessage.ERROR;


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    /**
     * 发送数据
     * @param code    状态码
     * @param message 消息
     * @param datas   数据
     * @return
     */
    protected MyRespBundle sendJsonData(Integer code, String message, Object datas){
        MyRespBundle myRespBundle = initResult(code,message,datas);
        return myRespBundle;
    }

    /**
     *  发送数据
     * @param resultMessage  返回类型枚举
     * @param datas          数据
     * @return
     */
    protected MyRespBundle sendJsonData(ResultMessage resultMessage, Object datas){
        MyRespBundle myRespBundle = initResult(resultMessage.code,resultMessage.message,datas);
        return myRespBundle;
    }

    /**
     * 发送成功消息
     * @param message  消息
     * @return
     */
    protected MyRespBundle sendSuccessMessage(String message){
        MyRespBundle myRespBundle = initResult(success,message);
        return myRespBundle;
    }

    /**
     * 发送错误消息
     * @param message
     * @return
     */
    protected MyRespBundle sendFailMessage(String message){
        MyRespBundle myRespBundle = initResult(fail,message);
        return myRespBundle;
    }


    /**
     *  初始化消息返回
     * @param code
     * @param message
     * @return
     */
    protected MyRespBundle initResult(Integer code, String message){
        MyRespBundle myRespBundle = new MyRespBundle();
        myRespBundle.setCode(code);
        myRespBundle.setMsg(message);
        myRespBundle.setData(message);
        myRespBundle.setTimestamp(Instant.now().toEpochMilli());
        return myRespBundle;
    }

    /**
     * 初始化返回参数
     * @param code       状态码
     * @param message    消息
     * @param datas      数据
     * @return
     */
    protected MyRespBundle initResult(Integer code, String message, Object datas){
        MyRespBundle result = new MyRespBundle<>();
        result.setData(datas);
        result.setCode(code);
        result.setMsg(message);
        result.setTimestamp(Instant.now().toEpochMilli());
        return result;
    }


}
