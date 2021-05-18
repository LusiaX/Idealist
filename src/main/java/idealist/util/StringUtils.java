package idealist.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:06:41
 */
public abstract class StringUtils {
    private static final int PAD_LIMIT = 8192;
    private static final Pattern CAMEL_UNDERLINE_PATTERN = Pattern.compile("[A-Z]([a-z\\d]+)?");
    private static final Pattern UNDERLINE_CAMEL_PATTERN = Pattern.compile("([A-Za-z\\d]+)(_)?");

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isAllEmpty(String... strings) {
        for (String str : strings) {
            if (isNotEmpty(str)) return false;
        }
        return true;
    }

    public static boolean isAnyEmpty(String... strings) {
        for (String str : strings) {
            if (isEmpty(str)) return true;
        }
        return false;
    }

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isAllBlank(String... strings) {
        for (String str : strings) {
            if (isNotBlank(str)) return false;
        }
        return true;
    }

    public static boolean isAnyBlank(String... strings) {
        for (String str : strings) {
            if (isBlank(str)) return true;
        }
        return false;
    }

    public static String whenEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equals(str2);
    }

    public static String padLeft(String str, int size) {
        return padLeft(str, size, ' ');
    }

    public static String padLeft(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return padLeft(str, size, String.valueOf(padChar));
        }
        return padding(pads, padChar).concat(str);
    }

    public static String padLeft(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return padLeft(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    public static String padRight(String str, int size) {
        return padRight(str, size, ' ');
    }

    public static String padRight(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return padRight(str, size, String.valueOf(padChar));
        }
        return str.concat(padding(pads, padChar));
    }

    public static String padRight(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return padRight(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        Arrays.fill(buf, padChar);
        return new String(buf);
    }

    public static String toUnderline(String camel) {
        if (StringUtils.isEmpty(camel)) {
            return "";
        }

        camel = String.valueOf(camel.charAt(0)).toUpperCase()
                .concat(camel.substring(1));

        StringBuilder sb = new StringBuilder();
        Matcher matcher = CAMEL_UNDERLINE_PATTERN.matcher(camel);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == camel.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static String toCamelCase(String underline, boolean... smallCamel) {
        if (StringUtils.isEmpty(underline)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Matcher matcher = UNDERLINE_CAMEL_PATTERN.matcher(underline);
        while (matcher.find()) {
            String word = matcher.group();
            if ((smallCamel.length == 0 || smallCamel[0]) && matcher.start() == 0) {
                sb.append(Character.toLowerCase(word.charAt(0)));
            } else {
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
}
