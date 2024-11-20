package com.luoyu.plugin.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.luoyu.camellia.R;
import com.luoyu.camellia.activities.support.BaseActivity;
import com.luoyu.camellia.activities.SettingsActivity;

public class PluginActivity extends BaseActivity {
  @Override
  protected void onCreate(@Nullable Bundle bundle) {
    super.onCreate(bundle);
    setTheme(R.style.AppTheme);
    setContentView(R.layout.plugin_activity);
    requestHideNavigationBar();
    
  }
  
  /*
   * 嘿嘿嘿
   */
  @Override
  public void onBackPressed(){
    super.onBackPressed();
    startActivity(new Intent(PluginActivity.this, SettingsActivity.class));
  }
}
