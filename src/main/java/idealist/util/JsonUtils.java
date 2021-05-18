package idealist.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:04:50
 */
public abstract class JsonUtils {
    private static Gson gson;

    private static Gson getGson() {
        if (gson == null) {
            synchronized (JsonUtils.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    public static <T> String toJson(T obj) {
        String jsonStr;
        try {
            jsonStr = getGson().toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jsonStr;
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return getGson().fromJson(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            return getGson().fromJson(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
