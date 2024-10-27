# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn javax.**
-dontwarn java.**
-keep class com.android.dx.** {*;}

-dontwarn com.sun.**
-dontwarn edu.umd.**

-keep class com.lxj.xpopup.widget.**{*;}
-keep class com.luoyu.xposed.startup.Hook_Entry
-keep class com.luoyu.xposed.hook.*{*;}
-keep class com.luoyu.camellia.interfaces.jni.**{*;}
-keep class net.bytebuddy.** {*;}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class * implements java.io.Serializable { *; }

-keep class * extends android.app.Activity {*;}

-dontpreverify
-optimizationpasses 5
-keepattributes SourceFile,LineNumberTable
-dontusemixedcaseclassnames
-verbose

-keep class androidx.**.**{*;}
-keep class android.**.**{*;}

# Keep any classes referenced by DexKitBridge
-keep class org.luckypray.dexkit.DexKitBridge { *; }
-dontwarn kotlin.jvm.internal.**

#-keep class kotlin.coroutines.** { *; }
# 保持所有 Kotlin 内部类
-keep class kotlin.jvm.internal.** { *; }

# 保持 Kotlin 反射类
#-keep class kotlin.reflect.** { *; }

# 保持 Kotlin 标记注解
# -keep @kotlin.Metadata class * { *; }

# 保持使用动态类加载的类
#-keepclassmembers class * {
#    @androidx.annotation.Keep <fields>;
#    @androidx.annotation.Keep <methods>;
#}


 # -obfuscationdictionary Rules.txt
 # -classobfuscationdictionary Rules.txt
 # -packageobfuscationdictionary Rules.txt
 
-renamesourcefileattribute SourceFile
-repackageclasses "l"
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*