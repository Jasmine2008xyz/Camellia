package com.luoyu.dexfinder;

import android.provider.Settings;
import androidx.annotation.NonNull;
import com.luoyu.camellia.base.HookEnv;
import com.luoyu.camellia.base.MItem;
import com.luoyu.camellia.utils.DataUtil;
import com.luoyu.camellia.utils.PathUtil;
import java.io.File;
import com.luoyu.camellia.utils.FileUtil;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SoLoader {
    public static final String TAG = "SoLoader(So加载器)";

    public static void loadByName(@NonNull String name) {
        String cachePath =
                HookEnv.getContext().getCacheDir()
                        + "/"
                        + DataUtil.getStrMD5(Settings.Secure.ANDROID_ID + "_").substring(0, 8)
                        + "/";
        String tempName = "" + name.hashCode();
        FileUtil.deleteFile(new File(cachePath + tempName));
        outputLibToCache(cachePath + tempName, name);
        System.load(cachePath + tempName);
    }

    private static void outputLibToCache(String cachePath, String name) {
        String apkPath = PathUtil.getModuleApkPath();
        try {
            ZipInputStream zInp = new ZipInputStream(new FileInputStream(apkPath));
            ZipEntry entry;
            while ((entry = zInp.getNextEntry()) != null) {
                if (android.os.Process.is64Bit()
                        && entry.getName().startsWith("lib/arm64-v8a/" + name)) {
                    FileUtil.WriteToFile(cachePath, DataUtil.readAllBytes(zInp));
                    break;
                } else if (!android.os.Process.is64Bit()
                        && entry.getName().startsWith("lib/armeabi-v7a/" + name)) {
                    FileUtil.WriteToFile(cachePath, DataUtil.readAllBytes(zInp));
                    break;
                }
            }
        } catch (Exception ignored) {
            MItem.QQLog.log(TAG, ignored);
        }
    }
}
