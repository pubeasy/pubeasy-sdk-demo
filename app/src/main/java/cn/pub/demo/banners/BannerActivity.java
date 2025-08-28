package cn.pub.demo.banners;

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
import io.pubeasy.ad.banner.PubBanner;
import io.pubeasy.ad.banner.PubBannerAdListener;
import io.pubeasy.ad.bean.PubAdError;
import io.pubeasy.ad.bean.PubAdInfo;

/**
 * banner广告
 * banner广告的PubBanner本身是一个view，需要开发者创建后添加到指定位置
 * 广告loaded成功后，Pub SDK会自动的把广告内容填充到PubBanner中
 * banner自带有刷新功能，在Pub后台配置刷新时间，一次loaded后，间隔固定的时间SDK内部会自动触发下一次load并在loaded成功后替换内容
 */
public class BannerActivity extends AppCompatActivity implements View.OnClickListener {

    private BannerUtils bannerUtils;
    private PubBanner pubBanner;
    private ViewGroup adContainer;
    private static final String TAG = "BannerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        adContainer = findViewById(R.id.ad_container);
        bannerUtils = BannerUtils.getInstance();
        findViewById(R.id.btn_load).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        findViewById(R.id.second_page).setOnClickListener(this);
        loadBanner();
    }


    /**
     * --------------------------------------------------------------------------------------------------------------
     * banner的基本用法，如果没有特殊需求，按照如下代码接入即可
     * --------------------------------------------------------------------------------------------------------------
     */

    private void loadBanner() {
        pubBanner = new PubBanner(this);
        pubBanner.closeAutoShow();
        pubBanner.setAdListener(new PubBannerAdListener() {
            @Override
            public void onAdClicked(PubAdInfo pubAdInfo) {
                Toast.makeText(BannerActivity.this, "广告被点击了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdImpression(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdImpression: " + pubAdInfo.adSourceName + "展示了 ecpm " + pubAdInfo.ecpm);
                Toast.makeText(BannerActivity.this, "广告展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdLoaded: " + pubAdInfo.adSourceName + "加载成功 ecpm " + pubAdInfo.ecpm);
                Toast.makeText(BannerActivity.this, "广告加载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoadFailed(PubAdError error) {
                Log.e("BannerActivity", "onAdLoadFailed " + error.getErrorMsg() + " , " + error.getErrorCode());
                Toast.makeText(BannerActivity.this, "广告加载失败" + error.getErrorMsg(), Toast.LENGTH_SHORT).show();
                findViewById(R.id.btn_show).setClickable(false);
                findViewById(R.id.second_page).setClickable(false);
            }

            @Override
            public void onAdClosed(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdClosed: " + pubAdInfo.adSourceName + "广告关闭");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_load) {
            if (pubBanner != null) {
                bannerUtils.loadBanner(pubBanner, TestAdUnitId.BANNER_ADUNITID);
            }
        } else if (view.getId() == R.id.btn_show) {
            if (bannerUtils.isReady()) {
                bannerUtils.showBanner(adContainer);
            } else {
                Toast.makeText(BannerActivity.this, "无可用广告 or 已经展示", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.second_page) {
            // 进入下一页
            Intent intent = new Intent(BannerActivity.this, SecondPageActivity.class);
            intent.putExtra("type", TestAdUnitId.TYPE_BANNER);
            startActivity(intent);
        }
    }
}
