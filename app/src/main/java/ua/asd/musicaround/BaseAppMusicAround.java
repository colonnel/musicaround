package ua.asd.musicaround;


import android.app.Application;
import android.content.Context;

public class BaseAppMusicAround extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        appContext = getApplicationContext();
        super.onCreate();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        BaseAppMusicAround.appContext = appContext;
    }
}
