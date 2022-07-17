package com.bitwisor.quizy.fragments.joinFragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentJoinHomeBinding
import com.google.android.material.snackbar.Snackbar
import java.net.InetAddress

class JoinHomeFragment : Fragment() {
    lateinit var binding: FragmentJoinHomeBinding
    var quizid = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findQuizBtn.setOnClickListener {
            quizid = binding.quizRoomCodeEditText.text.toString()
            var bundle= Bundle()
            bundle.putString("QuizId",quizid)
            if (!quizid.isNullOrEmpty() and (quizid.length == 4)){
                findNavController().navigate(R.id.action_joinHomeFragment_to_quizDetailsFragment,bundle)
            }
            else{
                Snackbar.make(view,"Please enter a valid code",Snackbar.LENGTH_SHORT).show()
            }
        }

    }
    private fun isNetworkConnected(): Boolean {
        val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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



}