A demo app that shows how to load and show ads using Pubeasy SDK plugin.  
Dependency versions:  
Pubeasy SDK 14.1.20.1


# Getting Started with Integration
```
project’s build.gradle
repositories {
  maven {
            url = uri("https://sdk-maven.pubeasy.io/repository/pubeasy")
        }
		}
```
## app’s build.gradle
```javascript
   //noinspection GradleCompatible
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.appcompat:appcompat:1.3.0-alpha02'
  
     api'io.pubeasy:pub-io:14.1.21.1'

   implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.squareup.okhttp3:okhttp:4.9.0'
//获取gaid
     api('com.google.android.gms:play-services-ads-identifier:18.2.0')
```
##AndroidManifest.xml
```javascript
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="your.app.package.name">
    <uses-permission android:name="android.permission.AD_ID" />
    <application>
    </application>
</manifest>
```
##ProGuard Configuration
```javascript
-keep  class io.pubeasy.ad.** { *; }
-keep public class com.tradplus.** { *; }
-keep class com.tradplus.ads.** { *; }
```
##Initialization
```javascript
   PubSdk.setPubInitListener(new PubSdk.PubInitListener() {
                @Override
                public void onInitSuccess() {
                    Log.i("PubLog", "onInitSuccess: ");
                }
            });
            // 初始化SDK
            PubSdk.initSdk(this, TestAdUnitId.APPID);
```
##Ad type Integration

| Advertisement Type        | Explanation   |
| --------   | --------  |
|tInterstitial ad      | Interstitial advertisements are generally full-screen, and the calling time is when the page is switched. Generally, there are two types of pictures and videos, and some channels will have customized interstitial screens.  |
| Rewarded video        |   Rewarded video advertisements are generally full-screen 15-30s videos. The calling time is when rewarding the user or obtaining certain items. If the user successfully finishes watching the advertisement, the reward will be issued to the user.  |
| Splash ads        |    Splash Ad is generally a 3-5s full-screen ad that is displayed when the app starts.   |
| native         |    The size of native ads can be controlled by developers and integrated into the content of the app as much as possible, so as to improve the click and conversion of ads.   |
| Banner ads        |    Banner ads are rectangular ads that appear at the top or bottom of a device's screen. Banner ads stay on the screen while the user interacts with the app and support auto-refresh.    |

# Privacy Regulations

**In order to protect the interests and privacy of our developers and your users, and conduct business in compliance with relevant laws, regulations, policies and standards。**

---

## Step 1 、Check the current region
+ Android V8.4.0.1 began to support
+ <font style="color:#dd0000;">Call this method before initializing the SDK</font>

```java
PubSdk.checkCurrentArea(this,new PubSdk.OnPubPrivacyRegionListener() {
		@Override
		public void onSuccess(boolean isEu, boolean isCn, boolean isCalifornia) {
			// After obtaining the relevant regional configuration, set the relevant privacy API, and then initialize the SDK
			if(isEu) {
				// Indicates that it is the European region, set GDPR
			}
			if(isCalifornia){
				// Indicates that it is the California region of the United States, set CCPA
			}
		}

		@Override
		public void onFailed() {
			// Generally, the query fails due to network problems. Developers need to judge the region by themselves, and then set the privacy
		}
});
```

## Step 2、CCPA
It mainly introduces how to set CCPA in the Android project: ：

The California Consumer Privacy Act (CCPA) is the first comprehensive privacy law in the United States. Signed into law in late June 2018, it provides California consumers with a variety of privacy rights. Businesses subject to the CCPA will have several obligations to these consumers, including disclosure of information, consumer rights similar to the European Union's General Data Protection Regulation (GDPR), the right to "opt out" of certain data transfers, and the right to "opt-in" not The Rights of Adults.

---

#### When to set
+ <font style="color:#dd0000;">Developers below V8.4.0.1 have to judge the region by themselves</font>，If they are in the California region of the United States, they need to set CCPA and then initialize the SDK.
+ <font style="color:#dd0000;">V8.4.0.1 and above, you can call the</font><font style="color:#dd0000;"> </font>`checkCurrentArea()`<font style="color:#dd0000;"> </font><font style="color:#dd0000;">method to determine the region</font>（see above to view the current area introduction), set CCPA when the monitor callback isCalifornia returns true, and then initialize the SDK.

#### Pub API
| Platform | Method | Note |
| --- | --- | --- |
| Android | `PubSdk.setCCPADoNotSell(context, boolean);` | `false`<br/> California users do not report data ；`true`<br/> accept reported data |
| Unity3dAndroid | `PubSdk.setCCPADoNotSell(boolean);` | `false`<br/> California users do not report data ；`true`<br/> accept reported data |


---

## Step 3、COPPA
It mainly introduces how to set COPPA in the Android project:

The Children's Online Privacy Protection Act mainly targets the online collection of personal information of children under the age of 13.

The protection law stipulates that website administrators should abide by privacy rules, must explain when to ask for consent from children's parents and provide verifiable methods, and website administrators must protect children's online privacy and safety, including restricting sales to children under the age of 13.

---

+ <font style="color:#dd0000;">Must be called before initializing </font><font style="color:#DF2A3F;">PubSdk</font> <font style="color:#dd0000;">SDK</font>
+ If the application is for adults, you can upload it directly false

#### API
| Platform | Method | Note |
| --- | --- | --- |
| Android | `PubSdk.setCOPPAIsAgeRestrictedUser(context, boolean);` | `false`<br/> show that it is not a child ；`true`<br/> show that it is a child |
| Unity3dAndroid | `PubSdk.setCOPPAIsAgeRestrictedUser(boolean);` | `false`<br/> show that it is not a child ；`true`<br/> show that it is a child |


#### Start.io
+ PubV9.3.0.1 and above versions support Start.io to configure Coppa in the AndroidManifest.xml file
+ The app is aimed at a mixed audience (that is, for everyone, including children and families), is a mixed user profile `true`, is not a mixed user profile `false`

```csharp
<meta-data
	android:name="com.startapp.sdk.MIXED_AUDIENCE"
	android:value="true"/>
```

+ Indicates whether a particular end user is a child, is a child user pass `true`, is not a child user pass `false`

```csharp
<meta-data
	android:name="com.startapp.sdk.CHILD_DIRECTED"
	android:value="true"/>
```

## Step 4、GDPR
It mainly introduces how to set GDPR in the Android project:

The General Data Protection Regulation (GDPR) is a regulation of data protection and privacy law for all citizens of the European Union (EU) and European Economic Area (EEA). We have added a privacy permission setting in the SDK. Please check the configuration below and complete the SDK integration.

On May 25, 2018, after the GDPR came into effect, Twitter, WhatsApp and other social apps updated their user terms, saying that they would prohibit teenagers under the age of 16 from using these apps. This is because there are strict regulations on the protection of children's personal information in the GDPR.

### Android platform settings GDPR
Android Access Reference [Google UMP](https://developers.google.com/admob/android/privacy) set UMP

##### APIs
| Note | method | Remark |
| --- | --- | --- |
| Are you in the EU | `PubSdk.isEUTraffic(context);` | Need to be called in setting GDPR Listener `success`<br/> callback |
| Set GDPR level | `PubSdk.setGDPRDataCollection(context,level);` | Parameter two: PERSONALIZED device data is allowed to be reported; NONPERSONALIZED device data is not allowed to be reported |
| Get the GDPR rating | `PubSdk.getGDPRDataCollection(context);` | Return value 0 means agree, 1 means disagree |
| Is it the first time the user selects | `PubSdk.isFirstShowGDPR(context);` | By default, `false`<br/> no selection has been made; `true`<br/> indicates that the user has selected |
| Set up GDPR children | `PubSdk.setGDPRChild(context,boolean);` | `true`<br/> Indicates that the user is a child |


### Unity3DAndroid platform settings GDPR
Unity3DAndroid Access Reference<font style="color:#117CEE;"> </font>[<font style="color:#117CEE;">Google Unity UMP</font>](https://developers.google.com/admob/unity/privacy)<font style="color:#117CEE;"> </font>set UMP

##### APIs
+ Other version API

| Note | Method | Remark |
| --- | --- | --- |
| Are you in the EU | PubSdk.isEUTraffic(); | Need to be called in setting GDPR Listener success callback |
| Set GDPR level | `PubSdk.setGDPRDataCollection(int);` | 0 The device data is allowed to be reported; 1 The device data is not allowed to be reported |
| Get the GDPR rating | `PubSdk.getGDPRDataCollection();` | Return value 0 means agree, 1 means disagree |
| Is it the first time the user selects | `PubSdk.isFirstShow();` | By default, `false`<br/> no selection has been made; `true`<br/> indicates that the user has selected |
| Set up GDPR children | `PubSdk.setGDPRChild(boolean);` | `true`<br/> Indicates that the user is a child, only supported by Admob's Support |


## Step 5、How to set LGPD
Lei Geral de Proteção de Dados (LGPD)  is a comprehensive Brazilian data protection law, effective September 18, 2020, that provides individuals with broader data rights and increases compliance responsibilities for organizations. At its core, the LGPD is about giving Brazilian residents greater control over their personal data and giving national regulators new powers to impose hefty fines on organizations that violate the law, with rights and protections similar to those afforded to European residents by the GDPR.

+ Must be called before initializing TradPlus SDK
+ Only need to call in Brazil, do not set in non-Brazilian regions

| Note | Method | Remark |
| --- | --- | --- |
| Set LGPD level | `PubSdk.setLGPDConsent(Context context, int consent)` | Parameter 2: 0, device data is allowed to be reported; 1, device data is not allowed to be reported |
| Get LGPD rating | `PubSdk.getLGPDConsent(Context context)` | Return value 0 means agree, 1 means disagree |


## Step 6、Google Content Rating
+ V9.4.0.1 began to support
+ Called before requesting an ad, taking a rewarded video as an example:

```java
//digital content tags all audiences
private String mAdContentRating = RequestConfiguration.MAX_AD_CONTENT_RATING_G;
 ...
Map<String, Object> mLocalExtras = new HashMap<>();
mLocalExtras.put("max_ad_content_rating", mAdContentRating);
rewardAd.setCustomParams(mLocalExtras);

// Called before requesting an ad
rewardAd.loadAd();
```

+ digital content label

| digital content label | illustrate |
| --- | --- |
| RequestConfiguration.MAX_AD_CONTENT_RATING_G | all audiences |
| RequestConfiguration.MAX_AD_CONTENT_RATING_T | teenager |
| RequestConfiguration.MAX_AD_CONTENT_RATING_MA | aldult |
| RequestConfiguration.MAX_AD_CONTENT_RATING_PG | Need to be accompanied by parents to watch |

# Banner
## **1、Load an ad**
+ Developers can preload ads before displaying them.
+ PubBanner is a ViewGroup, the size and position can be customized, developers need to add PubBanner to the specified position.
+ Create an advertisement object PubBanner, some advertisement platforms require acitivity to be passed in, otherwise the advertisement cannot be loaded successfully

```plain
PubBanner pubBanner = new PubBanner(activity);
pubBanner.setAdListener(new PubBannerAdListener());
pubBanner.loadAd("AdUnitID");

// It is recommended to use FrameLayout. If you use LinearLayout, you need to set layoutParams while addView
adContainer.addView(pubBanner);
```

## **2、Show banner  ad**
+ After the ad is loaded successfully, Pub will add the ad directly to the PubBanner without calling the showAd() method;

### 3、Destroy an ad
```plain
pubBanner.onDestroy();
pubBanner = null；
```



## **4、Register Ad Event Callback**
+ Note: Don't perform the retry loading method ad in `onAdFailed` callback – it'll cause a lot of useless requests and could make your app run slowly.

```plain
pubBanner.setAdListener(new PubBannerAdListener() {

		@Override // Callback when the first ad source is loaded successfully；A load will only be called back once
		public void onAdLoaded(PubAdInfo pubAdInfo) {}

		@Override // Banner ad clicked
		public void onAdClicked(PubAdInfo pubAdInfo) {}

		@Override // Banner ad appears on the screen
		public void onAdImpression(PubAdInfo pubAdInfo) {}

		@Override // Banner ad failed to load
		public void onAdLoadFailed(PubAdError error) {}

		@Override // Banner ad closed
		public void onAdClosed(PubAdInfo pubAdInfo) {}
});

```

# Native Ads
## **1、Load an ad**
+ Developers can preload ads before displaying them.
+ To request a native ad, you need to declare a PubNative object first, set the listener and load the native creative.

```plain
PubNative pubNative = new PubNative(activity，"AdUnitID");
pubNative.setAdListener(new PubNativeAdListener());
pubNative.loadAd();
```

## **2、Show native ad**
+ When the display opportunity arrives, check whether there is an available ad through the isReady() method; or monitor whether the ad is called back onAdLoaded.
+ AdContainer is the container for displaying ads.Pub will add the loaded ad to the container.
+ layoutId is the layout file，The default layout file is provided in the [TradPlusSDK download platform](https://docs.tradplusad.com/en/docs/tradplussdk_android_doc_v6/download/) zip, the developer can change the layout style, but cannot change the `android:id` resource ID

```plain
pubNative.showAd(adContainer, layoutId);
```

## **3、Register Ad Event Callback**
+ Note: Don't perform the retry loading method ad in `onAdFailed` callback – it'll cause a lot of useless requests and could make your app run slowly.

```plain
pubNative.setAdListener(new PubNativeAdListener() {

		@Override // Callback when the first ad source is loaded successfully；A load will only be called back once
		public void onAdLoaded(PubAdInfo pubAdInfo, PubBaseAd pubBaseAd) {}

		@Override // Native ad clicked
		public void onAdClicked(PubAdInfo pubAdInfo) {}

		@Override // Native ad appears on the screen
		public void onAdImpression(PubAdInfo pubAdInfo) {}

		@Override // Native ad failed to load
		public void onAdLoadFailed(PubAdInfo pubAdInfo) {}
		
		@Override // Native ad is shown failed（Some advertising platforms support）
		public void onAdShowFailed(PubAdInfo pubAdInfo, PubAdInfo pubAdInfo) {}

		@Override // Native ad closed
		public void onAdClosed(PubAdInfo pubAdInfo) {}
});

```
# Splash
## **1、Load an ad**
+ Developers can preload ads before displaying them.
+ Create an advertisement object PubSplash, some advertisement platforms require acitivity to be passed in, otherwise the advertisement cannot be successfully loaded
+ You don't need to pass in the container when loadingAd, you can pass in the container when displaying the ad

```plain
PubSplash pubSplashpubSplash = new PubSplash(activity,"AdUnitID");
pubSplash.setAdListener(new PubSplashAdListener());
pubSplash.loadAd(null);
```

## **2、Show splash  ad**
+ During cold start, call loadAd as soon as possible, and display the ad immediately after listening to the onAdLoaded callback
+ During hot start, the advertisement can be loaded in advance. When the event of switching the foreground of the device is detected, the isReady() method is called to check whether there is an available advertisement. When there is an available advertisement, the show method is called to display the advertisement.
+ The developer needs to provide an ad container, and some of the three parties return it as a view; after listening to the onAdClosed callback, remove the container

```plain
if(pubSplash.isReady()) {
	pubSplash.showAd(adContainer);
}
```

## **3、Register Ad Event Callback**
+ Note: DoNote: Don't perform the retry loading method ad in `onAdFailed` callback – it'll cause a lot of useless requests and could make your app run slowly.
+ After listening to the onAdClosed callback, remove the container adContainer

```plain
pubSplash.setAdListener(new PubSplashAdListener() {

		@Override // Callback when the first ad source is loaded successfully；A load will only be called back once
		public void onAdLoaded(PubAdInfo pubAdInfo) {}

		@Override // Splash ad clicked
		public void onAdClicked(PubAdInfo pubAdInfo) {}

		@Override // Splash ad appears on the screen
		public void onAdImpression(PubAdInfo pubAdInfo) {}

		@Override // Splash ad failed to load
		public void onAdLoadFailed(PubAdError pubAdInfo) {}
		
		@Override // Splash ad closed
		public void onAdClosed(PubAdInfo pubAdInfo) {
			adContainer.removeAllViews();
		}
});

```


# Interstitials
## **1、Load an ad**
+ Developers can preload ads before displaying them.
+ To request a interstitial ad, you need to declare a PubInterstitial object first, set the listener and load the video creative.

```plain
PubInterstitial mPubInterstitial = new PubInterstitial(activity，"AdUnitID");
mPubInterstitial.setAdListener(new PubInterstitialAdListener());
mPubInterstitial.loadAd();
```

## **2、Show interstitial ad**
+ When the display opportunity arrives, check whether there is an available ad through the isReady() method; or monitor whether the ad is called back onAdLoaded.

```plain
if(mPubInterstitial.isReady()) {
	mPubInterstitial.showAd(activity, null);
}
```

## **3、Register Ad Event Callback**
+ Note: Don't perform the retry loading method ad in `onAdFailed` callback – it'll cause a lot of useless requests and could make your app run slowly.

```plain
mPubInterstitial.setAdListener(new PubInterstitialAdListener() {

		@Override // Callback when the first ad source is loaded successfully；A load will only be called back once
		public void onAdLoaded(PubAdInfo pubAdInfo) {}

		@Override // Interstitial ad clicked
		public void onAdClicked(PubAdInfo pubAdInfo) {}

		@Override // Interstitial ad appears on the screen
		public void onAdImpression(PubAdInfo pubAdInfo) {}

		@Override // Interstitial ad failed to load
		public void onAdFailed(PubAdError error) {}

		@Override // Interstitial ad closed
		public void onAdClosed(PubAdInfo pubAdInfo) {}

		@Override // Interstitial ad played start（Some advertising platforms support）
		public void onAdVideoStart(PubAdInfo pubAdInfo) {}

		@Override // Interstitial ad played completely（Some advertising platforms support）
		public void onAdVideoEnd(PubAdInfo pubAdInfo) {}

		@Override // Interstitial ad is shown failed（Some advertising platforms support）
		public void onAdVideoError(PubAdInfo pubAdInfo, PubAdError error) {}
});

```


# Rewarded Ads
## **1、Load an ad**
+ Developers can preload ads before displaying them.
+ To request a rewarded video ad, you need to declare a PubReward object first, set the listener and load the video creative.

```plain
PubReward mPubReward = new PubReward(activity，"AdUnitID");
mPubReward.setAdListener(new PubRewardAdListener());
mPubReward.loadAd();
```

## **2、Show reward video  ad**
+ When the display opportunity arrives, check whether there is an available ad through the `isReady()` method; or monitor whether the ad is called back onAdLoaded.

```plain
if(mPubReward.isReady()) {
	mPubReward.showAd(activity, null);
}
```

## **3、Register Ad Event Callback**
+ Note: Don't perform the retry loading method ad in `onAdFailed` callback – it'll cause a lot of useless requests and could make your app run slowly.

```plain
mPubReward.setAdListener(new PubRewardAdListener() {
		@Override // Callback when the first ad source is loaded successfully；A load will only be called back once
A load will only be called back once
		public void onAdLoaded(PubAdInfo pubAdInfo) {}

		@Override // Rewarded video ad clicked
		public void onAdClicked(PubAdInfo pubAdInfo) {}
		
		@Override // Rewarded video ad appears on the screen
		public void onAdImpression(PubAdInfo pubAdInfo) {}

		@Override // Rewarded video ad failed to load
		public void onAdFailed(PubAdError error) {}

		@Override // Rewarded video ad closed.
		public void onAdClosed(PubAdInfo pubAdInfo) {}

		@Override // It's time to offer some reward to the user
		public void onAdReward(PubAdInfo pubAdInfo) {}

		@Override // Rewarded video ad played start
		public void onAdVideoStart(PubAdInfo pubAdInfo) {}

		@Override // Rewarded video ad played completely
		public void onAdVideoEnd(PubAdInfo pubAdInfo) {}

		@Override // Rewarded video ad is shown failed（Some advertising platforms support）
		public void onAdVideoError(PubAdInfo pubAdInfo, PubAdError error) {}
});

```

# Android AdMob Custom Adapter
## Supported Ad Formats
+ **Banner**
+ **Native**: Pubeasy only supports configuring self-rendering
+ **Interstitial**
+ **Rewarded Video**

## SDK Versions
+ **AdMob SDK Version**: v23.5.0+
+ **Pubeasy SDK Version**: v14.1.10.1+

## SDK Down
[libs.zip](https://www.yuque.com/attachments/yuque/0/2025/zip/57821747/1753596866859-0650a0e7-257f-42e6-af36-2a9c2677ef85.zip)

## Google Backend Configuration
### Prerequisites
Before configuration, ensure that you have already added the app to the Google AdMob backend and successfully created the ad unit.

### Mediation Page
1. Choose the **Waterfall Sources** tab and click **Manage mappings** in the current app.

![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596119512-a0ef5014-e4c1-426b-9e07-4e833ab18aaa.png)

1. Select the ad unit ID and add the corresponding types for the custom platform.
    - Example: Select **Banner**, click **Add mapping** to add parameters, then save it after completion.
2. **Mapping details**:
    - **Mapping name**: `TPMediation` (You can customize the name)
    - **Class Name**: `com.google.ads.mediation.customevent.AdNetworkCustomEvent` (Cannot be modified)
    - **Parameter**: `{ "appid": "The App ID you created on Pubeasy", "pid": "The ad unit ID you configured on Pubeasy" }` (Parameters from Google servers)
    - ![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596142189-a0b3287e-138d-4759-9730-0c04fe10758e.png)

### Mediation Groups
1. Choose the **Mediation groups** tab and click **Create mediation group**.

![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596169528-f11df987-ebc3-4e81-8883-479c75ecd4c1.png)

2. Select **Ad format** and **Platform**, then click **Continue**.
3. Fill in the **name** (the "Mapping name" you provided earlier).

![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596194989-76e54c63-61fd-4e67-bc5c-fe72783a64bc.png)

4. Click on the **Ad units** tab, then click **Add ad units** and select the ad unit ID you created.

![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596211128-d7bb3275-bd4e-4864-bc83-94030ee6d439.png)



5. In **Waterfall**, click **Add Custom event**, fill in `TPMediation` in the **Label**, set the **cost per thousand impressions (eCPM)**, and click **Continue**.
+ **Testing Suggestion**: During testing, set a **high eCPM** for `TPMediation` and a **low eCPM** for **AdMob Network** to prioritize `TPMediation` ads. After testing, adjust pricing to normal levels.

![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596304520-86acd816-bf55-42ad-a193-1cbc56898fab.png)

## SDK Integration
### Add Dependencies
It is recommended to download the Pubeasy sdk version ，and then add the dependencies for **Pubeasy SDK & TPMediation SDK** to your module's **app-level Gradle** file:

```java

    api("com.google.android.gms:play-services-ads:24.4.0")

    api('com.google.android.gms:play-services-ads-identifier:18.0.1')


    //noinspection GradleCompatible
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.appcompat:appcompat:1.3.0-alpha02'


    // 必须依赖项
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.squareup.okhttp3:okhttp:4.9.0'

    
    api files("libs/pub-ad-14.1.20.1.aar")

//   pubcore
    api files("libs/pub-14.1.20.1.aar")
    api files("libs/pub-core-14.1.20.1.aar")
    api files("libs/pub-sdk-14.1.20.1.aar")

//    pub-crosspromotion
    api files("libs/pub-crosspromotion-27.14.1.20.1.aar")
    api files("libs/pub_common-14.1.20.1.aar")
    api files("libs/pub-om-sdk-1.4.10.aar")

//    pub_exchange
    api files("libs/pub_exchange-40.14.1.20.1.aar")

//    pub_admob
    api files("libs/google_mediation_1.0.0.aar")

```

## Obfuscation Configuration
After enabling obfuscation, add the following rules to the `proguard-rules.pro` file:

```java
-keep public class com.tradplus.** { *; }
-keep class com.tradplus.ads.** { *; }
-keep class com.google.ads.mediation.customevent.** {*;}
```

## Shrink Resources
Advertising SDK resources cannot be obfuscated. If using a third-party resource shrinking framework, exclude the SDK's resources by adding them to the whitelist:

```java
R.string.tp_*
R.drawable.tp_*
R.layout.tp_*
R.id.tp_*
```

## Advertising Initialization
AdMob supports automatically initializing other ad SDKs, so there is no need for separate initialization at the device level.

## Ad Loading and Rendering
For ad requests and display instructions, refer to the official AdMob documentation: [AdMob Banner Ads](https://developers.google.com/admob/android/banner/).

## Testing and Verification
Testing should be conducted using **test mode**

Supported test ad types:

![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596508836-1fa05fbf-dc82-4317-8c47-4326afd86199.png)![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596498116-c2bdcffd-fa3b-4761-bb21-b020a86b7de9.png)![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753596515314-4249cce8-4646-414a-acf9-47b3742ab6ff.png)


# Android Topon Custom Adapter
## Supported Ad Formats
+ **Banner**
+ **Native**: Pubeasy only supports configuring self-rendering
+ **Interstitial**
+ **Rewarded Video**

## SDK Versions
+ **Pubeasy SDK Version**: v14.1.10.1+

## SDK Down


[pub-taku.zip](https://www.yuque.com/attachments/yuque/0/2025/zip/57821747/1754277089190-5d5f5b9e-4317-4d09-9950-6ce3fc711d3c.zip)



## Topon Backend Configuration
### Prerequisites
Before configuration, ensure that you have already added the app to the Topon backend and successfully created the ad unit.

### Mediation Page
#### 1.add  custom platform
Native class name :  
io.pubeasy.ad.topon.adapter.PubTopOnNativeAdapter

RewardVideo class name: 

io.pubeasy.ad.topon.adapter.PubTopOnRewardVideoAdapter

Banner class name :

io.pubeasy.ad.topon.adapter.PubTopOnBannerAdapter

Interstitial class name :

io.pubeasy.ad.topon.adapter.PubTopOnInterstitialAdapter

Splashclass name :

io.pubeasy.ad.topon.adapter.PubTopOnSplashAdapter

![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753597248493-592b8d9e-981b-4233-a378-3b52834ea17d.png)



#### 2.Add app_id   Parameter
# ![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753597625393-bc18de69-b1ab-43bc-bd2f-24bafc6e2761.png)
#### 3.Add ad unit ID
![](https://cdn.nlark.com/yuque/0/2025/png/57821747/1753597710739-bf2e6716-f9bf-4a3f-ac36-dc29bb672f62.png)



## SDK Integration
### Add Dependencies
It is recommended to download the Pubeasy sdk version ，and then add the dependencies for **Pubeaasy SDK** to your module's **app-level Gradle** file:



## Obfuscation Configuration
After enabling obfuscation, add the following rules to the `proguard-rules.pro` file:

```java
-keep public class com.tradplus.** { *; }
-keep class com.tradplus.ads.** { *; }
-keep class cn.pub.ad.**{*;}
```

## Shrink Resources
Advertising SDK resources cannot be obfuscated. If using a third-party resource shrinking framework, exclude the SDK's resources by adding them to the whitelist:

```java
R.string.tp_*
R.drawable.tp_*
R.layout.tp_*
R.id.tp_*
```










































Documentation link:  
https://www.pubeasy.io/doc.html
