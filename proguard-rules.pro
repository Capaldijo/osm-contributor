## Proguard config file for dma-osm-template


#-dontobfuscate

####################################################################################################
## Project specific

-keepclasseswithmembernames class mobi.designmyapp.osmtemplate.map.vectorial.GeoJSONFileDescriptor {
    *;
}

####################################################################################################
## Crashlytics

-keepattributes SourceFile,LineNumberTable

####################################################################################################
## EventBus

-keepclassmembers class ** {
    public void onEvent*(**);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

####################################################################################################
## Butterknife

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

####################################################################################################
## OrmLite

-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-keepclassmembers class **DateTime {
    <init>(long);
    long getMillis();
}

-keepclassmembers class * {
  public <init>(android.content.Context);
}

####################################################################################################
## Retrofit

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions


####################################################################################################
## OkHttp /OkIO

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**
-dontwarn -dontwarn com.squareup.okhttp.internal.huc.HttpURLConnectionImpl

####################################################################################################
## SimpleXML

-keep public class org.simpleframework.**{ *; }
-keep class org.simpleframework.xml.**{ *; }
-keep class org.simpleframework.xml.core.**{ *; }
-keep class org.simpleframework.xml.util.**{ *; }
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn org.simpleframework.xml.**

-keepclasseswithmembernames class * {
    @org.simpleframework.xml.* <fields>;
}

####################################################################################################
## Mapbox

-keepclassmembers class com.mapbox.mapboxsdk.views.MapView { *; }
-dontwarn com.vividsolutions.jts.awt.**

## Use a method from android.util.FloatMath deleted at API 23
-dontwarn com.almeros.android.multitouch.TwoFingerGestureDetector

####################################################################################################
## Jodatime

-dontwarn org.joda.convert.ToString
-dontwarn org.joda.convert.FromString

####################################################################################################