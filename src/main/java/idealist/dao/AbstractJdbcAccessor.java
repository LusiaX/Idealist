package idealist.dao;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Arrays;

/**
 * The base class for JdbcAccessor &amp; AsyncJdbcAccessor. This class is thread safe.
 *
 * @since 1.4 (mostly extracted from JdbcAccessor)
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:59:18
 */
public abstract class AbstractJdbcAccessor {
    /**
     * The DataSource to retrieve connections from.
     *
     * @deprecated Access to this field should be through {@link #getDataSource()}.
     */
    @Deprecated
    protected final DataSource ds;
    /**
     * Configuration to use when preparing statements.
     */
    private final StatementConfiguration stmtConfig;
    /**
     * Is {@link ParameterMetaData#getParameterType(int)} broken (have we tried
     * it yet)?
     */
    private volatile boolean pmdKnownBroken = false;

    /**
     * Default constructor, sets pmdKnownBroken to false, ds to null and stmtConfig to null.
     */
    public AbstractJdbcAccessor() {
        ds = null;
        this.stmtConfig = null;
    }

    /**
     * Constructor to control the use of <code>ParameterMetaData</code>.
     *
     * @param pmdKnownBroken Some drivers don't support
     *                       {@link ParameterMetaData#getParameterType(int) }; if
     *                       <code>pmdKnownBroken</code> is set to true, we won't even try
     *                       it; if false, we'll try it, and if it breaks, we'll remember
     *                       not to use it again.
     */
    public AbstractJdbcAccessor(boolean pmdKnownBroken) {
        this.pmdKnownBroken = pmdKnownBroken;
        ds = null;
        this.stmtConfig = null;
    }

    /**
     * Constructor to provide a <code>DataSource</code>. Methods that do not
     * take a <code>Connection</code> parameter will retrieve connections from
     * this <code>DataSource</code>.
     *
     * @param ds The <code>DataSource</code> to retrieve connections from.
     */
    public AbstractJdbcAccessor(DataSource ds) {
        this.ds = ds;
        this.stmtConfig = null;
    }

    /**
     * Constructor for JdbcAccessor that takes a <code>StatementConfiguration</code> to configure statements when
     * preparing them.
     *
     * @param stmtConfig The configuration to apply to statements when they are prepared.
     */
    public AbstractJdbcAccessor(StatementConfiguration stmtConfig) {
        this.ds = null;
        this.stmtConfig = stmtConfig;
    }

    /**
     * Constructor to provide a <code>DataSource</code> and control the use of
     * <code>ParameterMetaData</code>. Methods that do not take a
     * <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds             The <code>DataSource</code> to retrieve connections from.
     * @param pmdKnownBroken Some drivers don't support
     *                       {@link ParameterMetaData#getParameterType(int) }; if
     *                       <code>pmdKnownBroken</code> is set to true, we won't even try
     *                       it; if false, we'll try it, and if it breaks, we'll remember
     *                       not to use it again.
     */
    public AbstractJdbcAccessor(DataSource ds, boolean pmdKnownBroken) {
        this.pmdKnownBroken = pmdKnownBroken;
        this.ds = ds;
        this.stmtConfig = null;
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
    public AbstractJdbcAccessor(DataSource ds, StatementConfiguration stmtConfig) {
        this.ds = ds;
        this.stmtConfig = stmtConfig;
    }

    /**
     * Constructor for JdbcAccessor that takes a <code>DataSource</code>, a <code>StatementConfiguration</code>, and
     * controls the use of <code>ParameterMetaData</code>.  Methods that do not take a <code>Connection</code> parameter
     * will retrieve connections from this <code>DataSource</code>.
     *
     * @param ds             The <code>DataSource</code> to retrieve connections from.
     * @param pmdKnownBroken Some drivers don't support {@link ParameterMetaData#getParameterType(int) };
     *                       if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     *                       and if it breaks, we'll remember not to use it again.
     * @param stmtConfig     The configuration to apply to statements when they are prepared.
     */
    public AbstractJdbcAccessor(DataSource ds, boolean pmdKnownBroken, StatementConfiguration stmtConfig) {
        this.pmdKnownBroken = pmdKnownBroken;
        this.ds = ds;
        this.stmtConfig = stmtConfig;
    }

    /**
     * Returns the <code>DataSource</code> this runner is using.
     * <code>JdbcAccessor</code> methods always call this method to get the
     * <code>DataSource</code> so subclasses can provide specialized behavior.
     *
     * @return DataSource the runner is using
     */
    public DataSource getDataSource() {
        return this.ds;
    }

    /**
     * Some drivers don't support
     * {@link ParameterMetaData#getParameterType(int) }; if
     * <code>pmdKnownBroken</code> is set to true, we won't even try it; if
     * false, we'll try it, and if it breaks, we'll remember not to use it
     * again.
     *
     * @return the flag to skip (or not)
     * {@link ParameterMetaData#getParameterType(int) }
     * @since 1.4
     */
    public boolean isPmdKnownBroken() {
        return pmdKnownBroken;
    }

    /**
     * Factory method that creates and initializes a
     * <code>PreparedStatement</code> object for the given SQL.
     * <code>JdbcAccessor</code> methods always call this method to prepare
     * statements for them. Subclasses can override this method to provide
     * special PreparedStatement configuration if needed. This implementation
     * simply calls <code>conn.prepareStatement(sql)</code>.
     *
     * @param conn The <code>Connection</code> used to create the
     *             <code>PreparedStatement</code>
     * @param sql  The SQL statement to prepare.
     * @return An initialized <code>PreparedStatement</code>.
     * @throws SQLException if a database access error occurs
     */
    protected PreparedStatement prepareStatement(Connection conn, String sql)
            throws SQLException {

        PreparedStatement ps = conn.prepareStatement(sql);
        try {
            configureStatement(ps);
        } catch (SQLException e) {
            ps.close();
            throw e;
        }
        return ps;
    }

    /**
     * Factory method that creates and initializes a
     * <code>PreparedStatement</code> object for the given SQL.
     * <code>JdbcAccessor</code> methods always call this method to prepare
     * statements for them. Subclasses can override this method to provide
     * special PreparedStatement configuration if needed. This implementation
     * simply calls <code>conn.prepareStatement(sql, returnedKeys)</code>
     * which will result in the ability to retrieve the automatically-generated
     * keys from an auto_increment column.
     *
     * @param conn         The <code>Connection</code> used to create the
     *                     <code>PreparedStatement</code>
     * @param sql          The SQL statement to prepare.
     * @param returnedKeys Flag indicating whether to return generated keys or not.
     * @return An initialized <code>PreparedStatement</code>.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    protected PreparedStatement prepareStatement(Connection conn, String sql, int returnedKeys)
            throws SQLException {

        PreparedStatement ps = conn.prepareStatement(sql, returnedKeys);
        try {
            configureStatement(ps);
        } catch (SQLException e) {
            ps.close();
            throw e;
        }
        return ps;
    }

    private void configureStatement(Statement stmt) throws SQLException {

        if (stmtConfig != null) {
            if (stmtConfig.isFetchDirectionSet()) {
                stmt.setFetchDirection(stmtConfig.getFetchDirection());
            }

            if (stmtConfig.isFetchSizeSet()) {
                stmt.setFetchSize(stmtConfig.getFetchSize());
            }

            if (stmtConfig.isMaxFieldSizeSet()) {
                stmt.setMaxFieldSize(stmtConfig.getMaxFieldSize());
            }

            if (stmtConfig.isMaxRowsSet()) {
                stmt.setMaxRows(stmtConfig.getMaxRows());
            }

            if (stmtConfig.isQueryTimeoutSet()) {
                stmt.setQueryTimeout(stmtConfig.getQueryTimeout());
            }
        }
    }

    /**
     * Factory method that creates and initializes a
     * <code>CallableStatement</code> object for the given SQL.
     * <code>JdbcAccessor</code> methods always call this method to prepare
     * callable statements for them. Subclasses can override this method to
     * provide special CallableStatement configuration if needed. This
     * implementation simply calls <code>conn.prepareCall(sql)</code>.
     *
     * @param conn The <code>Connection</code> used to create the
     *             <code>CallableStatement</code>
     * @param sql  The SQL statement to prepare.
     * @return An initialized <code>CallableStatement</code>.
     * @throws SQLException if a database access error occurs
     */
    protected CallableStatement prepareCall(Connection conn, String sql)
            throws SQLException {

        return conn.prepareCall(sql);
    }

    /**
     * Factory method that creates and initializes a <code>Connection</code>
     * object. <code>JdbcAccessor</code> methods always call this method to
     * retrieve connections from its DataSource. Subclasses can override this
     * method to provide special <code>Connection</code> configuration if
     * needed. This implementation simply calls <code>ds.getConnection()</code>.
     *
     * @return An initialized <code>Connection</code>.
     * @throws SQLRuntimeException if a database access error occurs
     * @since DbUtils 1.1
     */
    protected Connection prepareConnection() {
        if (this.getDataSource() == null) {
            throw new SQLRuntimeException(
                    "JdbcAccessor requires a DataSource to be "
                            + "invoked in this way, or a Connection should be passed in");
        }
        try {
            return this.getDataSource().getConnection();
        } catch (SQLException throwables) {
            throw new SQLRuntimeException(throwables);
        }
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given objects.
     *
     * @param stmt   PreparedStatement to fill
     * @param params Query replacement parameters; <code>null</code> is a valid
     *               value to pass in.
     * @throws SQLRuntimeException if a database access error occurs
     */
    public void fillStatement(PreparedStatement stmt, Object... params) {

        try {
            // check the parameter count, if we can
            ParameterMetaData pmd = null;
            if (!pmdKnownBroken) {
                try {
                    pmd = stmt.getParameterMetaData();
                    if (pmd == null) { // can be returned by implementations that don't support the method
                        pmdKnownBroken = true;
                    } else {
                        int stmtCount = pmd.getParameterCount();
                        int paramsCount = params == null ? 0 : params.length;

                        if (stmtCount != paramsCount) {
                            throw new SQLRuntimeException("Wrong number of parameters: expected "
                                    + stmtCount + ", was given " + paramsCount);
                        }
                    }
                } catch (SQLFeatureNotSupportedException ex) {
                    pmdKnownBroken = true;
                }
                // TODO see DBUTILS-117: would it make sense to catch any other SQLEx types here?
            }

            // nothing to do here
            if (params == null) {
                return;
            }

            CallableStatement call = null;
            if (stmt instanceof CallableStatement) {
                call = (CallableStatement) stmt;
            }

            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    if (call != null && params[i] instanceof OutParameter) {
                        ((OutParameter) params[i]).register(call, i + 1);
                    } else {
                        stmt.setObject(i + 1, params[i]);
                    }
                } else {
                    // VARCHAR works with many drivers regardless
                    // of the actual column type. Oddly, NULL and
                    // OTHER don't work with Oracle's drivers.
                    int sqlType = Types.VARCHAR;
                    if (!pmdKnownBroken) {
                        // TODO see DBUTILS-117: does it make sense to catch SQLEx here?
                        try {
                            /*
                             * It's not possible for pmdKnownBroken to change from
                             * true to false, (once true, always true) so pmd cannot
                             * be null here.
                             */
                            sqlType = pmd.getParameterType(i + 1);
                        } catch (SQLException e) {
                            pmdKnownBroken = true;
                        }
                    }
                    stmt.setNull(i + 1, sqlType);
                }
            }
        } catch (SQLException throwables) {
            throw new SQLRuntimeException(throwables);
        }
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given object's bean property values.
     *
     * @param stmt       PreparedStatement to fill
     * @param bean       a JavaBean object
     * @param properties an ordered array of properties; this gives the order to insert
     *                   values in the statement
     * @throws SQLException if a database access error occurs
     */
    public void fillStatementWithBean(PreparedStatement stmt, Object bean,
                                      PropertyDescriptor[] properties) throws SQLException {
        Object[] params = new Object[properties.length];
        for (int i = 0; i < properties.length; i++) {
            PropertyDescriptor property = properties[i];
            Object value = null;
            Method method = property.getReadMethod();
            if (method == null) {
                throw new RuntimeException("No read method for bean property "
                        + bean.getClass() + " " + property.getName());
            }
            try {
                value = method.invoke(bean, new Object[0]);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Couldn't invoke method: " + method,
                        e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(
                        "Couldn't invoke method with 0 arguments: " + method, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't invoke method: " + method,
                        e);
            }
            params[i] = value;
        }
        fillStatement(stmt, params);
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given object's bean property values.
     *
     * @param stmt          PreparedStatement to fill
     * @param bean          A JavaBean object
     * @param propertyNames An ordered array of property names (these should match the
     *                      getters/setters); this gives the order to insert values in the
     *                      statement
     * @throws SQLException If a database access error occurs
     */
    public void fillStatementWithBean(PreparedStatement stmt, Object bean,
                                      String... propertyNames) throws SQLException {
        PropertyDescriptor[] descriptors;
        try {
            descriptors = Introspector.getBeanInfo(bean.getClass())
                    .getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException("Couldn't introspect bean "
                    + bean.getClass().toString(), e);
        }
        PropertyDescriptor[] sorted = new PropertyDescriptor[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            if (propertyName == null) {
                throw new NullPointerException("propertyName can't be null: "
                        + i);
            }
            boolean found = false;
            for (PropertyDescriptor descriptor : descriptors) {
                if (propertyName.equals(descriptor.getName())) {
                    sorted[i] = descriptor;
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("Couldn't find bean property: "
                        + bean.getClass() + " " + propertyName);
            }
        }
        fillStatementWithBean(stmt, bean, sorted);
    }

    /**
     * Throws a new exception with a more informative error message.
     *
     * @param cause  The original exception that will be chained to the new
     *               exception when it's rethrown.
     * @param sql    The query that was executing when the exception happened.
     * @param params The query replacement parameters; <code>null</code> is a valid
     *               value to pass in.
     * @throws SQLRuntimeException if a database access error occurs
     */
    protected void rethrow(SQLException cause, String sql, Object... params) {

        String causeMessage = cause.getMessage();
        if (causeMessage == null) {
            causeMessage = "";
        }
        StringBuilder msg = new StringBuilder(causeMessage);

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (params == null) {
            msg.append("[]");
        } else {
            msg.append(Arrays.deepToString(params));
        }

        SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
                cause.getErrorCode());
        e.setNextException(cause);

        throw new SQLRuntimeException(e);
    }

    /**
     * Wrap the <code>ResultSet</code> in a decorator before processing it. This
     * implementation returns the <code>ResultSet</code> it is given without any
     * decoration.
     *
     * <p>
     * Often, the implementation of this method can be done in an anonymous
     * inner class like this:
     * </p>
     *
     * <pre>
     * JdbcAccessor run = new JdbcAccessor() {
     *     protected ResultSet wrap(ResultSet rs) {
     *         return StringTrimmedResultSet.wrap(rs);
     *     }
     * };
     * </pre>
     *
     * @param rs The <code>ResultSet</code> to decorate; never
     *           <code>null</code>.
     * @return The <code>ResultSet</code> wrapped in some decorator.
     */
    protected ResultSet wrap(ResultSet rs) {
        return rs;
    }

    /**
     * Close a <code>Connection</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param conn Connection to close
     * @throws SQLRuntimeException if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(Connection conn) {
        JdbcUtils.close(conn);
    }

    /**
     * Close a <code>Statement</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param stmt Statement to close
     * @throws SQLRuntimeException if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(Statement stmt) {
        JdbcUtils.close(stmt);
    }

    /**
     * Close a <code>ResultSet</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param rs ResultSet to close
     * @throws SQLRuntimeException if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(ResultSet rs) {
        JdbcUtils.close(rs);
    }

}
