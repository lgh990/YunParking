package com.yuncitys.smart.parking.common.data;

import com.yuncitys.ag.core.context.BaseContextHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.io.StringReader;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 租户数据隔离拦截器
 *
 * @author smart
 * @create 2018/2/9.
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})})
public class MybatisDataInterceptor implements Interceptor {
    private IUserDepartDataService userDepartDataService;

    public MybatisDataInterceptor(IUserDepartDataService userDepartDataService, String... ignoreMappers) {
        this.userDepartDataService = userDepartDataService;
        for (String ignore : ignoreMappers) {
            this.ignoreMappers.add(ignore);
        }
    }

    private Set<String> ignoreMappers = new HashSet<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        //由于mappedStatement中有我们需要的方法id,但却是protected的，所以要通过反射获取
        MetaObject statementHandler = SystemMetaObject.forObject(handler);
        MappedStatement mappedStatement = (MappedStatement) statementHandler.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        String namespace = mappedStatement.getId();
        if (ignoreMappers.contains(namespace)) {
            return invocation.proceed();
        }
        String className = namespace.substring(0, namespace.lastIndexOf("."));
        Class<?> clazz = Class.forName(className);

        Tenant tenant = clazz.getAnnotation(Tenant.class);
        Depart depart = clazz.getAnnotation(Depart.class);
        BoundSql boundSql = handler.getBoundSql();
        //获取sql
        String sql = boundSql.getSql();
        StringBuffer whereSql = new StringBuffer();
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        // 获取当前查询条件
        Expression where = plain.getWhere();
        // 租户数据隔离
        if (tenant != null) {
            whereSql.append("( 1 = 0 or ");
            whereSql.append(addAlias(plain, tenant.userField())).append(" = '").append(BaseContextHandler.getUserID()).append("'");
            whereSql.append(" or ");
            whereSql.append(addAlias(plain, tenant.tenantField()) + " = '" + BaseContextHandler.getTenantID() + "' )");
            if (depart != null) {
                whereSql.append(" and ( ");
            }
        }
        // 部门数据隔离
        if (depart != null) {
            // 添加用户自己的查询条件
            whereSql.append(" ( 1 = 0 or ");
            whereSql.append(addAlias(plain, depart.userField())).append(" = '").append(BaseContextHandler.getUserID()).append("'");
            // 拼接部门数据sql
            if (userDepartDataService != null) {
                List<String> userDataDepartIds = userDepartDataService.getUserDataDepartIds(BaseContextHandler.getUserID());
                if (userDataDepartIds != null && userDataDepartIds.size() > 0) {
                    for (int i = 0; i < userDataDepartIds.size(); i++) {
                        whereSql.append(" or ");
                        whereSql.append(addAlias(plain, depart.departField())).append(" = '").append(userDataDepartIds.get(i)).append("' ");
                    }
                }
            }
            whereSql.append(" ) ");
        }
        if (where == null) {
            if (tenant != null) {
                whereSql.append(")");
            }
            if (whereSql.length() > 0) {
                Expression expression = CCJSqlParserUtil
                        .parseCondExpression(whereSql.toString());
                Expression whereExpression = (Expression) expression;
                plain.setWhere(whereExpression);
            }
        } else {
            if (whereSql.length() > 0) {
                whereSql.append(" and ( " + where.toString() + " )");
            } else {
                whereSql.append(where.toString());
            }
            if (tenant != null) {
                whereSql.append(")");
            }
            Expression expression = CCJSqlParserUtil
                    .parseCondExpression(whereSql.toString());
            plain.setWhere(expression);
        }
        statementHandler.setValue("delegate.boundSql.sql", select.toString());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String addAlias(PlainSelect plain, String field) {
        if (plain.getFromItem().getAlias() != null) {
            return plain.getFromItem().getAlias() + "." + field;
        } else {
            return field;
        }
    }
}
