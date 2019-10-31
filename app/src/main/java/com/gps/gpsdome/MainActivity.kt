package com.gps.gpsdome

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xkai.gps.GpsService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mBond: Boolean = false
    private var serverMessenger: Messenger? = null
    private var conn: MyConn? = null

    private val mMessenger = Messenger(object : Handler() {
        override fun handleMessage(msg: Message) {
            Toast.makeText(applicationContext, msg.arg1.toString() + "", Toast.LENGTH_SHORT).show()
            super.handleMessage(msg)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //绑定服务
        val intent = Intent(this, GpsService::class.java)
        conn = MyConn()
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
        bt_hello.setOnClickListener {
            val clientMessage = Message.obtain()
            clientMessage.what = 1
            clientMessage.arg1 = 12345
            try {
                clientMessage.replyTo = mMessenger
                serverMessenger!!.send(clientMessage)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    private inner class MyConn : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            //连接成功
            serverMessenger = Messenger(service)
            Log.i("Main", "服务连接成功")
            mBond = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serverMessenger = null
            mBond = false
        }
    }

    override fun onDestroy() {
        if (mBond) {
            unbindService(conn)
        }
        super.onDestroy()
    }
}