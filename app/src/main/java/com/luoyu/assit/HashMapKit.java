package com.luoyu.assit;

import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;

public class HashMapKit {
    private static final String TAG = "HashMapKit";

    /*
     * 反向HashMap
     * 通过Value获取Map中的首个Key
     */
    public static <K, V> K getKeyFromValue(@NonNull HashMap<K, V> hashmap, V value) {
        for (Map.Entry<K, V> entry : hashmap.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("No such key in the HashMap");
    }
}
