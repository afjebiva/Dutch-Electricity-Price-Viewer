# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# signingConfig.enableV1Signing property.

-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep Retrofit classes
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep GSON classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
