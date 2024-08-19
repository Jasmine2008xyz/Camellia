package com.luoyu.dexfinder;

import com.luoyu.camellia.interfaces.IDexFinder;

public class DexKitFinder implements IDexFinder {
    private DexKitFinder() {}
    
    public static DexKitFinder create(){
        return null;
    }

    @Override
    public void findMethodByUseString(String[] SearchPackageName, String[] UseStrings) {}
}
