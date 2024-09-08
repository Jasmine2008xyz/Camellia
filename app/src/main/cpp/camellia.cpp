#include <iostream>
#include <jni.h>

extern "C" {
    JNIEXPORT void JNICALL
    Java_com_luoyu_camellia_interfaces_jni_NativeEntry_init(JNIEnv* env, jobject thiz);
}

void Java_com_luoyu_camellia_interfaces_jni_NativeEntry_init(JNIEnv* env, jobject thiz)
{
    jclass cls = env->FindClass("com/luoyu/camellia/utils/FileUtil");
    jmethodID mid = env->GetMethodID(cls, "writeToFile", "(Ljava/lang/String;Ljava/lang/String;)V");
    jstring path = env->NewStringUTF("/storage/emulated/0/Android/media/com.luoyu.camellia/吴迪.txt");
    jstring content = env->NewStringUTF("吴迪");
    env->CallStaticVoidMethod(cls, mid, path, content);
   // env->DeleteLocalRef(path);
   // env->DeleteLocalRef(content);
    
}