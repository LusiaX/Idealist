package idealist.dao;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import idealist.util.StringUtils;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-139 10:53:24
 */
public class BeanJdbcAccessor {

    private final NameJdbcAccessor jdbcAccessor;

    public BeanJdbcAccessor() {
        this.jdbcAccessor = new NameJdbcAccessor();
    }

    public BeanJdbcAccessor(boolean pmdKnownBroken) {
        this.jdbcAccessor = new NameJdbcAccessor(pmdKnownBroken);
    }

    public BeanJdbcAccessor(DataSource ds) {
        this.jdbcAccessor = new NameJdbcAccessor(ds);
    }

    public BeanJdbcAccessor(StatementConfiguration stmtConfig) {
        this.jdbcAccessor = new NameJdbcAccessor(stmtConfig);
    }

    public BeanJdbcAccessor(DataSource ds, boolean pmdKnownBroken) {
        this.jdbcAccessor = new NameJdbcAccessor(ds, pmdKnownBroken);
    }

    public BeanJdbcAccessor(DataSource ds, StatementConfiguration stmtConfig) {
        this.jdbcAccessor = new NameJdbcAccessor(ds, stmtConfig);
    }

    public BeanJdbcAccessor(DataSource ds, boolean pmdKnownBroken, StatementConfiguration stmtConfig) {
        this.jdbcAccessor = new NameJdbcAccessor(ds, pmdKnownBroken, stmtConfig);
    }

    public DataSource getDataSource() {
        return this.jdbcAccessor.getDataSource();
    }

    public int[] batch(Connection conn, String sql, List<Object> beans) {
        return this.jdbcAccessor.batch(conn, sql, beans.stream().map(BeanJdbcAccessor::toMap).collect(Collectors.toList()));
    }

    public int[] batch(String sql, List<Object> beans) {
        return this.jdbcAccessor.batch(sql, beans.stream().map(BeanJdbcAccessor::toMap).collect(Collectors.toList()));
    }

    public <T> T select(Connection conn, String sql, ResultSetHandler<T> rsh, Object bean) {
        return this.jdbcAccessor.select(conn, sql, rsh, toMap(bean));
    }

    public <T> T select(Connection conn, String sql, ResultSetHandler<T> rsh) {
        return this.jdbcAccessor.select(conn, sql, rsh);
    }

    public <T> T select(String sql, ResultSetHandler<T> rsh, Object bean) {
        return this.jdbcAccessor.select(sql, rsh, toMap(bean));
    }

    public <T> T select(String sql, ResultSetHandler<T> rsh) {
        return this.jdbcAccessor.select(sql, rsh);
    }

    public int update(Connection conn, String sql) {
        return this.jdbcAccessor.update(conn, sql);
    }

    public int update(Connection conn, String sql, Object bean) {
        return this.jdbcAccessor.update(conn, sql, toMap(bean));
    }

    public int update(String sql) {
        return this.jdbcAccessor.update(sql);
    }

    public int update(String sql, Object bean) {
        return this.jdbcAccessor.update(sql, toMap(bean));
    }

    public <T> T insert(String sql, ResultSetHandler<T> rsh) {
        return this.jdbcAccessor.insert(sql, rsh);
    }

    public <T> T insert(String sql, ResultSetHandler<T> rsh, Object bean) {
        return this.jdbcAccessor.insert(sql, rsh, toMap(bean));
    }

    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh) {
        return this.jdbcAccessor.insert(conn, sql, rsh);
    }

    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh, Object bean) {
        return this.jdbcAccessor.insert(conn, sql, rsh, toMap(bean));
    }

    public <T> T insertBatch(String sql, ResultSetHandler<T> rsh, List<Object> bean) {
        return this.jdbcAccessor.insertBatch(sql, rsh, bean.stream().map(BeanJdbcAccessor::toMap).collect(Collectors.toList()));
    }

    public <T> T insertBatch(Connection conn, String sql, ResultSetHandler<T> rsh, List<Object> beans) {
        return this.jdbcAccessor.insertBatch(conn, sql, rsh, beans.stream().map(BeanJdbcAccessor::toMap).collect(Collectors.toList()));
    }

    public int execute(Connection conn, String sql, Object bean) {
        return this.jdbcAccessor.execute(conn, sql, toMap(bean));
    }

    public int execute(String sql) {
        return this.jdbcAccessor.execute(sql);
    }

    public int execute(Connection conn, String sql) {
        return this.jdbcAccessor.execute(conn, sql);
    }

    public int execute(String sql, Object bean) {
        return this.jdbcAccessor.execute(sql, toMap(bean));
    }

    public <T> List<T> execute(Connection conn, String sql, ResultSetHandler<T> rsh, Object bean) {
        return this.jdbcAccessor.execute(conn, sql, rsh, toMap(bean));
    }

    public <T> List<T> execute(String sql, ResultSetHandler<T> rsh, Object bean) {
        return this.jdbcAccessor.execute(sql, rsh, BeanJdbcAccessor.toMap(bean));
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }

        Map<String, Object> map = new HashMap<>();
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                var property = new PropertyDescriptor(field.getName(), obj.getClass());
                Method method = property.getReadMethod();
                if (method == null) continue;
                var name = StringUtils.toUnderline(property.getName());
                map.put(name, method.invoke(obj));
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return map;
    }
}
