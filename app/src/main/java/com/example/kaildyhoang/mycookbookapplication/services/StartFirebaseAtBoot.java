package com.example.kaildyhoang.mycookbookapplication.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Microsoft Windows on 15/07/2017.
 */

public class StartFirebaseAtBoot extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,FirebaseBackgroundService.class));
    }
}
