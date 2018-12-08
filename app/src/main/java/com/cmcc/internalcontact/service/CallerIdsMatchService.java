package com.cmcc.internalcontact.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.callerfloat.FloatWindowManager;

public class CallerIdsMatchService extends Service {

    public static final String CALLER_MATCH_STATE = "state";
    public static final String CALLER_MATCH_STATE_RINGING = "CALL_STATE_RINGING";
    public static final String CALLER_MATCH_STATE_IDLE = "CALL_STATE_IDLE";

    private SharePreferencesUtils preferencesEditor;
    private Context mContext;

    public static void startCallerMatchService(Context context, @NonNull String callerCode) {
        Intent callerIdsMatchIntent = new Intent(context, CallerIdsMatchService.class);
        callerIdsMatchIntent.putExtra("callerCodeValue", callerCode);
        callerIdsMatchIntent.putExtra(CALLER_MATCH_STATE, CALLER_MATCH_STATE_RINGING);
        context.startService(callerIdsMatchIntent);
    }

    public static void stopCallerMatchService(Context context) {
        Intent callerIdsMatchIntent = new Intent(context, CallerIdsMatchService.class);
        callerIdsMatchIntent.putExtra(CALLER_MATCH_STATE, CALLER_MATCH_STATE_IDLE);
        context.startService(callerIdsMatchIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        preferencesEditor = SharePreferencesUtils.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            if (intent.hasExtra(CALLER_MATCH_STATE)) {
                switch (intent.getStringExtra(CALLER_MATCH_STATE)) {
                    case CALLER_MATCH_STATE_RINGING:
                        String callerCode = intent.getStringExtra("callerCodeValue");
                        if (!TextUtils.isEmpty(callerCode)) {
                            showCallerMatchWindow(callerCode);
                        }
                        break;
                    case CALLER_MATCH_STATE_IDLE:
                        FloatWindowManager.getInstance().dismissFloatWindow();
                        break;
                    default:
                        break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 打开窗口
     */
    private void showCallerMatchWindow(final String callerIdsCode) {
        //功能开关
        if (preferencesEditor != null && !preferencesEditor.getBoolean(Constant.SWITCH_INCOME_STATUE)) {
            return;
        }
     /*   EoaApplication eoaApplication = (EoaApplication) mContext.getApplicationContext();
        //未登录，返回
        DomainComponent domainComponent = eoaApplication.createDomainComponent();
        domainComponent.callerIDsMatchUseCase()
                .params(callerIdsCode)
                .execute(new Subscriber<PersonBean>() {
                    @Override
                    public void onNext(PersonBean personBean) {
                        super.onNext(personBean);
                        if (null == personBean) {
                            return;
                        }
                        FloatWindowManager.getInstance().showCallerFloatWindow(getApplicationContext(), callerIdsCode, personBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        FloatWindowManager.getInstance().dismissFloatWindow();
                    }
                });*/
    }
}
