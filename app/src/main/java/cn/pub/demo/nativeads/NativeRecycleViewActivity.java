package cn.pub.demo.nativeads;


import static cn.pub.demo.utils.TestAdUnitId.NATIVE_ADUNITID;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.pub.R;
import io.pubeasy.ad.base.PubBaseAd;
import io.pubeasy.ad.base.PubCustomNativeAd;
import io.pubeasy.ad.bean.PubAdError;
import io.pubeasy.ad.bean.PubAdInfo;
import io.pubeasy.ad.nativeads.PubNative;
import io.pubeasy.ad.nativeads.PubNativeAdListener;


public class NativeRecycleViewActivity extends Activity {
    private static final String TAG = "NativeRecycleViewActivity";
    private RecyclerView recycler_native;
    private PubNative pubNative;
    private List<PubCustomNativeAd> mData = new ArrayList<>();
    private NativeRecycleViewAdapter adapter;
    private int newState;
    private int firstVisible;
    private int lastVisible;

    public static final int INTERVAL = 5;
    //0 inside; 1 up; 2;down;3 up down
    public static final int STATUS_INSIDE = 0;
    public static final int STATUS_UP = 1;
    public static final int STATUS_DOWN = 2;
    public static final int STATUS_UPDOWN = 3;

    private int addAdsStatus = STATUS_INSIDE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_native_recycle);

        initData();
        initView();
        initNative();
    }

    private void initData() {
        for (int i = 0; i < 200; i++) {
            mData.add(null);
        }
    }

    private void initView() {
        recycler_native = findViewById(R.id.recycler_native);
        recycler_native.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_native.addItemDecoration(new SpacesItemDecoration(1));

        recycler_native.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                NativeRecycleViewActivity.this.newState = newState;
                checkNeedAddNativeAdToData();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        adapter = new NativeRecycleViewAdapter(this, mData);
        recycler_native.setAdapter(adapter);
    }

    private void checkNeedAddNativeAdToData() {
        if (newState == 0) {
            setStopScrollVisibleCount();

            addNativeAdToData();
        }
    }

    private void setStopScrollVisibleCount() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recycler_native.getLayoutManager();
        if (layoutManager != null) {
            firstVisible = layoutManager.findFirstVisibleItemPosition();
            lastVisible = layoutManager.findLastVisibleItemPosition();
        }
    }

    //可视范围内判断是否需要添加
    private void addAndReloadAds(int i) {
        PubCustomNativeAd pubCustomNativeAd;
        if (i % INTERVAL == 0 && i != 0) {
            if (mData.get(i) == null) {
                pubCustomNativeAd = getNativeAd();
                mData.set(i, pubCustomNativeAd);
                adapter.notifyItemChanged(i);
                loadAd();
            }
        }

    }

    private void addNativeAdToData() {
        if(addAdsStatus == STATUS_INSIDE){
            for (int i = firstVisible; i < lastVisible; i++) {
                addAndReloadAds(i);
            }
        }else {
            if (addAdsStatus == STATUS_UP || addAdsStatus == STATUS_UPDOWN) {
                int upIndex = firstVisible < INTERVAL ? 0 : firstVisible - INTERVAL;
                for (int i = upIndex; i < firstVisible; i++) {
                    addAndReloadAds(i);
                }
            }
            if (addAdsStatus == STATUS_DOWN || addAdsStatus == STATUS_UPDOWN) {
                int downEndIndex = lastVisible + INTERVAL > mData.size() ? mData.size() : lastVisible + INTERVAL;
                for (int i = lastVisible; i < downEndIndex; i++) {
                    addAndReloadAds(i);
                }
            }
        }

    }

    private void initNative() {
        pubNative = new PubNative(this, NATIVE_ADUNITID);
        pubNative.setAdListener(new PubNativeAdListener() {
            @Override
            public void onAdClicked(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdClicked: " + pubAdInfo.adSourceName + "被点击");
            }

            @Override
            public void onAdImpression(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdImpression: " + pubAdInfo.adSourceName + "展示 ecpm " + pubAdInfo.ecpm);
            }

            @Override
            public void onAdShowFailed(PubAdError pubAdError, PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdShowFailed: " + pubAdInfo.adSourceName + "展示失败");
            }

            @Override
            public void onAdLoadFailed(PubAdError pubAdError) {
                Log.e(TAG, "onAdLoadFailed: 加载失败 , code : " + pubAdError.getErrorCode() + ", msg :" + pubAdError.getErrorMsg());
            }

            @Override
            public void onAdClosed(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdClosed: " + pubAdInfo.adSourceName + "广告关闭");
            }

            @Override
            public void onAdLoaded(PubAdInfo pubAdInfo, PubBaseAd tpBaseAd) {
                Log.e(TAG, "onAdLoaded: " + pubAdInfo.adSourceName + "加载成功 ecpm " + pubAdInfo.ecpm);
                checkNeedAddNativeAdToData();
            }
        });
    }

    private PubCustomNativeAd getNativeAd() {
        PubCustomNativeAd pubCustomNativeAd = null;
        if (pubNative != null) {
            pubCustomNativeAd = pubNative.getNativeAd();
            if (pubCustomNativeAd == null) {
                loadAd();
            }
        }
        return pubCustomNativeAd;
    }

    private void loadAd() {
        if (pubNative != null && !pubNative.isReady()) {
            pubNative.loadAd();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pubNative != null){
            pubNative.onDestroy();
        }
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;//空白间隔

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;//左边空白间隔
            outRect.right = space;//右边空白间隔
            outRect.bottom = space;//下方空白间隔
            outRect.top = space;//上方空白间隔
        }
    }
}
