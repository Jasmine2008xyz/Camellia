package com.luoyu.camellia.adapters

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.materialswitch.MaterialSwitch
import com.luoyu.camellia.R

sealed class SettingViewHolder(view: View): RecyclerView.ViewHolder(view)

class SettingItemViewHolder(view: View): SettingViewHolder(view) {
    val cardView: CardView = view.findViewById(R.id.setting_recyclerView_item)
    val settingItemName: TextView = view.findViewById(R.id.setting_item_name)
}

class SettingSwitchViewHolder(view: View): SettingViewHolder(view) {
    val cardView: CardView = view.findViewById(R.id.setting_recyclerView_switch_item)
    val settingSwitchName: TextView = view.findViewById(R.id.setting_switch_name)
    val switch: MaterialSwitch = view.findViewById(R.id.setting_switch)
}
