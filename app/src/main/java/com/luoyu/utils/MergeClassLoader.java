package com.luoyu.utils;

import android.content.Context;

public class MergeClassLoader extends ClassLoader {
    
    private ClassLoader hostLoader;
    private ClassLoader moduleLoader;

    public MergeClassLoader(ClassLoader moduleloader, ClassLoader hostloader) {
        this.hostLoader = hostloader;
        this.moduleLoader = moduleloader;
    }

    @Override
    protected Class<?> loadClass(String classnane, boolean resolve) throws ClassNotFoundException {
        if(isConflictingClass(classnane)) super.loadClass(classnane,resolve);
        try {
            return Context.class.getClassLoader().loadClass(classnane);
        } catch (Exception e) {
            try {
                return moduleLoader.loadClass(classnane);
            } catch (Exception e2) {
                try {
                    return hostLoader.loadClass(classnane);
                } catch (Exception err) {
                    return super.loadClass(classnane,resolve);
                }
            }
        }
    }
    public boolean isConflictingClass(String name) {
        return name.startsWith("androidx.") || name.startsWith("android.support.")
            || name.startsWith("kotlin.") || name.startsWith("kotlinx.")
            || name.startsWith("com.tencent.mmkv.")
            || name.startsWith("com.android.tools.r8.")
            || name.startsWith("com.google.android.")
            || name.startsWith("com.google.gson.")
            || name.startsWith("com.google.common.")
            || name.startsWith("com.microsoft.appcenter.")
            || name.startsWith("org.intellij.lang.annotations.")
            || name.startsWith("org.jetbrains.annotations.")
            || name.startsWith("com.bumptech.glide.")
            || name.startsWith("com.google.errorprone.annotations.");
    }
}
