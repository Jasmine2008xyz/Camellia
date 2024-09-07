
#include <string.h>
#include <jni.h>

jstring
Java_com_luorui_java_nativeinterface_Classes_AioMsgItem( JNIEnv* env,jobject thiz)
{

	return (*env)->NewStringUTF(env,"com.tencent.mobileqq.aio.msg.AIOMsgItem");
	
}

