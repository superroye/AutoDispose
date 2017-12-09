package com.uber.autodispose.sample;

import android.app.Application;
import com.uber.autodispose.BlackMirror;
import java.lang.reflect.Method;

public final class MyApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    try {
      // Example of deferred classloading in application.
      // Can use BlackMirror.getInstance() directly or getClassLoader() or with the arg
      // or through getClassLoader(). Do not use Class.forName(String).
      // You're fine after this though
      Class<?> clazz = BlackMirror.getInstance(this)
          .loadClass("com.uber.autodispose.sample.AppDelegate");
      Method method = clazz.getDeclaredMethod("onCreate");
      method.setAccessible(true);
      method.invoke(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
