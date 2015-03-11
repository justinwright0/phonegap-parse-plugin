package com.apptentive.cordova;

import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseAnalytics;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.net.Uri;
import android.util.Log;

import com.apptentive.android.sdk.Apptentive;

import org.json.JSONObject;
import org.json.JSONException;

public class ApptentiveParsePluginReceiver extends ParsePushBroadcastReceiver
{	
	public static final String TAG = "ApptentiveParsePluginReceiver";

	@Override
    protected void onPushOpen(Context context, Intent intent) {
        Log.v(TAG, "onPushOpen");

        super.onPushOpen(context, intent);
        boolean forApptentive = Apptentive.setPendingPushNotification(context, intent);
    }
}
