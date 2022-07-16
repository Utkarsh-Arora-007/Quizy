package com.bitwisor.quizy

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.net.InetAddress

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if(!(isNetworkConnected() || isInternetAvailable())){
            val i = Intent(this,NoInternetActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }
    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
    fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }

    override fun onPause() {
        super.onPause()
        if(!(isNetworkConnected() || isInternetAvailable())){
            val i = Intent(this,NoInternetActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    override fun onStart() {
        super.onStart()
        if(!(isNetworkConnected() || isInternetAvailable())){
            val i = Intent(this,NoInternetActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if(!(isNetworkConnected() || isInternetAvailable())){
            val i = Intent(this,NoInternetActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!(isNetworkConnected() || isInternetAvailable())){
            val i = Intent(this,NoInternetActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    override fun onStop() {
        super.onStop()
        if(!(isNetworkConnected() || isInternetAvailable())){
            val i = Intent(this,NoInternetActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }
}