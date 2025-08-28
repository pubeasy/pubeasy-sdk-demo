package cn.pub.demo.utils;


public class TestAdUnitId {
    //测试提供
    //应用ID
    //AdUnitID，Pub后台设置 对应广告类型的广告位（非三方广告网络的placementId）
    //正式上线时需要替换成申请的广告位ID和您的应用ID
    public static final String APPID = "6F5EDDDF0DA174AF1ECFAE11AFB9C001";

    // 测试的adUnitId在Pub后台配置了部分广告平台
    // 如果复制adUnitId到自己项目中但是项目中没集成这些广告平台就会报adapter找不到
    public static final String REWRDVIDEO_ADUNITID = "FFB99979271F91CFCA427E8E984D153D";
    public static final String NATIVE_ADUNITID = "2985C8017A674F25B487452E83E8C696";
    public static final String NATIVEBANNER_ADUNITID = "9F4D76E204326B16BD42FA877AFE8E7D";
    public static final String INTERSTITIAL_ADUNITID = "2CC8169FCBF2A4BCEE9B87FA8567FF6B";
    public static final String BANNER_ADUNITID = "B176F62CCEBFFB8AF8E86924CEC60BB3";
    public static final String SPLASH_ADUNITID = "7038FE2F9FC5505329779857159AB623" ;


    public static final String ENTRY_AD_REWARD = "177010A4403105" ;
    public static final String ENTRY_AD_INTERSTITIAL = "01EAD2CCED1870" ;
    public static final String TYPE_NATIVE = "native" ;
    public static final String TYPE_NATIVEBANNER = "nativebanner" ;
    public static final String TYPE_BANNER = "banner" ;
    public static final String TYPE_REWARDED = "reward" ;
    public static final String TYPE_INTERSTITIAL = "interstitial" ;
}
