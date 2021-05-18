package idealist.dao.handlers;

import idealist.dao.ResultSetHandler;
import idealist.dao.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * <code>ResultSetHandler</code> implementation that converts the first
 * <code>ResultSet</code> row into a <code>Map</code>. This class is thread
 * safe.
 *
 * @see ResultSetHandler
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:56:41
 */
public class MapHandler implements ResultSetHandler<Map<String, Object>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of MapHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public MapHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of MapHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     *                to use when converting rows into Maps.
     */
    public MapHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Converts the first row in the <code>ResultSet</code> into a
     * <code>Map</code>.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code> with the values from the first row or
     * <code>null</code> if there are no rows in the <code>ResultSet</code>.
     * @throws SQLException if a database access error occurs
     * @see ResultSetHandler#handle(ResultSet)
     */
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }

}
