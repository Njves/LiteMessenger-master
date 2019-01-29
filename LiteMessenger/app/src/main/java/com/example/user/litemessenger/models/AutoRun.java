package com.example.user.litemessenger.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.user.litemessenger.service.FoneService;

public class AutoRun extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // получили boot_completed - запустили FoneService
            context.startService(new Intent(context, FoneService.class));
            Log.i("chat", "+ AutoRun - отработал");
        }
    }
}
