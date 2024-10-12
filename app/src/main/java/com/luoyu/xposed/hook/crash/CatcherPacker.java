package com.luoyu.xposed.hook.crash;

import com.luoyu.utils.PathUtil;
import com.luoyu.xposed.logging.LogCat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CatcherPacker {
    ZipOutputStream zOut;
    ByteArrayOutputStream bArr = new ByteArrayOutputStream();
    String Path;

    private CatcherPacker() {
        Path = PathUtil.getApkDataPath() + "CrashReport/" + getRandomName();
        new File(PathUtil.getApkDataPath() + "CrashReport").mkdirs();
        try {
            zOut = new ZipOutputStream(new FileOutputStream(Path));
        } catch (FileNotFoundException e) {
            zOut = new ZipOutputStream(bArr);
        }
    }

    public static CatcherPacker getInstance() {
        return new CatcherPacker();
    }

    private String getRandomName() {
        String curNano = System.nanoTime() + "";
        return "Crash_" + LogCat.GetNowTime() + "." + curNano.substring(6) + ".zip";
    }

    public void AddStackTrace(String Track) {
        try {
            ZipEntry newTrack = new ZipEntry("StackTrace.log");
            zOut.putNextEntry(newTrack);
            zOut.write(Track.getBytes(StandardCharsets.UTF_8));
            zOut.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddDeviceInfo(String DeviceInfo) {
        try {
            ZipEntry newTrack = new ZipEntry("DeviceInfo.log");
            zOut.putNextEntry(newTrack);
            zOut.write(DeviceInfo.getBytes(StandardCharsets.UTF_8));
            zOut.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddLogcatInfo(String Logcat) {
        try {
            ZipEntry newTrack = new ZipEntry("LogCat.log");
            zOut.putNextEntry(newTrack);
            zOut.write(Logcat.getBytes(StandardCharsets.UTF_8));
            zOut.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddXposedLog(String LogInfo) {
        try {
            ZipEntry newTrack = new ZipEntry("Xposed_Log.log");
            zOut.putNextEntry(newTrack);
            zOut.write(LogInfo.getBytes(StandardCharsets.UTF_8));
            zOut.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddXposedErr(String LogInfo) {
        try {
            ZipEntry newTrack = new ZipEntry("Xposed_Err.log");
            zOut.putNextEntry(newTrack);
            zOut.write(LogInfo.getBytes(StandardCharsets.UTF_8));
            zOut.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CloseAll() {
        try {
            zOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return Path;
    }
}
