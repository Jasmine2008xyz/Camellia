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
-keep class com.luorui.formula.core.**{*;}
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

 # -obfuscationdictionary Rules.txt
 # -classobfuscationdictionary Rules.txt
 # -packageobfuscationdictionary Rules.txt
 
-renamesourcefileattribute SourceFile
-repackageclasses "l"
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*