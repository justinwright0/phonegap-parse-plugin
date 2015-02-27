package org.apache.cordova.core;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import <YOUR_APP_PACKAGE>.CordovaApp;

public class ParseApplication extends Application 
{
	private static ParseApplication instance = new ParseApplication();

	public ParseApplication() {
		instance = this;
	}

	public static Context getContext() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// register device for parse
		Parse.initialize(this, "<PARSE_APPLICATION_ID>", "<PARSE_CLIENT_KEY>");
		PushService.setDefaultPushCallback(this, CordovaApp.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
}
