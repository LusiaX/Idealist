package idealist.dao;


import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;


/**
 * Provides generous name matching (e.g. underscore-aware) from DB
 * columns to Java Bean properties.
 *
 * @since 1.6
 * <p>
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 01:00:30
 */
public class GenerousBeanProcessor extends BeanProcessor {

    /**
     * Default constructor.
     */
    public GenerousBeanProcessor() {
        super();
    }

    @Override
    protected int[] mapColumnsToProperties(final ResultSetMetaData rsmd,
                                           final PropertyDescriptor[] props) throws SQLException {

        final int cols = rsmd.getColumnCount();
        final int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);

            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }

            final String generousColumnName = columnName.replace("_", "");

            for (int i = 0; i < props.length; i++) {
                final String propName = props[i].getName();

                // see if either the column name, or the generous one matches
                if (columnName.equalsIgnoreCase(propName) ||
                        generousColumnName.equalsIgnoreCase(propName)) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

}
