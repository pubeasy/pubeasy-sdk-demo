package cn.pub.demo.rewarded;

import android.app.Activity;

import io.pubeasy.ad.interstitial.PubInterstitial;
import io.pubeasy.ad.rewarded.PubReward;

public class VideoUtils {

    private static VideoUtils sInstance;
    private PubReward mPubReward;
    private PubInterstitial  mPubInterstitial;


    public synchronized static VideoUtils getInstance() {
        if (sInstance == null) {
            sInstance = new VideoUtils();
        }
        return sInstance;
    }

    public void loadReward(PubReward mPubReward) {
       if (mPubReward != null) {
           this.mPubReward = mPubReward;
           mPubReward.loadAd();
       }
    }

    public void showReward(Activity activity) {
        if (mPubReward != null) {
            mPubReward.showAd(activity,null);
        }

    }

    public boolean isReady() {
        return mPubReward != null && mPubReward.isReady();
    }

    public void loadInterstitial(PubInterstitial pubInterstitial) {
        if (pubInterstitial != null) {
            mPubInterstitial = pubInterstitial;
            mPubInterstitial.loadAd();
        }
    }

    public void showInterstitial(Activity activity) {
        if (mPubInterstitial != null) {
            mPubInterstitial.showAd(activity,null);
        }

    }

    public boolean isReadyInterstitial() {
        return mPubInterstitial != null && mPubInterstitial.isReady();
    }

    public void onDestroy() {
        if (mPubReward != null) {
            mPubReward.onDestroy();
        }

        if (mPubInterstitial != null) {
            mPubInterstitial.onDestroy();
        }

    }
}
