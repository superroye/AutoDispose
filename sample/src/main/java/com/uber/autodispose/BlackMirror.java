package com.uber.autodispose;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.uber.autodispose.sample.BuildConfig;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Field;
import java.util.List;

/**
 * https://www.youtube.com/watch?v=kIvEu-XjZ40
 */
public final class BlackMirror extends PathClassLoader {

  private static BlackMirror INSTANCE;

  public static synchronized BlackMirror getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new BlackMirror(location(context.getPackageManager()));
    }
    return INSTANCE;
  }

  public static void install(Context context) throws Exception {
    BlackMirror instance = getInstance(context);

    // Application stuff still ends up going through here?
    Log.d("BlackMirror", "BlackMirror.install - Created hack");
    Class<?> systemLoader = Class.forName("java.lang.ClassLoader$SystemClassLoader");
    Log.d("BlackMirror", "BlackMirror.install - Found class");
    Field field = systemLoader.getDeclaredField("loader");
    Log.d("BlackMirror", "BlackMirror.install - Acquired system classloader field");
    field.set(null, instance);
    Log.d("BlackMirror", "BlackMirror.install - setting system classLoader");
    Thread.currentThread()
        .setContextClassLoader(instance);
    Log.d("BlackMirror", "BlackMirror.install - set system classLoader successful");
    if (ClassLoader.getSystemClassLoader() != instance) {
      throw new RuntimeException("DARN");
    }

    // This is the real magic
    // This is a "ContextImpl"
    Log.d("BlackMirror", "BlackMirror.install - Reading ContextImpl");
    Context baseContext = ((ContextWrapper) context.getApplicationContext()).getBaseContext();
    Class<?> clazz = baseContext.getClass();

    // DOES NOT WORK ON SAMSUNG 😬🔫
    //Field field = clazz.getDeclaredField("mClassLoader");
    //field.setAccessible(true);
    //field.set(context, instance);

    // LoadedApk is what holds the classloader that all android bits route through
    Log.d("BlackMirror", "BlackMirror.install - Reading context packageInfo");
    Field packageInfoField = clazz.getDeclaredField("mPackageInfo");
    packageInfoField.setAccessible(true);
    Log.d("BlackMirror", "BlackMirror.install - Reading LoadedApk");
    Class<?> loadedApkClazz = Class.forName("android.app.LoadedApk");
    Log.d("BlackMirror", "BlackMirror.install - Reading LoadedApk.mClassLoader");
    Field classLoaderField = loadedApkClazz.getDeclaredField("mClassLoader");
    Log.d("BlackMirror", "BlackMirror.install - Setting LoadedApk.mClassLoader");
    classLoaderField.setAccessible(true);
    classLoaderField.set(packageInfoField.get(baseContext), instance);
    Log.d("BlackMirror", "BlackMirror.install - Success");
  }

  private static String location(PackageManager pm) {
    List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
    List<ApplicationInfo> appInfoList = pm.getInstalledApplications(0);
    for (int x = 0; x < packages.size(); x++) {
      PackageInfo pkginfo = packages.get(x);
      if (BuildConfig.APPLICATION_ID.equals(pkginfo.packageName)) {
        return appInfoList.get(x).sourceDir;
      }
    }
    throw new RuntimeException("DID NOT FIND OUR PACKAGE LOL");
  }

  private BlackMirror(String sourceDir) {
    super(sourceDir, getSystemClassLoader());
  }

  @Override protected Class<?> findClass(String name) throws ClassNotFoundException {
    Log.d("BlackMirror", "BlackMirror.findClass - " + name);
    return super.findClass(name);
  }

  @Override public Class<?> loadClass(String name) throws ClassNotFoundException {
    Log.d("BlackMirror", "BlackMirror.loadClass - " + name);
    return super.loadClass(name);
  }

  @Override protected Class<?> loadClass(String name, boolean resolve)
      throws ClassNotFoundException {
    Log.d("BlackMirror", "BlackMirror.loadClass - " + name + " " + resolve);
    return super.loadClass(name, resolve);
  }
}
