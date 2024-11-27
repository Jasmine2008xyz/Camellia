package com.luoyu.plugin.ui.adapters;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PluginAdapter extends RecyclerView.Adapter<PluginAdapter.PluginViewHolder> {
  private List<PluginItem> dataList;
  public PluginAdapter(List<PluginItem> data) {
    this.dataList = data;
  }

  public static class PluginItem {
    public String name;

    public PluginItem(String name) {
      this.name = name;
    }
  }

  public static class PluginViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;

    public PluginViewHolder(LinearLayout itemView) {
      super(itemView);

      textView = (TextView) itemView.getChildAt(0);
    }

    public void bind(int i, String item) {}
  }

  @Override
  public PluginViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
    return null;
  }

  @Override
  public void onBindViewHolder(PluginViewHolder arg0, int arg1) {}

  @Override
  public int getItemCount() {
    return dataList.size();
  }
}
