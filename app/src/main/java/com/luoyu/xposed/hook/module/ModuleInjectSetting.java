package com.luoyu.xposed.hook.module;

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick;

@Xposed_Item_Controller(itemTag = "模块注入设置", isApi = true)
public class ModuleInjectSetting {
  @Xposed_Item_UiClick
  public void onClick() {}
}
