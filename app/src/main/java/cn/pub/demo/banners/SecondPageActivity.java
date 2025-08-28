package cn.pub.demo.banners;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.pub.R;
import cn.pub.demo.rewarded.VideoUtils;
import cn.pub.demo.utils.TestAdUnitId;


public class SecondPageActivity extends AppCompatActivity {

    private ViewGroup adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        adContainer = findViewById(R.id.ad_container);
        Bundle extras = getIntent().getExtras();
        if(TestAdUnitId.TYPE_BANNER.equals(extras.get("type"))) {
            showBanner();
        }

        if(TestAdUnitId.TYPE_REWARDED.equals(extras.get("type"))) {
            showRewardVideo();
        }

        if(TestAdUnitId.TYPE_INTERSTITIAL.equals(extras.get("type"))) {
            showInterstital();
        }

    }

    private void showBanner() {
        BannerUtils bannerUtils = BannerUtils.getInstance();
        if (bannerUtils.isReady()) {
            bannerUtils.showBanner(adContainer);
        }else{
            Toast.makeText(SecondPageActivity.this, "无可用广告 or 已经展示", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRewardVideo() {
        VideoUtils videoUtils = VideoUtils.getInstance();
        if (videoUtils.isReady()) {
            videoUtils.showReward(SecondPageActivity.this);
        }else{
            Toast.makeText(SecondPageActivity.this, "无可用广告 or 已经展示", Toast.LENGTH_SHORT).show();
        }
    }

    private void showInterstital() {
        VideoUtils videoUtils = VideoUtils.getInstance();
        if (videoUtils.isReadyInterstitial()) {
            videoUtils.showInterstitial(SecondPageActivity.this);
        }else{
            Toast.makeText(SecondPageActivity.this, "无可用广告 or 已经展示", Toast.LENGTH_SHORT).show();
        }
    }
}