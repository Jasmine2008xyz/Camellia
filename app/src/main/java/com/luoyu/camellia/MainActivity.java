package com.luoyu.camellia;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.luoyu.camellia.activities.ModuleIntroductionActivity;
import com.luoyu.camellia.activities.support.BaseActivity;
import com.luoyu.camellia.adapters.MainActivityLayoutAdapter;
import com.luoyu.camellia.databinding.ActivityMainBinding;
import com.luoyu.camellia.utils.IntentUtil;

import java.util.ArrayList;
import com.luoyu.camellia.R;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final ArrayList<MainActivityLayoutAdapter.Item> ItemList = new ArrayList<>();
    private static Context act;

    static {
        ItemList.add(
                new MainActivityLayoutAdapter.Item(
                        "Camellia(" + BuildConfig.VERSION_NAME + ")", () -> {}));
        ItemList.add(
                new MainActivityLayoutAdapter.Item(
                        "模块介绍",
                        () -> {
                            Intent intent = new Intent(act, ModuleIntroductionActivity.class);
                            act.startActivity(intent);
                        }));
        ItemList.add(
                new MainActivityLayoutAdapter.Item(
                        "交流讨论",
                        () -> {
                            CharSequence[] items = {"加入QQ聊天群", "加入QQ通知群"};
                            new MaterialAlertDialogBuilder(act)
                                    .setTitle("模块介绍")
                                    .setItems(
                                            items,
                                            (dia, which) -> {
                                                switch (which) {
                                                    case 0 -> {
                                                        IntentUtil.openQQGroup(act, "902327702");
                                                    }
                                                    case 1 -> {
                                                        IntentUtil.openQQGroup(act, "837012640");
                                                    }
                                                    default -> {}
                                                }
                                            })
                                    .setPositiveButton("取消", null)
                                    .show();
                        }));
        ItemList.add(
                new MainActivityLayoutAdapter.Item(
                        "检测更新",
                        () -> {
                            Toast.makeText(act, "江西第一纯情男高 拒绝为您服务。", Toast.LENGTH_SHORT).show();
                        }));
        ItemList.add(
                new MainActivityLayoutAdapter.Item(
                        "查看作者哔哩哔哩动态",
                        () -> {
                            IntentUtil.openBilibili(act);
                        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        act = this;
        // set content view to binding's root
        setContentView(binding.getRoot());

        // Init recyclerview
        RecyclerView recycler = (RecyclerView) binding.getRoot().getChildAt(0);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new MainActivityLayoutAdapter(ItemList));

        // Set recyclerView's background
        GradientDrawable recyclerViewBackground = new GradientDrawable();
        recyclerViewBackground.setColor(Color.parseColor("#F5F5F5"));
        recyclerViewBackground.setCornerRadius(30);
        recyclerViewBackground.setAlpha(130);

        recycler.setBackground(recyclerViewBackground);

        recycler.setY(-500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
}
