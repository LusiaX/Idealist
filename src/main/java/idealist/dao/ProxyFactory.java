package idealist.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.*;

/**
 * Creates proxy implementations of JDBC interfaces.  This avoids
 * incompatibilities between the JDBC 2 and JDBC 3 interfaces.  This class is
 * thread safe.
 *
 * @see Proxy
 * @see InvocationHandler
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 01:01:15
 */
public class ProxyFactory {

    /**
     * The Singleton instance of this class.
     */
    private static final ProxyFactory instance = new ProxyFactory();

    /**
     * Protected constructor for ProxyFactory subclasses to use.
     */
    protected ProxyFactory() {
        super();
    }

    /**
     * Returns the Singleton instance of this class.
     *
     * @return singleton instance
     */
    public static ProxyFactory instance() {
        return instance;
    }

    /**
     * Convenience method to generate a single-interface proxy using the handler's classloader
     *
     * @param <T>     The type of object to proxy
     * @param type    The type of object to proxy
     * @param handler The handler that intercepts/overrides method calls.
     * @return proxied object
     */
    public <T> T newProxyInstance(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class<?>[]{type}, handler));
    }

    /**
     * Creates a new proxy <code>CallableStatement</code> object.
     *
     * @param handler The handler that intercepts/overrides method calls.
     * @return proxied CallableStatement
     */
    public CallableStatement createCallableStatement(InvocationHandler handler) {
        return newProxyInstance(CallableStatement.class, handler);
    }

    /**
     * Creates a new proxy <code>Connection</code> object.
     *
     * @param handler The handler that intercepts/overrides method calls.
     * @return proxied Connection
     */
    public Connection createConnection(InvocationHandler handler) {
        return newProxyInstance(Connection.class, handler);
    }

    /**
     * Creates a new proxy <code>Driver</code> object.
     *
     * @param handler The handler that intercepts/overrides method calls.
     * @return proxied Driver
     */
    public Driver createDriver(InvocationHandler handler) {
        return newProxyInstance(Driver.class, handler);
    }

    /**
     * Creates a new proxy <code>PreparedStatement</code> object.
     *
     * @param handler The handler that intercepts/overrides method calls.
     * @return proxied PreparedStatement
     */
    public PreparedStatement createPreparedStatement(InvocationHandler handler) {
        return newProxyInstance(PreparedStatement.class, handler);
    }

    /**
     * Creates a new proxy <code>ResultSet</code> object.
     *
     * @param handler The handler that intercepts/overrides method calls.
     * @return proxied ResultSet
     */
    public ResultSet createResultSet(InvocationHandler handler) {
        return newProxyInstance(ResultSet.class, handler);
    }

    /**
     * Creates a new proxy <code>ResultSetMetaData</code> object.
     *
     * @param handler The handler that intercepts/overrides method calls.
     * @return proxied ResultSetMetaData
     */
    public ResultSetMetaData createResultSetMetaData(InvocationHandler handler) {
        return newProxyInstance(ResultSetMetaData.class, handler);
    }

    /**
     * Creates a new proxy <code>Statement</code> object.
     *
     * @param handler The handler that intercepts/overrides method calls.
     * @return proxied Statement
     */
    public Statement createStatement(InvocationHandler handler) {
        return newProxyInstance(Statement.class, handler);
    }

}
