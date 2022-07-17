package com.bitwisor.quizy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitwisor.quizy.Adapters.TotalQuizzesAdapter
import com.bitwisor.quizy.databinding.FragmentTotalQuizzesInfoBinding
import com.bitwisor.quizy.utils.TotalQuizzesInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class TotalQuizzesInfoFragment : Fragment() {
    lateinit var binding: FragmentTotalQuizzesInfoBinding
    lateinit var database: DatabaseReference
    lateinit var FirebaseDatabase: FirebaseDatabase
    private lateinit var adapter:TotalQuizzesAdapter
    private lateinit var QuizArrayList: ArrayList<TotalQuizzesInfo>
    private lateinit var userRecyclerView: RecyclerView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTotalQuizzesInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance()
        userRecyclerView=binding.totalQuizRView
        userRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)



        QuizArrayList= arrayListOf<TotalQuizzesInfo>()
        getUserData()





    }

    private fun getUserData() {
        database = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("UserProfiles") .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("MyQuiz")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    for(userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(TotalQuizzesInfo::class.java)
                        QuizArrayList.add(user!!)
                    }

                    userRecyclerView.adapter=TotalQuizzesAdapter(QuizArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}