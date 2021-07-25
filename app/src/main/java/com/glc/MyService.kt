package com.glc

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class MyService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val zhuangtai = intent.getStringExtra("zhuangtai")

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1){
            val channel = NotificationChannel("normal","Normal",NotificationManager
                .IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        if (zhuangtai == "多云" || zhuangtai == "阴") {
            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle(intent.getStringExtra("wendu") + "   " + intent.getStringExtra("zhuangtai"))
                .setContentText(intent.getStringExtra("cityname"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.tubiao)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.yin
                    )
                )
                .setContentIntent(pi)
                .build()
            startForeground(1, notification)
        } else if (zhuangtai == "晴") {
            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle(intent.getStringExtra("wendu") + "   " + intent.getStringExtra("zhuangtai"))
                .setContentText(intent.getStringExtra("cityname"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.tubiao)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.qinglang
                    )
                )
                .setContentIntent(pi)
                .build()
            startForeground(1, notification)
        } else if (zhuangtai == "雨夹雪" || zhuangtai == "小雪") {
            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle(intent.getStringExtra("wendu") + "   " + intent.getStringExtra("zhuangtai"))
                .setContentText(intent.getStringExtra("cityname"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.tubiao)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.xiaoxue
                    )
                )
                .setContentIntent(pi)
                .build()
            startForeground(1, notification)
        } else if (zhuangtai == "小雨") {
            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle(intent.getStringExtra("wendu") + "   " + intent.getStringExtra("zhuangtai"))
                .setContentText(intent.getStringExtra("cityname"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.tubiao)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.xiaoyu
                    )
                )
                .setContentIntent(pi)
                .build()
            startForeground(1, notification)
        } else if (zhuangtai == "中雨" || zhuangtai == "大雨") {
            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle(intent.getStringExtra("wendu") + "   " + intent.getStringExtra("zhuangtai"))
                .setContentText(intent.getStringExtra("cityname"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.tubiao)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.dayu
                    )
                )
                .setContentIntent(pi)
                .build()
            startForeground(1, notification)
        } else if (zhuangtai == "雷阵雨") {
            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle(intent.getStringExtra("wendu") + "   " + intent.getStringExtra("zhuangtai"))
                .setContentText(intent.getStringExtra("cityname"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.tubiao)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.leizhenyu
                    )
                )
                .setContentIntent(pi)
                .build()
            startForeground(1, notification)
        } else if (zhuangtai == "雾") {
            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle(intent.getStringExtra("wendu") + "   " + intent.getStringExtra("zhuangtai"))
                .setContentText(intent.getStringExtra("cityname"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.tubiao)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.wu
                    )
                )
                .setContentIntent(pi)
                .build()
            startForeground(1, notification)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf();
    }
}