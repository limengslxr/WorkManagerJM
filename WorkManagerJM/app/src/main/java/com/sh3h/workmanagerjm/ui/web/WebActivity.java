package com.sh3h.workmanagerjm.ui.web;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.squareup.otto.Bus;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewEngine;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.PluginManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


public class WebActivity extends ParentActivity implements WebMvpView,
        SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loading_process)
    SmoothProgressBar mSmoothProgressBar;

    @BindView(R.id.ll_content)
    LinearLayout mLinearLayout;

    @Inject
    Bus mEventBus;

    @Inject
    WebPresenter mWebPresenter;

    // http://113.107.139.145:24578/GrabWorkJM/src/app.m.html#/mobile/grabWork
    private final static String TAG = "WebActivity";
    private final static String DEFAULT_BASE_URL = "http://113.107.139.145:24578/";
    private final static String DEFAULT_GRAB_URL = "GrabWorkJM/src/";
    private static final String PARAMS = "params";
    private static final String REL_URL = "relUrl:";
    private static final String SEPARATOR = ";";
    private Unbinder mUnbinder;
    private String params;
    private String relUrl;

    // The webview for our app
    protected CordovaWebView appView;

    // Keep app running when pause is received. (default = true)
    // If true, then the JavaScript and native code continue to run in the background
    // when another application (activity) is started.
    private boolean keepRunning = true;

    // Read from config.xml:
    private CordovaPreferences preferences;
    private ArrayList<PluginEntry> pluginEntries;
    private CordovaInterfaceImpl cordovaInterface;
    private boolean isDestroyed;
    private String account;
    private MenuItem searchMenu, refreshMenu, grabWorkMenu;
    private boolean searchViewExpand = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mWebPresenter.attachView(this);
        mEventBus.register(this);

        initToolBar(TextUtil.EMPTY);

        parseParams(savedInstanceState);

        initConfig(savedInstanceState);
        isDestroyed = false;

        controlData();
        setSwipeBackEnable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mWebPresenter.detachView();
        mUnbinder.unbind();
        mEventBus.unregister(this);

        if (this.appView != null) {
            appView.handleDestroy();
        }
        isDestroyed = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        searchMenu = menu.findItem(R.id.action_search);
        refreshMenu = menu.findItem(R.id.action_refresh);
        grabWorkMenu = menu.findItem(R.id.action_grab_work);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        //给searchView设置展开收缩事件，展开时过滤按钮去消失，收缩时过滤按钮显示
        MenuItemCompat.setOnActionExpandListener(searchMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchViewExpand = true;
                refreshMenu.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchViewExpand = false;
                refreshMenu.setVisible(true);
                return true;
            }
        });
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (appView != null) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    searchMenu.setVisible(true);
                    refreshMenu.setVisible(true);
                    grabWorkMenu.setVisible(false);
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.action_refresh:
                    appView.getPluginManager().postMessage("refreshMenu", null);
                    break;
                case R.id.action_grab_work:
                    appView.getPluginManager().postMessage("grabWork", null);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtil.isNullOrEmpty(query)) {
            return false;
        }

        if (appView != null) {
            appView.getPluginManager().postMessage("searchKey", query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void initConfigEnd() {
        super.initConfigEnd();
        if (initStep == ConstantUtil.InitStep.CLEAN_DATA) {
            loadUrl(getUrl());
        }
    }

    private void controlData() {
        if (initStep > ConstantUtil.InitStep.CLEAN_DATA) {
            loadUrl(getUrl());
        }
    }

    private void parseParams(Bundle savedInstanceState) {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                params = intent.getStringExtra(ConstantUtil.TaskEntrance.PARAMES);
                account = intent.getStringExtra(ConstantUtil.TaskEntrance.ACCOUNT);
            } else if (savedInstanceState != null) {
                params = savedInstanceState.getString(PARAMS);
                account = savedInstanceState.getString(ACCOUNT);
            }

            if (TextUtil.isNullOrEmpty(params)) {
                return;
            }

            // relative url
            int totalLength = params.length();
            int length = REL_URL.length();
            int start = params.indexOf(REL_URL);
            int end = params.indexOf(SEPARATOR);
            if ((start <= -1) || (start + length > end) || (end >= totalLength)) {
                return;
            }
            relUrl = params.substring(start + length, end);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i(TAG, "---parseParams---" + e.getMessage());
        }
    }

    private void initToolBar(String title) {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    private void initConfig(Bundle savedInstanceState) {
        // need to activate preferences before super.onCreate to avoid "requestFeature() must be called before adding content" exception
        loadConfig();

        cordovaInterface = makeCordovaInterface();
        if (savedInstanceState != null) {
            cordovaInterface.restoreInstanceState(savedInstanceState);
        }
    }

    protected void initCordova() {
        appView = makeWebView();
        createViews();
        if (!appView.isInitialized()) {
            appView.init(cordovaInterface, pluginEntries, preferences);
        }
        cordovaInterface.onCordovaInit(appView.getPluginManager());

        // Wire the hardware volume controls to control media if desired.
        String volumePref = preferences.getString("DefaultVolumeStream", "");
        if ("media".equals(volumePref.toLowerCase(Locale.ENGLISH))) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }

    @SuppressWarnings("deprecation")
    protected void loadConfig() {
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(this);
        preferences = parser.getPreferences();
        preferences.setPreferencesBundle(getIntent().getExtras());
        //launchUrl = parser.getLaunchUrl();
        pluginEntries = parser.getPluginEntries();
        //Config.parser = parser;
    }

    //Suppressing warnings in AndroidStudio
    @SuppressWarnings({"deprecation", "ResourceType"})
    protected void createViews() {
        //Why are we setting a constant as the ID? This should be investigated
        appView.getView().setId(100);
        appView.getView().setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mLinearLayout.addView(appView.getView());

        if (preferences.contains("BackgroundColor")) {
            int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLACK);
            // Background of activity:
            appView.getView().setBackgroundColor(backgroundColor);
        }
        appView.getView().requestFocusFromTouch();
    }

    /**
     * Construct the default web view object.
     * <p/>
     * Override this to customize the webview that is used.
     */
    protected CordovaWebView makeWebView() {
        return new CordovaWebViewImpl(makeWebViewEngine());
    }

    protected CordovaWebViewEngine makeWebViewEngine() {
        return CordovaWebViewImpl.createEngine(this, preferences);
    }

    protected CordovaInterfaceImpl makeCordovaInterface() {
        return new CordovaInterfaceImpl(this) {
            @Override
            public Object onMessage(String id, Object data) {
                // Plumb this to CordovaActivity.onMessage for backwards compatibility
                if (isDestroyed) {
                    return "destroy";
                }
                return WebActivity.this.onMessage(id, data);
            }

            @Override
            public String getAccount() {
                return account + "#" + mWebPresenter.getBaseUrl();
            }

            @Override
            public boolean updateToolbar(String title, boolean showSearch, boolean showRefresh,
                                         boolean showGrabWork) {
                toolbar.setTitle(title);
                if (showGrabWork && searchViewExpand) {
                    searchMenu.collapseActionView();
                }
                searchMenu.setVisible(showSearch);
                refreshMenu.setVisible(showRefresh);
                grabWorkMenu.setVisible(showGrabWork);
                return true;
            }
        };
    }

    private String getUrl() {
        String url = mWebPresenter.getBaseUrl();
        if (TextUtil.isNullOrEmpty(url)) {
            url = DEFAULT_BASE_URL;
        }

        url += TextUtil.isNullOrEmpty(mWebPresenter.getGrabUrl())
                ? DEFAULT_GRAB_URL : mWebPresenter.getGrabUrl();

        if (!TextUtil.isNullOrEmpty(relUrl)) {
            url += relUrl;
        }

        // http://113.107.139.145:24578/GrabWorkJM/src/app.m.html#/mobile/grabWork
        return url;
    }

    /**
     * Load the url into the webview.
     */
    private void loadUrl(String url) {
        if (appView == null) {
            initCordova();
        }

        if (NetworkUtil.isNetworkConnected(MainApplication.get(this))) {
            appView.getView().setVisibility(View.VISIBLE);
        } else {
            //无网络连接
            appView.getView().setVisibility(View.GONE);
        }
        // If keepRunning
        this.keepRunning = preferences.getBoolean("KeepRunning", true);

//        if (MainApplication.get(this).isClearingCache()) {
//            MainApplication.get(this).setIsClearingCache(false);
//            appView.clearCache();
//        }
        appView.loadUrlIntoView(url, true);
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     */
    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "Paused the activity.");

        if (this.appView != null) {
            // CB-9382 If there is an activity that started for result and main activity is waiting for callback
            // result, we shoudn't stop WebView Javascript timers, as activity for result might be using them
            boolean keepRunning = this.keepRunning || this.cordovaInterface.activityResultCallback != null;
            this.appView.handlePause(keepRunning);
        }
    }

    /**
     * Called when the activity receives a new intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Forward to plugins
        if (this.appView != null) {
            this.appView.onNewIntent(intent);
        }
    }

    /**
     * Called when the activity will start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, "Resumed the activity.");

        if (this.appView == null) {
            return;
        }
        // Force window to have focus, so application always
        // receive user input. Workaround for some devices (Samsung Galaxy Note 3 at least)
        this.getWindow().getDecorView().requestFocus();

        this.appView.handleResume(this.keepRunning);
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(TAG, "Stopped the activity.");

        if (this.appView == null) {
            return;
        }
        this.appView.handleStop();
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(TAG, "Started the activity.");

        if (this.appView == null) {
            return;
        }
        this.appView.handleStart();
    }

    @SuppressLint("NewApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        // Capture requestCode here so that it is captured in the setActivityResultCallback() case.
        cordovaInterface.setActivityResultRequestCode(requestCode);
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode The request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param intent      An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        LogUtil.d(TAG, "Incoming Result. Request code = " + requestCode);
        super.onActivityResult(requestCode, resultCode, intent);
        cordovaInterface.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * Report an error to the host application. These errors are unrecoverable (i.e. the main resource is unavailable).
     * The errorCode parameter corresponds to one of the ERROR_* constants.
     *
     * @param errorCode   The error code corresponding to an ERROR_* value.
     * @param description A String describing the error.
     * @param failingUrl  The url that failed to load.
     */
    public void onReceivedError(final int errorCode, final String description, final String failingUrl) {
        final WebActivity me = this;

        // If errorUrl specified, then load it
        final String errorUrl = preferences.getString("errorUrl", null);
        if ((errorUrl != null) && (!failingUrl.equals(errorUrl)) && (appView != null)) {
            // Load URL on UI thread
            me.runOnUiThread(() ->
                    me.appView.showWebPage(errorUrl, false, true, null));
        }
        // If not, then display error dialog
        else {
            final boolean exit = !(errorCode == WebViewClient.ERROR_HOST_LOOKUP);
            me.runOnUiThread(() -> {
                if (exit) {
                    me.appView.getView().setVisibility(View.GONE);
                    me.displayError("Application Error", description + " (" + failingUrl + ")", "OK", true);
                }
            });
        }
    }

    /**
     * Display an error dialog and optionally exit application.
     */
    public void displayError(final String title, final String message, final String button, final boolean exit) {
        final WebActivity me = this;
        me.runOnUiThread(() -> {
                    try {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(me);
                        dlg.setMessage(message);
                        dlg.setTitle(title);
                        dlg.setCancelable(false);
                        dlg.setPositiveButton(button, (dialog, which) -> {
                            dialog.dismiss();
                            if (exit) {
                                finish();
                            }
                        });
                        dlg.create();
                        dlg.show();
                    } catch (Exception e) {
                        finish();
                    }
                }
        );
    }

    /**
     * Called when a message is sent to plugin.
     *
     * @param id   The message id
     * @param data The message data
     * @return Object or null
     */
    public Object onMessage(String id, Object data) {
        if ("onReceivedError".equals(id)) {
            JSONObject d = (JSONObject) data;
            try {
                this.onReceivedError(d.getInt("errorCode"), d.getString("description"), d.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if ("exit".equals(id)) {
            finish();
        } else if ("onPageStarted".equals(id)) {
            if (mSmoothProgressBar != null) {
                mSmoothProgressBar.setVisibility(View.VISIBLE);
            }
        } else if ("onPageFinished".equals(id)) {
            if (mSmoothProgressBar != null) {
                mSmoothProgressBar.setVisibility(View.GONE);
            }
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        cordovaInterface.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putString(PARAMS, params);
        outState.putString(ACCOUNT, account);
    }

    /**
     * Called by the system when the device configuration changes while your activity is running.
     *
     * @param newConfig The new device configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.appView == null) {
            return;
        }
        PluginManager pm = this.appView.getPluginManager();
        if (pm != null) {
            pm.onConfigurationChanged(newConfig);
        }
    }

    /**
     * Called by the system when the user grants permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        try {
            cordovaInterface.onRequestPermissionResult(requestCode, permissions, grantResults);
        } catch (JSONException e) {
            LogUtil.d(TAG, "JSONException: Parameters fed into the method are not valid");
            e.printStackTrace();
        }

    }
}
