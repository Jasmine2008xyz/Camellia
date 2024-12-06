package com.luoyu.xposed.hook.voicepanel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.luoyu.utils.PathUtil;
import com.luoyu.xposed.data.table.ContactTable;
import com.luoyu.xposed.data.table.SessionTable;
import com.luoyu.xposed.message.MsgElementCreator;
import de.robv.android.xposed.XposedBridge;
import java.util.ArrayList;
import java.util.List;
import com.luoyu.camellia.R;
import com.luoyu.xposed.base.QQApi;
import com.luoyu.xposed.message.MsgUtil;

public class VoicePanel_Adapter
    extends RecyclerView.Adapter<VoicePanel_Adapter.VoicePanel_ViewHolder> {
  private List<VoiceItem> itemList;

  public VoicePanel_Adapter(List<VoiceItem> itemList) {
    this.itemList = itemList;
  }

  @NonNull
  @Override
  public VoicePanel_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.voice_panel_item, parent, false);
    return new VoicePanel_ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull VoicePanel_ViewHolder holder, int position) {
    holder.textView.setText(itemList.get(position).getName());
  }

  @Override
  public int getItemCount() {
    return itemList.size();
  }

  static class VoicePanel_ViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    VoicePanel_ViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.voice_panel_item_textView);
      ImageView imageView = itemView.findViewById(R.id.voice_panel_item_imageView);
      imageView.setOnClickListener(
          v -> {
            ArrayList<Object> list = new ArrayList<>();
            try {
              list.add(
                  MsgElementCreator.createPttElement(
                      PathUtil.getApkDataPath() + "voice/" + textView.getText()));
              MsgUtil.sendMsg(
                  QQApi.createContact(ContactTable.chatType, ContactTable.peerUid), list);
            } catch (Exception err) {
XposedBridge.log("发送语音失败："+Log.getStackTraceString(err));
            }
          });
    }
  }

  public static class VoiceItem {
    private String name;

    public VoiceItem(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
