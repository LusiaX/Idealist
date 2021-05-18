package idealist.dao.handlers;

import idealist.dao.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <code>ResultSetHandler</code> implementation that converts the
 * <code>ResultSet</code> into a <code>List</code> of <code>Object[]</code>s.
 * This class is thread safe.
 *
 * @see idealist.dao.ResultSetHandler
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:55:32
 */
public class ArrayListHandler extends AbstractListHandler<Object[]> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Object[]s.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of ArrayListHandler using a
     * <code>BasicRowProcessor</code> for conversions.
     */
    public ArrayListHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of ArrayListHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     *                to use when converting rows into Object[]s.
     */
    public ArrayListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }


    /**
     * Convert row's columns into an <code>Object[]</code>.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return <code>Object[]</code>, never <code>null</code>.
     * @throws SQLException if a database access error occurs
     * @see idealist.dao.handlers.AbstractListHandler#handle(ResultSet)
     */
    @Override
    protected Object[] handleRow(ResultSet rs) throws SQLException {
        return this.convert.toArray(rs);
    }

}
