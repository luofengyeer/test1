package com.cmcc.internalcontact;

import android.app.Application;

import com.cmcc.internalcontact.db.AppDataBase;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sqlcipher.SQLCipherOpenHelper;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;

import java.util.UUID;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharePreferencesUtils sharePreferencesUtils = SharePreferencesUtils.getInstance();
        sharePreferencesUtils.setContext(this);
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
    }
}
