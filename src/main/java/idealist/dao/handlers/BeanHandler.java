package idealist.dao.handlers;

import idealist.dao.ResultSetHandler;
import idealist.dao.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <code>ResultSetHandler</code> implementation that converts the first
 * <code>ResultSet</code> row into a JavaBean. This class is thread safe.
 *
 * @param <T> the target bean type
 * @see ResultSetHandler
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:55:45
 */
public class BeanHandler<T> implements ResultSetHandler<T> {

    /**
     * The Class of beans produced by this handler.
     */
    private final Class<? extends T> type;

    /**
     * The RowProcessor implementation to use when converting rows
     * into beans.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of BeanHandler.
     *
     * @param type The Class that objects returned from <code>handle()</code>
     *             are created from.
     */
    public BeanHandler(Class<? extends T> type) {
        this(type, ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of BeanHandler.
     *
     * @param type    The Class that objects returned from <code>handle()</code>
     *                are created from.
     * @param convert The <code>RowProcessor</code> implementation
     *                to use when converting rows into beans.
     */
    public BeanHandler(Class<? extends T> type, RowProcessor convert) {
        this.type = type;
        this.convert = convert;
    }

    /**
     * Convert the first row of the <code>ResultSet</code> into a bean with the
     * <code>Class</code> given in the constructor.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return An initialized JavaBean or <code>null</code> if there were no
     * rows in the <code>ResultSet</code>.
     * @throws SQLException if a database access error occurs
     * @see ResultSetHandler#handle(ResultSet)
     */
    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toBean(rs, this.type) : null;
    }

}
