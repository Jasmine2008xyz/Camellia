package com.luoyu.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.luoyu.camellia.base.MItem;
import com.luoyu.camellia.utils.PathUtil;
import org.json.JSONObject;
import java.util.List;
import com.luoyu.camellia.utils.FileUtil;
import com.luoyu.camellia.logging.QLog;
import android.util.Config;
import android.util.Log;

public class MConfig {
    public static final String TAG = "Config(配置)";

    private volatile JSONObject json;

    private volatile MConfig config;

    private volatile String ConfigPath;

    /*
     * Create a new Config
     */
    public MConfig(@NonNull String ConfigPath) {
        try {
            this.config = this;
            this.ConfigPath = ConfigPath;
            var read =
                    FileUtil.ReadFileString(
                            PathUtil.getApkDataPath() + "config/" + this.ConfigPath);
            if (read == null) {
                FileUtil.WriteToFile(PathUtil.getApkDataPath() + "config/" + this.ConfigPath, "{}");
            }
            read = FileUtil.ReadFileString(PathUtil.getApkDataPath() + "config/" + this.ConfigPath);
            this.json = new JSONObject(read);
        } catch (Exception e) {
            MItem.QQLog.e(TAG, e);
        }
    }

    public MConfig() {
        new MConfig("公式");
    }

    /*
     * ? it might be more graceful
     */

    public static MConfig create(@NonNull String ConfigPath) {
        return new MConfig(ConfigPath);
    }

    /*
     * void putData(String,T);
     */
    public synchronized <T> void putData(@NonNull String key, @Nullable T value) {
        try {
            this.json.put(key, value);
            FileUtil.WriteToFile(
                    PathUtil.getApkDataPath() + "config/" + ConfigPath, json.toString());
        } catch (Exception e) {
            MItem.QQLog.e(TAG, e);
        }
    }

    /*
     * T getData(String,T);
     */
    public synchronized <T> T getData(@NonNull String key, @Nullable T defaultValue) {
        try {
            if (!this.json.isNull(key)) return (T) this.json.get(key);
        } catch (Exception err) {
            /*
             * Do nothing
             */
        }
        return defaultValue;
    }

    public synchronized <T> String getStringData(
            @NonNull String key, @Nullable String defaultValue) {
        try {
            if (!this.json.isNull(key)) return (String) this.json.get(key);
        } catch (Exception err) {
            /*
             * Do nothing
             */
        }
        return defaultValue;
    }

    public synchronized <T> int getIntData(@NonNull String key, @Nullable int defaultValue) {
        try {
            if (!this.json.isNull(key)) return (int) this.json.get(key);
        } catch (Exception err) {
            /*
             * Do nothing
             */
        }
        return defaultValue;
    }

    public synchronized <T> Boolean getBooleanData(
            @NonNull String key, @Nullable boolean defaultValue) {
        try {
            return (Boolean) this.json.get(key);
        } catch (Exception err) {
         //   MItem.QQLog.i(TAG, Log.getStackTraceString(err));
            /*
             * Do nothing
             */
        }
        return defaultValue;
    }

    public synchronized <T> List getListData(@NonNull String key, @Nullable List defaultValue) {
        try {
            if (!this.json.isNull(key)) return (List) this.json.get(key);
        } catch (Exception err) {
            /*
             * Do nothing
             */
        }
        return defaultValue;
    }
}
