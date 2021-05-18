package idealist.dao.handlers.columns;

import idealist.dao.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:53:32
 */
public class SQLXMLColumnHandler implements ColumnHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(SQLXML.class);
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }
}
