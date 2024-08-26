package com.luoyu.camellia.adapters;


import android.graphics.Typeface;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.graphics.Color;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.luoyu.camellia.R;
import com.luoyu.camellia.interfaces.IActivityPerform;
import java.util.List;

public class MainActivityLayoutAdapter
        extends RecyclerView.Adapter<MainActivityLayoutAdapter.MainViewHolder> {
    /*
     * 模块应用主界面布局适配器
     */
    private List<Item> mData;

    public MainActivityLayoutAdapter(List<Item> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout itemView =
                (LinearLayout)
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.activity_main_adapter_view, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String item = mData.get(position).name;
        holder.bind(position, item, mData.get(position).behaviour);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MainViewHolder(LinearLayout itemView) {
            super(itemView);

            textView = (TextView) itemView.getChildAt(0);
        }

        public void bind(int i, String item, IActivityPerform b) {
            textView.setText(item);
            ((View) textView.getParent())
                    .setOnClickListener(
                            v -> {
                                b.exec();
                            });
            if (i == 0) {

                textView.setTypeface(null, Typeface.BOLD);
                textView.setText(
                        Html.fromHtml("<font color=\"#009EFD\">" + textView.getText() + "</font>"));

                LinearLayout root = (LinearLayout) textView.getParent();

                var blackline = new View(textView.getContext());
                blackline.setBackgroundColor(Color.parseColor("#1A1A1A"));

                var blackline_params = new LinearLayout.LayoutParams(-1, 1);
                blackline_params.setMargins(15, 0, 15, 0);
                blackline.setLayoutParams(blackline_params);

                root.addView(blackline);
            } else if (i != 4) {
                LinearLayout root = (LinearLayout) textView.getParent();

                var blackline = new View(textView.getContext());
                blackline.setBackgroundColor(Color.parseColor("#1A1A1A"));

                var blackline_params = new LinearLayout.LayoutParams(-1, 1);
                blackline_params.setMargins(75, 0, 75, 0);
                blackline.setLayoutParams(blackline_params);

                root.addView(blackline);
            }
        }
    }

    public static class Item {
        public String name;
        public IActivityPerform behaviour;

        public Item(String name, IActivityPerform behaviour) {
            this.name = name;
            this.behaviour = behaviour;
        }
    }
}

