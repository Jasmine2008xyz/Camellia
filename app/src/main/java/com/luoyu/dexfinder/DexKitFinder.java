package com.luoyu.dexfinder;

import com.luoyu.camellia.interfaces.IDexFinder;

public class DexKitFinder implements IDexFinder {

    private DexKitFinder(ClassLoader cl) {}

    public static DexKitFinder create(ClassLoader cl) {
        return new DexKitFinder(cl);
    }

    @Override
    public void findMethodByUseString(
            String TAG, String[] SearchPackageName, String[] UseStrings) {}

    public void findMethodsByUseAnnotation(String TAG,Class<?> UseAnnotation) {}
}
