package com.fine.friendlycc.manager;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.fine.friendlycc.R;

import java.util.Locale;

import me.goldze.mvvmhabit.utils.Utils;

public class LocaleManager {

    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale() {
        String text = Utils.getContext().getString(R.string.playcc_local_language_val);
        return new Locale(text);
    }


    public static Context setLocal(Context context) {
        return updateResources(context, getSystemLocale());
    }

    private static Context updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static void onConfigurationChanged(Context context){
        setLocal(context);
    }

}
