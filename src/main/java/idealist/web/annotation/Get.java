package idealist.web.annotation;

import java.lang.annotation.*;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:26:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Request
@Documented
public @interface Get {
    String value();
}
