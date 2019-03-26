package com.sh3h.workmanagerjm.service;


import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.squareup.otto.Bus;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Subscription;

class BaseHandler {
    private static final String TAG = "BaseHandler";
    private static ExecutorService mService = null;

    private BlockingQueue<SyncMessage> mBlockingQueue;
    private MyRunnable mMyRunnable;

    protected MainApplication mMainApplication;
    protected DataManager mDataManager;
    protected ConfigHelper mConfigHelper;
    protected PreferencesHelper mPreferencesHelper;
    protected EventPosterHelper mEventPosterHelper;
    protected Bus mBus;
    protected Subscription mSubscription;

    BaseHandler(MainApplication mainApplication,
                DataManager dataManager,
                ConfigHelper configHelper,
                PreferencesHelper preferencesHelper,
                EventPosterHelper eventPosterHelper,
                Bus bus) {
        mBlockingQueue = null;
        mMyRunnable = null;

        mMainApplication = mainApplication;
        mDataManager = dataManager;
        mConfigHelper = configHelper;
        mPreferencesHelper = preferencesHelper;
        mEventPosterHelper = eventPosterHelper;
        mBus = bus;
    }

    public static void init() {
        if (mService == null || mService.isShutdown()) {
            mService = Executors.newCachedThreadPool();
        }
    }

    static void destroy() {
        if (mService != null) {
            mService.shutdown();
        }
    }

    void start() {
        LogUtil.i(TAG, "start");
        if (mService == null) {
            LogUtil.e(TAG, "failure to start");
            return;
        }

        if (mBlockingQueue == null) {
            mBlockingQueue = new LinkedBlockingQueue<>();
            LogUtil.i(TAG, "create a blocking queue");
        }

        mMyRunnable = new MyRunnable();
        mService.execute(mMyRunnable);
        LogUtil.i(TAG, "create a thread");

        mBus.register(this);
    }

    void post(SyncMessage syncMessage) {
        try {
            if (mBlockingQueue == null) {
                LogUtil.e(TAG, "blocking queue is null");
                return;
            }

            mBlockingQueue.add(syncMessage);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
        }
    }

    protected boolean process(SyncMessage syncMessage) {
        if (syncMessage == null) {
            return false;
        }

        LogUtil.i(TAG, "process " + syncMessage.getOperate());

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        return true;
    }

    void stop() {
        mBus.unregister(this);

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }

        if (mMyRunnable != null) {
            mMyRunnable.stop();
        }
    }

    private class MyRunnable implements Runnable {
        private volatile boolean mIsRunning = true;

        @Override
        public void run() {
            LogUtil.i(TAG, "MyRunnable is running");
            try {
                while (mIsRunning) {
                    SyncMessage syncMessage = mBlockingQueue.take();
                    process(syncMessage);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                LogUtil.e(TAG, "MyRunnable is interrupted");
            } finally {
                LogUtil.i(TAG, "MyRunnable is finished");
            }
        }

        void stop() {
            mIsRunning = false;
        }
    }
}
