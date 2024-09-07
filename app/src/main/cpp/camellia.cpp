#include <iostream>
#include <jni.h>

extern "C" {
    JNIEXPORT void JNICALL
    Java_com_luoyu_camellia_interfaces_jni_NativeEntry_init(JNIEnv* env, jobject thiz);
}

void Java_com_luoyu_camellia_interfaces_jni_NativeEntry_init(JNIEnv* env, jobject thiz)
{
    
}
