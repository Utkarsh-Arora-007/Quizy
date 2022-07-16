package com.bitwisor.quizy.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.JoinActivity
import com.bitwisor.quizy.LoginActivity
import com.bitwisor.quizy.MainActivity
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentHomeBinding
import com.bitwisor.quizy.utils.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database:FirebaseDatabase
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
        database = FirebaseDatabase.getInstance()
        binding.apply {



            LeaderBoardView.setOnClickListener {
                if(firebaseAuth.currentUser != null)
                {
                    findNavController().navigate(R.id.action_homeFragment_to_totalQuizzesInfoFragment)
                }
                else
                {
                    Toast.makeText(requireContext(), "Please Login First to View stats of your created quizzes", Toast.LENGTH_SHORT).show()
                }
            }


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


            if(firebaseAuth.currentUser!=null)
            {
                Picasso.get()
                    .load(firebaseAuth!!.currentUser!!.photoUrl)
                    .placeholder(R.drawable.ic_baseline_account_circle_24).into(binding.profileImage)
                binding.profileImage.setOnClickListener {
                    showDialog("Are you sure want to logout?")
                }
            }








            joinQuizBtn.setOnClickListener {
                val i = Intent(requireActivity(),JoinActivity::class.java)
                startActivity(i)
                requireActivity().finish()
            }
        }
    }
    private fun showDialog(title: String) {
        val dialog = Dialog(requireActivity())
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        val body = dialog.findViewById(R.id.body) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.yesbtn) as TextView
        val noBtn = dialog.findViewById(R.id.nobtn) as TextView
        yesBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            var  i = Intent(requireActivity(),MainActivity::class.java)
            startActivity(i)
            requireActivity().finishAffinity()
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

}