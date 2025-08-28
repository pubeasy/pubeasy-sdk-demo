package cn.pub.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.pub.R;
import cn.pub.demo.utils.TestAdUnitId;
import io.pubeasy.ad.bean.PubAdError;
import io.pubeasy.ad.bean.PubAdInfo;
import io.pubeasy.ad.splash.PubSplash;
import io.pubeasy.ad.splash.PubSplashAdListener;


/**
 * 开屏广告
 * 开屏广告是打开app的时候展示一个3-5s的全屏的广告
 * 开屏广告分冷启动和热启动，冷启动时要尽可能提前开始加载广告，这样才能确保在进入app之前加载到并展示广告
 * 热启动是app切换到后台，并没有真正的退出，这种情况下要能检测到并提前加载广告
 * <p>
 * 开屏广告一般要配合app的启动页来使用，在加载的时间先给用户看启动页，等广告加载成功后展示广告，广告结束进入app内部
 */
public class SplashActivity extends AppCompatActivity {

    private PubSplash pubSplash;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.splash_start_loadad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 加载广告
                loadSplashAd();
            }
        });


        findViewById(R.id.splash_start_showad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 广告加载成功后调用show方法 展示广告
                // 传入一个容器（容器一般要求全屏或者至少占屏幕75%以上，其余部分可以展示app的logo信息）
                if (pubSplash != null) {
                    pubSplash.showAd(findViewById(R.id.splash_container));

                }
            }
        });


        // 开屏广告一般要配合app的启动页来使用，在加载的时间先给用户看启动页，等广告加载成功后展示广告，广告结束进入app内部
        // 启动超时定时器
        startTimeoutTimer();

    }

    private void startTimeoutTimer() {
        // 这里要做一个超时判断，如果超过xx秒以后没有广告返回，那么需要自动跳转到app内部，不影响app的使用
    }


    //开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (KeyEvent.KEYCODE_BACK == keyCode || KeyEvent.KEYCODE_HOME == keyCode) {
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * --------------------------------------------------------------------------------------------------------------
     * 开屏的基本用法，如果没有特殊需求，按照如下代码接入即可
     * --------------------------------------------------------------------------------------------------------------
     */
    private void loadSplashAd() {
        // 初始化广告位,注意快手的sdk需要传入的activity是FragmentActivity，否则无法展示快手开屏
        if (pubSplash == null) {
            pubSplash = new PubSplash(SplashActivity.this, TestAdUnitId.SPLASH_ADUNITID);
        }
        // 设置监听
        pubSplash.setAdListener(new PubSplashAdListener() {
            @Override
            public void onAdClicked(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdClicked: ");
                SplashActivity.this.finish();
            }

            @Override
            public void onAdImpression(PubAdInfo pubAdInfo) {
                Log.d(TAG, "onAdImpression:  ecpm " + pubAdInfo.ecpm);
            }

            @Override
            public void onAdClosed(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdClosed: ");
                // 广告关闭后，要把开屏页面关闭，如果是跟内容在同一个activity，这里把开屏的容器remove掉
                SplashActivity.this.finish();
            }

            @Override
            public void onAdLoaded(PubAdInfo pubAdInfo) {
                Log.e(TAG, "onAdLoaded:  ecpm " + pubAdInfo.ecpm);
                // 加载成功后展示广告
                //======================================================================================================
                // 这里一定要注意，需要判断一下是否已经进入app内部，如果加载时间过长，已经进入到app内部，这次load结果就不展示了
                Toast.makeText(SplashActivity.this, "广告加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoadFailed(PubAdError pubAdInfo) {
                Log.e(TAG, "onAdLoadFailed: "+pubAdInfo.getErrorMsg()+pubAdInfo.getErrorCode());
                // 广告加载失败
                //======================================================================================================
                // 这里一定要注意，需要判断一下是否已经进入app内部，如果加载时间过长，已经进入到app内部，这次load结果就不展示了
                Toast.makeText(SplashActivity.this, "广告加载失败", Toast.LENGTH_SHORT).show();


               // SplashActivity.this.finish();
            }

        });

        // 开始加载开屏
        Toast.makeText(SplashActivity.this, "广告加载中", Toast.LENGTH_SHORT).show();
        pubSplash.loadAd(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pubSplash != null) {
            pubSplash.onDestroy();
        }
    }

}
