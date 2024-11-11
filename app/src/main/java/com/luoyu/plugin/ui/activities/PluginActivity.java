package com.luoyu.plugin.ui.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.luoyu.camellia.activities.support.BaseActivity;

public class PluginActivity extends BaseActivity {
  @Override
  protected void onCreate(@Nullable Bundle bundle) {
    super.onCreate(bundle);
    requestHideNavigationBar();
    
  }
}
