#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Support
-dontwarn android.support.**

# Icepick
-dontwarn icepick.**
-keep class **$$State { *; }
-keepnames class * { @icepick.State *;}
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}

# Ok Http
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**


# Realm
-keep @io.realm.annotations.RealmModule class *
-dontwarn javax.**
-dontwarn io.realm.**

# Butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# For RxJava:
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**
-dontwarn rx.**
-keep class sun.misc.Unsafe { *; }

##---------------Begin: proguard configuration for Gson  ----------
# Adapted from https://code.google.com/p/google-gson/source/browse/trunk/examples/android-proguard-example/proguard.cfg
#
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# We use Gson's @SerializedName annotation which won't work without this:
-keepattributes *Annotation*

# Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Classes that will be serialized/deserialized over Gson
# http://stackoverflow.com/a/7112371/56285
-keep class io.explod.android.emptyshell.model.** { *; }

##---------------End: proguard configuration for Gson  ----------

# Remove logging calls
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# Retrolambda
-dontwarn java.lang.invoke.*