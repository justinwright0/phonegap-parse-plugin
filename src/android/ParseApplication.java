package org.apache.cordova.core;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class ParseApplication extends Application 
{
	private static String TAG = "ParseApplication";

	@Override
	public void onCreate() {
		super.onCreate();

		final Context appContext = this;

		// Find the parse application Id and client Key from metadata set in the manifest
		try {
			ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = ai.metaData;

			if (metaData != null ) {
				String applicationId = metaData.getString("parseApplicaitonId");
				String clientKey = metaData.getString("parseClientKey");

				Parse.initialize(this, applicationId, clientKey);
		        ParsePush.subscribeInBackground("", new SaveCallback() {
		            @Override
		            public void done(ParseException e) {
		                if (e == null) {
		                    Log.d(TAG, "successfully subscribed to the broadcast channel.");
		                } else {
		                    Log.e(TAG, "failed to subscribe for push broadcast channel.", e);
		                }
		            }
		        });
			} else {
				throw new Exception("Missing Parse Application Id or Client Key");
			}

		} catch (Exception e) {
			Log.e(TAG, "Unexpected error while reading application info.", e);
		}

	}
}
