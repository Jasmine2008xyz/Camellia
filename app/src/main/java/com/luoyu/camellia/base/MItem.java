package com.luoyu.camellia.base;

import com.luoyu.camellia.logging.QLog;
import com.luoyu.config.MConfig;

public class MItem {
    // 这里别用ILog谢谢
    public static QLog QQLog = new QLog();

    public static MConfig Config = new MConfig("Camellia");
}
