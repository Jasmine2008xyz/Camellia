package com.luoyu.plugin.hook;

import android.util.Log;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import com.luoyu.utils.FileUtil;
import com.luoyu.utils.PathUtil;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick;
import de.robv.android.xposed.XposedBridge;

@Xposed_Item_Controller(itemTag = "Java插件")
public class Plugin_Entry {
  @Xposed_Item_UiClick
  public void onClick() {
    
  }
}
