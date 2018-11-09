package cn.thinkfree.core.base;


import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.editor.DateEditor;
import cn.thinkfree.core.editor.DoubleEditor;
import cn.thinkfree.core.editor.IntegerEditor;
import cn.thinkfree.core.editor.LongEditor;
import cn.thinkfree.core.logger.AbsLogPrinter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.Instant;
import java.util.Date;

/**
 *  控制层
 *  提供日志方法
 *  提供数据绑定
 *  提供出口方法
 */
public abstract class AbsBaseController extends AbsLogPrinter {


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
        myRespBundle.setMessage(SUCCESS.message);
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
    protected   MyRespBundle initResult(Integer code, String message, Object datas){
        MyRespBundle result = new MyRespBundle<>();
        result.setData(datas);
        result.setCode(code);
        result.setMessage(message);
        result.setTimestamp(Instant.now().toEpochMilli());
        return result;
    }


}
