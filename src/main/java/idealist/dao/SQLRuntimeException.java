package idealist.dao;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 01:09:21
 */
public class SQLRuntimeException extends RuntimeException {
    public SQLRuntimeException() {
        super();
    }

    public SQLRuntimeException(String message) {
        super(message);
    }

    public SQLRuntimeException(Throwable cause) {
        super(cause);
    }

    public SQLRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
