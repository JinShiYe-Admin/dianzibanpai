package net.jiaobaowang.gonggaopai.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;


public class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
            Log.e("BaseApplication", "onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
            Log.e("BaseApplication", "onTerminate");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }




}
