package cn.pub.demo.nativeads;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.pub.R;
import cn.pub.demo.utils.TestAdUnitId;
import io.pubeasy.ad.base.PubBaseAd;
import io.pubeasy.ad.bean.PubAdError;
import io.pubeasy.ad.bean.PubAdInfo;
import io.pubeasy.ad.nativeads.PubNative;
import io.pubeasy.ad.nativeads.PubNativeAdListener;

/**
 * 标准原生广告，这个广告是可以由开发者控制大小，尽可能融入到app的内容中去，从而提升广告的点击和转化
 * <p>
 * native广告分自渲染和模板渲染
 * native 自渲染广告是三方广告平台返回广告素材由开发者来拼接成对于的样式
 * native 模板渲染是三方广告平台返回渲染好的view（很多广告平台可以在对应的后台设置样式），开发者直接添加到一个容器就可以展示出来
 */
public class NativeActivity extends AppCompatActivity implements View.OnClickListener {

    private NativeUtils nativeUtils;
    private PubNative pubNative;
    private ViewGroup adContainer;
    private static final String TAG = "NativeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        adContainer = findViewById(R.id.ad_container);
        nativeUtils = NativeUtils.getInstance();
        findViewById(R.id.btn_load).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        findViewById(R.id.second_page).setOnClickListener(this);
        findViewById(R.id.native_listview).setOnClickListener(this);

    }


    @Override
    protected void onDestroy() {
        if (pubNative != null) {
            //释放资源
            pubNative.onDestroy();
        }
        super.onDestroy();
    }


    /**
     * --------------------------------------------------------------------------------------------------------------
     * native的基本用法，如果没有特殊需求，按照如下代码接入即可
     * --------------------------------------------------------------------------------------------------------------
     */
    private void loadNormalNative() {
        pubNative = new PubNative(NativeActivity.this, TestAdUnitId.NATIVE_ADUNITID);
        pubNative.setAdListener(new PubNativeAdListener() {
            @Override
            public void onAdLoaded(PubAdInfo pubAdInfo, PubBaseAd pubBaseAd) {
                Log.e(TAG, "onAdLoaded: " + pubAdInfo.adSourceName + "加载成功 ecpm " + pubAdInfo.ecpm);
                Toast.makeText(NativeActivity.this, "广告加载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdClicked: " + pubAdInfo.adSourceName + "被点击");
                Toast.makeText(NativeActivity.this, "广告被点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdImpression(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdImpression: " + pubAdInfo.adSourceName + "展示 ecpm " + pubAdInfo.ecpm);
                Toast.makeText(NativeActivity.this, "广告展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdShowFailed(PubAdError pubAdError, PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdShowFailed: " + pubAdInfo.adSourceName + "展示失败");
            }

            @Override
            public void onAdLoadFailed(PubAdError pubAdError) {
                Log.e(TAG, "onAdLoadFailed: 加载失败 , code : " + pubAdError.getErrorCode() + ", msg :" + pubAdError.getErrorMsg());
                Toast.makeText(NativeActivity.this, "广告加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdClosed: " + pubAdInfo.adSourceName + "广告关闭");
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_load) {
            loadNormalNative();
            if (pubNative != null) {
                nativeUtils.loadNative(pubNative);
            }
        } else if (view.getId() == R.id.btn_show) {
            if (nativeUtils.isReady()) {
                nativeUtils.showNative(adContainer);
            } else {
                Toast.makeText(NativeActivity.this, "无可用广告", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.second_page) {
            // 进入下一页
            Intent intent = new Intent(NativeActivity.this, SecondPage.class);
            intent.putExtra("type", TestAdUnitId.TYPE_NATIVE);
            startActivity(intent);
        } else if (view.getId() == R.id.native_listview) {
            // 进入下一页
            Intent intentlist = new Intent(NativeActivity.this, NativeRecycleViewActivity.class);
            startActivity(intentlist);
        }
    }
}
