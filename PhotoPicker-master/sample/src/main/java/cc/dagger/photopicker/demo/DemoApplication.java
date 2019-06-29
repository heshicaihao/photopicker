package cc.dagger.photopicker.demo;

import android.app.Application;

/**
 * Created by wzfu on 16/5/22.
 */
public class DemoApplication extends Application {

    private static DemoApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

    }

    public static DemoApplication getInstance() {
        return mInstance;
    }
}