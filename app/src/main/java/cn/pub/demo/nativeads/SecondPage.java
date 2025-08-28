package cn.pub.demo.nativeads;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.pub.R;
import cn.pub.demo.utils.TestAdUnitId;


public class SecondPage extends AppCompatActivity {

    private ViewGroup adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        adContainer = findViewById(R.id.ad_container);
        Bundle extras = getIntent().getExtras();
        if (TestAdUnitId.TYPE_NATIVE.equals(extras.get("type"))) {
            loadNative();
        }
    }

    private void loadNative() {
        NativeUtils nativerUtils = NativeUtils.getInstance();
        if (nativerUtils.isReady()) {
            nativerUtils.showNative(adContainer);
        } else {
            Toast.makeText(SecondPage.this, "无可用广告 or 已经展示", Toast.LENGTH_SHORT).show();
        }
    }
}