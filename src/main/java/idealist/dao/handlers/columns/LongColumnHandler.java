package idealist.dao.handlers.columns;

import idealist.dao.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:53:10
 */
public class LongColumnHandler implements ColumnHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Long.TYPE) || propType.equals(Long.class);
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        return Long.valueOf(rs.getLong(columnIndex));
    }
}
