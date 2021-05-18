package idealist.dao.wrappers;

import idealist.dao.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * Wraps a <code>ResultSet</code> to trim strings returned by the
 * <code>getString()</code> and <code>getObject()</code> methods.
 *
 * <p>
 * Usage Example:
 * This example shows how to decorate ResultSets so processing continues as
 * normal but all Strings are trimmed before being returned from the
 * <code>ResultSet</code>.
 * </p>
 *
 * <pre>
 * ResultSet rs = // somehow get a ResultSet;
 *
 * // Substitute wrapped ResultSet with additional behavior for real ResultSet
 * rs = StringTrimmedResultSet.wrap(rs);
 *
 * // Pass wrapped ResultSet to processor
 * List list = new BasicRowProcessor().toBeanList(rs);
 * </pre>
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:59:05
 */
public class StringTrimmedResultSet implements InvocationHandler {

    /**
     * The factory to create proxies with.
     */
    private static final ProxyFactory factory = ProxyFactory.instance();
    /**
     * The wrapped result.
     */
    private final ResultSet rs;

    /**
     * Constructs a new instance of <code>StringTrimmedResultSet</code>
     * to wrap the specified <code>ResultSet</code>.
     *
     * @param rs ResultSet to wrap
     */
    public StringTrimmedResultSet(ResultSet rs) {
        super();
        this.rs = rs;
    }

    /**
     * Wraps the <code>ResultSet</code> in an instance of this class.  This is
     * equivalent to:
     * <pre>
     * ProxyFactory.instance().createResultSet(new StringTrimmedResultSet(rs));
     * </pre>
     *
     * @param rs The <code>ResultSet</code> to wrap.
     * @return wrapped ResultSet
     */
    public static ResultSet wrap(ResultSet rs) {
        return factory.createResultSet(new StringTrimmedResultSet(rs));
    }

    /**
     * Intercept calls to the <code>getString()</code> and
     * <code>getObject()</code> methods and trim any Strings before they're
     * returned.
     *
     * @param proxy  Not used; all method calls go to the internal result set
     * @param method The method to invoke on the result set
     * @param args   The arguments to pass to the result set
     * @return string trimmed result
     * @throws Throwable error
     * @see InvocationHandler#invoke(Object, Method, Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        Object result = method.invoke(this.rs, args);

        if ((method.getName().equals("getObject")
                || method.getName().equals("getString"))
                && result instanceof String) {
            result = ((String) result).trim();
        }

        return result;
    }

}
