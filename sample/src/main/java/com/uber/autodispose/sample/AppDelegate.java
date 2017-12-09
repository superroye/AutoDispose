package com.uber.autodispose.sample;

import android.support.annotation.Keep;
import android.util.Log;

public final class AppDelegate {

  @Keep public static void onCreate() {
    try {
      Log.d("BLAH",
          "AppDelegate.onCreate - " + Class.forName("com.uber.autodispose.AutoDisposePlugins")
              .getClassLoader()
              .getClass()
              .getName());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    //AutoDisposePlugins.setFillInOutsideLifecycleExceptionStacktraces(true);
    //AutoDisposePlugins.setOutsideLifecycleHandler(new Consumer<OutsideLifecycleException>() {
    //  @Override public void accept(OutsideLifecycleException e) {
    //    Log.d("BLAH", "MyApplication.accept - ");
    //  }
    //});
  }
}
