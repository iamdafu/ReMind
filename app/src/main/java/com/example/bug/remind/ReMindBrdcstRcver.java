package com.example.bug.remind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by bug on 15.07.14.
 */
public class ReMindBrdcstRcver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MyNotifyService.class));
    }
}
