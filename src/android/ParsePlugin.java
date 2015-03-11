package org.apache.cordova.core;

import android.util.Log;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

public class ParsePlugin extends CordovaPlugin {
    private static final String TAG = "ParsePlugin";

    public static final String ACTION_INITIALIZE = "initialize";
    public static final String ACTION_GET_INSTALLATION_ID = "getInstallationId";
    public static final String ACTION_GET_DEVICE_TOKEN = "getDeviceToken";
    public static final String ACTION_GET_INSTALLATION_OBJECT_ID = "getInstallationObjectId";
    public static final String ACTION_GET_SUBSCRIPTIONS = "getSubscriptions";
    public static final String ACTION_SUBSCRIBE = "subscribe";
    public static final String ACTION_UNSUBSCRIBE = "unsubscribe";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(ACTION_INITIALIZE)) {
            this.initialize(callbackContext, args);
            return true;
        }
        if (action.equals(ACTION_GET_INSTALLATION_ID)) {
            this.getInstallationId(callbackContext);
            return true;
        }
        if (action.equals(ACTION_GET_DEVICE_TOKEN)) {
            this.getDeviceToken(callbackContext);
            return true;
        }
        if (action.equals(ACTION_GET_INSTALLATION_OBJECT_ID)) {
            this.getInstallationObjectId(callbackContext);
            return true;
        }
        if (action.equals(ACTION_GET_SUBSCRIPTIONS)) {
            this.getSubscriptions(callbackContext);
            return true;
        }
        if (action.equals(ACTION_SUBSCRIBE)) {
            this.subscribe(args.getString(0), callbackContext);
            return true;
        }
        if (action.equals(ACTION_UNSUBSCRIBE)) {
            this.unsubscribe(args.getString(0), callbackContext);
            return true;
        }
        return false;
    }

    private void initialize(final CallbackContext callbackContext, final JSONArray args) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    String appId = args.getString(0);
                    String clientKey = args.getString(1);
                    Parse.initialize(cordova.getActivity(), appId, clientKey);
                    PushService.setDefaultPushCallback(cordova.getActivity(), cordova.getActivity().getClass());
                    ParseInstallation.getCurrentInstallation().saveInBackground();
                    callbackContext.success();
                } catch (JSONException e) {
                    callbackContext.error("JSONException");
                }
            }
        });
    }

    private void getInstallationId(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                String installationId = ParseInstallation.getCurrentInstallation().getInstallationId();
                callbackContext.success(installationId);
            }
        });
    }

    private void getDeviceToken(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                String deviceToken = (String) ParseInstallation.getCurrentInstallation().get("deviceToken");
                callbackContext.success(deviceToken);
            }
        });
    }

    private void getInstallationObjectId(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                String objectId = ParseInstallation.getCurrentInstallation().getObjectId();
                callbackContext.success(objectId);
            }
        });
    }

    private void getSubscriptions(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                 Set<String> subscriptions = PushService.getSubscriptions(cordova.getActivity());
                 callbackContext.success(subscriptions.toString());
            }
        });
    }

    private void subscribe(final String channel, final CallbackContext callbackContext) {
        Log.v(TAG, "subscribe: "+channel);
        ParsePush.subscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                    callbackContext.success();
                } else {
                    Log.e("com.parse.push", "failed to subscribe to: "+channel, e);
                    callbackContext.error("Failed to subscribe to "+channel);
                }
            }
        });
    }

    private void unsubscribe(final String channel, final CallbackContext callbackContext) {
        Log.v(TAG, "unsubscribe: "+channel);
        ParsePush.unsubscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully unssubscribed from "+channel);
                    callbackContext.success();
                } else {
                    Log.e("com.parse.push", "Failed to unsubscribe from: "+channel, e);
                    callbackContext.error("Failed to unsubscribe from: "+channel);
                }
            }
        });
    }

}

