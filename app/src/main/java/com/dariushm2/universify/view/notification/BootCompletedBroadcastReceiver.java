package com.dariushm2.universify.view.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null)
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
                PictureOfTheDayBroadcastReceiver.setAlarm((context));
    }
}
