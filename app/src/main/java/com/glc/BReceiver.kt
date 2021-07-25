package com.glc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "更新时间：" + intent.extras!!.getString("new"), Toast.LENGTH_SHORT)
            .show()
    }
}