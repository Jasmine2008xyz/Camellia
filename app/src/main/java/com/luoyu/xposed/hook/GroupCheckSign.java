package com.luoyu.xposed.hook;

import android.widget.Button;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.SearchView;
import android.widget.CheckBox;
import android.widget.ScrollView;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick;
import com.luoyu.xposed.manager.TroopManager;
import java.util.ArrayList;
import android.widget.ImageView;
import java.util.List;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Color;
import com.luoyu.camellia.activities.helper.ActivityAttributes;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.utils.QQUtil;

@Xposed_Item_Controller(itemTag = "群聊打卡")
public class GroupCheckSign {
  private static ArrayList<String> list = new ArrayList<>();
  private static ArrayList<LinearLayout> layoutlist = new ArrayList<>();

  @Xposed_Item_Entry
  public void work() {
    if (!ModuleController.Config.getBooleanData("群聊打卡/开关", false)) return;
    List<String> l = ModuleController.Config.getListData("群聊打卡/目标", new ArrayList());
    if (l.size() == 0) return;
    for (String uin : l) {
      TroopManager.checkSign(uin, QQUtil.getCurrentUin());
    }
  }

  @Xposed_Item_UiClick
  public void show() {
    var act = ActivityAttributes.context;
    var builder = new MaterialAlertDialogBuilder(act);
    builder.setTitle("群聊打卡");
    var root = new LinearLayout(act);
    root.setOrientation(LinearLayout.VERTICAL);
    // 开关
    var layout = new LinearLayout(act);
    layout.setGravity(Gravity.CENTER);
    layout.setOrientation(LinearLayout.HORIZONTAL);
    var tx = new TextView(act);
    tx.setText("开关");
    Switch sw = new Switch(act);
    sw.setChecked(ModuleController.Config.getBooleanData("群聊打卡/开关", false));
    sw.setOnCheckedChangeListener(
        (v, b) -> {
          //   if (b) ToastUtils.Toast("设置成功");
          ModuleController.Config.putData("群聊打卡/开关", b);
        });
    layout.addView(tx);
    layout.addView(sw);
    var itemlayout = new LinearLayout(act);
    itemlayout.setOrientation(0);
    itemlayout.setGravity(Gravity.CENTER);
    var searcher = new SearchView(act);
    var btn = new Button(act);
    var btn2 = new Button(act);
    btn = FixButton(btn);
    btn2 = FixButton(btn2);
    searcher.setQueryHint("搜索");
    searcher.setIconifiedByDefault(false);
    var searcherparam = new LinearLayout.LayoutParams(600, -2);
    searcher.setLayoutParams(searcherparam);
    btn.setText("全选");
    btn.setOnClickListener(
        v -> {
          for (LinearLayout item : layoutlist) {
            ((CheckBox) item.getChildAt(2)).setChecked(true);
          }
        });
    btn2.setText("反选");
    btn2.setOnClickListener(
        v -> {
          for (LinearLayout item : layoutlist) {
            ((CheckBox) item.getChildAt(2))
                .setChecked(!((CheckBox) item.getChildAt(2)).isChecked());
          }
        });
    itemlayout.addView(searcher);
    itemlayout.addView(btn);
    itemlayout.addView(btn2);
    var scroll = new ScrollView(act);
    var l = new LinearLayout(act);
    scroll.addView(l);
    l.setOrientation(1);
    ArrayList<TroopManager.GroupInfo> grouplist = TroopManager.Group_Get_List();
    for (TroopManager.GroupInfo info : grouplist) {
      var item = new LinearLayout(act);
      item.setTag(info.Uin + info.Name + info.Code + info.Creator + info.source + info.adminList);
      var params = new LinearLayout.LayoutParams(-1, 125);
      params.setMargins(25, 16, 25, 16);
      item.setLayoutParams(params);
      var image = new ImageView(act);
      var imageparam = new LinearLayout.LayoutParams(100, 100);
      imageparam.setMarginEnd(5);
      image.setLayoutParams(imageparam);
      var t = new TextView(act);
      var tparam = new LinearLayout.LayoutParams(650, -2);
      t.setLayoutParams(tparam);
      var box = new CheckBox(act);
      List l2 = ModuleController.Config.getListData("群聊打卡/目标", new ArrayList());
      if (l2.contains(info.Uin)) {
        box.setChecked(true);
      }
      item.setOrientation(0);
      Glide.with(act)
          .applyDefaultRequestOptions(RequestOptions.bitmapTransform(new CircleCrop()))
          .load("http://p.qlogo.cn/gh/" + info.Uin + "/" + info.Uin + "/100/")
          .into(image);
      t.setText(info.Name);
      box.setOnCheckedChangeListener(
          (v, b) -> {
            if (!b) {
              list.remove(info.Uin);
            } else {
              if (list.contains(info.Uin)) return;
              list.add(info.Uin);
            }
            ModuleController.Config.putData("群聊打卡/目标", list);
          });
      item.addView(image);
      item.addView(t);
      item.addView(box);
      l.addView(item);
      layoutlist.add(item);
    }
    searcher.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            // 当用户提交搜索时调用
            // 在这里处理搜索逻辑
            return false;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
            // 当搜索框中的文本发生变化时调用
            // 在这里处理搜索框文本变化的逻辑
            try {
              scroll.removeAllViews();
              // l.removeAllViews();
              LinearLayout newitem = new LinearLayout(act);
              newitem.setOrientation(1);
              for (LinearLayout item : layoutlist) {
                if (item.getParent() != null) {
                  ((LinearLayout) item.getParent()).removeView(item);
                }
                //  QLog.log("a",item.getTag().toString());
                if (item.getTag().toString().contains(newText)) {

                  newitem.addView(item);
                }
              }
              scroll.addView(newitem);
            } catch (Exception err) {
              //	QLog.log("abab",Log.getStackTraceString(err));
            }
            return false;
          }
        });
    root.addView(layout);
    root.addView(itemlayout);
    root.addView(scroll);
    builder.setView(root);
    builder.setPositiveButton(
        "退出",
        (dia, which) -> {
          work();
        });
    builder.show();
  }

  private Button FixButton(Button button) {
    var params = new LinearLayout.LayoutParams(160, 100);
    button.setLayoutParams(params);
    GradientDrawable shape = new GradientDrawable();
    shape.setColor(Color.parseColor("#0099FF"));
    shape.setStroke(5, Color.parseColor("#0099FF"));
    shape.setPadding(25, 25, 25, 25);
    shape.setCornerRadius(25);
    shape.setAlpha(150);
    shape.setShape(GradientDrawable.RECTANGLE);
    button.setBackground(shape);
    button.setTextColor(Color.parseColor("#FFFFFF"));
    return button;
  }
}
