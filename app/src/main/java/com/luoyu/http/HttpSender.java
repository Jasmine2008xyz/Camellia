package com.luoyu.http;

import com.luoyu.utils.FileUtil;
import com.luoyu.utils.PathUtil;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicBoolean;
import com.luoyu.xposed.base.HookEnv;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.io.InputStream;

public class HttpSender {
  public static String get(String url) {
    /*  OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url("https://api.github.com/").build();
    try (Response response = client.newCall(request).execute()) {
        if (response.isSuccessful()) {
            return response.message();
        } else {
            throw new RuntimeException("Get failed");
        }
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Get failed");
    }*/
    final StringBuffer buffer = new StringBuffer();
    Thread mThread =
        new Thread(
            new Runnable() {

              public void run() {
                InputStreamReader isr = null;
                try {
                  URL urlObj = new URL(url);
                  URLConnection uc = urlObj.openConnection();
                  uc.setConnectTimeout(10000);
                  uc.setReadTimeout(10000);
                  isr = new InputStreamReader(uc.getInputStream(), "utf-8");
                  BufferedReader reader = new BufferedReader(isr); // 缓冲
                  String line;
                  while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                } finally {
                  try {
                    if (null != isr) {
                      isr.close();
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
              }
            });
    mThread.start();
    try {
      mThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (buffer.length() == 0) return buffer.toString();
    buffer.delete(buffer.length() - 1, buffer.length());
    return buffer.toString();
  }

  public static boolean downloadToFileFromQQ(String url, String local) {
    try {
      if (Thread.currentThread().getName().equals("main")) {
        AtomicBoolean builder = new AtomicBoolean();
        Thread thread = new Thread(() -> builder.getAndSet(downloadToFileFromQQ(url, local)));
        thread.start();
        thread.join();
        return builder.get();
      }
      File cache = new File(PathUtil.getApkDataPath() + "cache");
      if (!cache.exists()) cache.mkdirs();
      String cachePath = PathUtil.getApkDataPath() + "cache/" + Math.random();
      File parent = new File(local).getParentFile();
      if (!parent.exists()) parent.mkdirs();

      FileOutputStream fOut = new FileOutputStream(cachePath);
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setConnectTimeout(10000);
      connection.setReadTimeout(10000);
      InputStream ins = connection.getInputStream();
      byte[] buffer = new byte[4096];
      int read;
      while ((read = ins.read(buffer)) != -1) {
        fOut.write(buffer, 0, read);

        // 线程中断
        if (Thread.currentThread().isInterrupted()) {
          fOut.close();
          ins.close();
          return false;
        }
      }
      fOut.flush();
      fOut.close();
      ins.close();

      if (new File(cachePath).length() < 1) return false;
      FileUtil.copyFile(cachePath, local);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean downloadToFileFromModule(String url, String local) {
    try {
      if (Thread.currentThread().getName().equals("main")) {
        AtomicBoolean builder = new AtomicBoolean();
        Thread thread = new Thread(() -> builder.getAndSet(downloadToFileFromModule(url, local)));
        thread.start();
        thread.join();
        return builder.get();
      }
      File cache = new File("/storage/emulated/0/Android/data/com.luoyu.camellia/cache");
      if (!cache.exists()) cache.mkdirs();
      String cachePath =
          "/storage/emulated/0/Android/data/com.luoyu.camellia/cache/" + Math.random();
      File parent = new File(local).getParentFile();
      if (!parent.exists()) parent.mkdirs();

      FileOutputStream fOut = new FileOutputStream(cachePath);
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setConnectTimeout(10000);
      connection.setReadTimeout(10000);
      InputStream ins = connection.getInputStream();
      byte[] buffer = new byte[4096];
      int read;
      while ((read = ins.read(buffer)) != -1) {
        fOut.write(buffer, 0, read);

        // 线程中断
        if (Thread.currentThread().isInterrupted()) {
          fOut.close();
          ins.close();
          return false;
        }
      }
      fOut.flush();
      fOut.close();
      ins.close();

      if (new File(cachePath).length() < 1) return false;
      FileUtil.copyFile(cachePath, local);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
