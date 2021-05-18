package idealist.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * <p>
 * Wraps a <code>ResultSet</code> in an <code>Iterator&lt;Object[]&gt;</code>.  This is useful
 * when you want to present a non-database application layer with domain
 * neutral data.
 * </p>
 *
 * <p>
 * This implementation requires the <code>ResultSet.isLast()</code> method
 * to be implemented.
 * </p>
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 01:02:03
 */
public class ResultSetIterator implements Iterator<Object[]> {

    /**
     * The wrapped <code>ResultSet</code>.
     */
    private final ResultSet rs;

    /**
     * The processor to use when converting a row into an Object[].
     */
    private final RowProcessor convert;

    /**
     * Constructor for ResultSetIterator.
     *
     * @param rs Wrap this <code>ResultSet</code> in an <code>Iterator</code>.
     */
    public ResultSetIterator(ResultSet rs) {
        this(rs, new BasicRowProcessor());
    }

    /**
     * Constructor for ResultSetIterator.
     *
     * @param rs      Wrap this <code>ResultSet</code> in an <code>Iterator</code>.
     * @param convert The processor to use when converting a row into an
     *                <code>Object[]</code>.  Defaults to a
     *                <code>BasicRowProcessor</code>.
     */
    public ResultSetIterator(ResultSet rs, RowProcessor convert) {
        this.rs = rs;
        this.convert = convert;
    }

    /**
     * Generates an <code>Iterable</code>, suitable for use in for-each loops.
     *
     * @param rs Wrap this <code>ResultSet</code> in an <code>Iterator</code>.
     * @return an <code>Iterable</code>, suitable for use in for-each loops.
     */
    public static Iterable<Object[]> iterable(final ResultSet rs) {
        return new Iterable<Object[]>() {

            @Override
            public Iterator<Object[]> iterator() {
                return new ResultSetIterator(rs);
            }

        };
    }

    /**
     * Returns true if there are more rows in the ResultSet.
     *
     * @return boolean <code>true</code> if there are more rows
     * @throws RuntimeException if an SQLException occurs.
     */
    @Override
    public boolean hasNext() {
        try {
            return !rs.isLast();
        } catch (SQLException e) {
            rethrow(e);
            return false;
        }
    }

    /**
     * Returns the next row as an <code>Object[]</code>.
     *
     * @return An <code>Object[]</code> with the same number of elements as
     * columns in the <code>ResultSet</code>.
     * @throws RuntimeException if an SQLException occurs.
     * @see Iterator#next()
     */
    @Override
    public Object[] next() {
        try {
            rs.next();
            return this.convert.toArray(rs);
        } catch (SQLException e) {
            rethrow(e);
            return null;
        }
    }

    /**
     * Deletes the current row from the <code>ResultSet</code>.
     *
     * @throws RuntimeException if an SQLException occurs.
     * @see Iterator#remove()
     */
    @Override
    public void remove() {
        try {
            this.rs.deleteRow();
        } catch (SQLException e) {
            rethrow(e);
        }
    }

    /**
     * Rethrow the SQLException as a RuntimeException.  This implementation
     * creates a new RuntimeException with the SQLException's error message.
     *
     * @param e SQLException to rethrow
     * @since DbUtils 1.1
     */
    protected void rethrow(SQLException e) {
        throw new RuntimeException(e.getMessage());
    }

}
