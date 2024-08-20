package com.luoyu.camellia.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.luoyu.camellia.base.HookEnv;
import com.luoyu.camellia.ui.base.EasyButton;
import com.luoyu.camellia.utils.ScreenUtil;



public class RDialog extends Dialog {
    private Context context;
    private RDialog dialog;
    private Window window;

    public RDialog(Context act) {
        // super必须在第一行
        super(act);

        // 填补一些变量，方便调用
        this.context = act;
        this.dialog = this;

        // 获取window
        show();
        hide();
        this.window = getWindow();

        // 先用Window设置好对话框大小和属性
        window.setGravity(Gravity.CENTER);

        // 设置为无色，我们用xml的根布局来写背景
        window.setBackgroundDrawableResource(android.R.color.transparent /*透明*/);

        // void Window.setLayout(int,int);

        window.setLayout(
                ScreenUtil.getScreenWidth(window.getWindowManager()) * 4 / 5,
                ScreenUtil.getScreenHeight(window.getWindowManager()) / 2);

        // 加载xml资源
        LayoutInflater inflater = LayoutInflater.from(HookEnv.getActivity());
        RelativeLayout root =
                (RelativeLayout) inflater.inflate(com.luoyu.camellia.R.layout.rdialog, null);

        // 造个花里胡哨的背景
        GradientDrawable background = new GradientDrawable();
       // background.setPadding(16, 16, 16, 16);
        background.setStroke(1, Color.parseColor("#1A1A1A"));
        background.setCornerRadius(30);
        background.setColor(Color.parseColor("#dfdfdf"));

        root.setBackground(background);

        Button btn1 = (Button) root.findViewById(com.luoyu.camellia.R.id.RDialog_Button);
        btn1=EasyButton.create(btn1).getButton();
        btn1.setOnClickListener(
                v -> {
                    super.cancel();
                });
        super.setContentView(root);
    }
 /*   @Override
    public void setTitle(String title){
        
    }*/
}

