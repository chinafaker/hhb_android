package utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class JsonUtil {
    public static Gson gson = new Gson();
    public static Gson gson2 = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .create();

    public static String jsonFromObject(Object object) {
        String result = "";
        try {
            result = gson.toJson(object);
        } catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static <T> T objectFromJson(String json, Class<T> klass) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        T object = null;
        try {
            object = gson.fromJson(json, klass);
        } catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static <T> ArrayList<T> readJsonArray(String json, Class<T> klass) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        ArrayList<T> lcs = new ArrayList<T>();
        try {
            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(json).getAsJsonArray();

            for (JsonElement obj : Jarray) {
                T cse = gson.fromJson(obj, klass);
                lcs.add(cse);
            }

        } catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return lcs;
    }


    public static <T> T objectFromJson2(String json, Class<T> klass) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        T object = null;
        try {
            object = gson2.fromJson(json, klass);
        } catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static <T> T objectFromJson3(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        T object = null;
        try {
            object = gson2.fromJson(json, type);
        } catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return object;
    }
}

class DateSerializer implements JsonDeserializer<Date> {
    // Json转对象的时候调用
    @Override
    public Date deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        Long m = json.getAsLong();
        return new Date(m);
    }


    public static String hashMapFromjsonStr(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Iterator ite = jsonObject.keys();
            // 遍历jsonObject数据,添加到Map对象
            while (ite.hasNext()) {
                String key = (String) ite.next();
                Object value = jsonObject.get(key);
            }


        } catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return "";
    }


}
