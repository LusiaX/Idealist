package idealist.dao.handlers;

import idealist.dao.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that simplify development of <code>ResultSetHandler</code>
 * classes that convert <code>ResultSet</code> into <code>List</code>.
 *
 * @param <T> the target List generic type
 * @see ResultSetHandler
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:55:08
 */
public abstract class AbstractListHandler<T> implements ResultSetHandler<List<T>> {
    /**
     * Whole <code>ResultSet</code> handler. It produce <code>List</code> as
     * result. To convert individual rows into Java objects it uses
     * <code>handleRow(ResultSet)</code> method.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return a list of all rows in the result set
     * @throws SQLException error occurs
     * @see #handleRow(ResultSet)
     */
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> rows = new ArrayList<T>();
        while (rs.next()) {
            rows.add(this.handleRow(rs));
        }
        return rows;
    }

    /**
     * Row handler. Method converts current row into some Java object.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return row processing result
     * @throws SQLException error occurs
     */
    protected abstract T handleRow(ResultSet rs) throws SQLException;
}
