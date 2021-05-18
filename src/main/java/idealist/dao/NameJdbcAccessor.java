package idealist.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 01:07:32
 */
public class NameJdbcAccessor {
    private final JdbcAccessor jdbcAccessor;

    public NameJdbcAccessor() {
        this.jdbcAccessor = new JdbcAccessor();
    }

    public NameJdbcAccessor(boolean pmdKnownBroken) {
        this.jdbcAccessor = new JdbcAccessor(pmdKnownBroken);
    }

    public NameJdbcAccessor(DataSource ds) {
        this.jdbcAccessor = new JdbcAccessor(ds);
    }

    public NameJdbcAccessor(StatementConfiguration stmtConfig) {
        this.jdbcAccessor = new JdbcAccessor(stmtConfig);
    }

    public NameJdbcAccessor(DataSource ds, boolean pmdKnownBroken) {
        this.jdbcAccessor = new JdbcAccessor(ds, pmdKnownBroken);
    }

    public NameJdbcAccessor(DataSource ds, StatementConfiguration stmtConfig) {
        this.jdbcAccessor = new JdbcAccessor(ds, stmtConfig);
    }

    public NameJdbcAccessor(DataSource ds, boolean pmdKnownBroken, StatementConfiguration stmtConfig) {
        this.jdbcAccessor = new JdbcAccessor(ds, pmdKnownBroken, stmtConfig);
    }

    public DataSource getDataSource() {
        return this.jdbcAccessor.getDataSource();
    }

    public int[] batch(Connection conn, String sql, List<Map<String, Object>> params) {
        return this.jdbcAccessor.batch(conn, NameParameter.getSql(sql), NameParameter.getParameters(sql, params));
    }

    public int[] batch(String sql, List<Map<String, Object>> params) {
        return this.jdbcAccessor.batch(NameParameter.getSql(sql), NameParameter.getParameters(sql, params));
    }

    public <T> T select(Connection conn, String sql, ResultSetHandler<T> rsh, Map<String, Object> params) {
        return this.jdbcAccessor.select(conn, NameParameter.getSql(sql), rsh, NameParameter.getParameters(sql, params));
    }

    public <T> T select(Connection conn, String sql, ResultSetHandler<T> rsh) {
        return this.jdbcAccessor.select(conn, NameParameter.getSql(sql), rsh);
    }

    public <T> T select(String sql, ResultSetHandler<T> rsh, Map<String, Object> params) {
        return this.jdbcAccessor.select(NameParameter.getSql(sql), rsh, NameParameter.getParameters(sql, params));
    }

    public <T> T select(String sql, ResultSetHandler<T> rsh) {
        return this.jdbcAccessor.select(NameParameter.getSql(sql), rsh);
    }

    public int update(Connection conn, String sql) {
        return this.jdbcAccessor.update(conn, NameParameter.getSql(sql));
    }

    public int update(Connection conn, String sql, Map<String, Object> params) {
        return this.jdbcAccessor.update(conn, NameParameter.getSql(sql), NameParameter.getParameters(sql, params));
    }

    public int update(String sql) {
        return this.jdbcAccessor.update(NameParameter.getSql(sql));
    }

    public int update(String sql, Map<String, Object> params) {
        return this.jdbcAccessor.update(NameParameter.getSql(sql), NameParameter.getParameters(sql, params));
    }

    public <T> T insert(String sql, ResultSetHandler<T> rsh) {
        return this.jdbcAccessor.insert(NameParameter.getSql(sql), rsh);
    }

    public <T> T insert(String sql, ResultSetHandler<T> rsh, Map<String, Object> params) {
        return this.jdbcAccessor.insert(NameParameter.getSql(sql), rsh, NameParameter.getParameters(sql, params));
    }

    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh) {
        return this.jdbcAccessor.insert(conn, NameParameter.getSql(sql), rsh);
    }

    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh, Map<String, Object> params) {
        return this.jdbcAccessor.insert(conn, NameParameter.getSql(sql), rsh, NameParameter.getParameters(sql, params));
    }

    public <T> T insertBatch(String sql, ResultSetHandler<T> rsh, List<Map<String, Object>> params) {
        return this.jdbcAccessor.insertBatch(NameParameter.getSql(sql), rsh, NameParameter.getParameters(sql, params));
    }

    public <T> T insertBatch(Connection conn, String sql, ResultSetHandler<T> rsh, List<Map<String, Object>> params) {
        return this.jdbcAccessor.insertBatch(conn, NameParameter.getSql(sql), rsh, NameParameter.getParameters(sql, params));
    }

    public int execute(Connection conn, String sql, Map<String, Object> params) {
        return this.jdbcAccessor.execute(conn, NameParameter.getSql(sql), NameParameter.getParameters(sql, params));
    }

    public int execute(String sql) {
        return this.jdbcAccessor.execute(sql);
    }

    public int execute(Connection conn, String sql) {
        return this.jdbcAccessor.execute(conn, sql);
    }

    public int execute(String sql, Map<String, Object> params) {
        return this.jdbcAccessor.execute(NameParameter.getSql(sql), NameParameter.getParameters(sql, params));
    }

    public <T> List<T> execute(Connection conn, String sql, ResultSetHandler<T> rsh, Map<String, Object> params) {
        return this.jdbcAccessor.execute(conn, NameParameter.getSql(sql), rsh, NameParameter.getParameters(sql, params));
    }

    public <T> List<T> execute(String sql, ResultSetHandler<T> rsh, Map<String, Object> params) {
        return this.jdbcAccessor.execute(NameParameter.getSql(sql), rsh, NameParameter.getParameters(sql, params));
    }
}
