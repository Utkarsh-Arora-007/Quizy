package com.bitwisor.quizy

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bitwisor.quizy.databinding.ActivityJoinBinding
import java.net.InetAddress

class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment
        val navController = navHostFragment.navController

        var uri: Uri? = intent.data
        if(uri!=null){
            var params:List<String> = uri.pathSegments
            var id:String = params.get(params.size-1)
            Toast.makeText(this,"id = $id",Toast.LENGTH_SHORT).show()
            var bundle= Bundle()
            bundle.putString("QuizId",id)
            navController.navigate(R.id.action_joinHomeFragment_to_quizDetailsFragment,bundle)
        }

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