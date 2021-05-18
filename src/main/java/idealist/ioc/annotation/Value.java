package idealist.ioc.annotation;

import java.lang.annotation.*;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:23:35
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Value {
    String value() default "";
}
