package com.cmcc.internalcontact;

import android.app.Application;

import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.raizlabs.android.dbflow.config.FlowManager;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharePreferencesUtils sharePreferencesUtils = SharePreferencesUtils.getInstance();
        sharePreferencesUtils.setContext(this);
        FlowManager.init(this);
      /*  SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new FrescoImageLoader())
                .setToolbaseColor(getColor(R.color.colorPrimary))
                .build());*/
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
}
