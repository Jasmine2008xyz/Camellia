#include <iostream>
#include <jni.h>

extern "C" {
    JNIEXPORT void JNICALL
    Java_com_luoyu_camellia_interfaces_jni_NativeEntry_init(JNIEnv* env, jobject thiz);
}

void Java_com_luoyu_camellia_interfaces_jni_NativeEntry_init(JNIEnv* env, jobject thiz)
{
    jclass cls = env->FindClass("com/luoyu/camellia/utils/FileUtil");
    jmethodID mid = env->GetMethodID(cls, "writeToFile", "(I)V");
    env->CallVoidMethod(cls, mid, "/storage/emulated/0/Android/media/com.tencent.mobileqq/吴迪.txt","吴迪"); 
}
