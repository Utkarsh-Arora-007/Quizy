package com.bitwisor.quizy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitwisor.quizy.Adapters.DetailsPerQuizAdapter
import com.bitwisor.quizy.Adapters.TotalQuizzesAdapter
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentDetailsPerQuizBinding
import com.bitwisor.quizy.databinding.FragmentTotalQuizzesInfoBinding
import com.bitwisor.quizy.utils.DetailsPerQuiz
import com.bitwisor.quizy.utils.TotalQuizzesInfo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailsPerQuizFragment : Fragment() {
    lateinit var binding: FragmentDetailsPerQuizBinding
    private lateinit var adapter: DetailsPerQuizAdapter
    private lateinit var detailsArrayList: ArrayList<DetailsPerQuiz>
    private lateinit var userRecyclerView: RecyclerView
    var quizId=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsPerQuizBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRecyclerView=binding.mrecview
        binding.leaderProgresscircle.visibility = View.VISIBLE
        detailsArrayList= arrayListOf<DetailsPerQuiz>()
        var bundle = arguments
        if (bundle!=null){
            quizId = bundle.getString("QuizId","0000").toString()
        }
        FirebaseDatabase.getInstance().getReference("LeaderBoard")
            .child(quizId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        for(userSnapshot in snapshot.children){
                            val pname = "${userSnapshot.child("DisplayName").value}"
                            val pscore= "${userSnapshot.child("Score").value}"
                            val op = DetailsPerQuiz(pname,pscore)
                            detailsArrayList.add(op)
                        }

                        binding.leaderProgresscircle.visibility = View.GONE
                        userRecyclerView.adapter=DetailsPerQuizAdapter(detailsArrayList)
                        userRecyclerView.layoutManager= LinearLayoutManager(requireContext())
                        userRecyclerView.setHasFixedSize(true)
                    }
                    else{
                        binding.leaderProgresscircle.visibility = View.GONE

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Snackbar.make(view,"Some Error Occured", Snackbar.LENGTH_SHORT).show()
                }

            })
    }


}