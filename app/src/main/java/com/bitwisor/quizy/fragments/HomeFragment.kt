package com.bitwisor.quizy.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.JoinActivity
import com.bitwisor.quizy.LoginActivity
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
                database.reference.child("UserProfiles").child(FirebaseAuth.getInstance().uid!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val users: User? = snapshot.getValue(User::class.java)
                            if (users != null) {
                                Picasso.get()
                                    .load(users.profilepic)
                                    .placeholder(R.drawable.scientist).into(binding.profileImage)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })


            }









            joinQuizBtn.setOnClickListener {
                val i = Intent(requireActivity(),JoinActivity::class.java)
                startActivity(i)
                requireActivity().finish()
            }
        }
    }

}