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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class TotalQuizzesInfoFragment : Fragment() {
    lateinit var binding: FragmentTotalQuizzesInfoBinding
    private lateinit var adapter:TotalQuizzesAdapter
    private lateinit var QuizArrayList: ArrayList<TotalQuizzesInfo>
    private lateinit var userRecyclerView: RecyclerView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTotalQuizzesInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRecyclerView=binding.totalQuizRView
        binding.totalProgresscircle.visibility = View.VISIBLE
        binding.totalquizToshowlottie.visibility = View.GONE
        QuizArrayList= arrayListOf<TotalQuizzesInfo>()
            FirebaseDatabase.getInstance().getReference("UserProfiles") .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("MyQuiz").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    for(userSnapshot in snapshot.children){
                        val key = userSnapshot.key
                        val name = userSnapshot.child("Details").child("quizName").value
                        val op = TotalQuizzesInfo(name.toString(),key)
                        QuizArrayList.add(op)
                    }
                    binding.totalProgresscircle.visibility = View.GONE
                    userRecyclerView.adapter=TotalQuizzesAdapter(QuizArrayList)
                    userRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                    userRecyclerView.setHasFixedSize(true)
                }
                else{
                    binding.totalProgresscircle.visibility = View.GONE
                    binding.totalquizToshowlottie.visibility = View.VISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(view,"Some Error Occured",Snackbar.LENGTH_SHORT).show()
            }

        })





    }




}