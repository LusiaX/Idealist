package idealist.dao.handlers;

import idealist.dao.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into a <code>List</code> of <code>Map</code>s.
 * This class is thread safe.
 *
 * @see idealist.dao.ResultSetHandler
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:57:24
 */
public class MapListHandler extends AbstractListHandler<Map<String, Object>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of MapListHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public MapListHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of MapListHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     *                to use when converting rows into Maps.
     */
    public MapListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Converts the <code>ResultSet</code> row into a <code>Map</code> object.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code>, never null.
     * @throws SQLException if a database access error occurs
     * @see idealist.dao.handlers.AbstractListHandler#handle(ResultSet)
     */
    @Override
    protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }

}
