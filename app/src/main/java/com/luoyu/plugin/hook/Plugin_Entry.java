package com.luoyu.plugin.hook;

import android.content.Intent;
import android.util.Log;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import com.luoyu.camellia.activities.helper.ActivityAttributes;
import com.luoyu.plugin.ui.activities.PluginActivity;
import com.luoyu.utils.FileUtil;
import com.luoyu.utils.PathUtil;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick;
import com.luoyu.xposed.logging.LogCat;
import de.robv.android.xposed.XposedBridge;

@Xposed_Item_Controller(itemTag = "Java插件")
public class Plugin_Entry {
  @Xposed_Item_UiClick
  public void onClick() {
  /*  try{
      Interpreter interpreter = new Interpreter();
      NameSpace nameSpace = interpreter.getNameSpace();
      interpreter.eval(FileUtil.readFileString(PathUtil.getApkDataPath()+"plugin/小明.java"),nameSpace);
    }catch(EvalError e){
      LogCat.e("运行JAVA插件异常",Log.getStackTraceString(e));
    }*/
    ActivityAttributes.context.finish();
    HookEnv.getActivity().startActivity(new Intent(HookEnv.getContext(),PluginActivity.class));
  }
}
