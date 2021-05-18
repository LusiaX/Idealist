package idealist.dao.handlers.properties;

import idealist.dao.PropertyHandler;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:54:29
 */
public class StringEnumPropertyHandler implements PropertyHandler {
    @Override
    public boolean match(Class<?> parameter, Object value) {
        return value instanceof String && parameter.isEnum();
    }

    @Override
    public Object apply(Class<?> parameter, Object value) {
        return Enum.valueOf(parameter.asSubclass(Enum.class), (String) value);
    }
}
