package com.luoyu.http;


import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
}
