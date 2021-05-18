package idealist.dao.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <code>ResultSetHandler</code> implementation that converts one
 * <code>ResultSet</code> column into a <code>List</code> of
 * <code>Object</code>s. This class is thread safe.
 *
 * @param <T> The type of the column.
 * @see idealist.dao.ResultSetHandler
 * @since DbUtils 1.1
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:56:19
 */
public class ColumnListHandler<T> extends AbstractListHandler<T> {

    /**
     * The column number to retrieve.
     */
    private final int columnIndex;

    /**
     * The column name to retrieve.  Either columnName or columnIndex
     * will be used but never both.
     */
    private final String columnName;

    /**
     * Creates a new instance of ColumnListHandler.  The first column of each
     * row will be returned from <code>handle()</code>.
     */
    public ColumnListHandler() {
        this(1, null);
    }

    /**
     * Creates a new instance of ColumnListHandler.
     *
     * @param columnIndex The index of the column to retrieve from the
     *                    <code>ResultSet</code>.
     */
    public ColumnListHandler(int columnIndex) {
        this(columnIndex, null);
    }

    /**
     * Creates a new instance of ColumnListHandler.
     *
     * @param columnName The name of the column to retrieve from the
     *                   <code>ResultSet</code>.
     */
    public ColumnListHandler(String columnName) {
        this(1, columnName);
    }

    /**
     * Private Helper
     *
     * @param columnIndex The index of the column to retrieve from the
     *                    <code>ResultSet</code>.
     * @param columnName  The name of the column to retrieve from the
     *                    <code>ResultSet</code>.
     */
    private ColumnListHandler(int columnIndex, String columnName) {
        super();
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    /**
     * Returns one <code>ResultSet</code> column value as <code>Object</code>.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return <code>Object</code>, never <code>null</code>.
     * @throws SQLException       if a database access error occurs
     * @throws ClassCastException if the class datatype does not match the column type
     * @see idealist.dao.handlers.AbstractListHandler#handle(ResultSet)
     */
    // We assume that the user has picked the correct type to match the column
    // so getObject will return the appropriate type and the cast will succeed.
    @SuppressWarnings("unchecked")
    @Override
    protected T handleRow(ResultSet rs) throws SQLException {
        if (this.columnName == null) {
            return (T) rs.getObject(this.columnIndex);
        }
        return (T) rs.getObject(this.columnName);
    }

}
