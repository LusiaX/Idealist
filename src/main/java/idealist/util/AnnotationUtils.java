package idealist.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:10:52
 */
public class AnnotationUtils {
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Annotation annotation, String methodName) {
        for (Method method : annotation.annotationType().getMethods()) {
            if (method.getName().equals(methodName)) {
                try {
                    return (T) method.invoke(annotation);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new IllegalArgumentException("Could not find method " + methodName + "() in " + annotation.annotationType().getName() + " instance.");
    }
}
