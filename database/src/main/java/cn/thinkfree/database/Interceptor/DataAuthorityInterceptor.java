package cn.thinkfree.database.Interceptor;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.annotation.AuthAnnotation;
import cn.thinkfree.database.utils.ReflectUtil;
import cn.thinkfree.database.vo.UserVO;

/**
 * mybatis数据权限拦截器 - prepare
 * @author GaoYuan
 * @date 2018/4/17 上午9:52
 */
@Intercepts({
//          @Signature(type = Executor.class, method = "update prepare", args = { MappedStatement.class, Object.class }),
        @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class })
//        @Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
})
@Component
public class DataAuthorityInterceptor implements Interceptor {
    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(DataAuthorityInterceptor.class);

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(log.isInfoEnabled()){
            log.info("进入 PrepareInterceptor 拦截器...");
        }
//        if(invocation.getTarget() instanceof RoutingStatementHandler) {
//            RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
//            StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
//            //通过反射获取delegate父类BaseStatementHandler的mappedStatement属性
//            MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
//            String id = mappedStatement.getId();
//           // String className = id.substring(0, id.lastIndexOf("."));
//            String methodName = id.substring(id.lastIndexOf(".") + 1, id.length());
//            if(!methodName.contains("Page")){
//              return invocation.proceed();
//            }
//            BoundSql boundSql = delegate.getBoundSql();
//            
//            ReflectUtil.setFieldValue(boundSql, "sql", permissionSql(boundSql.getSql()));
//        }
//        return invocation.proceed();
		try {
			if (invocation.getTarget() instanceof RoutingStatementHandler) {
				RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
				StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(statementHandler, "delegate");
				BoundSql boundSql = delegate.getBoundSql();
				MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate,"mappedStatement");
				Class<?> classType = Class
						.forName(mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf(".")));
				String mName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1,
						mappedStatement.getId().length());
				Object obj = boundSql.getParameterObject();
				
				
				for (Method method : classType.getDeclaredMethods()) {
					//抽象接口中 返回true
					if(obj != null){
						boolean flag = ReflectUtil.getFieldValue(obj, "dataFlag")==null?false:(boolean) ReflectUtil.getFieldValue(obj, "dataFlag");
						
						if (flag == true && mappedStatement.getSqlCommandType().toString().equals("SELECT")) {
							String sql = boundSql.getSql();
							sql = permissionSql(sql);
							ReflectUtil.setFieldValue(boundSql, "sql", sql);
							break;
						}
					}
					//自定义注解走这里
					if (method.isAnnotationPresent(AuthAnnotation.class) && mName.equals(method.getName())) {
						AuthAnnotation companyAnnotation = method.getAnnotation(AuthAnnotation.class);
						String sql = boundSql.getSql();
						if (mappedStatement.getSqlCommandType().toString().equals("SELECT")) {
							sql = permissionSql(sql);
//							if (companyAnnotation.Alias().equals("")) {
//								sql = sql + " and COMPANY_ID = '" + COMPANY_ID + "' ";
//							} else {
//								//sql = sql + " and " + companyAnnotation.Alias() + ".COMPANY_ID = '" + COMPANY_ID + "' ";
//							}
						} 
						// System.err.println("执行后SQL:"+sql);
						ReflectUtil.setFieldValue(boundSql, "sql", sql);
						break;
					}
					
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return invocation.proceed();

    }
    
  


    /**
     * 权限sql包装
     * @author lqd
     * @date 2018/4/17 上午9:51
     */
    protected String permissionSql(String sql) {
    	UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
    	if(userVO == null){
    		return sql+" where company_id in (1)";//测试玩
    	}
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < userVO.getRelationMap().size(); i++) {
    		if(userVO.getRelationMap().size()< i){
    			sb.append("'"+userVO.getRelationMap().get(i)+"'")
        		.append(",");
    		}else{
    			sb.append("'"+userVO.getRelationMap().get(i)+"'");
    		}
    	
		}
    	sql += " where company_id in ("+userVO.getRelationMap()+")";
    	
		return sql;
    }



}
