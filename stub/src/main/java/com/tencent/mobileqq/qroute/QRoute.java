package com.tencent.mobileqq.qroute;
import androidx.annotation.NonNull;

public class QRoute {
    public static final String TAG = "QRoute";
    public static final String TAG_QROUTE_API = "QRouteApi: ";
    private QRoute() {
    }
    @NonNull
    public static <T extends QRouteApi> T api(Class<T> cls) {
        throw new RuntimeException("stub!");
    }
}
