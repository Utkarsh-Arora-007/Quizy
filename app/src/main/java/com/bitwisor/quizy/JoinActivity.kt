package com.bitwisor.quizy

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

    }


}