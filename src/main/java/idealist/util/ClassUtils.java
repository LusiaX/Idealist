package idealist.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:09:38
 */
public abstract class ClassUtils {
    public static String getResourceAsString(String fileName) {
        try (InputStream is = ClassUtils.class.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResourceAsString(String fileName, Charset charset) {
        try (InputStream is = ClassUtils.class.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is, charset); BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
