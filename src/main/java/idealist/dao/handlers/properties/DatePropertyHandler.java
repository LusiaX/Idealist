package idealist.dao.handlers.properties;

import idealist.dao.PropertyHandler;

import java.sql.Timestamp;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:54:17
 */
public class DatePropertyHandler implements PropertyHandler {
    @Override
    public boolean match(Class<?> parameter, Object value) {
        if (value instanceof java.util.Date) {
            final String targetType = parameter.getName();
            if ("java.sql.Date".equals(targetType)) {
                return true;
            } else if ("java.sql.Time".equals(targetType)) {
                return true;
            } else if ("java.sql.Timestamp".equals(targetType)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object apply(Class<?> parameter, Object value) {
        final String targetType = parameter.getName();
        if ("java.sql.Date".equals(targetType)) {
            value = new java.sql.Date(((java.util.Date) value).getTime());
        } else if ("java.sql.Time".equals(targetType)) {
            value = new java.sql.Time(((java.util.Date) value).getTime());
        } else if ("java.sql.Timestamp".equals(targetType)) {
            Timestamp tsValue = (Timestamp) value;
            int nanos = tsValue.getNanos();
            value = new Timestamp(tsValue.getTime());
            ((Timestamp) value).setNanos(nanos);
        }

        return value;
    }
}
