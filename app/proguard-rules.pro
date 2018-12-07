# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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

-keep class me.zsj.dan.BuildConfig{*;}
-keep class me.zsj.dan.** {*;}
-keep public class me.zsj.dan.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#glide
-keep class me.zsj.dan.binder.glide.OkHttpProgressGlideModule
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** {*;}

-keepattributes *Annotation*
-keep class com.squareup.okhttp3.** { *; }
-dontwarn okio.**
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**

# bughd
-keepattributes Exceptions, Signature, LineNumberTable

-dontwarn java.lang.invoke**

-dontnote okhttp3.**, okio.**, retrofit2.**, pl.droidsonroids.**
-ignorewarnings
