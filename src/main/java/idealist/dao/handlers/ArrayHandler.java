package idealist.dao.handlers;

import idealist.dao.BasicRowProcessor;
import idealist.dao.ResultSetHandler;
import idealist.dao.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into an <code>Object[]</code>. This class is
 * thread safe.
 *
 * @see ResultSetHandler
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:55:20
 */
public class ArrayHandler implements ResultSetHandler<Object[]> {

    /**
     * Singleton processor instance that handlers share to save memory.  Notice
     * the default scoping to allow only classes in this package to use this
     * instance.
     */
    static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor();

    /**
     * An empty array to return when no more rows are available in the ResultSet.
     */
    private static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * The RowProcessor implementation to use when converting rows
     * into arrays.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of ArrayHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public ArrayHandler() {
        this(ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of ArrayHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     *                to use when converting rows into arrays.
     */
    public ArrayHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Places the column values from the first row in an <code>Object[]</code>.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return An Object[]. If there are no rows in the <code>ResultSet</code>
     * an empty array will be returned.
     * @throws SQLException if a database access error occurs
     * @see ResultSetHandler#handle(ResultSet)
     */
    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : EMPTY_ARRAY;
    }

}
