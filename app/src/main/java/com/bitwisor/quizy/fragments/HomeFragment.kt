package com.bitwisor.quizy.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.JoinActivity
import com.bitwisor.quizy.LoginActivity
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.apply {
            createQuizBtn.setOnClickListener {
                if (firebaseAuth.currentUser != null){
                    findNavController().navigate(R.id.action_homeFragment_to_createFragment)
                }
                else{
                    val i = Intent(requireActivity(),LoginActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                }
            }
            joinQuizBtn.setOnClickListener {
                val i = Intent(requireActivity(),JoinActivity::class.java)
                startActivity(i)
                requireActivity().finish()
            }
        }
    }

}