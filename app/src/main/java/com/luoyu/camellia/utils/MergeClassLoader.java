package com.luoyu.camellia.utils;

import android.content.Context;

public class MergeClassLoader extends ClassLoader {
    
    private ClassLoader HostLoader;
    private ClassLoader ModuleLoader;

    public MergeClassLoader(ClassLoader moduleloader, ClassLoader hostloader) {
        this.HostLoader = hostloader;
        this.ModuleLoader = moduleloader;
    }

    @Override
    protected Class<?> loadClass(String classnane, boolean resolve) throws ClassNotFoundException {
        try {
            return Context.class.getClassLoader().loadClass(classnane);
        } catch (Exception e) {
            try {
                return ModuleLoader.loadClass(classnane);
            } catch (Exception e2) {
                try {
                    return HostLoader.loadClass(classnane);
                } catch (Exception err) {
                    return null;
                }
            }
        }
    }
}
