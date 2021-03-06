package idealist.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Executes SQL queries with pluggable strategies for handling
 * <code>ResultSet</code>s.  This class is thread safe.
 *
 * @see ResultSetHandler
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 01:01:38
 */
public class JdbcAccessor extends AbstractJdbcAccessor {

    /**
     * Constructor for JdbcAccessor.
     */
    public JdbcAccessor() {
        super();
    }

    /**
     * Constructor for JdbcAccessor that controls the use of <code>ParameterMetaData</code>.
     *
     * @param pmdKnownBroken Some drivers don't support {@link java.sql.ParameterMetaData#getParameterType(int) };
     *                       if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     *                       and if it breaks, we'll remember not to use it again.
     */
    public JdbcAccessor(boolean pmdKnownBroken) {
        super(pmdKnownBroken);
    }

    /**
     * Constructor for JdbcAccessor that takes a <code>DataSource</code> to use.
     * <p>
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds The <code>DataSource</code> to retrieve connections from.
     */
    public JdbcAccessor(DataSource ds) {
        super(ds);
    }

    /**
     * Constructor for JdbcAccessor that takes a <code>StatementConfiguration</code> to configure statements when
     * preparing them.
     *
     * @param stmtConfig The configuration to apply to statements when they are prepared.
     */
    public JdbcAccessor(StatementConfiguration stmtConfig) {
        super(stmtConfig);
    }

    /**
     * Constructor for JdbcAccessor that takes a <code>DataSource</code> and controls the use of <code>ParameterMetaData</code>.
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds             The <code>DataSource</code> to retrieve connections from.
     * @param pmdKnownBroken Some drivers don't support {@link java.sql.ParameterMetaData#getParameterType(int) };
     *                       if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     *                       and if it breaks, we'll remember not to use it again.
     */
    public JdbcAccessor(DataSource ds, boolean pmdKnownBroken) {
        super(ds, pmdKnownBroken);
    }

    /**
     * Constructor for JdbcAccessor that takes a <code>DataSource</code> to use and a <code>StatementConfiguration</code>.
     * <p>
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds         The <code>DataSource</code> to retrieve connections from.
     * @param stmtConfig The configuration to apply to statements when they are prepared.
     */
    public JdbcAccessor(DataSource ds, StatementConfiguration stmtConfig) {
        super(ds, stmtConfig);
    }

    /**
     * Constructor for JdbcAccessor that takes a <code>DataSource</code>, a <code>StatementConfiguration</code>, and
     * controls the use of <code>ParameterMetaData</code>.  Methods that do not take a <code>Connection</code> parameter
     * will retrieve connections from this <code>DataSource</code>.
     *
     * @param ds             The <code>DataSource</code> to retrieve connections from.
     * @param pmdKnownBroken Some drivers don't support {@link java.sql.ParameterMetaData#getParameterType(int) };
     *                       if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     *                       and if it breaks, we'll remember not to use it again.
     * @param stmtConfig     The configuration to apply to statements when they are prepared.
     */
    public JdbcAccessor(DataSource ds, boolean pmdKnownBroken, StatementConfiguration stmtConfig) {
        super(ds, pmdKnownBroken, stmtConfig);
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     *
     * @param conn   The Connection to use to run the query.  The caller is
     *               responsible for closing this Connection.
     * @param sql    The SQL to execute.
     * @param params An array of query replacement parameters.  Each row in
     *               this array is one set of batch replacement values.
     * @return The number of rows updated per statement.
     * @throws SQLRuntimeException if a database access error occurs
     * @since DbUtils 1.1
     */
    public int[] batch(Connection conn, String sql, Object[][] params) {
        return this.batch(conn, false, sql, params);
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.  The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the update will not be saved.
     *
     * @param sql    The SQL to execute.
     * @param params An array of query replacement parameters.  Each row in
     *               this array is one set of batch replacement values.
     * @return The number of rows updated per statement.
     * @throws SQLRuntimeException if a database access error occurs
     * @since DbUtils 1.1
     */
    public int[] batch(String sql, Object[][] params) {
        Connection conn = this.prepareConnection();

        return this.batch(conn, true, sql, params);
    }

    /**
     * Calls update after checking the parameters to ensure nothing is null.
     *
     * @param conn      The connection to use for the batch call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param params    An array of query replacement parameters.  Each row in
     *                  this array is one set of batch replacement values.
     * @return The number of rows updated in the batch.
     * @throws SQLRuntimeException If there are database or parameter errors.
     */
    private int[] batch(Connection conn, boolean closeConn, String sql, Object[][] params) {
        if (conn == null) {
            throw new SQLRuntimeException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null SQL statement");
        }

        if (params == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null parameters. If parameters aren't need, pass an empty array.");
        }

        PreparedStatement stmt = null;
        int[] rows = null;
        try {
            stmt = this.prepareStatement(conn, sql);

            for (int i = 0; i < params.length; i++) {
                this.fillStatement(stmt, params[i]);
                stmt.addBatch();
            }
            rows = stmt.executeBatch();

        } catch (SQLException e) {
            this.rethrow(e, sql, (Object[]) params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }

    /**
     * Execute an SQL SELECT query with a single replacement parameter. The
     * caller is responsible for closing the connection.
     *
     * @param <T>   The type of object that the handler returns
     * @param conn  The connection to execute the query in.
     * @param sql   The query to execute.
     * @param param The replacement parameter.
     * @param rsh   The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @deprecated Use {@link #select(Connection, String, ResultSetHandler, Object...)}
     */
    @Deprecated
    public <T> T select(Connection conn, String sql, Object param, ResultSetHandler<T> rsh) {
        return this.<T>select(conn, false, sql, rsh, new Object[]{param});
    }

    /**
     * Execute an SQL SELECT query with replacement parameters.  The
     * caller is responsible for closing the connection.
     *
     * @param <T>    The type of object that the handler returns
     * @param conn   The connection to execute the query in.
     * @param sql    The query to execute.
     * @param params The replacement parameters.
     * @param rsh    The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @deprecated Use {@link #select(Connection, String, ResultSetHandler, Object...)} instead
     */
    @Deprecated
    public <T> T select(Connection conn, String sql, Object[] params, ResultSetHandler<T> rsh) {
        return this.<T>select(conn, false, sql, rsh, params);
    }

    /**
     * Execute an SQL SELECT query with replacement parameters.  The
     * caller is responsible for closing the connection.
     *
     * @param <T>    The type of object that the handler returns
     * @param conn   The connection to execute the query in.
     * @param sql    The query to execute.
     * @param rsh    The handler that converts the results into an object.
     * @param params The replacement parameters.
     * @return The object returned by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public <T> T select(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) {
        return this.<T>select(conn, false, sql, rsh, params);
    }

    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for closing the connection.
     *
     * @param <T>  The type of object that the handler returns
     * @param conn The connection to execute the query in.
     * @param sql  The query to execute.
     * @param rsh  The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public <T> T select(Connection conn, String sql, ResultSetHandler<T> rsh) {
        return this.<T>select(conn, false, sql, rsh, (Object[]) null);
    }

    /**
     * Executes the given SELECT SQL with a single replacement parameter.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param <T>   The type of object that the handler returns
     * @param sql   The SQL statement to execute.
     * @param param The replacement parameter.
     * @param rsh   The handler used to create the result object from
     *              the <code>ResultSet</code>.
     * @return An object generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @deprecated Use {@link #select(String, ResultSetHandler, Object...)}
     */
    @Deprecated
    public <T> T select(String sql, Object param, ResultSetHandler<T> rsh) {
        Connection conn = this.prepareConnection();

        return this.<T>select(conn, true, sql, rsh, new Object[]{param});
    }

    /**
     * Executes the given SELECT SQL query and returns a result object.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param <T>    The type of object that the handler returns
     * @param sql    The SQL statement to execute.
     * @param params Initialize the PreparedStatement's IN parameters with
     *               this array.
     * @param rsh    The handler used to create the result object from
     *               the <code>ResultSet</code>.
     * @return An object generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @deprecated Use {@link #select(String, ResultSetHandler, Object...)}
     */
    @Deprecated
    public <T> T select(String sql, Object[] params, ResultSetHandler<T> rsh) {
        Connection conn = this.prepareConnection();

        return this.<T>select(conn, true, sql, rsh, params);
    }

    /**
     * Executes the given SELECT SQL query and returns a result object.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param <T>    The type of object that the handler returns
     * @param sql    The SQL statement to execute.
     * @param rsh    The handler used to create the result object from
     *               the <code>ResultSet</code>.
     * @param params Initialize the PreparedStatement's IN parameters with
     *               this array.
     * @return An object generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public <T> T select(String sql, ResultSetHandler<T> rsh, Object... params) {
        Connection conn = this.prepareConnection();

        return this.<T>select(conn, true, sql, rsh, params);
    }

    /**
     * Executes the given SELECT SQL without any replacement parameters.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     *            the <code>ResultSet</code>.
     * @return An object generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public <T> T select(String sql, ResultSetHandler<T> rsh) {
        Connection conn = this.prepareConnection();

        return this.<T>select(conn, true, sql, rsh, (Object[]) null);
    }

    /**
     * Calls query after checking the parameters to ensure nothing is null.
     *
     * @param conn      The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param params    An array of query replacement parameters.  Each row in
     *                  this array is one set of batch replacement values.
     * @return The results of the query.
     * @throws SQLRuntimeException If there are database or parameter errors.
     */
    private <T> T select(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params) {
        if (conn == null) {
            throw new SQLRuntimeException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rs = this.wrap(stmt.executeQuery());
            result = rsh.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            try {
                close(rs);
            } finally {
                close(stmt);
                if (closeConn) {
                    close(conn);
                }
            }
        }

        return result;
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters.
     *
     * @param conn The connection to use to run the query.
     * @param sql  The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public int update(Connection conn, String sql){
        return this.update(conn, false, sql, (Object[]) null);
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query with a single replacement
     * parameter.
     *
     * @param conn  The connection to use to run the query.
     * @param sql   The SQL to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public int update(Connection conn, String sql, Object param) {
        return this.update(conn, false, sql, new Object[]{param});
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn   The connection to use to run the query.
     * @param sql    The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public int update(Connection conn, String sql, Object... params) {
        return update(conn, false, sql, params);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement without
     * any replacement parameters. The <code>Connection</code> is retrieved
     * from the <code>DataSource</code> set in the constructor.  This
     * <code>Connection</code> must be in auto-commit mode or the update will
     * not be saved.
     *
     * @param sql The SQL statement to execute.
     * @return The number of rows updated.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public int update(String sql) {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, (Object[]) null);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement with
     * a single replacement parameter.  The <code>Connection</code> is
     * retrieved from the <code>DataSource</code> set in the constructor.
     * This <code>Connection</code> must be in auto-commit mode or the
     * update will not be saved.
     *
     * @param sql   The SQL statement to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public int update(String sql, Object param) {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, new Object[]{param});
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.  The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the update will not be saved.
     *
     * @param sql    The SQL statement to execute.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     *               parameters.
     * @return The number of rows updated.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public int update(String sql, Object... params) {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, params);
    }

    /**
     * Calls update after checking the parameters to ensure nothing is null.
     *
     * @param conn      The connection to use for the update call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param params    An array of update replacement parameters.  Each row in
     *                  this array is one set of update replacement values.
     * @return The number of rows updated.
     * @throws SQLRuntimeException If there are database or parameter errors.
     */
    private int update(Connection conn, boolean closeConn, String sql, Object... params) {
        if (conn == null) {
            throw new SQLRuntimeException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null SQL statement");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }

    /**
     * Executes the given INSERT SQL without any replacement parameters.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     *
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     *            the <code>ResultSet</code> of auto-generated keys.
     * @return An object generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(String sql, ResultSetHandler<T> rsh) {
        return insert(this.prepareConnection(), true, sql, rsh, (Object[]) null);
    }

    /**
     * Executes the given INSERT SQL statement. The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the insert will not be saved.
     *
     * @param <T>    The type of object that the handler returns
     * @param sql    The SQL statement to execute.
     * @param rsh    The handler used to create the result object from
     *               the <code>ResultSet</code> of auto-generated keys.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     * @return An object generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... params) {
        return insert(this.prepareConnection(), true, sql, rsh, params);
    }

    /**
     * Execute an SQL INSERT query without replacement parameters.
     *
     * @param <T>  The type of object that the handler returns
     * @param conn The connection to use to run the query.
     * @param sql  The SQL to execute.
     * @param rsh  The handler used to create the result object from
     *             the <code>ResultSet</code> of auto-generated keys.
     * @return An object generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh) {
        return insert(conn, false, sql, rsh, (Object[]) null);
    }

    /**
     * Execute an SQL INSERT query.
     *
     * @param <T>    The type of object that the handler returns
     * @param conn   The connection to use to run the query.
     * @param sql    The SQL to execute.
     * @param rsh    The handler used to create the result object from
     *               the <code>ResultSet</code> of auto-generated keys.
     * @param params The query replacement parameters.
     * @return An object generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) {
        return insert(conn, false, sql, rsh, params);
    }

    /**
     * Executes the given INSERT SQL statement.
     *
     * @param conn      The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param rsh       The handler used to create the result object from
     *                  the <code>ResultSet</code> of auto-generated keys.
     * @param params    The query replacement parameters.
     * @return An object generated by the handler.
     * @throws SQLRuntimeException If there are database or parameter errors.
     * @since 1.6
     */
    private <T> T insert(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params) {
        if (conn == null) {
            throw new SQLRuntimeException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        T generatedKeys = null;

        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            generatedKeys = rsh.handle(resultSet);
        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return generatedKeys;
    }

    /**
     * Executes the given batch of INSERT SQL statements. The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the insert will not be saved.
     *
     * @param <T>    The type of object that the handler returns
     * @param sql    The SQL statement to execute.
     * @param rsh    The handler used to create the result object from
     *               the <code>ResultSet</code> of auto-generated keys.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     * @return The result generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @since 1.6
     */
    public <T> T insertBatch(String sql, ResultSetHandler<T> rsh, Object[][] params) {
        return insertBatch(this.prepareConnection(), true, sql, rsh, params);
    }

    /**
     * Executes the given batch of INSERT SQL statements.
     *
     * @param <T>    The type of object that the handler returns
     * @param conn   The connection to use to run the query.
     * @param sql    The SQL to execute.
     * @param rsh    The handler used to create the result object from
     *               the <code>ResultSet</code> of auto-generated keys.
     * @param params The query replacement parameters.
     * @return The result generated by the handler.
     * @throws SQLRuntimeException if a database access error occurs
     * @since 1.6
     */
    public <T> T insertBatch(Connection conn, String sql, ResultSetHandler<T> rsh, Object[][] params) {
        return insertBatch(conn, false, sql, rsh, params);
    }

    /**
     * Executes the given batch of INSERT SQL statements.
     *
     * @param conn      The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param rsh       The handler used to create the result object from
     *                  the <code>ResultSet</code> of auto-generated keys.
     * @param params    The query replacement parameters.
     * @return The result generated by the handler.
     * @throws SQLRuntimeException If there are database or parameter errors.
     * @since 1.6
     */
    private <T> T insertBatch(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object[][] params) {
        if (conn == null) {
            throw new SQLRuntimeException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null SQL statement");
        }

        if (params == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null parameters. If parameters aren't need, pass an empty array.");
        }

        PreparedStatement stmt = null;
        T generatedKeys = null;
        try {
            stmt = this.prepareStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++) {
                this.fillStatement(stmt, params[i]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            ResultSet rs = stmt.getGeneratedKeys();
            generatedKeys = rsh.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql, (Object[]) params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return generatedKeys;
    }

    /**
     * Execute an SQL statement, including a stored procedure call, which does
     * not return any result sets.
     * Any parameters which are instances of {@link OutParameter} will be
     * registered as OUT parameters.
     * <p>
     * Use this method when invoking a stored procedure with OUT parameters
     * that does not return any result sets.  If you are not invoking a stored
     * procedure, or the stored procedure has no OUT parameters, consider using
     * {@link #update(Connection, String, Object...) }.
     * If the stored procedure returns result sets, use
     * {@link #execute(Connection, String, idealist.dao.ResultSetHandler, Object...) }.
     *
     * @param conn   The connection to use to run the query.
     * @param sql    The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public int execute(Connection conn, String sql, Object... params) {
        return this.execute(conn, false, sql, params);
    }

    /**
     * Execute an SQL statement, including a stored procedure call, which does
     * not return any result sets.
     * Any parameters which are instances of {@link OutParameter} will be
     * registered as OUT parameters.
     * <p>
     * Use this method when invoking a stored procedure with OUT parameters
     * that does not return any result sets.  If you are not invoking a stored
     * procedure, or the stored procedure has no OUT parameters, consider using
     * {@link #update(String, Object...) }.
     * If the stored procedure returns result sets, use
     * {@link #execute(String, idealist.dao.ResultSetHandler, Object...) }.
     * <p>
     * The <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the update will not be saved.
     *
     * @param sql    The SQL statement to execute.
     * @param params Initializes the CallableStatement's parameters (i.e. '?').
     * @return The number of rows updated.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public int execute(String sql, Object... params) {
        Connection conn = this.prepareConnection();

        return this.execute(conn, true, sql, params);
    }

    /**
     * Execute an SQL statement, including a stored procedure call, which
     * returns one or more result sets.
     * Any parameters which are instances of {@link OutParameter} will be
     * registered as OUT parameters.
     * <p>
     * Use this method when: a) running SQL statements that return multiple
     * result sets; b) invoking a stored procedure that return result
     * sets and OUT parameters.  Otherwise you may wish to use
     * {@link #select(Connection, String, idealist.dao.ResultSetHandler, Object...) }
     * (if there are no OUT parameters) or
     * {@link #execute(Connection, String, Object...) }
     * (if there are no result sets).
     *
     * @param <T>    The type of object that the handler returns
     * @param conn   The connection to use to run the query.
     * @param sql    The SQL to execute.
     * @param rsh    The result set handler
     * @param params The query replacement parameters.
     * @return A list of objects generated by the handler
     * @throws SQLRuntimeException if a database access error occurs
     */
    public <T> List<T> execute(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) {
        return this.execute(conn, false, sql, rsh, params);
    }

    /**
     * Execute an SQL statement, including a stored procedure call, which
     * returns one or more result sets.
     * Any parameters which are instances of {@link OutParameter} will be
     * registered as OUT parameters.
     * <p>
     * Use this method when: a) running SQL statements that return multiple
     * result sets; b) invoking a stored procedure that return result
     * sets and OUT parameters.  Otherwise you may wish to use
     * {@link #select(String, idealist.dao.ResultSetHandler, Object...) }
     * (if there are no OUT parameters) or
     * {@link #execute(String, Object...) }
     * (if there are no result sets).
     *
     * @param <T>    The type of object that the handler returns
     * @param sql    The SQL to execute.
     * @param rsh    The result set handler
     * @param params The query replacement parameters.
     * @return A list of objects generated by the handler
     * @throws SQLRuntimeException if a database access error occurs
     */
    public <T> List<T> execute(String sql, ResultSetHandler<T> rsh, Object... params) {
        Connection conn = this.prepareConnection();

        return this.execute(conn, true, sql, rsh, params);
    }

    /**
     * Invokes the stored procedure via update after checking the parameters to
     * ensure nothing is null.
     *
     * @param conn      The connection to use for the update call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param params    An array of update replacement parameters.  Each row in
     *                  this array is one set of update replacement values.
     * @return The number of rows updated.
     * @throws SQLRuntimeException If there are database or parameter errors.
     */
    private int execute(Connection conn, boolean closeConn, String sql, Object... params) {
        if (conn == null) {
            throw new SQLRuntimeException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null SQL statement");
        }

        CallableStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareCall(conn, sql);
            this.fillStatement(stmt, params);
            stmt.execute();
            rows = stmt.getUpdateCount();
            this.retrieveOutParameters(stmt, params);

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }

    /**
     * Invokes the stored procedure via update after checking the parameters to
     * ensure nothing is null.
     *
     * @param conn      The connection to use for the update call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param rsh       The result set handler
     * @param params    An array of update replacement parameters.  Each row in
     *                  this array is one set of update replacement values.
     * @return List of all objects generated by the ResultSetHandler for all result sets handled.
     * @throws SQLRuntimeException If there are database or parameter errors.
     */
    private <T> List<T> execute(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params) {
        if (conn == null) {
            throw new SQLRuntimeException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLRuntimeException("Null ResultSetHandler");
        }

        CallableStatement stmt = null;
        List<T> results = new LinkedList<T>();

        try {
            stmt = this.prepareCall(conn, sql);
            this.fillStatement(stmt, params);
            boolean moreResultSets = stmt.execute();
            // Handle multiple result sets by passing them through the handler
            // retaining the final result
            ResultSet rs = null;
            while (moreResultSets) {
                try {
                    rs = this.wrap(stmt.getResultSet());
                    results.add(rsh.handle(rs));
                    moreResultSets = stmt.getMoreResults();

                } finally {
                    close(rs);
                }
            }
            this.retrieveOutParameters(stmt, params);

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return results;
    }

    /**
     * Set the value on all the {@link OutParameter} instances in the
     * <code>params</code> array using the OUT parameter values from the
     * <code>stmt</code>.
     *
     * @param stmt   the statement from which to retrieve OUT parameter values
     * @param params the parameter array for the statement invocation
     * @throws SQLRuntimeException when the value could not be retrieved from the
     *                      statement.
     */
    private void retrieveOutParameters(CallableStatement stmt, Object[] params) {
        try {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof OutParameter) {
                        ((OutParameter) params[i]).setValue(stmt, i + 1);
                    }
                }
            }
        } catch (SQLException throwables) {
            throw new SQLRuntimeException(throwables);
        }
    }
}
