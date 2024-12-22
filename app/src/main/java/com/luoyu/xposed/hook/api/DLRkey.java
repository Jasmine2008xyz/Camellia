package com.luoyu.xposed.hook.api;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
@Xposed_Item_Controller(itemTag = "劫持RSkey",isApi = true)
public class DLRkey {
  @Xposed_Item_Entry
  public void loadHook(){
    
  }
}
