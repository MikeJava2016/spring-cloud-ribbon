package com.sunshine.utils.common;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class JsonUtil implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    public static final Gson s_gson = (new GsonBuilder()).serializeNulls().registerTypeAdapter(Date.class, new JsonUtil.UtilDateAdapter()).create();
    public static final Gson n_gson = (new GsonBuilder()).registerTypeAdapter(Date.class, new JsonUtil.UtilDateAdapter()).create();

    public JsonUtil() {
    }

    public static <W> String toJson(W object) {
        return null == object ? null : s_gson.toJson(object);
    }

    public static <W> W fromJson(String jsonStr, Type typeOfT) {
        try {
            return s_gson.fromJson(jsonStr, typeOfT);
        } catch (JsonSyntaxException var3) {
            logger.error("json格式错误, jsonStr={}", jsonStr, var3);
            throw new RuntimeException("json格式错误");
        }
    }

    public static <W> W fromJson(String jsonStr, Class<W> cls) {
        try {
            return s_gson.fromJson(jsonStr, cls);
        } catch (JsonSyntaxException var3) {
            logger.error("json格式错误, jsonStr={}", jsonStr, var3);
            throw new RuntimeException("json格式错误");
        }
    }

    public static <W> List<W> listFromJson(String jsonStr, Class<W> cls) {
        try {
            return (List) s_gson.fromJson(jsonStr, new JsonUtil.ParameterizedTypeImpl(List.class, new Type[]{cls}));
        } catch (JsonSyntaxException var3) {
            logger.error("json格式错误, jsonStr={}", jsonStr, var3);
            throw new RuntimeException("json格式错误");
        }
    }

    public static <W> String toJsonExcludeNull(W object) {
        return null == object ? null : n_gson.toJson(object);
    }

    private static class UtilDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        private UtilDateAdapter() {
        }

        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            String dateStr = DateUtil.format(src, "yyyy-MM-dd HH:mm:ss");
            return new JsonPrimitive(dateStr);
        }

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            } else {
                String dateStr = json.getAsString();
                Date date = null;
                if (null != dateStr && "" != dateStr){
                    date = DateUtil.parseDate(dateStr);
                }

                return date;
            }
        }
    }

    public static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        public Type[] getActualTypeArguments() {
            return this.args;
        }

        public Type getRawType() {
            return this.raw;
        }

        public Type getOwnerType() {
            return null;
        }
    }
}
