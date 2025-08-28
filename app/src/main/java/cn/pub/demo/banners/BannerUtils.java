package cn.pub.demo.banners;

import android.text.TextUtils;
import android.view.ViewGroup;

import io.pubeasy.ad.banner.PubBanner;


public class BannerUtils {

    private static BannerUtils sInstance;
    private PubBanner mPubbanner;


    public synchronized static BannerUtils getInstance() {
        if (sInstance == null) {
            sInstance = new BannerUtils();
        }
        return sInstance;
    }

    public void loadBanner(PubBanner pubbanner, String adUnitId) {
       if (pubbanner != null && !TextUtils.isEmpty(adUnitId)) {
           mPubbanner = pubbanner;
           pubbanner.loadAd(adUnitId);
       }
    }

    public void showBanner(ViewGroup adContainer) {
        if (mPubbanner != null) {
            if(mPubbanner.getParent() != null) {
                ((ViewGroup) mPubbanner.getParent()).removeView(mPubbanner);
            }
            adContainer.addView(mPubbanner);
            mPubbanner.showAd();
        }

    }

    public boolean isReady() {
        return mPubbanner != null && mPubbanner.isReady();
    }

    public void onDestroy() {
        if (mPubbanner != null) {
            mPubbanner.onDestroy();
        }

    }
}
