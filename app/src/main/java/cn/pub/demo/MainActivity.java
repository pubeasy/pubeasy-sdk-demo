package cn.pub.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import cn.pub.R;
import cn.pub.demo.banners.BannerActivity;
import cn.pub.demo.interstititals.InterstitialActivity;
import cn.pub.demo.nativeads.NativeActivity;
import cn.pub.demo.rewarded.RewardedVideoActivity;
import cn.pub.demo.utils.TestAdUnitId;
import io.pubeasy.ad.init.PubSdk;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button  splash_ads, native_advanced_btn, rewarded_video_btn, interstitial_ad, banner_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 海外隐私政策
        setPrivacyConsent();
        initView();
    }


    private void setPrivacyConsent() {
        // Google UMP
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                // 指示用户是否低于同意年龄; true 低于同意年龄
                // 未满同意年龄的用户不会收到 GDPR 消息表单
                .setTagForUnderAgeOfConsent(false)
                .build();

        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this, params, () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(this,
                            loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.

                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    Log.e("PubLog", "授权完成,初始化SDK: ");
                                    // 授权完成,初始化SDK
                                    initPubSDK();
                                }
                            });
                }, (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.


                });


        // 用户已经进行过UMP选择
        if (consentInformation.canRequestAds()) {
            // 授权完成,初始化SDK
            initPubSDK();
        }

        // 集成Google UMP后; 如果美国加州没有投放APP，无需调用
//        checkAreaSetCCPA();

    }

/*    private void setPrivacyConsent() {
        // 用户已经进行过UMP选择
        initTPSDK();

        // 集成Google UMP后; 如果美国加州没有投放APP，无需调用
//        checkAreaSetCCPA();

    }*/

    private void checkAreaSetCCPA() {
        // 判断用户是否已经选择过，返回true表示已经进行过选择，就不需要再次进行GDPR弹窗
        boolean firstShowGDPR = PubSdk.isFirstShowGDPR(this);
        // 查询地区
        PubSdk.checkCurrentArea(this, new PubSdk.OnPubPrivacyRegionListener() {
            @Override
            public void onSuccess(boolean isEu, boolean isCn, boolean isCalifornia) {
                // 获取到相关地域配置后，设置相关隐私API

                // 集成Google UMP后无需处理欧洲地区


                // 表明是美国加州地区，设置CCPA
                if (isCalifornia) {
                    // false 加州用户均不上报数据 ；true 接受上报数据
                    // 默认不上报，如果上报数据，需要让用户选择
                    PubSdk.setCCPADoNotSell(MainActivity.this, false);
                }


                if (!isEu) {
                    initPubSDK();
                }

            }

            @Override
            public void onFailed() {
                // 一般为网络问题导致查询失败，开发者需要自己判断地区，然后进行隐私设置
                // 然后在初始化SDK
                initPubSDK();
            }
        });
    }


    private void initPubSDK() {
        // appId为Pub后台建的应用ID
        if (!PubSdk.getIsInit()) {
            // 初始化是否成功 （可选）
            PubSdk.setPubInitListener(new PubSdk.PubInitListener() {
                @Override
                public void onInitSuccess() {
                    Log.e("PubLog", "onInitSuccess: ");
                 //   ImportSDKUtil.getInstance().showTestTools(MainActivity.this, TestAdUnitId.APPID);
                }
            });
            // 初始化SDK
            PubSdk.initSdk(this, TestAdUnitId.APPID);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton.getId() == R.id.is_personad) {
            // 个性化推荐广告开关；默认是开启状态 true
            PubSdk.setOpenPersonalizedAd(isChecked);
        } else if (compoundButton.getId() == R.id.is_privacyUser) {
            // 隐私信息控制开关；默认是开启状态 true
            PubSdk.setPrivacyUserAgree(isChecked);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.splash_ads) {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
        } else if (view.getId() == R.id.native_ad_advanced) {
            startActivity(new Intent(MainActivity.this, NativeActivity.class));
        } else if (view.getId() == R.id.rewarded_video_ad) {
            Intent intent = new Intent(MainActivity.this, RewardedVideoActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.interstitial_ad) {
            Intent interstitialIntent = new Intent(MainActivity.this, InterstitialActivity.class);
            startActivity(interstitialIntent);
        } else if (view.getId() == R.id.banner_ad) {
            startActivity(new Intent(MainActivity.this, BannerActivity.class));
        }
    }

    private void initView() {
        // 开屏
        splash_ads = findViewById(R.id.splash_ads);
        splash_ads.setOnClickListener(this);

        // 标准原生
        native_advanced_btn = (Button) findViewById(R.id.native_ad_advanced);
        native_advanced_btn.setOnClickListener(this);

        // 激励
        rewarded_video_btn = (Button) findViewById(R.id.rewarded_video_ad);
        rewarded_video_btn.setOnClickListener(this);

        // 插屏
        interstitial_ad = (Button) findViewById(R.id.interstitial_ad);
        interstitial_ad.setOnClickListener(this);

        // 横幅
        banner_btn = (Button) findViewById(R.id.banner_ad);
        banner_btn.setOnClickListener(this);

        ((CheckBox) findViewById(R.id.is_personad)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.is_privacyUser)).setOnCheckedChangeListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
