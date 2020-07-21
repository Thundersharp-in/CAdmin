package com.thundersharp.cadmin.calendar.weather;

import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;


/**
 * Broadcast receiver that triggers launching weather sync service
 */
public class WeatherSyncAlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context, new Intent(context, WeatherSyncService.class));
    }
}
