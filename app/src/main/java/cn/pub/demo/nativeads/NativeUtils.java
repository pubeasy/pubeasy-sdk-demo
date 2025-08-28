package cn.pub.demo.nativeads;

import android.view.ViewGroup;

import cn.pub.R;
import io.pubeasy.ad.nativeads.PubNative;

public class NativeUtils {

    private static NativeUtils sInstance;
    private PubNative mPubNative;


    public synchronized static NativeUtils getInstance() {
        if (sInstance == null) {
            sInstance = new NativeUtils();
        }
        return sInstance;
    }

    public void loadNative(PubNative mPubNative) {
        if (mPubNative != null) {
            this.mPubNative = mPubNative;
            this.mPubNative.loadAd();
        }
    }

    public void showNative(ViewGroup adContainer) {
        if (mPubNative != null) {
            mPubNative.showAd(adContainer, R.layout.tp_native_ad_list_item, "");
        }
    }

    public boolean isReady() {
        return mPubNative != null && mPubNative.isReady();
    }

    public void onDestroy() {
        if (mPubNative != null) {
            mPubNative.onDestroy();
        }
    }
}
