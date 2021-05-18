package idealist.web.annotation;

import java.lang.annotation.*;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:27:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Request
@Documented
public @interface Post {
    String value();
}
