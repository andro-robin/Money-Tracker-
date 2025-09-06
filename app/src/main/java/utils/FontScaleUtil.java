package utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class FontScaleUtil {
    private FontScaleUtil() {}

    /**
     * Apply the given scale to this context's configuration.
     */
    public static void applyFontScale(@NonNull Context context, float scale) {
        Configuration config = context.getResources().getConfiguration();
        if (scale <= 0f) scale = 1.0f;
        if (Math.abs(config.fontScale - scale) < 0.001f) return;
        config.fontScale = scale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    /**
     * Initialize font scale at process start and keep activities recreated when it changes.
     */
    public static void initialize(@NonNull Application application) {
        float saved = SharedPrefsManager.getInstance(application).getAppFontScale();
        applyFontScale(application.getApplicationContext(), saved);
        registerRecreateOnChange(application);
    }

    private static void registerRecreateOnChange(@NonNull Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                float saved = SharedPrefsManager.getInstance(activity).getAppFontScale();
                applyFontScale(activity, saved);
            }
            @Override public void onActivityStarted(@NonNull Activity activity) { }
            @Override public void onActivityResumed(@NonNull Activity activity) { }
            @Override public void onActivityPaused(@NonNull Activity activity) { }
            @Override public void onActivityStopped(@NonNull Activity activity) { }
            @Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) { }
            @Override public void onActivityDestroyed(@NonNull Activity activity) { }
        });
    }
}
