package idealist.web.annotation;

import java.lang.annotation.*;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:25:52
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Request
@Documented
public @interface Delete {

    String value();
}
