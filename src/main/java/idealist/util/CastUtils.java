package idealist.util;

import java.math.BigDecimal;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:15:36
 */
public abstract class CastUtils {
    public static String toString(Object obj) {
        return obj == null ? null : String.valueOf(obj);
    }

    public static Long toLong(String str) {
        return StringUtils.isEmpty(str) ? null : Long.parseLong(str);
    }

    public static Integer toInteger(String str) {
        return StringUtils.isEmpty(str) ? null : Integer.parseInt(str);
    }

    public static BigDecimal toBigDecimal(String str) {
        return StringUtils.isEmpty(str) ? null : new BigDecimal(str);
    }
}
