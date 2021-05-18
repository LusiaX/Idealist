package idealist.dao.handlers;

import idealist.dao.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <code>ResultSetHandler</code> implementation that returns a Map.
 * <code>ResultSet</code> rows are converted into objects (Vs) which are then stored
 * in a Map under the given keys (Ks).
 * </p>
 *
 * @param <K> the type of keys maintained by the returned map
 * @param <V> the type of mapped values
 * @see ResultSetHandler
 * @since DbUtils 1.3
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:54:55
 */
public abstract class AbstractKeyedHandler<K, V> implements ResultSetHandler<Map<K, V>> {


    /**
     * Convert each row's columns into a Map and store then
     * in a <code>Map</code> under <code>ResultSet.getObject(key)</code> key.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code>, never <code>null</code>.
     * @throws SQLException if a database access error occurs
     * @see ResultSetHandler#handle(ResultSet)
     */
    @Override
    public Map<K, V> handle(ResultSet rs) throws SQLException {
        Map<K, V> result = createMap();
        while (rs.next()) {
            result.put(createKey(rs), createRow(rs));
        }
        return result;
    }

    /**
     * This factory method is called by <code>handle()</code> to create the Map
     * to store records in.  This implementation returns a <code>HashMap</code>
     * instance.
     *
     * @return Map to store records in
     */
    protected Map<K, V> createMap() {
        return new HashMap<K, V>();
    }

    /**
     * This factory method is called by <code>handle()</code> to retrieve the
     * key value from the current <code>ResultSet</code> row.
     *
     * @param rs ResultSet to create a key from
     * @return K from the configured key column name/index
     * @throws SQLException if a database access error occurs
     */
    protected abstract K createKey(ResultSet rs) throws SQLException;

    /**
     * This factory method is called by <code>handle()</code> to store the
     * current <code>ResultSet</code> row in some object.
     *
     * @param rs ResultSet to create a row from
     * @return V object created from the current row
     * @throws SQLException if a database access error occurs
     */
    protected abstract V createRow(ResultSet rs) throws SQLException;

}
