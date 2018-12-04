package com.cmcc.internalcontact.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.cmcc.internalcontact.service.CallerIdsMatchService;


public class InComingReceiver extends BroadcastReceiver {
    private Context mContext = null;
    private long lastRingingTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == intent) {
            return;
        }

        if (!Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
            mContext = context.getApplicationContext();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    /**
     * 通话状态回调
     */
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    long currentRingingTime = System.currentTimeMillis();
                    if (currentRingingTime - lastRingingTime < 1000) {
                        return;
                    }
                    lastRingingTime = currentRingingTime;
                    CallerIdsMatchService.startCallerMatchService(mContext, incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                case TelephonyManager.CALL_STATE_IDLE:
                    //接通、挂掉电话
                    CallerIdsMatchService.stopCallerMatchService(mContext);
                    break;
            }
        }
    };
}
