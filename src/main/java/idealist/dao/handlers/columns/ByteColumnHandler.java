package idealist.dao.handlers.columns;

import idealist.dao.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:52:21
 */
public class ByteColumnHandler implements ColumnHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Byte.TYPE) || propType.equals(Byte.class);
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }
}
