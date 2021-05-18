package idealist.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 01:08:05
 */
public class NameParameter {
    private static final Pattern pattern = Pattern.compile("#\\{(.*?)}");
    private static final Map<String, String> sqls = new HashMap<>();
    private static final Map<String, String[]> names = new HashMap<>();

    public static String getSql(String sql) {
        String result = sqls.get(sql);
        if (result != null) {
            return result;
        }
        parse(sql);
        return sqls.get(sql);
    }

    public static String[] getNames(String sql) {
        String[] result = names.get(sql);
        if (result != null) {
            return result;
        }
        parse(sql);
        return names.get(sql);
    }

    public static Object[] getParameters(String sql, Map<String, Object> params) {
        String[] names = getNames(sql);
        Object[] parameters = new Object[names.length];
        for (int i = 0; i < names.length; i++) {
            parameters[i] = params.get(names[i]);
        }
        return parameters;
    }

    public static Object[][] getParameters(String sql, List<Map<String, Object>> params) {
        String[] names = getNames(sql);
        Object[][] parameters = new Object[params.size()][names.length];
        for (int i = 0; i < params.size(); i++) {
            for (int j = 0; j < names.length; j++) {
                parameters[i][j] = params.get(i).get(names[j]);
            }
        }
        return parameters;
    }

    private static synchronized void parse(String sql) {
        List<String> names = new LinkedList<>();
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        NameParameter.sqls.put(sql, matcher.replaceAll("?"));
        NameParameter.names.put(sql, names.toArray(new String[0]));
    }
}
