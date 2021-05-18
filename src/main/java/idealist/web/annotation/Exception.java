package idealist.web.annotation;

import idealist.ioc.annotation.Component;

import java.lang.annotation.*;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:26:24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Exception {
    Class<? extends java.lang.Exception> target();
}
