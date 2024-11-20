package com.luoyu.xposed.hook.voicepanel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.luoyu.camellia.R;
import com.luoyu.utils.PathUtil;
import com.luoyu.utils.ScreenUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main_VoicePanel extends Dialog {
  /*
   * 面向过程🤓
   */
  public Main_VoicePanel(Context ctx) {
    super(ctx);
    show();
    Window window = getWindow();
    getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    getWindow().setGravity(Gravity.BOTTOM);

    GradientDrawable shape = new GradientDrawable();
    shape.setColor(Color.parseColor("#FFFFFF"/*"#00000000"*/));
    shape.setStroke(1, Color.parseColor("#000000"));
    shape.setPadding(15, 15, 15, 15);
    shape.setCornerRadius(30);
    //    shape.setAlpha(15);
    
    shape.setShape(GradientDrawable.RECTANGLE);

    final LayoutInflater inflater = LayoutInflater.from(ctx);
    final LinearLayout root = (LinearLayout) inflater.inflate(R.layout.voice_panel, null);
    final Toolbar toolBar = root.findViewById(R.id.voice_panel_toolbar);
    final TextView textView = new TextView(ctx);
    final View blackline = new View(ctx);

    LinearLayout.LayoutParams toolBar_params =
        new LinearLayout.LayoutParams(
            ScreenUtil.getScreenWidth(window.getWindowManager()) * 9 / 10, -2);
    toolBar.setLayoutParams(toolBar_params);

    toolBar.getMenu().add("退出");
    toolBar.setOnMenuItemClickListener(
        item -> {
          super.dismiss();
          return true;
        });

    textView.setText("语音面板");
    textView.setTextSize(20);
    textView.setTextColor(Color.parseColor("#1A1A1A"));

    toolBar.addView(textView);

    blackline.setBackgroundColor(Color.parseColor("#1A1A1A"));
    var blackline_param = new LinearLayout.LayoutParams(-1, 1);
    blackline_param.setMargins(20, 5, 20, 25);
    blackline.setLayoutParams(blackline_param);

    window.setLayout(
        (int) (ScreenUtil.getScreenWidth(window.getWindowManager()) * 0.95),
        (int) (ScreenUtil.getScreenHeight(window.getWindowManager()) * 0.7));

    root.addView(blackline, 1);
    RecyclerView recyclerView = new RecyclerView(ctx);
    // root.findViewById(R.id.voice_panel_recycleView);
    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
    // 创建数据
    List<VoicePanel_Adapter.VoiceItem> itemList = new ArrayList<>();
    File file = new File(PathUtil.getApkDataPath() + "voice");
    if (!file.exists()) file.mkdir();

    for (File f : file.listFiles()) {
      if (f.isFile()) itemList.add(new VoicePanel_Adapter.VoiceItem(f.getName()));
    }

    VoicePanel_Adapter adapter = new VoicePanel_Adapter(itemList);
    recyclerView.setAdapter(adapter);

    RecyclerView.LayoutParams recyclerViewParams = new RecyclerView.LayoutParams(-1, -2);

    recyclerView.setLayoutParams(recyclerViewParams);

    root.addView(recyclerView);
    root.setBackground(shape);
    super.setContentView(root);
  }

  public static Main_VoicePanel create(Context context) {
    return new Main_VoicePanel(context);
  }
}
