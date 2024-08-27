package com.luoyu.camellia.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.luoyu.camellia.activities.support.BaseActivity;
@Deprecated
public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.luoyu.camellia.R.layout.setting_activity);
    }
}
