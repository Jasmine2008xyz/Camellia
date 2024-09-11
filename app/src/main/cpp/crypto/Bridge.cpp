#include <jni.h>
#include <string>

// Declare all JNI methods in a single extern "C" block
extern "C" {
    JNIEXPORT jstring JNICALL
    Java_com_luoyu_camellia_interfaces_jni_Crypto_Encrypt(JNIEnv* env, jobject thiz, jstring message);
    
    JNIEXPORT jstring JNICALL
    Java_com_luoyu_camellia_interfaces_jni_Crypto_Decode(JNIEnv* env, jobject thiz, jstring message);
}

// Implement the Encrypt method
jstring Java_com_luoyu_camellia_interfaces_jni_Crypto_Encrypt(JNIEnv* env, jobject thiz, jstring message) {
    const char* nativeMessage = env->GetStringUTFChars(message, 0);
    std::string encryptedMessage = "Encrypted: " + std::string(nativeMessage);
    env->ReleaseStringUTFChars(message, nativeMessage);
    return env->NewStringUTF(encryptedMessage.c_str());
}

// Implement the Decode method
jstring Java_com_luoyu_camellia_interfaces_jni_Crypto_Decode(JNIEnv* env, jobject thiz, jstring message) {
    const char* nativeMessage = env->GetStringUTFChars(message, 0);
    std::string decodedMessage = "Decoded: " + std::string(nativeMessage);
    env->ReleaseStringUTFChars(message, nativeMessage);
    return env->NewStringUTF(decodedMessage.c_str());
}
