package idealist.dao.handlers.columns;

import idealist.dao.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:53:53
 */
public class TimestampColumnHandler implements ColumnHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Timestamp.class);
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }
}
