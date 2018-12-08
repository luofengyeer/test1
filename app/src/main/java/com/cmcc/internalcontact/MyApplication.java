package com.cmcc.internalcontact;

import android.app.Application;
import android.content.Context;

import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.Utils;
import com.cmcc.internalcontact.utils.imagepicker.GlideImageLoader;
import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.raizlabs.android.dbflow.config.FlowManager;


public class MyApplication extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        instance = this;
        SharePreferencesUtils sharePreferencesUtils = SharePreferencesUtils.getInstance();
        sharePreferencesUtils.setContext(this);
        FlowManager.init(this);
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new GlideImageLoader())
                .setToolbaseColor(getColor(R.color.color_ff4c96f7))
                .build());
/*
        FlowManager.init(FlowConfig.builder(this)
                .addDatabaseConfig(DatabaseConfig.builder(AppDataBase.class)
                        .openHelper(new DatabaseConfig.OpenHelperCreator() {
                            @Override
                            public OpenHelper createHelper(DatabaseDefinition databaseDefinition, DatabaseHelperListener helperListener) {
                                return new SQLCipherOpenHelper(databaseDefinition, helperListener) {
                                    @Override
                                    protected String getCipherSecret() {
                                        if (BuildConfig.DEBUG) {
                                            return "test";
                                        }
                                        if (sharePreferencesUtils.getString("app", null) == null) {
                                            String value = UUID.randomUUID().toString();
                                            sharePreferencesUtils.setString("app", value);
                                        }
                                        return sharePreferencesUtils.getString("app", null);
                                    }
                                };
                            }
                        })
                        .build())
                .build());
*/
    }

    public static Context getAppContext() {
        return instance;
    }

}
