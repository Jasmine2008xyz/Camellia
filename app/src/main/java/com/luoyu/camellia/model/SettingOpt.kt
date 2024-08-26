package com.luoyu.camellia.model

data class SettingOpt(val id: Int, var text: String, var type: Int) {
    companion object{
        const val TYPE_ITEM = 0
        const val TYPE_SWITCH = 1
    }
}
