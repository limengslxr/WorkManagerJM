package com.sh3h.workmanagerjm.plugin;

import com.sh3h.mobileutil.util.LogUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

/**
 * Created by limeng on 2017/1/9.
 * 用户信息
 */

public class UserInfoPlugin extends CordovaPlugin {
    private final static String TAG = "UserInfoPlugin";
    private final static String ACTION_SETACCOUNT = "getAccount";

    public UserInfoPlugin() {
        LogUtil.i(TAG, "UserInfoPlugin");
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          The action to execute.
     * @param args            JSONArry of arguments for the plugin.
     * @param callbackContext The callback id used when calling back into JavaScript.
     * @return True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        if ((action == null) || (args == null) || (callbackContext == null)) {
            LogUtil.i(TAG, "execute");
            return false;
        }

        if (ACTION_SETACCOUNT.equals(action)) {
            cordova.getThreadPool().execute(() ->
                    callbackContext.success(cordova.getAccount())
            );
        } else {
            callbackContext.error("UserInfoPlugin action error");
        }

        return true;
    }

}
