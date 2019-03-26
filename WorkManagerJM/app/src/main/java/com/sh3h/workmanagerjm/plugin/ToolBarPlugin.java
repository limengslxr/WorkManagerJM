package com.sh3h.workmanagerjm.plugin;

import com.sh3h.mobileutil.util.LogUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by limeng on 2017/1/11.
 * 修改toolBar
 */
public class ToolBarPlugin extends CordovaPlugin {
    private final static String TAG = "ToolBarPlugin";
    private final static String ACTION_UPDATE_TOOLBAR = "updateToolbar";

    public ToolBarPlugin() {
        LogUtil.i(TAG, "ToolBarPlugin");
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

        if (ACTION_UPDATE_TOOLBAR.equals(action)) {
            cordova.getThreadPool().execute(() -> {
                cordova.getActivity().runOnUiThread(() -> {
                            try {
                                cordova.updateToolbar(args.getString(0), args.getBoolean(1),
                                        args.getBoolean(2), args.getBoolean(3));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                );
                callbackContext.success("true");
            });

            cordova.getThreadPool().execute(() ->
                    callbackContext.success(cordova.getAccount())
            );
        } else {
            callbackContext.error("ToolBarPlugin action error");
        }

        return true;
    }

}