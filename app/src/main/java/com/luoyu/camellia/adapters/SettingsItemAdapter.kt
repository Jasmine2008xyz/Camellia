package com.luoyu.camellia.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.luoyu.camellia.R
import com.luoyu.camellia.utils.showToast

class SettingsItemAdapter(val context: Context, val settingItemList: List<String>): RecyclerView.Adapter<SettingsItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.setting_recyclerView_item)
        val settingItemName: TextView = view.findViewById(R.id.setting_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.setting_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.cardView.setOnClickListener {
            "You click RecyclerView Item".showToast()
        }
        return ViewHolder(view)
    }

    override fun getItemCount() = settingItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.settingItemName.text= settingItemList[position]
    }

}