package com.xkai.gps

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.widget.Toast

//服务端service
class GpsService : Service() {
    companion object {
        private const val CODE = 1
        private const val TAG = "GpsService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    //创建一个送信人，封装handler
    private val mMessenger = Messenger(object : Handler() {
        override fun handleMessage(msg: Message) {
            val toClient = Message.obtain()
            when (msg.what) {
                CODE -> {
                    //接收来自客户端的消息，并作处理
                    val arg = msg.arg1
                    Toast.makeText(applicationContext, arg.toString() + "", Toast.LENGTH_SHORT)
                        .show()
                    toClient.arg1 = 1111111111
                    try {
                        //回复客户端消息
                        msg.replyTo.send(toClient)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
            }
            super.handleMessage(msg)
        }
    })

    override fun onBind(intent: Intent): IBinder? {
        return mMessenger.binder
    }
}
